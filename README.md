# Blockbuster Fake

Una aplicación Java moderna construida con Spring Boot, JPA/Hibernate y MySQL.

## 🚀 Tecnologías

- **Java 17** - Lenguaje de programación
- **Spring Boot 3.5.0** - Framework de aplicación
- **Spring Data JPA 3.5.0** - Capa de persistencia
- **Hibernate** - ORM (Object-Relational Mapping)
- **MySQL 7.4.12** - Base de datos
- **Maven 3.9.9** - Gestión de dependencias

## 📋 Prerrequisitos

Antes de ejecutar este proyecto, asegúrate de tener instalado:

- [Java 17](https://openjdk.org/projects/jdk/17/) o superior
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [MySQL 7.4.12+](https://dev.mysql.com/downloads/mysql/)
- [Git](https://git-scm.com/)

## 🛠️ Instalación

1. **Clona el repositorio**
   ```bash
   git clone https://github.com/wualtervera/LPII_T1_VERA_GUERRA_WUALTER.git
   cd LPII_T1_VERA_GUERRA_WUALTER
   ```

2. **Configura la base de datos**

   Debe crear la base de datos y importar el script que se menciona mas abajo:
   

3. **Configura las propiedades de la aplicación**

   Edita el archivo `src/main/resources/application.properties`:
   ```properties
   # Configuración de la aplicación
    server.port=8092
    spring.application.name=peliculas
   # Configuración de la base de datos
    spring.datasource.url=jdbc:mysql://localhost:3306/bd_t2_apellido
    spring.datasource.username=root
    spring.datasource.password=
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.datasource.hikari.connection-init-sql=SET NAMES utf8mb4
    spring.datasource.hikari.maximum-pool-size=25
   
   # Configuración de JPA/Hibernate
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
    spring.jpa.properties.hibernate.format_sql=true
   
   # Configuración de thymeleaf
    spring.thymeleaf.cache=false
    spring.thymeleaf.mode=HTML5
    spring.thymeleaf.encoding=UTF-8
    logging.level.org.thymeleaf=DEBUG
   ```

4. **Instala las dependencias**
   ```bash
   mvn clean install
   ```

## ▶️ Ejecución

### Modo desarrollo
```bash
mvn spring-boot:run
```

### Generar JAR ejecutable
```bash
mvn clean package
java -jar target/mi-proyecto-1.0.0.jar
```

La aplicación estará disponible en: `http://localhost:8080`


## 🔧 Configuración de Entorno

### Aplication properties

Los parámetros de como: nombre de la base de dato(**bd_t2_apellido**), username(**root**) y password(en mi caso la base de datos no tiene passwor) son flexibles y pueden cambiarlo.


Y en `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bd_t2_apellido
spring.datasource.username=root
spring.datasource.password=
```

## 📊 Base de Datos

### Migraciones

Las migraciones se crean atumoticamente al levantar el proyecto.
Sinembargo se deja el script de creación de tablas con data de prueba
### Datos de Prueba (Opcional)

Para importar importar el script sql(**bd_t2_apellido.sql**) debe crar la base de datos con el nombre **bd_t2_apellido**

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/mejoras`)
3. Commit tus cambios (`git commit -m 'Add mejoras xxx'`)
4. Push a la rama (`git push origin feature/mejoras`)
5. Abre un Pull Request

## 📝 Licencia

Este proyecto está bajo la licencia MIT. Ver el archivo [LICENSE](LICENSE) para más detalles.

## 👤 Autor

**Tu Nombre**
- GitHub: [@wualtervera](https://github.com/tu-usuario)
- Email: waltervera693@gmail.com

## 🙏 Agradecimientos
- CHRISTIAN RUIZ GONZALES (Mi master)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Hibernate](https://hibernate.org/)
- [MySQL](https://www.mysql.com/)

---

⭐ ¡No olvides dar una estrella al proyecto si te fue útil!