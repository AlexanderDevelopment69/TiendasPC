//package com.tienda.ControllerGUI.User;
//
//import com.tienda.DAO.UserDao;
//import javafx.concurrent.Task;
//import java.util.function.Consumer;
//
//public class LoginManager {
//
//    private boolean loginInProgress = false; // Indica si hay un inicio de sesión en curso
//    private UserDao userDAO; // Instancia de UserDAO, se debe configurar en el constructor
//
//    /**
//     * Constructor de LoginManager.
//     *
//     * @param userDAO Instancia de UserDAO utilizada para autenticar a los usuarios.
//     */
//    public LoginManager(UserDao userDAO) {
//        this.userDAO = userDAO; // Inicializa UserDAO con la instancia proporcionada
//    }
//
//    /**
//     * Inicia el proceso de inicio de sesión.
//     *
//     * @param username           Nombre de usuario.
//     * @param password           Contraseña del usuario.
//     * @param loginResultHandler Manejador que se llama con el resultado del inicio de sesión.
//     */
//
//    // Método para iniciar el proceso de inicio de sesión
//    public void handleLogin(String username, String password, Consumer<Boolean> loginResultHandler) {
//        if (loginInProgress) {
//            System.out.println("Un proceso de inicio de sesión ya está en curso.");
//            return; // Evita iniciar otro inicio de sesión si ya hay uno en curso
//        }
//
//        loginInProgress = true; // Marca que hay un inicio de sesión en curso
//
//        // Crea una tarea (Task) para realizar la autenticación en un hilo separado
//        Task<Boolean> loginTask = createLoginTask(username, password);
//
//        // Configura acciones a realizar cuando la tarea se completa con éxito
//        loginTask.setOnSucceeded(e -> {
//            handleLoginResult(loginTask.getValue(), loginResultHandler);
//            loginInProgress = false; // Marca que el inicio de sesión ha terminado
//        });
//
//        // Configura acciones a realizar cuando la tarea falla
//        loginTask.setOnFailed(e -> {
//            System.out.println("Error de inicio de sesión: Contraseña incorrecta.");
//            loginInProgress = false; // Marca que el inicio de sesión ha terminado debido a un error
//        });
//
//        // Crea y inicia un nuevo hilo para ejecutar la tarea de inicio de sesión
//        Thread thread = new Thread(loginTask);
//        thread.setDaemon(true);
//        thread.start();
//    }
//
//
//    /**
//     * Crea una tarea para realizar el proceso de inicio de sesión en un hilo separado.
//     *
//     * @param username Nombre de usuario.
//     * @param password Contraseña del usuario.
//     * @return Tarea que realiza la autenticación.
//     */
//
//    // Método para crear la tarea de inicio de sesión
//    private Task<Boolean> createLoginTask(String username, String password) {
//        return new Task<>() {
//            @Override
//            protected Boolean call() throws Exception {
//                // Llama a UserDAO para autenticar al usuario con las credenciales proporcionadas
//                return userDAO.authenticateUser(username, password);
//            }
//        };
//    }
//
//    /**
//     * Maneja el resultado del inicio de sesión y llama al manejador externo con el resultado.
//     *
//     * @param isAuthenticated    Indica si el usuario se autenticó con éxito.
//     * @param loginResultHandler Manejador para el resultado del inicio de sesión.
//     */
//
//    // Método para manejar el resultado del inicio de sesión
//    private void handleLoginResult(boolean isAuthenticated, Consumer<Boolean> loginResultHandler) {
//        if (isAuthenticated) {
//            System.out.println("Contraseña correcta");
//        } else {
//            System.out.println("Credenciales incorrectas");
//        }
//
//        // Invoca el manejador externo con el resultado del inicio de sesión
//        loginResultHandler.accept(isAuthenticated);
//    }
//}
