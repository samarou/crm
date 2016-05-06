package com.itechart.security.business.dao;

import com.itechart.security.business.model.persistent.Email;

public interface EmailDao {
    Long save(Email email);
    void update(Email email);
    void delete(Long id);
}
