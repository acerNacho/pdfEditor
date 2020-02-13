package ui;

public class PanelPdfUnir extends PanelPdf {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
    
    public PanelPdfUnir(String action) {
		super(action);
		SpringUtilities.makeCompactGrid(this,
                                        2, 2, //rows, cols
                                        12, 12,        //initX, initY
                                        6, 6);       //xPad, yPad
    }

}