package com.tienda.ControllerGUI.ViewsManager;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

public interface ViewsInterface {
    void showLogin();
    void showRegisterUser();
    void showMain();

    // Agrega otros métodos para mostrar más vistas si es necesario
    //module User
    void showUserManagement();
    void showMainView();
    void showProductManagement();
    void showSupplierManagement();


}
