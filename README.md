Banking System with JDBC (Java + MySQL)

A console-based Java banking system using JDBC and MySQL for real-time account management, including account creation, deposit, withdrawal, and viewing—all data securely stored in a MySQL database.

Features
Create new bank accounts
Deposit and withdraw money
Show all accounts in the system
Persistent storage with MySQL database using JDBC

Technologies
Java
JDBC (MySQL Connector/J)
MySQL 8.0+

Setup
Create Database and Table

step1 :

sql
CREATE DATABASE bankdb;
USE bankdb;
CREATE TABLE accounts (
  accountNo INT PRIMARY KEY,
  holderName VARCHAR(100),
  type VARCHAR(20),
  balance DOUBLE,
  active BOOLEAN
);

step 2 :
Add Dependency
Download and add mysql-connector-j-9.4.0.jar to your project/module dependencies in IntelliJ.

Configure Credentials in Code

java
private final String URL = "jdbc:mysql://localhost:3306/bankdb?useSSL=false&serverTimezone=UTC";
private final String USER = "root";
private final String PASS = "yourpassword";
Run the Program
Compile and run Banking_system_with_db.java in IntelliJ IDEA.

Usage
Interact with the menu in your terminal to manage accounts.

All actions update the MySQL database instantly.

Author

Samruddha Belsare
