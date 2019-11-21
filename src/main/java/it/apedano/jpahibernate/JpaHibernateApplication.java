package it.apedano.jpahibernate;

import it.apedano.jpahibernate.entity.Review;
import it.apedano.jpahibernate.repository.CourseRepository;
import it.apedano.jpahibernate.repository.StudentRepository;
import java.util.Arrays;
import java.util.List;
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

    public static void main(String[] args) {
        SpringApplication.run(JpaHibernateApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //studentRepository.saveStudentWithPassport();
        //courseRepository.addHardcodedReviewsToCourse();
        List<Review> reviews = Arrays.asList(
                new Review("Review1", "1"),
                new Review("Review2", "2"),
                new Review("Review3", "3")
        );
        courseRepository.addReviewsForCourse(10003l, reviews);
    }

}
