package com.app.pandastock.database;

public class DatabaseContract {
    public static class UsuarioEntry {
        public static final String TABLE_NAME = "Usuarios";
        public static final String COL_ID = "Id";
        public static final String COL_NOMBRE = "Nombres";
        public static final String COL_APELLIDO = "Apellidos";
        public static final String COL_EMAIL = "Email";
        public static final String COL_CONTRASENA = "Contrasena";
    }

    public static class ProductoEntry {
        public static final String TABLE_NAME = "Productos";
        public static final String COL_ID = "Id";
        public static final String COL_TIPO_PRODUCTO_ID = "IdTipoProducto";
        public static final String COL_MARCA_ID = "IdMarca";
        public static final String COL_MODELO = "Modelo";
        public static final String COL_PRECIO = "Precio";
        public static final String COL_STOCK = "Stock";
        public static final String COL_FECHA_CREACION = "FechaCreacion";
        public static final String COL_FECHA_ACTUALIZACION = "FechaActualizacion";
    }

    public static class InventarioProductoEntry {
        public static final String TABLE_NAME = "InventarioProducto";
        public static final String COL_ID = "Id";
        public static final String COL_PRODUCTO_ID = "IdProducto";
        public static final String COL_CODIGO_BARRAS = "CodigoBarras";
    }

    public static class TipoProductoEntry {
        public static final String TABLE_NAME = "TipoProducto";
        public static final String COL_ID = "Id";
        public static final String COL_NOMBRE = "Nombre";
    }

    public static class MarcaEntry {
        public static final String TABLE_NAME = "Marcas";
        public static final String COL_ID = "Id";
        public static final String COL_NOMBRE = "Nombre";
        public static final String COL_TIPO_PRODUCTO_ID = "IdTipoProducto";
    }
}
