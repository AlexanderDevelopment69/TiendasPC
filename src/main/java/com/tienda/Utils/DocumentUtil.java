package com.tienda.Utils;

import com.tienda.Dao.CustomerDAO;
import com.tienda.Dao.DocumentDAO;
import com.tienda.dto.DocumentDTO;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;

import java.util.concurrent.CompletableFuture;

public class DocumentUtil {

    private DocumentDAO documentDAO;
    private boolean isDocumentSaveInProgress = false;
    private boolean isGetDocumentBySaleIdInProgress = false;

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

    public DocumentDTO getDocumentBySaleId(Long saleId) {
        if (isGetDocumentBySaleIdInProgress) {
            // Puedes manejar el caso cuando una solicitud ya está en progreso.
            // Puedes lanzar una excepción, mostrar un mensaje o tomar la acción que desees.
            return null; // Por ejemplo, devolvemos null en este caso.
        }

        isGetDocumentBySaleIdInProgress = true;

        Task<DocumentDTO> task = new Task<>() {
            @Override
            protected DocumentDTO call() {
                try {
                    System.out.println("Hilo de obtención de documento iniciado");
                    DocumentDTO result = documentDAO.getDocumentBySaleId(saleId);
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null; // Manejo de errores, puedes ajustarlo según tus necesidades.
                } finally {
                    System.out.println("Hilo de obtención de documento terminado");
                    isGetDocumentBySaleIdInProgress = false;
                }
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        return task.getValue();
    }
}
