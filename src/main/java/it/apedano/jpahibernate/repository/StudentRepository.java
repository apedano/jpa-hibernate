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
import it.apedano.jpahibernate.entity.Passport;
import it.apedano.jpahibernate.entity.Student;
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
public class StudentRepository {

    private final Logger LOGGER = LoggerFactory.getLogger(StudentRepository.class);

    @Autowired
    EntityManager em;

    public Student findById(Long id) {
        return em.find(Student.class, id);
    }

    public Student save(Student student) {
        if (student.getId() == null) {
            em.persist(student);
        } else {
            em.merge(student);
        }
        return student;
    }

    public void deleteById(Long id) {
        //this method tries to make a change in data
        //therefore you need a transaction
        Student student = findById(id);
        em.remove(student);
    }

    public void saveStudentWithPassport() {
        //Step 1 - retrieve a student
        Student student = this.findById(20001l);
        //Now the persistence context contains only the student entity instance

        //Step 2 - get the passport from student using the student entity instance (lazy loaded)
        Passport passport = student.getPassport();
        //persistence context (student, passport)

        //Step 3 - update passport
        passport.setNumber("E123456");
        //persistence context (student, passport++)
        //if this method wasn't transactional there wouldn't be any persistence context here and an exception would
        //have been thrown
        //LazyInitializationException: could not initialize proxy [it.apedano.jpahibernate.entity.Passport#40001] - no Session
        //Step 4 - update student
        student.setName(student.getName() + " - updated");
        //persistence context (student++, passport++)

        //only here the changes will be persisted in the database or rolled back in case of errors
    }
    
    public void insertHardcodedStudenAndCourse() {
        Student student = new Student("Jack");
        Course course = new Course("Microservices in 100 steps");
        em.persist(student); //call next value for hibernate_sequence
        em.persist(course); //call next value for hibernate_sequence
        
        student.addCourse(course);
        course.addStudent(student);
        /*
        At the end of the method (transactional)
        
         insert 
    into
        student
        (name, passport_id, id) 
    values
        (?, ?, ?)
        
        insert 
    into
        course_details
        (create_date, last_updated_date, name, id) 
    values
        
        insert 
    into
        student_course
        (student_id, course_id) 
    values
        (?, ?)
        */
        
    }

}
