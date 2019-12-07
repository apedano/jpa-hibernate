/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.apedano.jpahibernate.entity;

import java.math.BigDecimal;
import javax.persistence.Entity;

/**
 *
 * @author Alessandro
 */
@Entity
public class FullTimeEmployee extends Employee {

    
    private BigDecimal salary;

    protected FullTimeEmployee(){}
    
    public FullTimeEmployee(String name, BigDecimal salary) {
        super(name);
        this.salary = salary;
    }
    
    
    
}
