-- Insert Sellers
INSERT INTO sellers (name, reputation, total_sales, years_active, response_time) VALUES
('TechStore Premium', 95, 15420, 5, '2 horas'),
('ElectroMundo', 88, 8750, 3, '4 horas'),
('GadgetZone', 92, 12300, 4, '3 horas');

-- Insert Products
INSERT INTO products (title, price, original_price, currency, available_quantity, sold_quantity, condition, description, warranty, seller_id, created_at) VALUES
('iPhone 15 Pro Max 256GB - Titanio Natural', 1299.99, 1499.99, 'USD', 50, 1250, 'new', 
 'El iPhone 15 Pro Max es el smartphone más avanzado de Apple. Cuenta con el chip A17 Pro, cámara de 48MP con zoom óptico 5x, pantalla Super Retina XDR de 6.7 pulgadas con ProMotion, diseño de titanio aeroespacial y puerto USB-C. Batería de larga duración con hasta 29 horas de reproducción de video.', 
 '1 año de garantía del fabricante Apple', 1, CURRENT_TIMESTAMP),

('MacBook Pro 14" M3 Pro 18GB RAM 512GB SSD - Gris Espacial', 1999.99, 2299.99, 'USD', 30, 580, 'new',
 'La MacBook Pro de 14 pulgadas con chip M3 Pro ofrece un rendimiento excepcional para profesionales. Pantalla Liquid Retina XDR, hasta 18 horas de batería, cámara FaceTime HD 1080p, sistema de sonido de seis altavoces, tres puertos Thunderbolt 4, HDMI, lector de tarjetas SDXC y MagSafe 3.',
 '1 año de garantía Apple con opción a AppleCare+', 1, CURRENT_TIMESTAMP),

('AirPods Pro (2da generación) con Estuche de Carga MagSafe USB-C', 249.99, 279.99, 'USD', 150, 3200, 'new',
 'Los AirPods Pro de segunda generación ofrecen cancelación activa de ruido hasta 2x más efectiva, audio espacial personalizado, modo de transparencia adaptativa, resistencia al agua y sudor (IPX4), hasta 6 horas de reproducción con una sola carga y hasta 30 horas con el estuche MagSafe.',
 '1 año de garantía Apple', 2, CURRENT_TIMESTAMP),

('Samsung Galaxy S24 Ultra 512GB - Titanium Gray', 1199.99, 1399.99, 'USD', 75, 890, 'new',
 'El Samsung Galaxy S24 Ultra redefine la innovación móvil con su pantalla Dynamic AMOLED 2X de 6.8", procesador Snapdragon 8 Gen 3, sistema de cámaras con IA de 200MP, S Pen integrado, batería de 5000mAh, y marco de titanio. Incluye Galaxy AI para traducción en tiempo real, edición de fotos con IA y más.',
 '1 año de garantía Samsung', 3, CURRENT_TIMESTAMP),

('Sony WH-1000XM5 Audífonos Inalámbricos con Cancelación de Ruido - Negro', 349.99, 399.99, 'USD', 100, 2150, 'new',
 'Los audífonos premium Sony WH-1000XM5 ofrecen la mejor cancelación de ruido de su clase, calidad de audio Hi-Res, hasta 30 horas de batería, carga rápida (3 min = 3 horas), 8 micrófonos para llamadas cristalinas, diseño ultraligero y cómodo, multipoint para conectar 2 dispositivos.',
 '2 años de garantía Sony', 2, CURRENT_TIMESTAMP);

-- Insert Product Images
INSERT INTO product_images (url, is_primary, product_id) VALUES
-- iPhone images
('https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=800', true, 1),
('https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=800&h=600', false, 1),
('https://images.unsplash.com/photo-1695048064537-a5a4e2dfb0f8?w=800', false, 1),

-- MacBook images
('https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=800', true, 2),
('https://images.unsplash.com/photo-1611186871348-b1ce696e52c9?w=800', false, 2),

-- AirPods images
('https://images.unsplash.com/photo-1606841837239-c5a1a4a07af7?w=800', true, 3),
('https://images.unsplash.com/photo-1588423771073-b8903fbb85b5?w=800', false, 3),

-- Samsung images
('https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?w=800', true, 4),
('https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=800', false, 4),

-- Sony images
('https://images.unsplash.com/photo-1546435770-a3e426bf472b?w=800', true, 5),
('https://images.unsplash.com/photo-1484704849700-f032a568e944?w=800', false, 5);

