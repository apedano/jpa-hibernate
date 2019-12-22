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
import java.util.List;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Alessandro Pedano <alessandro.pedano@transsmart.com>
 */
@RunWith(SpringRunner.class) //used to launch the spring context
@SpringBootTest(classes = JpaHibernateApplication.class) //the spring context reference
public class PerformanceTuningTest {

    private final Logger LOGGER = LoggerFactory.getLogger(PerformanceTuningTest.class);

    @Autowired
    private EntityManager em;

    @Test
    @Transactional
    public void findByIdTest() {
        LOGGER.info("Test is running...");
        List<Course> courses = em.createNamedQuery("get_all_courses", Course.class)
                .getResultList();
        /*
        Query for all courses
        select
        course0_.id as id1_0_,
        course0_.create_date as create_d2_0_,
        course0_.is_deleted as is_delet3_0_,
        course0_.last_updated_date as last_upd4_0_,
        course0_.name as name5_0_ 
    from
        course_details course0_ 
    where
        (
            course0_.is_deleted = 0
        )
        */
        courses.forEach(course -> 
                LOGGER.info("Students for course [{}] -> {}", course, course.getStudents()));
        /*
        For each course we retrieve the students resulting in N+1 queries (problem)
        select
        students0_.course_id as course_i2_6_0_,
        students0_.student_id as student_1_6_0_,
        student1_.id as id1_5_1_,
        student1_.city as city2_5_1_,
        student1_.line1 as line3_5_1_,
        student1_.line2 as line4_5_1_,
        student1_.name as name5_5_1_,
        student1_.passport_id as passport6_5_1_ 
    from
        student_course students0_ 
    inner join
        student student1_ 
            on students0_.student_id=student1_.id 
    where
        students0_.course_id=?
        */
        /*
        We could solve it by setting the assoctiation with students  as EAGER, 
        but it will retrieve the association also if it is not required creating
        performance leak
        */
    }
    
    @Test
    @Transactional
    public void solvingNPlusOneProblem_EntityGraph() {
        EntityGraph<Course> entityGraph = em.createEntityGraph(Course.class);
        entityGraph.addSubgraph("students");
        LOGGER.info("Test is running...");
        List<Course> courses = em.createNamedQuery("get_all_courses", Course.class) //original named querrt
                .setHint("javax.persistence.loadgraph", entityGraph)
                .getResultList();
        courses.forEach(course -> 
                LOGGER.info("Students for course [{}] -> {}", course, course.getStudents()));
        /*
        Only one query is run with left outer join 
        select
        ...
        from
        course_details course0_ 
    left outer join
        student_course students1_ 
            on course0_.id=students1_.course_id 
    left outer join
        student student2_ 
            on students1_.student_id=student2_.id 
    where
        (
            course0_.is_deleted = 0
        )
        */
    }
    
    @Test
    @Transactional
    public void solvingNPlusOneProblem_JoinFetch() {
        LOGGER.info("Test is running...");
        List<Course> courses = em.createNamedQuery("get_all_courses_join_fetch", Course.class) //original named querrt
                .getResultList();
        courses.forEach(course -> 
                LOGGER.info("Students for course [{}] -> {}", course, course.getStudents()));
        /*
        Only one query with JOIN FETCH (inner join)
        
         select
        ...
    from
        course_details course0_ 
    inner join
        student_course students1_ 
            on course0_.id=students1_.course_id 
    inner join
        student student2_ 
            on students1_.student_id=student2_.id 
    where
        (
            course0_.is_deleted = 0
        )
        */
    }

    
}
