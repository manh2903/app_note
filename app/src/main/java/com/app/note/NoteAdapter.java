package com.app.note;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<Note> noteList;
    private OnNoteClickListener listener;

    public interface OnNoteClickListener {
        void onEditClick(Note note);
        void onDeleteClick(Note note);
    }

    public NoteAdapter(List<Note> noteList, OnNoteClickListener listener) {
        this.noteList = noteList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.tvNoiDung.setText(note.getNoidung());
        holder.tvNgayTao.setText(note.getNgaytao());

        holder.itemView.setOnClickListener(v -> listener.onEditClick(note));
//        holder.btnXoa.setOnClickListener(v -> listener.onDeleteClick(note));
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public void updateData(List<Note> newList) {
        this.noteList = newList;
        notifyDataSetChanged();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvNoiDung, tvNgayTao;
        Button btnSua, btnXoa;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNoiDung = itemView.findViewById(R.id.tvNoiDung);
            tvNgayTao = itemView.findViewById(R.id.tvNgayTao);
        }
    }
} 