package com.example.deals.adapters;

import android.content.Context;
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
import com.example.deals.details_category;
import com.example.deals.details_deal;
import com.example.deals.entities.CategoryVo;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    ArrayList<CategoryVo> mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public CategoryAdapter(ArrayList<CategoryVo> myDataset) {
        this.mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // creamos una vista nueva
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_categories_layout, null, false);
        return new CategoryViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        //asignamos los datos al cardView

        holder.txtIdCategory.setText(mDataset.get(position).getId());
        holder.txtNameCategory.setText(mDataset.get(position).getName());
        holder.txtDescriptionCategory.setText(mDataset.get(position).getDescription());
        holder.txtIdDealOnCardCategory.setText(mDataset.get(position).getIdDeal());
        //botones
        holder.setOnClickListeners();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public static class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context context;

        // dentro de esta clase hay que hacer referencia a los items que aparezcan en nuestro item (elemento de la lista)

        // ImageView logoDealMain;
        TextView txtIdCategory;
        TextView txtNameCategory;
        TextView txtDescriptionCategory;
        TextView txtNumProducts;
        TextView txtIdDealOnCardCategory;

        //hacemos referencia a los botones para asignar eventos
        Button btnMoreDetails;
        Button btnDestroyDeal;

        CategoryViewHolder(View v) {
            super(v);
            context = v.getContext();
            txtIdCategory = (TextView) v.findViewById(R.id.txtIdCategory);
            txtNameCategory = (TextView) v.findViewById(R.id.txtNameCategory);
            txtDescriptionCategory = (TextView) v.findViewById(R.id.txtDescriptionCategory);
            txtNumProducts = (TextView) v.findViewById(R.id.txtNumProducts);

            //para poder enviar el id de la tienda hay que cargarlo en el card view de categoria
            txtIdDealOnCardCategory = (TextView) v.findViewById(R.id.txtIdDealOnCardCategory);

            //botones
            btnMoreDetails = (Button) v.findViewById(R.id.btnMoreDetailsCategory);
            btnDestroyDeal = (Button) v.findViewById(R.id.btnDestroyCategory);
        }

        void setOnClickListeners() {
            btnMoreDetails.setOnClickListener(this);
            btnDestroyDeal.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String id = txtIdCategory.getText().toString();
            String name = txtNameCategory.getText().toString();
            String idDeal_ = txtIdDealOnCardCategory.getText().toString();
            switch (v.getId()) {
                case R.id.btnMoreDetailsCategory:
                    Intent intent = new Intent(context, details_category.class); ///hay que enviar a la actividad details_category
                    //enviamos el id y el nombre de una actividad a otra
                    intent.putExtra("idCategory", id);
                    intent.putExtra("nameCategory", name);
                    intent.putExtra("idDeal", idDeal_);
                    Toast.makeText(context, "Categoría: " + name, Toast.LENGTH_SHORT).show();
                    context.startActivity(intent);
                    break;
                case R.id.btnDestroyCategory:
                    Connection db = new Connection(v.getContext(), "bdDeals", null, 1);
                    SQLiteDatabase baseDatos = db.getWritableDatabase();

                    if (!id.equals("")) {
                        Cursor fila = baseDatos.rawQuery("SELECT * FROM category WHERE id = " + id, null);
                        if (fila.getCount() <= 0) {
                            Toast.makeText(v.getContext(), "Nada para eliminar.", Toast.LENGTH_SHORT).show();
                        } else {
                            baseDatos.delete("product", "idCategory = " + id, null);
                            baseDatos.delete("category", "id = " + id, null);
                            baseDatos.close();
                            Toast.makeText(v.getContext(), "Se elimino: " + name, Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(context, details_deal.class);
                            intent1.putExtra("idDeal", idDeal_);
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

