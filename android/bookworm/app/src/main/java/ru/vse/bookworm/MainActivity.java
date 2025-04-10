package ru.vse.bookworm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.UUID;

import ru.vse.bookworm.book.Fb2Parser;
import ru.vse.bookworm.databinding.ActivityMainBinding;
import ru.vse.bookworm.db.DatabaseHelper;
import ru.vse.bookworm.repository.BookRepository;
import ru.vse.bookworm.repository.DbBookRepository;
import ru.vse.bookworm.ui.reader.ReaderActivity;
import ru.vse.bookworm.utils.Json;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int OPEN_BOOK_FILE_REQUEST_CODE = 2;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private NavController navController;
    private BookRepository bookRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHelper.dropDatabase(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
//        binding.appBarMain.fab.setOnClickListener(view -> {
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null)
//                    .setAnchorView(R.id.fab).show();
//        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_catalog, R.id.nav_login)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        //NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
        bookRepository = new DbBookRepository(DatabaseHelper.getInstance(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        var itemId = item.getItemId();
        if (itemId == R.id.nav_file) {
            openFileDialog();
        } else {
            navController.navigate(itemId);
        }
        return true;
    }

    private void openFileDialog() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Выберите файл книги"),
                    OPEN_BOOK_FILE_REQUEST_CODE);
        } catch (Exception e) {
            Log.e("FILE_LOAD :", "Error open file dialog", e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            var uri = data.getData();
            if (uri == null) {
                return;
            }
            var docFile = DocumentFile.fromSingleUri(this, uri);
            if (docFile == null) {
                return;
            }
            var filename = docFile.getName();
            if (filename == null || !filename.endsWith(".fb2")) {
                return;
            }
            try (var inStream = getContentResolver().openInputStream(uri)) {
                var book = Fb2Parser.instance().parse(inStream)
                        .setId(UUID.randomUUID().toString())
                        .build();
                bookRepository.saveBook(book);
                var bundle = new Bundle();
                bundle.putString("bookInfo", Json.toJson(book.bookInfo()));
                var intent = new Intent(getApplicationContext(), ReaderActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}