package ru.vse.bookworm.ui.catalog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;
import ru.vse.bookworm.R;

/**
 * A fragment representing a list of Items.
 */
public class CatalogFragment extends Fragment {
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CatalogFragment() {
    }

    @SuppressWarnings("unused")
    public static CatalogFragment newInstance() {
        return new CatalogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog_list, container, false);

        if (view instanceof RecyclerView) {
            var recyclerView = (RecyclerView) view;
            var context = view.getContext();
            var adapter = new CatalogViewAdapter(context);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setItemAnimator(new FadeInLeftAnimator());
            recyclerView.setAdapter(adapter);
        }
        return view;
    }
}