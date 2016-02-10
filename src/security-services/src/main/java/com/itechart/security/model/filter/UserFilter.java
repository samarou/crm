package com.itechart.security.model.filter;

/**
 * Filter for user data
 *
 * @author andrei.samarou
 */
public class UserFilter extends PageableFilter {

    /**
     * User role ID
     */
    private Long roleId;
    /**
     * User group ID
     */
    private Long groupId;
    /**
     * Only active users
     */
    private boolean active;


    /*
•	Поиск по подстроке. Одно текстовое поле на все текстовые атрибуты пользователя.
•	Выпадающий список с перечислением всех ролей отсортированных по имени
•	Выпадающий список с перечислением всех групп отсортированных по имени
•	Флажок ‘Только активные’. По умолчанию включен
     */

}