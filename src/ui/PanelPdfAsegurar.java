package ui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;



import java.awt.FlowLayout;

public class PanelPdfAsegurar extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JLabel headerLabel = new JLabel("Seleccionar PDF a aplanar");
    private JButton btnAsegurar = new JButton("Asegurar Pdf");
    
    public PanelPdfAsegurar() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 50,50));
        add(headerLabel);
        add(btnAsegurar);
    }

    public JButton getBtnAsegurar() {
        return btnAsegurar;
    }

    public JLabel getHeaderLabel() {
        return headerLabel;
    }
    
}