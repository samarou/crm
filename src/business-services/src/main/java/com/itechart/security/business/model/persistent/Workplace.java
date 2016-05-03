package com.itechart.security.business.model.persistent;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Workplace extends SecuredEntity{

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String position;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Type(type="text")
    private String comment;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    @Override
    public Long getId() {
        return id;
    }
}
