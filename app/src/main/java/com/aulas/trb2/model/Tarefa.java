package com.aulas.trb2.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Tarefa implements Serializable {
    private Long id;
    private String titulo;
    private String descricao;
    private Grau grau;
    private Date dataLimite;
    private Date dataAtualizacao;
    private Estado estado;
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private List<Etiqueta> tags;

    public Tarefa(Long id, String titulo, String descricao, String dataatu, String datalime, int estado, int grau) throws ParseException {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataAtualizacao = format.parse(dataatu);
        this.dataLimite = format.parse(datalime);
        this.estado = Estado.valueOf(estado);
        this.grau = Grau.valueOf(grau);

    }

    public Tarefa(String titulo, String descricao, Grau grau, Estado estado, String dataLimite, List<Etiqueta> etiquetas) throws ParseException {
        this.titulo = titulo;
        this.descricao = descricao;
        this.grau = grau;
        this.estado = estado;
        this.dataLimite = format.parse(dataLimite);
        this.tags = etiquetas;
    }

    public Tarefa(){};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public Tarefa setDescricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public Grau getGrau() {
        return grau;
    }

    public void setGrau(Grau grau) {
        this.grau = grau;
    }

    public String getDataLimite() {
        return format.format(dataLimite);
    }

    public Tarefa setDataLimite(Date dataLimite) {
        this.dataLimite = dataLimite;
        return this;
    }

    public Tarefa setHorarioCriacao(String dataLimite) throws ParseException {
        this.dataLimite =format.parse(dataLimite);
        return this;
    }


    public String getDataAtualizacao() {
        return format.format(dataAtualizacao);
    }

    public void setDataAtualizacao(Date dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public List<Etiqueta> getTags() {
        return tags;
    }

    public void setTags(List<Etiqueta> tags) {
        this.tags = tags;
    }

    public String makeDescription(){
        return String.format(Locale.getDefault(),
                "Id %d \n" +
                        "Título %s \n" +
                        "Data Limite %s \n" +
                        "Data Atualização %s \n" +
                        "Grau de Dificuldade %s \n" +
                        "Estado %s",
                this.getId(),
                this.getTitulo(),
                this.getDataLimite(),
                this.getDataAtualizacao(),
                this.getGrau(),
                this.getEstado());
    }

    public Calendar getCalendar(){
        Calendar date = Calendar.getInstance();
        date.setTime(this.dataLimite);
        return date;
    }
}
