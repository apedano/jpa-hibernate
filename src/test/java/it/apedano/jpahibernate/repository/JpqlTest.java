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
    
    
}
 