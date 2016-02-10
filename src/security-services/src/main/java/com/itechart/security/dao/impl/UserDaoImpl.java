package com.itechart.security.dao.impl;

import com.itechart.security.dao.UserDao;
import com.itechart.security.model.filter.UserFilter;
import com.itechart.security.model.persistent.User;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

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

            /*

•	Поиск по подстроке. Одно текстовое поле на все текстовые атрибуты пользователя.
•	Выпадающий список с перечислением всех ролей отсортированных по имени
•	Выпадающий список с перечислением всех групп отсортированных по имени
•	Флажок ‘Только активные’. По умолчанию включен

*/


            appendSortableFilterConditions(criteria, filter);
            return criteria.list();
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