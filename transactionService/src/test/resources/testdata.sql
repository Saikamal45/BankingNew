CREATE TABLE IF NOT EXISTS transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    account_id INT,
    amount DECIMAL(10,2),
    transaction_created_at TIMESTAMP,
    transaction_status VARCHAR(50),
    transaction_type VARCHAR(50)
);


INSERT INTO transactions (account_id, amount, transaction_created_at, transaction_status, transaction_type)
VALUES (1, 1000, NOW(), 'SUCCESS', 'DEPOSIT');
