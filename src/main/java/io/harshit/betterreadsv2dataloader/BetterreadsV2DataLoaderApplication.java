package io.harshit.betterreadsv2dataloader;

import io.harshit.betterreadsv2dataloader.author.AuthorLoader;
import io.harshit.betterreadsv2dataloader.book.BookLoader;
import io.harshit.betterreadsv2dataloader.connection.DataStaxAstraProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableConfigurationProperties(DataStaxAstraProperties.class)
public class BetterreadsV2DataLoaderApplication {

    @Autowired
    private AuthorLoader authorLoader;

    @Autowired
    private BookLoader bookLoader;

	public static void main(String[] args) {
		SpringApplication.run(BetterreadsV2DataLoaderApplication.class, args);
	}

    @PostConstruct
    public void start() {
        //authorLoader.loadAuthors();
        bookLoader.loadBooks();
    }


}
