/*
 * COPYRIGHT NOTICE
 * © 2019  Transsmart Holding B.V.
 * All Rights Reserved.
 * All information contained herein is, and remains the
 * property of Transsmart Holding B.V. and its suppliers,
 * if any.
 * The intellectual and technical concepts contained herein
 * are proprietary to Transsmart Holding B.V. and its
 * suppliers and may be covered by European and Foreign
 * Patents, patents in process, and are protected by trade
 * secret or copyright law.
 * Dissemination of this information or reproduction of this
 * material is strictly forbidden unless prior written
 * permission is obtained from Transsmart Holding B.V.
 */
package it.apedano.jpahibernate.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 *
 * @author Alessandro Pedano <alessandro.pedano@transsmart.com>
 */
@Entity
@Table(name="CourseDetails") //Hibernate will use the SQL convetions therefore the name will be course_details
@NamedQueries(
        value = {
            @NamedQuery(name="get_all_courses", query="Select c from Course c"),
            @NamedQuery(name="get_courses_like_giocare", query="Select c from Course c where c.name like '%giocare%'")
        })
public class Course implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="name", nullable = false, unique = false)
    private String name;
    
    
    @UpdateTimestamp //not JPA but custom Hibernate annotation
    private LocalDateTime lastUpdatedDate;
    
    @CreationTimestamp//not JPA but custom Hibernate annotation
    private LocalDateTime createDate;
    
    /**
     * Default constractor created by JPA Protected because has not to be public
     */
    protected Course() {
    }

    public Course(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Course{" + "id=" + id + ", name=" + name + ", lastUpdatedDate=" + lastUpdatedDate + ", createDate=" + createDate + '}';
    }

    

}
