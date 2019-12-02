package com.example.deals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deals.bd.Connection;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class details_deal extends AppCompatActivity {
    FloatingActionButton btnAddCategory;
    Toolbar toolbar;
    TextView nameDeal;
    TextView addressDeal;
    TextView rucDeal;
    ImageView logoDeal;
    TextView idDeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_deal);

        //agrega la flecha para regresar en toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
    }

    public void consult (String id){
        Connection db = new Connection(this, "bdDeals", null, 1);
        SQLiteDatabase baseDatos = db.getWritableDatabase();

            Cursor fila = baseDatos.rawQuery("SELECT id, ruc, name, direction, logo FROM deal WHERE id = " + id, null);

            if (fila.moveToFirst()) {
                idDeal.setText(fila.getString(0));
                rucDeal.setText(fila.getString(1));
                nameDeal.setText(fila.getString(2));
                addressDeal.setText(fila.getString(3));

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

    }

    public void gotoFrmCategory(View v){
        Intent intent = new Intent(v.getContext(), frm_categories.class);
        intent.putExtra("idDeal",idDeal.getText().toString());
        startActivity(intent);
    }

}

