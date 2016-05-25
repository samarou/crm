package com.itechart.security.business.dao;

import com.itechart.security.business.model.persistent.Workplace;

public interface WorkplaceDao {
    Long save(Workplace telephone);

    void update(Workplace telephone);

    void delete(Long id);
}
