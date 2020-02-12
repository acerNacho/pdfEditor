package controller;

import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.DefaultResourceCache;
import org.apache.pdfbox.pdmodel.PDDocument;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import ui.PanelPdf;
import ui.View;

public class Controller {
    private static final String OWNER_PASSWORD = "prueba";
    private static String userPassword = "";
    private static final int KEY_LENGTH = 128;

    private static boolean encriptar = false;
    private static boolean formatear = false;

    private static View view;
    private JFileChooser fileDialog = new JFileChooser();
    private File files[];
    private ArrayList<PDDocument> pdfs = new ArrayList<>();

    /**
     * Whether or not debug output is printed to the console.
     */
    public static boolean SHOW_DEBUG = true;

    /**
     * The suggested prefix to add to output files.
     */
    public static String DESTINATION_FILENAME_PREFIX = "PDFPLANO--";
    private static final String DOCUMENT_CRYPT_PREFIX = "ENCRIPTADO--";
    private static final boolean PRINT_ENABLED = false;

    /**
     * The DPI that should be used when generating images. Higher DPI increases the
     * memory requirements and output file sizes, but also produces sharper images.
     */

    public static int IMAGE_DPI = 200;

    Controller(View view) {
        Controller.view = view;
    }

    protected void init() {
        view.agregarListeners(new BotonesListener());
    }

    private class BotonesListener implements ActionListener {


        @Override 
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
            case "Seleccionar":
                files = selectFiles();
                PanelPdf panelSeleccionado = (PanelPdf) view.getTabbedPane().getSelectedComponent();

