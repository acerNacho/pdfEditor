package ui;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.GridLayout;

public class PanelPdfAsegurar extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JLabel headerLabel = new JLabel("Seleccionar PDF a aplanar");
    private JButton btnAsegurar = new JButton("Asegurar Pdf");
    private JCheckBox checkBox = new JCheckBox("Encriptar");
    private JTextField textArea = new JTextField("");
    
    public PanelPdfAsegurar() {
        setLayout(new GridLayout(3,3, 5 ,5));
        
        textArea.setEditable(false);
        add(headerLabel);
        add(btnAsegurar);
        add(checkBox);
        add(textArea);
        
    }

    public JButton getBtnAsegurar() {
        return btnAsegurar;
    }

    public JLabel getHeaderLabel() {
        return headerLabel;
    }

    public JCheckBox getCheckBox() {
        return checkBox;
    }

    public JTextField getTextArea() {
        return textArea;
    }

    
}