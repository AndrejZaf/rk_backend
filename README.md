<h1 style="font-weight: bold; text-align:center;">RareKickz Backend 👟</h1>

<p style="text-align:center;">
  <a href="#tech">Technologies</a> •
  <a href="#started">Getting Started</a> •
  <a href="#routes">API Endpoints</a> •
  <a href="#arch">Architecture</a>
</p>

<p style="text-align:center;">
    <b>Backend e-commerce application consisting of inventory management, order management and payment APIs.</b>
</p>

<h2 id="technologies">💻 Technologies</h2>

- [![Java](https://img.shields.io/badge/Java-%23ED8B00.svg?logo=openjdk&logoColor=white)](#)
- [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&logoColor=fff)](#)
- [![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=fff)](#)
- [![Stripe](https://img.shields.io/badge/Stripe-5851DD?logo=stripe&logoColor=fff)](#)

<h2 id="started">🚀 Getting started</h2>
<h3>Prerequisites</h3>

- [Java 17](https://adoptium.net/temurin/releases/?version=17)
- [Docker](https://www.docker.com/products/docker-desktop/)
- [Maven](https://maven.apache.org/download.cgi)

<h3>Environment Variables</h3>

- For NgRok it's required for you to create an account and add the token as an environment variable `NG_ROK_TOKEN` which
  will be loaded in the docker-compose.yml.
- For the payment service, you will need to add your Stripe token as an environment variable `STRIPE_API_KEY`
- The payment service contains a callback endpoint environment variable `WEBHOOK_CALLBACK_URL` which needs to be
  replaced with the NgRok URL once that's generated for testing purposes, or the location of the service once the
  application has been deployed.

<h3>Starting the Project</h3>
Clone the project, and run the docker-compose.yml file located in the `~/local` folder. This will allow you to bootstrap
some of the project dependencies such as keycloak, ngrok, mailhog and the database.

Once that's done execute the following maven command in order to download the necessary dependencies.

```
mvn clean install
```

<h2 id="routes">📍 API Endpoints</h2>

The project includes Swagger documentation to help you explore and understand the available APIs in detail. To access
the Swagger UI for a specific service, navigate to the service's URL followed by /swagger/index.html. For example, if
your service is hosted at http://localhost:8080, you would visit http://localhost:8080/swagger-ui/index.html. Once
there, you'll find a comprehensive interface that lists all the endpoints, along with their request and response models.
This interactive documentation allows you to test API calls directly from your browser, view parameter requirements, and
examine response formats, making it an invaluable resource for both development and integration efforts.

<h2 id="arch">⛩️ Architecture</h2>
<h3>Services</h3>
The backend application adopts a microservices architecture, with gRPC facilitating efficient communication between the
services. This approach ensures high performance, low latency, and type-safe interactions, leveraging Protocol Buffers
for serialization to optimize data exchange across the microservices.

- Inventory Service
- Order Service
- Payment Service
- Notification Service
- Gateway Service
- Registry Service