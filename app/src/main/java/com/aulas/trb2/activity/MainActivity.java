package com.aulas.trb2.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import com.aulas.trb2.R;

import java.util.Date;

import com.aulas.trb2.dao.EtiquetaDAO;
import com.aulas.trb2.dao.TarefaDAO;
import com.aulas.trb2.dao.TarefaEtiquetaDAO;
import com.aulas.trb2.model.Estado;
import com.aulas.trb2.model.Etiqueta;
import com.aulas.trb2.model.Grau;
import com.aulas.trb2.model.Tarefa;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);
        Tarefa tarefa = new Tarefa().setDescricao("Dispositivos moveis");
        tarefa.setEstado(Estado.EXECUCAO);
        tarefa.setDataLimite(new Date());
        tarefa.setTitulo("Trabalho 2 - Dispositivos moveis");
        tarefa.setGrau(Grau.DIFICIL);
        Etiqueta etiqueta1 = new Etiqueta().setDescricao("Faculdade");
        Etiqueta etiqueta2 = new Etiqueta().setDescricao("Pessoal");
        Etiqueta etiqueta3 = new Etiqueta().setDescricao("Trabalho");
        tarefa = TarefaDAO.getInstance().save(tarefa,this);
        etiqueta1 = EtiquetaDAO.getInstance().save(etiqueta1,this);
        etiqueta2 = EtiquetaDAO.getInstance().save(etiqueta2,this);
        etiqueta3 = EtiquetaDAO.getInstance().save(etiqueta3,this);
        TarefaEtiquetaDAO.getInstance().save(etiqueta1,tarefa,this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_item_tarefa: {
                Intent intent = new Intent(MainActivity.this,TarefaListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_item_etiqueta: {
                Intent intent = new Intent(MainActivity.this, EtiquetaListActivity.class);
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
}

