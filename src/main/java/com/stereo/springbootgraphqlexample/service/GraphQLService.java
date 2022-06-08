package com.stereo.springbootgraphqlexample.service;

import com.stereo.springbootgraphqlexample.model.Book;
import com.stereo.springbootgraphqlexample.repository.BookRepository;
import com.stereo.springbootgraphqlexample.service.datafetcher.AllBooksDataFetcher;
import com.stereo.springbootgraphqlexample.service.datafetcher.BookDataFetcher;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

@Service
public class GraphQLService {

    GraphQL graphQL;

    @Value("classpath:books.graphql")
    Resource resource;

    @Autowired
    private AllBooksDataFetcher allBooksDataFetcher;

    @Autowired
    private BookDataFetcher bookDataFetcher;

    @Autowired
    BookRepository bookRepository;

    @PostConstruct
    private void loadSchema() throws IOException {

        loadDataIntoHSQL();

        File schemaFile = resource.getFile();
        TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(schemaFile);
        RuntimeWiring wiring = buildRuntimeWiring();
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, wiring);
        graphQL = GraphQL.newGraphQL(schema).build();
    }

    private void loadDataIntoHSQL() {
        Stream.of(
                new Book("123", "Book 1", "ABC", new String[] {"XYZ"}, "10/10/2022"),
                new Book("456", "Book 2", "DEF", new String[] {"KLM"}, "10/10/2022"),
                new Book("789", "Book 3", "HIJ", new String[] {"NOP"}, "10/10/2022")
        ).forEach(book ->
                bookRepository.save(book));
    }

    private RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring ->
                    typeWiring
                            .dataFetcher("allBooks", allBooksDataFetcher)
                            .dataFetcher("book", bookDataFetcher))
                .build();
    }

    public GraphQL getGraphQL() {
        return graphQL;
    }
}
