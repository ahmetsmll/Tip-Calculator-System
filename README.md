# Tip Calculator System 💰

This project is a **Tip Calculation and Distribution System** developed using **Java** and **MySQL**.  
Its main purpose is to distribute tips fairly among employees based on their employment type, scores, and working information.

---

## 🚀 Technologies Used

- **Java (Swing GUI)**
- **MySQL**
- **JDBC**
- **Git & GitHub**
- **Eclipse IDE**

---

## 📌 Features

- Enter total tip amount
- Employees are classified as **Full-Time** or **Part-Time**
- Part-time employees receive tips based on:
  - Number of working days
  - Individual score
- Remaining tip amount is distributed among full-time employees proportionally to their scores
- Data is stored in a MySQL database
- User-friendly desktop interface built with Java Swing

---
Project Documentation
[Tip Calculator System – Project Report (PDF)](docs/Tip-Calculator-System-Project-Report.pdf)

## 🗄️ Database (MySQL)

Since this project uses MySQL, **database files are not included in the repository**.

Example table structure:

```sql
CREATE TABLE employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    employee_type VARCHAR(10), -- full / part
    score INT,
    work_days INT
);
