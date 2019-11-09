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

insert into course_details(id, name, create_date, last_updated_date) 
 values (10001, 'Corso 10001', SYSDATE(), SYSDATE());
insert into course_details(id, name, create_date, last_updated_date)
 values (10002, 'Corso 10002', SYSDATE(), SYSDATE());
insert into course_details(id, name, create_date, last_updated_date)
 values (10003, 'Corso 10003', SYSDATE(), SYSDATE());

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

insert into review(id, rating, description)
 values (500001, '5', 'Great course');
insert into review(id, rating, description)
 values (50002, '1' , 'Na schifezza');
insert into review(id, rating, description)
 values (50003, '4', 'Bello ma non ottimo');