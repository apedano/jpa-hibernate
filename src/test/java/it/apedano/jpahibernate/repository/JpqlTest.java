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
import it.apedano.jpahibernate.entity.Student;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
public class JpqlTest {

    private final Logger LOGGER = LoggerFactory.getLogger(JpqlTest.class);

    @Autowired
    private EntityManager em;

    @Test
    @DirtiesContext //spring will automatically reset the data changed by the test method. This makes the test atomic and isolated
    public void jpql_basic() {
        //querying entities instead of tables in the db
        Query query = em.createQuery("Select c from Course c");
        List courses = query.getResultList();
        LOGGER.info("Results of 'Select c from Course c' -> {}", courses);
        
    }
    
    @Test
    @DirtiesContext //spring will automatically reset the data changed by the test method. This makes the test atomic and isolated
    public void jpql_typed() {
        //em.createQuey("Select c from Course c")
        TypedQuery<Course> query = 
                em.createQuery("Select c from Course c", Course.class);
        List<Course> courses = query.getResultList();
        LOGGER.info("Results of 'Select c from Course c' with mapping -> {}", courses);
    }
    
    @Test
    @DirtiesContext //spring will automatically reset the data changed by the test method. This makes the test atomic and isolated
    public void jpql_WithConditions() {
        //em.createQuey("Select c from Course c")
        TypedQuery<Course> query = 
                em.createQuery("Select c from Course c where c.name like '%giocare%'", Course.class);
        List<Course> courses = query.getResultList();
        LOGGER.info("Results of 'Select c from Course c' with conditions -> {}", courses);
        //[Course{id=1, name=Nuovo corso per giocare}, Course{id=2, name=Nuovo corso per giocare2 - updated}]
    }
    
    @Test
    @DirtiesContext //spring will automatically reset the data changed by the test method. This makes the test atomic and isolated
    public void jpql_WithNamedQuery() {
        //em.createQuey("Select c from Course c")
        TypedQuery<Course> query = 
                em.createNamedQuery("get_all_courses", Course.class);
        List<Course> courses = query.getResultList();
        LOGGER.info("Results of 'Select c from Course c' with conditions -> {}", courses);
        //[Course{id=1, name=Nuovo corso per giocare}, Course{id=2, name=Nuovo corso per giocare2 - updated}]
    }
    
    @Test
    @DirtiesContext //spring will automatically reset the data changed by the test method. This makes the test atomic and isolated
    public void jpql_WithConditionsWithNamedQuery() {
        //em.createQuey("Select c from Course c")
        TypedQuery<Course> query = 
                em.createNamedQuery("get_courses_like_giocare", Course.class);
        List<Course> courses = query.getResultList();
        LOGGER.info("Results of 'Select c from Course c' with conditions -> {}", courses);
        //[Course{id=1, name=Nuovo corso per giocare}, Course{id=2, name=Nuovo corso per giocare2 - updated}]
    }
    
    @Test
    @DirtiesContext //spring will automatically reset the data changed by the test method. This makes the test atomic and isolated
    public void jpql_coursesWithoutStudent() {
        //em.createQuey("Select c from Course c")
        TypedQuery<Course> query = 
                em.createQuery("Select c from Course c where c.students is empty", Course.class);
        /*
       select
        course0_.id as id1_0_,
        course0_.create_date as create_d2_0_,
        course0_.last_updated_date as last_upd3_0_,
        course0_.name as name4_0_ 
    from
        course_details course0_ 
    where
        not (exists (select
            student2_.id 
        from
            student_course students1_,
            student student2_ 
        where
            course0_.id=students1_.course_id 
            and students1_.student_id=student2_.id))
        */
        List<Course> courses = query.getResultList();
        LOGGER.info("Results of 'Select c from Course c where c.students is empty' -> {}", courses);
    }   
    
    @Test
    @DirtiesContext
    public void jpql_coursesWithAtLeast2Students() {
        TypedQuery<Course> query = 
                em.createQuery("Select c from Course c where size(c.students) >= 2", Course.class);
        /*
        select
        course0_.id as id1_0_,
        course0_.create_date as create_d2_0_,
        course0_.last_updated_date as last_upd3_0_,
        course0_.name as name4_0_ 
    from
        course_details course0_ 
    where
        (
            select
                count(students1_.course_id) -->  count
            from
                student_course students1_ 
            where
                course0_.id=students1_.course_id
        )>=2
        */
        List<Course> courses = query.getResultList();
        LOGGER.info("Results of 'Select c from Course c where size(c.students) >= 2' -> {}", courses);
    }   
    
