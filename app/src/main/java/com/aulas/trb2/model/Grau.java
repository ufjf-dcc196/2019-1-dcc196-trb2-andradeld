package com.aulas.trb2.model;

import java.util.HashMap;
import java.util.Map;

public enum Grau {
    MUITOFACIL(1,"Muito Fácil"),FACIL(2,"Fácil"),MEDIO(3,"Médio"),DIFICIL(4,"Difícil"),MUITODIFICIL(5,"Muito Difícil");
    private static Map map = new HashMap<>();
    private int grau;
    private String descricao;

    Grau(int i, String s) {
        this.grau = i;
        this.descricao = s;
    }

    public static Grau valueOf(int grau) {
        return (Grau) map.get(grau);
    }

    public int getGrau() {
        return grau;
    }

    public String getDescricao() {
        return descricao;
    }


    static {
        for (Grau grau : Grau.values()) {
            map.put(grau.grau, grau);
        }
    }

    @Override
    public String toString(){
        return this.descricao;
    }
}
