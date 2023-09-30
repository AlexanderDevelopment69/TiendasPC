package com.tienda.Tools;

import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MFXTextFieldValidator {
    private MFXTextField textField;
    private Label validationLabel;
    private ValidationCriteria validationCriteria;

    public enum ValidationCriteria {
        EMAIL,
        PASSWORD,
        DNI,
        CHARACTERS_ONLY,
        NAMES,
        LASTNAMES,
        POSITIVE_DECIMAL_NUMBERS,
        CUSTOM // Puedes agregar otros criterios personalizados según tus necesidades
    }


    private static final String VALID_COLOR = "#21A366";
    private static final String INVALID_COLOR = "#E43D4B";


    public MFXTextFieldValidator(TextField textField, ValidationCriteria validationCriteria) {
        this.textField = (MFXTextField) textField;
        this.validationCriteria = validationCriteria;
        setupValidation();
    }

    private void setupValidation() {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                switch (validationCriteria) {
                    case EMAIL:
                        if (newValue.isEmpty()) {
                            textField.setFloatingText("Email");
                            textField.setStyle(" -mfx-main: #5771f7;");
                            return;
                        }
                        validateEmail(newValue);
                        break;
                    case PASSWORD:
                        if (newValue.isEmpty()) {
                            textField.setFloatingText("Password");
                            textField.setStyle(" -mfx-main: #5771f7;");
                            return;
                        }
                        validatePassword(newValue);
                        break;
                    case DNI:
                        if (newValue.isEmpty()) {
                            textField.setFloatingText("Dni");
                            textField.setStyle(" -mfx-main: #5771f7;");
                            return;
                        }
                        validateDni(newValue);
                        break;
                    case CHARACTERS_ONLY:
                        validateCharactersOnly(newValue);
                        break;
                    case NAMES:
                        if (newValue.isEmpty()) {
                            textField.setFloatingText("Nombres");
                            textField.setStyle(" -mfx-main: #5771f7;");
                            return;
                        }
                        validateCharactersOnly(newValue);
                        break;
                    case LASTNAMES:
                        if (newValue.isEmpty()) {
                            textField.setFloatingText("Apellidos");
                            textField.setStyle(" -mfx-main: #5771f7;");
                            return;
                        }
                        validateCharactersOnly(newValue);
                        break;
                    case  POSITIVE_DECIMAL_NUMBERS:
                        validatePositiveDecimalNumbers(newValue);
                        break;

                    case CUSTOM:
                        // Implementa tu lógica de validación personalizada aquí
                        break;
                    default:
                        break;
                }
            }
        });
    }


    public Boolean validateEmail(String email) {
        boolean isValid = isValidEmail(email);
        if (isValid) {
            textField.setFloatingText("Correo electrónico válido");
            textField.setStyle("-mfx-main: " + VALID_COLOR + ";");
            textField.setStyle("-fx-border-color: " + VALID_COLOR + ";");

        } else {
            textField.setFloatingText("Correo electrónico no válido");
            textField.setStyle("-mfx-main: " + INVALID_COLOR + ";");
            textField.setStyle("-fx-border-color: " + INVALID_COLOR + ";");
        }
        return isValid; // Devuelve true si el correo electrónico es válido, false en caso contrario
    }

    public Boolean validatePassword(String password) {
        boolean hasSpecialChar = false;
        boolean hasUppercaseLetter = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (!hasSpecialChar && !Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
                continue;
            }
            if (!hasUppercaseLetter && Character.isUpperCase(c)) {
                hasUppercaseLetter = true;
                continue;
            }
            if (!hasDigit && Character.isDigit(c)) {
                hasDigit = true;
            }
        }

        if (password.length() >= 10 && hasSpecialChar && hasUppercaseLetter && hasDigit) {
            textField.setFloatingText("Contraseña válida");
            textField.setStyle("-mfx-main: " + VALID_COLOR + ";");
            textField.setStyle("-fx-border-color: " + VALID_COLOR + ";");

        } else {
            if (password.length() < 10) {
                textField.setFloatingText("La contraseña debe tener al menos 10 caracteres");
            } else if (!hasSpecialChar) {
                textField.setFloatingText("Debería tener al menos un carácter especial");
            } else if (!hasUppercaseLetter) {
                textField.setFloatingText("Debería tener al menos una letra mayúscula");
            } else if (!hasDigit) {
                textField.setFloatingText("Debería tener al menos un número");
            }

            textField.setStyle("-mfx-main: " + INVALID_COLOR + ";");
            textField.setStyle("-fx-border-color: " + INVALID_COLOR + ";");
        }
        return password.length() >= 10 && hasSpecialChar && hasUppercaseLetter && hasDigit;
    }

    public Boolean validateDni(String dni) {
        boolean isValid = isValidDni(dni);
        if (isValid) {
            textField.setFloatingText("DNI válido");
            textField.setStyle("-mfx-main: " + VALID_COLOR + ";");
            textField.setStyle("-fx-border-color: " + VALID_COLOR + ";");
        } else {
            textField.setFloatingText("DNI no válido");
            textField.setStyle("-mfx-main: " + INVALID_COLOR + ";");
            textField.setStyle("-fx-border-color: " + INVALID_COLOR + ";");

        }
        return isValid; // Devuelve true si el DNI es válido, false en caso contrario
    }

    public Boolean validateCharactersOnly(String input) {
        boolean isValid = isValidCharactersOnly(input);
        if (isValid) {
//            textField.setFloatingText("Caracteres válidos");
            textField.setStyle("-mfx-main: " + VALID_COLOR + ";");
            textField.setStyle("-fx-border-color: " + VALID_COLOR + ";");
        } else {
//            textField.setFloatingText("No se permiten números ni signos");
            textField.setStyle("-mfx-main: " + INVALID_COLOR + ";");
            textField.setStyle("-fx-border-color: " + INVALID_COLOR + ";");
        }
        return isValid; // Devuelve true si solo contiene caracteres válidos, false en caso contrario
    }

    public static void handleLogin(MFXPasswordField textField, boolean isAuthenticated) {
        if (!isAuthenticated) {
            textField.setFloatingText("Credenciales incorrectas");
            textField.setStyle("-mfx-main: " + INVALID_COLOR + ";");
            textField.setStyle("-fx-border-color: " + INVALID_COLOR + ";");
        }
    }

    public static void emailExists(MFXTextField textField, boolean isExists) {
        if (isExists) {
            textField.setFloatingText("Email ya registrado");
            textField.setStyle("-mfx-main: " + INVALID_COLOR + ";");
            textField.setStyle("-fx-border-color: " + INVALID_COLOR + ";");
        }
    }
    public static void dniExists(MFXTextField textField, boolean isExists) {
        if (isExists) {
            textField.setFloatingText("DNI ya registrado");
            textField.setStyle("-mfx-main: " + INVALID_COLOR + ";");
            textField.setStyle("-fx-border-color: " + INVALID_COLOR + ";");
        }
    }



    private boolean isValidEmail(String email) {
        // Implementa tu lógica de validación de correo electrónico aquí
        // En este ejemplo, se utiliza una expresión regular simple para verificar el formato básico
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private boolean isValidDni(String dni) {
        // El DNI debe tener exactamente 8 dígitos
        return dni.matches("^[0-9]{8}$");
    }

    private boolean isValidCharactersOnly(String input) {
        return input.matches("^[a-zA-Z ]*$");
    }

    public Boolean validatePositiveDecimalNumbers(String input) {
        if (input.isEmpty()) {
            textField.setFloatingText("Precio");
            textField.setStyle("-mfx-main: #5771f7;");
            return false; // Empty input is not valid
        }

        try {
            double number = Double.parseDouble(input);
            if (number > 0) {
                textField.setFloatingText("Positive Decimal Number");
                textField.setStyle("-mfx-main: " + VALID_COLOR + ";");
                textField.setStyle("-fx-border-color: " + VALID_COLOR + ";");
                return true;
            } else {
                textField.setFloatingText("Not a positive decimal number");
            }
        } catch (NumberFormatException e) {
            // Not a valid decimal number
        }

        textField.setStyle("-mfx-main: " + INVALID_COLOR + ";");
        textField.setStyle("-fx-border-color: " + INVALID_COLOR + ";");
        return false;
    }





}
