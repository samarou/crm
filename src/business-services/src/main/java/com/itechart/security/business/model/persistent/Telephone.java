package com.itechart.security.business.model.persistent;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Telephone extends SecuredEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 100)
    private String number;

    @ManyToOne(optional = false)
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    @ManyToOne(optional = false)
    @JoinColumn(name = "telephone_type_id", nullable = false)
    private TelephoneType type;

    @Override
    public Long getId() {
        return id;
    }
}
