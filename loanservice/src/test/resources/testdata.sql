-- Drop tables if they exist
DROP TABLE IF EXISTS loan_details;
DROP TABLE IF EXISTS loan_application;

-- Create LoanApplication table
CREATE TABLE loan_application (
    loan_application_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    account_id INT NOT NULL,
    loan_type VARCHAR(50),
    loan_amount DOUBLE,
    loan_status VARCHAR(50),
    loan_issue_date DATE
);

-- Create LoanDetails table
CREATE TABLE loan_details (
    loan_details_id INT PRIMARY KEY AUTO_INCREMENT,
    interest_rate DOUBLE,
    tenure INT,
    emi_amount DOUBLE,
    loan_application_id INT UNIQUE NOT NULL,
    CONSTRAINT fk_loan_application
        FOREIGN KEY (loan_application_id)
        REFERENCES loan_application(loan_application_id)
        ON DELETE CASCADE
);

-- Insert sample data into loan_application
INSERT INTO loan_application (user_id, account_id, loan_type, loan_amount, loan_status, loan_issue_date)
VALUES 
(101, 2001, 'HOMELOAN', 500000.00, 'PENDING', '2024-12-01'),
(102, 2002, 'PERSONALOAN', 150000.00, 'APPROVED', '2024-11-15'),
(103, 2003, 'CARLOAN', 150000.00, 'APPROVED', '2024-11-15');

-- Insert sample data into loan_details
INSERT INTO loan_details (interest_rate, tenure, emi_amount, loan_application_id)
VALUES 
(7.2, 240, 4500.00, 1),
(10.0, 60, 3200.00, 2),
(10.0, 60, 3000.00, 3);
