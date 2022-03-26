package io.harshit.betterreadsv2dataloader.book;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookByIdRepo extends CassandraRepository<BookById, String> {
}
