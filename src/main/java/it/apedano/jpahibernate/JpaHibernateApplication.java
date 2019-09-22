package it.apedano.jpahibernate;

import it.apedano.jpahibernate.entity.Course;
import it.apedano.jpahibernate.repository.CourseRepository;
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

    public static void main(String[] args) {
        SpringApplication.run(JpaHibernateApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Course course = courseRepository.findById(10001l);
        LOGGER.info("Course loaded from db: {}", course);
    }

}
