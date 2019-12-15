package it.apedano.jpahibernate;

import it.apedano.jpahibernate.entity.Course;
import it.apedano.jpahibernate.repository.CourseRepository;
import it.apedano.jpahibernate.repository.EmployeeRepository;
import it.apedano.jpahibernate.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JpaHibernateApplication implements CommandLineRunner {

    private Logger LOGGER = LoggerFactory.getLogger(JpaHibernateApplication.class);

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    StudentRepository studentRepository;
    
    @Autowired
    EmployeeRepository employeeRepository;

    public static void main(String[] args) {
        SpringApplication.run(JpaHibernateApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //studentRepository.saveStudentWithPassport();
        //courseRepository.addHardcodedReviewsToCourse();
//        List<Review> reviews = Arrays.asList(
//                new Review("Review1", "1"),
//                new Review("Review2", "2"),
//                new Review("Review3", "3")
//        );
//        courseRepository.addReviewsForCourse(10003l, reviews);
        //studentRepository.insertHardcodedStudenAndCourse();
//        employeeRepository.insert(new FullTimeEmployee("Jack", BigDecimal.valueOf(10000)));
//        employeeRepository.insert(new PartTimeEmployee("Jill", BigDecimal.valueOf(50)));
//        //LOGGER.info("Employees -> {}", employeeRepository.retrieveAll());
//        //For MappedClassSuperclass
//        LOGGER.info("PartTimeEmployees -> {}", employeeRepository.retrieveAllPartTimeEmployee());
//        LOGGER.info("FullTimeEmployees -> {}", employeeRepository.retrieveAllFullTimeEmployee());
        
/*
This method is not transactional therefore the transactions will be one for each load method
but, because of the L2 cache, the course will be retrieved from the DB only once
*/
        Course course = courseRepository.findById(10001l);
        LOGGER.info("Course retrieved first -> {}", course);
        //1785000 nanoseconds spent performing 1 L2C puts;
        
        Course course1 = courseRepository.findById(10001l);
        LOGGER.info("Course retrieved again -> {}", course1);
        //105800 nanoseconds spent performing 1 L2C hits;    
    }

}
