package com.example.languagelearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddWordActivity extends AppCompatActivity {

    private EditText editWord, editMeaning;
    private Button save_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

        editWord = findViewById(R.id.edit_word);
        editMeaning = findViewById(R.id.edit_meaning);
        save_button = findViewById(R.id.btn_save);

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = editWord.getText().toString().trim();
                String meaning = editMeaning.getText().toString().trim();

                if (word.isEmpty() || meaning.isEmpty()) {
                    Toast.makeText(AddWordActivity.this, "Both fields are required!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("word", word);
                    resultIntent.putExtra("meaning", meaning);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }
}
