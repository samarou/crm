package com.itechart.sample.security.service;

import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;

import java.util.List;

/**
 * Service containing a set of methods that allows to test security annotations
 *
 * @author andrei.samarou
 */
public class SecuredService {

    public boolean hasAccess(boolean has) {
        return has;
    }

    @PreAuthorize("hasPermission(#objectId, 'Object', 'READ')")
    public void doPreAuthorizeByObjectId(Long objectId) {
    }

    @PreFilter("filterObject.property == authentication.name")
    public <T> List<T> doPreFilterWithObjectPropertyEqUserName(List<T> input) {
        return input;
    }

    @PostFilter("filterObject.property == authentication.name")
    public <T> List<T> doPostFilterWithObjectPropertyEqUserName() {
        return null;
    }

    @PreFilter("hasPermission(filterObject, 'READ')")
    public <T> List<T> doPreFilterByPermissionRead(List<T> input) {
        return input;
    }

    @PostFilter("hasPermission(filterObject, 'READ')")
    public <T> List<T> doPostFilterByPermissionRead(List<T> input) {
        return null;
    }

    //todo hasAnyPermission
}
