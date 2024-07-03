package com.collaberadigital.librarysystem.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * Configuration class for OpenAPI documentation.
 * This class defines the information and servers for the Library Management System API.
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Library Management System API",
                description = "This API provides endpoints for managing a library system.",
                summary = "API for managing books, borrowers, and borrowings in a library system.",
                version = "1.0",
                contact = @io.swagger.v3.oas.annotations.info.Contact(name = "Udara Liyanage",
                        email = "udara.wikum@gmail.com"),
                license = @io.swagger.v3.oas.annotations.info.License(name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html")
        ),
        servers = {
                @Server(
                        description = "dev",
                        url = "http://localhost:8080/"
                ),
                @Server(
                        description = "prod",
                        url = "http://localhost:8080/"
                )}

)
public class OpenApiConfig {

}
