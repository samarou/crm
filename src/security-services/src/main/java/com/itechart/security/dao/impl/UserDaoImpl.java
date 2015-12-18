package com.itechart.security.dao.impl;

import com.itechart.security.model.persistent.User;
import com.itechart.security.dao.UserDao;
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





