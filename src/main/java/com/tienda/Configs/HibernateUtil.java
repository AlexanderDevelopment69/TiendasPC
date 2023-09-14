package com.tienda.Configs;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    // Crear una única instancia compartida de la fábrica de sesiones (SessionFactory)
    private static final SessionFactory sessionFactory = buildSessionFactory();

    /**
     * Método privado para construir y configurar la fábrica de sesiones de Hibernate.
     *
     * @return La fábrica de sesiones configurada.
     */
    private static SessionFactory buildSessionFactory() {
        try {
            // Cargar la configuración de Hibernate desde hibernate.cfg.xml
            Configuration configuration = new Configuration().configure();

            // Construir y devolver la fábrica de sesiones basada en la configuración
            return configuration.buildSessionFactory();
        } catch (Throwable ex) {
            // Manejar errores durante la inicialización de la fábrica de sesiones
            System.err.println("Initial SessionFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Método para obtener la fábrica de sesiones de Hibernate.
     *
     * @return La fábrica de sesiones de Hibernate.
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Abre una nueva sesión de Hibernate.
     *
     * @return Una nueva sesión de Hibernate.
     */
    public static Session openSession() {
        return sessionFactory.openSession();
    }

    /**
     * Cierra una sesión de Hibernate.
     *
     * @param session La sesión de Hibernate que se debe cerrar.
     */
    public static void closeSession(Session session) {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }

    /**
     * Cierra la fábrica de sesiones de Hibernate.
     */
    public static void closeSessionFactory() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
