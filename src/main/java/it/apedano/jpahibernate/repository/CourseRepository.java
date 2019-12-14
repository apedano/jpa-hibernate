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
package it.apedano.jpahibernate.repository;

import it.apedano.jpahibernate.entity.Course;
import it.apedano.jpahibernate.entity.Review;
import java.util.List;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Alessandro Pedano <alessandro.pedano@transsmart.com>
 */
@Repository
@Transactional(isolation = Isolation.READ_UNCOMMITTED) //any public method in this class will be executed within a transaction
public class CourseRepository {

    private final Logger LOGGER = LoggerFactory.getLogger(CourseRepository.class);

    @Autowired
    EntityManager em;

    public Course findById(Long id) {
        return em.find(Course.class, id);
    }

    public Course save(Course course) {
        if (course.getId() == null) {
            em.persist(course);
        } else {
            em.merge(course);
        }
        return course;
    }

    public void deleteById(Long id) {
        //this method tries to make a change in data
        //therefore you need a transaction
        Course course = findById(id);
        em.remove(course);
    }

    public void flushDetachClearExample() {
        String newCourseName = "Nuovo corso per giocare";
        LOGGER.info("Method [{}] called", "playWithEntityManager()");
        Course course1 = new Course(newCourseName);
        em.persist(course1); //here the course is persiste
        em.flush(); //all changes until now are all persisted to the database
        //this will force the persist regardless of the em internal persist
        //strategy

        //I don't need to call the em.merge() the entity because the entity manager keep tracks of all the changes and flush
        //them at the transaction commit
        //because the class is annotated as Transactional
        Course course2 = new Course(newCourseName + "2");
        em.persist(course2);
        em.flush();
        em.detach(course2); //all changes to course2 from now on,
        //will not be transferred to the database
        em.clear(); //all entities wil not be tracked by the em
        //it's like a detachAll method
        course1.setName(course1.getName() + " - updated");
        course2.setName(course2.getName() + " - updated");
    }

    public void refreshExample() {
        String newCourseName = "Nuovo corso per giocare";
        LOGGER.info("Method [{}] called", "playWithEntityManager()");
        Course course1 = new Course(newCourseName);
        em.persist(course1); //here the course is persiste
        em.flush(); //all changes until now are all persisted to the database
        Course course2 = new Course(newCourseName + "2");
        em.persist(course2);
        em.flush();

        course1.setName(course1.getName() + " - updated");
        course2.setName(course2.getName() + " - updated");

        //course1 will be restored with data from db
        //coming from the last flush operation
        //(the last change to the name will not be keeped)
        LOGGER.info("Course1 name before refresh: " + course1.getName());
        em.refresh(course1); //triggers a select from the db
        //the printed name will be without updated
        LOGGER.info("Course1 name after refresh: " + course1.getName());
        em.flush();
    }

    public void timestampExample() throws InterruptedException {
        String newCourseName = "Nuovo corso per giocare";
        LOGGER.info("Method [{}] called", "playWithEntityManager()");
        Course course1 = new Course(newCourseName);
        em.persist(course1); //here the course is persiste
        em.flush(); //here creation and update change will be the same
        Thread.sleep(1500l);
        Course course10003 = findById(10003l);
        course10003.setName(course10003.getName() + " - updated");
        //here the transaction will be persisted and the course updated
    }

    public void addHardcodedReviewsToCourse() {
        Course course = findById(10001l);
        List<Review> reviews = course.getReviews();
        /*
        select
        reviews0_.course_id as course_i4_2_0_,
        reviews0_.id as id1_2_0_,
        reviews0_.id as id1_2_1_,
        reviews0_.course_id as course_i4_2_1_,
        reviews0_.description as descript2_2_1_,
        reviews0_.rating as rating3_2_1_
    from
        review reviews0_
    where
        reviews0_.course_id=?
         */
        LOGGER.info("Reviews of course 10001 -> {}", reviews);
        LOGGER.info("Create new reviews");
        Review review1 = new Review("New description1", "1");
        Review review2 = new Review("New description2", "2");

        LOGGER.info("Set the relationships");
        review1.setCourse(course);
        course.addReview(review1);
        review2.setCourse(course);
        course.addReview(review2);

        LOGGER.info("Save it in the database");
        em.persist(review1);
        em.persist(review2);
        /* What happens here at transation commit
2019-11-17 12:49:52.914  INFO 183812 --- [           main] i.a.j.repository.CourseRepository        : Create new reviews
2019-11-17 12:49:52.914  INFO 183812 --- [           main] i.a.j.repository.CourseRepository        : Set the relationships
2019-11-17 12:49:52.914  INFO 183812 --- [           main] i.a.j.repository.CourseRepository        : Save it in the database
        Hibernate:
    call next value for hibernate_sequence -> review 1
Hibernate:
    call next value for hibernate_sequence -> review 2
Hibernate:
    insert
    into
        review
        (course_id, description, rating, id)
    values
        (?, ?, ?, ?)
2019-11-17 12:49:52.939 TRACE 183812 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [BIGINT] - [10001]
2019-11-17 12:49:52.939 TRACE 183812 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [2] as [VARCHAR] - [New description1]
2019-11-17 12:49:52.940 TRACE 183812 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [3] as [VARCHAR] - [1]
2019-11-17 12:49:52.940 TRACE 183812 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [4] as [BIGINT] - [1]
Hibernate:
    insert
    into
        review
        (course_id, description, rating, id)
    values
        (?, ?, ?, ?)
2019-11-17 12:49:52.941 TRACE 183812 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [BIGINT] - [10001]
2019-11-17 12:49:52.941 TRACE 183812 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [2] as [VARCHAR] - [New description2]
2019-11-17 12:49:52.942 TRACE 183812 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [3] as [VARCHAR] - [2]
2019-11-17 12:49:52.942 TRACE 183812 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [4] as [BIGINT] - [2]
         */
    }

    public void addReviewsForCourse(Long courseId, List<Review> reviews) {
        Course course = findById(courseId);
        LOGGER.info("Reviews of course 10001 -> {}", reviews);
        reviews.forEach(review -> {
            review.setCourse(course);
            course.addReview(review);
            em.persist(review);
        });
    }

}
