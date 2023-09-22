//package com.tienda.DAO;
//
//import com.tienda.Model.User;
//import com.tienda.Configs.HibernateUtil;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.Transaction;
//import org.hibernate.query.Query;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class UserDaoHibernateTest {
//
//    private SessionFactory sessionFactory;
//    private UserDao userDao;
//
//    @BeforeEach
//    public void setUp() {
//        // Configurar una fábrica de sesiones de Hibernate para las pruebas.
//        sessionFactory = HibernateUtil.getSessionFactory();
//        userDao = new UserDaoHibernate(sessionFactory);
//    }
//
//    @AfterEach
//    public void tearDown() {
//        // Cerrar la fábrica de sesiones de Hibernate después de cada prueba.
//        sessionFactory.close();
//    }
//
//    @Test
//    public void testGetUserByEmail_UserExists() {
//        // Crear un usuario de prueba y agregarlo a la base de datos.
//        User user = new User("testemail@example.com", "password");
//        addUserToDatabase(user);
//
//        // Realizar la prueba para obtener el usuario por dirección de correo electrónico.
//        User retrievedUser = userDao.getUserByEmail("testemail@example.com");
//
//        assertNotNull(retrievedUser);
//        assertEquals("testemail@example.com", retrievedUser.getEmail());
//    }
//
//    @Test
//    public void testGetUserByEmail_UserDoesNotExist() {
//        // Realizar la prueba para obtener un usuario por dirección de correo electrónico que no existe.
//        User retrievedUser = userDao.getUserByEmail("nonexistent@example.com");
//
//        assertNull(retrievedUser);
//    }
//
//    @Test
//    public void testAuthenticateUser_ValidCredentials() {
//        // Crear un usuario de prueba y agregarlo a la base de datos.
//        User user = new User("testauth@example.com", "password");
//        addUserToDatabase(user);
//
//        // Realizar la prueba de autenticación con credenciales válidas.
//        boolean authenticated = userDao.authenticateUser("testauth@example.com", "password");
//
//        assertTrue(authenticated);
//    }
//
//    @Test
//    public void testAuthenticateUser_InvalidCredentials() {
//        // Crear un usuario de prueba y agregarlo a la base de datos.
//        User user = new User("testauth@example.com", "password");
//        addUserToDatabase(user);
//
//        // Realizar la prueba de autenticación con credenciales inválidas.
//        boolean authenticated = userDao.authenticateUser("testauth@example.com", "wrongpassword");
//
//        assertFalse(authenticated);
//    }
//
//    @Test
//    public void testAddUser() {
//        // Crear un usuario de prueba y agregarlo a la base de datos.
//        User user = new User("newuser@example.com", "newpassword");
//        userDao.addUser(user);
//
//        // Verificar si el usuario se ha agregado correctamente.
//        User retrievedUser = getUserByEmailFromDatabase("newuser@example.com");
//        assertNotNull(retrievedUser);
//        assertEquals("newuser@example.com", retrievedUser.getEmail());
//    }
//
//    @Test
//    public void testGetUserById_UserExists() {
//        // Crear un usuario de prueba y agregarlo a la base de datos.
//        User user = new User("userbyid@example.com", "password");
//        addUserToDatabase(user);
//
//        // Obtener el ID del usuario agregado.
//        Long userId = user.getId();
//
//        // Realizar la prueba para obtener el usuario por su ID.
//        User retrievedUser = userDao.getUserById(userId);
//
//        assertNotNull(retrievedUser);
//        assertEquals(userId, retrievedUser.getId());
//    }
//
//    @Test
//    public void testGetUserById_UserDoesNotExist() {
//        // Realizar la prueba para obtener un usuario por un ID que no existe.
//        User retrievedUser = userDao.getUserById(999L); // ID no existente.
//
//        assertNull(retrievedUser);
//    }
//
//    @Test
//    public void testGetAllUsers() {
//        // Crear varios usuarios de prueba y agregarlos a la base de datos.
//        User user1 = new User("user1@example.com", "password1");
//        User user2 = new User("user2@example.com", "password2");
//        User user3 = new User("user3@example.com", "password3");
//        addUserToDatabase(user1);
//        addUserToDatabase(user2);
//        addUserToDatabase(user3);
//
//        // Realizar la prueba para obtener una lista de todos los usuarios.
//        List<User> userList = userDao.getAllUsers();
//
//        assertNotNull(userList);
//        assertEquals(3, userList.size());
//    }
//
//    @Test
//    public void testIsDniExists_DniExists() {
//        // Crear un usuario de prueba con un DNI y agregarlo a la base de datos.
//        User user = new User("dnitest@example.com", "password");
//        user.setDni("123456789");
//        addUserToDatabase(user);
//
//        // Realizar la prueba para verificar si el DNI existe.
//        boolean dniExists = userDao.isDniExists("123456789");
//
//        assertTrue(dniExists);
//    }
//
//    @Test
//    public void testIsDniExists_DniDoesNotExist() {
//        // Realizar la prueba para verificar si un DNI no existe en la base de datos.
//        boolean dniExists = userDao.isDniExists("nonexistentdni");
//
//        assertFalse(dniExists);
//    }
//
//    @Test
//    public void testIsEmailExists_EmailExists() {
//        // Crear un usuario de prueba con una dirección de correo electrónico y agregarlo a la base de datos.
//        User user = new User("emailexists@example.com", "password");
//        addUserToDatabase(user);
//
//        // Realizar la prueba para verificar si la dirección de correo electrónico existe.
//        boolean emailExists = userDao.isEmailExists("emailexists@example.com");
//
//        assertTrue(emailExists);
//    }
//
//    @Test
//    public void testIsEmailExists_EmailDoesNotExist() {
//        // Realizar la prueba para verificar si una dirección de correo electrónico no existe en la base de datos.
//        boolean emailExists = userDao.isEmailExists("nonexistentemail@example.com");
//
//        assertFalse(emailExists);
//    }
//
//    // Método de utilidad para agregar un usuario a la base de datos.
//    private void addUserToDatabase(User user) {
//        try (Session session = sessionFactory.openSession()) {
//            Transaction transaction = session.beginTransaction();
//            session.save(user);
//            transaction.commit();
//        }
//    }
//
//    // Método de utilidad para obtener un usuario por dirección de correo electrónico desde la base de datos.
//    private User getUserByEmailFromDatabase(String email) {
//        try (Session session = sessionFactory.openSession()) {
//            Query<User> query = session.createQuery("FROM User WHERE email = :email", User.class);
//            query.setParameter("email", email);
//            return query.uniqueResult();
//        }
//    }
//}
