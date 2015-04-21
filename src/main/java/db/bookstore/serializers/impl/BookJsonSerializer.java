package db.bookstore.serializers.impl;

import db.bookstore.dao.Book;
import db.bookstore.dao.impl.DefaultAuthor;
import db.bookstore.dao.impl.DefaultBook;
import db.bookstore.serializers.Serializer;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vio on 18.04.2015.
 */
@Component
public class BookJsonSerializer implements Serializer<Book> {

    private final static Logger log = LoggerFactory.getLogger(BookJsonSerializer.class);


    private static class JsonAuthor {
        @JsonProperty
        public int id;

        @JsonProperty
        public String name;

        @JsonProperty
        public String birthDate;

        @JsonProperty
        public String deathDate;

        @JsonCreator
        public JsonAuthor(@JsonProperty("id") int id,
                          @JsonProperty("name") String name,
                          @JsonProperty("birthDate") String birthDate,
                          @JsonProperty("deathDate") String deathDate) {
            this.id = id;
            this.name = name;
            this.birthDate = birthDate;
            this.deathDate = deathDate;
        }

        @Override
        public String toString() {
            return "JsonAuthor{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", birthDate=" + birthDate +
                    ", deathDate=" + deathDate +
                    '}';
        }
    }

    private static class JsonBook {
        @JsonProperty
        public int id;

        @JsonProperty
        public String name;

        @JsonProperty
        public double price;

        @JsonProperty
        public String publicationDate;

        @JsonProperty
        public List<JsonAuthor> authors;

        @JsonCreator
        public JsonBook(@JsonProperty("id") int id,
                        @JsonProperty("name") String name,
                        @JsonProperty("price") double price,
                        @JsonProperty("publicationDate") String publicationDate,
                        @JsonProperty("authors") List<JsonAuthor> authors) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.publicationDate = publicationDate;
            this.authors = authors;
        }

        @Override
        public String toString() {
            return "JsonBook{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", price=" + price +
                    ", publicationDate=" + publicationDate +
                    ", authors=" + authors +
                    '}';
        }
    }

    @Override
    public void serialize(@NotNull Collection<Book> elements, @NotNull String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
        try {
            log.info("All books serialization to file {} is started", fileName);
            mapper.writerWithType(new TypeReference<Collection<JsonBook>>() {
            })
                    .writeValue(
                            new File(fileName),
                            elements.stream().map(b ->
                                    new JsonBook(
                                            b.getId(),
                                            b.getName(),
                                            b.getPrice(),
                                            b.getPublicationDate().toString(),
                                            b.getAuthors().stream().map(a ->
                                                    new JsonAuthor(
                                                            a.getId(),
                                                            a.getName(),
                                                            a.getBirthDate().toString(),
                                                            a.getDeathDate() != null ? a.getDeathDate().toString() : null
                                                    )).collect(Collectors.toList())
                                    )).collect(Collectors.toList()));
            log.info("All books serialization to file {} is finished", fileName);
        } catch (IOException e) {
            log.error("Serialization error is occurred.", e);
        }
    }

    @NotNull
    @Override
    public List<Book> deserialize(@NotNull String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            log.info("All books deserialization from file {} is started", fileName);
            List<JsonBook> jsonBooks = mapper.readValue(new File(fileName), new TypeReference<List<JsonBook>>() {
            });
            List<Book> books = jsonBooks.stream().map(b -> new DefaultBook(
                    b.id,
                    b.name,
                    b.price,
                    new DateTime(b.publicationDate),
                    b.authors.stream().map(a -> new DefaultAuthor(
                            a.id,
                            a.name,
                            new DateTime(a.birthDate),
                            a.deathDate != null ? new DateTime(a.deathDate) : null
                    )).collect(Collectors.toList())
            )).collect(Collectors.toList());
            log.info("All books deserialization from file {} is finished", fileName);
            return books;
        } catch (IOException e) {
            log.error("Serialization error is occurred. Returnng empty list", e);
            return Collections.emptyList();
        }
    }
}
