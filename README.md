# 📦 Product API — Documentación de Endpoints

API REST para la gestión de productos. Permite crear, consultar, actualizar, eliminar (soft delete), filtrar por categoría y obtener estadísticas.

Base path:

```json
/api/products
```

## 📘 Endpoints


## Crear producto  
### **POST** `/api/products`
Crea un nuevo producto.

#### Request body
```json
{
  "name": "Producto A",
  "category": "Categoria X",
  "price": 100.0,
  "active": true
}
```