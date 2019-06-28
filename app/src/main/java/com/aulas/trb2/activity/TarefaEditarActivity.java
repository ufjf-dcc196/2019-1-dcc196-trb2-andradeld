package com.aulas.trb2.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.aulas.trb2.R;
import com.aulas.trb2.adapter.EtiquetaTarefaAdapter;
import com.aulas.trb2.dao.EtiquetaDAO;
import com.aulas.trb2.dao.TarefaDAO;
import com.aulas.trb2.dao.TarefaEtiquetaDAO;
import com.aulas.trb2.model.Estado;
import com.aulas.trb2.model.Etiqueta;
import com.aulas.trb2.model.Grau;
import com.aulas.trb2.model.Tarefa;

public class TarefaEditarActivity extends AppCompatActivity implements
        View.OnFocusChangeListener, View.OnClickListener {
    private Tarefa tarefa;
    private Calendar calendar;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private EditText txtDate, txtTime, txtTitulo, txtDescricao;
    private Spinner spinnerEstado, spinnerGrau;
    private RecyclerView recyclerViewCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefa_editar);
        Bundle bundle = getIntent().getExtras();
        this.tarefa = (Tarefa) bundle.get("tarefa");
        this.calendar = this.tarefa.getCalendar();
        txtTitulo = (EditText) findViewById(R.id.edt_tituloEditar);
        txtDescricao = (EditText) findViewById(R.id.edt_descricaoEditar);
        txtDate = (EditText) findViewById(R.id.edt_dateEditar);
        txtTime = (EditText) findViewById(R.id.edt_horaEditar);
        spinnerEstado = (Spinner) findViewById(R.id.spinnerEstadoEditar);
        spinnerGrau = (Spinner) findViewById(R.id.spinnerGrauEditar);
        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.spinner_layout_item, Estado.values());
        adapter.setDropDownViewResource(R.layout.spinner_layout_item);
        ArrayAdapter adapterGrau = new ArrayAdapter<>(this, R.layout.spinner_layout_item, Grau.values());
        adapterGrau.setDropDownViewResource(R.layout.spinner_layout_item);
        spinnerGrau.setAdapter(adapterGrau);
        spinnerEstado.setAdapter(adapter);
        spinnerEstado.setSelection(adapter.getPosition(this.tarefa.getEstado()));
        spinnerGrau.setSelection(adapterGrau.getPosition(this.tarefa.getGrau()));
        txtDate.setOnFocusChangeListener(this);
        txtTime.setOnFocusChangeListener(this);
        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);
        this.updateDate();
        this.updateTime();
        txtDate.setText(String.format(Locale.getDefault(), "%d/%02d/%d", this.mDay, this.mMonth + 1, this.mYear));
        txtTime.setText(String.format(Locale.getDefault(), "%d:%d", this.mHour, this.mMinute));
        txtDescricao.setText(this.tarefa.getDescricao());
        txtTitulo.setText(this.tarefa.getTitulo());
        recyclerViewCheck = (RecyclerView) findViewById(R.id.recyclerviewTagsEditar);
        final EtiquetaTarefaAdapter etiquetaTarefaAdapter = new EtiquetaTarefaAdapter(EtiquetaDAO.getInstance().getEtiquetas(TarefaEditarActivity.this));
        etiquetaTarefaAdapter.setItemStateArray(TarefaEtiquetaDAO.getInstance().getEtiquetasByTarefa(TarefaEditarActivity.this, this.tarefa));
        recyclerViewCheck.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewCheck.setAdapter(etiquetaTarefaAdapter);
        Button botaoSalvarTarefa = findViewById(R.id.buttonSalvarTarefaEditar);
        botaoSalvarTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (testaTela()) {
                    Estado estado = (Estado) spinnerEstado.getSelectedItem();
                    Grau grau = (Grau) spinnerGrau.getSelectedItem();
                    String dataLimite = String.format("%s %s", txtDate.getText().toString(), txtTime.getText().toString());
                    String titulo = txtTitulo.getText().toString();
                    String descricao = txtDescricao.getText().toString();
                    SparseBooleanArray escolhidos = etiquetaTarefaAdapter.getItemStateArray();
                    List<Etiqueta> etiquetaList = new ArrayList<>();
                    for (int i = 0; i < escolhidos.size(); i++) {
                        int key = escolhidos.keyAt(i);
                        if (escolhidos.get(key)) {
                            etiquetaList.add(etiquetaTarefaAdapter.getEtiqueta(key));
                        }
                    }
                    try {
                        TarefaEditarActivity.this.tarefa.setTitulo(titulo);
                        TarefaEditarActivity.this.tarefa.setDescricao(descricao);
                        TarefaEditarActivity.this.tarefa.setHorarioCriacao(dataLimite);
                        TarefaEditarActivity.this.tarefa.setGrau(grau);
                        TarefaEditarActivity.this.tarefa.setEstado(estado);
                        TarefaEditarActivity.this.tarefa.setTags(etiquetaList);
                        TarefaDAO.getInstance().update(tarefa, TarefaEditarActivity.this);
                        TarefaEtiquetaDAO.getInstance().delete(tarefa, TarefaEditarActivity.this);
                        for (Etiqueta e : tarefa.getTags()) {
                            TarefaEtiquetaDAO.getInstance().save(e, tarefa, TarefaEditarActivity.this);
                        }
                        Intent intent = new Intent();
                        intent.putExtra("tarefa", tarefa);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }

            private boolean testaTela() {
                boolean apto = true;
                if (TextUtils.isEmpty(txtTitulo.getText())) {
                    txtTitulo.requestFocus();
                    Toast.makeText(TarefaEditarActivity.this,
                            "Informe um Título para a Tarefa",
                            Toast.LENGTH_SHORT).show();
                    apto = false;
                } else if (TextUtils.isEmpty(txtDescricao.getText())) {
                    txtDescricao.requestFocus();
                    Toast.makeText(TarefaEditarActivity.this,
                            "Informe uma Descrição para a Tarefa",
                            Toast.LENGTH_SHORT).show();
                    apto = false;
                } else if (TextUtils.isEmpty(txtDate.getText())) {
                    txtDate.requestFocus();
                    Toast.makeText(TarefaEditarActivity.this,
                            "Informe uma Data Limite para a Tarefa",
                            Toast.LENGTH_SHORT).show();
                    apto = false;
                } else if (TextUtils.isEmpty(txtTime.getText())) {
                    txtTime.requestFocus();
                    Toast.makeText(TarefaEditarActivity.this,
                            "Informe uma Hora Limite para a Tarefa",
                            Toast.LENGTH_SHORT).show();
                    apto = false;
                }
                return apto;
            }
        });
        Button botaoLimparEtiqueta = findViewById(R.id.buttonLimparEtiqueta);
        botaoLimparEtiqueta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etiquetaTarefaAdapter.limpar();
            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == txtDate && hasFocus) {

            // Get Current Date
            this.updateDate();


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            txtDate.setText(String.format(Locale.getDefault(), "%d/%02d/%d", dayOfMonth, monthOfYear + 1, year));
                            Calendar calendar = Calendar.getInstance();
                            TarefaEditarActivity.this.calendar.set(Calendar.YEAR, year);
                            TarefaEditarActivity.this.calendar.set(Calendar.MONTH, monthOfYear);
                            TarefaEditarActivity.this.calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == txtTime && hasFocus) {

            // Get Current Time
            this.updateTime();

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(String.format(Locale.getDefault(), "%d:%d", hourOfDay, minute));
                            TarefaEditarActivity.this.calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            TarefaEditarActivity.this.calendar.set(Calendar.MINUTE, minute);
                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }
    }

    private void updateDate() {
        mYear = this.calendar.get(Calendar.YEAR);
        mMonth = this.calendar.get(Calendar.MONTH);
        mDay = this.calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void updateTime() {
        mHour = this.calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = this.calendar.get(Calendar.MINUTE);
    }

    @Override
    public void onClick(View v) {
        if (v == txtDate) {

            // Get Current Date
            this.updateDate();


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            txtDate.setText(String.format(Locale.getDefault(), "%d/%02d/%d", dayOfMonth, monthOfYear + 1, year));
                            Calendar calendar = Calendar.getInstance();
                            TarefaEditarActivity.this.calendar.set(Calendar.YEAR, year);
                            TarefaEditarActivity.this.calendar.set(Calendar.MONTH, monthOfYear);
                            TarefaEditarActivity.this.calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == txtTime) {

            // Get Current Time
            this.updateTime();

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(String.format(Locale.getDefault(), "%d:%d", hourOfDay, minute));
                            TarefaEditarActivity.this.calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            TarefaEditarActivity.this.calendar.set(Calendar.MINUTE, minute);
                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }
    }
}
