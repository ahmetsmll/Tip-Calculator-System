# Tip Calculator System 


Project Info documentation     --->    [Tip Calculator System – Project Report (PDF)](Project-Content.pdf)

##  Technologies Used

- Java (Swing GUI)
- MySQL
- JDBC
- Git & GitHub
- Apache NetBeans

---

## Project  Features 

- Enter total tip amount
- Employees are classified as **Full-Time** or **Part-Time**
- Part-time employees receive tips based on:
  - Number of working days
  - Individual score
- Remaining tip amount is distributed among full-time employees proportionally to their scores
- Data is stored in a MySQL database
- User-friendly desktop interface built with Java Swing



##  Database (MySQL)

 !!! Note : Database files are not included in the repository.
 Codes:
 
'''sql
 
CREATE DATABASE IF NOT EXISTS tip_calculator;
USE tip_calculator;

CREATE TABLE IF NOT EXISTS employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    is_full_time BOOLEAN NOT NULL,
    points INT NOT NULL,
    days_worked INT DEFAULT NULL 
);



