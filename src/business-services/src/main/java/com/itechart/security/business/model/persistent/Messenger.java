package com.itechart.security.business.model.persistent;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Table(name = "messenger")
public class Messenger extends SecuredEntity{

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "messenger")
    private Set<MessengerAccount> messengerAccounts;

    @Override
    public Long getId() {
        return id;
    }
}
