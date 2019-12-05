package com.example.deals.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.deals.MainActivity;
import com.example.deals.R;
import com.example.deals.bd.Connection;
import com.example.deals.details_deal;
import com.example.deals.entities.DealVo;

import java.util.ArrayList;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealViewHolder> {
    ArrayList<DealVo> mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public DealAdapter(ArrayList<DealVo> myDataset) {
        this.mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // creamos una vista nueva
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_deals_layout, null, false);
        return new DealViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(DealViewHolder holder, int position) {
        //asignamos los datos al cardView

        holder.txtIdDeal.setText(mDataset.get(position).getId());
        holder.txtNameDeal.setText(mDataset.get(position).getName());
        holder.txtDirectionDeal.setText(mDataset.get(position).getDirection());

        //imagen
        byte[] logoDeal = mDataset.get(position).getLogo();
        Bitmap bitmap = BitmapFactory.decodeByteArray(logoDeal, 0, logoDeal.length);
        holder.logoDealMain.setImageBitmap(bitmap);

        //botones
        holder.setOnClickListeners();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public static class DealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context context;

        // dentro de esta clase hay que hacer referencia a los items que aparezcan en nuestro item (elemento de la lista)
        ImageView logoDealMain;
        TextView txtIdDeal;
        TextView txtNameDeal;
        TextView txtDirectionDeal;
        TextView txtNumCategory;

        //hacemos referencia a los botones para asignar eventos
        Button btnMoreDetails;
        Button btnDestroyDeal;

        DealViewHolder(View v) {
            super(v);
            context = v.getContext();
            logoDealMain = (ImageView) v.findViewById(R.id.logoDealMain);
            txtIdDeal = (TextView) v.findViewById(R.id.txtIdDeal);
            txtNameDeal = (TextView) v.findViewById(R.id.txtNameDeal);
            txtDirectionDeal = (TextView) v.findViewById(R.id.txtDirectionDeal);
            txtNumCategory = (TextView) v.findViewById(R.id.txtNumCategory);

            //botones
            btnMoreDetails = (Button) v.findViewById(R.id.btnMoreDetails);
            btnDestroyDeal = (Button) v.findViewById(R.id.btnDestroyDeal);
        }

        void setOnClickListeners() {
            btnMoreDetails.setOnClickListener(this);
            btnDestroyDeal.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String id = txtIdDeal.getText().toString();
            String name = txtNameDeal.getText().toString();
            switch (v.getId()) {
                case R.id.btnMoreDetails:
                    Intent intent = new Intent(context, details_deal.class);
                    //enviamos el id y el nombre de una actividad a otra
                    intent.putExtra("idDeal", id);
                    intent.putExtra("nameDeal", name);
                    Toast.makeText(context, "Tienda: " + name, Toast.LENGTH_SHORT).show();
                    context.startActivity(intent);
                    break;
                case R.id.btnDestroyDeal:
                    Connection db = new Connection(v.getContext(), "bdDeals", null, 1);
                    SQLiteDatabase baseDatos = db.getWritableDatabase();

                    if (!id.equals("")) {
                        Cursor fila = baseDatos.rawQuery("SELECT * FROM deal WHERE id = " + id, null);
                        if (fila.getCount() <= 0) {
                            Toast.makeText(v.getContext(), "Nada para eliminar.", Toast.LENGTH_SHORT).show();
                        } else {
                            baseDatos.delete("category", "idDeal = " + id, null);
                            baseDatos.delete("deal", "id = " + id, null);
                            baseDatos.close();
                            Toast.makeText(v.getContext(), "Se elimino: " + name, Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(context, MainActivity.class);
                            context.startActivity(intent1);
                        }
                    } else {
                        Toast.makeText(v.getContext(), "Nada para eliminar.", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }
}
