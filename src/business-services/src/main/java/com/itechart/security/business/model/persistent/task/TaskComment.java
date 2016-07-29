package com.itechart.security.business.model.persistent.task;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "task_comment")
public class TaskComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "date_deleted")
    private Date dateDeleted;

    @Column(name = "text", length = 2000)
    private String text;

    @Column(name = "task_id")
    private Long taskId;

}
