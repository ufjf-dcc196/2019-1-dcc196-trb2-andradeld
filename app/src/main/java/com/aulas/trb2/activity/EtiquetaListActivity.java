package com.aulas.trb2.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.aulas.trb2.R;
import com.aulas.trb2.adapter.EtiquetaAdapter;
import com.aulas.trb2.adapter.TarefaAdapter;
import com.aulas.trb2.dao.EtiquetaDAO;
import com.aulas.trb2.dao.TarefaEtiquetaDAO;
import com.aulas.trb2.dao.TarefaDAO;
import com.aulas.trb2.model.Etiqueta;

public class EtiquetaListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private EtiquetaAdapter adapter;
    public static final int REQUEST_ETIQUETA_CADASTRAR = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etiqueta_list);
        this.toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);

        final RecyclerView rv = findViewById(R.id.recyclerEtiqueta);
        this.adapter = new EtiquetaAdapter(EtiquetaDAO.getInstance().getEtiquetas(this));

        adapter.setOnEtiquetaClickListener(new EtiquetaAdapter.OnEtiquetaClickListener() {
            @Override
            public void onEtiquetaClick(View etiquetaView, int position) {
                Etiqueta etiqueta = adapter.getEtiqueta(position);
                Cursor cursor = TarefaEtiquetaDAO.getInstance().getTarefasByEtiqueta(EtiquetaListActivity.this, etiqueta);
                if (cursor.getCount() > 0) {
                    Intent intent = new Intent(EtiquetaListActivity.this, TarefaListActivity.class);
                    intent.putExtra("etiqueta", etiqueta);
                    startActivity(intent);
                } else {
                    Toast.makeText(EtiquetaListActivity.this,
                            "Não há tarefas vinculadas a essa Etiqueta",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        rv.setAdapter(adapter);
        rv.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rv.setBackgroundColor(Color.parseColor("#FFFFFF"));
        rv.setLayoutManager(new LinearLayoutManager(this));
        Button botaoEtiqueta = findViewById(R.id.buttonCadastrarEtiqueta);
        botaoEtiqueta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EtiquetaListActivity.this, EtiquetaInsertActivity.class);
                startActivityForResult(intent, EtiquetaListActivity.REQUEST_ETIQUETA_CADASTRAR);
            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_item_tarefa: {
                Intent intent = new Intent(EtiquetaListActivity.this, TarefaListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_item_etiqueta: {
                Intent intent = getIntent();
                finish();
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
        if (requestCode == EtiquetaListActivity.REQUEST_ETIQUETA_CADASTRAR) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                this.adapter.setCursor(EtiquetaDAO.getInstance().getEtiquetas(EtiquetaListActivity.this));
            }
        }
    }
}
