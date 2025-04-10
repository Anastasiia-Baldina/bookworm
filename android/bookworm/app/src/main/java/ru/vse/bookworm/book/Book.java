package ru.vse.bookworm.book;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Book {
    private final BookInfo bookInfo;
    private final List<Binary> binaries;
    private final List<Note> notes;
    private List<Chapter> chapters;

    private Book(Builder b) {
        String author = Stream.of(b.firstName, b.middleName, b.lastName)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
        if(author.isEmpty()) {
            author = "автор неизвестен";
        }
        bookInfo = BookInfo.builder()
                .setAuthor(author)
                .setId(b.id)
                .setTitle(b.title)
                .setProgress(0)
                .setUpdateTime(Instant.now())
                .setTgGroup(null)
                .build();
        chapters = List.copyOf(b.chapters);
        binaries = List.copyOf(b.binaries);
        notes = List.copyOf(b.notes);
    }

    public BookInfo bookInfo() {
        return bookInfo;
    }

    public List<Chapter> chapters() {
        return chapters;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private List<Binary> binaries = List.of();
        private List<Note> notes = List.of();
        private List<Chapter> chapters = List.of();
        private String title = "";
        private String firstName = "";
        private String middleName = "";
        private String lastName = "";

        public Book build() {
            return new Book(this);
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder addBinary(Binary binary) {
            if (binaries.isEmpty()) {
                binaries = new ArrayList<>();
            }
            binaries.add(binary);
            return this;
        }

        public Builder addNote(Note note) {
            if (notes.isEmpty()) {
                notes = new ArrayList<>();
            }
            notes.add(note);
            return this;
        }

        public Builder addChapter(Chapter chapter) {
            if (chapters.isEmpty()) {
                chapters = new ArrayList<>();
            }
            chapters.add(chapter);
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setMiddleName(String middleName) {
            this.middleName = middleName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
    }
}
