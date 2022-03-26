package io.harshit.betterreadsv2dataloader.author;

import org.springframework.data.cassandra.repository.CassandraRepository;

public interface AuthorByIdRepo extends CassandraRepository<AuthorById, String> {



}
