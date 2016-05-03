package com.itechart.security.business.model.persistent;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "social_network")
public class SocialNetwork extends SecuredEntity{
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "socialNetwork")
    private Set<SocialNetworkAccount> socialNetworkAccounts;

    @Override
    public Long getId() {
        return id;
    }
}
