package com.app.pandastock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.app.pandastock.R;
import com.app.pandastock.adapters.ReportePagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ReporteActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        ImageButton btnBack = findViewById(R.id.btnBack);

        ReportePagerAdapter pagerAdapter = new ReportePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // Configurar botón de retroceso
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ReporteActivity.this, MenuInicoActivity.class);
            startActivity(intent);
            finish();});

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Productos Vendidos");
                            break;
                        case 1:
                            tab.setText("Tipo Producto");
                            break;
                        case 2:
                            tab.setText("Ventas x Tiempo");
                            break;
                        case 3:
                            tab.setText("Movimiento Inventario");
                            break;
                    }
                }).attach();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ReporteActivity.this, MenuInicoActivity.class);
        startActivity(intent);
        finish();
    }
}

