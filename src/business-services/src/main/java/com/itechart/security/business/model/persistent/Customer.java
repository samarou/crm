package com.itechart.security.business.model.persistent;

import com.itechart.security.business.model.enums.ObjectTypes;

import javax.persistence.*;
import java.util.Set;

/**
 * Customer
 *
 * @author andrei.samarou
 */
@Entity
@Table(name = "customer")
public class Customer extends SecuredEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstName", nullable = false, length = 50)
    private String firstName;

    @Column(name = "lastName", nullable = false, length = 50)
    private String lastName;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "address", length = 250)
    private String address;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "customer")
    private Set<Order> orders;

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getObjectType() {
        return ObjectTypes.CUSTOMER.getName();
    }
}
