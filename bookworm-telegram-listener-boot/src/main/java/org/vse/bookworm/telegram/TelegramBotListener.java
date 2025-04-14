package org.vse.bookworm.telegram;

import com.google.gson.Gson;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Document;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vse.bookworm.dto.kafka.FileMessageDto;
import org.vse.bookworm.kafka.KafkaDataSender;
import org.vse.bookworm.properties.TelegramListenerProperties;
import org.vse.bookworm.telegram.utils.TgUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class TelegramBotListener implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(TelegramBotListener.class);
    private final Gson GSON = new Gson();
    private final TelegramBot bot;
    private final AtomicBoolean started = new AtomicBoolean(true);
    private final ExecutorService scheduler;
    private final TelegramListenerProperties cfg;
    private final KafkaDataSender msgSender;

    public TelegramBotListener(TelegramBot bot,
                               TelegramListenerProperties cfg,
                               KafkaDataSender msgSender) {
        this.bot = bot;
        this.cfg = cfg;
        this.scheduler = schedule();
        this.msgSender = msgSender;
    }

    private ExecutorService schedule() {
        var scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleWithFixedDelay(
                this::pollLoop,
                cfg.getDelayOnErrorMillis(),
                cfg.getDelayOnErrorMillis(),
                TimeUnit.MILLISECONDS);

        return scheduler;
    }

    private void pollLoop() {
        int updateId = 0;
        try {
            while (started.get() && !Thread.currentThread().isInterrupted()) {
                var rq = new GetUpdates()
                        .limit(cfg.getMaxPollSize())
                        .offset(updateId)
                        .timeout(cfg.getMaxPollTimeSecs());
                GetUpdatesResponse rsp = bot.execute(rq);
                List<Update> updates = rsp.updates();
                if (rsp.isOk() && updates != null && !updates.isEmpty()) {
                    process(updates);
                    updateId = updates.getLast().updateId() + 1;
                }
            }
        } catch (Throwable th) {
            log.error("Fetch updates error.", th);
        }
    }

    private void process(List<Update> updates) {
        List<WrappedFuture<Update, RecordMetadata>> ftrList = new ArrayList<>(updates.size());
        updates.forEach(u -> {
            var ftr = processUpdate(u);
            if(ftr == null) {
                log.info("Inapplicable message with updateId={}", u.updateId());
            } else {
                ftrList.add(ftr);
            }
        });
        ftrList.forEach(ftr -> {
            try {
                Update upd = ftr.get();
                RecordMetadata meta = ftr.unwrapResult();
                if(meta != null) {
                    log.info("Kafka commited: updateId={},topic={},p={},ofs={}",
                            upd.updateId(), meta.topic(), meta.partition(), meta.offset());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Kafka error: updateId={}", ftr.payload().updateId(), e);
            } catch (ExecutionException e) {
                log.error("Kafka error: updateId={}", ftr.payload().updateId(), e);
            }
        });
    }

    @Nullable
    private WrappedFuture<Update, RecordMetadata> processUpdate(Update upd) {
        log.info(GSON.toJson(upd));
        try {
            Message updMsg = upd.message();
            Message edMsg = upd.editedMessage();
            if(updMsg != null) {
                if (updMsg.text() != null) {
                    return new WrappedFuture<>(upd, msgSender.send(TgUtils.textMessage(upd)));
                } else if (updMsg.document() != null) {
                    Document doc = updMsg.document();
                    if (doc.fileName() != null) {
                        GetFile rqFile = new GetFile(doc.fileId());
                        GetFileResponse rsFile = bot.execute(rqFile);
                        FileMessageDto fileMsg = TgUtils.fileMessage(upd, rsFile.file());
                        return new WrappedFuture<>(upd, msgSender.send(fileMsg));
                    }
                } else if(updMsg.newChatMembers() != null) {
                    for(var newUser : updMsg.newChatMembers()) {
                        if(newUser.isBot() && Objects.equals(newUser.username(), cfg.getSelfUsername())) {
                            return new WrappedFuture<>(upd, msgSender.send(TgUtils.joinMessage(upd)));
                        }
                    }
                }
            } else if(edMsg != null && edMsg.text() != null) {
                return new WrappedFuture<>(upd, msgSender.send(TgUtils.editedMessage(upd)));
            }
        } catch (Exception e) {
            log.error("Failed process message, updateId={}", upd.updateId(), e);
        }
        return null;
    }

    @Override
    public void close() throws InterruptedException {
        if (started.compareAndSet(false, true)) {
            scheduler.shutdown();
            if (scheduler.awaitTermination(10_000, TimeUnit.SECONDS)) {
                scheduler.shutdown();
            }
        }
    }
}
