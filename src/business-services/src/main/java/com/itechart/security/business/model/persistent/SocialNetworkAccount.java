package com.itechart.security.business.model.persistent;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "social_network_account")
public class SocialNetworkAccount extends SecuredEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "social_network_id", nullable = false)
    private SocialNetwork socialNetwork;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    @Column(name = "url", nullable = false)
    private String url;

    @Override
    public Long getId() {
        return id;
    }
}
