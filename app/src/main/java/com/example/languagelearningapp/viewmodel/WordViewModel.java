package com.example.languagelearningapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.languagelearningapp.data.WordRepository;
import com.example.languagelearningapp.model.Word;

import java.util.List;

public class WordViewModel extends AndroidViewModel {
    private final WordRepository repository;
    private final LiveData<List<Word>> allWords;

    public WordViewModel(Application application) {
        super(application);
        repository = new WordRepository(application);
        allWords = repository.getAllWords();
    }

    public LiveData<List<Word>> getAllWords() {
        return allWords;
    }

    public void insert(Word word) {
        repository.insert(word);
    }

    public void delete(Word word) {
        repository.delete(word);
    }
}
