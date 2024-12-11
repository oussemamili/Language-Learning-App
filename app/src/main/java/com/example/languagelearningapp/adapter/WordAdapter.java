package com.example.languagelearningapp.adapter;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.languagelearningapp.R;
import com.example.languagelearningapp.data.WordRepository;
import com.example.languagelearningapp.model.Word;
import com.example.languagelearningapp.viewmodel.WordViewModel;

import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {

    private Context context;
    private WordViewModel wordViewModel;
    private List<Word> words;
    private int lastClickedPosition = -1;

    public WordAdapter(Context context, List<Word> words) {
        this.context = context;
        this.words = words;
        wordViewModel = new ViewModelProvider((AppCompatActivity) context).get(WordViewModel.class);
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        Word currentWord = words.get(position);
        holder.textWord.setText("• " + currentWord.getWord());

        // Show the meaning if the word is clicked
        if (position == lastClickedPosition) {
            holder.textMeaning.setVisibility(View.VISIBLE);
            holder.textMeaning.setText(currentWord.getMeaning());
            holder.textLink.setVisibility(View.VISIBLE);
        } else {
            holder.textMeaning.setVisibility(View.GONE);
            holder.textLink.setVisibility(View.GONE);
        }

        // Handle click event to toggle the meaning
        holder.itemView.setOnClickListener(v -> {
            if (lastClickedPosition == position) {
                lastClickedPosition = -1; // Deselect if the same word is clicked
            } else {
                lastClickedPosition = position;
            }
            notifyDataSetChanged();
        });

        holder.textLink.setOnClickListener(v -> {
            String searchUrl = "https://dictionary.cambridge.org/dictionary/english/" + currentWord.getWord();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(searchUrl));
            context.startActivity(intent);
        });

        holder.iconEdit.setOnClickListener(v -> showEditDialog(currentWord, position));
        holder.iconDelete.setOnClickListener(v -> deleteWord(position));
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public void setWords(List<Word> words) {
        this.words = words;
        notifyDataSetChanged();
    }

    private void showEditDialog(Word word, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_update_word, null);
        builder.setTitle("Update Word Info");
        builder.setView(dialogView);

        TextView wordInput = dialogView.findViewById(R.id.wordInputDialog);
        TextView meaningInput = dialogView.findViewById(R.id.meaningInputDialog);

        wordInput.setText(word.getWord());
        meaningInput.setText(word.getMeaning());

        builder.setPositiveButton("Update", (dialog, which) -> {
            word.setWord(wordInput.getText().toString());
            word.setMeaning(meaningInput.getText().toString());
            wordViewModel.insert(word);
            notifyItemChanged(position);
            Toast.makeText(context, "Word updated", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void deleteWord(int position) {
        if (!words.isEmpty()) {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Word")
                    .setMessage("Are you sure you want to delete this word?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Word wordToDelete = words.get(position);

                        words.remove(position);
                        WordRepository wordRepository = new WordRepository((Application) context.getApplicationContext());
                        wordRepository.delete(wordToDelete);

                        if (words.isEmpty()) {
                            notifyDataSetChanged();
                        } else {
                            notifyItemRemoved(position);
                        }

                        Toast.makeText(context, "Word removed!", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        }
    }

    public static class WordViewHolder extends RecyclerView.ViewHolder {
        private final TextView textWord;
        private final TextView textMeaning;
        private final TextView textLink;
        private final ImageView iconEdit;
        private final ImageView iconDelete;

        public WordViewHolder(View itemView) {
            super(itemView);
            textWord = itemView.findViewById(R.id.text_word);
            textMeaning = itemView.findViewById(R.id.text_meaning);
            textLink = itemView.findViewById(R.id.text_link);
            iconEdit = itemView.findViewById(R.id.icon_edit);
            iconDelete = itemView.findViewById(R.id.icon_delete);
        }
    }
}
