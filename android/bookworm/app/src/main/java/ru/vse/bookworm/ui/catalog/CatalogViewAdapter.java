package ru.vse.bookworm.ui.catalog;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import ru.vse.bookworm.R;
import ru.vse.bookworm.repository.BookRepository;
import ru.vse.bookworm.book.BookInfo;
import ru.vse.bookworm.ui.reader.ReaderActivity;
import ru.vse.bookworm.utils.Json;

public class CatalogViewAdapter extends RecyclerSwipeAdapter<CatalogViewAdapter.ViewHolder> {
    private final BookRepository bookRepository;
    private final Context context;
    private final List<BookInfo> storedBooks;
    private final SearchFilter searchFilter;
    private List<BookInfo> filteredBooks;


    public CatalogViewAdapter(Context context,
                              BookRepository bookRepository,
                              List<BookInfo> storedBooks) {
        this.context = context;
        this.bookRepository = bookRepository;
        this.storedBooks = storedBooks;
        filteredBooks = new ArrayList<>(storedBooks);
        searchFilter = new SearchFilter();
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.fragment_catalog, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        var bookInfo = filteredBooks.get(position);
        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        viewHolder.author.setText(bookInfo.author());
        viewHolder.title.setText(bookInfo.title());
        String progress = String.valueOf(bookInfo.progress()) + '%';
        viewHolder.progress.setText(progress);
        viewHolder.bookLayout.setOnClickListener(view -> {
            var toast = Toast.makeText(context, "Загружаем: " + bookInfo.title(), Toast.LENGTH_SHORT);
            toast.show();
            var bundle = new Bundle();
            bundle.putString("bookInfo", Json.toJson(bookInfo));
            var intent = new Intent(context, ReaderActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtras(bundle);
            startActivity(context, intent, bundle);
        });
        viewHolder.btnDelete.setOnClickListener(view -> {
            var removed = filteredBooks.get(position);
            mItemManger.removeShownLayouts(viewHolder.swipeLayout);
            bookRepository.markAsDeleted(removed.id());
            filteredBooks.remove(position);
            storedBooks.removeIf(x -> Objects.equals(x.id(), removed.id()));
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, filteredBooks.size());
            mItemManger.closeAllItems();
        });
        String tgGroup = bookInfo.telegramGroup();
        if (tgGroup != null) {
            viewHolder.cover.setImageResource(R.drawable.ic_telegram_group_cover);
            viewHolder.source.setText(tgGroup);
        } else {
            viewHolder.cover.setImageResource(R.drawable.ic_book_cover);
            viewHolder.source.setText("личная");
        }

        mItemManger.bind(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return filteredBooks.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.book_swipe;
    }

    public SearchFilter getSearchFilter() {
        return searchFilter;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final SwipeLayout swipeLayout;
        final Button btnDelete;
        final TextView author;
        final TextView title;
        final TextView progress;
        final ImageView cover;
        final TextView source;
        final LinearLayout bookLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            swipeLayout = itemView.findViewById(R.id.book_swipe);
            btnDelete = itemView.findViewById(R.id.btn_book_delete);
            author = itemView.findViewById(R.id.book_author);
            title = itemView.findViewById(R.id.book_title);
            progress = itemView.findViewById(R.id.book_progress);
            cover = itemView.findViewById(R.id.book_cover);
            source = itemView.findViewById(R.id.book_source);
            bookLayout = itemView.findViewById(R.id.book_layout);
        }
    }

    public class SearchFilter {
        private List<BookInfo> matched;

        public void performFiltering(CharSequence constraint) {
            if (constraint == null || constraint.length() == 0) {
                matched = storedBooks;
            } else {
                matched = new ArrayList<>(storedBooks.size());
                String text = constraint.toString().toLowerCase();
                for (BookInfo book : storedBooks) {
                    if (book.author() != null && book.author().toLowerCase().contains(text)
                            || book.title() != null && book.title().toLowerCase().contains(constraint)
                            || book.telegramGroup() != null && book.telegramGroup().toLowerCase().contains(text)) {
                        matched.add(book);
                    }
                }
            }
            done();
        }

        public void done() {
            if (matched != null) {
                filteredBooks = matched;
                matched = null;
                notifyDatasetChanged();
                mItemManger.closeAllItems();
            }
        }
    }

    public void invalidate(List<BookInfo> newBooks) {
        storedBooks.clear();
        storedBooks.addAll(newBooks);
        filteredBooks = newBooks;
        notifyDatasetChanged();
        mItemManger.closeAllItems();
    }

    public void updateBookState(List<BookInfo> allBooks) {
        storedBooks.clear();
        storedBooks.addAll(allBooks);
        var filteredIds = filteredBooks.stream()
                .map(BookInfo::id)
                .collect(Collectors.toSet());
        filteredBooks =  allBooks.stream()
                .filter(x -> filteredIds.contains(x.id()))
                .collect(Collectors.toList());
        notifyDatasetChanged();
        mItemManger.closeAllItems();
    }
}
