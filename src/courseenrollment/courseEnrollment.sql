
    -- make sure we are using ejd database
    USE ejd;

    -- delete tables if already exist
    DROP TABLE IF EXISTS Course, CourseStudent, Student;

    -- create tables
    CREATE TABLE Course
    (
        id VARCHAR(10) NOT NULL PRIMARY KEY,
        isbn VARCHAR(50) NOT NULL,
        title DOUBLE NOT NULL
    );

    CREATE TABLE Student
    (
        id VARCHAR(10) NOT NULL PRIMARY KEY,
        firstName VARCHAR(25) NOT NULL,
        lastName VARCHAR(25) NOT NULL
    );

    -- junction table
    CREATE TABLE CourseStudent
    (
        courseId VARCHAR(10) NOT NULL REFERENCES Course(id),     -- FK
        studentId VARCHAR(10) NOT NULL REFERENCES Student(id), -- FK
        PRIMARY KEY (courseId, studentId)      -- PK
    );

    -- show tables in ejd
    SHOW TABLES;

    -- populate data to tables
    INSERT INTO Course (id, isbn, title) VALUES
            ('MATH10000', 'Math for Programming', 3.0),
            ('DBAS20000', 'Advanced Database', 3.0),
            ('PROG30000', 'Enterprise Java Development', 6.0);

    INSERT INTO Student (id, firstName, lastName) VALUES
            ('000000001', 'Anna', 'Allen'),
            ('000000002', 'Bob', 'Baker'),
            ('000000003', 'Chris', 'Collins'),
            ('000000004', 'Diana', 'Davis'),
            ('000000005', 'Ed', 'Edison'),
            ('000000006', 'Frida', 'Fischer');

    INSERT INTO CourseStudent (courseId, studentId) VALUES
            ('MATH10000', '000000001'),
            ('MATH10000', '000000003'),
            ('MATH10000', '000000005'),
            ('DBAS20000', '000000002'),
            ('DBAS20000', '000000004'),
            ('DBAS20000', '000000005'),
            ('PROG30000', '000000001'),
            ('PROG30000', '000000004'),
            ('PROG30000', '000000005'),
            ('PROG30000', '000000006');


    -- queries
    SELECT firstName, lastName FROM Student
            INNER JOIN CourseStudent ON student.id = CourseStudent.studentId
            WHERE CourseStudent.courseId = 'MATH10000';

    SELECT title FROM Course
            INNER JOIN CourseStudent ON course.id = CourseStudent.courseId
            WHERE CourseStudent.studentId = '000000002';

