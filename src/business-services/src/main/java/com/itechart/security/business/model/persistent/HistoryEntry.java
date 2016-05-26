package com.itechart.security.business.model.persistent;

import javax.persistence.*;
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
    private ObjectKey objectKey;

    @Column(name = "creator_id")
    private long creatorId;

    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @Column(name = "editor_id")
    private long editorId;

    @Column(name = "modification_date")
    private Date modificationDate;

    public HistoryEntry() {
    }

    public HistoryEntry(ObjectKey objectKey, long creatorId, Date createdDate, long editorId, Date modificationDate) {
        this.objectKey = objectKey;
        this.creatorId = creatorId;
        this.createdDate = createdDate;
        this.editorId = editorId;
        this.modificationDate = modificationDate;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ObjectKey getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(ObjectKey objectKey) {
        this.objectKey = objectKey;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public long getEditorId() {
        return editorId;
    }

    public void setEditorId(long editorId) {
        this.editorId = editorId;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }
}
