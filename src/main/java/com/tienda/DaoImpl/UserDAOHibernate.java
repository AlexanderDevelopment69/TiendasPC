package com.tienda.DaoImpl;

import com.tienda.Dao.UserDAO;
import com.tienda.Model.Role;
import com.tienda.Model.User;
import com.tienda.dto.UserDTO;
import com.tienda.mapper.UserMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserDAOHibernate implements UserDAO {
    private final SessionFactory sessionFactory;

    /**
     * Constructor que recibe la fábrica de sesiones de Hibernate.
     *
     * @param sessionFactory La fábrica de sesiones de Hibernate para la gestión de sesiones.
     */
    public UserDAOHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Guarda un nuevo usuario en la base de datos.
     *
     * @param userDTO El DTO del usuario a ser guardado.
     */
    @Override
    public void saveUser(UserDTO userDTO) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        Transaction transaction = null; // Inicializa una transacción

        try {
            transaction = session.beginTransaction(); // Inicia la transacción
            User user = UserMapper.toUser(userDTO); // Convierte el DTO en una entidad User

            // Genera el hash de la contraseña utilizando jBCrypt.
            String hashedPassword = BCrypt.hashpw(userDTO.getUserPassword(), BCrypt.gensalt());
            // Asigna el hash de la contraseña al usuario.
            user.setUserPassword(hashedPassword);

            session.save(user); // Guarda el usuario en la base de datos
            transaction.commit(); // Confirma la transacción
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Si ocurre un error, revierte la transacción
            }
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }
    }


    /**
     * Actualiza un usuario existente en la base de datos.
     *
     * @param userDTO El DTO del usuario a ser actualizado.
     *                Debe contener la información actualizada del usuario, incluyendo su ID y el nuevo ID del rol (newRoleId) si se desea asignar un nuevo rol.
     *                Si no se proporciona newRoleId o es null, el rol del usuario no se actualizará.
     *                Si se proporciona newRoleId y corresponde a un rol válido en la base de datos, se asignará al usuario antes de la actualización.
     *                Si se proporciona newRoleId pero no corresponde a un rol válido, la actualización del rol no se realizará.
     */
    @Override
    public void updateUser(UserDTO userDTO) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        Transaction transaction = null; // Inicializa una transacción

        try {
            transaction = session.beginTransaction(); // Inicia la transacción
            User user = UserMapper.toUser(userDTO); // Convierte el DTO en una entidad User

            // Genera el hash de la contraseña utilizando jBCrypt.
            String hashedPassword = BCrypt.hashpw(userDTO.getUserPassword(), BCrypt.gensalt());
            // Asigna el hash de la contraseña al usuario.
            user.setUserPassword(hashedPassword);

            // Verifica si userDTO tiene un nuevo roleId válido
            if (userDTO.getRoleId()!= null) {
                Role newRole = session.get(Role.class, userDTO.getRoleId());
                if (newRole != null) {
                    user.setRole(newRole);
                } else {
                    // Manejar el caso en el que el ID del nuevo rol no existe
                }
            }

            session.update(user); // Actualiza el usuario en la base de datos
            transaction.commit(); // Confirma la transacción
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Si ocurre un error, revierte la transacción
            }
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }
    }


    /**
     * Elimina un usuario de la base de datos por su ID.
     *
     * @param userId El ID del usuario que se desea eliminar.
     */
    @Override
    public void deleteUser(Long userId) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        Transaction transaction = null; // Inicializa una transacción

        try {
            transaction = session.beginTransaction(); // Inicia la transacción
            session.delete(session.get(User.class, userId)); // Elimina el usuario de la base de datos
            transaction.commit(); // Confirma la transacción
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Si ocurre un error, revierte la transacción
            }
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }
    }

    /**
     * Obtiene una lista de todos los usuarios.
     *
     * @return Una lista de DTOs de usuarios.
     */
    @Override
    public List<UserDTO> getAllUsers() {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        List<UserDTO> userDTOs = null; // Inicializa una lista de DTOs de usuarios

        try {
            // Consulta todos los usuarios en la base de datos
            Query<User> query = session.createQuery("FROM User", User.class);
            // Obtiene la lista de usuarios
            List<User> users = query.list();
            // Convierte las entidades en DTOs
            userDTOs = UserMapper.toUserDTOList(users);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }

        return userDTOs; // Devuelve la lista de DTOs de usuarios
    }









    /**
     * Obtiene un usuario por su dirección de correo electrónico.
     *
     * @param email La dirección de correo electrónico del usuario.
     * @return El DTO del usuario si se encuentra en la base de datos, de lo contrario, null.
     */
    @Override
    public UserDTO getUserByEmail(String email) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        UserDTO userDTO = null; // Inicializa un DTO de usuario

        try {
            // Consulta el usuario por su dirección de correo electrónico
            Query<User> query = session.createQuery("FROM User WHERE userEmail = :email", User.class);
            query.setParameter("email", email); // Establece el valor del parámetro "email" en la consulta Hibernate
            User user = query.uniqueResult(); // Obtiene el resultado único
            if (user != null) {
                userDTO = UserMapper.toUserDTO(user); // Convierte la entidad en un DTO si se encuentra
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }

        return userDTO; // Devuelve el DTO del usuario
    }

    /**
     * Obtiene un usuario por su número de DNI.
     *
     * @param dni El número de DNI del usuario.
     * @return El DTO del usuario si se encuentra en la base de datos, de lo contrario, null.
     */
    @Override
    public UserDTO getUserByDni(String dni) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        UserDTO userDTO = null; // Inicializa un DTO de usuario

        try {
            // Consulta el usuario por su número de DNI
            Query<User> query = session.createQuery("FROM User WHERE userDni = :dni", User.class);
            query.setParameter("dni", dni); // Establece el valor del parámetro "dni" en la consulta Hibernate
            User user = query.uniqueResult(); // Obtiene el resultado único
            if (user != null) {
                userDTO = UserMapper.toUserDTO(user); // Convierte la entidad en un DTO si se encuentra
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }

        return userDTO; // Devuelve el DTO del usuario
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * @param userId El ID del usuario que se desea obtener.
     * @return El DTO del usuario si se encuentra en la base de datos, de lo contrario, null.
     */
    @Override
    public UserDTO getUserById(Long userId) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        UserDTO userDTO = null; // Inicializa un DTO de usuario

        try {
            // Consulta el usuario por su ID
            User user = session.get(User.class, userId);
            if (user != null) {
                userDTO = UserMapper.toUserDTO(user); // Convierte la entidad en un DTO si se encuentra
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }

        return userDTO; // Devuelve el DTO del usuario
    }

    /**
     * Autentica a un usuario comparando su dni y contraseña.
     *
     * @param dni    El dni del usuario.
     * @param password La contraseña del usuario.
     * @return true si las credenciales son válidas, false en caso contrario.
     */
    @Override
    public boolean authenticateUser(String dni, String password) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        boolean authenticated = false; // Inicializa la variable de autenticación

        try {
            // Consulta el usuario por su dni
            Query<User> query = session.createQuery("FROM User WHERE userDni = :dni", User.class);
            query.setParameter("dni", dni); // Establece el valor del parámetro "dni" en la consulta Hibernate
            User user = query.uniqueResult(); // Obtiene el resultado único

            // Verifica si el usuario existe y si la contraseña coincide utilizando BCrypt.
            if (user != null && BCrypt.checkpw(password, user.getUserPassword())) {
                return true; // Autenticación exitosa
            } else {
                return false; // Contraseña incorrecta o usuario no encontrado
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }

        return authenticated; // Devuelve true si las credenciales son válidas, false en caso contrario
    }

    /**
     * Verifica si un número de DNI ya existe en la base de datos.
     *
     * @param dni El número de DNI a verificar.
     * @return true si el DNI ya existe en la base de datos, false en caso contrario.
     */
    @Override
    public boolean isDniExists(String dni) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        boolean exists = false; // Inicializa la variable de existencia

        try {
            // Crea una consulta Hibernate para contar registros en la tabla "User" donde el campo "userDni" coincide con el valor proporcionado.
            // Se espera que la consulta devuelva un valor numérico (Long) que representa el recuento de coincidencias.
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM User WHERE userDni = :dni", Long.class);
            query.setParameter("dni", dni); // Establece el valor del parámetro "dni" en la consulta Hibernate

            Long count = query.uniqueResult(); // Obtiene el resultado único como Long
            exists = count != null && count > 0; // Verifica si el recuento es mayor que 0
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }

        return exists; // Devuelve true si el DNI ya existe en la base de datos, false en caso contrario
    }

    /**
     * Verifica si una dirección de correo electrónico ya existe en la base de datos.
     *
     * @param email La dirección de correo electrónico a verificar.
     * @return true si el correo electrónico ya existe en la base de datos, false en caso contrario.
     */
    @Override
    public boolean isEmailExists(String email) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        boolean exists = false; // Inicializa la variable de existencia

        try {
            // Crea una consulta Hibernate para contar registros en la tabla "User" donde el campo "userEmail" coincide con el valor proporcionado.
            // Se espera que la consulta devuelva un valor numérico (Long) que representa el recuento de coincidencias.
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM User WHERE userEmail = :email", Long.class);
            query.setParameter("email", email); // Establece el valor del parámetro "email" en la consulta Hibernate

            Long count = query.uniqueResult(); // Obtiene el resultado único como Long
            exists = count != null && count > 0; // Verifica si el recuento es mayor que 0
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }

        return exists; // Devuelve true si el correo electrónico ya existe en la base de datos, false en caso contrario
    }

    /**
     * Asigna un rol a un usuario existente en la base de datos.
     *
     * @param userId El ID del usuario al que se le asignará el rol.
     * @param roleId El ID del rol que se asignará al usuario.
     */
    @Override
    public void assignUserRole(Long userId, Long roleId) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        Transaction transaction = null; // Inicializa una transacción

        try {
            transaction = session.beginTransaction(); // Inicia la transacción

            User user = session.get(User.class, userId); // Obtiene el usuario por su ID
            Role role = session.get(Role.class, roleId); // Obtiene el rol por su ID

            if (user != null && role != null) {
                user.setRole(role); // Asigna el rol al usuario
                session.update(user); // Actualiza el usuario en la base de datos
            } else {
                // Manejar el caso en el que el usuario o el rol no existen
            }

            transaction.commit(); // Confirma la transacción
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Si ocurre un error, revierte la transacción
            }
            e.printStackTrace();
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }
    }

    /**
     * Busca usuarios en función de un criterio de búsqueda proporcionado. El criterio se compara con varios campos
     * de la entidad User, incluyendo nombre, apellido, DNI, correo electrónico y nombre del rol.
     *
     * @param criteria El criterio de búsqueda a aplicar.
     * @return Una lista de UserDTO que coinciden con el criterio de búsqueda.
     */
    @Override
    public List<UserDTO> searchUsersBySingleCriteria(String criteria) {
        Session session = sessionFactory.openSession(); // Abre una nueva sesión de Hibernate
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder(); // Obtiene el generador de criterios
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class); // Crea una consulta de criterios para la entidad User
            Root<User> root = criteriaQuery.from(User.class); // Establece la raíz de la consulta en la entidad User

            // Crea un predicado de búsqueda que compara el criterio con varios campos de la entidad User
            Predicate searchPredicate = criteriaBuilder.or(
                    criteriaBuilder.like(root.get("userName"), "%" + criteria + "%"), // Nombre del usuario
                    criteriaBuilder.like(root.get("userLastName"), "%" + criteria + "%"), // Apellido del usuario
                    criteriaBuilder.like(root.get("userDni"), "%" + criteria + "%"), // DNI del usuario
                    criteriaBuilder.like(root.get("userEmail"), "%" + criteria + "%"), // Correo electrónico del usuario
                    criteriaBuilder.like(root.join("role").get("roleName"), "%" + criteria + "%") // Nombre del rol del usuario
            );

            criteriaQuery.where(searchPredicate); // Establece el predicado de búsqueda en la consulta
            Query<User> query = session.createQuery(criteriaQuery); // Crea una consulta Hibernate
            List<User> users = query.getResultList(); // Ejecuta la consulta y obtiene los resultados

            return UserMapper.toUserDTOList(users); // Convierte los resultados a DTO y los devuelve
        } finally {
            session.close(); // Cierra la sesión de Hibernate
        }
    }


}









