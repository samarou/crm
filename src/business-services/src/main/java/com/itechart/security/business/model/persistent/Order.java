package com.itechart.security.business.model.persistent;

import com.itechart.security.business.model.enums.ObjectTypes;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Order
 *
 * @author andrei.samarou
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "`order`")
public class Order extends SecuredEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product", nullable = false, length = 100)
    private String product;

    @Column(name = "count", nullable = false)
    private Integer count;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getObjectType() {
        return ObjectTypes.ORDER.getName();
    }
}
