package com.tienda.ControllerGUI.User;

import com.tienda.Configs.HibernateUtil;
import com.tienda.ControllerGUI.ViewsManager.ViewsManager;
import com.tienda.DAO.UserDao;
import com.tienda.DAO.UserDaoHibernate;
import com.tienda.Model.Role;
import com.tienda.Model.User;
import com.tienda.Tools.MFXTextFieldValidator;
import com.tienda.Tools.TextFieldValidator;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;


public class LoginController implements Initializable  {
    @FXML
    private StackPane root;
    @FXML
    private Pane pane;

    @FXML
    private MFXTextField textEmail;

    @FXML
    private MFXPasswordField textPassword;

    @FXML
    private Label passwordValidationLabel;

    @FXML
    private Label emailValidationLabel;


    @FXML
    private MFXButton btnRegister;
    @FXML
    private MFXButton btnLogin;

    private UserDao userDAO;

    private ViewsManager viewsManager;

    /**
     * Variable que indica si un proceso de inicio de sesión está en curso.
     * Esta variable se utiliza para evitar múltiples intentos de inicio de sesión
     * simultáneos al rastrear si ya hay un proceso de inicio de sesión en marcha
     * o no.
     */
    private boolean loginInProgress = false;

    @FXML
    void handleExit(ActionEvent event) {
        Platform.exit();
    }


    @FXML
    void handleLogin(ActionEvent event) {

        if (loginInProgress==true) {
            // Si ya hay un proceso de inicio de sesión en curso, no hagas nada
            return;
        }

        loginInProgress = true; // Marcar que un inicio de sesión está en progreso

        String username = textEmail.getText(); // Obtiene el correo electrónico desde la UI
        String password = textPassword.getText();// Obtiene la contraseña desde la UI


        if (username.isEmpty() || password.isEmpty()) {
            // Mostrar una alerta indicando que se deben completar ambos campos
            System.out.println("Error de inicio de sesión\", \"Por favor, complete todos los campos");
            return; // Salir del método
        }

        Task<Boolean> loginTask = new Task<>() {

            @Override
            protected Boolean call() throws Exception {
                // Realiza la autenticación (consulta a la base de datos u otro método) aquí
                Boolean isAuthenticated = userDAO.authenticateUser(username, password);
                return isAuthenticated;
            }
        };


        loginTask.setOnSucceeded(e -> {
            boolean isAuthenticated = loginTask.getValue();
            if (isAuthenticated) {

                // Ahora, puedes obtener los detalles del usuario autenticado desde la base de datos.
                User authenticatedUser = userDAO.getUserByEmail(username);

                // Crear y gestionar la sesión de usuario
                UserSession session = UserSession.getInstance();
                session.setUser(authenticatedUser.getId(), authenticatedUser.getName(), session.getLastName(), authenticatedUser.getEmail(), authenticatedUser.getRole().getRoleName());




                // Autenticación exitosa, puedes navegar a la siguiente vista o realizar acciones necesarias
                System.out.println("Password correcto");
                viewsManager.showMain();
            }
            else {
                // Autenticación fallida, muestra un mensaje de error al usuario
                System.out.println("Credenciales incorrectas");
                // Llama a handleLogin con el resultado de la autenticación
                    MFXTextFieldValidator.handleLogin(textPassword, false);
            }
            loginInProgress = false; // Restablecer el estado a "no en progreso"
        });

        loginTask.setOnFailed(e -> {
            // Manejo de errores, muestra un mensaje de error al usuario
            System.out.println("Error de inicio de sesion");
            loginInProgress = false; // Restablecer el estado a "no en progreso"
        });

        // Inicia el Task en un nuevo hilo
        Thread thread = new Thread(loginTask);
        thread.setDaemon(true);
        thread.start();


//        // Obtener el nombre de usuario y la contraseña ingresados por el usuario
//        String username = textEmail.getText();
//        String password = textPassword.getText();
//
//        // Validar que se ingresaron datos de usuario y contraseña
//        if (username.isEmpty() || password.isEmpty()) {
//            // Mostrar una alerta indicando que se deben completar ambos campos
//            System.out.println("Error de inicio de sesión\", \"Por favor, complete todos los campos");
//            return; // Salir del método
//        }

//        // Verificar si el usuario existe en la base de datos
//        User user = userDAO.getUserByUsernameAsync(username);
//
//        if (user == null) {
//            // Si el usuario no existe, mostrar un mensaje de error
//            System.out.println("Error de inicio de sesión\", \"Usuario no encontrado");
//        } else {
//
//
//            // Si el usuario existe, verificar la contraseña
//            if (password.equals(user.getPassword())) {
//                // La contraseña es correcta, aquí puedes redirigir a la siguiente vista
//                // Por ejemplo, puedes abrir una nueva ventana o cargar una nueva escena
//                // Aquí puedes agregar tu lógica para cambiar la vista
//
//                // Ejemplo: Cerrar la ventana actual
////                loginButton.getScene().getWindow().hide();
//
//                // Ejemplo: Abrir una nueva ventana o cargar una nueva escena
//                // ...
//
//                System.out.println("Password correcto");
//            } else {
//                // La contraseña es incorrecta, mostrar un mensaje de error
//                System.out.println("Error de inicio de sesion\", \" Password incorrecta.");
//            }
//        }

    }


    @FXML
    void handleRegister(ActionEvent event) {
        viewsManager.showRegisterUser();

    }


    // Este método se llama después de cargar la vista y se utiliza para inicializar la clase
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicializar el objeto UserDaoHibernate con la fábrica de sesiones de Hibernate
        userDAO = new UserDaoHibernate(HibernateUtil.getSessionFactory());

        // Inicializa ViewsManager con el StackPane principal
        viewsManager = new ViewsManager(root);




    }

}
