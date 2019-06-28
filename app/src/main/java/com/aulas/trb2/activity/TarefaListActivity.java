package com.aulas.trb2.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.Toast;

import com.aulas.trb2.R;
import com.aulas.trb2.adapter.TarefaAdapter;
import com.aulas.trb2.dao.TarefaEtiquetaDAO;
import com.aulas.trb2.dao.TarefaDAO;
import com.aulas.trb2.model.Etiqueta;

public class TarefaListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TarefaAdapter adapter;
    private Etiqueta etiqueta;
    public static final int REQUEST_TAREFA_CADASTRAR = 1;
    public static final int REQUEST_TAREFA_EDITAR = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefa_list);
        this.toolbar = findViewById(R.id.toolbar);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.etiqueta = (Etiqueta) bundle.get("etiqueta");
        }
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);

        final RecyclerView rv = findViewById(R.id.recyclerTarefa);
        if (this.etiqueta == null) {
            this.adapter = new TarefaAdapter(TarefaDAO.getInstance().getTarefasByEstado(this), null);
        } else {
            this.adapter = new TarefaAdapter(TarefaEtiquetaDAO.getInstance().getTarefasByEtiqueta(this, etiqueta), etiqueta);
        }
        adapter.setOnTarefaClickListener(new TarefaAdapter.OnTarefaClickListener() {
            @Override
            public void onTarefaClick(View tarefaView, int position) {
                Intent intent = new Intent(TarefaListActivity.this, TarefaEditarActivity.class);
                intent.putExtra("tarefa", adapter.getTarefa(position));
                startActivityForResult(intent, TarefaListActivity.REQUEST_TAREFA_EDITAR);
            }
        });
        rv.setAdapter(adapter);
        rv.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rv.setBackgroundColor(Color.parseColor("#FFFFFF"));
        rv.setLayoutManager(new LinearLayoutManager(this));
        Button botaoTarefa = findViewById(R.id.buttonCadastrarTarefa);
        botaoTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TarefaListActivity.this, TarefaInsertActivity.class);
                startActivityForResult(intent, TarefaListActivity.REQUEST_TAREFA_CADASTRAR);
            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_item_tarefa: {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                break;
            }
            case R.id.nav_item_etiqueta: {
                Intent intent = new Intent(TarefaListActivity.this, EtiquetaListActivity.class);
                startActivity(intent);
                break;
            }

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == TarefaListActivity.REQUEST_TAREFA_CADASTRAR) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                if (this.etiqueta == null) {
                    this.adapter = new TarefaAdapter(TarefaDAO.getInstance().getTarefasByEstado(this), null);
                } else {
                    this.adapter = new TarefaAdapter(TarefaEtiquetaDAO.getInstance().getTarefasByEtiqueta(this, etiqueta), etiqueta);
                }
            }
        }
        if (requestCode == TarefaListActivity.REQUEST_TAREFA_EDITAR) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Edição realizado com sucesso", Toast.LENGTH_SHORT).show();
                if (this.etiqueta == null) {
                    this.adapter = new TarefaAdapter(TarefaDAO.getInstance().getTarefasByEstado(this), null);
                } else {
                    this.adapter = new TarefaAdapter(TarefaEtiquetaDAO.getInstance().getTarefasByEtiqueta(this, etiqueta), etiqueta);
                }

            }
        }

    }


}