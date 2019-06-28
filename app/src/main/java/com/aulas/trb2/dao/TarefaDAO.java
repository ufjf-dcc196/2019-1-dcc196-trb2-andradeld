package com.aulas.trb2.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.aulas.trb2.contract.TarefaContract;
import com.aulas.trb2.contract.TarefaDBHelper;
import com.aulas.trb2.model.Tarefa;

public class TarefaDAO {
    private static final TarefaDAO tarefaDAO = new TarefaDAO();

    public static TarefaDAO getInstance() {
        return tarefaDAO;
    }

    private TarefaDAO() {
    }

    public Tarefa save(Tarefa tarefa, Context context){
        TarefaDBHelper tarefaDBHelper = new TarefaDBHelper(context);
        SQLiteDatabase db = tarefaDBHelper.getWritableDatabase();
        tarefa.setId(db.insert(TarefaContract.Tarefa.TABLE_NAME, null, toValues(tarefa)));
        return tarefa;
    }
    public Integer update(Tarefa tarefa, Context context){
        TarefaDBHelper tarefaDBHelper = new TarefaDBHelper(context);
        SQLiteDatabase db = tarefaDBHelper.getWritableDatabase();
        return db.update(TarefaContract.Tarefa.TABLE_NAME, toValues(tarefa),TarefaContract.Tarefa._ID+"=?",toArgs(tarefa));
    }

    public Integer delete(Tarefa tarefa,Context context){
        TarefaDBHelper tarefaDBHelper = new TarefaDBHelper(context);
        SQLiteDatabase db = tarefaDBHelper.getWritableDatabase();
        return db.delete(TarefaContract.Tarefa.TABLE_NAME,TarefaContract.Tarefa._ID+"=?",toArgs(tarefa));

    }

    private String[] toArgs(Tarefa tarefa) {
        String[] args = {tarefa.getId().toString()};
        return args;
    }

    public Cursor getTarefasByEstado(Context context){
        TarefaDBHelper tarefaDBHelper = new TarefaDBHelper(context);
        SQLiteDatabase db = tarefaDBHelper.getReadableDatabase();
        String[] visao = {
                TarefaContract.Tarefa._ID,
                TarefaContract.Tarefa.COLUMN_NAME_TITULO,
                TarefaContract.Tarefa.COLUMN_NAME_DESCRICAO,
                TarefaContract.Tarefa.COLUMN_NAME_DATAHORAATU,
                TarefaContract.Tarefa.COLUMN_NAME_DATAHORALIMITE,
                TarefaContract.Tarefa.COLUMN_NAME_GRAUDIFICULDADE,
                TarefaContract.Tarefa.COLUMN_NAME_ESTADO
        };
        //String selecao = BibliotecaContract.Livro.COLUMN_NAME_ANO + " > ?";
        //String[] args = {"1950"};
        String sort = TarefaContract.Tarefa.COLUMN_NAME_ESTADO + " ASC";
        return db.query(TarefaContract.Tarefa.TABLE_NAME, visao, null, null, null, null, sort);
    }

    public ContentValues toValues(Tarefa tarefa){
        ContentValues values = new ContentValues();
        values.put(TarefaContract.Tarefa.COLUMN_NAME_TITULO,tarefa.getTitulo());
        values.put(TarefaContract.Tarefa.COLUMN_NAME_DESCRICAO,tarefa.getDescricao());
        values.put(TarefaContract.Tarefa.COLUMN_NAME_DATAHORALIMITE,tarefa.getDataLimite());
        values.put(TarefaContract.Tarefa.COLUMN_NAME_DATAHORAATU, new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        values.put(TarefaContract.Tarefa.COLUMN_NAME_ESTADO,tarefa.getEstado().getValor());
        values.put(TarefaContract.Tarefa.COLUMN_NAME_GRAUDIFICULDADE,tarefa.getGrau().getGrau());
        return values;
    }
}
