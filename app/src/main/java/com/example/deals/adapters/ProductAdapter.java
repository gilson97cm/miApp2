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

import com.example.deals.R;
import com.example.deals.bd.Connection;
import com.example.deals.details_category;
import com.example.deals.entities.ProductVo;
import com.example.deals.frm_edit_products;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    ArrayList<ProductVo> mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ProductAdapter(ArrayList<ProductVo> myDataset) {
        this.mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // creamos una vista nueva
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_products_layout, null, false);
        return new ProductViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //asignamos los datos al cardView
        holder.txtIdProductInCard.setText(mDataset.get(position).getId());
        holder.txtNameProduct.setText(mDataset.get(position).getName());
        holder.txtDescriptionProduct.setText("Descripción:  "+mDataset.get(position).getDescription());
        holder.txtPriceProduct.setText("Precio:  $"+mDataset.get(position).getPrice());
        holder.txtStockProduct.setText("Stock:  "+mDataset.get(position).getStock());
        holder.txtIdCategoryInCardProduct.setText(mDataset.get(position).getIdCategory());

        //imagen
        byte[] ImgProduct = mDataset.get(position).getImgProduct();
        Bitmap bitmap = BitmapFactory.decodeByteArray(ImgProduct, 0, ImgProduct.length);
        holder.imgProductDetail.setImageBitmap(bitmap);

        //botones
        holder.setOnClickListeners();

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public static class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context context;

        // dentro de esta clase hay que hacer referencia a los items que aparezcan en nuestro item (elemento de la lista)
        ImageView imgProductDetail;

        TextView txtIdProductInCard;
        TextView txtNameProduct;
        TextView txtDescriptionProduct;
        TextView txtPriceProduct;
        TextView txtStockProduct;
        TextView txtIdCategoryInCardProduct;

        //hacemos referencia a los botones para asignar eventos
        Button btnEditProduct;
        Button btnDestroyProduct;

        ProductViewHolder(View v) {
            super(v);
            context = v.getContext();
            imgProductDetail = (ImageView) v.findViewById(R.id.imgProductDetail);

            txtIdProductInCard = (TextView) v.findViewById(R.id.txtIdProductInCard);
            txtNameProduct = (TextView) v.findViewById(R.id.txtNameProduct);
            txtDescriptionProduct = (TextView) v.findViewById(R.id.txtDescriptionProduct);
            txtPriceProduct = (TextView) v.findViewById(R.id.txtPriceProduct);
            txtStockProduct = (TextView) v.findViewById(R.id.txtStockProduct);
            txtIdCategoryInCardProduct = (TextView) v.findViewById(R.id.txtIdCategoryInCardProduct);

            //botones
            btnEditProduct = (Button) v.findViewById(R.id.btnEditProduct);
            btnDestroyProduct = (Button) v.findViewById(R.id.btnDestroyProduct);

        }
        void setOnClickListeners() {
            btnEditProduct.setOnClickListener(this);
            btnDestroyProduct.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String id = txtIdProductInCard.getText().toString();
            String name = txtNameProduct.getText().toString();
            String idCategory_ = txtIdCategoryInCardProduct.getText().toString();
            String idDeal_ = "0";
            switch (v.getId()) {
                case R.id.btnEditProduct:
                    Toast.makeText(context, "Producto: " + name, Toast.LENGTH_SHORT).show();
                  Intent intent = new Intent(context, frm_edit_products.class);
                    intent.putExtra("idCategory",idCategory_);
                    intent.putExtra("idProduct",id);
                    context.startActivity(intent);
                    break;
                case R.id.btnDestroyProduct:
                    Connection db = new Connection(v.getContext(), "bdDeals", null, 1);
                    SQLiteDatabase baseDatos = db.getWritableDatabase();
                    if (!id.equals("")) {
                            baseDatos.delete("product", "id = " + id, null);
                            baseDatos.close();
                            Toast.makeText(v.getContext(), "Se elimino: " + name, Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(context, details_category.class);
                            intent1.putExtra("idDeal",idDeal_);
                           intent1.putExtra("idCategory",idCategory_);
                            context.startActivity(intent1);

                    } else {
                        Toast.makeText(v.getContext(), "Nada para eliminar.", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }
}

