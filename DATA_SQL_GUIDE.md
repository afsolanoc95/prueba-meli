# 📝 Guía de Datos con data.sql

## ✅ Cambio Realizado

Ahora los datos de ejemplo se cargan desde `src/main/resources/data.sql` en lugar de estar quemados en código Java.

## 🎯 Ventajas

### Antes (DataInitializationService.java)
- ❌ Código Java verboso y difícil de leer
- ❌ Difícil agregar/modificar datos
- ❌ Requiere recompilar para cambios
- ❌ ~240 líneas de código Java

### Ahora (data.sql)
- ✅ SQL estándar, fácil de leer
- ✅ Agregar datos es tan simple como agregar un INSERT
- ✅ No requiere recompilar (solo reiniciar app)
- ✅ Formato familiar para DBAs

## 📂 Ubicación del Archivo

```
src/main/resources/data.sql
```

## 🔧 Configuración en application.properties

```properties
# Data Initialization
spring.sql.init.mode=always
spring.jpa.defer-datasource-initialization=true
```

- `spring.sql.init.mode=always`: Ejecuta `data.sql` siempre al iniciar
- `spring.jpa.defer-datasource-initialization=true`: Espera a que Hibernate cree las tablas antes de ejecutar `data.sql`

## ✏️ Cómo Agregar Más Datos

### 1. Agregar un Nuevo Vendedor

```sql
INSERT INTO sellers (name, reputation, total_sales, years_active, response_time) VALUES
('Mi Nueva Tienda', 90, 5000, 2, '1 hora');
```

### 2. Agregar un Nuevo Producto

```sql
-- Primero obtén el ID del seller (en este caso, seller_id = 1)
INSERT INTO products (title, price, original_price, currency, available_quantity, sold_quantity, condition, description, warranty, seller_id, created_at) VALUES
('iPad Pro 12.9" M2 256GB', 1099.99, 1299.99, 'USD', 40, 320, 'new',
 'iPad Pro con chip M2, pantalla Liquid Retina XDR de 12.9 pulgadas, cámara TrueDepth frontal y sistema de cámaras Pro.',
 '1 año de garantía Apple', 1, CURRENT_TIMESTAMP);
```

### 3. Agregar Imágenes al Producto

```sql
-- Asumiendo que el nuevo producto tiene ID = 6
INSERT INTO product_images (url, is_primary, product_id) VALUES
('https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=800', true, 6),
('https://images.unsplash.com/photo-1585790050230-5dd28404f1b4?w=800', false, 6);
```

### 4. Agregar Atributos

```sql
INSERT INTO product_attributes (name, attribute_value, product_id) VALUES
('Marca', 'Apple', 6),
('Modelo', 'iPad Pro 12.9"', 6),
('Procesador', 'Apple M2', 6),
('Pantalla', '12.9" Liquid Retina XDR', 6);
```

### 5. Agregar Reseñas

```sql
INSERT INTO reviews (rating, comment, user_name, product_id, created_at) VALUES
(5, 'Excelente tablet para diseño gráfico.', 'María L.', 6, CURRENT_TIMESTAMP),
(4, 'Muy buena pero cara.', 'José R.', 6, CURRENT_TIMESTAMP);
```

### 6. Agregar Preguntas

```sql
INSERT INTO questions (question, answer, user_name, product_id, created_at, answered_at) VALUES
('¿Incluye Apple Pencil?', 'No, se vende por separado.', 'Ana P.', 6, DATEADD('DAY', -1, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP);
```

## 🔄 Orden de Inserción (Importante)

Debes respetar este orden por las foreign keys:

1. **Sellers** (primero, no depende de nadie)
2. **Products** (depende de Sellers)
3. **Product Images** (depende de Products)
4. **Product Attributes** (depende de Products)
5. **Reviews** (depende de Products)
6. **Questions** (depende de Products)

## 🎨 Funciones SQL Útiles

### Fechas
```sql
CURRENT_TIMESTAMP              -- Fecha/hora actual
DATEADD('DAY', -7, CURRENT_TIMESTAMP)  -- 7 días atrás
DATEADD('HOUR', -2, CURRENT_TIMESTAMP) -- 2 horas atrás
```

### IDs Auto-incrementales
H2 asigna IDs automáticamente, no necesitas especificarlos en los INSERT.

## 🚀 Aplicar Cambios

1. Edita `src/main/resources/data.sql`
2. Guarda el archivo
3. Reinicia la aplicación
4. Los nuevos datos se cargarán automáticamente

## ⚠️ Notas Importantes

- **H2 In-Memory**: Los datos se pierden al cerrar la app
- **create-drop**: Hibernate borra y recrea las tablas al iniciar
- **data.sql se ejecuta después** de que Hibernate crea las tablas

## 🔧 Troubleshooting

### Error: "Table not found"
- Verifica que `spring.jpa.defer-datasource-initialization=true` esté configurado
- Asegúrate que `spring.jpa.hibernate.ddl-auto=create-drop` esté activo

### Error: "Foreign key constraint"
- Verifica el orden de los INSERT
- Asegúrate que los IDs de foreign keys existan

### Los datos no se cargan
- Verifica que `spring.sql.init.mode=always` esté configurado
- Revisa la consola por errores SQL

## 📊 Ejemplo Completo: Agregar un Producto

```sql
-- 1. Agregar producto (usa seller_id existente)
INSERT INTO products (title, price, original_price, currency, available_quantity, sold_quantity, condition, description, warranty, seller_id, created_at) VALUES
('Nintendo Switch OLED', 349.99, 399.99, 'USD', 80, 1500, 'new',
 'Nintendo Switch modelo OLED con pantalla de 7 pulgadas, 64GB de almacenamiento, dock mejorado y audio envolvente.',
 '1 año de garantía Nintendo', 2, CURRENT_TIMESTAMP);

-- 2. Agregar imágenes (product_id = 6, asumiendo que es el siguiente ID)
INSERT INTO product_images (url, is_primary, product_id) VALUES
('https://images.unsplash.com/photo-1578303512597-81e6cc155b3e?w=800', true, 6);

-- 3. Agregar atributos
INSERT INTO product_attributes (name, attribute_value, product_id) VALUES
('Marca', 'Nintendo', 6),
('Modelo', 'Switch OLED', 6),
('Pantalla', '7" OLED', 6),
('Almacenamiento', '64 GB', 6);

-- 4. Agregar reseñas
INSERT INTO reviews (rating, comment, user_name, product_id, created_at) VALUES
(5, 'La pantalla OLED se ve increíble!', 'Gamer123', 6, CURRENT_TIMESTAMP);

-- 5. Agregar preguntas
INSERT INTO questions (question, answer, user_name, product_id, created_at, answered_at) VALUES
('¿Incluye juegos?', 'No, los juegos se venden por separado.', 'Usuario1', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
```

## ✅ Ventaja Principal

**Ahora puedes editar `data.sql` directamente sin tocar código Java!** 🎉
