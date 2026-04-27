# Práctica de Metodología de la Programación - Grupo 12

Este repositorio contiene el código y la documentación del proyecto de la asignatura Metodología de la Programación correspondiente al curso 2025/2026. 

El proyecto consiste en el desarrollo de un videojuego de luchas de criaturas fantásticas para la empresa simulada MetProg URJC S.L.

## 👥 Miembros del Equipo
El equipo está organizado de manera autogestionada con los siguientes roles:

* **Ivo Hagemann Martinez**: Jefe de Proyectos y Analista programador
* **Daniel Marcos Serrano**: Ingeniero de desarrollo
* **Héctor Moreno García**: QA (Aseguramiento de Calidad)
* **Jaime Bergad Lario**: Ingeniero de desarrollo
* **Marcos Laorga Sagaseta de Ilurdoz**: Analista funcional

## ⚔️ Descripción del Videojuego
El sistema permite a los usuarios registrarse, gestionar personajes y retarse en combates. La aplicación cuenta con dos tipos de usuarios: administrador (operador) y jugador.

### Características Principales
* **Tipos de Personajes**: Los jugadores pueden controlar Vampiros (usan puntos de sangre y Disciplinas), Licántropos (usan Rabia y Dones) y Cazadores (usan Voluntad y Talentos).
* **Gestión de Equipo**: Los personajes pueden equiparse con diferentes armas (de 1 o 2 manos) y armaduras que modifican sus estadísticas de ataque y defensa.
* **Esbirros**: Sistema de acompañantes que absorben el daño antes que el personaje principal, incluyendo humanos, ghouls y demonios (que pueden tener a su vez otros esbirros).
* **Desafíos y Apuestas**: Los jugadores pueden desafiar a otros usuarios apostando oro, sujeto a la validación de un operador del sistema.
* **Sistema de Combate**: Resolución de batallas por rondas automatizadas mediante el cálculo de potenciales de ataque y defensa, sumado a tiradas de éxito aleatorias.
* **Persistencia**: Almacenamiento del estado de los usuarios, personajes y un registro histórico de todos los combates realizados.

## 🛠️ Tecnologías y Herramientas
Para el desarrollo de este proyecto se han utilizado las siguientes herramientas:
* **Lenguaje**: Java JDK 21
* **Entorno de Desarrollo (IDE)**: IntelliJ IDEA
* **Control de Versiones**: GitHub
* **Comunicación**: Microsoft Teams
* **Diseño UML**: Draw.io

## ⚙️ Instrucciones de Ejecución

1. Clonar el repositorio: `git clone https://github.com/daanii03/Practica-MP-G12.git`
2. Abrir el proyecto en IntelliJ IDEA.
3. Asegurarse de tener configurado el JDK 21.
4. Ejecutar la clase principal del sistema.
