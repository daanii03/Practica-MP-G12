# Práctica de Metodología de la Programación - Grupo 12

## 📋 Descripción del Proyecto

Este proyecto es un videojuego de luchas de criaturas fantásticas llamado **Blood & Bones**, desarrollado para la empresa simulada **MetProg URJC S.L.**, en el marco de la asignatura *Metodología de la Programación* (curso 2025/2026).

La aplicación permite a los usuarios registrarse, gestionar personajes y enfrentarse en combates por rondas. Existen dos roles de usuario: **jugador** y **administrador (operador)**. Los jugadores pueden elegir entre tres tipos de personajes —Vampiros, Licántropos y Cazadores—, equiparlos con armas y armaduras, gestionar esbirros y desafiar a otros jugadores apostando oro. El sistema resuelve los combates automáticamente mediante cálculo de potenciales de ataque/defensa y tiradas aleatorias, y persiste el estado de la partida y el historial de combates en una base de datos SQLite.

---

## ✅ Requisitos Previos

Antes de compilar o ejecutar el proyecto, asegúrate de tener instalado:

- **Java JDK 21** → [Descargar aquí](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
- **Git** → [Descargar aquí](https://git-scm.com/)

Verifica tu versión de Java con:
```bash
java -version
```

---

## ⚙️ Compilación y Ejecución

**1. Clona el repositorio:**
```bash
git clone https://github.com/daanii03/Practica-MP-G12.git
cd Practica-MP-G12
```

**2. Compila el proyecto:**

En **Linux/macOS**:
```bash
javac -d bin -cp lib/sqlite-jdbc-3.53.0.0.jar $(find src/main/java -name "*.java")
```

En **Windows (CMD)**:
```cmd
javac -d bin -cp lib/sqlite-jdbc-3.53.0.0.jar $(dir /s /b src\main\java\*.java)
```

**3. Ejecuta el programa:**

En **Linux/macOS**:
```bash
java -cp bin:lib/sqlite-jdbc-3.53.0.0.jar urjc.metprog.Main
```

En **Windows (CMD)**:
```cmd
java -cp bin;lib/sqlite-jdbc-3.53.0.0.jar urjc.metprog.Main
```

---

## 💬 Interacción con el Usuario

Toda la interacción se realiza por **teclado** a través de menús de texto. No se requieren rutas de archivos ni configuración adicional.

### Menú inicial
Al arrancar el programa aparece el menú principal de **Blood & Bones** con las siguientes opciones:
- `1` Iniciar sesión → pide **nick** y **contraseña**
- `2` Registrar nuevo jugador → pide **nombre completo**, **nick** y **contraseña** (8-12 caracteres). Al completarse se muestra el número de registro asignado.
- `3` Registrar nuevo administrador → mismos campos que el jugador.
- `0` Salir

### Menú del jugador
Tras iniciar sesión como jugador se accede a:
- **Gestionar Personaje**: crear personaje (nombre + raza: Vampiro, Licántropo o Cazador), ver su información, eliminarlo, gestionar el inventario (armas y armaduras) y gestionar esbirros (Humano, Ghoul o Demonio, cada uno con sus propios atributos).
- **Desafíos**: retar a otro jugador apostando una cantidad de oro, responder desafíos recibidos y consultar el historial de combates. Si hay un desafío pendiente de respuesta, el sistema avisa al iniciar sesión.
- **Ver Ranking Global**: muestra la clasificación de todos los jugadores.
- **Darse de baja**: elimina la cuenta del jugador.

### Menú del administrador
Tras iniciar sesión como administrador se accede a:
- **Editar personaje de usuario**: modificar las estadísticas del personaje de cualquier jugador.
- **Añadir equipo/esbirros/fortalezas/debilidades**: gestionar el inventario y los esbirros de cualquier jugador desde un catálogo global.
- **Validar desafíos pendientes**: aprobar o rechazar desafíos. Al validar, se pueden añadir modificadores de escenario (nombre + valor numérico) que afectan al combate.
- **Bloquear/Desbloquear usuario**: introduciendo el nick del jugador.
- **Darse de baja**: elimina la cuenta del administrador.

---

## 👥 Miembros del Equipo

| Nombre | Rol |
|---|---|
| **Ivo Hagemann Martinez** | Jefe de Proyectos y Analista Programador |
| **Daniel Marcos Serrano** | Ingeniero de Desarrollo |
| **Héctor Moreno García** | QA (Aseguramiento de Calidad) |
| **Jaime Bergad Lario** | Ingeniero de Desarrollo |