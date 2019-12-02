package com.example.deals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deals.bd.Connection;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class frm_categories extends AppCompatActivity {

   Toolbar toolbar;
    TextView nameDeal;
    TextView idDeal;
    TextInputLayout lblNameCategory;
    TextInputEditText nameCategory;
    TextInputLayout lblDescriptionCategory;
    TextInputEditText descriptionCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_categories);
        //agrega la flecha para regresar en toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_frmCategory);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), details_deal.class));
            }
        });


        //recuperar variables que envia MainActivity
      /*  Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String idDeal_ = extras.getString("idDeal");
          //  consult(idDeal);
            idDeal.setText(idDeal_);
        }*/
    }

    //visto para registrar tiendas
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_deal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.saveCategory) {
            insertCategory();
          //  Toast.makeText(this, "Agregar Categoria", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //insertar datos en la bbdd
    private void insertCategory() {
        Connection db = new Connection(this, "bdDeals", null, 1);
        SQLiteDatabase baseDatos = db.getWritableDatabase();
        String idDeal_ = idDeal.getText().toString();
        String name = nameCategory.getText().toString();
        String description = descriptionCategory.getText().toString();

        if ((!name.isEmpty()) && (!description.isEmpty())) {

            ContentValues registro = new ContentValues();

            Cursor fila = baseDatos.rawQuery("SELECT * FROM category WHERE name = " + name, null);

            if (fila.getCount() <= 0) {
                registro.put("idDeal", idDeal_);
                registro.put("name", name);
                registro.put("description", description);

                baseDatos.insert("category", null, registro);
                baseDatos.close();

                //clean();
                Toast.makeText(this, "Categoria registrada con exito.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, details_deal.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "el RUC ya existe", Toast.LENGTH_SHORT).show();
            }
        } else {
            nameCategory.setError(" ");
            descriptionCategory.setError(" ");
            nameCategory.requestFocus();
            Toast.makeText(this, "Hay campos vacios.", Toast.LENGTH_SHORT).show();
        }

    }

    private void init(){

         nameDeal = (TextView) findViewById(R.id.txtNameDealCategory);;
         idDeal = (TextView) findViewById(R.id.txtIdDealCategory);
         lblNameCategory = (TextInputLayout) findViewById(R.id.txtInputNameCategory);
         nameCategory = (TextInputEditText) findViewById(R.id.txtEditNameCategory);
         lblDescriptionCategory = (TextInputLayout) findViewById(R.id.txtInputDescriptionCategory);
         descriptionCategory = (TextInputEditText) findViewById(R.id.txtEditDescriptionCategory);
    }
}