-- Insert Product Attributes
INSERT INTO product_attributes (name, attribute_value, product_id) VALUES
-- iPhone attributes
('Marca', 'Apple', 1),
('Modelo', 'iPhone 15 Pro Max', 1),
('Capacidad', '256 GB', 1),
('Color', 'Titanio Natural', 1),
('Pantalla', '6.7 pulgadas Super Retina XDR', 1),
('Procesador', 'A17 Pro', 1),
('Cámara', '48MP principal + 12MP ultra gran angular + 12MP telefoto', 1),
('Sistema Operativo', 'iOS 17', 1),

-- MacBook attributes
('Marca', 'Apple', 2),
('Modelo', 'MacBook Pro 14"', 2),
('Procesador', 'Apple M3 Pro', 2),
('RAM', '18 GB', 2),
('Almacenamiento', '512 GB SSD', 2),
('Pantalla', '14.2" Liquid Retina XDR', 2),
('Gráficos', 'GPU de 14 núcleos', 2),
('Peso', '1.6 kg', 2),

-- AirPods attributes
('Marca', 'Apple', 3),
('Modelo', 'AirPods Pro 2da Gen', 3),
('Conectividad', 'Bluetooth 5.3', 3),
('Cancelación de ruido', 'Activa', 3),
('Resistencia', 'IPX4', 3),
('Puerto de carga', 'USB-C', 3),
('Chip', 'Apple H2', 3),

-- Samsung attributes
('Marca', 'Samsung', 4),
('Modelo', 'Galaxy S24 Ultra', 4),
('Capacidad', '512 GB', 4),
('RAM', '12 GB', 4),
('Pantalla', '6.8" Dynamic AMOLED 2X', 4),
('Procesador', 'Snapdragon 8 Gen 3', 4),
('Cámara', '200MP + 50MP + 12MP + 10MP', 4),
('Batería', '5000 mAh', 4),

-- Sony attributes
('Marca', 'Sony', 5),
('Modelo', 'WH-1000XM5', 5),
('Tipo', 'Over-ear', 5),
('Conectividad', 'Bluetooth 5.2, NFC', 5),
('Batería', 'Hasta 30 horas', 5),
('Cancelación de ruido', 'Activa con 8 micrófonos', 5),
('Peso', '250 gramos', 5);

-- Insert Reviews
-- Insert Reviews
INSERT INTO reviews (rating, comment, user_name, product_id, created_at) VALUES
-- iPhone reviews
(5, 'Excelente teléfono, la cámara es increíble y la batería dura todo el día.', 'Carlos M.', 1, CURRENT_TIMESTAMP),
(5, 'Mejor iPhone que he tenido. El titanio se siente premium.', 'Ana García', 1, CURRENT_TIMESTAMP),
(4, 'Muy bueno pero el precio es alto. Vale la pena si tienes el presupuesto.', 'Luis R.', 1, CURRENT_TIMESTAMP),
(5, 'La pantalla ProMotion es hermosa, todo se ve súper fluido.', 'María S.', 1, CURRENT_TIMESTAMP),
(4, 'Gran teléfono, solo le falta cargador en la caja.', 'Pedro L.', 1, CURRENT_TIMESTAMP),

-- MacBook reviews
(5, 'Perfecta para desarrollo y edición de video. Muy rápida.', 'Diego F.', 2, CURRENT_TIMESTAMP),
(5, 'La pantalla es espectacular, colores muy precisos.', 'Sofía V.', 2, CURRENT_TIMESTAMP),
(4, 'Excelente laptop, solo es un poco cara.', 'Andrés K.', 2, CURRENT_TIMESTAMP),

-- AirPods reviews
(5, 'La cancelación de ruido es impresionante, perfectos para viajar.', 'Elena B.', 3, CURRENT_TIMESTAMP),
(5, 'Sonido excelente y muy cómodos de usar todo el día.', 'Fernando G.', 3, CURRENT_TIMESTAMP),
(4, 'Muy buenos, pero el estuche es un poco grande.', 'Patricia H.', 3, CURRENT_TIMESTAMP),
(5, 'Los mejores auriculares que he tenido.', 'Ricardo N.', 3, CURRENT_TIMESTAMP),

-- Samsung reviews
(5, 'El mejor Android del mercado. La cámara es brutal.', 'Javier M.', 4, CURRENT_TIMESTAMP),
(5, 'El S Pen es muy útil para tomar notas rápidas.', 'Valentina P.', 4, CURRENT_TIMESTAMP),
(4, 'Excelente teléfono, pero es muy grande para manos pequeñas.', 'Gabriel O.', 4, CURRENT_TIMESTAMP),

