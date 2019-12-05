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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class frm_edit_categories extends AppCompatActivity {

    Toolbar toolbar;
    TextView nameDeal;
    TextView idDeal;
    TextView txtIdCategory;
    TextInputLayout lblNameCategory;
    TextInputEditText nameCategory;
    TextInputLayout lblDescriptionCategory;
    TextInputEditText descriptionCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_edit_categories);
        init();
        toolbar = (Toolbar) findViewById(R.id.toolbar_frmCategoryEdit);

        //recuperar variables que envia detail_category
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String idDeal_ = extras.getString("idDeal");
            String nameDeal_ = extras.getString("nameDeal");
            String idCategory_ = extras.getString("idCategory");
            idDeal.setText(idDeal_);
            nameDeal.setText(nameDeal_);
            txtIdCategory.setText(idCategory_);
            consultCategory(idCategory_);
            //
        }

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), details_category.class);
                intent.putExtra("idDeal", idDeal.getText().toString());
                intent.putExtra("idCategory", txtIdCategory.getText().toString());
                startActivity(intent);
                finish();
            }
        });
    }

    //lapiz para registrar editar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_category_frm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.editCategoryFrm) {
            String idCategory_ = txtIdCategory.getText().toString();
            String idDeal_ = idDeal.getText().toString();
            updateCategory(idCategory_,idDeal_);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void init() {

        txtIdCategory = (TextView) findViewById(R.id.txtIdCategory);
        nameDeal = (TextView) findViewById(R.id.txtNameDealCategory);
        idDeal = (TextView) findViewById(R.id.txtIdDealCategory);
        lblNameCategory = (TextInputLayout) findViewById(R.id.txtInputNameCategory);
        nameCategory = (TextInputEditText) findViewById(R.id.txtEditNameCategory);
        lblDescriptionCategory = (TextInputLayout) findViewById(R.id.txtInputDescriptionCategory);
        descriptionCategory = (TextInputEditText) findViewById(R.id.txtEditDescriptionCategory);
    }

    private void consultCategory(String idCategory_){
        Connection db = new Connection(this, "bdDeals", null, 1);
        SQLiteDatabase baseDatos = db.getWritableDatabase();

        Cursor fila = baseDatos.rawQuery("SELECT * FROM category WHERE id = " + idCategory_, null);

        if (fila.moveToFirst()) {
            nameCategory.setText(fila.getString(1));
            descriptionCategory.setText(fila.getString(2));

        } else {
            Toast.makeText(this, "No hay registro.", Toast.LENGTH_SHORT).show();
        }
        baseDatos.close();
    }

    private void updateCategory (String idCategory_, String idDeal_){
        Connection db = new Connection(this, "bdDeals", null, 1);
        SQLiteDatabase baseDatos = db.getWritableDatabase();

        String name = nameCategory.getText().toString();
        String description = descriptionCategory.getText().toString();


        if ((!name.isEmpty()) && (!description.isEmpty())) {
         //   Cursor fila = baseDatos.rawQuery("SELECT * FROM category WHERE name = "+name,null);
            //if(fila.getCount() <= 0){
            ContentValues registro = new ContentValues();
            registro.put("name", name);
            registro.put("description", description);

            baseDatos.update("category", registro, "id = "+idCategory_,null);
            baseDatos.close();

            // clean();
            Toast.makeText(this, "Categoría actualizada con exito.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, details_category.class);
            intent.putExtra("idCategory",idCategory_);
            intent.putExtra("idDeal",idDeal_);
            startActivity(intent);
            finish();
            //}else{
            //  Toast.makeText(this, "El RUC ya existe", Toast.LENGTH_SHORT).show();
            //}

        } else {
            lblNameCategory.setError(" ");
            lblDescriptionCategory.setError(" ");
            nameCategory.requestFocus();
            Toast.makeText(this, "Hay campos vacios.", Toast.LENGTH_SHORT).show();
        }
    }
}
