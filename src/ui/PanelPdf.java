package ui;

import javax.swing.*;

public class PanelPdf extends JPanel {

	private static final long serialVersionUID = 1L;

    private JLabel headerLabel;
    private JLabel statusLabel= new JLabel("");
    private JButton btnFiles = new JButton("Seleccionar");
    private JButton btnAction = new JButton();

    protected PanelPdf(String action) {
        super();
        
        
        //setLayout(new GridLayout(3, 2, 50, 5));
        setLayout(new SpringLayout());
        
        this.btnAction.setText(action);
        
        JLabel headerLabel = new JLabel("Archivos seleccionados");
        
        statusLabel.setSize(250, 100);
        btnAction.setVisible(false);

        headerLabel.setText("Elija los pdfs a " + action + " y luego el destino");
		
        //btnFiles.setHorizontalAlignment(JButton.CENTER);
        
        add(headerLabel);
        
		add(btnFiles);

		add(statusLabel);

        add(btnAction);
        

    }

    public JLabel getHeaderLabel() {
        return headerLabel;
    }

    public void setHeaderLabel(JLabel headerLabel) {
        this.headerLabel = headerLabel;
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(JLabel statusLabel) {
        this.statusLabel = statusLabel;
    }

    public JButton getBtnFiles() {
        return btnFiles;
    }

    public void setBtnFiles(JButton btnFiles) {
        this.btnFiles = btnFiles;
    }

    public JButton getBtnAction() {
        return btnAction;
    }

    public void setBtnAction(JButton btnAction) {
        this.btnAction = btnAction;
    }

    
}