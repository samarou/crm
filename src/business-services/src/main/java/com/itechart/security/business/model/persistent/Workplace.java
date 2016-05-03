package com.itechart.security.business.model.persistent;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Date;

@Entity
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    @Override
    public Long getId() {
        return id;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Workplace workplace = (Workplace) o;

        if (id != null ? !id.equals(workplace.id) : workplace.id != null) return false;
        if (name != null ? !name.equals(workplace.name) : workplace.name != null) return false;
        if (position != null ? !position.equals(workplace.position) : workplace.position != null) return false;
        if (startDate != null ? !startDate.equals(workplace.startDate) : workplace.startDate != null) return false;
        if (endDate != null ? !endDate.equals(workplace.endDate) : workplace.endDate != null) return false;
        if (comment != null ? !comment.equals(workplace.comment) : workplace.comment != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }
}
