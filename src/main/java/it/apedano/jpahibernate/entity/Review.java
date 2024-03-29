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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Alessandro Pedano <alessandro.pedano@transsmart.com>
 */
@Entity
public class Review implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "description", nullable = true)
    private String description;

    @ManyToOne
    private Course course;

    /*By default the corresponding value stored in the database will be the ordinal
    of the enum: 1 for the first value, 2 for the second and so on...
     but it is a bad database design because if we insert a value in between existing ones
    all ordinals in our database will change creating inconsistencies.
    That's why we use the enum type String
    
    */
    @Enumerated(EnumType.STRING) 
    private ReviewRating rating;

    /**
     * Default constractor created by JPA Protected because has not to be public
     */
    protected Review() {
    }

    public Review(String description, ReviewRating rating) {
        this.description = description;
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ReviewRating getRating() {
        return rating;
    }

    public void setRating(ReviewRating rating) {
        this.rating = rating;
    }

    

    public Long getId() {
        return id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "Review{" + "id=" + id + ", description=" + description + ", rating=" + rating + '}';
    }

}
