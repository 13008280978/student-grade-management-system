drop database if exists grade_center;
create database grade_center default character set utf8mb4 collate utf8mb4_general_ci;
use grade_center;

create table sys_role (
    role_id int primary key auto_increment,
    role_code varchar(24) not null unique,
    role_title varchar(50) not null
);

create table sys_account (
    account_id int primary key auto_increment,
    login_name varchar(32) not null unique,
    pass_digest varchar(64) not null,
    mail_address varchar(96) not null unique,
    mobile varchar(20),
    role_id int not null,
    disabled tinyint not null default 0,
    joined_on date not null,
    constraint fk_account_role foreign key(role_id) references sys_role(role_id)
);

create table student_profile (
    student_id int primary key auto_increment,
    account_id int,
    student_no varchar(24) not null unique,
    full_name varchar(40) not null,
    gender varchar(8) not null,
    class_name varchar(40) not null,
    enrollment_year int not null,
    archived tinyint not null default 0,
    constraint fk_student_account foreign key(account_id) references sys_account(account_id) on delete set null
);

create table course_catalog (
    course_id int primary key auto_increment,
    course_code varchar(24) not null unique,
    course_name varchar(60) not null,
    credit decimal(3,1) not null,
    teacher_name varchar(40) not null
);

create table score_record (
    score_id int primary key auto_increment,
    student_id int not null,
    course_id int not null,
    term_label varchar(20) not null,
    score_value decimal(5,2) not null,
    remark varchar(120),
    created_at datetime not null,
    constraint fk_score_student foreign key(student_id) references student_profile(student_id) on delete cascade,
    constraint fk_score_course foreign key(course_id) references course_catalog(course_id) on delete cascade,
    constraint uk_student_course_term unique(student_id, course_id, term_label)
);

insert into sys_role(role_code, role_title) values
('manager', '教务管理员'),
('learner', '学生用户');

insert into sys_account(login_name, pass_digest, mail_address, mobile, role_id, disabled, joined_on) values
('teacher', sha2('123456', 256), 'teacher@grade.com', '13600000000', (select role_id from sys_role where role_code='manager'), 0, curdate()),
('stu1001', sha2('123456', 256), 'stu1001@grade.com', '13700000001', (select role_id from sys_role where role_code='learner'), 0, curdate()),
('stu1002', sha2('123456', 256), 'stu1002@grade.com', '13700000002', (select role_id from sys_role where role_code='learner'), 0, curdate());

insert into student_profile(account_id, student_no, full_name, gender, class_name, enrollment_year, archived) values
((select account_id from sys_account where login_name='stu1001'), '202601001', '陈雨', '女', '软件工程 1 班', 2026, 0),
((select account_id from sys_account where login_name='stu1002'), '202601002', '周航', '男', '软件工程 1 班', 2026, 0);

insert into course_catalog(course_code, course_name, credit, teacher_name) values
('JAVA-WEB', 'JSP 程序设计', 3.0, '刘老师'),
('DB-BASE', '数据库原理', 3.5, '王老师'),
('OO-JAVA', '面向对象程序设计', 4.0, '赵老师');

insert into score_record(student_id, course_id, term_label, score_value, remark, created_at) values
((select student_id from student_profile where student_no='202601001'), (select course_id from course_catalog where course_code='JAVA-WEB'), '2026-春', 88.50, '课堂表现稳定', now()),
((select student_id from student_profile where student_no='202601001'), (select course_id from course_catalog where course_code='DB-BASE'), '2026-春', 91.00, '实验完成度高', now()),
((select student_id from student_profile where student_no='202601002'), (select course_id from course_catalog where course_code='JAVA-WEB'), '2026-春', 76.00, '需加强项目实践', now());
