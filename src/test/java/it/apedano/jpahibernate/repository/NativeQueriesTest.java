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
import javax.transaction.Transactional;
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
public class NativeQueriesTest {

    private final Logger LOGGER = LoggerFactory.getLogger(NativeQueriesTest.class);

    @Autowired
    private EntityManager em;

    @Test
    @DirtiesContext //spring will automatically reset the data changed by the test method. This makes the test atomic and isolated
    public void nativeQuery_basic() {
        //you are not usinf the persistence context here with native queries
        Query query = em.createNativeQuery("SELECT * FROM COURSE_DETAILS", Course.class);
        List<Course> courses = query.getResultList();
        LOGGER.info("Results of 'SELECT * FROM COURSE_DETAILS' -> {}", courses);
    }
    
    @Test
    @DirtiesContext //spring will automatically reset the data changed by the test method. This makes the test atomic and isolated
    public void nativeQuery_withParameter() {
        //you are not usinf the persistence context here with native queries
        Query query = em.createNativeQuery("SELECT * FROM COURSE_DETAILS WHERE id = ?", Course.class);
        query.setParameter(1, 10003l);
        List<Course> courses = query.getResultList();
        LOGGER.info("Results of 'SELECT * FROM COURSE_DETAILS WHERE id = ?' with parameters -> {}", courses);
        //[Course{id=10003, name=Corso 10003 - updated, lastUpdatedDate=2019-11-02T20:00:40.617819, createDate=2019-11-02T00:00}]
    }
    
    @Test
    @DirtiesContext //spring will automatically reset the data changed by the test method. This makes the test atomic and isolated
    public void nativeQuery_withNamedParameter() {
        //you are not usinf the persistence context here with native queries
        Query query = em.createNativeQuery("SELECT * FROM COURSE_DETAILS WHERE id = :id", Course.class);
        query.setParameter("id", 10003l);
        List<Course> courses = query.getResultList();
        LOGGER.info("Results of 'SELECT * FROM COURSE_DETAILS WHERE id = :id' with named parameters -> {}", courses);
        //[Course{id=10003, name=Corso 10003 - updated, lastUpdatedDate=2019-11-02T20:00:40.617819, createDate=2019-11-02T00:00}]
    }   
    
    @Test
    @Transactional //we need transaction because of the native update query
    public void nativeQuery_MassUpdate() {
        //you are not usinf the persistence context here with native queries
        Query query = em.createNativeQuery("UPDATE COURSE_DETAILS set LAST_UPDATED_DATE = sysdate()", Course.class);
        int noOfRowsUpdated = query.executeUpdate();
        LOGGER.info("Rows updated by 'UPDATE COURSE_DETAILS set LAST_UPDATED_DATE = sysdate()' -> {}", noOfRowsUpdated);
    }   
}