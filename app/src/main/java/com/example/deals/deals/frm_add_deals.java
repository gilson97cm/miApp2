package com.example.deals.deals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentValues;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.deals.MainActivity;
import com.example.deals.R;
import com.example.deals.bd.Connection;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class frm_add_deals extends AppCompatActivity {
    Toolbar toolbar;
    TextInputLayout txtInputRuc;
    TextInputLayout txtInputNameDeal;
    TextInputLayout txtInputDirection;

    TextInputEditText txtEditRuc;
    TextInputEditText txtEditNameDeal;
    TextInputEditText txtEditDirection;


    ImageView logoDeal;
    Button btnChoseImgDeal;

    MainActivity functions = new MainActivity();

    final int REQUEST_CODE_GALLERY = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_add_deals);


        //agrega la flecha para regresar en toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_frmDeal);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
        init();
    }

    //visto para registrar tiendas
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_deal_frm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.saveDeal) {
            insertDeal();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //insertar datos en la bbdd
    private void insertDeal() {
        Connection db = new Connection(this, "bdDeals", null, 1);
        SQLiteDatabase baseDatos = db.getWritableDatabase();
        String ruc = txtEditRuc.getText().toString();
        String name = txtEditNameDeal.getText().toString();
        String direction = txtEditDirection.getText().toString();
        byte[] imgDeal = ImageViewToByte(logoDeal);

        if ((!ruc.isEmpty()) && (!name.isEmpty()) && (!direction.isEmpty())) {

            ContentValues registro = new ContentValues();

            Cursor fila = baseDatos.rawQuery("SELECT * FROM deal WHERE ruc = " + ruc, null);

            if (fila.getCount() <= 0) {
                registro.put("ruc", ruc);
                registro.put("name", name);
                registro.put("direction", direction);
                registro.put("logo", imgDeal);

                baseDatos.insert("deal", null, registro);
                baseDatos.close();

               // clean();
                Toast.makeText(this, "Tienda registrada con exito.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "el RUC ya existe", Toast.LENGTH_SHORT).show();
            }
        } else {
            txtInputRuc.setError(" ");
            txtInputNameDeal.setError(" ");
            txtInputDirection.setError(" ");
            txtEditRuc.requestFocus();
            Toast.makeText(this, "Hay campos vacios.", Toast.LENGTH_SHORT).show();
        }

    }

    //de imagen a byte
    private byte[] ImageViewToByte(ImageView logoDeal) {
        Bitmap bitmap = ((BitmapDrawable) logoDeal.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    //cambiar el logo
    public void choseImg(View v) {
        ActivityCompat.requestPermissions(frm_add_deals.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
    }
    //poner el logo por defecto
    public void imgDefault(View v){
        logoDeal.setImageResource(R.drawable.ic_default);
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
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                logoDeal.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //iniciar elementos de la vista
    private void init() {
        txtInputRuc = (TextInputLayout) findViewById(R.id.txtInputRuc);
        txtInputNameDeal = (TextInputLayout) findViewById(R.id.txtInputNameDeal);
        txtInputDirection = (TextInputLayout) findViewById(R.id.txtInputDirection);


        txtEditRuc = (TextInputEditText) findViewById(R.id.txtEditRuc);
        txtEditNameDeal = (TextInputEditText) findViewById(R.id.txtEditNameDeal);
        txtEditDirection = (TextInputEditText) findViewById(R.id.txtEditDirection);


        logoDeal = (ImageView) findViewById(R.id.logoPrevDeal);
        btnChoseImgDeal = (Button) findViewById(R.id.btnChoseImgDeal);

    }

    //limpiar formularios
    private void clean() {
        txtInputRuc.setError(null);
        txtInputNameDeal.setError(null);
        txtInputDirection.setError(null);
        txtEditRuc.setText("");
        txtEditRuc.requestFocus();
        txtEditNameDeal.setText("");
        txtEditDirection.setText("");
        logoDeal.setImageResource(R.drawable.ic_default);
    }

}
