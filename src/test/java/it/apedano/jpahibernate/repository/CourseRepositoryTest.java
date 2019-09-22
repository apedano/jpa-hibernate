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
import static org.junit.Assert.assertEquals;
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
        courseRepository.playWithEntityManager();
    }

}
