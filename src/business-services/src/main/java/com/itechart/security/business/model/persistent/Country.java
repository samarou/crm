package com.itechart.security.business.model.persistent;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Table(name = "country")
public class Country extends SecuredEntity{

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Override
    public Long getId() {
        return id;
    }
}
