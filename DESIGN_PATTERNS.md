# Principios y Patrones de Diseño Aplicados

Este documento detalla los principios de ingeniería de software y patrones de diseño implementados para mejorar la calidad, mantenibilidad y escalabilidad del proyecto.

## 1. Patrón Builder (Builder Pattern)
Se ha implementado el patrón **Builder** en todas las entidades del modelo (`Product`, `Review`, `Question`, `Seller`) y en los DTOs.

### Problema
Los constructores tenían demasiados parámetros, lo que hacía difícil leer y mantener el código al instanciar objetos.
```java
// Antes
new Product("Titulo", price, null, "USD", 10, ...);
```

### Solución
Con `@Builder` de Lombok, la creación de objetos es fluida y explícita:
```java
// Después
Product.builder()
    .title("Titulo")
    .price(price)
    .currency("USD")
    .build();
```

## 2. Patrón Mapper (Mapper Pattern)
Se han creado clases dedicadas (`ProductMapper`, `ReviewMapper`, `QuestionMapper`) para manejar la conversión entre Entidades y DTOs.

### Problema
La lógica de conversión estaba mezclada dentro de los Servicios (`ProductService`), violando la separación de responsabilidades.

### Solución
Los servicios ahora delegan esa tarea a los Mappers:
```java
return productMapper.toDetailDTO(product);
```

## 3. Principio de Responsabilidad Única (SRP - SOLID)
Se ha refactorizado la clase `ProductService` (que actuaba como una "Clase Dios") dividiéndola en tres servicios especializados.

### Problema
`ProductService` manejaba lógica de Productos, Reviews y Preguntas, haciéndola difícil de mantener y propensa a errores colaterales.

### Solución
- **`ProductService`**: Solo gestiona el ciclo de vida de los productos.
- **`ReviewService`**: Gestiona la creación y lectura de reseñas.
- **`QuestionService`**: Gestiona las preguntas y respuestas.

## 4. Principio Open/Closed (OCP - SOLID) y Partial Updates
Se ha mejorado el método `updateProduct` para ser más dinámico y resistente al cambio.

### Problema
El método `updateProduct` contenía una larga lista de condicionales `if (request.getField() != null)`. Si se agregaba un nuevo campo al producto, se moestaba obligado a modificar el código del servicio, violando el principio Open/Closed.

### Solución
Implementación de un mecanismo de **Reflexión (Reflection)** mediante `BeanUtils` y `BeanWrapper` para copiar dinámicamente propiedades no nulas.
- **Abierto a extensión**: Ahora puedes agregar nuevos campos primitivos (String, Integer, etc.) al `Product` y `UpdateProductRequest`, y se actualizarán automáticamente sin tocar una sola línea de lógica en `ProductService`.
- **Cerrado a modificación**: La lógica de "patch" (parcheo) está encapsulada y no requiere cambios manuales por cada nuevo atributo.

## 5. Inyección de Dependencias (Dependency Injection - DI)
Este es el patrón fundamental sobre el que se construye Spring Framework (Inversión de Control).

### Uso en el Proyecto
En lugar de que las clases creen sus propias dependencias (ej. `new ProductRepository()`), estas son "inyectadas" por el framework.

- **Implementación**: Hemos optado por la **Inyección por Constructor**, que es la práctica recomendada hoy en día sobre la inyección por atributos (`@Autowired` en campos).
- **Ventajas**:
  - Hace que las clases sean fáciles de probar (Testability), ya que puedes pasar Mocks en el constructor.
  - Asegura que las dependencias requeridas no sean nulas (Inmutabilidad del bean).
  - Elimina la necesidad de usar `@Autowired` explícitamente en el código.

**Ejemplo en `ProductController`:**
```java
public ProductController(ProductService productService, ...) {
    this.productService = productService;
}
```
Spring automáticamente "busca" el Bean de `ProductService` y lo pasa al crear el controlador.

## 6. Patrón Singleton (Singleton Pattern)

Aunque no implementamos `private static instance` manualmente, este patrón es **nativo** del Framework Spring.

### Uso en el Proyecto
Todas las clases anotadas con `@Service`, `@RestController`, `@Component` y `@Repository` son manejadas por el contenedor de Inyección de Dependencias (IoC) de Spring como **Singletons** por defecto.


## 7. Patrón Facade (Fachada)
Este patrón se utiliza para proporcionar una interfaz unificada y simplificada a un conjunto de interfaces en un subsistema.

### Uso en el Proyecto
El `ProductController` actúa como una **Fachada** para el cliente (Frontend/Postman).

- **Como funciona**: El cliente solo interactúa con los endpoints del controlador (`/api/products/...`), sin saber que por detrás hay múltiples servicios (`ProductService`, `ReviewService`, `QuestionService`) y repositorios trabajando juntos.
- **Simplificación**: El controlador orquesta las llamadas a estos servicios para cumplir con la solicitud HTTP, ocultando la complejidad de la lógica de negocio y la persistencia.