                if (files != null) {
                    if(panelSeleccionado.getBtnAction().getText().equals("Proteger")) {
                        panelSeleccionado.getBtnAction().setVisible(true);
                        panelSeleccionado.getStatusLabel().setText("Se han seleccionado " + files.length + " archivos");
                        
                    } else if (files.length > 1) {
                        panelSeleccionado.getBtnAction().setVisible(true);
                        panelSeleccionado.getStatusLabel().setText("Se han seleccionado " + files.length + " archivos");
                    } 
                }else {
                    panelSeleccionado.getStatusLabel().setText("Cancelado por el usuario");
                }
                break;
            case "Unir":
                unirPdfs();
                break;
            case "Proteger":
                asegurarPdf();
                break;
            case "Encriptar":
                encriptar = !encriptar;
                view.getPanelPdfAsegurar().getTextField().setEditable(encriptar);
                view.getPanelPdfAsegurar().getTextField().setEnabled(encriptar);
                break;
            case "Quitar formato":
                formatear = !formatear;
                break;
            default:
                log("Error: "+ e.getActionCommand());
                break;
            }
        }

    }

    private File[] selectFiles() {
        fileDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileDialog.setMultiSelectionEnabled(true);
        fileDialog.addChoosableFileFilter(new FileNameExtensionFilter("Documentos PDF", "pdf"));
        fileDialog.setAcceptAllFileFilterUsed(false);

        int returnVal = fileDialog.showOpenDialog(view.getContentPane());

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return fileDialog.getSelectedFiles();
        }
        return null;
    }

    private String getSeparatorBarByOS() {
        String aux = "";

        for (int i = 0; i < files.length - 1; i++) {
            aux += files[i].getName().substring(0, files[i].getName().length() - 4) + " union ";
        }

        return fileDialog.getSelectedFile().toString() + File.separator + aux
                + files[files.length - 1].getName().substring(0, files[files.length - 1].getName().length() - 4)
                + ".pdf";
    }

    /**
     * Takes a PDF file and merge it with other PDF file. No metadata from the
     * source PDF file is copied into the new PDF file.
     */
    private void unirPdfs() {
        fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (fileDialog.showOpenDialog(view.getContentPane()) == JFileChooser.APPROVE_OPTION) {
            try {

                for (File file : files) {

                    PDDocument pdf = PDDocument.load(file);
                    pdfs.add(pdf);
                }

                log("uniendo");
                PDFMergerUtility PDFmerger = new PDFMergerUtility();

                PDFmerger.setDestinationFileName(getSeparatorBarByOS());

                for (File file : files) {
                    PDFmerger.addSource(file);
                }

                PDFmerger.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());

                pdfs.forEach(pdf -> {
                    try {
                        pdf.close();
                    } catch (IOException e) {
                        log("Error: " + e.getMessage());
                    }
                });

                view.getPanelPdfUnir().getStatusLabel().setText("Se ha creado el pdf sin errores");
            } catch (Exception e) {
                view.getPanelPdfUnir().getStatusLabel().setText("Se produjo un error al crear el PDF");
                log("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Takes a PDF file and flattens it into a new PDF file. The new PDF file is a
     * series of images generated from the source PDF file. No metadata from the
     * source PDF file is copied into the new PDF file.
     */
    private void asegurarPdf() {
        fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileDialog.setDialogTitle("Elegir a donde guardar PDF - (PDF Plano - Por IA)");
        fileDialog.setApproveButtonText("Seleccionar");

        if (fileDialog.showOpenDialog(view.getContentPane()) == JFileChooser.APPROVE_OPTION) {
            try {
                int returnValue = 0;
                for (File file : files) {
                        returnValue = flattenPDF(file, new File(fileDialog.getSelectedFile().toString() + File.separator
                            + DESTINATION_FILENAME_PREFIX + file.getName()));
                }
                if (returnValue == 0) {
                    JOptionPane.showMessageDialog(null, "Se han creado los PDF satisfactoriamente", "Proceso completo",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al crear el pdf.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception e) {
                log("Error: " + e.getMessage());
            }
        }

    }

    /**
     * Takes a PDF file and flattens it into a new PDF file. The new PDF file is a
     * series of images generated from the source PDF file. No metadata from the
     * source PDF file is copied into the new PDF file.
     * 
     * @param sourceFile      - The source PDF file to be flattened.
     * @param destinationFile - The output PDF file.
     * @return - 0 if successful
     */

    public static int flattenPDF(File sourceFile, File destinationFile) {

        PDDocument sourceDoc = null;
        PDDocument destDoc = new PDDocument();

        try {
 
            long maxAvailableMemoryInMB = Runtime.getRuntime().maxMemory() / 1024 / 1024;
			
			// If less than 1GB available, be more memory conscious
			if (maxAvailableMemoryInMB < 2048) {
				
				log("Max memory limited to " + maxAvailableMemoryInMB + "MB. Resource cache will be disabled.");
				
				sourceDoc = PDDocument.load(sourceFile, MemoryUsageSetting.setupTempFileOnly());

				sourceDoc.setResourceCache(new DefaultResourceCache() {
					public void put (COSObject indirect, PDXObject xobject) {
						// discard
					}
				});
			}
			else {
				sourceDoc = PDDocument.load(sourceFile);
            }
            
            // sourceDoc = PDDocument.load(sourceFile, MemoryUsageSetting.setupTempFileOnly()); //  Por si hay poca memoria pero lento
            // sourceDoc = PDDocument.load(sourceFile); mas memoria pero se traba
            PDFRenderer pdfRenderer = new PDFRenderer(sourceDoc);
            final int pageCount = sourceDoc.getDocumentCatalog().getPages().getCount();

            for (int i = 0; i < pageCount; i += 1) {
                log("Procesando página " + (i + 1) + " de " + pageCount + " de " + sourceFile.getName());
                view.setTitle("Procesando página " + (i + 1) + " de " + pageCount + " de " + sourceFile.getName());
                view.getPanelPdfAsegurar().getBtnAction().setEnabled(false);

                BufferedImage img = pdfRenderer.renderImageWithDPI(i, IMAGE_DPI, ImageType.RGB);

                PDPage imagePage = new PDPage(new PDRectangle(img.getWidth(), img.getHeight()));
                destDoc.addPage(imagePage);

                PDImageXObject imgObj = LosslessFactory.createFromImage(destDoc, img);

                PDPageContentStream imagePageContentStream = new PDPageContentStream(destDoc, imagePage);
                imagePageContentStream.drawImage(imgObj, 0, 0);

                log("Image added successfully.");
                view.getPanelPdfAsegurar().getBtnAction().setEnabled(true);

                /*
                 * Close and clear images
                 */

                log("Close and clear images.");
                imagePageContentStream.close();
                view.setTitle("PDF Merge & Flat - Por I. Arce - v0.3");

                imgObj = null;

                img.flush();
                img = null;
            }

            log("New flattened PDF created in memory.");

            /*
             * Remove links to the source document before saving. (Get back as much memory
             * as possible.)
             */

            pdfRenderer = null;

            sourceDoc.close();
            sourceDoc = null;

            /*
             * Write the new PDF file
             */

            log("Saving new flattened PDF...");
            
            

            if (encriptar) {
                encryptDoc(destDoc,destinationFile);
            } else {
                destDoc.save(destinationFile);
            }
            
            destDoc.close();

            log("Saved successfully.");
            
        } catch (Exception e) {
            log("Error: " + e.getMessage());
            return 1;
        } finally {

            try {
                sourceDoc.close();
            } catch (Exception e) {
                // ignore
            }

            try {
                destDoc.close();
            } catch (Exception e) {
                // ignore
            }
        }

        return 0;
    }

    private static void encryptDoc(PDDocument doc, File destFile) {
        try {
            final AccessPermission ap = new AccessPermission();

            ap.setCanPrint(PRINT_ENABLED);

            userPassword = view.getPanelPdfAsegurar().getTextField().getText();

            StandardProtectionPolicy spp = new StandardProtectionPolicy(OWNER_PASSWORD, userPassword, ap);
            spp.setEncryptionKeyLength(KEY_LENGTH);
            spp.setPermissions(ap);

            doc.protect(spp);
            doc.save(destFile.getParent() + File.separator + DOCUMENT_CRYPT_PREFIX + destFile.getName()); // cambiar esto
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void log(String message) {
        if (SHOW_DEBUG) {
            if (message.startsWith("Error")) {
                JOptionPane.showMessageDialog(null, message, "Error",
                            JOptionPane.ERROR_MESSAGE);
                System.err.println(new Date().toString() + " - " + message);
            } else {
                System.out.println(new Date().toString() + " - " + message);
            }
            
        }
    }

}