package com.aulas.trb2.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.aulas.trb2.R;
import com.aulas.trb2.contract.TarefaContract;
import com.aulas.trb2.model.Etiqueta;

public class EtiquetaTarefaAdapter extends RecyclerView.Adapter<EtiquetaTarefaAdapter.ViewHolder> {
    private SparseBooleanArray itemStateArray = new SparseBooleanArray();
    private Cursor cursor;
    private OnEtiquetaTarefaClickListener listener;

    public Etiqueta getEtiqueta(int position) {
        int idxTitulo = cursor.getColumnIndexOrThrow(TarefaContract.Etiqueta.COLUMN_NAME_TITULO);
        int idxId = cursor.getColumnIndexOrThrow(TarefaContract.Etiqueta._ID);
        cursor.moveToPosition(position);
        Etiqueta etiqueta;
        etiqueta = new Etiqueta(cursor.getLong(idxId), cursor.getString(idxTitulo));
        return etiqueta;
    }

    public List<Etiqueta> getListEtiqueta(Cursor cursor){
        List<Etiqueta> etiquetaList = new ArrayList<>();
        for (int i = 0; i <cursor.getCount();i++){
            int idxTitulo = cursor.getColumnIndexOrThrow(TarefaContract.Etiqueta.COLUMN_NAME_TITULO);
            int idxId = cursor.getColumnIndexOrThrow(TarefaContract.Etiqueta._ID);
            cursor.moveToPosition(i);
            Etiqueta etiqueta;
            etiqueta = new Etiqueta(cursor.getLong(idxId), cursor.getString(idxTitulo));
            etiquetaList.add(etiqueta);
        }
        return etiquetaList;
    }

    @NonNull
    @Override
    public EtiquetaTarefaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.itemlistacheck_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EtiquetaTarefaAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public void limpar() {
        this.itemStateArray.clear();
        notifyDataSetChanged();
    }

    public interface OnEtiquetaTarefaClickListener {
        void onEtiquetaClick(View tarefaView, int position);
    }

    public void setOnEtiquetaClickListener(OnEtiquetaTarefaClickListener listener) {
        this.listener = listener;
    }

    public EtiquetaTarefaAdapter(Cursor c) {
        cursor = c;
        notifyDataSetChanged();
    }

    public void setCursor(Cursor c) {
        cursor = c;
        notifyDataSetChanged();
    }

    public SparseBooleanArray getItemStateArray() {
        return itemStateArray;
    }

    public void setItemStateArray(Cursor cursorTarefa) {
        SparseBooleanArray itemStateArray = new SparseBooleanArray();
        List<Etiqueta> etiquetasTarefa = getListEtiqueta(cursorTarefa);
        for (int i =0; i<this.cursor.getCount();i++){
            if(etiquetasTarefa.contains(getEtiqueta(i))) {
                itemStateArray.put(i,true );
            }
        }
        this.itemStateArray = itemStateArray;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CheckedTextView mCheckedTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckedTextView = (CheckedTextView) itemView.findViewById(R.id.checked_text_view);
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            if (!itemStateArray.get(position, false)) {
                mCheckedTextView.setChecked(false);
            } else {
                mCheckedTextView.setChecked(true);
            }
            Etiqueta etiqueta = EtiquetaTarefaAdapter.this.getEtiqueta(position);
            mCheckedTextView.setText(etiqueta.getDescricao());
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            if (!itemStateArray.get(adapterPosition, false)) {
                mCheckedTextView.setChecked(true);
                itemStateArray.put(adapterPosition, true);
            } else {
                mCheckedTextView.setChecked(false);
                itemStateArray.put(adapterPosition, false);
            }
        }


    }
}
