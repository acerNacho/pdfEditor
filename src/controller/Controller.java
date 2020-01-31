package controller;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;

import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.DefaultResourceCache;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import ui.View;

public class Controller {
    private static View view;
    private final JFileChooser fileDialog = new JFileChooser();
    private File file1;
    private File file2;
    private PDDocument pdf1;
    private PDDocument pdf2;

    /**
	 * Whether or not debug output is printed to the console.
	 */
	public static boolean SHOW_DEBUG = true;
	
	/**
	 * The suggested prefix to add to output files. 
	 */
	public static String DESTINATION_FILENAME_PREFIX = "PDFPLANO--";
	
	/**
	 * The DPI that should be used when generating images.
	 * Higher DPI increases the memory requirements and output file sizes, but also produces sharper images.
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
            case "Seleccionar 1":
                pdf1 = new PDDocument();
                archivoUno();
                break;
            case "Seleccionar 2":
                pdf2 = new PDDocument();
                archivoDos();
                break;
            case "Unir":
                unirPdfs();
                break;
            case "Asegurar Pdf":
                asegurarPdf();
                break;
            default:
                System.out.println("Error desconocido");
                break;
            }
        }

    }

    private void archivoUno() {
        fileDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fileDialog.showOpenDialog(view.getContentPane());

        try {
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file1 = fileDialog.getSelectedFile();
                pdf1 = PDDocument.load(file1);
                if (file2 != null && file1 != null) {
                    view.getPanelUnir().getBtnMerge().setVisible(true);
                }
            } else {
                view.getPanelUnir().getStatusLabel().setText("Cancelado por el usuario");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void archivoDos() {
        fileDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fileDialog.showOpenDialog(view.getContentPane());
        try {
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file2 = fileDialog.getSelectedFile();
                pdf2 = PDDocument.load(file1);
                if (file2 != null && file1 != null) {
                    view.getPanelUnir().getBtnMerge().setVisible(true);
                }
            } else {
                view.getPanelUnir().getStatusLabel().setText("Cancelado por el usuario");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getSeparatorBarByOS() {
        String OS = System.getProperty("os.name").toLowerCase();
        String osCharacter = (OS.indexOf("win") >= 0 || OS.indexOf("mac") >= 0) ? "\\" : "/";

        return fileDialog.getSelectedFile().toString() + osCharacter
                + file1.getName().substring(0, file1.getName().length() - 4) + " union "
                + file2.getName().substring(0, file2.getName().length() - 4) + ".pdf";
    }

    private void unirPdfs() {
        fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fileDialog.showOpenDialog(view.getContentPane());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                System.out.println("uniendo");
                PDFMergerUtility PDFmerger = new PDFMergerUtility();
                System.out.println(getSeparatorBarByOS());

                PDFmerger.setDestinationFileName(getSeparatorBarByOS());

                PDFmerger.addSource(file1);
                PDFmerger.addSource(file2);
                PDFmerger.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());
                pdf1.close();
                pdf2.close();
                view.getPanelUnir().getStatusLabel().setText("Se ha creado el pdf sin errores");
            } catch (Exception ex) {
                view.getPanelUnir().getStatusLabel().setText("Se produjo un error al crear el PDF");
                ex.printStackTrace();
            }
        }
    }

    private void asegurarPdf() {
    
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Documentos PDF", "pdf"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        /*
         * Get the source file
         */

        File sourceFile = null;

        boolean sourceFileFromArgument = false;

