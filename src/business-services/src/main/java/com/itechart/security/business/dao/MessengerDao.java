package com.itechart.security.business.dao;

import com.itechart.security.business.model.persistent.Messenger;

import java.util.List;

public interface MessengerDao {
    List<Messenger> loadAll();
}
