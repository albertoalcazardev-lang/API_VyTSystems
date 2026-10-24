# VyT Systems - CRUD de solicitudes en Java y MySQL

Este proyecto esta basado en la estructura del `.rar`: Maven, paquetes separados, modelos, DAO, conexion y menu. El modulo funcional es el del administrador; el usuario de consulta queda creado como clase, pero su menu solo muestra que esta pendiente.

## Que cubre

- Agregar una nueva solicitud.
- Editar una solicitud existente.
- Eliminar una solicitud por id.
- Visualizar todas las solicitudes.
- Filtrar por palabra clave, estado y prioridad.
- Validar datos desde setters y desde el menu.
- Calcular dias restantes hasta el plazo de vencimiento.
- Usar POO: clases, encapsulamiento, constructores, getters, setters, herencia, polimorfismo, clases abstractas, metodos abstractos, interfaces y manejo de errores.

## Como usarlo en IntelliJ IDEA

1. Abre IntelliJ IDEA.
2. Crea o abre un proyecto Maven.
3. Copia la carpeta `src` y el archivo `pom.xml` de este entregable.
4. Configura las variables de entorno `DB_URL`, `DB_USER` y `DB_PASSWORD` con los datos de tu instancia MySQL. `DB_URL` y `DB_USER` tienen valores locales predeterminados; `DB_PASSWORD` queda vacia si no se define.
5. Ejecuta el script `database/schema_solicitudes.sql` en MySQL Workbench si aun no tienes la tabla.
6. Ejecuta `org.example.Main`.

## Archivos principales

- `Main.java`: punto de entrada; llama al menu y captura errores generales de consola.
- `Conexion.java`: centraliza la conexion JDBC con MySQL.
- `SolicitudDAO.java`: contiene el CRUD usando `PreparedStatement` para evitar errores comunes e inyeccion SQL.
- `DocumentoBase.java`: clase abstracta para documentos; contiene datos comunes y metodos abstractos.
- `Solicitud.java`: modelo principal del CRUD; hereda de `DocumentoBase`, implementa interfaces y valida los campos.
- `Usuario.java`: clase abstracta para usuarios.
- `Administrador.java`: usuario con permisos para usar el CRUD.
- `UsuarioConsulta.java`: usuario de solo lectura, creado pero pendiente de funcionalidad.
- `Menu.java`: menu de consola para capturar, editar, eliminar, visualizar y filtrar solicitudes.
- `schema_solicitudes.sql`: estructura sugerida de MySQL compatible con el DAO.

## Campos de solicitud usados del documento de requerimientos

- Fecha de entrega.
- Hora de entrega.
- Autoridad o area receptora.
- Asunto corto.
- Medio de entrega: Digital o Impreso.
- Estado: Entregada o En proceso.
- Tipo de solicitud.
- Evidencia: carpeta, folio o link.
- Acuse: si/no y detalle.
- Plazo de vencimiento.
- Prioridad: Alta, Media o Baja.

## Donde se ve cada requisito de POO

- Clases: todos los archivos `.java`.
- Encapsulamiento: atributos privados y acceso por getters/setters.
- Constructores: los constructores llaman setters para validar.
- Getters con formato: fechas, horas, textos, estados, prioridad y acuse salen formateados; los ids int salen sin formato.
- Setters con validaciones: campos vacios, fechas invalidas, formatos y limites de texto.
- `toString`: llama getters para mostrar datos ya formateados.
- Herencia: `Solicitud extends DocumentoBase`; `Administrador` y `UsuarioConsulta` heredan de `Usuario`.
- Polimorfismo: el menu imprime `Usuario` y `DocumentoBase`, pero en ejecucion recibe objetos concretos.
- Clases abstractas: `DocumentoBase` y `Usuario`.
- Metodos abstractos: `mostrarTipoDocumento`, `calcularDiasRestantes`, `mostrarTipoUsuario`, `obtenerPermisos`.
- Interfaces: `Vencible`, `Filtrable`, `GestionadorSolicitudes`.
- Try-catch: menu, `Main` y `SolicitudDAO`.
