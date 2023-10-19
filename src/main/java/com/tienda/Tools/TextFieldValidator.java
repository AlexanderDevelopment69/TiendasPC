package com.tienda.Tools;

import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TextFieldValidator {
    private TextField textField;
    private Label validationLabel;
    private ValidationCriteria validationCriteria;


    public enum ValidationCriteria {
        EMAIL,
        PASSWORD,
        DNI,
        CHARACTERS_ONLY,
        PRICE,
        COST,
        QUANTITY,

        DISCOUNT,
        RUC,
        PHONE_NUMBER,
        FIRST_NAME,
        LAST_NAME,
        CUSTOMER_EXISTS,
        CUSTOM // Puedes agregar otros criterios personalizados según tus necesidades


    }


    private static final double LABEL_FONT_SIZE = 11;
    // Variables para configurar los colores del Label

    private static final Color VALID_COLOR = Color.GREEN;
    private static final Color INVALID_COLOR = Color.web("#E43D4B");


    public TextFieldValidator(TextField textField, Label validationLabel, ValidationCriteria validationCriteria) {
        this.textField = textField;
        this.validationLabel = validationLabel;
        this.validationCriteria = validationCriteria;

        // Configurar el tamaño de fuente y negrita del Label
        Font boldFont = Font.font("System", FontWeight.BOLD, LABEL_FONT_SIZE);
        validationLabel.setFont(boldFont);

        setupValidation();
    }


    private void setupValidation() {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.isEmpty()) {
                    validationLabel.setText(""); // Si estdewdá vacío, no muestra ningún mensaje
                    return;
                }

                switch (validationCriteria) {
                    case EMAIL:
                        validateEmail(newValue);
                        break;
                    case PASSWORD:
                        validatePassword(newValue);
                        break;
                    case DNI:
                        validateDni(newValue);
                        break;
                    case CHARACTERS_ONLY:
                        validateCharactersOnly(newValue);
                        break;
                    case PRICE:
                        validatePrice(newValue);
                        break;
                    case RUC:
                        validateRUC(newValue);
                        break;
                    case PHONE_NUMBER:
                        validatePhoneNumber(newValue);
                        break;
                    case FIRST_NAME:
                        validateFirstName(newValue);
                        break;
                    case LAST_NAME:
                        validateLastName(newValue);
                    break;
                    case COST:
                        validateCost(newValue);
                        break;
                    case QUANTITY:
                        validateQuantity(newValue);
                        break;
                    case DISCOUNT:
                        validateDiscount(newValue);
                        break;
                    case CUSTOMER_EXISTS:
                        validateCustomerExists(newValue);
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
        if (isValidEmail(email)) {
            validationLabel.setText("Correo electrónico válido");
            validationLabel.setTextFill(VALID_COLOR);
        } else {
            validationLabel.setText("Correo electrónico no válido");
            validationLabel.setTextFill(INVALID_COLOR);
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
            validationLabel.setText("Contraseña válida");
            validationLabel.setTextFill(VALID_COLOR);
        } else {
            if (password.length() < 10) {
                validationLabel.setText("La contraseña debe tener al menos 10 caracteres");
            } else if (!hasSpecialChar) {
                validationLabel.setText("Debería tener al menos un carácter especial");
            } else if (!hasUppercaseLetter) {
                validationLabel.setText("Debería tener al menos una letra mayúscula");
            } else if (!hasDigit) {
                validationLabel.setText("Debería tener al menos un número");
            }
            validationLabel.setTextFill(INVALID_COLOR);
        }
        return password.length() >= 10 && hasSpecialChar && hasUppercaseLetter && hasDigit;
    }

    public Boolean validateDni(String dni) {
        boolean isValid = isValidDni(dni);
        if (isValid) {
            validationLabel.setText("DNI válido");
            validationLabel.setTextFill(VALID_COLOR);
        } else {
            validationLabel.setText("DNI no válido");
            validationLabel.setTextFill(INVALID_COLOR);
        }
        return isValid; // Devuelve true si el DNI es válido, false en caso contrario
    }

    public Boolean validateRUC(String ruc) {
        if (ruc.length() != 11 || !ruc.matches("^[0-9]+$")) {
            validationLabel.setText("RUC no válido");
            validationLabel.setTextFill(INVALID_COLOR);
            return false;
        }

        validationLabel.setText("RUC válido");
        validationLabel.setTextFill(VALID_COLOR);
        return true;
    }

    public Boolean validateCharactersOnly(String input) {
        boolean isValid = isValidCharactersOnly(input);
        if (isValid) {
            validationLabel.setText("Caracteres válidos");
            validationLabel.setTextFill(VALID_COLOR);
        } else {
            validationLabel.setText("No se permiten números ni signos");
            validationLabel.setTextFill(INVALID_COLOR);
        }
        return isValid; // Devuelve true si solo contiene caracteres válidos, false en caso contrario
    }

    public static void handleLogin(Label validationLabel, boolean isAuthenticated) {
        if (!isAuthenticated) {
            validationLabel.setText("Contraseña incorrecta");
            validationLabel.setTextFill(INVALID_COLOR);
        }
    }


    public Boolean validatePrice(String input) {

        try {
            double price = Double.parseDouble(input);
            if (price >= 0) {
                validationLabel.setText("Precio valido");
                validationLabel.setTextFill(VALID_COLOR);
                return true;
            } else {
                validationLabel.setText("No se admiten negativos");
                validationLabel.setTextFill(INVALID_COLOR);
            }
        } catch (NumberFormatException e) {
            // No es un número decimal válido
            validationLabel.setText("No se admiten caracteres");
            validationLabel.setTextFill(INVALID_COLOR);
        }

        return false;
    }




    public Boolean validatePhoneNumber(String phoneNumber) {

        // Elimina los espacios en blanco y los guiones, si los hay
        phoneNumber = phoneNumber.replaceAll("\\s+", "").replaceAll("-", "");

        if (!phoneNumber.matches("^[0-9]+$")) {
            validationLabel.setText("No es un número de teléfono válido");
            validationLabel.setTextFill(INVALID_COLOR);
            return false;
        }

        // Verifica que el número de teléfono tenga una longitud aceptable (puedes ajustar los valores)
        if (phoneNumber.length() < 7 || phoneNumber.length() > 15) {
            validationLabel.setText("Número de teléfono no válido (longitud incorrecta)");
            validationLabel.setTextFill(INVALID_COLOR);
            return false;
        }

        validationLabel.setText("Número de teléfono válido");
        validationLabel.setTextFill(VALID_COLOR);
        return true;
    }


    public Boolean validateFirstName(String firstName) {
        boolean isValid = isValidName(firstName);
        if (isValid) {
            validationLabel.setText("Nombre válido");
            validationLabel.setTextFill(VALID_COLOR);
        } else {
            validationLabel.setText("Nombre no válido");
            validationLabel.setTextFill(INVALID_COLOR);
        }
        return isValid; // Devuelve true si el nombre es válido, false en caso contrario
    }

    public Boolean validateLastName(String lastName) {
        boolean isValid = isValidName(lastName);
        if (isValid) {
            validationLabel.setText("Apellido válido");
            validationLabel.setTextFill(VALID_COLOR);
        } else {
            validationLabel.setText("Apellido no válido");
            validationLabel.setTextFill(INVALID_COLOR);
        }
        return isValid; // Devuelve true si el apellido es válido, false en caso contrario
    }

    public Boolean validateCost(String cost) {
        try {
            double costValue = Double.parseDouble(cost);
            if (costValue > 0) {
                validationLabel.setText("Costo válido");
                validationLabel.setTextFill(VALID_COLOR);
                return true;
            }
            if (costValue == 0) {
                validationLabel.setText("No se admiten costos en cero");
                validationLabel.setTextFill(INVALID_COLOR);
            }
            else {
                validationLabel.setText("No se admiten costos negativos");
                validationLabel.setTextFill(INVALID_COLOR);
            }
        } catch (NumberFormatException e) {
            // No es un número decimal válido
            validationLabel.setText("No se admiten caracteres");
            validationLabel.setTextFill(INVALID_COLOR);
        }

        return false;
    }


    public Boolean validateQuantity(String quantity) {
        try {
            int quantityValue = Integer.parseInt(quantity);
            if (quantityValue > 0) {
                validationLabel.setText("Cantidad válida");
                validationLabel.setTextFill(VALID_COLOR);
                return true;
            }
            if (quantityValue == 0) {
                validationLabel.setText("No se admiten cantidades en cero");
                validationLabel.setTextFill(INVALID_COLOR);
            }

            else {
                validationLabel.setText("No se admiten cantidades negativas");
                validationLabel.setTextFill(INVALID_COLOR);
            }
        } catch (NumberFormatException e) {
            // No es un número entero válido
            validationLabel.setText("No se admiten caracteres");
            validationLabel.setTextFill(INVALID_COLOR);
        }

        return false;
    }

    public Boolean validateDiscount(String discount) {
        try {
            double discountValue = Double.parseDouble(discount);
            if (discountValue >= 0) {
                validationLabel.setText("Descuento válido");
                validationLabel.setTextFill(VALID_COLOR);
                return true;
            } else {
                validationLabel.setText("No se admite descuento negativo");
                validationLabel.setTextFill(INVALID_COLOR);
            }
        } catch (NumberFormatException e) {
            // No es un número decimal válido
            validationLabel.setText("No se admiten caracteres");
            validationLabel.setTextFill(INVALID_COLOR);
        }

        return false;
    }

    public Boolean validateCustomerExists(String customerDniOrRuc) {
        if (!customerDniOrRuc.isEmpty()) {
            // El campo no está vacío, consideramos que el cliente existe
            validationLabel.setText("Cliente valido");
            validationLabel.setTextFill(VALID_COLOR);
            return true;
        }
//        else {
//            // El campo está vacío, consideramos que el cliente no existe
//            validationLabel.setText("Cliente no existe");
//            validationLabel.setTextFill(INVALID_COLOR);
//            return false;
//        }
        return false;
    }


    private boolean isValidName(String name) {
        // Aquí puedes implementar tu lógica de validación de nombres
        // Puedes establecer tus propios criterios, como la longitud mínima, caracteres permitidos, etc.
        // Por ejemplo, este método verifica que el nombre no esté vacío y contenga solo letras y espacios.
        return !name.isEmpty() && name.matches("^[a-zA-Z ]*$");
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


}
