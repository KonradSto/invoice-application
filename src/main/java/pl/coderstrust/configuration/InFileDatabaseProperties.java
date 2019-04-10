package pl.coderstrust.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "pl.coderstrust.database.in-file")
public class InFileDatabaseProperties {
    private String filePath;

    // TODO: 10/04/2019 Component
    @Bean
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
