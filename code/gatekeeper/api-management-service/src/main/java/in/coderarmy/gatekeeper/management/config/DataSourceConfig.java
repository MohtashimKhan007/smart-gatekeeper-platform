package in.coderarmy.gatekeeper.management.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url:jdbc:mysql://localhost:3306/gatekeeper_management}")
    private String configuredUrl;

    @Value("${spring.datasource.username:root}")
    private String configuredUsername;

    @Value("${spring.datasource.password:Mohtashim}")
    private String configuredPassword;

    @Bean
    @Primary
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();

        String rawUrl = System.getenv("SPRING_DATASOURCE_URL");
        if (rawUrl == null || rawUrl.isBlank()) {
            rawUrl = System.getenv("DATABASE_URL");
        }
        if (rawUrl == null || rawUrl.isBlank()) {
            rawUrl = configuredUrl;
        }

        // Support cloud PostgreSQL / MySQL URLs (e.g. postgres://user:pass@host:port/dbname or mysql://user:pass@host:port/dbname)
        if (rawUrl != null && (rawUrl.startsWith("postgres://") || rawUrl.startsWith("postgresql://") || rawUrl.startsWith("mysql://"))) {
            try {
                URI uri = new URI(rawUrl);
                String userInfo = uri.getUserInfo();
                String host = uri.getHost();
                int port = uri.getPort();
                String path = uri.getPath();

                boolean isPostgres = rawUrl.startsWith("postgres");
                int defaultPort = isPostgres ? 5432 : 3306;
                int resolvedPort = port == -1 ? defaultPort : port;

                String prefix = isPostgres ? "jdbc:postgresql://" : "jdbc:mysql://";
                String jdbcUrl = prefix + host + ":" + resolvedPort + path;
                dataSource.setJdbcUrl(jdbcUrl);

                if (userInfo != null && userInfo.contains(":")) {
                    String[] parts = userInfo.split(":", 2);
                    dataSource.setUsername(parts[0]);
                    dataSource.setPassword(parts[1]);
                } else {
                    dataSource.setUsername(configuredUsername);
                    dataSource.setPassword(configuredPassword);
                }

                dataSource.setDriverClassName(isPostgres ? "org.postgresql.Driver" : "com.mysql.cj.jdbc.Driver");
                return dataSource;
            } catch (Exception e) {
                if (!rawUrl.startsWith("jdbc:")) {
                    rawUrl = "jdbc:" + rawUrl;
                }
                dataSource.setJdbcUrl(rawUrl);
                dataSource.setUsername(configuredUsername);
                dataSource.setPassword(configuredPassword);
            }
        } else {
            dataSource.setJdbcUrl(rawUrl);
            dataSource.setUsername(configuredUsername);
            dataSource.setPassword(configuredPassword);
        }

        if (dataSource.getJdbcUrl() != null && dataSource.getJdbcUrl().startsWith("jdbc:h2:")) {
            dataSource.setDriverClassName("org.h2.Driver");
        } else if (dataSource.getJdbcUrl() != null && dataSource.getJdbcUrl().startsWith("jdbc:postgresql:")) {
            dataSource.setDriverClassName("org.postgresql.Driver");
        } else {
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        }

        return dataSource;
    }
}
