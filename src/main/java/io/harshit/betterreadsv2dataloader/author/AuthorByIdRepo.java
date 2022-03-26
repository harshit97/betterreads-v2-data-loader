package io.harshit.betterreadsv2dataloader.author;

import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;

public interface AuthorByIdRepo extends CassandraRepository<AuthorById, String> {

    List<AuthorById> findByIdIn(List<String> ids);

}
