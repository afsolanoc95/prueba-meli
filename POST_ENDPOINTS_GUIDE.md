# 📝 Guía de Endpoints POST

## ✅ Endpoints Implementados

### 1. Crear Producto
```
POST /api/products
```

### 2. Agregar Reseña
```
POST /api/products/{id}/reviews
```

### 3. Agregar Pregunta
```
POST /api/products/{id}/questions
```

---

## 1️⃣ Crear Producto

### Endpoint
```
POST http://localhost:8080/api/products
Content-Type: application/json
```

### Request Body
```json
{
  "title": "Nintendo Switch OLED",
  "price": 349.99,
  "originalPrice": 399.99,
  "currency": "USD",
  "availableQuantity": 80,
  "condition": "new",
  "description": "Nintendo Switch modelo OLED con pantalla de 7 pulgadas, 64GB de almacenamiento, dock mejorado y audio envolvente.",
  "warranty": "1 año de garantía Nintendo",
  "sellerId": 2,
  "imageUrls": [
    "https://images.unsplash.com/photo-1578303512597-81e6cc155b3e?w=800",
    "https://images.unsplash.com/photo-1606144042614-b2417e99c4e3?w=800"
  ],
  "attributes": [
    {
      "name": "Marca",
      "value": "Nintendo"
    },
    {
      "name": "Modelo",
      "value": "Switch OLED"
    },
    {
      "name": "Pantalla",
      "value": "7 pulgadas OLED"
    },
    {
      "name": "Almacenamiento",
      "value": "64 GB"
    }
  ]
}
```

### Response (201 Created)
```json
{
  "id": 6,
  "title": "Nintendo Switch OLED",
  "price": 349.99,
  "originalPrice": 399.99,
  "currency": "USD",
  "availableQuantity": 80,
  "soldQuantity": 0,
  "condition": "new",
  "description": "Nintendo Switch modelo OLED...",
  "warranty": "1 año de garantía Nintendo",
  "createdAt": "2025-12-16T15:45:00",
  "discount": 12,
  "images": [
    "https://images.unsplash.com/photo-1578303512597-81e6cc155b3e?w=800",
    "https://images.unsplash.com/photo-1606144042614-b2417e99c4e3?w=800"
  ],
  "attributes": [
    {
      "name": "Marca",
      "value": "Nintendo"
    },
    {
      "name": "Modelo",
      "value": "Switch OLED"
    }
  ],
  "seller": {
    "id": 2,
    "name": "ElectroMundo",
    "reputation": 88,
    "totalSales": 8750,
    "yearsActive": 3,
    "responseTime": "4 horas"
  },
  "reviewSummary": {
    "averageRating": 0.0,
    "totalReviews": 0,
    "fiveStars": 0,
    "fourStars": 0,
    "threeStars": 0,
    "twoStars": 0,
    "oneStar": 0
  },
  "recentReviews": [],
  "questions": []
}
```

### Validaciones
- ✅ `title`: Requerido, máximo 500 caracteres
- ✅ `price`: Requerido, mayor a 0
- ✅ `originalPrice`: Opcional, mayor a 0 si se proporciona
- ✅ `currency`: Requerido, exactamente 3 caracteres (ej: USD, MXN)
- ✅ `availableQuantity`: Requerido, no negativo
- ✅ `condition`: Requerido, debe ser: `new`, `used`, o `refurbished`
- ✅ `description`: Requerido
- ✅ `sellerId`: Requerido, debe existir en la base de datos

### Errores Posibles
```json
// 400 Bad Request - Validación fallida
{
  "timestamp": "2025-12-16T15:45:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": {
    "title": "Title is required",
    "price": "Price must be greater than 0",
    "condition": "Condition must be: new, used, or refurbished"
  }
}

// 404 Not Found - Seller no existe
{
  "timestamp": "2025-12-16T15:45:00",
  "status": 404,
  "error": "Not Found",
  "message": "Seller not found with id: 999"
}
```

---

## 2️⃣ Agregar Reseña

### Endpoint
```
POST http://localhost:8080/api/products/1/reviews
Content-Type: application/json
```

### Request Body
```json
{
  "rating": 5,
  "comment": "Excelente producto, superó mis expectativas. La calidad es increíble y llegó muy rápido.",
  "userName": "María López"
}
```

### Response (201 Created)
```json
{
  "id": 25,
  "rating": 5,
  "comment": "Excelente producto, superó mis expectativas. La calidad es increíble y llegó muy rápido.",
  "userName": "María López",
  "createdAt": "2025-12-16T15:50:00"
}
```

