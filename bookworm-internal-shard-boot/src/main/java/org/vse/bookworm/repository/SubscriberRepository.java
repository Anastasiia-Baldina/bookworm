package org.vse.bookworm.repository;

import org.vse.bookworm.dao.Subscriber;

import java.util.Collection;

public interface SubscriberRepository {

    void create(Subscriber subscriber);

    boolean delete(Subscriber subscriber);

    Collection<Subscriber> findByUserId(long userId);

    Collection<Subscriber> findByChatId(long chatId);
}
