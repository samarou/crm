package com.itechart.security.business.model.persistent;

import com.itechart.security.business.model.enums.ObjectTypes;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Table(name = "contact")
public class Contact extends SecuredEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstName", nullable = false, length = 50)
    private String firstName;

    @Column(name = "lastName", nullable = false, length = 50)
    private String lastName;

    @Column(length = 50)
    private String patronymic;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "is_male")
    private Boolean isMale;

    private String nationality;

    @Column(name = "photo_url")
    private String photoUrl;

    @OneToMany(orphanRemoval = true, mappedBy = "contact")
    private Set<Order> orders;

    @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY)
    private Set<MessengerAccount> messengers;

    @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY)
    private Set<SocialNetworkAccount> socialNetworks;

    @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY)
    private Set<Telephone> telephones;

    @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY)
    private Set<Address> addresses;

    @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY)
    private Set<Workplace> workplaces;

    @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY)
    private Set<Email> emails;

    @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY)
    private Set<Attachment> attachments;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getObjectType() {
        return ObjectTypes.CONTACT.getName();
    }

    @Column(name = "date_deleted")
    private Date dateDeleted;
}
