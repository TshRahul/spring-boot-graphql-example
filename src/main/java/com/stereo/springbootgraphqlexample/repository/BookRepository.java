package com.stereo.springbootgraphqlexample.repository;

import com.stereo.springbootgraphqlexample.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String> {
}
