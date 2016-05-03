package com.itechart.security.business.model.persistent;

import javax.persistence.*;

@Entity
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Contact getContact() {
        return this.contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public TelephoneType getType() {
        return this.type;
    }

    public void setType(TelephoneType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Telephone telephone = (Telephone) o;

        if (id != null ? !id.equals(telephone.id) : telephone.id != null) return false;
        if (number != null ? !number.equals(telephone.number) : telephone.number != null) return false;
        if (contact != null ? !contact.equals(telephone.contact) : telephone.contact != null) return false;
        if (type != null ? !type.equals(telephone.type) : telephone.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (contact != null ? contact.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
