INSERT INTO room(name) VALUES ('Phelps 2510');
INSERT INTO room(name) VALUES('Phelps 3526');
INSERT INTO room(name) VALUES('CSIL');
INSERT INTO course_offering (course_id,quarter,instructor_name, instructor_email) VALUES ('CMPSC 16','F19','Mirza', 'mirza@ucsb.edu');
INSERT INTO course_offering (course_id,quarter,instructor_name, instructor_email) VALUES ('CMPSC 160','F19','Ding', 'ding@ucsb.edu');
INSERT INTO course_offering (course_id,quarter,instructor_name, instructor_email) VALUES ('CMPSC 130A','W20','Koc', 'koc@ucsb.edu');
INSERT INTO course_offering (course_id,quarter,instructor_name, instructor_email) VALUES ('CMPSC 130B','W20','Lokshtanov', 'lokshtanov@ucsb.edu');
INSERT INTO tutor (first_name,last_name, email, is_active,number_of_courses_assigned) VALUES ('Scott','Chow','scottpchow@example.org', 1,0);
INSERT INTO tutor (first_name,last_name, email, is_active,number_of_courses_assigned) VALUES ('Zach','Sisco','zachsisco@example.org', 1,0);
INSERT INTO tutor (first_name,last_name, email, is_active,number_of_courses_assigned) VALUES ('Yinon','Rousso','yinonRousso@example.org', 1,0);
INSERT INTO tutor (first_name,last_name, email, is_active,number_of_courses_assigned) VALUES ('Kate','Perkins','kateperkins@example.org', 1,0);
INSERT INTO tutor (first_name,last_name, email, is_active,number_of_courses_assigned) VALUES ('George','Kripac','Georgekripac@example.org', 1,0);
INSERT INTO tutor (first_name,last_name, email, is_active,number_of_courses_assigned) VALUES ('Aaron','Huang','aaronhuang@ucsb.edu', 1,0);

INSERT INTO tutor_assignment(course_offering_id, tutor_id, is_course_lead) VALUES (1, 1, 0);