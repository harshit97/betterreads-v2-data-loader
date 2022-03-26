package io.harshit.betterreadsv2dataloader;

import io.harshit.betterreadsv2dataloader.connection.DataStaxAstraProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.nio.file.Path;

@SpringBootApplication
@EnableConfigurationProperties(DataStaxAstraProperties.class)
public class BetterreadsV2DataLoaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(BetterreadsV2DataLoaderApplication.class, args);
	}

    @Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties dataStaxAstraProperties) {
        Path bundle = dataStaxAstraProperties.getSecureConnectBundle().toPath();
        return builder -> builder.withCloudSecureConnectBundle(bundle);
    }
}
