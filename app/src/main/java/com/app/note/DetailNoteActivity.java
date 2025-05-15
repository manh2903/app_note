package com.app.note;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;

public class DetailNoteActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private EditText edtNoiDung;
    private MaterialButton btnLuu;
    private int noteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);

        // Khởi tạo DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Thiết lập Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Ánh xạ view
        edtNoiDung = findViewById(R.id.edtNoiDung);
        btnLuu = findViewById(R.id.btnLuu);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            noteId = intent.getIntExtra("note_id", -1);
            String noidung = intent.getStringExtra("note_content");
            if (noteId != -1 && noidung != null) {
                // Chế độ sửa
                getSupportActionBar().setTitle("Sửa ghi chú");
                edtNoiDung.setText(noidung);
            } else {
                // Chế độ thêm mới
                getSupportActionBar().setTitle("Thêm ghi chú mới");
            }
        }

        // Xử lý sự kiện click nút lưu
        btnLuu.setOnClickListener(v -> {
            String noidung = edtNoiDung.getText().toString().trim();
            if (!noidung.isEmpty()) {
                if (noteId != -1) {
                    // Cập nhật ghi chú
                    updateNote(noteId, noidung);
                } else {
                    // Thêm ghi chú mới
                    String ngaytao = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
                            .format(new java.util.Date());
                    addNote(noidung, ngaytao);
                }
                finish();
            } else {
                Toast.makeText(this, "Vui lòng nhập nội dung", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addNote(String noidung, String ngaytao) {
        android.database.sqlite.SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "INSERT INTO " + DatabaseHelper.TABLE_GHICHU + 
                " (" + DatabaseHelper.COLUMN_NOIDUNG + ", " + 
                DatabaseHelper.COLUMN_NGAYTAO + ") VALUES (?, ?)";
        db.execSQL(query, new String[]{noidung, ngaytao});
        db.close();
        Toast.makeText(this, "Đã lưu ghi chú", Toast.LENGTH_SHORT).show();
    }

    private void updateNote(int id, String noidung) {
        android.database.sqlite.SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "UPDATE " + DatabaseHelper.TABLE_GHICHU + 
                " SET " + DatabaseHelper.COLUMN_NOIDUNG + " = ? WHERE " + 
                DatabaseHelper.COLUMN_ID + " = ?";
        db.execSQL(query, new String[]{noidung, String.valueOf(id)});
        db.close();
        Toast.makeText(this, "Đã cập nhật ghi chú", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
} 