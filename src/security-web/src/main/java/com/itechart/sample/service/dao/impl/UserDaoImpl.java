package com.itechart.sample.service.dao.impl;

import com.itechart.sample.model.persistent.security.User;
import com.itechart.sample.service.dao.UserDao;
import org.springframework.stereotype.Repository;

/**
 * @author andrei.samarou
 */
@Repository
public class UserDaoImpl extends BaseHibernateDao<User> implements UserDao {

    @Override
    public User findUser(String userName) {
        return findObject("from User u where u.userName = ?", userName);
    }

}





