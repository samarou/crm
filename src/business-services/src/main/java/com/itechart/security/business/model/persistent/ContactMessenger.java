package com.itechart.security.business.model.persistent;

import javax.persistence.*;

@Entity

@Table(name = "contact_messenger")
public class ContactMessenger extends SecuredEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "messenger_id", nullable = false)
    private Messenger messenger;

    @ManyToOne(optional = false)
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    @Column(name = "username", nullable = false)
    private String username;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Messenger getMessenger() {
        return this.messenger;
    }

    public void setMessenger(Messenger messenger) {
        this.messenger = messenger;
    }

    public Contact getContact() {
        return this.contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContactMessenger that = (ContactMessenger) o;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (messenger != null ? !messenger.equals(that.messenger) : that.messenger != null) return false;
        if (contact != null ? !contact.equals(that.contact) : that.contact != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (messenger != null ? messenger.hashCode() : 0);
        result = 31 * result + (contact != null ? contact.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }
}
