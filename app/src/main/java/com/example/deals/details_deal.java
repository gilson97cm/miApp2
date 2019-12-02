package com.example.deals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class details_deal extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtNameDealDetail;

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

        init();

        //recuperar variables que envia MainActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String idDeal = extras.getString("idDeal");
            String nameDeal = extras.getString("nameDeal");
            txtNameDealDetail.setText(nameDeal);
        }
    }

    public void init(){
        txtNameDealDetail = (TextView) findViewById(R.id.txtNameDealDetail);
    }
}

