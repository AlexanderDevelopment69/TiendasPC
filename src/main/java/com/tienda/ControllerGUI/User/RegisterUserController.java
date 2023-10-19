package com.tienda.ControllerGUI.User;

import com.tienda.Configs.HibernateUtil;
import com.tienda.ControllerGUI.Components.ModalDialog;
import com.tienda.ControllerGUI.ViewsManager.ViewsManager;
import com.tienda.Dao.UserDAO;
import com.tienda.DaoImpl.UserDAOHibernate;

import com.tienda.Tools.MFXTextFieldValidator;
import com.tienda.dto.UserDTO;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import org.hibernate.HibernateException;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterUserController implements Initializable {


    @FXML
    private StackPane root;

    @FXML
    private MFXTextField textDni;

    @FXML
    private MFXTextField textNames;

    @FXML
    private MFXTextField textLastNames;

    @FXML
    private MFXTextField textEmail;

    @FXML
    private MFXPasswordField textPassword;

    @FXML
    private MFXButton btnRegister;

    @FXML
    private MFXButton btnBack;

    private ViewsManager viewsManager;

    private UserDAO userDAO;


    MFXTextFieldValidator passwordValidator;
    MFXTextFieldValidator emailValidator;
    MFXTextFieldValidator dniValidator;
    MFXTextFieldValidator lastNamesValidator;
    MFXTextFieldValidator namesValidator;


    @FXML
    void handleBack(ActionEvent event) {
        viewsManager.showLogin();

    }


    @FXML
    void handleRegister(ActionEvent event) {

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

        // Verificar si el usuario existe en la base de datos
        Boolean isEmailExists = userDAO.isEmailExists(email);
        Boolean isDniExists = userDAO.isDniExists(dni);


        if (isEmailExists) {
            errorMessages.append("Usuario ya registrado con este correo.\n");
            MFXTextFieldValidator.emailExists(textEmail, true);
        }

        if (isDniExists) {
            errorMessages.append("Usuario ya registrado con este DNI.\n");
            MFXTextFieldValidator.dniExists(textDni, true);
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
                    "Registro incorrecto.",
                    "No se pudo registrar correctamente, verfifica todos los campos: \n" + "\n" + errorMessages,
                    "OK",
                    e -> {
                        // Lógica cuando se hace clic en el botón "Confirmar"
                        System.out.println("Se hizo clic en Confirmar");
                        modalDialog.close();

                    }

            );

            modalDialog.showModal(root);


        } else {

            // Si no hay errores, proceder con el registro
            // Crear un objeto UserDTO con los datos ingresados
            UserDTO newUser = new UserDTO();
            newUser.setUserDni(dni);
            newUser.setUserPassword(password);
            newUser.setUserLastName(lastNames);
            newUser.setUserName(names);
            newUser.setUserEmail(email);
            try {
                // Guardar el nuevo usuario en la base de datos
                userDAO.saveUser(newUser);

                // Limpiar los campos después del registro
                textDni.clear();
                textEmail.clear();
                textNames.clear();
                textLastNames.clear();
                textPassword.clear();


                //Crear una instancia del modal
                ModalDialog modalDialog = new ModalDialog();
                // Configurar el modal mediante un solo método
                modalDialog.configureModal(
                        new Image("Images/iconCheck.png"),
                        "Registrado correctamente.",
                        "Registrado correctamente, ahora puedes ingresar al sistema.",
                        "Continuar",
                        "Cancelar",
                        e -> {
                            // Lógica cuando se hace clic en el botón "Confirmar"
                            System.out.println("Se hizo clic en Confirmar");
                            viewsManager.showLogin();
                        },
                        e -> {
                            // Lógica cuando se hace clic en el botón "Cancelar"
                            System.out.println("Se hizo clic en Cancelar");
                            modalDialog.close(); // Cierra el modal
                        }
                );

                modalDialog.showModal(root);


                // Mostrar un mensaje de éxito
                System.out.println("Usuario registrado con éxito.");
            } catch (HibernateException e) {
                // Manejar errores de acceso a la base de datos
                System.out.println("Error al registrar el usuario.");
            }


        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicializar el objeto UserDaoHibernate con la fábrica de sesiones de Hibernate
        userDAO = new UserDAOHibernate(HibernateUtil.getSessionFactory());

        // Inicializa ViewsManager con el StackPane principal
        viewsManager = new ViewsManager(root);


        // TextFieldValidator passwordValidator = new TextFieldValidator(textPassword, passwordValidationLabel, TextFieldValidator.ValidationCriteria.PASSWORD);
//        TextFieldValidator emailValidator = new TextFieldValidator(textEmail, emailValidationLabel, TextFieldValidator.ValidationCriteria.EMAIL);
        passwordValidator = new MFXTextFieldValidator(textPassword, MFXTextFieldValidator.ValidationCriteria.PASSWORD);
        emailValidator = new MFXTextFieldValidator(textEmail, MFXTextFieldValidator.ValidationCriteria.EMAIL);
        dniValidator = new MFXTextFieldValidator(textDni, MFXTextFieldValidator.ValidationCriteria.DNI);
        lastNamesValidator = new MFXTextFieldValidator(textLastNames, MFXTextFieldValidator.ValidationCriteria.LASTNAMES);
        namesValidator = new MFXTextFieldValidator(textNames, MFXTextFieldValidator.ValidationCriteria.NAMES);


    }


}
