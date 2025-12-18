# 📝 Guía de Endpoints PUT y DELETE

## ✅ Endpoints Implementados

### PUT (Actualizar)
- `PUT /api/products/{id}` - Actualizar producto
- `PUT /api/products/{productId}/reviews/{reviewId}` - Actualizar reseña
- `PUT /api/products/{productId}/questions/{questionId}` - Responder pregunta

### DELETE (Eliminar)
- `DELETE /api/products/{id}` - Eliminar producto
- `DELETE /api/products/{productId}/reviews/{reviewId}` - Eliminar reseña
- `DELETE /api/products/{productId}/questions/{questionId}` - Eliminar pregunta

---

## 1️⃣ Actualizar Producto

### Endpoint
```
PUT http://localhost:8080/api/products/1
Content-Type: application/json
```

### Request Body (todos los campos son opcionales)
```json
{
  "title": "iPhone 15 Pro Max 512GB - Titanio Natural",
  "price": 1399.99,
  "originalPrice": 1599.99,
  "availableQuantity": 45,
  "description": "Descripción actualizada del producto...",
  "warranty": "2 años de garantía extendida",
  "imageUrls": [
    "https://images.unsplash.com/photo-new-url-1?w=800",
    "https://images.unsplash.com/photo-new-url-2?w=800"
  ],
  "attributes": [
    {
      "name": "Capacidad",
      "value": "512 GB"
    },
    {
      "name": "Color",
      "value": "Titanio Natural"
    }
  ]
}
```

### Response (200 OK)
```json
{
  "id": 1,
  "title": "iPhone 15 Pro Max 512GB - Titanio Natural",
  "price": 1399.99,
  "originalPrice": 1599.99,
  ...
}
```

### Notas Importantes
- ✅ Todos los campos son **opcionales** - solo envía los que quieres actualizar
- ❌ **NO se puede cambiar**: `sellerId`, `currency`, `condition`, `soldQuantity`, `createdAt`
- ⚠️ Si envías `imageUrls` o `attributes`, **reemplazará** completamente los existentes

---

## 2️⃣ Actualizar Reseña

### Endpoint
```
PUT http://localhost:8080/api/products/1/reviews/5
Content-Type: application/json
```

### Request Body
```json
{
  "rating": 4,
  "comment": "Actualicé mi opinión. Muy buen producto pero el precio es alto."
}
```

### Response (200 OK)
```json
{
  "id": 5,
  "rating": 4,
  "comment": "Actualicé mi opinión. Muy buen producto pero el precio es alto.",
  "userName": "Carlos M.",
  "createdAt": "2025-12-16T10:00:00"
}
```

### Notas
- ✅ Campos opcionales: `rating`, `comment`
- ❌ **NO se puede cambiar**: `userName`, `createdAt`

---

## 3️⃣ Responder Pregunta

### Endpoint
```
PUT http://localhost:8080/api/products/1/questions/3
Content-Type: application/json
```

### Request Body
```json
{
  "answer": "Sí, el producto incluye cargador rápido de 45W en la caja."
}
```

### Response (200 OK)
```json
{
  "id": 3,
  "question": "¿Incluye cargador?",
  "answer": "Sí, el producto incluye cargador rápido de 45W en la caja.",
  "userName": "Juan P.",
  "createdAt": "2025-12-14T15:00:00",
  "answeredAt": "2025-12-16T16:30:00"
}
```

### Notas
- ✅ Campo requerido: `answer`
- ⚠️ Establece automáticamente `answeredAt` con timestamp actual
- ❌ **NO se puede cambiar**: `question`, `userName`, `createdAt`

---

## 4️⃣ Eliminar Producto

### Endpoint
```
DELETE http://localhost:8080/api/products/6
```

### Response (204 No Content)
```
(Sin body)
```

### Notas
- ✅ Elimina en **cascada**: imágenes, atributos, reseñas, preguntas
- ⚠️ **Acción irreversible**
- ❌ Error 404 si el producto no existe

---

## 5️⃣ Eliminar Reseña

### Endpoint
```
DELETE http://localhost:8080/api/products/1/reviews/10
```

