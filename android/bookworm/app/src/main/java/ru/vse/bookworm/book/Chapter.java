package ru.vse.bookworm.book;

import android.text.Html;
import android.text.SpannableString;

public class Chapter {
    private final int order;
    private final String title;
    private final String text;

    private Chapter(Builder b) {
        this.text = b.text.toString().trim();
        this.title = b.title.toString().trim();
        this.order = b.order;
    }

    public SpannableString spanText() {
        return new SpannableString(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
    }

    public String text() {
        return text;
    }

    public String title() {
        return title;
    }

    public int order() {
        return order;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final StringBuilder text = new StringBuilder();
        private int order;
        private final StringBuilder title = new StringBuilder();

        public Chapter build() {
            return new Chapter(this);
        }

        public Builder appendText(String text) {
            this.text.append(text);
            return this;
        }

        public Builder appendTitle(String text) {
            this.title.append(text);
            return this;
        }

        public Builder setOrder(int order) {
            this.order = order;
            return this;
        }
    }
}
