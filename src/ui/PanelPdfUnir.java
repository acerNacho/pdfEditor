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
	private JButton btnFile1 = new JButton("Seleccionar 1");
	private JButton btnFile2 = new JButton("Seleccionar 2");
    private JButton btnMerge = new JButton("Unir");
    
    public PanelPdfUnir() {
        setLayout(new GridLayout(4, 2, 5, 5));
        
        JLabel lblFile1 = new JLabel("Archivo 1", JLabel.CENTER);
        JLabel lblFile2 = new JLabel("Archivo 2", JLabel.CENTER);
        
        statusLabel.setSize(250, 100);
        btnMerge.setVisible(false);

        headerLabel.setText("Elija los pdfs a unir y luego el destino");
		
		btnFile1.setAlignmentX(JButton.CENTER);


        add(headerLabel);
	    add(new JPanel());
		add(lblFile1);
		add(btnFile1);

		add(lblFile2);
		add(btnFile2);

		add(statusLabel);

		add(btnMerge);
    }

	public void setStatusLabel(JLabel statusLabel) {
		this.statusLabel = statusLabel;
	}

	public JLabel getStatusLabel() {
		return statusLabel;
	}

	public JButton getBtnFile1() {
		return btnFile1;
	}

	public JButton getBtnFile2() {
		return btnFile2;
	}

	public JButton getBtnMerge() {
		return btnMerge;
	}

	
}