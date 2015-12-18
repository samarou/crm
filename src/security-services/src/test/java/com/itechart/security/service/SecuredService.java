package com.itechart.security.service;

import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Service containing a set of methods that allows to test security annotations
 *
 * @author andrei.samarou
 */
public class SecuredService {

    @SuppressWarnings("unused")
    public boolean hasAccess(boolean has) {
        return has;
    }

    @PreAuthorize("hasPermission(#objectId, 'TestObject', 'READ')")
    public void doPreAuthorizeByReadObjectId(@P("objectId") Long objectId) {
    }

    @PreFilter("filterObject.getProperty() == authentication.name")
    public <T> List<T> doPreFilterWithObjectPropertyEqUserName(List<T> input) {
        return new ArrayList<>(input);
    }

    @PostFilter("filterObject.getProperty() == authentication.name")
    public <T> List<T> doPostFilterWithObjectPropertyEqUserName(List<T> input) {
        return new ArrayList<>(input);
    }

    @PreFilter("hasPermission(filterObject, 'READ')")
    public <T> List<T> doPreFilterByPermissionRead(List<T> input) {
        return new ArrayList<>(input);
    }

    @PostFilter("hasPermission(filterObject, 'WRITE')")
    public <T> T[] doPostFilterByPermissionWrite(T[] input) {
        return Arrays.copyOf(input, input.length);
    }

    @PostFilter("hasPermission(filterObject, 'READ')")
    public <T> List<T> doPostFilterByPermissionRead(List<T> input) {
        return new ArrayList<>(input);
    }

}