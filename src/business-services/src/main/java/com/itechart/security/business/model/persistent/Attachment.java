/*
package com.itechart.security.business.model.persistent;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "attachment")
public class Attachment extends SecuredEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Type(type="text")
    private String comment;

    @Column(name = "date_upload", nullable = false)
    private Date dateUpload;

    @ManyToOne(optional = false)
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    @Column(name = "date_deleted")
    private Date dateDeleted;

    @Override
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDateUpload() {
        return dateUpload;
    }

    public void setDateUpload(Date uploadDate) {
        this.dateUpload = uploadDate;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
*/
