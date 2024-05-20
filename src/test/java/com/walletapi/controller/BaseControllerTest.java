package com.walletapi.controller;

import com.walletapi.BaseTest;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class BaseControllerTest extends BaseTest {
    @Autowired
    protected MockMvc mockMvc;

    private static final String TEST_DATABASE_IMAGE = "postgres:15-alpine";
    private static final String TEST_DATABASE_NAME = "stock-compass-db";
    private static final String TEST_DATABASE_USER = "db_consumer";
    private static final String TEST_DATABASE_PWD = "db_consumer_password";

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(TEST_DATABASE_IMAGE)
            .withDatabaseName(TEST_DATABASE_NAME)
            .withUsername(TEST_DATABASE_USER)
            .withPassword(TEST_DATABASE_PWD)
            .withExposedPorts(5432);

    @BeforeAll
    static void setup(@Autowired DataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/sql/drop_tables.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/sql/init.sql"));
        }
    }

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        String jdbcUrl = String.format("jdbc:postgresql://localhost:%d/%s", 5432, TEST_DATABASE_NAME);
        registry.add("spring.datasource.url", () -> jdbcUrl);
        registry.add("spring.datasource.username", () -> TEST_DATABASE_USER);
        registry.add("spring.datasource.password", () -> TEST_DATABASE_PWD);
    }

}

