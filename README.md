# 📦 Product API --- Documentación de Endpoints

API REST para la gestión de productos. Permite crear, consultar,
actualizar, eliminar (soft delete), filtrar por categoría y obtener
estadísticas.

Base path: /api/products

## 📘 Endpoints

## ➕ Crear producto

### POST /api/products

Crea un nuevo producto.

#### Request body

``` json
{
  "name": "Producto A",
  "category": "Categoria X",
  "price": 100.0,
  "active": true
}
```

## 🔍 Obtener producto por ID

### GET /api/products/{id}

## 📋 Listar productos

### GET /api/products

Query params: name, category

## ✔️ Productos activos

### GET /api/products/active

## ✏️ Actualizar producto

### PUT /api/products/{id}

## ❌ Eliminar producto

### DELETE /api/products/{id}

## 🏷️ Productos por categoría

### GET /api/products/category/{category}

## 🏷️✔️ Productos activos por categoría

### GET /api/products/category/{category}/active

## 📚 Todas las categorías

### GET /api/products/categories

## 🔢 Cantidad por categoría

### GET /api/products/categories/count

## 🔍 Buscar categorías

### GET /api/products/categories/search?q=

## 📦 Por múltiples categorías

### POST /api/products/by-categories

## ⚠️ Errores

-   404 ProductNotFoundException
-   400 IllegalArgumentException
-   409 IllegalStateException
