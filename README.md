### **The Technical Documentation**

This is technical documentation to understand how it works the projects.

**1. Installation**

There are two projects that can be installed using Docker containers that include Redis, MySQL, Spring Boot backend and ReactJS frontend projects.
To build and run containers should run the command below:

`./init.sh`

or

``docker-compose up --build -d``

**2. Endpoints**

To check API and frontend projects the links below can be used.

Frontend: http://localhost:8086

Swagger API Documentation: http://localhost:8085/api/documentation

API Base URL: http://localhost:8085/api

**3. Testing**

Tests have been written for both projects.

Unit and integration tests have been written for the backend projects.

Tests will be running on docker containers. Junit, Mockito, and Rest Assured are used on the backend side.

**4. About the Project**

On the backend side, to authenticate users JWT web security is used. Users can register using their emails and then after the login part every endpoint works with the JWT token. It provides reliable security.

Unit and integration tests are written using RestAssured and Junit. The test coverage is %93 for the project according to Intelij coverage calculation.

The frontend side is easy to implement, anyone can create a wallet and perform top-up and payment actions as the documentation is written.

On the other hand, the money can be transferred using the wallet serial number. After the money transfer is completed, transactions will be seen in both wallets as payment and top-up.

Pagination is used to provide scalability on every list page and endpoint.