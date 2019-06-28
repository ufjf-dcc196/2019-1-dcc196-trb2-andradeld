package com.aulas.trb2.model;

import java.util.HashMap;
import java.util.Map;

public enum Estado {
    FAZER(1, "A Fazer"), EXECUCAO(4, "Em Execução"), BlOQUEADA(2, "Bloqueada"), CONCLUIDA(3, "Concluída");
    private static Map map = new HashMap<>();
    private String texto;
    private int valor;

    Estado(int i, String t) {
        this.texto = t;
        this.valor = i;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    static {
        for (Estado estado : Estado.values()) {
            map.put(estado.valor, estado);
        }
    }

    public static Estado valueOf(int estado) {
        return (Estado) map.get(estado);
    }

    @Override
    public String toString(){
        return this.texto;
    }


}
