package ru.vse.bookworm.book;

import android.util.Base64;
import android.util.Log;
import android.util.Xml;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Fb2Parser implements Parser {
    private static final String LOG_TAG = Fb2Parser.class.getSimpleName();
    private static final Parser INSTANCE = new Fb2Parser();

    public static Parser instance() {
        return INSTANCE;
    }

    @Override
    public Book.Builder parse(InputStream inputStream) {
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, null);
            var eventType = parser.getEventType();
            var ctx = new Context();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    ctx.startEvent(parser);
                } else if (eventType == XmlPullParser.END_TAG) {
                    ctx.endEvent();
                } else if (eventType == XmlPullParser.TEXT) {
                    ctx.textEvent(parser);
                }
                eventType = parser.next();
            }

            return ctx.bookBuilder();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Binary.Builder parseBinaryAttrs(XmlPullParser parser) {
        return Binary.builder()
                .setContentType(parser.getAttributeValue(null, "content-type"))
                .setId(parser.getAttributeValue(null, "id"));
    }

    private static Note.Builder parseNoteAttrs(XmlPullParser parser) {
        return Note.builder()
                .setId(parser.getAttributeValue(null, "id"));
    }

    private static boolean hasNotes(XmlPullParser parser) {
        return "notes".equals(parser.getAttributeValue("", "name"));
    }

    private static String parseLink(XmlPullParser parser) {
        var noteId = parser.getAttributeValue(null, "xlink:href");
        if (noteId == null) {
            noteId = parser.getAttributeValue(null, "l:href");
        }

        return noteId == null ? "<a>" : "<a href='" + noteId + "'>";
    }

    private static String parseImageLink(XmlPullParser parser) {
        var src = parser.getAttributeValue(null, "xlink:href");
        if (src == null) {
            src = parser.getAttributeValue(null, "l:href");
        }
        if (src == null) {
            src = parser.getAttributeValue(null, "href");
        }

        return src == null ? "<image>" : "<image src='" + src + "'>";
    }

    private static class Context {
        private final Book.Builder book = Book.builder();
        private final Deque<Tag> tags = new LinkedList<>();
        private final Deque<Chapter.Builder> chapterStack = new LinkedList<>();
        private final List<Chapter.Builder> chapterList = new ArrayList<>();
        private Binary.Builder binary;
        private Note.Builder note;

        void startEvent(XmlPullParser parser) {
            String tagName = parser.getName();
            Log.d(LOG_TAG, "startEvent: " + tagName);

            Tag tag = Tag.byName(tagName);
            switch (tag) {
                case BODY:
                    if (hasNotes(parser)) {
                        tag = Tag.NOTES;
                    } else {
                        beginChapter();
                    }
                    break;
                case SECTION:
                    if (hasTag(Tag.NOTES)) {
                        note = parseNoteAttrs(parser);
                    } else {
                        beginChapter();
                    }
                    break;
                case BINARY:
                    binary = parseBinaryAttrs(parser);
                    break;
                case A:
                    appendText(parseLink(parser));
                    break;
                case IMAGE:
                    appendText(parseImageLink(parser));
                    break;
                case TITLE:
                    appendTitle(tag.prefixHtml());
                    break;
                default:
                    if (tag.prefixHtml() != null) {
                        if (hasTag(Tag.NOTES) && hasTag(Tag.SECTION)) {
                            note.append(tag.prefixHtml());
                        } else if (hasTag(Tag.TITLE)) {
                            appendTitle(tag.prefixHtml());
                        } else {
                            appendText(tag.prefixHtml());
                        }
                    }
                    break;
            }
            tags.push(tag);
        }

        private void beginChapter() {
            var cpt = Chapter.builder()
                    .setOrder(chapterList.size());
            chapterStack.push(cpt);
            chapterList.add(cpt);
        }

        private void appendText(String str) {
            var cpt = chapterStack.peek();
            if (cpt != null) {
                cpt.appendText(str);
            }
        }

        private void appendTitle(String str) {
            var cpt = chapterStack.peek();
            if (cpt != null) {
                cpt.appendTitle(str);
            }
        }

        void endEvent() {
            var tag = tags.poll();
            Log.d(LOG_TAG, "endEvent: " + tag);
            if (tag == null) {
                return;
            }
            if (tag.postfixHtml() != null) {
                if (hasTag(Tag.NOTES) && hasTag(Tag.SECTION)) {
                    note.append(tag.postfixHtml());
                } else if (hasTag(Tag.TITLE) || tag == Tag.TITLE) {
                    appendTitle(tag.postfixHtml());
                } else {
                    appendText(tag.postfixHtml());
                }
            }
            switch (tag) {
                case BINARY:
                    book.addBinary(binary.build());
                    break;
                case SECTION:
                    if (hasTag(Tag.NOTES)) {
                        book.addNote(note.build());
                    } else {
                        chapterStack.poll();
                    }
                    break;
                case BODY:
                    chapterStack.poll();
                    for (var chapter : chapterList) {
                        book.addChapter(chapter.build());
                    }
                    break;
            }
        }

        void textEvent(XmlPullParser parser) {
            var tagText = parser.getText();
            var tagType = tags.peek();
            Log.d(LOG_TAG, "textEvent: " + tagType);
            if (tagType != null) {
                switch (tagType) {
                    case BOOK_TITLE:
                        if (hasTag(Tag.DESCRIPTION)) {
                            book.setTitle(tagText);
                        }
                        break;
                    case FIRST_NAME:
                        if (tagBefore() == Tag.AUTHOR) {
                            book.setFirstName(tagText);
                        }
                        break;
                    case MIDDLE_NAME:
                        if (tagBefore() == Tag.AUTHOR) {
                            book.setMiddleName(tagText);
                        }
                        break;
                    case LAST_NAME:
                        if (tagBefore() == Tag.AUTHOR) {
                            book.setLastName(tagText);
                        }
                        break;
                    case BINARY:
                        binary.setData(decodeBase64(tagText));
                        break;
                    case TITLE:
                        appendTitle(tagText);
                        break;
                    case STYLESHEET:
                    case GENRE:
                    case LANG:
                    case SRC_LANG:
                    case KEYWORDS:
                        break;
                    default:
                        if (hasTag(Tag.NOTES) && hasTag(Tag.SECTION)) {
                            note.append(tagText);
                        } else if (hasTag(Tag.TITLE)) {
                            appendTitle(tagText);
                        } else {
                            appendText(tagText);
                        }
                        break;
                }
            }
        }

        @Nullable
        private byte[] decodeBase64(String text) {
            try {
                return Base64.decode(text, Base64.DEFAULT);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Couldn't decode base64 text", e);
                return null;
            }
        }

        @SuppressWarnings("SameParameterValue")
        private boolean hasTag(Tag tagType) {
            for (var each : tags) {
                if (each == tagType) {
                    return true;
                }
            }
            return false;
        }

        @Nullable
        private Tag tagBefore() {
            var it = tags.iterator();
            return it.hasNext() && it.next() != null && it.hasNext()
                    ? it.next()
                    : null;
        }

        Book.Builder bookBuilder() {
            return book;
        }
    }

    private enum Tag {
        UNUSED(null),
        LANG("lang"),
        SRC_LANG("src-lang"),
        KEYWORDS("keywords"),
        GENRE("genre"),
        FICTION_BOOK("FictionBook"),
        STYLESHEET("stylesheet"),
        DESCRIPTION("description"),
        AUTHOR("author"),
        BOOK_TITLE("book-title"),
        FIRST_NAME("first-name"),
        MIDDLE_NAME("middle-name"),
        LAST_NAME("last-name"),
        BINARY("binary"),
        NOTES(null),
        SECTION("section"),
        BODY("body"),
        BOOK_NAME("book-name", "<br><div style=\"text-align:center;\"><b>", "</b></div>"),
        PUBLISHER("publisher", "<div style=\"text-align:center;\">", "</div>"),
        CITY("city", "<div style=\"text-align:center;\">", "</div>"),
        YEAR("year", "<div style=\"text-align:center;\">", "</div>"),
        ISBN("isbn", "<div style=\"text-align:center;\">", "</div>"),
        TITLE("title", "<div style=\"text-align:center;\"><h3>", "</h3></div>"),
        EMPHASIS("emphasis", "<em>", "</em>"),
        STRONG("strong", "<strong>", "</strong>"),
        STRIKETHROUGH("strikethrough", "<strike>", "</strike>"),
        STYLE("style", "<i>", "</i>"),
        SUB("sub", "<sub>", "</sub>"),
        SUP("sup", "<sup>", "</sup>"),
        EMPTY_LINE("empty-line", "<br>", ""),
        STANZA("stanza", "<br><div class=\"stanza\"", "</div>"),
        V("v", "<br>", ""),
        A("a", null, "</a>"),
        P("p", "<p><div style=\"text-align:justify;\">", "</div></p>"),
        CITE("cite", "<div style=\"text-align:right;\"><i>", "</i></div>"),
        IMAGE("image", null, "</image>");

        private static final Map<String, Tag> byName = Stream.of(Tag.values())
                .filter(x -> x.tagName != null)
                .collect(Collectors.toMap(Tag::tagName, x -> x));
        private final String tagName;
        private final String prefixHtml;
        private final String postfixHtml;

        Tag(String tag) {
            this(tag, null, null);
        }

        Tag(@NonNull String tag, @Nullable String prefixHtml, @Nullable String postfixHtml) {
            this.tagName = tag;
            this.prefixHtml = prefixHtml;
            this.postfixHtml = postfixHtml;
        }

        @NonNull
        public String tagName() {
            return tagName;
        }

        @Nullable
        public String prefixHtml() {
            return prefixHtml;
        }

        @Nullable
        public String postfixHtml() {
            return postfixHtml;
        }

        @NonNull
        public static Tag byName(String tagName) {
            var tag = byName.get(tagName);
            return tag == null ? UNUSED : tag;
        }
    }
}
