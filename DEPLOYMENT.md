# Deployment Guide

This guide provides instructions for deploying the BatchFlow application to a production-like environment.

## Prerequisites

- A server with Java 17, Maven, Node.js, and npm installed.
- A production database (e.g., PostgreSQL).
- A reverse proxy / web server (e.g., Nginx).

## 1. Backend Deployment

### 1.1. Configure the Database

1.  Open the `src/main/resources/application.properties` file.
2.  Comment out the H2 database configuration.
3.  Add the configuration for your production PostgreSQL database.

    ```properties
    # spring.datasource.url=jdbc:h2:mem:batchflowdb
    # ... (other H2 properties)

    # PostgreSQL Configuration
    spring.datasource.url=jdbc:postgresql://<your-db-host>:5432/<your-db-name>
    spring.datasource.username=<your-db-username>
    spring.datasource.password=<your-db-password>
    spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

    # It is highly recommended to use a database migration tool like Flyway or Liquibase for production schemas.
    # For production, you should set ddl-auto to 'validate' or 'none'.
    spring.jpa.hibernate.ddl-auto=update
    ```

### 1.2. Build the Application

1.  Navigate to the project root directory.
2.  Run the Maven package command to build the executable JAR file.
    ```bash
    mvn clean package
    ```
3.  The JAR file will be created in the `target/` directory (e.g., `batchflow-0.0.1-SNAPSHOT.jar`).

### 1.3. Run the Application

1.  Copy the JAR file to your deployment server.
2.  Run the application using the `java -jar` command.
    ```bash
    java -jar target/batchflow-0.0.1-SNAPSHOT.jar
    ```
3.  The backend server will now be running on port 8080.

## 2. Frontend Deployment

### 2.1. Build the Static Files

1.  Navigate to the `frontend` directory.
2.  Install the dependencies: `npm install`.
3.  Run the build script:
    ```bash
    npm run build
    ```
4.  This will create a `build` directory inside `frontend` containing the optimized, static HTML, CSS, and JavaScript files for the application.

## 3. Nginx Configuration (Recommended)

It is recommended to use a web server like Nginx to serve the static frontend files and act as a reverse proxy for the backend API. This allows you to serve the entire application under a single domain.

1.  Copy the contents of the `frontend/build` directory to your web server's root directory (e.g., `/var/www/batchflow`).
2.  Configure Nginx. Below is a sample configuration file.

    ```nginx
    server {
        listen 80;
        server_name your-domain.com;

        # Path to the static frontend files
        root /var/www/batchflow;
        index index.html;

        location / {
            # Fallback to index.html for single-page application routing
            try_files $uri /index.html;
        }

        # Reverse proxy for API requests
        location /api/ {
            proxy_pass http://localhost:8080;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        }

        # Reverse proxy for WebSocket connections
        location /ws/ {
            proxy_pass http://localhost:8080;
            proxy_http_version 1.1;
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header Connection "Upgrade";
            proxy_set_header Host $host;
        }
    }
    ```

3.  Reload the Nginx configuration: `sudo systemctl reload nginx`.

Your application should now be accessible at `http://your-domain.com`.
