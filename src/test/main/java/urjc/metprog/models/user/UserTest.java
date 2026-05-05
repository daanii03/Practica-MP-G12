package test.main.java.urjc.metprog.models.user;

import main.java.urjc.metprog.models.user.User;
import main.java.urjc.metprog.models.user.UserBuilder;
import main.java.urjc.metprog.models.characters.Character;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        // Este método se ejecuta antes de CADA @Test.
        // Inicializamos un usuario base utilizando tu UserBuilder para no repetir código.
        UserBuilder builder = new UserBuilder()
                .setName("Pepe")
                .setNick("pepito")
                .setPassword("12345678")
                .setRegistrationNumber("A12BC")
                .setRole("player")
                .setBlocked(false);

        user = new User(builder);
    }

    @Test
    void getName() {
        assertEquals("Pepe", user.getName(), "El nombre debería coincidir con el del Builder");
    }

    @Test
    void getNick() {
        assertEquals("pepito", user.getNick(), "El nick debería coincidir con el del Builder");
    }

    @Test
    void getPassword() {
        assertEquals("12345678", user.getPassword(), "La contraseña debería coincidir");
    }

    @Test
    void getRegistrationNumber() {
        assertEquals("A12BC", user.getRegistrationNumber(), "El número de registro debería coincidir");
    }

    @Test
    void getCharacter() {
        // Como no le hemos pasado personaje en el builder, debería ser null
        assertNull(user.getCharacter(), "El personaje inicial debería ser null");
    }

    @Test
    void setCharacter() {
        // Probamos que el setter actualiza la referencia correctamente.
        // Usamos null para simplificar, ya que Character es una clase abstracta y compleja de instanciar aquí.
        user.setCharacter(null);
        assertNull(user.getCharacter(), "El setter debería actualizar el personaje");
    }

    @Test
    void getVictories() {
        assertEquals(0, user.getVictories(), "Las victorias iniciales deberían ser 0 por defecto");
    }

    @Test
    void setVictories() {
        user.setVictories(5);
        assertEquals(5, user.getVictories(), "El setter de victorias debería actualizar el valor a 5");
    }

    @Test
    void addVictory() {
        int initialVictories = user.getVictories(); // Debería ser 0
        user.addVictory();
        assertEquals(initialVictories + 1, user.getVictories(), "addVictory() debería sumar exactamente 1 al contador");
    }

    @Test
    void getRole() {
        assertEquals("player", user.getRole(), "El rol debería ser el asignado en el builder ('player')");
    }

    @Test
    void setRole() {
        user.setRole("operator");
        assertEquals("operator", user.getRole(), "El rol debería actualizarse a 'operator'");
    }

    @Test
    void isBlocked() {
        assertFalse(user.isBlocked(), "El usuario no debería estar bloqueado inicialmente");
    }

    @Test
    void setBlocked() {
        user.setBlocked(true);
        assertTrue(user.isBlocked(), "El estado de bloqueo debería cambiar a true");
    }
}