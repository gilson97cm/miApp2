package com.example.deals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deals.bd.Connection;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class details_category extends AppCompatActivity {
    //sugerencias para la barra de busqueda
    private String[] SUGGESTION = new String[]{
            // "Apple", "Samsung"
    };
    private MaterialSearchView mMaterialSearchViewProduct;


    Toolbar toolbar;
    FloatingActionButton btnAddProduct;

    //datos de la tienda
    TextView txtNameDealInCategoryDetail;
    TextView txtIdDealInCategoryDetail;

    //datos de la categoria
    TextView txtIdCategoryDetail;
    TextView txtNameCategoryDetail;
    TextView txtDescriptionCategoryDetail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_category);

        btnAddProduct = (FloatingActionButton) findViewById(R.id.btnAddProduct);

        init();

        //agrega la flecha para regresar en toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_DetailCategory);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idDeal_ = txtIdDealInCategoryDetail.getText().toString();
                Intent intent = new Intent(getApplicationContext(), details_deal.class);
               intent.putExtra("idDeal", idDeal_);
                startActivity(intent);
                finish();
            }
        });

        //agregar icono de busqueda
        mMaterialSearchViewProduct = (MaterialSearchView) findViewById(R.id.searchViewProduct);
        mMaterialSearchViewProduct.setSuggestions(SUGGESTION);

        btnAddProduct = (FloatingActionButton) findViewById(R.id.btnAddProduct);

        //recuperar variables que envia detail_deal
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String idDeal = extras.getString("idDeal");
            String idCategory = extras.getString("idCategory");
            //llenar los datos de la tienda
            consultDeal(idDeal);

            //consultar los datos de la categoria
           consultCategory(idCategory);
           //
        }

    }

    //iconoes buscar y editar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //icono  para editar tiendas
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //icono buscar
        getMenuInflater().inflate(R.menu.edit_category, menu);
        MenuItem menuItem = menu.findItem(R.id.searchMenu);
        mMaterialSearchViewProduct.setMenuItem(menuItem);
        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.editCategory) {
            // edittDeal(String idDeal);
            Toast.makeText(this, "Editar Categoria", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //consultar datos de una categoria
    public void consultCategory (String id){
        Connection db = new Connection(this, "bdDeals", null, 1);
        SQLiteDatabase baseDatos = db.getWritableDatabase();

        Cursor fila = baseDatos.rawQuery("SELECT id, name, description, idDeal FROM category WHERE id = " + id, null);

        if (fila.moveToFirst()) {
            txtIdCategoryDetail.setText(fila.getString(0));
            txtNameCategoryDetail.setText(fila.getString(1));
            txtDescriptionCategoryDetail.setText("Descripción: "+fila.getString(2));

        } else {
            Toast.makeText(this, "No hay registro.", Toast.LENGTH_SHORT).show();
        }
        baseDatos.close();


    }

    //consultar datos de la tienda
    public void consultDeal (String id){
        Connection db = new Connection(this, "bdDeals", null, 1);
        SQLiteDatabase baseDatos = db.getWritableDatabase();

        Cursor fila = baseDatos.rawQuery("SELECT id, name FROM deal WHERE id = " + id, null);

        if (fila.moveToFirst()) {
            txtIdDealInCategoryDetail.setText(fila.getString(0));
            txtNameDealInCategoryDetail.setText("Tienda: "+fila.getString(1));

        } else {
            Toast.makeText(this, "No hay registro.", Toast.LENGTH_SHORT).show();
        }
        baseDatos.close();


    }

    public void init(){
        //datos de la tienda
        txtIdDealInCategoryDetail = (TextView) findViewById(R.id.txtIdDealInCategoryDetail);
        txtNameDealInCategoryDetail = (TextView) findViewById(R.id.txtNameDealInCategoryDetail);

        //datos de la categoria
         txtIdCategoryDetail = (TextView) findViewById(R.id.txtIdCategoryDetail);
         txtNameCategoryDetail = (TextView) findViewById(R.id.txtNameCategoryDetail);
         txtDescriptionCategoryDetail = (TextView) findViewById(R.id.txtDescriptionCategoryDetail);

    }




}