-- Sony reviews
(5, 'Sonido espectacular y la cancelación de ruido es perfecta.', 'Isabela C.', 5, CURRENT_TIMESTAMP),
(5, 'Muy cómodos para usar horas. Excelente para trabajar.', 'Tomás E.', 5, CURRENT_TIMESTAMP),
(4, 'Gran calidad, pero el estuche es un poco grande.', 'Natalia W.', 5, CURRENT_TIMESTAMP),
(5, 'Los mejores audífonos que he probado.', 'Sebastián Q.', 5, CURRENT_TIMESTAMP),
(2, 'Battery life is poor.', 'Charlie', 3, CURRENT_TIMESTAMP);

-- Users (Hashed password for 'password' is: $2a$10$coLBYOJocF4NEQIRa1KrF.jSg8AK3OrfCc4dYhxszD46tRQNz9LNS)
INSERT INTO users (username, password, role) VALUES ('seller', '$2a$10$coLBYOJocF4NEQIRa1KrF.jSg8AK3OrfCc4dYhxszD46tRQNz9LNS', 'ROLE_SELLER');
INSERT INTO users (username, password, role) VALUES ('buyer', '$2a$10$coLBYOJocF4NEQIRa1KrF.jSg8AK3OrfCc4dYhxszD46tRQNz9LNS', 'ROLE_BUYER');

-- Insert Questions
INSERT INTO questions (question, answer, user_name, product_id, created_at, answered_at) VALUES
-- iPhone questions
('¿Incluye cargador?', 'No, solo incluye cable USB-C. El cargador se vende por separado.', 'Juan P.', 1, DATEADD('DAY', -2, CURRENT_TIMESTAMP), DATEADD('DAY', -1, CURRENT_TIMESTAMP)),
('¿Es compatible con eSIM?', 'Sí, el iPhone 15 Pro Max soporta eSIM y dual SIM.', 'Laura M.', 1, DATEADD('DAY', -5, CURRENT_TIMESTAMP), DATEADD('DAY', -4, CURRENT_TIMESTAMP)),
('¿Tiene garantía internacional?', 'Sí, la garantía de Apple es internacional.', 'Roberto C.', 1, DATEADD('DAY', -7, CURRENT_TIMESTAMP), DATEADD('DAY', -6, CURRENT_TIMESTAMP)),

-- MacBook questions
('¿Cuántos monitores externos soporta?', 'Soporta hasta 2 monitores externos de 6K a 60Hz.', 'Miguel A.', 2, DATEADD('DAY', -3, CURRENT_TIMESTAMP), DATEADD('DAY', -2, CURRENT_TIMESTAMP)),
('¿Viene con Office instalado?', 'No, debes comprar Microsoft Office por separado o usar alternativas gratuitas.', 'Carmen R.', 2, DATEADD('DAY', -6, CURRENT_TIMESTAMP), DATEADD('DAY', -5, CURRENT_TIMESTAMP)),

-- AirPods questions
('¿Son compatibles con Android?', 'Sí, funcionan con Android pero pierdes algunas funciones como configuración automática.', 'Daniela S.', 3, DATEADD('DAY', -1, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP),
('¿Incluyen puntas de diferentes tamaños?', 'Sí, incluyen 4 tamaños de puntas de silicona.', 'Jorge T.', 3, DATEADD('DAY', -4, CURRENT_TIMESTAMP), DATEADD('DAY', -3, CURRENT_TIMESTAMP)),

-- Samsung questions
('¿Trae cargador incluido?', 'Sí, incluye cargador rápido de 45W.', 'Camila L.', 4, DATEADD('DAY', -2, CURRENT_TIMESTAMP), DATEADD('DAY', -1, CURRENT_TIMESTAMP)),
('¿El S Pen se guarda dentro del teléfono?', 'Sí, tiene un compartimento integrado para el S Pen.', 'Martín D.', 4, DATEADD('DAY', -5, CURRENT_TIMESTAMP), DATEADD('DAY', -4, CURRENT_TIMESTAMP)),

-- Sony questions
('¿Se pueden usar con cable?', 'Sí, incluyen cable de audio de 3.5mm para uso con cable.', 'Victoria F.', 5, DATEADD('DAY', -3, CURRENT_TIMESTAMP), DATEADD('DAY', -2, CURRENT_TIMESTAMP)),
('¿Son buenos para hacer ejercicio?', 'Son más para uso casual/oficina. No son deportivos ni resistentes al agua.', 'Emilio R.', 5, DATEADD('DAY', -6, CURRENT_TIMESTAMP), DATEADD('DAY', -5, CURRENT_TIMESTAMP));
