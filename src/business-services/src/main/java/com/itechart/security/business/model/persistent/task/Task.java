package com.itechart.security.business.model.persistent.task;

import com.itechart.security.business.model.persistent.SecuredEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

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

    @Column(name = "description", length = 300)
    private String description;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "assignee_id")
    private long assigneeId;

    @Column(name = "creator_id")
    private long creatorId;

    @ManyToOne
    private Status status;

    @ManyToOne
    private Priority priority;

    @Override
    public Long getId() {
        return id;
    }
}
