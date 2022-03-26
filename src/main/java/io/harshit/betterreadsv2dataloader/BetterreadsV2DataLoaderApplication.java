package io.harshit.betterreadsv2dataloader;

import io.harshit.betterreadsv2dataloader.author.AuthorLoader;
import io.harshit.betterreadsv2dataloader.connection.DataStaxAstraProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.nio.file.Path;

@SpringBootApplication
@EnableConfigurationProperties(DataStaxAstraProperties.class)
public class BetterreadsV2DataLoaderApplication {

    @Autowired
    private AuthorLoader authorLoader;

	public static void main(String[] args) {
		SpringApplication.run(BetterreadsV2DataLoaderApplication.class, args);
	}

    @PostConstruct
    public void start() {
        authorLoader.loadAuthors();
        //initAuthors();
        //initWorks();
    }


}
