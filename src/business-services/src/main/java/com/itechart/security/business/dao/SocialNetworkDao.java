package com.itechart.security.business.dao;

import com.itechart.security.business.model.persistent.SocialNetwork;

import java.util.List;


public interface SocialNetworkDao {
    List<SocialNetwork> loadAll();
}
