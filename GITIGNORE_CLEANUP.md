# Actualización de .gitignore - Guía de Limpieza

## ✅ Cambios Realizados

Se ha actualizado el `.gitignore` para incluir archivos de IDEs y otros archivos temporales que no deben subirse al repositorio:

### 🔧 IDEs Soportados

- **IntelliJ IDEA**: `.idea/`, `*.iml`, `*.ipr`, `*.iws`, `out/`
- **VS Code**: `.vscode/`, `*.code-workspace`, `.history/`
- **Eclipse**: `.classpath`, `.project`, `.settings/`, `bin/`
- **NetBeans**: `/nbproject/`, `/nbbuild/`, `/dist/`
- **Spring Tool Suite**: `.apt_generated`, `.springBeans`, `.sts4-cache`

### 💻 Sistemas Operativos

- **Windows**: `Thumbs.db`, `Desktop.ini`, `*.lnk`
- **macOS**: `.DS_Store`, `._*`
- **Linux**: `*~`, `.directory`

### ☕ Java & Build Tools

- **Java**: `*.class`, `*.jar`, `*.war`, `*.ear`
- **Maven**: `target/`, `.mvn/`
- **Gradle**: `.gradle/`, `**/build/`

### 🗄️ Bases de Datos

- `*.db`, `*.sqlite`, `*.sqlite3`, `*.h2.db`

### 🌱 Spring Boot

- `application-local.properties`
- `application-dev.properties`
- `application-prod.properties`
- `*.log`

---

## 🧹 Limpiar Archivos Ya Trackeados

Si ya tienes archivos de IDE en tu repositorio (como `.idea/` o `.vscode/`), necesitas eliminarlos del tracking de Git:

### Paso 1: Ver qué archivos están trackeados

```bash
git status
```

### Paso 2: Eliminar archivos/carpetas del tracking (sin borrarlos del disco)

```bash
# Eliminar carpeta .idea
git rm -r --cached .idea

# Eliminar carpeta .vscode
git rm -r --cached .vscode

# Eliminar archivos .iml
git rm --cached *.iml

# Eliminar carpeta target si está trackeada
git rm -r --cached target
```

### Paso 3: Verificar cambios

```bash
git status
```

Deberías ver algo como:
```
deleted:    .idea/...
deleted:    .vscode/...
```

### Paso 4: Hacer commit de los cambios

```bash
git add .gitignore
git commit -m "chore: update .gitignore to exclude IDE files and build artifacts"
```

---

## 🚀 Comando Rápido (Todo en Uno)

Si quieres limpiar todo de una vez:

```bash
# Eliminar del tracking (mantiene archivos localmente)
git rm -r --cached .idea .vscode *.iml target

# Agregar .gitignore actualizado
git add .gitignore

# Commit
git commit -m "chore: update .gitignore and remove IDE files from tracking"
```

---

## ⚠️ Importante

- **`--cached`**: Solo elimina del tracking de Git, NO borra los archivos de tu disco
- **Sin `--cached`**: Eliminaría los archivos de tu disco también (¡NO lo hagas!)

---

## ✅ Verificación Final

Después de hacer el commit, verifica que los archivos ya no están trackeados:

```bash
# Ver archivos trackeados
git ls-files | grep -E "\.idea|\.vscode|\.iml"
```

Si no devuelve nada, ¡perfecto! Los archivos de IDE ya no están en el repositorio.

---

## 📝 Archivos que SÍ deben estar en el repositorio

Estos archivos **SÍ** deben subirse:

✅ `src/` - Código fuente
✅ `pom.xml` - Configuración de Maven
✅ `README.md` - Documentación
✅ `LOMBOK_GUIDE.md` - Guías
✅ `postman_collection.json` - Colección de Postman
✅ `.gitignore` - Este archivo
✅ `application.properties` - Configuración base (sin secretos)

---

## 🔒 Archivos Sensibles

Si tienes archivos con credenciales o configuraciones locales, usa estos nombres (ya están en `.gitignore`):

- `application-local.properties`
- `application-dev.properties`
- `application-prod.properties`

Ejemplo:
```properties
# application-local.properties (NO se sube a Git)
spring.datasource.password=mi_password_secreto
```

---

## 🎯 Resumen

1. ✅ `.gitignore` actualizado con reglas para IDEs
2. 🧹 Ejecuta `git rm -r --cached .idea .vscode *.iml target`
3. 💾 Haz commit: `git commit -m "chore: update .gitignore"`
4. 🚀 Push: `git push`

¡Listo! Tu repositorio ahora está limpio de archivos de IDE. 🎉
