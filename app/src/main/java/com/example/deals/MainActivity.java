package com.example.deals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deals.adapters.DealAdapter;
import com.example.deals.bd.Connection;
import com.example.deals.deals.frm_add_deals;
import com.example.deals.entities.DealVo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //sugerencias para la barra de busqueda
    private String[] SUGGESTION = new String[]{
            // "Apple", "Samsung"
    };

    private MaterialSearchView mMaterialSearchView;
    private Toolbar toolbar;

    RecyclerView recyclerViewDeals;

    Button btnMoreDetails;
    TextView txtIdDeal;
    CardView cardView;
    Button btnMoreDestroyDeal;
    TextView txtNameDeal;

    ArrayList<DealVo> listDeals;
    DealAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

     //  mMaterialSearchView = (MaterialSearchView) findViewById(R.id.searchView);
      //  searchView = (SearchView) findViewById(R.id.searchView);
      //  mMaterialSearchView.setSuggestions(SUGGESTION);


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        init();

        //recycler adapter
        listDeals = new ArrayList<>();
        recyclerViewDeals = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerViewDeals.setLayoutManager(new LinearLayoutManager(this));
         adapter = new DealAdapter(listDeals);
        recyclerViewDeals.setAdapter(adapter);
        loadDeals();




    }

//accion buscar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MaterialSearchView searchViewM = (MaterialSearchView) findViewById(R.id.searchViewDeal);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_deal, menu);
        MenuItem menuItem = menu.findItem(R.id.searchDeal);
        searchViewM.setMenuItem(menuItem);
       // SearchView searchView =(SearchView) MenuItemCompat.getActionView(menuItem);
        //custom searchbar
        //searchView.setQueryHint("Buscar Tienda...");

        //EditText searchEditText = searchView.findViewById(R.id.search_src_text);
        //searchEditText.setTextColor(Color.WHITE);
        //searchEditText.setHintTextColor(Color.WHITE);
        /////

        searchViewM.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    ArrayList<DealVo> listaFiltrada = filter(listDeals,newText);
                    adapter.setFilter(listaFiltrada);

                }catch (Exception e){
                    e.printStackTrace();
                }
                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                adapter.setFilter(listDeals);
                return true;
            }
        });

        return true; //
    }

    private ArrayList<DealVo> filter(ArrayList<DealVo> deals, String text){
        ArrayList<DealVo> listaFiltrada =  new ArrayList<DealVo>();
        try {
            text = text.toLowerCase();

            for(DealVo dealVo: deals){
                String deal = dealVo.getName().toLowerCase();

                if(deal.contains(text)){
                    listaFiltrada.add(dealVo);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return listaFiltrada;
    }


    //agregar vistas al recyclerView
    public void loadDeals() {
         Connection db = new Connection(this, "bdDeals", null, 1);
        SQLiteDatabase baseDatos = db.getWritableDatabase();
        if (db != null) {
            Cursor fila = baseDatos.rawQuery("SELECT * FROM deal ORDER BY id DESC", null);
            int i = 0;
            if (fila.moveToFirst()) {
                do {
                    String id = fila.getString(0);
                    String ruc = fila.getString(1);
                    String name = fila.getString(2);
                    String direction = fila.getString(3);
                    byte[] img = fila.getBlob(4);

                    listDeals.add(new DealVo(id, ruc, name, direction, img));
                    i++;
                } while (fila.moveToNext());
            }
        }
    }


    //ir al formulario para agregar tiendas
    public void addDeal(View v) {
        Toast.makeText(this, "Agregar Tienda.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, frm_add_deals.class);
        startActivity(intent);

    }

    //iniciar elementos de la visa
    public void init() {

        btnMoreDetails = (Button) findViewById(R.id.btnMoreDetails);
        btnMoreDestroyDeal = (Button) findViewById(R.id.btnDestroyDeal);
        txtIdDeal = (TextView) findViewById(R.id.txtIdDeal);
        txtNameDeal = (TextView) findViewById(R.id.txtNameDeal);
        cardView = (CardView) findViewById(R.id.cardView);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Intent intent = new Intent(this, frm_add_deals.class);
                startActivity(intent);
                break;
        }

    }


}
