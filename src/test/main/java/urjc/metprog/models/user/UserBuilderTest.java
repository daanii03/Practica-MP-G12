package test.main.java.urjc.metprog.models.user;

import main.java.urjc.metprog.models.user.User;
import main.java.urjc.metprog.models.user.UserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserBuilderTest {

    private UserBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new UserBuilder();
    }

    @Test
    void getName() {
        assertNull(builder.getName(), "El nombre debería ser nulo al instanciar el builder vacío");
    }

    @Test
    void setName() {
        UserBuilder result = builder.setName("Ana");
        assertEquals("Ana", builder.getName(), "El nombre no se guardó correctamente");
        assertSame(builder, result, "El método setName debe devolver la misma instancia del builder");
    }

    @Test
    void getNick() {
        assertNull(builder.getNick(), "El nick debería ser nulo por defecto");
    }

    @Test
    void setNick() {
        UserBuilder result = builder.setNick("ana_gamer");
        assertEquals("ana_gamer", builder.getNick(), "El nick no se guardó correctamente");
        assertSame(builder, result, "El método setNick debe devolver la misma instancia del builder");
    }

    @Test
    void getPassword() {
        assertNull(builder.getPassword(), "La contraseña debería ser nula por defecto");
    }

    @Test
    void setPassword() {
        UserBuilder result = builder.setPassword("secreta123");
        assertEquals("secreta123", builder.getPassword(), "La contraseña no se guardó correctamente");
        assertSame(builder, result, "El método setPassword debe devolver la misma instancia");
    }

    @Test
    void getRegistrationNumber() {
        assertNull(builder.getRegistrationNumber(), "El número de registro debería ser nulo por defecto");
    }

    @Test
    void setRegistrationNumber() {
        UserBuilder result = builder.setRegistrationNumber("L12AA");
        assertEquals("L12AA", builder.getRegistrationNumber(), "El número de registro no se guardó correctamente");
        assertSame(builder, result, "El método setRegistrationNumber debe devolver la misma instancia");
    }

    @Test
    void getCharacter() {
        assertNull(builder.getCharacter(), "El personaje debería ser nulo por defecto");
    }

    @Test
    void setCharacter() {
        // Usamos null simulando la ausencia de un personaje para simplificar el test unitario
        UserBuilder result = builder.setCharacter(null);
        assertNull(builder.getCharacter(), "El setter debería haber asignado el personaje (null en este test)");
        assertSame(builder, result, "El método setCharacter debe devolver la misma instancia");
    }

    @Test
    void getVictories() {
        assertEquals(0, builder.getVictories(), "Las victorias deberían ser 0 por defecto (primitivo int)");
    }

    @Test
    void setVictories() {
        UserBuilder result = builder.setVictories(10);
        assertEquals(10, builder.getVictories(), "Las victorias no se actualizaron correctamente");
        assertSame(builder, result, "El método setVictories debe devolver la misma instancia");
    }

    @Test
    void getRole() {
        assertNull(builder.getRole(), "El rol debería ser nulo por defecto");
    }

    @Test
    void setRole() {
        UserBuilder result = builder.setRole("operator");
        assertEquals("operator", builder.getRole(), "El rol no se guardó correctamente");
        assertSame(builder, result, "El método setRole debe devolver la misma instancia");
    }

    @Test
    void isBlocked() {
        assertFalse(builder.isBlocked(), "isBlocked debería ser falso por defecto (primitivo boolean)");
    }

    @Test
    void setBlocked() {
        UserBuilder result = builder.setBlocked(true);
        assertTrue(builder.isBlocked(), "El estado bloqueado no se actualizó correctamente");
        assertSame(builder, result, "El método setBlocked debe devolver la misma instancia");
    }

    @Test
    void build() {
        // Probamos que todos los metodos funciones y que el objeto se cree correctamente
        User user = builder.setName("Carlos")
                .setNick("carlitos")
                .setPassword("pass1234")
                .setRole("player")
                .build();

        assertNotNull(user, "El método build no debería devolver un nulo");
        assertEquals("Carlos", user.getName(), "El objeto User no recibió el nombre del builder");
        assertEquals("carlitos", user.getNick(), "El objeto User no recibió el nick del builder");
        assertEquals("pass1234", user.getPassword(), "El objeto User no recibió la contraseña del builder");
        assertEquals("player", user.getRole(), "El objeto User no recibió el rol del builder");
        assertFalse(user.isBlocked(), "El objeto User debería heredar el false por defecto del builder");
    }
}