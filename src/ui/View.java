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
	private PanelPdfUnir panelUnir;
	private PanelPdfAsegurar panelAsegurar;
	private PanelPdfFirmar panelFirmar;
	private JTabbedPane tabbedPane;

	/**
	 * Initialise the JFrame containing the various JSwing components responsible of
	 * the Vencimiento System.
	 */
	public View() {
		super("PDF Merge & Flat - Por I. Arce - v0.3");

		// set look and feel
		try {
			javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			/*
			javax.swing.UIManager.LookAndFeelInfo[] looks = javax.swing.UIManager.getInstalledLookAndFeels(); 
        	for (javax.swing.UIManager.LookAndFeelInfo look : looks) { 
            	System.out.println(look.getClassName()); 
        	} */
		} catch (Exception e) {e.printStackTrace();}
		

		crearGUI();
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setSize(600, 200);
	}

	/**
	 * Crear GUI: Crea un panel de contenido del tipo CardLayout y en el crea el
	 * panel de la tabla y el panel agregar vencimiento
	 */

	public void crearGUI() {
		
		panelUnir = new PanelPdfUnir("Unir");
		panelAsegurar = new PanelPdfAsegurar("Proteger");
		panelFirmar = new PanelPdfFirmar("Firmar");


		tabbedPane = new JTabbedPane();

		cp.setLayout(new BorderLayout());

		// tabbedPane
		

		// Panel unir
		tabbedPane.addTab("Unir", null, panelUnir,"Une los dos archivos PDF seleccionados en uno solo");

		// Panel asegurar
		tabbedPane.addTab("Asegurar", null, panelAsegurar,"Asegura el PDF elegido como imagen");
		pack();

		// Panel firmar
		// tabbedPane.addTab("Firmar", null, panelFirmar,"Firma el PDF elegido");
		pack();
		
		cp.add(tabbedPane, BorderLayout.CENTER);
	}

	public void agregarListeners(ActionListener action) {
		panelUnir.getBtnFiles().addActionListener(action);
		panelUnir.getBtnAction().addActionListener(action);
		panelAsegurar.getCheckBox().addActionListener(action);
		panelAsegurar.getBtnFiles().addActionListener(action);
		panelAsegurar.getBtnAction().addActionListener(action);
	}

	public PanelPdf getPanelPdfUnir() {
		return panelUnir;
	}

	public PanelPdfAsegurar getPanelPdfAsegurar() {
		return panelAsegurar;
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

}
