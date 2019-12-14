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
import it.apedano.jpahibernate.entity.Passport;
import it.apedano.jpahibernate.entity.Student;
import java.util.List;
import javax.persistence.EntityManager;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Alessandro Pedano <alessandro.pedano@transsmart.com>
 */
@RunWith(SpringRunner.class) //used to launch the spring context
@SpringBootTest(classes = JpaHibernateApplication.class) //the spring context reference
public class StudentRepositoryTest {

    private final Logger LOGGER = LoggerFactory.getLogger(StudentRepositoryTest.class);

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DirtiesContext
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void retrieveStudentAndPassportDetailsOneToOneEagerLazyLoadingTest() {
        LOGGER.info("Test is running...");
        Student student = em.find(Student.class, 20001L);
        //the em loads the class instance also the passport is retrieved (in a one to one relationship the loading is
        //always EAGER by default!!!
        /*
        select
        student0_.id as id1_3_0_,
        student0_.name as name2_3_0_,
        student0_.passport_id as passport3_3_0_,
        passport1_.id as id1_1_1_,
        passport1_.number as number2_1_1_
    from
        student student0_
    left outer join
        passport passport1_
            on student0_.passport_id=passport1_.id
    where
        student0_.id=?
         */
        /**
         * Lazy loading
         *
         * select student0_.id as id1_3_0_, student0_.name as name2_3_0_, student0_.passport_id as passport3_3_0_ from
         * student student0_ where student0_.id=?
         *
         *
         */
        LOGGER.info("Student -> {}", student);
        Passport passport = student.getPassport();
        LOGGER.info("Passport -> {}", passport);
        /**
         * Only at this time the lazy loading is executed (the proxy is populated with the value fom the db) Hibernate:
         * select passport0_.id as id1_1_0_, passport0_.number as number2_1_0_ from passport passport0_ where
         * passport0_.id=?
         *
         * Note: the method has to be transactional because it requires the open session to load the entity
         */
    }

    @Test
    @Transactional
    public void getStudentFromPassport_oneToOneBidirectional() {
        Passport passport = em.find(Passport.class, 40001l);
        /*
         select
        passport0_.id as id1_1_0_,
        passport0_.number as number2_1_0_
    from
        passport passport0_
    where
        passport0_.id=?
2019-11-17 10:13:41.292 TRACE 132724 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [BIGINT] - [40001]
         */
        LOGGER.info("Passport (owned) -> {}", passport);
        Student student = passport.getStudent();
        /*
        select
        student0_.id as id1_3_0_,
        student0_.name as name2_3_0_,
        student0_.passport_id as passport3_3_0_
    from
        student student0_
    where
        student0_.passport_id=?
2019-11-17 10:13:41.293 TRACE 132724 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [BIGINT] - [40001]
2019-11-17 10:13:41.294 TRACE 132724 --- [           main] o.h.type.descriptor.sql.BasicExtractor   : extracted value ([id1_3_0_] : [BIGINT]) - [20001]
2019-11-17 10:13:41.295 TRACE 132724 --- [           main] o.h.type.descriptor.sql.BasicExtractor   : extracted value ([name2_3_0_] : [VARCHAR]) - [Alessandro - updated]
2019-11-17 10:13:41.295 TRACE 132724 --- [           main] o.h.type.descriptor.sql.BasicExtractor   : extracted value ([passport3_3_0_] : [BIGINT]) - [40001]
         */
        LOGGER.info("Student (owner) -> {}", student);
    }
    
    @Test
    @Transactional
    public void useManyToMany_fromStudentToCourses() {
        Student student = studentRepository.findById(20001l);
        LOGGER.info("Student -> {}", student);
        List<Course> courses = student.getCourses();
        LOGGER.info("Get courses called"); //not fetched yet because of the lazy fetching by default
        /*
        select
        courses0_.student_id as student_1_4_0_,
        courses0_.course_id as course_i2_4_0_,
        course1_.id as id1_0_1_,
        course1_.create_date as create_d2_0_1_,
        course1_.last_updated_date as last_upd3_0_1_,
        course1_.name as name4_0_1_ 
    from
        student_course courses0_ 
    inner join  --->> INNER JOIN
        course_details course1_ 
            on courses0_.course_id=course1_.id 
    where
        courses0_.student_id=?
        */
        LOGGER.info("Courses -> {}", courses);
        assertTrue(courses.size() == 2);
    }

}
