package com.itechart.security.business.model.persistent;

import com.itechart.security.business.model.enums.TelephoneType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Telephone extends SecuredEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 100)
    private String number;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    @Column(name = "date_deleted")
    private Date dateDeleted;

    @Enumerated(EnumType.STRING)
    private TelephoneType type;

    @Override
    public Long getId() {
        return id;
    }
}
