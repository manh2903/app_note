package com.app.note;

public class Note {
    private int id;
    private String noidung;
    private String ngaytao;

    public Note() {
    }

    public Note(int id, String noidung, String ngaytao) {
        this.id = id;
        this.noidung = noidung;
        this.ngaytao = ngaytao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public String getNgaytao() {
        return ngaytao;
    }

    public void setNgaytao(String ngaytao) {
        this.ngaytao = ngaytao;
    }
} 