package com.app.pandastock.firebase;

public class FirestoreContract {

    // Colección Usuarios
    public static class UsuarioEntry {
        public static final String COLLECTION_NAME = "AUsuarios";
        public static final String DOC_ID = "Id";
        public static final String FIELD_NOMBRE = "Empresa";
        public static final String FIELD_EMAIL = "Email";
        public static final String FIELD_CONTRASENA = "Contrasena";
    }

    // Colección Productos
    public static class ProductoEntry {
        public static final String COLLECTION_NAME = "AProductos";
        public static final String DOC_ID = "Id";
        public static final String FIELD_TIPO_PRODUCTO_REF = "tipoProductoRef";
        public static final String FIELD_MARCA_REF = "marcaRef";
        public static final String FIELD_MODELO = "Modelo";
        public static final String FIELD_PRECIO = "Precio";
        public static final String ARRAY_CODES_BAR = "CodigoBarra";
        public static final String FIELD_STOCK = "Stock";
        public static final String FIELD_FECHA_CREACION = "FechaCreacion";
        public static final String FIELD_FECHA_ACTUALIZACION = "FechaActualizacion";
    }

    // Colección MovimientoInventario
    public static class MovimientoInventarioEntry {
        public static final String COLLECTION_NAME = "AMovimientoInventario";
        public static final String DOC_ID = "Id";
        public static final String FIELD_USUARIO_REF = "Usuario";
        public static final String FIELD_PRODUCTO_REF = "Producto";
        public static final String ARRAY_CODES_BAR = "CodigoBarra";
        public static final String FIELD_CANTIDAD = "Cantidad";
        public static final String FIELD_TIPO = "Tipo";
        public static final String FIELD_FECHA = "FechaRegistro";
    }

    // Colección TipoProducto
    public static class TipoProductoEntry {
        public static final String COLLECTION_NAME = "TipoProducto";
        public static final String DOC_ID = "Id";
        public static final String FIELD_NOMBRE = "Nombre";
    }

    // Colección Marcas
    public static class MarcaEntry {
        public static final String COLLECTION_NAME = "Marcas";
        public static final String DOC_ID = "Id";
        public static final String FIELD_NOMBRE = "Nombre";
        public static final String FIELD_TIPO_PRODUCTO_ID = "IdTipoProducto";
    }

    // Colección Ventas
    public static class VentaEntry {
        public static final String COLLECTION_NAME = "AVentas";
        public static final String DOC_ID = "Id";
        public static final String FIELD_NOMBRE_CLIENTE = "NombreCliente";
        public static final String FIELD_APELLIDO_CLIENTE = "ApellidoCliente";
        public static final String FIELD_CELULAR = "Celular";
        public static final String FIELD_DNI = "Dni";
        public static final String FIELD_EMPLEADO_REF = "Empleado";
        public static final String FIELD_MONTO_TOTAL = "MontoTotal";
        public static final String FIELD_FECHA_CREACION = "FechaCreacion";
    }

    // Subcolección DetallesVenta dentro de Ventas
    public static class DetalleVentaEntry {
        public static final String COLLECTION_NAME = "ADetallesVenta";
        public static final String DOC_ID = "Id";
        public static final String FIELD_VENTA_REF = "Venta";
        public static final String FIELD_PRODUCTO_REF = "Producto";
        public static final String FIELD_CANTIDAD = "Cantidad";
        public static final String FIELD_PRECIO = "Precio";
        public static final String FIELD_SUBTOTAL = "SubTotal";
    }
}
