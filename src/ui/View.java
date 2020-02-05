package ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;


/**
 * Class responsible for launching the Vencimiento System.
 * 
 * @author acerNacho
 * @version 0.1b
 */
public class View extends JFrame {

	private static final long serialVersionUID = 1L;
	private final Container cp = getContentPane();
	private PanelPdfUnir panelUnir = new PanelPdfUnir();
	private PanelPdfAsegurar panelAsegurar = new PanelPdfAsegurar();
	private JTabbedPane tabbedPane = new JTabbedPane();
	/**
	 * Initialise the JFrame containing the various JSwing components responsible of
	 * the Vencimiento System.
	 */
	public View() {
		crearGUI();
		
		setTitle("PDF Merge & Flat - Por I. Arce - v0.2");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setSize(600, 200);
	}



	/**
	 * Crear GUI: Crea un panel de contenido del tipo CardLayout y en el crea el
	 * panel de la tabla y el panel agregar vencimiento
	 */

	public void crearGUI() {
		cp.setLayout(new BorderLayout());

		// tabbedPane
		

		// Panel unir
		tabbedPane.addTab("Unir", null, panelUnir,"Une los dos archivos PDF seleccionados en uno solo");

		// Panel asegurar
		tabbedPane.addTab("Asegurar", null, panelAsegurar,"Asegura el PDF elegido como imagen");
		pack();
		
		cp.add(tabbedPane, BorderLayout.CENTER);
	}

	public void agregarListeners(ActionListener action) {
		panelUnir.getBtnFiles().addActionListener(action);
		panelUnir.getBtnMerge().addActionListener(action);

		panelAsegurar.getBtnAsegurar().addActionListener(action);
	}

	public PanelPdfUnir getPanelUnir() {
		return panelUnir;
	}

	public PanelPdfAsegurar getPdfAsegurar() {
		return panelAsegurar;
	}

}
