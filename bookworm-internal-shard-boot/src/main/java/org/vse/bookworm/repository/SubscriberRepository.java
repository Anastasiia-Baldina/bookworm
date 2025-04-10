package org.vse.bookworm.repository;

import org.vse.bookworm.dao.Subscriber;

import java.util.Collection;

public interface SubscriberRepository {

    void save(Subscriber subscriber);

    boolean delete(Subscriber subscriber);

    boolean delete(long userId, String chatName);

    Collection<Subscriber> findByUserId(long userId);

    Collection<Subscriber> findByChatId(long chatId);
}
