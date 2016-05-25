package com.itechart.security.business.dao;

import com.itechart.security.business.model.persistent.Telephone;

public interface TelephoneDao {
    Long save(Telephone telephone);

    void update(Telephone telephone);

    void delete(Long id);
}
