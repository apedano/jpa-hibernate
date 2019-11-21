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

import it.apedano.jpahibernate.JpaHibernateApplication;
import it.apedano.jpahibernate.entity.Course;
import it.apedano.jpahibernate.entity.Review;
import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Alessandro Pedano <alessandro.pedano@transsmart.com>
 */
@RunWith(SpringRunner.class) //used to launch the spring context
@SpringBootTest(classes = JpaHibernateApplication.class) //the spring context reference
public class CourseRepositoryTest {

    private final Logger LOGGER = LoggerFactory.getLogger(CourseRepositoryTest.class);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EntityManager em;

    @Test
    public void findByIdTest() {
        //launches the the spring context and then close the application
        //this code is executed after the run in the application has beed exeecuted
        LOGGER.info("Test is running...");
        Course course = courseRepository.findById(10001L);
        assertEquals("Corso 10001", course.getName());
        //at the end the test the db is dropped
    }

    @Test
    @DirtiesContext //spring will automatically reset the data changed by the test method. This makes the test atomic and isolated
    public void deleteByIdTest() {
        long id = 10002L;
        courseRepository.deleteById(id);
        assertNull(courseRepository.findById(id));
    }

    @Test
    @DirtiesContext
    public void saveUpdateTest() {
        String newCourseName = "Sei un caro amico";
        Course course = courseRepository.findById(10001L);
        course.setName(newCourseName);
        courseRepository.save(course);
        Course loadedCourse = courseRepository.findById(10001L);
        assertEquals(newCourseName, loadedCourse.getName());
    }

    @Test
    @DirtiesContext
    public void playWithEntityManagerTest() {
        LOGGER.info("playWithEntityManagerTest - started");
        courseRepository.flushDetachClearExample();
    }

    @Test
    @Transactional
    public void oneToManyLazyLoadingTest() {
        LOGGER.info("oneToManyLazyLoadingTest - started");
        Course course = courseRepository.findById(10001l);
        List<Review> reviews = course.getReviews();
        /*
        Here, if the method is not transactional and since the default loading strategy is LAZY, the method would fail
        LazyInitializationException: failed to lazily initialize a collection of role
         */
        assertFalse(reviews.isEmpty());
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
        LOGGER.info("oneToManyLazyLoadingTest - finished");
    }

    @Test
    public void oneToManyEagerLoadingTest() {
        LOGGER.info("oneToManyEagerLoadingTest - started");
        Course course = this.courseRepository.findById(10001l);
        /* ***** Note the difference with the lazy loaded reviews association ****
        select
        course0_.id as id1_0_0_,
        course0_.create_date as create_d2_0_0_,
        course0_.last_updated_date as last_upd3_0_0_,
        course0_.name as name4_0_0_,
        reviewseag1_.course_id as course_i4_2_1_,
        reviewseag1_.id as id1_2_1_,
        reviewseag1_.id as id1_2_2_,
        reviewseag1_.course_id as course_i4_2_2_,
        reviewseag1_.description as descript2_2_2_,
        reviewseag1_.rating as rating3_2_2_
    from
        course_details course0_
    left outer join    <-----------------------------------
        review reviewseag1_
            on course0_.id=reviewseag1_.course_id
    where
        course0_.id=?
         */
        //here the reviews are loaded already and the persistence context doesn't need to be extended here.
        //Therefore this method doesn't need the Transactional annotation
        List<Review> reviews = course.getReviewsEager();
        assertFalse(reviews.isEmpty());
        LOGGER.info("oneToManyEagerLoadingTest - finished");

    }

    @Test
    public void getManyToOneEntityTest() {
        LOGGER.info("getManyToOneEntityTest - started");
        Review review = em.find(Review.class, 500001l);
        Course course = review.getCourse();
        /*
        select
        review0_.id as id1_2_0_,
        review0_.course_id as course_i4_2_0_,
        review0_.description as descript2_2_0_,
        review0_.rating as rating3_2_0_,
        course1_.id as id1_0_1_,
        course1_.create_date as create_d2_0_1_,
        course1_.last_updated_date as last_upd3_0_1_,
        course1_.name as name4_0_1_
    from
        review review0_
    left outer join
        course_details course1_
            on review0_.course_id=course1_.id
    where
        review0_.id=?
         */
        assertFalse(course == null);
        LOGGER.info("Course name : {}", course.getName());
        //toOne association is EAGER by default, therefore no transation is required here
        LOGGER.info("getManyToOneEntityTest - finished");
    }
}
