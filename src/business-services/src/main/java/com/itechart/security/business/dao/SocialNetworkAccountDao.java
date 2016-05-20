package com.itechart.security.business.dao;

import com.itechart.security.business.model.persistent.SocialNetworkAccount;

public interface SocialNetworkAccountDao {
    Long save(SocialNetworkAccount account);

    void update(SocialNetworkAccount account);

    void delete(Long id);
}
