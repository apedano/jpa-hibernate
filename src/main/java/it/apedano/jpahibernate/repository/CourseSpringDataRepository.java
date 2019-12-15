/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.apedano.jpahibernate.repository;

import it.apedano.jpahibernate.entity.Course;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 *
 * @author Alessandro
 */
@RepositoryRestResource(path= "courses") //exposed as a rest resource -> http://localhost:8080/courses
public interface CourseSpringDataRepository 
        extends JpaRepository<Course, Long> {
    
    List<Course> findByName(String name);
    
    List<Course> findByNameAndId(String name, Long id);
    
    List<Course> findByNameOrderByIdDesc(String name);
    
    void deleteByName(String name);
    
    Long countByName(String name);
    
//    @Query("Select c From Course where name like 'Corso%'")
//    List<Course> coursedWithCorsoInName();
    
    @Query("Select c from Course c where c.name LIKE CONCAT('%',:course_name,'%')")
    List<Course> coursedWithTextInName(@Param("course_name") String courseName);
    
//    @Query(value = "Select * From course_details where name like 'Corso%'", nativeQuery = true)
//    List<Course> coursedWithCorsoInNameNative();
//    
    @Query(name = "get_courses_like_giocare")
    List<Course> coursedWithGiocareInName();

}