### Response (204 No Content)
```
(Sin body)
```

### Validaciones
- ✅ Verifica que el producto exista
- ✅ Verifica que la reseña exista
- ✅ Verifica que la reseña pertenezca al producto
- ❌ Error 404 si no cumple alguna validación

---

## 6️⃣ Eliminar Pregunta

### Endpoint
```
DELETE http://localhost:8080/api/products/1/questions/8
```

### Response (204 No Content)
```
(Sin body)
```

### Validaciones
- ✅ Verifica que el producto exista
- ✅ Verifica que la pregunta exista
- ✅ Verifica que la pregunta pertenezca al producto
- ❌ Error 404 si no cumple alguna validación

---

## 🧪 Ejemplos con cURL

### Actualizar Producto
```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "price": 1299.99,
    "availableQuantity": 40
  }'
```

### Actualizar Reseña
```bash
curl -X PUT http://localhost:8080/api/products/1/reviews/5 \
  -H "Content-Type: application/json" \
  -d '{
    "rating": 5,
    "comment": "Cambié mi opinión, es excelente!"
  }'
```

### Responder Pregunta
```bash
curl -X PUT http://localhost:8080/api/products/1/questions/3 \
  -H "Content-Type: application/json" \
  -d '{
    "answer": "Sí, incluye garantía de 2 años."
  }'
```

### Eliminar Producto
```bash
curl -X DELETE http://localhost:8080/api/products/6
```

### Eliminar Reseña
```bash
curl -X DELETE http://localhost:8080/api/products/1/reviews/10
```

### Eliminar Pregunta
```bash
curl -X DELETE http://localhost:8080/api/products/1/questions/8
```

---

## 📊 Resumen Completo de Endpoints

| Método | Endpoint | Descripción | Status |
|--------|----------|-------------|--------|
| **GET** | `/api/products` | Listar productos | 200 OK |
| **GET** | `/api/products/{id}` | Ver producto | 200 OK |
| **POST** | `/api/products` | Crear producto | 201 Created |
| **PUT** | `/api/products/{id}` | Actualizar producto | 200 OK |
| **DELETE** | `/api/products/{id}` | Eliminar producto | 204 No Content |
| **GET** | `/api/products/{id}/reviews` | Ver reseñas | 200 OK |
| **POST** | `/api/products/{id}/reviews` | Agregar reseña | 201 Created |
| **PUT** | `/api/products/{pid}/reviews/{rid}` | Actualizar reseña | 200 OK |
| **DELETE** | `/api/products/{pid}/reviews/{rid}` | Eliminar reseña | 204 No Content |
| **GET** | `/api/products/{id}/questions` | Ver preguntas | 200 OK |
| **POST** | `/api/products/{id}/questions` | Agregar pregunta | 201 Created |
| **PUT** | `/api/products/{pid}/questions/{qid}` | Responder pregunta | 200 OK |
| **DELETE** | `/api/products/{pid}/questions/{qid}` | Eliminar pregunta | 204 No Content |

---

## ⚠️ Errores Comunes

### 404 Not Found
```json
{
  "timestamp": "2025-12-16T16:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found with id: 999"
}
```

### 404 - Recurso no pertenece al producto
```json
{
  "timestamp": "2025-12-16T16:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Review 10 does not belong to product 1"
}
```

### 400 Bad Request - Validación
```json
{
  "timestamp": "2025-12-16T16:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": {
    "price": "Price must be greater than 0",
    "rating": "Rating must be between 1 and 5"
  }
}
```

---

## ✅ Características

- ✅ **Actualización parcial** (PUT) - solo envía campos a modificar
- ✅ **Validación completa** de existencia y pertenencia
- ✅ **Cascade delete** automático
- ✅ **Transacciones** para consistencia
- ✅ **HTTP Status apropiados** (200, 204, 404)
- ✅ **Timestamps automáticos** (answeredAt)
- ✅ **Mensajes de error descriptivos**

---

## 🎯 CRUD Completo Implementado

✅ **C**reate - POST  
✅ **R**ead - GET  
✅ **U**pdate - PUT  
✅ **D**elete - DELETE  

¡API REST completa lista para usar! 🚀
