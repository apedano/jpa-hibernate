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
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
public class CriteriaQueryTest {

    private final Logger LOGGER = LoggerFactory.getLogger(CriteriaQueryTest.class);

    @Autowired
    private EntityManager em;

    @Test
    @DirtiesContext
    public void all_courses() {
        //1. Use Criteria Builder to create a query returning the expected result type
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Course> cq = cb.createQuery(Course.class);
        
        //2. Define roots for tables involved in the query -> from which table we retrieve data
        Root<Course> courseRoot = cq.from(Course.class);
        
        //3. Define Predicats etc using Criteria Builder
        //4. Add Predicats etc using Criteria Query
        //5. Build the TypedQuery using the entity manager and criteria query
        TypedQuery<Course> query = em.createQuery(cq.select(courseRoot));
        List<Course> courses = query.getResultList();
        LOGGER.info("Results of 'Select c from Course c' -> {}", courses);
    }
    
    @Test
    @DirtiesContext
    public void courses_with_10004() {
        //1. Use Criteria Builder to create a query returning the expected result type
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Course> cq = cb.createQuery(Course.class);
        
        //2. Define roots for tables involved in the query -> from which table we retrieve data
        Root<Course> courseRoot = cq.from(Course.class);
        
        //3. Define Predicats etc using Criteria Builder
        Predicate like10004 = cb.like(courseRoot.get("name"), "%10004%"); //column name and condition
        
        //4. Add Predicats etc using Criteria Query
        cq.where(like10004);
        
        //5. Build the TypedQuery using the entity manager and criteria query
        TypedQuery<Course> query = em.createQuery(cq.select(courseRoot));
        List<Course> courses = query.getResultList();
        LOGGER.info("Results of corso like 10004 -> {}", courses);
    }
    
    @Test
    @DirtiesContext
    public void allCoursesWithoutStudents() {
        //1. Use Criteria Builder to create a query returning the expected result type
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Course> cq = cb.createQuery(Course.class);
        
        //2. Define roots for tables involved in the query -> from which table we retrieve data
        Root<Course> courseRoot = cq.from(Course.class);
        
        //3. Define Predicats etc using Criteria Builder
        Predicate studentsIsEmpty = cb.isEmpty(courseRoot.get("students")); //column name and condition
        
        //4. Add Predicats etc using Criteria Query
        cq.where(studentsIsEmpty);
        
        //5. Build the TypedQuery using the entity manager and criteria query
        TypedQuery<Course> query = em.createQuery(cq.select(courseRoot));
        List<Course> courses = query.getResultList();
        LOGGER.info("Results of corso without students -> {}", courses);
    }
    
    @Test
    @DirtiesContext
    public void join() {
        //1. Use Criteria Builder to create a query returning the expected result type
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Course> cq = cb.createQuery(Course.class);
        
        //2. Define roots for tables involved in the query -> from which table we retrieve data
        Root<Course> courseRoot = cq.from(Course.class);
        
        //3. Define Predicats etc using Criteria Builder
        courseRoot.join("students");
        
        //4. Add Predicats etc using Criteria Query
        
        //5. Build the TypedQuery using the entity manager and criteria query
        TypedQuery<Course> query = em.createQuery(cq.select(courseRoot));
        List<Course> courses = query.getResultList();
        LOGGER.info("Results of corso join students -> {}", courses);
    }
    
    @Test
    @DirtiesContext
    public void left_join() {
        //1. Use Criteria Builder to create a query returning the expected result type
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Course> cq = cb.createQuery(Course.class);
        
        //2. Define roots for tables involved in the query -> from which table we retrieve data
        Root<Course> courseRoot = cq.from(Course.class);
        
        //3. Define Predicats etc using Criteria Builder
        courseRoot.join("students", JoinType.LEFT);
        
        //4. Add Predicats etc using Criteria Query
        
        //5. Build the TypedQuery using the entity manager and criteria query
        TypedQuery<Course> query = em.createQuery(cq.select(courseRoot));
        List<Course> courses = query.getResultList();
        LOGGER.info("Results of corso left join students -> {}", courses);
    }
    
    
    
    
    
    
    
    
}