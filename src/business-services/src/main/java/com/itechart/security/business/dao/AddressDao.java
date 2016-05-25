package com.itechart.security.business.dao;

import com.itechart.security.business.model.persistent.Address;

public interface AddressDao {
    Long save(Address address);

    void update(Address address);

    void delete(Long id);
}
