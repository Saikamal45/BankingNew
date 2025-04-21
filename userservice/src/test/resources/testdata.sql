CREATE table IF NOT EXISTS user(
id INT AUTO_INCREMENT PRIMARY KEY,
first_name varchar(50),
last_name varchar(50),
email varchar(50),
password varchar(50),
phone_number varchar(50)
);

INSERT INTO user(id,first_name,last_name,email,phone_number,password)
VALUES(1,'sai','kamal','sai@gmail.com','7894561230','sai@123');