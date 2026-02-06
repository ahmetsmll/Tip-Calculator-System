
CREATE DATABASE IF NOT EXISTS tip_calculator;


USE tip_calculator;


CREATE TABLE IF NOT EXISTS employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    is_full_time BOOLEAN NOT NULL,
    points INT NOT NULL,
    days_worked INT DEFAULT NULL 
);