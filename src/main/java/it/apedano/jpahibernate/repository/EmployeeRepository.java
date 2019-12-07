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

import it.apedano.jpahibernate.entity.Employee;
import it.apedano.jpahibernate.entity.FullTimeEmployee;
import it.apedano.jpahibernate.entity.PartTimeEmployee;
import java.util.List;
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
@Transactional
public class EmployeeRepository {

    private final Logger LOGGER = LoggerFactory.getLogger(EmployeeRepository.class);

    @Autowired
    EntityManager em;

    public Employee findById(Long id) {
        return em.find(Employee.class, id);
    }
    
    public void insert(Employee employee) {
        em.persist(employee);
    }
    
    public List<Employee> retrieveAll() {
        return em.createQuery("select e from Employee e", Employee.class).getResultList();
    }
    
    /**
     * Only for @MappedSuperclass annotation in the entity
     * Since the Employee is not an entity we cannot use the Employee class in the EM
     * We need to retrieve all subclasses one by one
     * 
     */
    public List<PartTimeEmployee> retrieveAllPartTimeEmployee() {
        return em.createQuery("select pte from PartTimeEmployee pte", PartTimeEmployee.class).getResultList();
    }
    
    public List<FullTimeEmployee> retrieveAllFullTimeEmployee() {
        return em.createQuery("select pte from FullTimeEmployee pte", FullTimeEmployee.class).getResultList();
    }
    

}
