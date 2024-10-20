This project focuses on developing a Student Admission System capable of performing CRUD operations using Java, Spring Boot, and a PostgreSQL database. I tried to implement a web application where students can enroll in new admission update your information as per your requirements. The development process strictly adhered to Test-Driven Development (TDD) practices, ensuring that tests were written before the actual code to guarantee functionality and correctness from the outset. The main aim of the project is to use TDD methodology where we write test scenarios before the method and also provide 100% code coverage on coveralls and quality of code should be passed on sonar cloud. By using TDD I implemented Unit testing, Integration testing & end-to-end testing.

[![Quality gate](https://sonarcloud.io/api/project_badges/quality_gate?project=Hamza1669_Student_Admission_TDD)](https://sonarcloud.io/summary/new_code?id=Hamza1669_Student_Admission_TDD)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=Hamza1669_Student_Admission_TDD&metric=bugs)](https://sonarcloud.io/summary/new_code?id=Hamza1669_Student_Admission_TDD)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=Hamza1669_Student_Admission_TDD&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=Hamza1669_Student_Admission_TDD)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=Hamza1669_Student_Admission_TDD&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=Hamza1669_Student_Admission_TDD)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=Hamza1669_Student_Admission_TDD&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=Hamza1669_Student_Admission_TDD)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=Hamza1669_Student_Admission_TDD&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=Hamza1669_Student_Admission_TDD)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=Hamza1669_Student_Admission_TDD&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=Hamza1669_Student_Admission_TDD)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Hamza1669_Student_Admission_TDD&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=Hamza1669_Student_Admission_TDD)
[![Java CI with Maven](https://github.com/Hamza1669/Student_Admission_TDD/actions/workflows/maven.yml/badge.svg)](https://github.com/Hamza1669/Student_Admission_TDD/actions/workflows/maven.yml)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Hamza1669_Student_Admission_TDD&metric=coverage)](https://sonarcloud.io/summary/new_code?id=Hamza1669_Student_Admission_TDD)
Coveralls Badge: [![Coverage Status](https://coveralls.io/repos/github/Hamza1669/Student_Admission_TDD/badge.svg?branch=master)](https://coveralls.io/github/Hamza1669/Student_Admission_TDD?branch=master)

- Steps to run e2e tests from Eclipse:

1. Start the PostgreSQL Docker container manually by running: mvn docker:start -Pdocker

2. Run e2e tests in eclipse Run as Junit test

3. After running the tests, stop the PostgreSQL container with: mvn docker:stop -Pdocker


- To run only e2e tests from command line: mvn clean verify -Pe2e-tests (It starts PostgreSQL Docker container runs all the e2e tests and than stop the PostgreSQL container).

- Steps to start real application from command line: 

1. Start the PostgreSQL Docker container manually by running: mvn docker:start -Pdocker

2. Start the application with PostgreSQL:mvn spring-boot:run -Pdocker



