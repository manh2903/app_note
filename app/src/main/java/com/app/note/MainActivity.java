package com.app.note;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteClickListener {
    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private List<Note> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ghi chú của tôi");

        recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);

        noteList = new ArrayList<>();
        adapter = new NoteAdapter(noteList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DetailNoteActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    private void loadNotes() {
        noteList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_GHICHU, null, null, null, null, null, 
                DatabaseHelper.COLUMN_NGAYTAO + " DESC");

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            String noidung = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOIDUNG));
            String ngaytao = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NGAYTAO));
            noteList.add(new Note(id, noidung, ngaytao));
        }
        cursor.close();
        db.close();
        adapter.updateData(noteList);
    }

    private void deleteNote(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "DELETE FROM " + DatabaseHelper.TABLE_GHICHU + 
                " WHERE " + DatabaseHelper.COLUMN_ID + " = ?";
        db.execSQL(query, new String[]{String.valueOf(id)});
        db.close();
        loadNotes();
        Snackbar.make(recyclerView, "Đã xóa ghi chú", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onEditClick(Note note) {
        Intent intent = new Intent(this, DetailNoteActivity.class);
        intent.putExtra("note_id", note.getId());
        intent.putExtra("note_content", note.getNoidung());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Note note) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa ghi chú này?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteNote(note.getId()))
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}