package com.employe.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
public class DatabaseConfig {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConfig.class);

    @Value("${spring.datasource.url:}")
    private String rawUrl;

    @Value("${spring.datasource.username:}")
    private String defaultUsername;

    @Value("${spring.datasource.password:}")
    private String defaultPassword;

    @Bean
    @Primary
    public DataSource dataSource() {
        // Check environment variable DATABASE_URL first (Render/Neon standard), then spring.datasource.url
        String envDbUrl = System.getenv("DATABASE_URL");
        String connectionUrl = (envDbUrl != null && !envDbUrl.isBlank()) ? envDbUrl.trim() : rawUrl.trim();

        String jdbcUrl;
        String username = defaultUsername;
        String password = defaultPassword;

        // Check if user passed postgresql:// or postgres:// URI (common in Neon / Render)
        if (connectionUrl.startsWith("postgresql://") || connectionUrl.startsWith("postgres://")) {
            try {
                URI uri = new URI(connectionUrl);
                String host = uri.getHost();
                int port = uri.getPort() == -1 ? 5432 : uri.getPort();
                String path = uri.getPath(); // e.g. /neondb
                String query = uri.getQuery(); // e.g. sslmode=require

                String userInfo = uri.getUserInfo();
                if (userInfo != null && userInfo.contains(":")) {
                    String[] parts = userInfo.split(":", 2);
                    username = parts[0];
                    password = parts[1];
                }

                StringBuilder jdbcBuilder = new StringBuilder();
                jdbcBuilder.append("jdbc:postgresql://").append(host).append(":").append(port).append(path);
                if (query != null && !query.isBlank()) {
                    jdbcBuilder.append("?").append(query);
                } else {
                    jdbcBuilder.append("?sslmode=require");
                }
                jdbcUrl = jdbcBuilder.toString();
                log.info("Detected Neon/Postgres URI format. Converted to JDBC URL: jdbc:postgresql://{}:{}{}", host, port, path);
            } catch (Exception e) {
                log.warn("Failed to parse PostgreSQL URI, falling back to raw URL: {}", e.getMessage());
                jdbcUrl = connectionUrl.replaceFirst("^(postgresql|postgres)://", "jdbc:postgresql://");
            }
        } else {
            jdbcUrl = connectionUrl;
        }

        // Ensure sslmode=require is present if connecting to neon.tech
        if (jdbcUrl.contains("neon.tech") && !jdbcUrl.contains("sslmode=")) {
            jdbcUrl += (jdbcUrl.contains("?") ? "&" : "?") + "sslmode=require";
        }

        log.info("Configuring HikariDataSource with JDBC URL (host): {}", maskJdbcUrl(jdbcUrl));

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("org.postgresql.Driver");

        // Pool configurations for Neon serverless Postgres
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000); // 30 seconds
        config.setIdleTimeout(600000); // 10 minutes
        config.setMaxLifetime(1800000); // 30 minutes

        return new HikariDataSource(config);
    }

    private String maskJdbcUrl(String url) {
        if (url == null) return "null";
        // Hide sensitive query parameters or userInfo if any
        return url.replaceAll("://[^@]+@", "://***:***@");
    }
}
