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
public class PartTimeEmployee extends Employee {

    
    private BigDecimal hourlyWage;

    protected PartTimeEmployee(){}
    
    public PartTimeEmployee(String name, BigDecimal hourlyWage) {
        super(name);
        this.hourlyWage = hourlyWage;
    }
    
}
