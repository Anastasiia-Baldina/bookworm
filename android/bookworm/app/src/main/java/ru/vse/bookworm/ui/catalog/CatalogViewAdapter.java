package ru.vse.bookworm.ui.catalog;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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

import java.util.List;

import ru.vse.bookworm.R;
import ru.vse.bookworm.db.DatabaseHelper;
import ru.vse.bookworm.repository.BookRepository;
import ru.vse.bookworm.repository.DbBookRepository;
import ru.vse.bookworm.book.BookInfo;
import ru.vse.bookworm.ui.reader.ReaderActivity;
import ru.vse.bookworm.utils.Json;

public class CatalogViewAdapter extends RecyclerSwipeAdapter<CatalogViewAdapter.ViewHolder> {
    private final BookRepository bookRepository;
    private final Context context;
    private final List<BookInfo> bookInfos;

    public CatalogViewAdapter(Context context) {
        this.context = context;
        bookRepository = new DbBookRepository(DatabaseHelper.getInstance(context));
        bookInfos = bookRepository.list();
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        var view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_catalog, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        var bookInfo = bookInfos.get(position);
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
            intent.putExtras(bundle);
            startActivity(context, intent, bundle);
            //toast.cancel();
        });
        viewHolder.btnDelete.setOnClickListener(view -> {
            var removed = bookInfos.get(position);
            mItemManger.removeShownLayouts(viewHolder.swipeLayout);
            bookRepository.markAsDeleted(removed.id());
            bookInfos.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, bookInfos.size());
            mItemManger.closeAllItems();
        });
        String tgGroup = bookInfo.telegramGroup();
        if (tgGroup != null) {
            viewHolder.cover.setImageResource(R.drawable.ic_telegram_group_cover);
            var srcText = "t.me/" + tgGroup;
            viewHolder.source.setText(tgGroup);
        } else {
            viewHolder.cover.setImageResource(R.drawable.ic_book_cover);
            viewHolder.source.setText("личная");
        }

        mItemManger.bind(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return bookInfos.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.book_swipe;
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
}
