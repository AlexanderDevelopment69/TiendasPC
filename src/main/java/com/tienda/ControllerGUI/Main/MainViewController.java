package com.tienda.ControllerGUI.Main;

import com.tienda.Configs.HibernateUtil;
import com.tienda.ControllerGUI.Components.ModalDialog;
import com.tienda.ControllerGUI.User.UserSession;
import com.tienda.Dao.UserDAO;
import com.tienda.DaoImpl.UserDAOHibernate;
import com.tienda.Tools.MFXTextFieldValidator;
import com.tienda.dto.UserDTO;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.hibernate.HibernateException;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML
    private AnchorPane centerAnchorPane;

    @FXML
    private StackPane root;

    @FXML
    private MFXTextField textDni;

    @FXML
    private MFXTextField textEmail;

    @FXML
    private MFXTextField textLastNames;

    @FXML
    private MFXTextField textNames;

    @FXML
    private MFXPasswordField textPassword;
    private UserDAO userDAO;

    MFXTextFieldValidator passwordValidator;
    MFXTextFieldValidator emailValidator;
    MFXTextFieldValidator dniValidator;
    MFXTextFieldValidator lastNamesValidator;
    MFXTextFieldValidator namesValidator;

    //Gestion del usuario - Variables donde almacenar los datos de la session del usuario
    private Long userId;
    private String name;
    private String lastName;
    private String email;
    private String dni;
    private String roleName;

    private Long roleId;


    @FXML
    void handleCancel(ActionEvent event) {

    }

    @FXML
    void handleUpdate(ActionEvent event) {

        // Obtener datos de los campos
        String email = textEmail.getText();
        String password = textPassword.getText();
        String dni = textDni.getText();
        String lastNames = textLastNames.getText();
        String names = textNames.getText();


        // Crear una cadena para almacenar mensajes de error
        StringBuilder errorMessages = new StringBuilder();

        // Validar campos vacíos
        if (email.isEmpty() || password.isEmpty() || dni.isEmpty() || lastNames.isEmpty() || names.isEmpty()) {
            System.out.println("Por favor, complete todos los campos.\n");
            return;
        }

        // Validar contraseña
        if (!passwordValidator.validatePassword(password)) {
            errorMessages.append("La contraseña no cumple con los requisitos.\n");
        }

        // Validar DNI
        if (!dniValidator.validateDni(dni)) {
            errorMessages.append("El DNI no es válido.\n");
        }

        // Validar correo electrónico
        if (!emailValidator.validateEmail(email)) {
            errorMessages.append("El correo electrónico no es válido.\n");
        }

        // Validar apellidos
        if (!lastNamesValidator.validateCharactersOnly(lastNames)) {
            errorMessages.append("Los caracteres para el apellido no están permitidos.\n");
        }

        // Validar nombres
        if (!namesValidator.validateCharactersOnly(names)) {
            errorMessages.append("Los caracteres para el nombre no están permitidos.\n");
        }

        // Mostrar todos los mensajes de error acumulados
        if (errorMessages.length() > 0) {
            System.out.println("Errores de validación:");
            System.out.println(errorMessages);


            //Crear una instancia del modal
            ModalDialog modalDialog = new ModalDialog();
            // Configurar el modal mediante un solo método
            modalDialog.configureModal(
                    new Image("Images/alert.png"),
                    "Actualizacion incorrecta.",
                    "No se pudo actualizar correctamente, verfifica todos los campos: \n" + "\n" + errorMessages,
                    "OK",
                    e -> {
                        // Lógica cuando se hace clic en el botón "Confirmar"
                        System.out.println("Se hizo clic en Confirmar");
                        modalDialog.close();

                    }

            );

            modalDialog.showModal(root);


        } else {

            UserDTO updateUser= new UserDTO();
            updateUser.setUserId(userId);
            updateUser.setUserDni(textDni.getText().trim());
            updateUser.setUserName(textNames.getText().trim());
            updateUser.setUserLastName(textLastNames.getText().trim());
            updateUser.setUserEmail(textEmail.getText().trim());
            updateUser.setUserPassword(textPassword.getText().trim());
            updateUser.setRoleId(roleId);


            try {
                // Actualiza el usuario en la base de datos
                userDAO.updateUser(updateUser);

                // Ahora, puedes obtener los detalles del usuario autenticado desde la base de datos.
                UserDTO authenticatedUser = userDAO.getUserByDni(dni);
                // Crear y gestionar la sesión de usuario
                UserSession session = UserSession.getInstance();
                session.setUser(authenticatedUser.getUserId(),authenticatedUser.getUserName(), authenticatedUser.getUserLastName(), authenticatedUser.getUserEmail(),authenticatedUser.getUserDni(),authenticatedUser.getRoleId(), authenticatedUser.getRoleName());

                textDni.setText(authenticatedUser.getUserDni());
                textEmail.setText(authenticatedUser.getUserEmail());
                textLastNames.setText(authenticatedUser.getUserLastName());
                textNames.setText(authenticatedUser.getUserName());



                //Crear una instancia del modal
                ModalDialog modalDialog = new ModalDialog();
                // Configurar el modal mediante un solo método
                modalDialog.configureModal(
                        new Image("Images/iconCheck.png"),
                        "Actualizado correctamente.",
                        "Datos actualizados correctamente.",
                        "Continuar",
                        "Cancelar",
                        e -> {
                            // Lógica cuando se hace clic en el botón "Confirmar"
                            modalDialog.close();
                            System.out.println("Se hizo clic en Confirmar");
                        },
                        e -> {
                            // Lógica cuando se hace clic en el botón "Cancelar"
                            System.out.println("Se hizo clic en Cancelar");
                            modalDialog.close(); // Cierra el modal
                        }
                );

                modalDialog.showModal(root);


                // Mostrar un mensaje de éxito
                System.out.println("Usuario actualizado con éxito.");
            } catch (HibernateException e) {
                // Manejar errores de acceso a la base de datos
                System.out.println("Error al actualizar al usuario.");
            }


        }








    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicializar el objeto UserDaoHibernate con la fábrica de sesiones de Hibernate
        userDAO = new UserDAOHibernate(HibernateUtil.getSessionFactory());

        passwordValidator = new MFXTextFieldValidator(textPassword, MFXTextFieldValidator.ValidationCriteria.PASSWORD);
        emailValidator = new MFXTextFieldValidator(textEmail, MFXTextFieldValidator.ValidationCriteria.EMAIL);
        dniValidator = new MFXTextFieldValidator(textDni, MFXTextFieldValidator.ValidationCriteria.DNI);
        lastNamesValidator = new MFXTextFieldValidator(textLastNames, MFXTextFieldValidator.ValidationCriteria.LASTNAMES);
        namesValidator = new MFXTextFieldValidator(textNames, MFXTextFieldValidator.ValidationCriteria.NAMES);


        UserSession session = UserSession.getInstance();
        if (session.isLoggedIn()) {
            // Utiliza esta información para personalizar la interfaz o tomar decisiones basadas en el usuario actual.
            userId = session.getUserId();
            name = session.getName();
            lastName=session.getLastName();
            email = session.getEmail();
            dni = session.getDni();
            roleName = session.getRoleName();
            roleId=session.getRoleId();

            textDni.setText(dni);
            textEmail.setText(email);
            textLastNames.setText(lastName);
            textNames.setText(name);


            System.out.println("Usuario Iniciado");
            System.out.println("ID: " + userId);
            System.out.println("EMAIL: " + email);
            System.out.println("DNI: " + dni);
            System.out.println("NAME: "+name);
            System.out.println("LASTNAME: "+lastName);

        }
    }
}
