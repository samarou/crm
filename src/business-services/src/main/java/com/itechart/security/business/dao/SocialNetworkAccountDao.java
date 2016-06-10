package com.itechart.security.business.dao;

import com.itechart.common.dao.BaseDao;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.security.business.model.persistent.SocialNetworkAccount;

public interface SocialNetworkAccountDao extends BaseDao<SocialNetworkAccount, Long, PagingFilter> {
}
