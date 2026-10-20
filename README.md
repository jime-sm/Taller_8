# Taller_8
# Taller_8
integrantes Sebastián Moreno , Karen Pabón , Juan Felipe Ramirez, Jimena Serrano

# Inventario — Tienda de Don Ernesto

Aplicación de escritorio hecha en Java (NetBeans / Maven) para gestionar el inventario de la tienda. El proyecto usa una arquitectura por capas (MVC/DAO), base de datos MySQL con JDBC y una interfaz gráfica construida con Java Swing.

---
Requisitos para ejecutar
1. Tener **Apache NetBeans** (o cualquier IDE compatible con Maven).
2. Tener **XAMPP** (o un servidor MySQL local) encendido.


Base de datos
1. Entra a tu gestor de MySQL (como phpMyAdmin) y crea una base de datos llamada `tienda`.
2. Asegúrate de tener creadas las tablas `categoria` y `producto` con su respectiva llave foránea (`id_categoria`).
3. Verifica que la clase `conexion/Conexion.java` tenga tus datos de acceso correctos (por lo general usuario `root` y contraseña vacía).

¿Cómo está organizado el código?
El proyecto se divide en 4 paquetes principales:
* **`conexion`**: Aquí está la clase `Conexion.java` que abre el puente con la base de datos mediante JDBC.
* **`model`**: Las clases base o POJOs (`Producto` y `Categoria`).
* **`dao`**: Las clases que hacen todo el trabajo pesado con la base de datos (`ProductoDAO` y `CategoriaDAO`), usando consultas preparadas (`PreparedStatement`) y `JOIN`.
* **`vista`**: Contiene la interfaz gráfica (`VentanaInventario.java`) y la clase `Principal.java` para arrancar el programa.

 Pasos para correr el proyecto
1. Descarga o clona el repositorio en tu PC.
2. Abre NetBeans, dale a *File > Open Project* y selecciona la carpeta del proyecto.
3. Espera a que Maven descargue las dependencias (como el conector de MySQL del `pom.xml`).
4. Ve al paquete `vista`, haz clic derecho sobre **`Principal.java`** y selecciona **Run File** (o presiona `Shift + F6`).


Pruebas y Reflexión (Ejercicio 6)

a) Prueba de persistencia
Qué se hizo: Registramos 5 productos desde la interfaz, actualizamos uno haciendo clic en la fila de la tabla y borramos otro usando el botón de eliminar (con confirmación de por medio).
<img width="529" height="435" alt="image" src="https://github.com/user-attachments/assets/21c60320-b53e-4abb-b038-e4c8633cfdb2" />
<img width="1364" height="713" alt="image" src="https://github.com/user-attachments/assets/c30229d8-d0a8-4b40-bc38-2f48905d81b0" />

* **Resultado:** Al cerrar la app y volver a abrirla, los datos seguían ahí cargados desde la base de datos, comprobando que la persistencia funciona al 100%.

### b) Verificación en MySQL
* **Consulta SQL:**
  ```sql
  SELECT p.id_producto, p.nombre, p.precio, p.cantidad, c.nombre AS categoria 
  FROM producto p 
  JOIN categoria c ON p.id_categoria = c.id_categoria;
<img width="655" height="372" alt="image" src="https://github.com/user-attachments/assets/f8bfb799-ede3-421e-8124-defb8a1e0a6a" />
Resultado: Lo que muestra esta consulta en MySQL es exactamente igual a lo que se ve en la tabla de la aplicación de Java.
<img width="644" height="465" alt="Captura de pantalla 2026-10-19 181540" src="https://github.com/user-attachments/assets/ab344a01-61c7-49fe-9e5d-ac77aa380302" />
c) Eliminar una categoría con productos asociados
¿Qué pasa? MySQL tira un error de clave foránea porque no permite dejar productos "huérfanos". A nivel de código, esto tiene todo que ver con la composición: un producto no puede existir si no tiene una categoría asignada.

d) Ventajas de la arquitectura y cambio a PostgreSQL
¿Por qué dividirlo en paquetes? Porque separa responsabilidades. Para cuando se requiera cambiar la interfaz gráfica, no tienes que tocar nada de la base de datos ni de los DAO y el código se mantiene más limpio y cómodo de mantener.

¿Qué cambiaría si usamos xampp? tendriamos  que cambiar la dependencia del driver en el pom.xml y actualizar la URL y el nombre del driver en Conexion.java. Los modelos, los DAO y la vista se quedan exactamente igual.
