package com.itechart.security.business.model.persistent;

import com.itechart.security.model.persistent.User;
import com.itechart.security.model.persistent.acl.AclObjectKey;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author yauheni.putsykovich
 */
@Entity
@Table(name = "history")
public class HistoryEntry extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private AclObjectKey objectKey;

    @OneToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @OneToOne
    @JoinColumn(name = "editor_id")
    private User editor;

    @Column(name = "modification_date")
    private Date modificationDate;

    public HistoryEntry() {
    }

    public HistoryEntry(AclObjectKey objectKey, User creator, Date createdDate, User editor, Date modificationDate) {
        this.objectKey = objectKey;
        this.creator = creator;
        this.createdDate = createdDate;
        this.editor = editor;
        this.modificationDate = modificationDate;
    }

    @Override
    public Serializable getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AclObjectKey getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(AclObjectKey objectKey) {
        this.objectKey = objectKey;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public User getEditor() {
        return editor;
    }

    public void setEditor(User editor) {
        this.editor = editor;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }
}
