package com.itechart.security.business.model.persistent;

import com.itechart.security.business.model.enums.ObjectTypes;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "contact")
public class Contact extends SecuredEntity {

    @Id
    @Column(name = "id", nullable = false)
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

    @Column(name = "industry")
    private String industry;

   /* @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Order> orders;*/

    @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<MessengerAccount> messengers;

    @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<SocialNetworkAccount> socialNetworks;

    @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Telephone> telephones;

    @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Address> addresses;

    @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Workplace> workplaces;

    @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Email> emails;

    @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Attachment> attachments;

    @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Skill> skills;

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
