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
        //textField.setHorizontalAlignment(JTextField.CENTER); 


        // JCheckBox properties
        checkBox1.setAlignmentX(JCheckBox.CENTER);
        //checkBox2.setHorizontalAlignment(JCheckBox.CENTER);

        
        

        // Add components to the panel
        add(checkBox1);
        add(textField);

        // TODO : FIX THIS
        //add(checkBox2);

        SpringUtilities.makeCompactGrid(this,
                                        3, 2, //rows, cols
                                        6, 6,        //initX, initY
                                        6, 6);       //xPad, yPad
        
    }

    public JCheckBox getCheckBox() {
        return checkBox1;
    }

    public JTextField getTextField() {
        return textField;
    }

    
}