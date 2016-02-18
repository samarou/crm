package com.itechart.security.dao.impl;

import com.itechart.security.dao.UserDao;
import com.itechart.security.model.filter.UserFilter;
import com.itechart.security.model.persistent.User;
import org.hibernate.*;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.loader.plan.exec.process.spi.ScrollableResultSetProcessor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author andrei.samarou
 */
@Repository
public class UserDaoImpl extends BaseHibernateDao<User> implements UserDao {

    @Override
    public User findByName(String userName) {
        return findObject("from User u where u.userName = ?", userName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findUsers(UserFilter filter) {
        return getHibernateTemplate().execute(session -> {
            Criteria criteria = session.createCriteria(User.class, "u");
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            if (filter.getRoleId() != null) {
                criteria.createAlias("u.roles", "r");
                criteria.add(Restrictions.eq("r.id", filter.getRoleId()));
            }
            if (filter.getGroupId() != null) {
                criteria.createAlias("u.groups", "g");
                criteria.add(Restrictions.eq("g.id", filter.getGroupId()));
            }
            if (filter.isActive()) {
                criteria.add(Restrictions.eq("u.active", true));
            }
            if (StringUtils.hasText(filter.getText())) {
                criteria.add(Restrictions.disjunction(
                        Restrictions.ilike("u.userName", filter.getText(), MatchMode.ANYWHERE),
                        Restrictions.ilike("u.firstName", filter.getText(), MatchMode.ANYWHERE),
                        Restrictions.ilike("u.lastName", filter.getText(), MatchMode.ANYWHERE),
                        Restrictions.ilike("u.email", filter.getText(), MatchMode.ANYWHERE)
                ));
            }
            appendSortableFilterConditions(criteria, filter);
            List list = criteria.list();

            return list;
        });
    }

    @Override
    public boolean changePassword(String userName, String oldPassword, String newPassword) {
        return getHibernateTemplate().execute(session -> {
            Query query = session.createQuery("update User u set u.password = :newPassword " +
                    "where u.userName = :userName and u.password = :oldPassword");
            query.setString("userName", userName);
            query.setString("oldPassword", oldPassword);
            query.setString("newPassword", newPassword);
            return query.executeUpdate() > 0;
        });
    }
}
