package com.tienda.Dao;

import com.tienda.Model.Role;
import com.tienda.Model.User;
import com.tienda.dto.UserDTO;
import com.tienda.mapper.UserMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Implemementacion:
 * SessionFactory sessionFactory = HibernateUtil.getSessionFactory(); // Obtén la fábrica de sesiones desde tu clase HibernateUtil
 * UserDao userDao = new UserDaoHibernate(sessionFactory); // Crea una instancia del UserDaoHibernate pasando la fábrica de sesiones
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
     * @param email La dirección de correo electrónico del usuario que se busca.
     * @return Un objeto UserDTO que representa al usuario encontrado o null si no se encuentra.
     */
    @Override
    public UserDTO getUserByEmail(String email) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            // Crea una consulta para buscar un usuario por dirección de correo electrónico.
            Query<User> query = session.createQuery("FROM User WHERE email = :email", User.class);
            query.setParameter("email", email);

            // Obtiene el usuario encontrado o null si no existe.
            User user = query.uniqueResult();

            // Si el usuario no se encuentra, devuelve null.
            if (user == null) {
                return null;
            }

            // Utiliza el método estático del mapeador para mapear el objeto User a UserDTO.
            return UserMapper.userToUserDTO(user);
        } catch (Exception e) {
            // Manejar la excepción aquí (por ejemplo, registrarla o lanzar una excepción personalizada).
            e.printStackTrace();
            return null; // Devuelve null en caso de error.
        } finally {
            // Cierra la sesión de Hibernate en un bloque finally para asegurar su cierre.
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }


    /**
     * Obtiene un usuario por su dni.
     *
     * @param dni El dni del usuario que se busca.
     * @return Un objeto UserDTO que representa al usuario encontrado o null si no se encuentra.
     */
    @Override
    public UserDTO getUserByDni(String dni) {

        Session session = null;
        try {
            session = sessionFactory.openSession();
            // Crea una consulta para buscar un usuario por el dni.
            Query<User> query = session.createQuery("FROM User WHERE dni = :dni", User.class);
            query.setParameter("dni", dni);

            // Obtiene el usuario encontrado o null si no existe.
            User user = query.uniqueResult();

            // Si el usuario no se encuentra, devuelve null.
            if (user == null) {
                return null;
            }

            // Utiliza el método estático del mapeador para mapear el objeto User a UserDTO.
            return UserMapper.userToUserDTO(user);
        } catch (Exception e) {
            // Manejar la excepción aquí (por ejemplo, registrarla o lanzar una excepción personalizada).
            e.printStackTrace();
            return null; // Devuelve null en caso de error.
        } finally {
            // Cierra la sesión de Hibernate en un bloque finally para asegurar su cierre.
            if (session != null && session.isOpen()) {
                session.close();
            }
        }


    }


    /**
     * Autentica a un usuario utilizando su dni y contraseña.
     *
     * @param dni El dni del usuario.
     * @param password La contraseña del usuario.
     * @return true si la autenticación es exitosa, false en caso contrario.
     */
    @Override
    public Boolean authenticateUser(String dni, String password) {
        Session session = null;
        try {
            // Abre una nueva sesión de Hibernate.
            session = sessionFactory.openSession();

            // Crea una consulta para buscar un usuario por el dni.
            Query<User> query = session.createQuery("FROM User WHERE dni = :dni", User.class);
            query.setParameter("dni", dni);

            // Obtiene el usuario encontrado o null si no existe.
            User user = query.uniqueResult();

            // Verifica si el usuario existe y si la contraseña coincide.
            return user != null && user.getPassword().equals(password);
        } catch (Exception e) {
            // Manejar la excepción aquí (por ejemplo, registrarla o lanzar una excepción personalizada).
            e.printStackTrace();
            return false;
        } finally {
            // Cierra la sesión de Hibernate en un bloque finally para asegurar su cierre.
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }


    /**
     * Agrega un nuevo usuario a la base de datos.
     *
     * @param userDTO El DTO del usuario que se va a agregar.
     */
    @Override
    public void addUser(UserDTO userDTO) {
        Transaction transaction = null;
        Session session = null;
        try {
            // Abre una nueva sesión de Hibernate.
            session = sessionFactory.openSession();
            // Inicia una transacción para realizar una operación de escritura.
            transaction = session.beginTransaction();

            // Utiliza el mapeador para convertir el DTO a una entidad User.
            User user = UserMapper.userDTOToUser(userDTO);

            // Guarda el nuevo usuario en la base de datos.
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
     * Actualiza los datos de un usuario, incluido su rol, en la base de datos.
     *
     * @param userId     El ID del usuario que se actualizará.
     * @param updatedUserDTO  Los nuevos datos del usuario en forma de objeto UserDTO,
     *                       incluyendo el ID del nuevo rol.
     */
    @Override
    public void updateUser(Long userId, UserDTO updatedUserDTO) {
        Session session = null;
        Transaction transaction = null;

        try {
            // Abre una nueva sesión de Hibernate.
            session = sessionFactory.openSession();
            // Inicia una transacción para realizar una operación de escritura.
            transaction = session.beginTransaction();

            // Obtén el usuario por su ID.
            User user = session.get(User.class, userId);

            if (user != null) {
                // Actualiza los campos del usuario con los datos del objeto UserDTO,
                // solo si los campos en UserDTO no son nulos.
                if (updatedUserDTO.getName() != null) {
                    user.setName(updatedUserDTO.getName());
                }
                if (updatedUserDTO.getLastName() != null) {
                    user.setLastName(updatedUserDTO.getLastName());
                }
                if (updatedUserDTO.getDni() != null) {
                    user.setDni(updatedUserDTO.getDni());
                }
                if (updatedUserDTO.getEmail() != null) {
                    user.setEmail(updatedUserDTO.getEmail());
                }
                if (updatedUserDTO.getPassword() != null) {
                    user.setPassword(updatedUserDTO.getPassword());
                }

                // Obtén el ID del nuevo rol del objeto UserDTO.
                Long newRoleId = updatedUserDTO.getRoleId();

                if (newRoleId != null) {
                    // Consulta y obtén el nuevo rol por su ID.
                    Role newRole = session.get(Role.class, newRoleId);

                    if (newRole != null) {
                        // Establece el nuevo rol del usuario.
                        user.setRole(newRole);
                    } else {
                        // Manejar el caso en el que el nuevo rol no existe.
                        // Puedes lanzar una excepción o tomar otra acción apropiada.

                    }
                }

                // Guarda el usuario actualizado en la base de datos.
                session.update(user);

                // Confirma la transacción para aplicar los cambios a la base de datos.
                transaction.commit();
            } else {
                // Manejar el caso en el que el usuario no existe.
                // Puedes lanzar una excepción o tomar otra acción apropiada.
            }
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
     * Actualiza el rol de un usuario en la base de datos.
     *
     * @param userId    El ID del usuario cuyo rol se actualizará.
     * @param newRoleId El nuevo ID del rol que se asignará al usuario.
     */
    @Override
    public void updateUserRole(Long userId, Long newRoleId) {
        Session session = null;
        Transaction transaction = null;

        try {
            // Abre una nueva sesión de Hibernate.
            session = sessionFactory.openSession();
            // Inicia una transacción para realizar una operación de escritura.
            transaction = session.beginTransaction();

            // Obtén el usuario por su ID.
            User user = session.get(User.class, userId);

            if (user != null) {
                // Cambia el ID del rol del usuario al nuevo valor.
                Role newRole = session.get(Role.class, newRoleId);
                user.setRole(newRole);

                // Guarda el usuario actualizado en la base de datos.
                session.update(user);

                // Confirma la transacción para aplicar los cambios a la base de datos.
                transaction.commit();
            } else {
                // Manejar el caso en el que el usuario no existe.
                // Puedes lanzar una excepción o tomar otra acción apropiada.
            }
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
     * Elimina un usuario de la base de datos por su identificador único (ID).
     *
     * @param userId El ID único del usuario que se va a eliminar.
     */
    @Override
    public void deleteUserById(Long userId) {
        Session session = null;
        Transaction transaction = null;
        try {
            // Abre una nueva sesión de Hibernate.
            session = sessionFactory.openSession();
            // Inicia una transacción para realizar una operación de escritura.
            transaction = session.beginTransaction();

            // Utiliza el método `session.get` para obtener el usuario por su ID.
            User user = session.get(User.class, userId);

            // Verifica si el usuario existe antes de eliminarlo.
            if (user != null) {
                // Elimina el usuario de la base de datos.
                session.delete(user);
            }

            // Confirma la transacción para aplicar los cambios a la base de datos.
            transaction.commit();
        } catch (Exception e) {
            // En caso de error, revierte la transacción.
            if (transaction != null) {
                transaction.rollback();
            }
            // Manejar la excepción aquí (por ejemplo, registrarla o lanzar una excepción personalizada).
            e.printStackTrace();
        } finally {
            // Cierra la sesión de Hibernate en un bloque finally para asegurar su cierre adecuado.
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }


    /**
     * Obtiene un usuario por su identificador único (ID).
     *
     * @param userId El ID único del usuario que se busca.
     * @return Un objeto UserDTO que representa al usuario encontrado o null si no se encuentra.
     */
    @Override
    public UserDTO getUserById(Long userId) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            // Obtiene un usuario por su ID.
            User user = session.get(User.class, userId);

            // Si el usuario no se encuentra, devuelve null.
            if (user == null) {
                return null;
            }

            // Utiliza el método estático del mapeador para mapear el objeto User a UserDTO.
            return UserMapper.userToUserDTO(user);
        } catch (Exception e) {
            // Manejar la excepción aquí (por ejemplo, registrarla o lanzar una excepción personalizada).
            e.printStackTrace();
            return null; // Devuelve null en caso de error.
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
//    @Override
//    public List<User> getAllUsers() {
//
//        Session session = null;
//        try {
//            // Abre una nueva sesión de Hibernate.
//            session = sessionFactory.openSession();
//            // Obtiene una lista de todos los usuarios en la base de datos.
//            return session.createQuery("FROM User", User.class).list();
//        } catch (Exception e) {
//            // Manejar la excepción aquí
//            e.printStackTrace();
//            return null;
//        } finally {
//            // Cierra la sesión de Hibernate en un bloque finally para asegurar su cierre.
//            if (session != null && session.isOpen()) {
//                session.close();
//            }
//        }
//    }

    /**
     * Obtiene una lista de todos los usuarios y los convierte en objetos UserDTO.
     *
     * @return Una lista de objetos UserDTO que representan a los usuarios.
     */
    public List<UserDTO> getAllUsers() {
        Session session = null;
        try {
            // Abre una nueva sesión de Hibernate.
            session = sessionFactory.openSession();

            // Obtiene una lista de todos los usuarios en la base de datos.
            List<User> users = session.createQuery("FROM User", User.class).list();


            // Utiliza un flujo de datos para mapear cada objeto User a UserDTO utilizando el método estático del mapeador.
            List<UserDTO> userDTOList = users.stream()
                    .map(UserMapper::userToUserDTO) // Mapea User a UserDTO utilizando el método estático del mapeador.
                    .collect(Collectors.toList());

            // Devuelve la lista de objetos UserDTO.
            return userDTOList;
        } catch (Exception e) {
            // Manejar la excepción aquí (por ejemplo, registrarla o lanzar una excepción personalizada).
            e.printStackTrace();
            return Collections.emptyList(); // Devuelve una lista vacía en caso de error.
        } finally {
            // Cierra la sesión de Hibernate en un bloque finally para asegurar su cierre.
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }


    /**
     * Verifica si un número de DNI (Documento Nacional de Identidad) existe en la base de datos.
     *
     * @param dni El número de DNI a verificar si existe en la base de datos.
     * @return true si el DNI existe en la base de datos; false en caso contrario o si ocurre un error.
     */
    @Override
    public boolean isDniExists(String dni) {
        try {
            // Abre una nueva sesión de Hibernate utilizando la sessionFactory previamente configurada.
            try (Session session = sessionFactory.openSession()) {
                // Crea una consulta de Hibernate para contar la cantidad de registros en la tabla "User"
                // donde el campo "dni" sea igual al valor proporcionado como parámetro ":dni".
                Query<Long> query = session.createQuery("SELECT COUNT(u) FROM User u WHERE u.dni = :dni", Long.class);

                // PARAM: Asigna el valor del parámetro ":dni" en la consulta para que coincida con el valor proporcionado.
                query.setParameter("dni", dni);

                // Ejecuta la consulta y obtiene el resultado como un valor Long.
                Long count = query.getSingleResult();

                // Retorna true si el recuento es mayor que 0 (indicando que el DNI existe en la base de datos),
                // de lo contrario, retorna false.
                return count > 0;
            }
        } catch (Exception e) {
            // En caso de cualquier excepción durante el proceso, imprime el stack trace de la excepción
            // para depuración y retorna false para indicar que no se pudo verificar el DNI.
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Verifica si una dirección de correo electrónico existe en la base de datos.
     *
     * @param email La dirección de correo electrónico a verificar si existe en la base de datos.
     * @return true si el correo electrónico existe en la base de datos; false en caso contrario o si ocurre un error.
     */
    @Override
    public boolean isEmailExists(String email) {
        try {
            // Abre una nueva sesión de Hibernate utilizando la sessionFactory previamente configurada.
            try (Session session = sessionFactory.openSession()) {
                // Crea una consulta de Hibernate para contar la cantidad de registros en la tabla "User"
                // donde el campo "email" sea igual al valor proporcionado como parámetro ":email".
                Query<Long> query = session.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class);

                // PARAM: Asigna el valor del parámetro ":email" en la consulta para que coincida con el valor proporcionado.
                query.setParameter("email", email);

                // Ejecuta la consulta y obtiene el resultado como un valor Long.
                Long count = query.getSingleResult();

                // Retorna true si el recuento es mayor que 0 (indicando que el correo electrónico existe en la base de datos),
                // de lo contrario, retorna false.
                return count > 0;
            }
        } catch (Exception e) {
            // En caso de cualquier excepción durante el proceso, imprime el stack trace de la excepción
            // para depuración y retorna false para indicar que no se pudo verificar el correo electrónico.
            e.printStackTrace();
            return false;
        }
    }


}
