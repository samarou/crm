package com.itechart.security.business.model.persistent.task;

import com.itechart.security.business.model.enums.ObjectTypes;
import com.itechart.security.business.model.persistent.Contact;
import com.itechart.security.business.model.persistent.SecuredEntity;
import com.itechart.security.business.model.persistent.company.Company;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author yauheni.putsykovich
 */

@Entity
@Setter
@Getter
@Table(name = "task")
public class Task extends SecuredEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "location", length = 500)
    private String location;

    @Column(name = "description", length = 300)
    private String description;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "assignee_id")
    private Long assigneeId;

    @Column(name = "creator_id")
    private long creatorId;

    @ManyToOne
    private Status status;

    @ManyToOne
    private Priority priority;

    @ManyToMany
    @JoinTable(name = "task_company",
            joinColumns = {@JoinColumn(name = "task_id", updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "company_id", updatable = false)})
    private List<Company> companies;

    @ManyToMany
    @JoinTable(name = "task_contact",
            joinColumns = {@JoinColumn(name = "task_id", updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "contact_id", updatable = false)})
    private List<Contact> contacts;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getObjectType() {
        return ObjectTypes.TASK.getName();
    }
}
