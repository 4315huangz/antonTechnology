<a name="readme-top"></a>

<!-- PROJECT LOGO -->
<div align="center">
  <img src="images/Anton_Logo.png" alt="Logo" width="100" height="80">
    <H3 align="center">Anton Technology Business Platform</H3>
</div>

<!-- TABLE OF CONTENTS -->
<details>
    <summary>Table of Contents</summary>
    <li><a href="#about-the-project">About The Project</a></li>
    <li><a href="#database-functionality">Database Functionality</a></li>
    <li><a href="#built-with">Built With</a></li>
    <li><a href="#getting-started">Getting Started</a></li>
    <li><a href="#contributing">Contributing</a></li>
</details>


<!-- ABOUT THE PROJECT -->
## About The Project
Anton Technology Business Platform is a web application designed to facilitate interaction between Chinese suppliers and
potential clients interested in entering the US market. The platform offers three primary functionalities:
* Product Database and Supplier Information: Users can access a comprehensive database of products from various 
suppliers or manufacturers, along with background information about the suppliers.

* Supplier/OEM Selection: Users can search, filter, and explore different suppliers or OEMs based on their products, 
helping them find potential matches for their requirements in the US market.

* Consulting Service Request: Users can express interest in specific products or suppliers by requesting consulting 
services from Anton Technology directly through the app, fostering seamless communication for further assistance and business relationship development.
<p align="right">(<a href="#readme-top">back to top</a>)</p>


## Database Functionality
* User Types: Users can be categorized as OEMs or suppliers, with the UI tailored based on their type.
* OEM Functionality: OEMs can search by industry to find available products. They can request more details about any product, which will notify Anton Technology.
* Supplier Functionality: Suppliers can post, update, or delete product information (excluding price) to avoid conflict.
<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Built With
* Java
* Spring Framework (Spring Boot, Spring MVC, Spring Data JPA)
* Hibernate
* Postgresql
* Maven
* Junit
* Flyway
* Mockito
* JWT
* SLF4J
* AWS
<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- GETTING STARTED -->
## Getting Started
To run the Anton Technology Business Platform locally, follow these steps:

1. Clone the repository
   ```sh
   git clone https://github.com/4315huangz/AntonTechnology.git
   ```
2. Import the project into your preferred IDE.
3. Ensure that you have PostgreSQL installed locally and create a database named 'antontech_db'.
4. Set up your database configuration either programmatically or through environment variables.
5. Build the project using Maven:
   ```sh
   mvn clean install
   ```
6. Run the Flyway migrations to set up the database schema:
   ```sh
   mvn flyway:migrate
   ```
7. Run the application:
   ```sh
   mvn spring-boot:run
   ```
<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- CONTRIBUTING -->
## Contributing
Contributions are welcome! If you find any bugs or have suggestions for improvements, please open an issue or create a pull request.
<p align="right">(<a href="#readme-top">back to top</a>)</p>
