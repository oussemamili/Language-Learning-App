package com.example.languagelearningapp.data;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.languagelearningapp.data.db.WordDao;
import com.example.languagelearningapp.data.db.WordDatabase;
import com.example.languagelearningapp.model.Word;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WordRepository {
    private final WordDao wordDao;
    private final LiveData<List<Word>> allWords;
    private final ExecutorService executorService;

    public WordRepository(Application application) {
        WordDatabase database = WordDatabase.getInstance(application);
        wordDao = database.wordDao();
        allWords = wordDao.getAllWords();
        executorService = Executors.newFixedThreadPool(2);
    }

    public void insert(Word word) {
        executorService.execute(() -> wordDao.insert(word));
    }

    public void delete(Word word) {
        executorService.execute(() -> wordDao.delete(word));
    }

    public LiveData<List<Word>> getAllWords() {
        return allWords;
    }
}
