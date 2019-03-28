package pl.coderstrust.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import pl.coderstrust.database.FileHelper;
import org.springframework.scheduling.annotation.EnableAsync;
import pl.coderstrust.database.InFileDataBase;

@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class})
@EnableAsync
public class ApplicationConfiguration {

    @Primary
    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return mapper;
    }

    @Bean
    public FileHelper getFileHelper() {
        // TODO: 28/03/2019  path to app properies
        return new FileHelper("src/test/resources/inFileDatabaseTest.txt");
    }

    @Primary
    @Bean
    public InFileDataBase getInFileDatabase() {
        // TODO: 28/03/2019  path to app properies
        String inFileDatabasePath = "src/test/resources/inFileDatabaseTest.txt";
        return new InFileDataBase(inFileDatabasePath);
    }
}
