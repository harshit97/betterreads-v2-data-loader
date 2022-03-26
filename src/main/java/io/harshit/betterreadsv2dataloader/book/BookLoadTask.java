package io.harshit.betterreadsv2dataloader.book;

import io.harshit.betterreadsv2dataloader.author.AuthorById;
import io.harshit.betterreadsv2dataloader.author.AuthorByIdRepo;
import lombok.Value;

import java.text.MessageFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Value
public class BookLoadTask implements Runnable {

    BookByIdRepo bookByIdRepo;
    AuthorByIdRepo authorByIdRepo;
    List<BookById> bookByIdList;

    @Override
    public void run() {
        System.out.println(MessageFormat.format("Saving STARTED for thread {0} at time {1}",
                Thread.currentThread().getName(), LocalTime.now(ZoneId.of("Asia/Kolkata"))
                        .truncatedTo(ChronoUnit.SECONDS)
        ));

        for (BookById bookById : bookByIdList) {
            List<String> authorIdList =
                    Optional.ofNullable(bookById.getAuthorIds()).orElse(new ArrayList<>()).stream()
                            .filter(authorId -> Objects.nonNull(authorId) && !authorId.isEmpty() && !"#".equals(authorId))
                            .collect(Collectors.toList());
            if (authorIdList.isEmpty()) {
                continue;
            }

            List<AuthorById> authorByIdList = authorByIdRepo.findByIdIn(authorIdList);

            if (authorByIdList.isEmpty()) {
                continue;
            }

            List<String> newAuthorIds = new ArrayList<>();
            List<String> newAuthorNames = new ArrayList<>();
            for (AuthorById authorById : authorByIdList) {
                newAuthorIds.add(authorById.getAuthorId());
                newAuthorNames.add(authorById.getAuthorName());
            }
            bookById.setAuthorNames(newAuthorNames);
            bookById.setAuthorIds(newAuthorIds);

            bookByIdRepo.save(bookById);
        }

        System.out.println(MessageFormat.format("==== === Saving finished for thread {0} at time {1}",
                Thread.currentThread().getName(), LocalTime.now(ZoneId.of("Asia/Kolkata"))
                        .truncatedTo(ChronoUnit.SECONDS)
        ));
    }

}
