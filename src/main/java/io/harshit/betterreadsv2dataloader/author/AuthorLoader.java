package io.harshit.betterreadsv2dataloader.author;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class AuthorLoader {

    @Autowired
    private AuthorByIdRepo authorByIdRepo;

    @Autowired
    private AuthorParser authorParser;

    @Value("${data.dump.location.author}")
    private String authorDumpLocation;

    private static final int AUTHOR_BATCH_SIZE = 1000;

    private static final Integer THREAD_POOL_SIZE = 1500;

    private static ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public void loadAuthors() {
        log.debug("Inside loadAuthors");
        Path path = Paths.get(authorDumpLocation);
        List<AuthorById> authorByIdList = new ArrayList<>();
        try {
            LineIterator authorLineIterator = FileUtils.lineIterator(path.toFile(), "UTF-8");
            while (authorLineIterator.hasNext()) {
                String authorLine = authorLineIterator.nextLine();
                AuthorById authorById = authorParser.getAuthorByIdFromLine(authorLine);
                if (Objects.nonNull(authorById)) {
                    authorByIdList.add(authorById);
                }
                if (authorByIdList.size() == AUTHOR_BATCH_SIZE) {
                    executorService.submit(new AuthorLoadTask(authorByIdRepo, authorByIdList));
                    authorByIdList = new ArrayList<>();
                }
            }

            if (!authorByIdList.isEmpty()) {
                executorService.submit(new AuthorLoadTask(authorByIdRepo, authorByIdList));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
