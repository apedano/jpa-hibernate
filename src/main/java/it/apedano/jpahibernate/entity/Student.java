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

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Alessandro Pedano <alessandro.pedano@transsmart.com>
 */
@Entity
public class Student implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * alter table student add constraint FK6i2dofwfuu97njtfprqv68pib foreign key (passport_id) references passport
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore //to avoid the infinite loop in the json output of the rest 
    private Passport passport;
    
    @ManyToMany
    //for the owning side of the relationship
    //we can define the join table name and column namse
    /**
     * create table student_course (
       student_id bigint not null,
        course_id bigint not null
        * 
        * 
        * alter table student_course 
       add constraint FKa09ocite73agl8tjixt02mhk7 
       foreign key (course_id) 
       references course_details
       * 
       *  alter table student_course 
       add constraint FKq7yw2wg9wlt2cnj480hcdn6dq 
       foreign key (student_id) 
       references student
    )
     */
    @JoinTable(name="STUDENT_COURSE",
            joinColumns = @JoinColumn(name = "STUDENT_ID"),
             inverseJoinColumns = @JoinColumn(name = "COURSE_ID")) 
    private List<Course> courses = new LinkedList<>();

    /**
     * Default constractor created by JPA Protected because has not to be public
     */
    protected Student() {
    }

    public Student(String name) {
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

    public Passport getPassport() {
        return passport;
    }

    public void setPassport(Passport passport) {
        this.passport = passport;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void addCourse(Course course) {
        this.courses.add(course);
    }

    @Override
    public String toString() {
        return "Student{" + "id=" + id + ", name=" + name + '}';
    }

}
