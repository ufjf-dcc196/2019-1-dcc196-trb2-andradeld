package com.aulas.trb2.model;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;

public class Etiqueta implements Serializable {
    private Long idEtiqueta;
    private String descricao;

    public Etiqueta() {
    }

    public Etiqueta(Long idEtiqueta, String descricao) {
        this.idEtiqueta = idEtiqueta;
        this.descricao = descricao;
    }

    public Etiqueta(String titulo) {
        this.descricao = titulo;
    }

    public Long getIdEtiqueta() {
        return idEtiqueta;
    }

    public Etiqueta setIdEtiqueta(Long idEtiqueta) {
        this.idEtiqueta = idEtiqueta;
        return this;
    }

    public String getDescricao() {
        return descricao;
    }

    public Etiqueta setDescricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Etiqueta etiqueta = (Etiqueta) o;
        return Objects.equals(idEtiqueta, etiqueta.idEtiqueta);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(idEtiqueta);
    }

    public String makeDescription(){
        return String.format(Locale.getDefault(),
                "Id %d \n" +
                        "Título %s \n" ,
                this.getIdEtiqueta(),
                this.getDescricao());


    }
}
