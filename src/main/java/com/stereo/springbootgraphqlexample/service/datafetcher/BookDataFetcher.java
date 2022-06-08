package com.stereo.springbootgraphqlexample.service.datafetcher;

import com.stereo.springbootgraphqlexample.model.Book;
import com.stereo.springbootgraphqlexample.repository.BookRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookDataFetcher implements DataFetcher<Book> {

    @Autowired
    BookRepository bookRepository;

    @Override
    public Book get(DataFetchingEnvironment environment) throws Exception {
        String isn = environment.getArgument("id");
        return bookRepository.findById(isn).get();
    }
}
