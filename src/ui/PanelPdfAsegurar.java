package ui;

import javax.swing.JCheckBox;
import javax.swing.JTextField;


public class PanelPdfAsegurar extends PanelPdf {


    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private JCheckBox checkBox1 = new JCheckBox("Encriptar");
    private JCheckBox checkBox2 = new JCheckBox("Quitar formato");
    private JTextField textField = new JTextField("");
    
    public PanelPdfAsegurar(String action) {
        super(action);
        

        // JTextField properties
        textField.setEditable(false);
        textField.setEnabled(false);
        textField.setHorizontalAlignment(JTextField.CENTER); 


        // JCheckBox properties
        checkBox1.setHorizontalAlignment(JCheckBox.CENTER);
        checkBox2.setHorizontalAlignment(JCheckBox.CENTER);

        checkBox2.setVisible(false);
        // TODO : FIX THIS

        // Add components to the panel
        add(checkBox1);
        add(textField);
        add(checkBox2);
        
    }

    public JCheckBox getCheckBox() {
        return checkBox1;
    }

    public JTextField getTextField() {
        return textField;
    }

    
}