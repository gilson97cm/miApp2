package com.example.deals.deals;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deals.MainActivity;
import com.example.deals.R;
import com.example.deals.adapters.CategoryAdapter;
import com.example.deals.bd.Connection;
import com.example.deals.entities.CategoryVo;
import com.example.deals.categories.frm_add_categories;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class details_deal extends AppCompatActivity {


    FloatingActionButton btnAddCategory;
    Toolbar toolbar;
    TextView nameDeal;
    TextView addressDeal;
    TextView rucDeal;
    ImageView logoDeal;
    TextView idDeal;

    //cargar categorias
    RecyclerView recyclerViewCategories;


    TextView txtIdCategory;
    TextView txtNameCategory;
    TextView txtDescriptionCategory;
    TextView txtNumProducts;
    TextView txtIdDealOnCardCategory;

    CardView cardViewCategory;
    Button btnMoreDetailsCategory;
    Button btnDestroyCategories;

    ArrayList<CategoryVo> listCategories;
    CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_deal);

        //agrega la flecha para regresar en toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_DetailDeal);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });



        btnAddCategory = (FloatingActionButton) findViewById(R.id.btnAddCategory);

        init();

        //recuperar variables que envia MainActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String idDeal = extras.getString("idDeal");
            consult(idDeal);
        }

        //recycler adapter
        listCategories = new ArrayList<>();
        recyclerViewCategories = (RecyclerView) findViewById(R.id.recyclerViewCategory);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this));
         adapter = new CategoryAdapter(listCategories);
        recyclerViewCategories.setAdapter(adapter);
        loadCategories(idDeal.getText().toString());

    }

    //iconoes buscar y editar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //icono  para editar tiendas
        getMenuInflater().inflate(R.menu.edit_deal, menu);

        MaterialSearchView searchViewM = (MaterialSearchView) findViewById(R.id.searchViewCategory);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_category, menu);
        MenuItem menuItem = menu.findItem(R.id.searchCategory);
        //SearchView searchView =(SearchView) MenuItemCompat.getActionView(menuItem);
        searchViewM.setMenuItem(menuItem);
        searchViewM.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    ArrayList<CategoryVo> listaFiltrada = filter(listCategories,newText);
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
                adapter.setFilter(listCategories);
                return true;
            }
        });

        return true; //
    }

    private ArrayList<CategoryVo> filter(ArrayList<CategoryVo> categories, String text){
        ArrayList<CategoryVo> listaFiltrada =  new ArrayList<CategoryVo>();
        try {
            text = text.toLowerCase();

            for(CategoryVo categoryVo: categories){
                String deal = categoryVo.getName().toLowerCase();

                if(deal.contains(text)){
                    listaFiltrada.add(categoryVo);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return listaFiltrada;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.editDeal) {
           Intent intent = new Intent(getApplicationContext(), frm_edit_deals.class);
           String  idDeal_ = idDeal.getText().toString();
           intent.putExtra("idDeal", idDeal_);
           startActivity(intent);
            Toast.makeText(this, "Editar Tienda", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void consult (String id){
        Connection db = new Connection(this, "bdDeals", null, 1);
        SQLiteDatabase baseDatos = db.getWritableDatabase();

            Cursor fila = baseDatos.rawQuery("SELECT id, ruc, name, direction, logo FROM deal WHERE id = " + id, null);

            if (fila.moveToFirst()) {
                idDeal.setText(fila.getString(0));
                rucDeal.setText("RUC: "+fila.getString(1));
                nameDeal.setText(fila.getString(2));
                addressDeal.setText("Dirección: "+fila.getString(3));

                byte[] blob = fila.getBlob(4);
                Bitmap bmp= BitmapFactory.decodeByteArray(blob,0,blob.length);
                ImageView image=new ImageView(this);
                logoDeal.setImageBitmap(bmp);

            } else {
                Toast.makeText(this, "No hay registro.", Toast.LENGTH_SHORT).show();
            }
            baseDatos.close();


    }

    public void init(){
        nameDeal = (TextView) findViewById(R.id.txtNameDealDetail);
        addressDeal = (TextView) findViewById(R.id.txtDirectionDealDetail);
        rucDeal = (TextView) findViewById(R.id.txtRucDealDetail);
        logoDeal =(ImageView) findViewById(R.id.logoPrevDealDetail);
        idDeal = (TextView)findViewById(R.id.txIdDealDetail);

        //iniciar card view categoria

         txtIdCategory = (TextView) findViewById(R.id.txtIdCategory);
         txtNameCategory = (TextView) findViewById(R.id.txtNameCategory);
         txtDescriptionCategory = (TextView) findViewById(R.id.txtDescriptionCategory);
         txtNumProducts = (TextView) findViewById(R.id.txtNumProducts);

         cardViewCategory = (CardView) findViewById(R.id.cardViewCategory);
         btnMoreDetailsCategory = (Button) findViewById(R.id.btnMoreDetailsCategory);
         btnDestroyCategories = (Button) findViewById(R.id.btnDestroyCategory);
        txtIdDealOnCardCategory = (TextView) findViewById(R.id.txtIdDealOnCardCategory);

    }

    public void gotoFrmCategory(View v){
        String idDeal_ = idDeal.getText().toString();
        String nameDeal_ = nameDeal.getText().toString();
        Intent intent = new Intent(v.getContext(), frm_add_categories.class);
        //enviamos el id y el nombre de la tienda para guardar una categoria
        intent.putExtra("idDealDetailDeal",idDeal_);
        intent.putExtra("nameDealDetailDeal",nameDeal_);
        startActivity(intent);
       // Toast.makeText(this, "ID: "+idDeal_+"\nTienda: "+nameDeal_, Toast.LENGTH_SHORT).show();
    }

    //CARGAR CATEGORIAS
    //agregar vistas al recyclerView
    public void loadCategories(String idDeal_) {
        Connection db = new Connection(this, "bdDeals", null, 1);
        SQLiteDatabase baseDatos = db.getWritableDatabase();
        if (db != null) {
            Cursor fila = baseDatos.rawQuery("SELECT * FROM category WHERE idDeal = '"+idDeal_+"' ORDER BY id DESC", null);
            int i = 0;
            if (fila.moveToFirst()) {
                do {
                    String id = fila.getString(0);
                    String name = fila.getString(1);
                    String description = fila.getString(2);
                    String idDeal = fila.getString(3);
                    listCategories.add(new CategoryVo(id, name, description, idDeal));
                    i++;
                } while (fila.moveToNext());
            }
        }
    }

}

