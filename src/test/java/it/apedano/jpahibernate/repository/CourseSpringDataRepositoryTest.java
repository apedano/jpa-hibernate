/*
 * COPYRIGHT NOTICE
 * Â© 2019  Transsmart Holding B.V.
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
import java.util.Optional;
import static junit.framework.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Alessandro Pedano <alessandro.pedano@transsmart.com>
 */
@RunWith(SpringRunner.class) //used to launch the spring context
@SpringBootTest(classes = JpaHibernateApplication.class) //the spring context reference
public class CourseSpringDataRepositoryTest {

    private final Logger LOGGER = LoggerFactory.getLogger(CourseRepositoryTest.class);

    @Autowired
    private CourseSpringDataRepository courseRepository;

    @Test
    public void findByIdTest_CoursePresent() {
        //launches the the spring context and then close the application
        //this code is executed after the run in the application has beed exeecuted
        LOGGER.info("Test is running...");
        Optional<Course> courseOptional = courseRepository.findById(10001L);
        LOGGER.info("Course is present -> {}", courseOptional.isPresent());
        assertTrue(courseOptional.isPresent());
        //at the end the test the db is dropped
    }
    
    @Test
    public void sort_test() {        
        LOGGER.info("Sorted Courses by name-> {}", courseRepository.findAll(Sort.by(Sort.DEFAULT_DIRECTION.ASC, "name")));
        LOGGER.info("Courses count -> {}", courseRepository.count());
        
        
    }
    
    @Test
    public void pagination_test() {        
        PageRequest pageRequest = PageRequest.of(0, 3); //first page of size 3
        Page<Course> firstPage = courseRepository.findAll(pageRequest); 
        LOGGER.info("First Page -> {}", firstPage); //Page 1 of 4 containing it.apedano.jpahibernate.entity.Course instances
        LOGGER.info("First Page content -> {}", firstPage.getContent()); 
        LOGGER.info("Page {} of {} ", firstPage.getNumber(), firstPage.getTotalPages()); 
        LOGGER.info("Elements {} of {}", firstPage.getNumberOfElements(), firstPage.getTotalElements()); 
        
        Pageable secondPageable = firstPage.nextPageable();
        Page<Course> secondPage = courseRepository.findAll(secondPageable); 
        LOGGER.info("Second Page content -> {}", secondPage.getContent()); 
        LOGGER.info("Page {} of {} ", secondPage.getNumber(), secondPage.getTotalPages()); 
    }

    @Test
    public void playAroundWithRepository() {
        Course course = new Course("Sample course created at runtime");
        courseRepository.save(course);
        course.setName(course.getName() + " updated");
        courseRepository.save(course);
        
        LOGGER.info("Courses -> {}", courseRepository.findAll());
        LOGGER.info("Courses count -> {}", courseRepository.count());
    }
    
    @Test
    public void additionalMethodTest() {
        String name = "Ora me ne vado";
        LOGGER.info("Courses by name [{}] -> {}", name, courseRepository.findByName(name));
        LOGGER.info("# of Courses with name [{}] -> {}", name, courseRepository.countByName(name));
        LOGGER.info("Courses with text [{}] -> {}", name, courseRepository.coursedWithTextInName(name));
        
    }
        
}
