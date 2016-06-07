package com.itechart.security.business.dao.impl;

import com.itechart.common.dao.impl.StaticDataDaoImpl;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.security.business.dao.MessengerDao;
import com.itechart.security.business.model.persistent.Messenger;
import org.springframework.stereotype.Repository;

@Repository
public class MessengerDaoImpl extends StaticDataDaoImpl<Messenger, Long, PagingFilter> implements MessengerDao{
}
