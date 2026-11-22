package in.coderarmy.gatekeeper.management.config;

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
public class DataSourceConfig {

    private static final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

    @Value("${spring.datasource.url:}")
    private String configuredUrl;

    @Value("${spring.datasource.username:}")
    private String configuredUsername;

    @Value("${spring.datasource.password:}")
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

        // Check if Render / Cloud Database URL is present
        if (rawUrl != null && !rawUrl.isBlank() && !rawUrl.contains("localhost")) {
            if (rawUrl.startsWith("postgres://") || rawUrl.startsWith("postgresql://") || rawUrl.startsWith("mysql://")) {
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
                    log.error("Failed to parse cloud database URI", e);
                }
            } else {
                dataSource.setJdbcUrl(rawUrl);
                dataSource.setUsername(configuredUsername);
                dataSource.setPassword(configuredPassword);
                return dataSource;
            }
        }

        // Fallback for standalone cloud deployment (Render without external DB):
        // Automatically spins up embedded MySQL-compatible H2 database with zero external setup needed
        log.info("No external cloud database detected. Initializing embedded production-mode in-memory storage.");
        dataSource.setJdbcUrl("jdbc:h2:mem:gatekeeper_management;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1");
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        return dataSource;
    }
}
