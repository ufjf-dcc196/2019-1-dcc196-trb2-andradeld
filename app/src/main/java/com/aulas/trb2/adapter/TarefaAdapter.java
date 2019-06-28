package com.aulas.trb2.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import java.text.ParseException;

import com.aulas.trb2.R;
import com.aulas.trb2.activity.TarefaListActivity;
import com.aulas.trb2.contract.TarefaContract;
import com.aulas.trb2.dao.TarefaEtiquetaDAO;
import com.aulas.trb2.dao.TarefaDAO;
import com.aulas.trb2.model.Estado;
import com.aulas.trb2.model.Etiqueta;
import com.aulas.trb2.model.Tarefa;

public class TarefaAdapter extends RecyclerView.Adapter<TarefaAdapter.ViewHolder> {
    private Cursor cursor;
    private OnTarefaClickListener listener;
    private Etiqueta etiqueta;

    public interface OnTarefaClickListener {
        void onTarefaClick(View tarefaView, int position);
    }

    public void setOnTarefaClickListener(OnTarefaClickListener listener) {
        this.listener = listener;
    }

    public TarefaAdapter(Cursor c, Etiqueta etiqueta) {
        cursor = c;
        this.etiqueta = etiqueta;
        notifyDataSetChanged();
    }

    public void setCursor(Cursor c) {
        cursor = c;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public TarefaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View tarefaView = inflater.inflate(R.layout.itemlista_layout, viewGroup, false);
        ViewHolder holder = new ViewHolder(tarefaView, context);
        return holder;
    }

    public Tarefa getTarefa(int position) {
        int idxTitulo = cursor.getColumnIndexOrThrow(TarefaContract.Tarefa.COLUMN_NAME_TITULO);
        int idxDescricao = cursor.getColumnIndexOrThrow(TarefaContract.Tarefa.COLUMN_NAME_DESCRICAO);
        int idxdataAtu = cursor.getColumnIndexOrThrow(TarefaContract.Tarefa.COLUMN_NAME_DATAHORAATU);
        int idxdataLimite = cursor.getColumnIndexOrThrow(TarefaContract.Tarefa.COLUMN_NAME_DATAHORALIMITE);
        int idxEstado = cursor.getColumnIndexOrThrow(TarefaContract.Tarefa.COLUMN_NAME_ESTADO);
        int idxGrau = cursor.getColumnIndexOrThrow(TarefaContract.Tarefa.COLUMN_NAME_GRAUDIFICULDADE);
        int idxId = cursor.getColumnIndexOrThrow(TarefaContract.Tarefa._ID);
        cursor.moveToPosition(position);
        Tarefa tarefa = null;
        try {
            tarefa = new Tarefa(cursor.getLong(idxId), cursor.getString(idxTitulo),
                    cursor.getString(idxDescricao),
                    cursor.getString(idxdataAtu),
                    cursor.getString(idxdataLimite),
                    cursor.getInt(idxEstado),
                    cursor.getInt(idxGrau));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tarefa;
    }

    @Override
    public void onBindViewHolder(@NonNull final TarefaAdapter.ViewHolder viewHolder, final int i) {
        final Tarefa tarefa = getTarefa(i);
        viewHolder.txtTitulo.setText(tarefa.makeDescription());
        if (tarefa.getEstado().equals(Estado.CONCLUIDA)) {
            viewHolder.txtTitulo.setTextColor(Color.parseColor("#4CAF50"));
        }
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TarefaDAO.getInstance().delete(tarefa, viewHolder.context);
                if (etiqueta != null) {
                    setCursor(TarefaEtiquetaDAO.getInstance().getTarefasByEtiqueta(viewHolder.context, etiqueta));
                } else {
                    setCursor(TarefaDAO.getInstance().getTarefasByEstado(viewHolder.context));
                }
                notifyItemRemoved(i); // bug ao final da lista
            }
        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitulo = itemView.findViewById(R.id.layout_text_item);
        private ImageButton delete = (ImageButton) itemView.findViewById(R.id.delete_button);
        private Context context;

        private ViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onTarefaClick(v, position);
                        }
                    }
                }
            });
        }
    }
}
