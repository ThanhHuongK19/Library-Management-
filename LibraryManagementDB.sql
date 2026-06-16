-- DROP TABLE IF EXISTS borrow_records;
-- DROP TABLE IF EXISTS user_roles;
-- DROP TABLE IF EXISTS roles;
-- DROP TABLE IF EXISTS books;
-- DROP TABLE IF EXISTS categories;
-- DROP TABLE IF EXISTS users;

CREATE TABLE categories (
    category_id SERIAL PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE books (
    book_id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(155) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    publisher VARCHAR(155),
    publish_year INT CHECK (publish_year >= 0),
    quantity INT DEFAULT 0 CHECK (quantity >= 0),
    summary TEXT,
    category_id INT,
    CONSTRAINT fk_book_category 
        FOREIGN KEY (category_id) 
        REFERENCES categories(category_id) 
        ON DELETE SET NULL
);

CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(155),
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE roles (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE user_roles (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user 
        FOREIGN KEY (user_id) 
        REFERENCES users(user_id) 
        ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role 
        FOREIGN KEY (role_id) 
        REFERENCES roles(role_id) 
        ON DELETE CASCADE
);

CREATE TABLE borrow_records (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    book_id INT NOT NULL,
    borrow_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    due_date TIMESTAMP NOT NULL,
    return_date TIMESTAMP,
    status VARCHAR(50) DEFAULT 'BORROWED'
        CHECK (status IN ('BORROWED', 'RETURNED', 'OVERDUE')),
    notes TEXT,
    CONSTRAINT fk_borrow_user 
        FOREIGN KEY (user_id) 
        REFERENCES users(user_id) 
        ON DELETE CASCADE,
    CONSTRAINT fk_borrow_book 
        FOREIGN KEY (book_id) 
        REFERENCES books(book_id) 
        ON DELETE CASCADE
);

INSERT INTO categories (category_name, description) VALUES
('Programming', 'Books about programming languages and software development'),
('Database', 'Books about database design and management'),
('Networking', 'Books about computer networks'),
('Artificial Intelligence', 'Books about AI and machine learning'),
('Web Development', 'Books about frontend and backend web development');

INSERT INTO books 
(title, author, isbn, publisher, publish_year, quantity, summary, category_id)
VALUES
('Java Core Programming', 'Nguyen Van A', 'ISBN001', 'Tech Books', 2022, 10, 'Basic to advanced Java programming.', 1),
('Spring Boot in Action', 'Craig Walls', 'ISBN002', 'Manning', 2021, 8, 'Build web applications with Spring Boot.', 5),
('Effective Java', 'Joshua Bloch', 'ISBN003', 'Addison-Wesley', 2018, 5, 'Best practices for Java developers.', 1),
('Database System Concepts', 'Abraham Silberschatz', 'ISBN004', 'McGraw Hill', 2020, 7, 'Database theory and SQL concepts.', 2),
('PostgreSQL Guide', 'John Smith', 'ISBN005', 'Database Press', 2023, 6, 'PostgreSQL from beginner to advanced.', 2),
('Computer Networking', 'James Kurose', 'ISBN006', 'Pearson', 2021, 9, 'Networking principles and protocols.', 3),
('Artificial Intelligence Basics', 'Peter Norvig', 'ISBN007', 'AI Press', 2022, 4, 'Introduction to artificial intelligence.', 4),
('Machine Learning Fundamentals', 'Andrew Ng', 'ISBN008', 'Stanford Press', 2023, 3, 'Machine learning concepts and examples.', 4),
('HTML CSS Bootstrap', 'Tran Thi B', 'ISBN009', 'Web Books', 2022, 12, 'Frontend web development with Bootstrap.', 5),
('REST API Design', 'Le Van C', 'ISBN010', 'Tech Books', 2024, 6, 'Design RESTful APIs using Spring Boot.', 5);

INSERT INTO books (title, author, isbn, publisher, publish_year, quantity, summary, category_id) VALUES
('Clean Code', 'Robert C. Martin', '9780132350884', 'Prentice Hall', 2008, 5, 'Bí quyết viết mã nguồn sạch.', 1),
('The Pragmatic Programmer', 'Andrew Hunt', '9780201616224', 'Addison-Wesley', 1999, 3, 'Hướng dẫn lập trình thực tế.', 1),
('Design Patterns', 'Erich Gamma', '9780201633610', 'Addison-Wesley', 1994, 2, 'Các mẫu thiết kế phần mềm.', 1),
('Introduction to Algorithms', 'Thomas H. Cormen', '9780262033848', 'MIT Press', 2009, 4, 'Giáo trình giải thuật kinh điển.', 2),
('Artificial Intelligence', 'Stuart Russell', '9780136042594', 'Pearson', 2010, 3, 'Tổng quan về AI.', 2),
('Database System Concepts', 'Abraham Silberschatz', '9780073523323', 'McGraw-Hill', 2013, 6, 'Lý thuyết hệ quản trị CSDL.', 3),
('Operating System Concepts', 'Abraham Silberschatz', '9781118063330', 'Wiley', 2012, 4, 'Các khái niệm hệ điều hành.', 3),
('Computer Networks', 'Andrew S. Tanenbaum', '9780132126953', 'Prentice Hall', 2011, 2, 'Kiến thức về mạng máy tính.', 4),
('Structure and Interpretation', 'Harold Abelson', '9780262510875', 'MIT Press', 1996, 3, 'Cấu trúc và thông dịch chương trình.', 4),
('The Mythical Man-Month', 'Frederick Brooks', '9780201835958', 'Addison-Wesley', 1995, 5, 'Quản lý dự án phần mềm.', 5);

-- All password is 123456
INSERT INTO users 
(username, password, email, full_name, is_active)
VALUES
('admin', '$2y$10$qSmng7jQAsVuuCD2WqDYLONerDMwKHU1DEs3QqsZyr.0h/0eC8wiq', 'admin@gmail.com', 'System Administrator', TRUE),
('user1', '$2y$10$XT/ElIG3m0J5519twWH0yuItBkWEmAVD3RLcTY4FO.zdGF33moUrO', 'user1@gmail.com', 'Nguyen Van User', TRUE),
('user2', '$2y$10$.1kJKnRo7HsW01o1p6KqPOSnWa1hVSQBuAtxNcAA8caHMjfi7iWP.', 'user2@gmail.com', 'Tran Thi User', TRUE),
('librarian', '$2y$10$qF9pk8UnVepqWTw7tfALh.cOzO8wRQ/1p6SqOPmi7oEIZZvSdxQNu', 'librarian@gmail.com', 'Library Staff', TRUE);

INSERT INTO roles (role_name) VALUES
('ADMIN'),
('USER'),
('LIBRARIAN');

INSERT INTO user_roles (user_id, role_id) VALUES
(1, 4),
(1, 5),
(2, 5),
(3, 5),
(4, 6),
(4, 5);

INSERT INTO borrow_records 
(user_id, book_id, borrow_date, due_date, return_date, status, notes)
VALUES
(2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '7 days', NULL, 'BORROWED', 'Borrowing Java book'),
(2, 2, CURRENT_TIMESTAMP - INTERVAL '10 days', CURRENT_TIMESTAMP - INTERVAL '3 days', NULL, 'OVERDUE', 'Book is overdue'),
(3, 4, CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP + INTERVAL '2 days', NULL, 'BORROWED', 'Database book borrowed'),
(3, 6, CURRENT_TIMESTAMP - INTERVAL '12 days', CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP - INTERVAL '6 days', 'RETURNED', 'Returned on time'),
(4, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '14 days', NULL, 'BORROWED', 'API design book');

-- ---------------------------------------
select * from categories;
select * from books;
select * from users;
select * from roles;
select * from user_roles;
select * from borrow_records;

delete from roles;