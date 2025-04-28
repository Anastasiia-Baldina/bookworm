package ru.vse.bookworm.ui.catalog;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;
import ru.vse.bookworm.R;
import ru.vse.bookworm.book.BookInfo;
import ru.vse.bookworm.db.DbHelper;
import ru.vse.bookworm.repository.BookRepository;
import ru.vse.bookworm.repository.sqlite.DbBookRepository;

/**
 * A fragment representing a list of Items.
 */
public class CatalogFragment extends Fragment {
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    private CatalogViewAdapter adapter;
    private BookRepository bookRepository;
    private RecyclerView recyclerView;
    private TextView emptyListView;
    private Context context;
    private SearchView searchView;

    public CatalogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog_list, container, false);
        context = view.getContext();
        recyclerView = view.findViewById(R.id.book_list);
        emptyListView = view.findViewById(R.id.empty_book_list);

        bookRepository = new DbBookRepository(DbHelper.getInstance(context));
        List<BookInfo> books = bookRepository.list();
        if(books.isEmpty()) {
            emptyListView.setVisibility(VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyListView.setVisibility(View.GONE);
            recyclerView.setVisibility(VISIBLE);
            adapter = new CatalogViewAdapter(context, bookRepository, books);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setItemAnimator(new FadeInLeftAnimator());
            recyclerView.setAdapter(adapter);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        List<BookInfo> books = bookRepository.list();
        if (!books.isEmpty()) {
            emptyListView.setVisibility(View.GONE);
            recyclerView.setVisibility(VISIBLE);
            if (adapter == null) {
                adapter = new CatalogViewAdapter(context, bookRepository, books);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setItemAnimator(new FadeInLeftAnimator());
                recyclerView.setAdapter(adapter);
            } else if (books.size() != adapter.getItemCount()) {
                if (searchView != null) {
                    searchView.setQuery("", false);
                    searchView.setIconified(true);
                }
                adapter.invalidate(books);
            } else if (adapter != null) {
                adapter.updateBookState(books);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.findItem(R.id.action_search).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menuItem.getActionView();
        if (searchView != null && adapter != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    var filter = adapter.getSearchFilter();
                    filter.performFiltering(query);
                    filter.done();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getSearchFilter().performFiltering(newText);
                    return false;
                }
            });
        }
    }
}