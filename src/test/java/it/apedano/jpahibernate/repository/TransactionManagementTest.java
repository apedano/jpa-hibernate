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
import it.apedano.jpahibernate.entity.Passport;
import it.apedano.jpahibernate.entity.Student;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.hibernate.LazyInitializationException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
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
 * Transaction is triggered by the persist method in the EntityManager and all changes in the method are persisted as
 * late as possible unless you call a flush. Everything till then is in the Persistence context
 *
 * @author Alessandro Pedano <alessandro.pedano@transsmart.com>
 */
@RunWith(SpringRunner.class) //used to launch the spring context
@SpringBootTest(classes = JpaHibernateApplication.class) //the spring context reference
public class TransactionManagementTest {

    private final Logger LOGGER = LoggerFactory.getLogger(TransactionManagementTest.class);

    @Autowired
    private EntityManager em;

    @Autowired
    private StudentRepository studentRepository;

    /**
     * The method is wrapped into a transaction, therefore it begins before the first line of the code and it ends at
     * the last line. When a call to em.persist(entity1) is call. Hibernate only runs the logic to assing and id to the
     * entity (calling the sequence created by hibernate) but it is not saved to the database yet, it is only part of
     * the persistence context (the Entity manager keeps track of it) EM will also keep track of all changes to the
     * entities.
     *
     * We need @Transactional because we need to use the database, like all methods in Repository implementations. There
     * the transaction starts and ends within the repository method. In the unit test method, in case of a test changing
     * data (using EntityManager) requires the
     *
     * @Transactional because we need new transaction boundary for the test method, even though is using methods from
     * the repository.
     */
    @Test
    @Transactional
    public void someMEthodWithChange() {
        //LOG: Began transaction (1) for test context
        Student student = new Student("Transaction example");
        em.persist(student);
        //LOG: call next value for hibernate_sequence
        em.flush();
        /**
         * insert into student (name, passport_id, id) values (?, ?, ?)
         */
        //LOG: Rolled back transaction for test: (the new entity will not be persisted)
    } //all changes are saved down to the database

    /**
     * In read only test methods the first call to the EntityManager in the method like
     */
    @Test(expected = LazyInitializationException.class)
    public void readOnlyMethod_WithoutTransactional_thenError() {
        Student student = em.find(Student.class, 20001l); //here the transaction for the find method ends
        //nothing here will provide a transaction, no session will be available.
        Passport passport = student.getPassport(); //error is not here because of lazy load
        LOGGER.info("Student passport number -> {}", passport.getNumber()); //no transaction -> fail
        //here hibernate tries to fetch the associated entity (set as lazy load)
        //LOG: org.hibernate.LazyInitializationException: could not initialize proxy [it.apedano.jpahibernate.entity.Passport#40001] - no Session
        fail("A LazyInitializationException should be thrown");
    }

    /**
     * @DirtiesContext is a Spring testing annotation. It indicates the associated test or class modifies the
     * ApplicationContext. It tells the testing framework to close and recreate the context for later tests.
     *
     * Now we'll show @DirtiesContext with the default MethodMode, AFTER_METHOD. This means Spring will mark the context
     * for closure after the corresponding test method completes.
     *
     * To isolate changes to a test, we add @DirtiesContext. Let's see how it works.
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void dirtiesContextUsage() {
        String newName = "New Name";
        //get student
        Student student = studentRepository.findById(20001l);

        //update details
        student.setName(newName);
        studentRepository.save(student);
        /*
         update
        student
    set
        name=?,
        passport_id=?
    where
        id=?
2019-11-17 11:17:29.411 TRACE 29788 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [VARCHAR] - [New Name]
2019-11-17 11:17:29.412 TRACE 29788 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [2] as [BIGINT] - [40001]
2019-11-17 11:17:29.412 TRACE 29788 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [3] as [BIGINT] - [20001]
         */

        //check the value
        Student student1 = studentRepository.findById(20001l);
        assertEquals(newName, student1.getName());
        // here we would leave the data changed in the database. We need to rollback, DirtiesContext will do that.
        //The test will ensure consistency of the data with the status of data before the test run

    }

}
