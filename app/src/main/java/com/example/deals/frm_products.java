package com.example.deals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.deals.bd.Connection;
import com.example.deals.entities.CategoryVo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class frm_products extends AppCompatActivity {

    final int REQUEST_CODE_GALLERY = 999;

    Toolbar toolbar;

    Spinner spinnerCategory;

    ImageView imgProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_products);

        init();

        //agrega la flecha para regresar en toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_frmProduct);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        //recuperar variables que envia detail_deal
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String idDeal = extras.getString("idDeal");
            //consultar los datos de la categoria
            loadCategoryInSpinner(idDeal);
            //
        }




    }

    //visto para registrar productos
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int idCategory = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (idCategory == R.id.saveProduct) {
            //insertProduct();
            Toast.makeText(this, "Agregar Producto", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //FUNCIONES PARA LA IMAGEN
    //de imagen a byte
    private byte[] ImageViewToByte(ImageView imgProduct) {
        Bitmap bitmap = ((BitmapDrawable) imgProduct.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    //cambiar el logo
    public void choseImgProduct(View v) {
        ActivityCompat.requestPermissions(frm_products.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
    }

    //poner el logo por defecto
    public void imgDefaultProduct(View v) {
        imgProduct.setImageResource(R.drawable.ic_product_default);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(this, "Sin acceso.", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgProduct.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init() {
        imgProduct = (ImageView) findViewById(R.id.ImagePrevProduct);
        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
    }
    //CARGAR CATEGORIAS
    //agregar vistas al recyclerView

    private void loadCategoryInSpinner(String idDeal_) {
        List<String> listSpinnerCategory = new ArrayList<String>();
        Connection db = new Connection(this, "bdDeals", null, 1);
        SQLiteDatabase baseDatos = db.getWritableDatabase();
        if (db != null) {
            Cursor fila = baseDatos.rawQuery("SELECT * FROM category WHERE idDeal = '" + idDeal_ + "' ORDER BY id DESC", null);
            int i = 0;
            if (fila.moveToFirst()) {
                do {
                    String id = fila.getString(0);
                    String name = fila.getString(1);
                    listSpinnerCategory.add(name);
                    i++;
                } while (fila.moveToNext());
                //llenar spiner con las categorias

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_categories, listSpinnerCategory);
                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item_categories);
                spinnerCategory.setAdapter(spinnerArrayAdapter);
            }

        }
    }

}
