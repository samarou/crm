package com.itechart.security.business.model.persistent;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "attachment")
public class Attachment extends SecuredEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Type(type="text")
    private String comment;

    @Column(name = "date_upload", nullable = false)
    private Date dateUpload;

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    @Column(name = "date_deleted")
    private Date dateDeleted;

    @Transient
    private String filePath;

    @Override
    public Long getId() {
        return id;
    }
}
