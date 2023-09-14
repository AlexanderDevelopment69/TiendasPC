package com.tienda.DAO;

import com.tienda.Model.User;
import com.tienda.Configs.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;


/**
 Implemementacion:
 SessionFactory sessionFactory = HibernateUtil.getSessionFactory(); // Obtén la fábrica de sesiones desde tu clase HibernateUtil
 UserDao userDao = new UserDaoHibernate(sessionFactory); // Crea una instancia del UserDaoHibernate pasando la fábrica de sesiones
 */
public class UserDaoHibernate implements UserDao {

    // La fábrica de sesiones de Hibernate que se inyectará en el constructor.
    private final SessionFactory sessionFactory;

    /**
     * Constructor que recibe la fábrica de sesiones de Hibernate.
     *
     * @param sessionFactory La fábrica de sesiones de Hibernate.
     */
    public UserDaoHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }



    /**
     * Obtiene un usuario por su dirección de correo electrónico.
     *
     * @param email La dirección de correo electrónico del usuario.
     * @return El usuario encontrado o null si no se encuentra.
     */
    @Override
    public User getUserByEmail(String email) {
        // Abre una nueva sesión de Hibernate.
        Session session = sessionFactory.openSession();
        try {
            // Realiza una consulta para obtener un usuario por su dirección de correo electrónico.
            return session.createQuery("FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();
        } finally {
            // Cierra la sesión de Hibernate en un bloque finally para asegurar su cierre.
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }


    /**
     * Autentica a un usuario verificando su dirección de correo electrónico y contraseña.
     *
     * @param email    La dirección de correo electrónico del usuario.
     * @param password La contraseña del usuario.
     * @return True si la autenticación es exitosa, False en caso contrario.
     */
    @Override
    public Boolean authenticateUser(String email, String password) {
        Session session = sessionFactory.openSession();
        try {
            // Crea una consulta para buscar un usuario por dirección de correo electrónico.
            Query<User> query = session.createQuery("FROM User WHERE email = :email", User.class);
            query.setParameter("email", email);

            // Obtiene el usuario encontrado o null si no existe.
            User user = query.uniqueResult();

            // Verifica si el usuario existe y si la contraseña coincide.
            return user != null && user.getPassword().equals(password);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }



    /**
     * Agrega un nuevo usuario a la base de datos.
     *
     * @param user El usuario que se va a agregar.
     */

    @Override
    public void addUser(User user) {
        // Abre una nueva sesión de Hibernate.
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            // Inicia una transacción para realizar una operación de escritura.
            transaction = session.beginTransaction();
            // Guarda un nuevo usuario en la base de datos.
            session.save(user);
            // Confirma la transacción para aplicar los cambios a la base de datos.
            transaction.commit();
        } catch (Exception e) {
            // En caso de error, revierte la transacción.
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            // Cierra la sesión de Hibernate en un bloque finally para asegurar su cierre.
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * Obtiene un usuario por su ID único.
     *
     * @param userId El ID del usuario.
     * @return El usuario encontrado o null si no se encuentra.
     */
    @Override
    public User getUserById(Long userId) {
        // Abre una nueva sesión de Hibernate.
        Session session = sessionFactory.openSession();
        try {
            // Obtiene un usuario por su ID único.
            return session.get(User.class, userId);
        } finally {
            // Cierra la sesión de Hibernate en un bloque finally para asegurar su cierre.
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }


    /**
     * Obtiene una lista de todos los usuarios en la base de datos.
     *
     * @return Una lista de usuarios.
     */
    @Override
    public List<User> getAllUsers() {
        // Abre una nueva sesión de Hibernate.
        Session session = sessionFactory.openSession();
        try {
            // Obtiene una lista de todos los usuarios en la base de datos.
            return session.createQuery("FROM User", User.class).list();
        } finally {
            // Cierra la sesión de Hibernate en un bloque finally para asegurar su cierre.
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public boolean isDniExists(String dni) {
        try (Session session = HibernateUtil.openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(u) FROM User u WHERE u.dni = :dni", Long.class);
            query.setParameter("dni", dni);

            Long count = query.getSingleResult();
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    @Override
    public boolean isEmailExists(String email) {
        try (Session session = HibernateUtil.openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class);
            query.setParameter("email", email);

            Long count = query.getSingleResult();
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }





    /*

    * */








}
