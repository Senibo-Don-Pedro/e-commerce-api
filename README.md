# E-commerce RESTful API

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A comprehensive and secure RESTful API for a modern e-commerce platform, built with Java and the Spring Boot framework.
This project was developed based on the detailed backend project specification provided by *
*[roadmap.sh](https://roadmap.sh/projects/ecommerce-api)**.

It provides a complete backend solution, handling everything from user authentication and product management to order
processing and payment integration.

---

## ✨ Features

- **Authentication & Security:** Robust user authentication using JWT (JSON Web Tokens) and social login via OAuth2 (
  Google). Role-based access control (Admin, Customer).
- **Product Catalog Management:** Full CRUD operations for products, with dynamic searching and filtering based on name,
  category, and price range.
- **Shopping Cart:** Persistent shopping carts for users to add, view, and remove items.
- **Order Processing:** Seamlessly convert a user's cart into a permanent order in a single transaction.
- **Payment Integration:** Secure payment processing handled by integration with the **Paystack** payment gateway,
  including webhook verification for transaction status updates.
- **API Documentation:** Interactive API documentation powered by **OpenAPI 3 (Swagger)** for easy testing and
  exploration of endpoints.
- **Database Seeding:** Automatic data seeding for development environments to populate the database with mock users and
  products.

---

## 🛠️ Tech Stack

- **Backend:**
    - Java 17+
    - Spring Boot 3
    - Spring Security (for JWT & OAuth2)
    - Spring Data JPA (Hibernate)
- **Database:** MySQL
- **Authentication:** JSON Web Tokens (JWT)
- **API Documentation:** Springdoc OpenAPI (Swagger UI)
- **Utilities:**
    - Lombok
    - JavaFaker (for data seeding)
    - Maven (for dependency management)

---

## 🚀 Getting Started

To get a local copy up and running, follow these simple steps.

### Prerequisites

- JDK 17 or higher
- Maven 3.6+
- A running instance of MySQL

### Installation

1. **Clone the repository**
   ```sh
   git clone [https://github.com/Senibo-Don-Pedro/e-commerce-api.git](https://github.com/Senibo-Don-Pedro/e-commerce-api.git)
   cd e-commerce-api
   ```

2. **Set Up Environment Variables**

   This project uses an `application.yaml` file that reads configuration from environment variables. You must set the
   variables listed in the section below in your local environment for the application to run. You can do this by:
    - Setting them in your operating system.
    - Configuring them in your IDE's run configuration (e.g., in IntelliJ).
    - Using a `.env` file with a tool like the `dotenv-maven-plugin`.

3. **Run the application**
   ```sh
   mvn spring-boot:run
   ```
   The application will start on `http://localhost:4403`.

---

## ⚙️ Environment Configuration

The following environment variables must be set for the application to run correctly. The application reads these values
from the `src/main/resources/application.yaml` file.

| Variable Name              | Description                                      | Example Value                                  |
| -------------------------- | ------------------------------------------------ | ---------------------------------------------- |
| `MY_SQL_USERNAME`          | The username for your MySQL database.            | `root`                                         |
| `MY_SQL_PASSWORD`          | The password for your MySQL database.            | `yourpassword`                                 |
| `JWT_SECRET`               | A Base64 encoded secret key for signing JWTs.    | `bXktc3VwZXItc2VjcmV0LWtleS1mb3ItandrLXRlc3Rpbmc=` |
| `JWT_EXPIRATION`           | The validity period for JWTs in milliseconds.    | `1800000` (30 minutes)                         |
| `PAYSTACK_TEST_SECRET_KEY` | Your Paystack Test Secret Key.                   | `sk_test_...`                                  |
| `PAYSTACK_TEST_PUBLIC_KEY` | Your Paystack Test Public Key.                   | `pk_test_...`                                  |
| `GOOGLE_CLIENT_ID`         | Your Google OAuth2 Client ID.                    | `your-google-client-id.apps.googleusercontent.com` |
| `GOOGLE_CLIENT_SECRET`     | Your Google OAuth2 Client Secret.                | `GOCSPX-your-google-client-secret`             |

---

## 📚 API Documentation

This API is documented using OpenAPI 3. Once the application is running, you can access the interactive Swagger UI to
explore and test the endpoints.

- **Swagger UI URL:** [http://localhost:4403/swagger](http://localhost:4403/swagger)

You can use the `Register` and `Login` endpoints to get a JWT, then click the "Authorize" button in Swagger UI to use
that token for protected endpoints.

---

## 👤 Author

- **Senibo Don-Pedro**
- **GitHub:** [@Senibo-Don-Pedro](https://github.com/Senibo-Don-Pedro)
- **Email:** senibodonpedro@gmail.com
