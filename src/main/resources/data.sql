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
/**
 * Author:  Alessandro Pedano <alessandro.pedano@transsmart.com>
 * Created: Sep 15, 2019
 */

insert into course_details(id, name, create_date, last_updated_date, is_deleted) 
 values (10001, 'Corso 10001', SYSDATE(), SYSDATE(), false);
insert into course_details(id, name, create_date, last_updated_date, is_deleted)
 values (10002, 'Corso 10002', SYSDATE(), SYSDATE(), false);
insert into course_details(id, name, create_date, last_updated_date, is_deleted)
 values (10003, 'Corso 10003', SYSDATE(), SYSDATE(), false);
insert into course_details(id, name, create_date, last_updated_date, is_deleted)
 values (10004, 'Corso 10004', SYSDATE(), SYSDATE(), false);
insert into course_details(id, name, create_date, last_updated_date, is_deleted)
 values (10005, 'Un corso', SYSDATE(), SYSDATE(), false);
insert into course_details(id, name, create_date, last_updated_date, is_deleted)
 values (10006, 'Ancora un altro corso', SYSDATE(), SYSDATE(), false);
insert into course_details(id, name, create_date, last_updated_date, is_deleted)
 values (10007, 'Sempre un altro corso', SYSDATE(), SYSDATE(), false);
insert into course_details(id, name, create_date, last_updated_date, is_deleted)
 values (10008, 'Mii quanti corsi', SYSDATE(), SYSDATE(), false);
insert into course_details(id, name, create_date, last_updated_date, is_deleted)
 values (10009, 'Basta ancora corsi', SYSDATE(), SYSDATE(), false);
insert into course_details(id, name, create_date, last_updated_date, is_deleted)
 values (10010, 'Ora me ne vado', SYSDATE(), SYSDATE(), false);
insert into course_details(id, name, create_date, last_updated_date, is_deleted)
 values (10011, 'Andato', SYSDATE(), SYSDATE(), false);

insert into passport(id, number)
 values (40001, 'N40001');
insert into passport(id, number)
 values (40002, 'N40002');
insert into passport(id, number)
 values (40003, 'N40003');

insert into student(id, name, passport_id)
 values (20001, 'Alessandro', 40001);
insert into student(id, name, passport_id)
 values (20002, 'Silvia', 40002);
insert into student(id, name, passport_id)
 values (20003, 'Pipipino', 40003);

insert into review(id, rating, description, course_id)
 values (500001, '5', 'Great course', 10001);
insert into review(id, rating, description, course_id)
 values (50002, '1' , 'Na schifezza', 10001);
insert into review(id, rating, description, course_id)
 values (50003, '4', 'Bello ma non ottimo', 10003);

insert into student_course (student_id, course_id) VALUES (20001,10001);
insert into student_course (student_id, course_id) VALUES (20002,10002);
insert into student_course (student_id, course_id) VALUES (20003,10001);
insert into student_course (student_id, course_id) VALUES (20001,10003);