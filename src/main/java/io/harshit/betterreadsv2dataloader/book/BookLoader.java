package io.harshit.betterreadsv2dataloader.book;

import io.harshit.betterreadsv2dataloader.author.AuthorByIdRepo;
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
public class BookLoader {


    @Autowired
    private BookByIdRepo bookByIdRepo;

    @Autowired
    private AuthorByIdRepo authorByIdRepo;

    @Autowired
    private BookParser bookParser;

    @Value("${data.dump.location.book}")
    private String bookDumpLocation;

    private static final int BOOK_BATCH_SIZE = 1000;

    private static final Integer THREAD_POOL_SIZE = 2000;

    private static ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public void loadBooks() {
        Path path = Paths.get(bookDumpLocation);
        List<BookById> bookByIdList = new ArrayList<>();
        try {
            LineIterator bookLineIterator = FileUtils.lineIterator(path.toFile(), "UTF-8");
            while (bookLineIterator.hasNext()) {
                String bookLine = bookLineIterator.nextLine();
                BookById bookById = bookParser.getBookByIdFromLine(bookLine);
                if (Objects.nonNull(bookById)) {
                    bookByIdList.add(bookById);
                }
                if (bookByIdList.size() == BOOK_BATCH_SIZE) {
                    executorService.submit(new BookLoadTask(bookByIdRepo, authorByIdRepo, bookByIdList));
                    bookByIdList = new ArrayList<>();
                }
            }

            if (!bookByIdList.isEmpty()) {
                executorService.submit(new BookLoadTask(bookByIdRepo, authorByIdRepo, bookByIdList));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
