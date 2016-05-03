package com.itechart.security.business.model.persistent;

import javax.persistence.*;

@Entity
@Table(name = "contact_social_network")
public class ContactSocialNetwork extends SecuredEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "social_network_id", nullable = false)
    private SocialNetwork socialNetwork;

    @ManyToOne(optional = false)
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    @Column(name = "url", nullable = false)
    private String url;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SocialNetwork getSocialNetwork() {
        return this.socialNetwork;
    }

    public void setSocialNetwork(SocialNetwork socialNetwork) {
        this.socialNetwork = socialNetwork;
    }

    public Contact getContact() {
        return this.contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContactSocialNetwork that = (ContactSocialNetwork) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (socialNetwork != null ? !socialNetwork.equals(that.socialNetwork) : that.socialNetwork != null)
            return false;
        if (contact != null ? !contact.equals(that.contact) : that.contact != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (socialNetwork != null ? socialNetwork.hashCode() : 0);
        result = 31 * result + (contact != null ? contact.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}
