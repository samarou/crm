package com.itechart.sample.security.service;

import com.itechart.sample.security.annotation.AclObjectFilter;
import com.itechart.sample.security.annotation.AclRule;
import com.itechart.sample.security.hibernate.aop.HibernateSecuredDao;
import com.itechart.sample.security.model.TestObject;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Map;

/**
 * Dao containing a set of methods that allows to test security annotations
 *
 * @author andrei.samarou
 */
public class SecuredDao implements HibernateSecuredDao {

    private SessionFactory sessionFactory;

    @AclObjectFilter
    public void doBadDeclaration1() {
    }

    @AclObjectFilter
    public <T> T[] doBadDeclaration2() {
        return null;
    }

    @AclObjectFilter
    public <T> T doBadDeclaration3() {
        return null;
    }

    @AclObjectFilter
    public <T> List<T> doBadDeclaration4() {
        return null;
    }

    @AclObjectFilter
    public <K, V> Map<K, V> doBadDeclaration5() {
        return null;
    }

    @AclObjectFilter
    @SuppressWarnings("UnusedParameters")
    public void doBadDeclaration6(List<TestObject> objects) {
    }

    @AclObjectFilter
    public TestObject doGoodDeclaration1() {
        return null;
    }

    @AclObjectFilter
    public TestObject[] doGoodDeclaration2() {
        return null;
    }

    @AclObjectFilter
    public List<TestObject> doGoodDeclaration3() {
        return null;
    }

    @AclObjectFilter
    public List<List<TestObject>> doGoodDeclaration4() {
        return null;
    }

    @AclObjectFilter
    public <T> Map<T, TestObject> doGoodDeclaration5() {
        return null;
    }

    @AclObjectFilter(@AclRule(type = TestObject.class))
    public <T> T[] doGoodDeclaration6() {
        return null;
    }

    @AclObjectFilter(@AclRule(type = TestObject.class))
    public <T> List<T> doGoodDeclaration7() {
        return null;
    }

    @AclObjectFilter(@AclRule(type = TestObject.class))
    public <K, V> Map<K, V>  doGoodDeclaration8() {
        return null;
    }

    @AclObjectFilter(@AclRule(type = TestObject.class))
    public void doGoodDeclaration9(){
    }

    @AclRule(type = TestObject.class)
    public void doGoodDeclaration10(){
    }

    @Override
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Required
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
