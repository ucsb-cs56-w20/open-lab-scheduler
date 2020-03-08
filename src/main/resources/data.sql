INSERT INTO room(name) VALUES ('PHELPS 2510');
INSERT INTO room(name) VALUES ('PHELPS 3526');
INSERT INTO room(name) VALUES ('PHELPS 3525');
INSERT INTO room_availability(quarter, start_time, end_time, day, room_id) VALUES ('F19', '800', '930', 'W', '1');
INSERT INTO room_availability(quarter, start_time, end_time, day, room_id) VALUES ('F19', '1000', '1030', 'W', '1');
INSERT INTO room_availability(quarter, start_time, end_time, day, room_id) VALUES ('W20', '1100', '1130', 'T', '2');
INSERT INTO room_availability(quarter, start_time, end_time, day, room_id) VALUES ('W20', '1200', '1239', 'M', '3');
INSERT INTO course_offering (course_id,quarter,instructor_name, instructor_email) VALUES ('CMPSC 16','F19','Mirza', 'mirza@ucsb.edu');
INSERT INTO course_offering (course_id,quarter,instructor_name, instructor_email) VALUES ('CMPSC 160','F19','Ding', 'ding@ucsb.edu');
INSERT INTO course_offering (course_id,quarter,instructor_name, instructor_email) VALUES ('CMPSC 130A','W20','Koc', 'koc@ucsb.edu');
INSERT INTO course_offering (course_id,quarter,instructor_name, instructor_email) VALUES ('CMPSC 130B','W20','Lokshtanov', 'lokshtanov@ucsb.edu');
INSERT INTO tutor (first_name,last_name, email, is_active,number_of_courses_assigned) VALUES ('Scott','Chow','scottpchow@example.org', 1,0);
INSERT INTO tutor (first_name,last_name, email, is_active,number_of_courses_assigned) VALUES ('Zach','Sisco','zachsisco@example.org', 1,0);
INSERT INTO tutor (first_name,last_name, email, is_active,number_of_courses_assigned) VALUES ('Yinon','Rousso','yinonRousso@example.org', 1,0);
INSERT INTO tutor (first_name,last_name, email, is_active,number_of_courses_assigned) VALUES ('Kate','Perkins','kateperkins@example.org', 1,0);
INSERT INTO tutor (first_name,last_name, email, is_active,number_of_courses_assigned) VALUES ('George','Kripac','Georgekripac@example.org', 1,0);
INSERT INTO tutor_assignment(course_offering_id, tutor_id, is_course_lead) VALUES (1, 1, 0);
INSERT INTO time_slot(room_availability_id, start_time, end_time) VALUES ('1', '800', '830');
INSERT INTO time_slot(room_availability_id, start_time, end_time) VALUES ('1', '830', '900');
INSERT INTO time_slot(room_availability_id, start_time, end_time) VALUES ('2', '1100', '1130');
INSERT INTO time_slot(room_availability_id, start_time, end_time) VALUES ('2', '1130', '1200');
INSERT INTO time_slot(room_availability_id, start_time, end_time) VALUES ('2', '1200', '1230');
INSERT INTO time_slot(room_availability_id, start_time, end_time) VALUES ('3', '1200', '1230');
INSERT INTO time_slot(room_availability_id, start_time, end_time) VALUES ('3', '1230', '1300');
INSERT INTO time_slot(room_availability_id, start_time, end_time) VALUES ('3', '1300', '1330');
INSERT INTO time_slot(room_availability_id, start_time, end_time) VALUES ('3', '1330', '1400');
INSERT INTO time_slot(room_availability_id, start_time, end_time) VALUES ('3', '1400', '1430');
INSERT INTO time_slot(room_availability_id, start_time, end_time) VALUES ('3', '1430', '1500');
<<<<<<< HEAD
INSERT INTO time_slot_assignment(tutor_id, time_slot_id, course_offering_id) VALUES (2,1,1);
INSERT INTO time_slot_assignment(tutor_id, time_slot_id, course_offering_id) VALUES (2,2,1);
INSERT INTO time_slot_assignment(tutor_id, time_slot_id, course_offering_id) VALUES (2,3,1);
INSERT INTO time_slot_assignment(tutor_id, time_slot_id, course_offering_id) VALUES (3,4,2);
INSERT INTO time_slot_assignment(tutor_id, time_slot_id, course_offering_id) VALUES (3,5,2);
INSERT INTO time_slot_assignment(tutor_id, time_slot_id, course_offering_id) VALUES (3,6,2);

INSERT INTO room_availability(quarter, start_time, end_time, day, room_id) VALUES ('W20', 1000, 1200, 'Monday', 1);
INSERT INTO room_availability(quarter, start_time, end_time, day, room_id) VALUES ('W20', 1000, 1200, 'Monday', 2);

INSERT INTO time_slot (room_availability_id, start_time, end_time) VALUES (1, 1000, 1200);
INSERT INTO time_slot (room_availability_id, start_time, end_time) VALUES (2, 1000, 1200);

INSERT INTO time_slot_assignment(time_slot_id, tutor_id, course_offering_id) VALUES (1, 4, 4);
INSERT INTO time_slot_assignment(time_slot_id, tutor_id, course_offering_id) VALUES (1, 1, 3);
INSERT INTO time_slot_assignment(time_slot_id, tutor_id, course_offering_id) VALUES (1, 2, 3);
INSERT INTO time_slot_assignment(time_slot_id, tutor_id, course_offering_id) VALUES (1, 3, 4);
INSERT INTO time_slot_assignment(time_slot_id, tutor_id, course_offering_id) VALUES (2, 4, 3);
=======
INSERT INTO time_slot_assignment(tutor_id, time_slot_id, course_offering_id) VALUES (1,8,2);
INSERT INTO time_slot_assignment(tutor_id, time_slot_id, course_offering_id) VALUES (2,1,1);
INSERT INTO time_slot_assignment(tutor_id, time_slot_id, course_offering_id) VALUES (2,2,2);
INSERT INTO time_slot_assignment(tutor_id, time_slot_id, course_offering_id) VALUES (2,3,1);
INSERT INTO time_slot_assignment(tutor_id, time_slot_id, course_offering_id) VALUES (2,4,2);
INSERT INTO time_slot_assignment(tutor_id, time_slot_id, course_offering_id) VALUES (1,5,1);
INSERT INTO time_slot_assignment(tutor_id, time_slot_id, course_offering_id) VALUES (1,6,2);
INSERT INTO time_slot_assignment(tutor_id, time_slot_id, course_offering_id) VALUES (1,7,1);
>>>>>>> bq - working towards hotspots table

