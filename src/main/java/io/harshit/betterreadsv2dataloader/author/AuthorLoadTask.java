package io.harshit.betterreadsv2dataloader.author;

import lombok.Value;

import java.text.MessageFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Value
public class AuthorLoadTask implements Runnable {

    AuthorByIdRepo authorByIdRepo;

    List<AuthorById> authorList;

    @Override
    public void run() {
        System.out.println(MessageFormat.format("Saving STARTED for thread {0} at time {1}",
                Thread.currentThread().getName(), LocalTime.now(ZoneId.of("Asia/Kolkata"))
                        .truncatedTo(ChronoUnit.SECONDS)
        ));
        authorByIdRepo.saveAll(authorList);
        System.out.println(MessageFormat.format("==== === Saving finished for thread {0} at time {1}",
                Thread.currentThread().getName(), LocalTime.now(ZoneId.of("Asia/Kolkata"))
                        .truncatedTo(ChronoUnit.SECONDS)
        ));
    }
}
