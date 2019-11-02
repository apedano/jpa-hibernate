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

import it.apedano.jpahibernate.entity.Course;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Alessandro Pedano <alessandro.pedano@transsmart.com>
 */
@Repository
@Transactional //any public method in this class will be executed within a transaction
public class CourseRepository {

    private final Logger LOGGER = LoggerFactory.getLogger(CourseRepository.class);

    @Autowired
    EntityManager em;

    public Course findById(Long id) {
        return em.find(Course.class, id);
    }

    public Course save(Course course) {
        if (course.getId() == null) {
            em.persist(course);
        } else {
            em.merge(course);
        }
        return course;
    }

    public void deleteById(Long id) {
        //this method tries to make a change in data
        //therefore you need a transaction
        Course course = findById(id);
        em.remove(course);
    }

    public void playWithEntityManager() {
        String newCourseName = "Nuovo corso per giocare";
        LOGGER.info("Method [{}] called", "playWithEntityManager()");
        Course course1 = new Course(newCourseName);
        em.persist(course1); //here the course is persiste
        em.flush(); //all changes until now are all persisted to the database
        
        //I don't need to call the em.merge() the entity because the entity manager keep tracks of all the changes and flush
        //them at the transaction commit 
        //because the class is annotated as Transactional
        Course course2 = new Course(newCourseName + "2");
        em.persist(course2);
        em.flush();
        em.detach(course2); //all changes to course2 from now on, 
        //will not be transferred to the database
        em.clear(); //all entities wil not be tracked by the em
        //it's like a detachAll method
        course1.setName(course1.getName() + " - updated");
        course2.setName(course2.getName() + " - updated");
        
        

    }
}
