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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author Alessandro Pedano <alessandro.pedano@transsmart.com>
 */
@Entity
public class Passport implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "number", nullable = false, unique = true)
    private String number;

    /**
     * Here we declare the owner of the relationship Mapped by stands for the owner of the entity, in this case Student
     * is the owner of the Passport The mapped by refers to then name of the attribute in the owner entity Access field
     * {@link Student#passport} The passport table will not have the studentID as column. Student will
     */
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "passport")
    private Student student;

    /**
     * Default constractor created by JPA Protected because has not to be public
     */
    protected Passport() {
    }

    public Passport(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return "Passport{" + "id=" + id + ", number=" + number + '}';
    }
}