### Validaciones
- ✅ `rating`: Requerido, debe ser entre 1 y 5
- ✅ `comment`: Requerido, máximo 2000 caracteres
- ✅ `userName`: Requerido, máximo 200 caracteres

### Errores Posibles
```json
// 400 Bad Request - Rating inválido
{
  "timestamp": "2025-12-16T15:50:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": {
    "rating": "Rating must be between 1 and 5"
  }
}

// 404 Not Found - Producto no existe
{
  "timestamp": "2025-12-16T15:50:00",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found with id: 999"
}
```

---

## 3️⃣ Agregar Pregunta

### Endpoint
```
POST http://localhost:8080/api/products/1/questions
Content-Type: application/json
```

### Request Body
```json
{
  "question": "¿Este producto tiene garantía extendida disponible?",
  "userName": "Carlos Rodríguez"
}
```

### Response (201 Created)
```json
{
  "id": 15,
  "question": "¿Este producto tiene garantía extendida disponible?",
  "answer": null,
  "userName": "Carlos Rodríguez",
  "createdAt": "2025-12-16T15:55:00",
  "answeredAt": null
}
```

### Validaciones
- ✅ `question`: Requerido, máximo 1000 caracteres
- ✅ `userName`: Requerido, máximo 200 caracteres

### Errores Posibles
```json
// 400 Bad Request - Pregunta vacía
{
  "timestamp": "2025-12-16T15:55:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": {
    "question": "Question is required"
  }
}

// 404 Not Found - Producto no existe
{
  "timestamp": "2025-12-16T15:55:00",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found with id: 999"
}
```

---

## 🧪 Ejemplos con cURL

### Crear Producto
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "title": "iPad Pro 12.9\" M2 256GB",
    "price": 1099.99,
    "originalPrice": 1299.99,
    "currency": "USD",
    "availableQuantity": 40,
    "condition": "new",
    "description": "iPad Pro con chip M2, pantalla Liquid Retina XDR de 12.9 pulgadas.",
    "warranty": "1 año de garantía Apple",
    "sellerId": 1,
    "imageUrls": ["https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=800"],
    "attributes": [
      {"name": "Marca", "value": "Apple"},
      {"name": "Modelo", "value": "iPad Pro 12.9\""}
    ]
  }'
```

### Agregar Reseña
```bash
curl -X POST http://localhost:8080/api/products/1/reviews \
  -H "Content-Type: application/json" \
  -d '{
    "rating": 5,
    "comment": "Producto excelente!",
    "userName": "Juan Pérez"
  }'
```

### Agregar Pregunta
```bash
curl -X POST http://localhost:8080/api/products/1/questions \
  -H "Content-Type: application/json" \
  -d '{
    "question": "¿Cuándo llega el producto?",
    "userName": "Ana García"
  }'
```

---

## 📊 Resumen de Endpoints

| Método | Endpoint | Descripción | Status Code |
|--------|----------|-------------|-------------|
| POST | `/api/products` | Crear producto | 201 Created |
| POST | `/api/products/{id}/reviews` | Agregar reseña | 201 Created |
| POST | `/api/products/{id}/questions` | Agregar pregunta | 201 Created |
| GET | `/api/products` | Listar productos | 200 OK |
| GET | `/api/products/{id}` | Ver producto | 200 OK |
| GET | `/api/products/{id}/reviews` | Ver reseñas | 200 OK |
| GET | `/api/products/{id}/questions` | Ver preguntas | 200 OK |

---

## ✅ Características Implementadas

- ✅ Validación automática con Jakarta Validation
- ✅ Respuestas con HTTP Status apropiados (201 Created)
- ✅ Manejo de errores con mensajes descriptivos
- ✅ Validación de existencia de recursos (seller, product)
- ✅ Transacciones para garantizar consistencia
- ✅ Cascade save para entidades relacionadas
- ✅ Timestamps automáticos (createdAt)
- ✅ CORS habilitado para frontend

---

## 🎯 Próximos Pasos Sugeridos

1. ✅ Implementar PUT/PATCH para actualizar
2. ✅ Implementar DELETE para eliminar
3. ✅ Agregar paginación a GET /api/products
4. ✅ Implementar búsqueda y filtros
5. ✅ Agregar autenticación/autorización
6. ✅ Implementar respuesta a preguntas (PUT /api/questions/{id}/answer)
7. ✅ Agregar validación de imágenes (URLs válidas)
8. ✅ Implementar rate limiting

¡Endpoints POST listos para usar! 🚀
