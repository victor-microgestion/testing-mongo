# Proyecto Java + Open Liberty + Docker

## Requisitos previos

1. **Java 17 o superior** (o la versión que requiera tu proyecto).  
2. **Apache Maven** (versión recomendada 3.8+).  
3. **Docker** instalado y en ejecución.  
4. (Opcional) **Visual Studio Code** con extensiones para Java/Maven, si deseas un entorno de desarrollo integrado.

---

## Compilación y empaquetado

1. **Limpiar y compilar el proyecto**:
   ```bash
   mvn clean package
   ```
   - `mvn clean` elimina archivos temporales y la carpeta `target`.
   - `mvn package` compila el código y genera el archivo `test.war` (o el nombre que tenga tu WAR) dentro de la carpeta `target/`.

2. (Opcional) **Instalar en tu repositorio local**:
   ```bash
   mvn clean install
   ```
   Esto compilará el proyecto y copiará el artefacto al repositorio local de Maven (generalmente `~/.m2/repository`).

3. **Modo desarrollo con Open Liberty** (hot reload):
   ```bash
   mvn liberty:dev
   ```
   - Levanta el servidor de Open Liberty localmente con el código fuente.  
   - Permite ver cambios en caliente mientras desarrollas (hot deploy).

---

## Crear la imagen Docker

Una vez tengas el WAR generado en `target/`, puedes crear la imagen con el siguiente comando:

```bash
docker build -t liberty-mongodb:1.0-SNAPSHOT .
```

- **`-t liberty-mongodb:1.0-SNAPSHOT`** asigna el nombre y la etiqueta a la imagen.  
- **`.`** indica que el `Dockerfile` se encuentra en el directorio actual.

Durante la construcción, se ejecutarán varios pasos, entre ellos:
1. Se extrae la imagen base de Open Liberty (kernel-slim-java21-openj9-ubi-minimal).
2. Se copian los archivos de configuración a `/config`.
3. Se copian las dependencias y el `.war` a `/config/apps`.
4. Se ejecutan scripts de configuración (`features.sh`, `configure.sh`, etc.).

Al final verás un mensaje indicando que la imagen se generó correctamente:

```
=> => naming to docker.io/library/liberty-mongodb:1.0-SNAPSHOT
```

---

## Ejecución del contenedor

Para ejecutar el contenedor en segundo plano (modo “detached”) y mapear los puertos:

```bash
docker run -d -p 9080:9080 -p 9443:9443 --name liberty-app liberty-mongodb:1.0-SNAPSHOT
```

- `-d`: Ejecuta el contenedor en modo background.  
- `-p 9080:9080`: Expone el puerto 9080 del contenedor en el 9080 de tu host.  
- `-p 9443:9443`: Expone el puerto 9443 del contenedor en el 9443 de tu host.  
- `--name liberty-app`: Asigna el nombre “liberty-app” al contenedor para facilitar su manejo.

Ahora podrás acceder a la aplicación desde tu navegador o mediante `curl` en la URL:

```
http://localhost:9080
```
o si tienes un endpoint específico, por ejemplo:
```
http://localhost:9080/api/mock
```

---

## Ejecución de pruebas (Testing)

Este proyecto usa **Testcontainers** para levantar contenedores de MongoDB y la imagen de Open Liberty para pruebas de integración. Para que las pruebas funcionen:

1. **Asegúrate de que Docker esté en ejecución**.  
2. **Crea la imagen** local de `liberty-mongodb:1.0-SNAPSHOT` (como se indicó arriba), ya que las clases de prueba pueden necesitarla para el contenedor de Liberty.  
3. **Ejecuta los tests** con:
   ```bash
   mvn test
   ```
   o simplemente:
   ```bash
   mvn clean test
   ```
4. **En Visual Studio Code**, si instalaste la extensión de **Maven**:
   - Abre la vista de Maven en la barra lateral.  
   - Expande el proyecto y busca el goal “test” dentro de las fases.  
   - Haz clic en el ícono de “play” para lanzar las pruebas.
   - Alternativamente, con la extensión de **Test Runner** o **Test Explorer**, puedes hacer clic en “Run Test” directamente en la clase de prueba `ControllerApiIT`.

Si todo está configurado correctamente, Maven iniciará los contenedores (MongoDB y la imagen Liberty) mediante Testcontainers, y ejecutará los tests de integración.

---

## Limpieza del proyecto

Para limpiar y recompilar el proyecto, ejecuta:

```bash
mvn clean package
```

Para compilar e “instalar” localmente (por si necesitas usar el artefacto en otros proyectos):

```bash
mvn clean install
```
