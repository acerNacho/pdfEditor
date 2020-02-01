package ui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.GridLayout;

public class PanelPdfUnir extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
    private JLabel headerLabel = new JLabel("");
	private JLabel statusLabel= new JLabel("");
	private JButton btnFiles = new JButton("Seleccionar pdfs");
    private JButton btnMerge = new JButton("Unir");
    
    public PanelPdfUnir() {
        setLayout(new GridLayout(4, 2, 5, 5));
        
        JLabel lblFiles = new JLabel("Archivos seleccionados", JLabel.CENTER);
        
        statusLabel.setSize(250, 100);
        btnMerge.setVisible(false);

        headerLabel.setText("Elija los pdfs a unir y luego el destino");
		
		btnFiles.setAlignmentX(JButton.CENTER);


        add(headerLabel);
	    add(new JPanel());
		add(lblFiles);
		add(btnFiles);

		add(statusLabel);

		add(btnMerge);
    }

	public void setStatusLabel(JLabel statusLabel) {
		this.statusLabel = statusLabel;
	}

	public JLabel getStatusLabel() {
		return statusLabel;
	}

	public JButton getBtnFiles() {
		return btnFiles;
	}

	public JButton getBtnMerge() {
		return btnMerge;
	}

	
}