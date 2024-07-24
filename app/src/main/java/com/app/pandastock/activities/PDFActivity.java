package com.app.pandastock.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.app.pandastock.R;
import com.app.pandastock.firebase.VentaDao;
import com.app.pandastock.models.Venta;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.List;

public class PDFActivity extends AppCompatActivity {

    private static final String FILE_NAME = "reporte_ventas.pdf";
    private VentaDao ventaDao;
    private static final int REQUEST_WRITE_STORAGE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfactivity);

        ventaDao = new VentaDao(this);

        // Verificar permisos
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Solicitar permisos si no están garantizados
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        } else {
            // Permiso ya concedido, obtener datos y generar PDF
            obtenerDatosDeFirestore();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, proceder con la generación de PDF
                obtenerDatosDeFirestore();
            } else {
                // Permiso denegado, informar al usuario
                Toast.makeText(this, "Permiso de escritura en almacenamiento externo denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void obtenerDatosDeFirestore() {
        ventaDao.getAllVentas(new VentaDao.FirestoreCallback<List<Venta>, Void>() {
            @Override
            public void onComplete(List<Venta> ventas, Void aVoid) {
                if (ventas != null && !ventas.isEmpty()) {
                    // Generar el PDF una vez obtenidos los datos
                    Toast.makeText(PDFActivity.this, "Ventas obtenidas: " + ventas.size(), Toast.LENGTH_SHORT).show();
                    createPdf(ventas);
                } else {
                    Toast.makeText(PDFActivity.this, "No se encontraron ventas.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createPdf(List<Venta> ventas) {
        String directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        File file = new File(directoryPath, FILE_NAME);

        try {
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Agregar título
            document.add(new Paragraph("Reporte de Ventas"));

            // Agregar fecha actual
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = sdf.format(new java.util.Date());
            document.add(new Paragraph("Fecha: " + currentDate));

            // Agregar detalles de ventas
            for (Venta venta : ventas) {
                document.add(new Paragraph("Venta ID: " + venta.getId()));
                document.add(new Paragraph("Cliente: " + venta.getNombreCliente() + " " + venta.getApellidoCliente()));
                document.add(new Paragraph("Monto Total: " + venta.getMontoTotal()));
                // Añadir más detalles según tu estructura de datos
                document.add(new Paragraph("---------------------------------------------"));
            }

            // Cerrar documento
            document.close();
            Toast.makeText(this, "PDF generado exitosamente: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al crear el archivo PDF", Toast.LENGTH_SHORT).show();
        }
    }
}