    @Test
    @DirtiesContext
    public void jpql_orderCoursedByNumberOfStudents() {
        TypedQuery<Course> query = 
                em.createQuery("Select c from Course c order by size(c.students) desc", Course.class);
        List<Course> courses = query.getResultList();
        LOGGER.info("Results of 'Select c from Course c order by size(c.students) desc' -> {}", courses);
        /*
        select
        course0_.id as id1_0_,
        course0_.create_date as create_d2_0_,
        course0_.last_updated_date as last_upd3_0_,
        course0_.name as name4_0_ 
    from
        course_details course0_ 
    order by
        (select
            count(students1_.course_id) 
        from
            student_course students1_ 
        where
            course0_.id=students1_.course_id)
        */
    }
    
    @Test
    @DirtiesContext
    public void jpql_studentsWithN400InThePassport() {
        
        TypedQuery<Student> query = 
                em.createQuery("Select s from Student s where s.passport.number like '%N400%'", Student.class);
        List<Student> students = query.getResultList();
        LOGGER.info("Results of 'Select s from Student s where s.passport.number like '%N400%'' -> {}", students);
        /*
        select
        student0_.id as id1_5_,
        student0_.name as name2_5_,
        student0_.passport_id as passport3_5_ 
    from
        student student0_ cross 
    join
        passport passport1_ 
    where
        student0_.passport_id=passport1_.id 
        and (
            passport1_.number like '%N400%'
        )
        */
    }
    
    //JPQL BETWEEN
    //IS NULL
    //upper, lower, trim, length
    
    @Test
    public void join() {
        //JOIN -> Select c, s from Course JOIN c.students s -> no Course without students in the result, if any
        Query query = 
                em.createQuery("Select c, s from Course c JOIN c.students s");
        List<Object[]> results = query.getResultList();
        LOGGER.info("Results found -> {}", results.size());
        results.forEach(result -> {
            LOGGER.info("Course -> {} ; Student -> {}", result[0], result[1]);        
        });
        /*
        select
        course0_.id as id1_0_0_,
        student2_.id as id1_5_1_,
        course0_.create_date as create_d2_0_0_,
        course0_.last_updated_date as last_upd3_0_0_,
        course0_.name as name4_0_0_,
        student2_.name as name2_5_1_,
        student2_.passport_id as passport3_5_1_ 
    from
        course_details course0_ 
    inner join
        student_course students1_ 
            on course0_.id=students1_.course_id 
    inner join
        student student2_ 
            on students1_.student_id=student2_.id
        */
    }
    
    @Test
    public void left_join() {
        //LEFT JOIN -> Select c, s from Course JOIN c.students s -> Here course without students are retrieved
        Query query = 
                em.createQuery("Select c, s from Course c LEFT JOIN c.students s");
        List<Object[]> results = query.getResultList();
        LOGGER.info("Results found -> {}", results.size()); //--> 4
        results.forEach(result -> {
            LOGGER.info("Course -> {} ; Student -> {}", result[0], result[1]);        
        });
        /*
        Course without student (null)
        Course -> Course{id=10004, name=Corso 10004, lastUpdatedDate=2019-12-08T00:00, createDate=2019-12-08T00:00} ; Student -> null
        */
        /*
        select
        course0_.id as id1_0_0_,
        student2_.id as id1_5_1_,
        course0_.create_date as create_d2_0_0_,
        course0_.last_updated_date as last_upd3_0_0_,
        course0_.name as name4_0_0_,
        student2_.name as name2_5_1_,
        student2_.passport_id as passport3_5_1_ 
    from
        course_details course0_ 
    left outer join
        student_course students1_ 
            on course0_.id=students1_.course_id 
    left outer join
        student student2_ 
            on students1_.student_id=student2_.id
        */
    }
    
    @Test
    public void cross_join() {
        //CROSSJOIN -> Select c, s from Course c, Student s -> takes all Courses and students and mix them without associations
        Query query = 
                em.createQuery("Select c, s from Course c, Student s");
        List<Object[]> results = query.getResultList();
        LOGGER.info("Results found -> {}", results.size()); //-> 9
        results.forEach(result -> {
            LOGGER.info("Course -> {} ; Student -> {}", result[0], result[1]);        
        });
        /*
        ENTITY_1 ENTITY_2
        1,2,3        AB
        
        CROSS JOIN (even if there is no relationship beyween them
        1           A
        2           A
        3           A
        
        1           B
        2           B
        3           B
        */
        
    }
}