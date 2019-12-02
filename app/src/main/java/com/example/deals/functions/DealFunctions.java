package com.example.deals.functions;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.deals.MainActivity;
import com.example.deals.bd.Connection;
import com.example.deals.details_deal;

public class DealFunctions {
    //eliminar la tienda
    public void destroyDeal(View v, String id,String name) {
        Connection db = new Connection(v.getContext(), "bdDeals", null, 1);
        SQLiteDatabase baseDatos = db.getWritableDatabase();

        if (!id.equals("")) {
            Cursor fila = baseDatos.rawQuery("SELECT * FROM deal WHERE id = " + id, null);

            if (fila.getCount() <= 0) {
                Toast.makeText(v.getContext(), "Nada para eliminar.", Toast.LENGTH_SHORT).show();
            } else {
                baseDatos.delete("deal", "id = " + id, null);
                baseDatos.close();
                Toast.makeText(v.getContext(), "Se elimino: "+name, Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(v.getContext(), MainActivity.class);
               // startActivity(intent);
                //finish();
            }
        } else {
            Toast.makeText(v.getContext(), "Nada para eliminar.", Toast.LENGTH_SHORT).show();
        }

    }


}
