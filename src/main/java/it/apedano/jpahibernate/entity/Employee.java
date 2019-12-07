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
package it.apedano.jpahibernate.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author Alessandro Pedano <alessandro.pedano@transsmart.com>
 */
@MappedSuperclass //the entity cannot be 
/*
* <p> A class designated with the <code>MappedSuperclass</code> 
 * annotation can be mapped in the same way as an entity except that the 
 * mappings will apply only to its subclasses since no table 
 * exists for the mapped superclass itself. When applied to the 
 * subclasses the inherited mappings will apply in the context 
 * of the subclass tables. Mapping information may be overridden 
 * in such subclasses by using the <code>AttributeOverride</code> and 
 * <code>AssociationOverride</code> annotations or corresponding XML elements.
 */
 /*
The Employee is just a common place for common properties
separate tables are created for each entity (subclass). It is as say that there is no real hiearchical relationship between subclasses
STRUCTURE

create table full_time_employee (
       id bigint not null,
        name varchar(255) not null,
        salary decimal(19,2),
        primary key (id)
    )

create table part_time_employee (
       id bigint not null,
        name varchar(255) not null,
        hourly_wage decimal(19,2),
        primary key (id)
    )

INSERTION

insert 
    into
        full_time_employee
        (name, salary, id) 
    values
        (?, ?, ?)


RETRIEVE (two separate queries on both tables)

select
        parttimeem0_.id as id1_2_,
        parttimeem0_.name as name2_2_,
        parttimeem0_.hourly_wage as hourly_w3_2_ 
    from
        part_time_employee parttimeem0_

PROS
- No inheritance is represented in the database, entities are totally separated
(can be an advantage if the application requires this feature)

CONS
-  We cannot query using the superclass beacuse it is not an entity we can use to 
query instances

 */

//@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //default strategy
//@DiscriminatorColumn(name = "EmployeeType") //used as name of the DTYPE column (EMPLOYEE_TYPE in the database)
/**
 * A strategy in which fields that are specific to a subclass are mapped to a
 * separate table than the fields that are common to the parent class, and a
 * join is performed to instantiate the subclass.
 */
/*
The DTYPE (Discriminatory tipe) column is added with the name of the subclass Entity
The table contains all columns of all subclas

SHOW COLUMNS FROM EMPLOYEE;
FIELD  	TYPE  	NULL  	KEY  	DEFAULT  
DTYPE	VARCHAR(31)	NO		NULL
ID	BIGINT(19)	NO	PRI	NULL
NAME	VARCHAR(255)	NO		NULL
SALARY	DECIMAL(19)	YES		NULL
HOURLY_WAGE	DECIMAL(19)	YES		NULL

DTYPE                   ID  	NAME  	SALARY  	HOURLY_WAGE  
FullTimeEmployee	1	Jack	10000.00	null
PartTimeEmployee	2	Jill	null            50.00

INSERT
insert 
    into
        employee
        (name, salary, dtype, id) 
    values
        (?, ?, 'FullTimeEmployee', ?)

Retrive all (very simple query)
select
        employee0_.id as id2_1_,
        employee0_.name as name3_1_,
        employee0_.salary as salary4_1_,
        employee0_.hourly_wage as hourly_w5_1_,
        employee0_.dtype as dtype1_1_ 
    from
        employee employee0_

PROS
- semplicity and performance

CONS
- data integrity is not well represented because it is only due to nullable (and null values) in the table 
 It's easy to break data integrity (use JOINED method if data integrity is relevant)
- if the hierarchy is large and variable, the single table will be very large
 */
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) //one table per concrete class, no class per the abstract superclass
/**
 * A strategy in which fields that are specific to a subclass are mapped to a
 * separate table than the fields that are common to the parent class, and a
 * join is performed to instantiate the subclass.
 */
/*
STRUCTURE
all commons columns are repeated in all tables 
create table full_time_employee (
       id bigint not null,
        name varchar(255) not null,
        salary decimal(19,2),
        primary key (id)
    )

create table part_time_employee (
       id bigint not null,
        name varchar(255) not null,
        hourly_wage decimal(19,2),
        primary key (id)
    )

INSERT
insert 
    into
        part_time_employee
        (name, hourly_wage, id) 
    values
        (?, ?, ?)

insert 
    into
        full_time_employee
        (name, salary, id) 
    values
        (?, ?, ?)

RETRIEVE ALL - UNION
 select
        employee0_.id as id1_1_,
        employee0_.name as name2_1_,
        employee0_.salary as salary1_2_,
        employee0_.hourly_wage as hourly_w1_3_,
        employee0_.clazz_ as clazz_ 
    from
        ( select
            id,
            name,
            salary,
            null as hourly_wage,
            1 as clazz_ 
        from
            full_time_employee 
        union
        all select
            id,
            name,
            null as salary,
            hourly_wage,
            2 as clazz_ 
        from
            part_time_employee 
    ) employee0_

PROS
- easy to insert, performance


CONS
- common columns are repeated (if they are many the repetition will be a lot of reduncancy)
- vary bad table design

 */


//@Inheritance(strategy = InheritanceType.JOINED)
/**
 * A strategy in which fields that are specific to a subclass are mapped to a
 * separate table than the fields that are common to the parent class, and a
 * join is performed to instantiate the subclass.
 */
/*
STRUCTURE
 - parent table with all common fields
create table employee (
       id bigint not null,
        name varchar(255) not null,
        primary key (id)
    )
- subclasses with concrete entity fields
 create table full_time_employee (
       salary decimal(19,2),
        id bigint not null,
        primary key (id)

alter table full_time_employee 
       add constraint FKhkidbx7pliabdmr4wycrog3mg 
       foreign key (id) 
       references employee

 create table part_time_employee (
       hourly_wage decimal(19,2),
        id bigint not null,
        primary key (id)
    )

alter table part_time_employee 
       add constraint FKfan9lj9g0g880a30ca7bitghi 
       foreign key (id) 
       references employee

INSERTION

insert 
    into
        employee
        (name, id) 
    values
        (?, ?)

insert 
    into
        full_time_employee
        (salary, id) 
    values
        (?, ?)

RETRIEVE ALL -> OUTER JOINS 

 select
        employee0_.id as id1_1_,
        employee0_.name as name2_1_,
        employee0_1_.salary as salary1_2_,
        employee0_2_.hourly_wage as hourly_w1_3_,
        case 
            when employee0_1_.id is not null then 1 
            when employee0_2_.id is not null then 2 
            when employee0_.id is not null then 0 
        end as clazz_ 
    from
        employee employee0_ 
    left outer join
        full_time_employee employee0_1_ 
            on employee0_.id=employee0_1_.id 
    left outer join
        part_time_employee employee0_2_ 
            on employee0_.id=employee0_2_.id

PROS
- better data structure
- data integrity is better (instead of the SINGLE TABLE method)

CONS
- data retrieval requires a JOIN (even multiple if the query is on the parent class) that could be a performance issue

 */
public abstract class Employee implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", nullable = false, unique = false)
    protected String name;

    protected Employee() {
    }

    public Employee(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Employee{" + "id=" + id + ", name=" + name + '}';
    }

}
