package com.itechart.security.business.dao;

import com.itechart.security.business.model.persistent.MessengerAccount;

public interface MessengerAccountDao {
    Long save(MessengerAccount account);

    void update(MessengerAccount account);

    void delete(Long id);
}
