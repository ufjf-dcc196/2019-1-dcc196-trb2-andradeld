package com.aulas.trb2.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Locale;

import com.aulas.trb2.contract.TarefaContract;
import com.aulas.trb2.contract.TarefaDBHelper;
import com.aulas.trb2.model.Etiqueta;
import com.aulas.trb2.model.Tarefa;

public class TarefaEtiquetaDAO {
    private static final TarefaEtiquetaDAO etiquetaDAO = new TarefaEtiquetaDAO();

    private final String TarefasByEtiqueta = String.format(Locale.getDefault(), "SELECT t.* FROM %s t INNER JOIN %s te ON" +
                    " t.%s = te.%s WHERE %s = ? ORDER BY %s",
            TarefaContract.Tarefa.TABLE_NAME,
            TarefaContract.EtiquetaTarefa.TABLE_NAME,
            TarefaContract.Tarefa._ID,
            TarefaContract.EtiquetaTarefa.COLUMN_NAME_TAREFA,
            TarefaContract.EtiquetaTarefa.COLUMN_NAME_ETIQUETA,
            TarefaContract.Tarefa.COLUMN_NAME_ESTADO);

    private final String EtiquetasByTarefa = String.format(Locale.getDefault(), "SELECT t.* FROM %s t INNER JOIN %s te ON" +
                    " t.%s = te.%s WHERE %s = ? ORDER BY %s",
            TarefaContract.Etiqueta.TABLE_NAME,
            TarefaContract.EtiquetaTarefa.TABLE_NAME,
            TarefaContract.Etiqueta._ID,
            TarefaContract.EtiquetaTarefa.COLUMN_NAME_ETIQUETA,
            TarefaContract.EtiquetaTarefa.COLUMN_NAME_TAREFA,
            TarefaContract.Etiqueta.COLUMN_NAME_TITULO);

    public static TarefaEtiquetaDAO getInstance() {
        return etiquetaDAO;
    }

    private TarefaEtiquetaDAO() {
    }

    public Long save(Etiqueta etiqueta, Tarefa tarefa, Context context) {
        TarefaDBHelper tarefaDBHelper = new TarefaDBHelper(context);
        SQLiteDatabase db = tarefaDBHelper.getWritableDatabase();
        return db.insert(TarefaContract.EtiquetaTarefa.TABLE_NAME, null, toValues(etiqueta, tarefa));
    }

    public Integer delete(Tarefa tarefa, Context context) {
        TarefaDBHelper tarefaDBHelper = new TarefaDBHelper(context);
        SQLiteDatabase db = tarefaDBHelper.getWritableDatabase();
        return db.delete(TarefaContract.EtiquetaTarefa.TABLE_NAME, TarefaContract.EtiquetaTarefa.COLUMN_NAME_TAREFA + "=?", toArgs(tarefa));

    }

    private String[] toArgs(Tarefa tarefa) {
        String[] args = {tarefa.getId().toString()};
        return args;
    }

    public Cursor getTarefasByEtiqueta(Context context, Etiqueta etiqueta) {
        TarefaDBHelper tarefaDBHelper = new TarefaDBHelper(context);
        SQLiteDatabase db = tarefaDBHelper.getReadableDatabase();
        return db.rawQuery(TarefasByEtiqueta, new String[]{String.valueOf(etiqueta.getIdEtiqueta())});
    }

    public Cursor getEtiquetasByTarefa(Context context, Tarefa tarefa) {
        TarefaDBHelper tarefaDBHelper = new TarefaDBHelper(context);
        SQLiteDatabase db = tarefaDBHelper.getReadableDatabase();
        return db.rawQuery(EtiquetasByTarefa, new String[]{String.valueOf(tarefa.getId())});
    }

    public ContentValues toValues(Etiqueta etiqueta, Tarefa tarefa) {
        ContentValues values = new ContentValues();
        values.put(TarefaContract.EtiquetaTarefa.COLUMN_NAME_ETIQUETA, etiqueta.getIdEtiqueta());
        values.put(TarefaContract.EtiquetaTarefa.COLUMN_NAME_TAREFA, tarefa.getId());
        return values;
    }
}
