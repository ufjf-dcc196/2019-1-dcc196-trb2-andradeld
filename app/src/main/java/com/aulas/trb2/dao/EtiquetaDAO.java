package com.aulas.trb2.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aulas.trb2.contract.TarefaContract;
import com.aulas.trb2.contract.TarefaDBHelper;
import com.aulas.trb2.model.Etiqueta;

public class EtiquetaDAO {
    private static final EtiquetaDAO etiquetaDAO = new EtiquetaDAO();

    public static EtiquetaDAO getInstance() {
        return etiquetaDAO;
    }

    private EtiquetaDAO() {
    }

    public Etiqueta save(Etiqueta etiqueta, Context context){
        TarefaDBHelper tarefaDBHelper = new TarefaDBHelper(context);
        SQLiteDatabase db = tarefaDBHelper.getWritableDatabase();
        etiqueta.setIdEtiqueta(db.insert(TarefaContract.Etiqueta.TABLE_NAME, null, toValues(etiqueta)));
        return etiqueta;
    }
    public Integer update(Etiqueta etiqueta, Context context){
        TarefaDBHelper tarefaDBHelper = new TarefaDBHelper(context);
        SQLiteDatabase db = tarefaDBHelper.getWritableDatabase();
        return db.update(TarefaContract.Etiqueta.TABLE_NAME, toValues(etiqueta),TarefaContract.Etiqueta._ID+"=?",toArgs(etiqueta));
    }

    public Integer delete(Etiqueta etiqueta,Context context){
        TarefaDBHelper tarefaDBHelper = new TarefaDBHelper(context);
        SQLiteDatabase db = tarefaDBHelper.getWritableDatabase();
        return db.delete(TarefaContract.Etiqueta.TABLE_NAME,TarefaContract.Etiqueta._ID+"=?",toArgs(etiqueta));

    }

    private String[] toArgs(Etiqueta etiqueta) {
        String[] args = {etiqueta.getIdEtiqueta().toString()};
        return args;
    }

    public Cursor getEtiquetas(Context context){
        TarefaDBHelper tarefaDBHelper = new TarefaDBHelper(context);
        SQLiteDatabase db = tarefaDBHelper.getReadableDatabase();
        String[] visao = {
                TarefaContract.Etiqueta._ID,
                TarefaContract.Etiqueta.COLUMN_NAME_TITULO
        };
        //String selecao = BibliotecaContract.Livro.COLUMN_NAME_ANO + " > ?";
        //String[] args = {"1950"};
        String sort = TarefaContract.Etiqueta.COLUMN_NAME_TITULO + " ASC";
        return db.query(TarefaContract.Etiqueta.TABLE_NAME, visao, null, null, null, null, sort);
    }

    public ContentValues toValues(Etiqueta etiqueta){
        ContentValues values = new ContentValues();
        values.put(TarefaContract.Etiqueta.COLUMN_NAME_TITULO,etiqueta.getDescricao());
        return values;
    }
}
