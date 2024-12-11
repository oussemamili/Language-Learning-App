package com.example.languagelearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.languagelearningapp.adapter.WordAdapter;
import com.example.languagelearningapp.data.WordRepository;
import com.example.languagelearningapp.model.Word;
import com.example.languagelearningapp.viewmodel.WordViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WordAdapter wordAdapter;
    private WordViewModel wordViewModel;
    private ArrayList<Word> wordList = new ArrayList<>();
    private static final int ADD_WORD_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        wordAdapter = new WordAdapter(this, wordList);
        recyclerView.setAdapter(wordAdapter);

        wordViewModel = new ViewModelProvider(this).get(WordViewModel.class);

        // Observe changes in the database and update the RecyclerView
        wordViewModel.getAllWords().observe(this, words -> {
            wordList.clear();
            wordList.addAll(words);
            wordAdapter.setWords(words);
        });

        EditText searchBox = findViewById(R.id.search_box);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                filterWords(query);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddWordActivity.class);
                startActivityForResult(intent, ADD_WORD_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == ADD_WORD_REQUEST && resultCode == RESULT_OK) {
                if (data != null) {
                    String word = data.getStringExtra("word");
                    String meaning = data.getStringExtra("meaning");

                    if (word != null && meaning != null) {
                        Word newWord = new Word(word, meaning);
                        wordList.add(newWord);

                        WordRepository repository = new WordRepository(getApplication());
                        repository.insert(newWord);

                        wordAdapter.notifyItemInserted(wordList.size() - 1);
                    } else {
                        Toast.makeText(this, "Error: Invalid word data!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Error: No data returned!", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "An error occurred while adding the word!", Toast.LENGTH_SHORT).show();
        }
    }

    private void filterWords(String query) {
        List<Word> filteredList = new ArrayList<>();
        for (Word word : wordList) {
            if (word.getWord().toLowerCase().startsWith(query.toLowerCase())) {
                filteredList.add(word);
            }
        }
        wordAdapter.setWords(filteredList);
    }
}
