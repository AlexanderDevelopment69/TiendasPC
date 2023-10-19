//package com.tienda.Dao.Impl;
//
//import com.tienda.dto.UserDTO;
//import org.hibernate.Session;
//import org.hibernate.Transaction;
//
//public class ejemplo {
//        /**
//     * Agrega un nuevo usuario a la base de datos.
//     *
//     * @param userDTO El DTO del usuario que se va a agregar.
//     */
//    @Override
//    public void addUser(UserDTO userDTO) {
//        Transaction transaction = null;
//        Session session = null;
//        try {
//            // Abre una nueva sesión de Hibernate.
//            session = sessionFactory.openSession();
//            // Inicia una transacción para realizar una operación de escritura.
//            transaction = session.beginTransaction();
//
//            // Utiliza el mapeador para convertir el DTO a una entidad User.
//            User user = UserMapper.userDTOToUser(userDTO);
//
//            // Genera el hash de la contraseña utilizando jBCrypt.
//            String hashedPassword = BCrypt.hashpw(userDTO.getUserPassword(), BCrypt.gensalt());
//            // Asigna el hash de la contraseña al usuario.
//            user.setUserPassword(hashedPassword);
//
//            // Guarda el nuevo usuario en la base de datos.
//            session.save(user);
//            // Confirma la transacción para aplicar los cambios a la base de datos.
//            transaction.commit();
//        } catch (Exception e) {
//            // En caso de error, revierte la transacción.
//            if (transaction != null) {
//                transaction.rollback();
//            }
//            e.printStackTrace();
//        } finally {
//            // Cierra la sesión de Hibernate en un bloque finally para asegurar su cierre.
//            if (session != null && session.isOpen()) {
//                session.close();
//            }
//        }
//    }
//}