        if (!sourceFileFromArgument) {

            fileChooser.setDialogTitle("Elegí el PDF - (PDF Plano - Por IA)");
            fileChooser.setApproveButtonText("Seleccionar PDF");

            int returnValue = fileChooser.showOpenDialog(null);

            if (returnValue != JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(null, "No hay archivos seleccionados", "Error",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }

            sourceFile = fileChooser.getSelectedFile();
        }

        /*
         * Get the destination file
         */

        File destinationFile = null;
        boolean destinationFileFromArgument = false;

        if (!destinationFileFromArgument) {

            destinationFile = new File(
                    sourceFile.getAbsolutePath() + File.separator + DESTINATION_FILENAME_PREFIX + sourceFile.getName());

            fileChooser.setDialogTitle("Elegir a donde guardar PDF - (PDF Plano - Por IA)");
            fileChooser.setApproveButtonText("Seleccionar PDF destino");
            fileChooser.setSelectedFile(destinationFile);

            int returnValue = fileChooser.showSaveDialog(null);

            if (returnValue != JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(null, "No hay archivos PDF seleccionados.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }

            destinationFile = fileChooser.getSelectedFile();
        }

        if (sourceFile.getPath().equals(destinationFile.getPath())
                && sourceFile.getName().equals(destinationFile.getName())) {
            JOptionPane.showMessageDialog(null, "No podes elegir el mismo PDF seleccionado", "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        if (!destinationFile.getName().toLowerCase().endsWith(".pdf")) {
            destinationFile = new File(destinationFile.getAbsolutePath() + ".pdf");
        }

        /*
         * Do the flattening
         */

        JOptionPane.showMessageDialog(null,
                "Se esta formateando el archivo\n" + sourceFile.getAbsolutePath() + "\n" + "en uno nuevo\n"
                        + destinationFile.getAbsolutePath() + "\n\n" + "Por favor, espere",
                "Listo para empezar", JOptionPane.INFORMATION_MESSAGE);

        int returnValue = flattenPDF(sourceFile, destinationFile);

        if (returnValue == 0) {
            JOptionPane.showMessageDialog(null, "Se ha creado el PDF satisfactoriamente", "Proceso completo",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error al crear el pdf.", "Error",
                    JOptionPane.ERROR_MESSAGE);
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

        long startMillis = System.currentTimeMillis();

        PDDocument sourceDoc = null;
        PDDocument destDoc = new PDDocument();

        try {

            long maxAvailableMemoryInMB = Runtime.getRuntime().maxMemory() / 1024 / 1024;

            // If less than 1GB available, be more memory conscious
            if (maxAvailableMemoryInMB < 2048) {

                log("Max memory limited to " + maxAvailableMemoryInMB + "MB. Resource cache will be disabled.");

                sourceDoc = PDDocument.load(sourceFile, MemoryUsageSetting.setupTempFileOnly());

                sourceDoc.setResourceCache(new DefaultResourceCache() {
                    public void put(COSObject indirect, PDXObject xobject) {
                        // discard
                    }
                });
            } else {
                sourceDoc = PDDocument.load(sourceFile);
            }

            PDFRenderer pdfRenderer = new PDFRenderer(sourceDoc);

            final int pageCount = sourceDoc.getDocumentCatalog().getPages().getCount();

            log(pageCount + " page" + (pageCount == 1 ? "" : "s") + " to flatten.");

            for (int i = 0; i < pageCount; i += 1) {

                log("Flattening page " + (i + 1) + " of " + pageCount + "...");
                //////////////////////////////////
                view.setTitle("Procesando página " + (i + 1) + " de " + pageCount + "...");
                view.getPdfAsegurar().getBtnAsegurar().setEnabled(false);

                
                
////////////////////////////////////////////////
                BufferedImage img = pdfRenderer.renderImageWithDPI(i, IMAGE_DPI, ImageType.RGB);

                log("  Image rendered in memory (" + img.getWidth() + "x" + img.getHeight() + " " + IMAGE_DPI
                        + "DPI).  Adding to PDF...");

                PDPage imagePage = new PDPage(new PDRectangle(img.getWidth(), img.getHeight()));
                destDoc.addPage(imagePage);

                PDImageXObject imgObj = LosslessFactory.createFromImage(destDoc, img);

                PDPageContentStream imagePageContentStream = new PDPageContentStream(destDoc, imagePage);
                imagePageContentStream.drawImage(imgObj, 0, 0);
                
                log("  Image added successfully.");
                view.getPdfAsegurar().getBtnAsegurar().setEnabled(true);
                /*
                 * Close and clear images
                 */

                imagePageContentStream.close();
                
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

            destDoc.save(destinationFile);
            destDoc.close();

            log("Saved successfully (" + ((System.currentTimeMillis() - startMillis) / 1000.0) + " seconds).");
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
    
    private static void log(String message) {
		if (SHOW_DEBUG) {
			System.out.println(new Date().toString() + " - " + message);
		}
	}

}