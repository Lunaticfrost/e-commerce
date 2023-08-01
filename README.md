# Spring Boot E-commerce Backend
This project is a Spring Boot application that simulates a basic e-commerce backend. It provides endpoints for managing products, orders, and customers. The application uses MySQL as the database for storing product information, order details, and customer data.

#Prerequisites
-Java Development Kit (JDK) 8 or higher
-Eclipse IDE for Java Developers (https://www.eclipse.org/downloads/packages/)
-MySQL database (version 5.7 or higher)

# Setup Instructions
- 1. Clone the Repository
      git clone https://github.com/your-username/spring-boot-e-commerce.git
      cd spring-boot-e-commerce
- 2. Import Project into Eclipse
      -Open Eclipse IDE.
      -Go to File -> Import.
      -Select Existing Maven Projects.
      -Browse to the root directory of the cloned project (spring-boot-e-commerce) and click Finish.
      -Eclipse will import the project and download its dependencies.
-3. Configure MySQL Database
      -Create a new MySQL database for the project.
      -Update the database configuration in src/main/resources/application.properties with your MySQL credentials and the database name.
-4. Run the Spring Boot Application
      Right-click on the project in Eclipse.
      Select Run As -> Java Application.
      The application should start, and you can access it at http://localhost:8084.
-5. Usage
      - Postman json file is included in the repo.
      - Use a tool like Postman or cURL to interact with the RESTful API endpoints for managing products, orders, and customers.

-6. Troubleshooting
      -If you encounter any issues during the setup or while running the application, contact me.
      -Check the database connection and credentials in the application.properties file.
