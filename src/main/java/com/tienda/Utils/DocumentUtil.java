package com.tienda.Utils;

import com.tienda.Dao.CustomerDAO;
import com.tienda.Dao.DocumentDAO;
import com.tienda.dto.DocumentDTO;
import javafx.concurrent.Task;

public class DocumentUtil {

    private DocumentDAO documentDAO;
    private boolean isDocumentSaveInProgress = false;

    public DocumentUtil(DocumentDAO documentDAO) {
        this.documentDAO = documentDAO;
    }


   public void saveDocument(DocumentDTO documentDTO) {
        if (isDocumentSaveInProgress) {
            System.out.println("Una operación de guardado está en progreso. Espere a que se complete.");
            return;
        }

        // Marcar que se está ejecutando una operación de guardado
        isDocumentSaveInProgress = true;

        Task<Void> saveDocumentTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                System.out.println("Iniciando el hilo de guardado...");
                documentDAO.saveDocument(documentDTO);

                return null;
            }
        };

        saveDocumentTask.setOnSucceeded(event -> {
            System.out.println("Hilo de guardado completado con éxito.");
            // Restablecer la bandera una vez que se completa la operación
            isDocumentSaveInProgress = false;
        });

        saveDocumentTask.setOnFailed(event -> {
            System.out.println("Error en el hilo de guardado: " + saveDocumentTask.getException().getMessage());
            // Asegurarse de restablecer la bandera en caso de error
            isDocumentSaveInProgress = false;
        });

        // Iniciar el hilo del documento
        new Thread(saveDocumentTask).start();
    }

}
