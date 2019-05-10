package com.saber.green.std.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.saber.green.std.entity.Note;
import com.saber.green.std.ui.adapter.NoteAdapter;
import com.saber.green.std.viewmodel.NoteViewModel;
import com.saber.green.std.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_NOTE_REQUEST = 101;
    public static final int EDIT_NOTE_REQUEST = 102;
    NoteViewModel noteViewModel;
    private FloatingActionButton buttonAddNote;
    private FloatingActionButton buttonDeleteAllNotes;
    private FloatingActionButton buttonReady;
    private RecyclerView recyclerView;
    final NoteAdapter adapter = new NoteAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        buttonAddNote = findViewById(R.id.button_add_note);
        buttonDeleteAllNotes = findViewById(R.id.button_delete_all_notes);
        buttonReady = findViewById(R.id.button_ready);
        recyclerView = findViewById(R.id.recycler_view);

        initRecyclerView();
        observeNoteChange();
        onNoteItemSwipe();
        onNoteItemClick();
        onAddNoteButtonClick();
        onDeleteAllNotesButtonClick();
        onReadyButtonClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String option = data.getStringExtra(AddEditNoteActivity.EXTRA_OPTION);
            Note note = new Note(option);
            noteViewModel.insert(note);
            Toast.makeText(this, "Option saved", Toast.LENGTH_SHORT).show();

        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Option can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String option = data.getStringExtra(AddEditNoteActivity.EXTRA_OPTION);
            Note note = new Note(option);
            note.setId(id);
            noteViewModel.update(note);
            Toast.makeText(this, "Option updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Option not saved", Toast.LENGTH_SHORT).show();

        }
    }

    public void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    public void observeNoteChange() {
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setNotes(notes);
            }
        });
    }

    public void onNoteItemSwipe() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void onAddNoteButtonClick() {
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });
    }

    public void onDeleteAllNotesButtonClick() {
        buttonDeleteAllNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteViewModel.deleteAllNotes();
            }
        });
    }

    public void onReadyButtonClick() {
        buttonReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ShakeActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onNoteItemClick() {
        adapter.setOnItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());
                intent.putExtra(AddEditNoteActivity.EXTRA_OPTION, note.getTitle());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });
    }

   @Override
    public void onBackPressed(){
       finishAffinity();

   }
}
