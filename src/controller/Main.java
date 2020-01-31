package controller;

import javax.swing.SwingUtilities;

import ui.View;


public class Main {
    public static void main(String[] args) throws Exception {
        View view = new View();
        Controller controller = new Controller(view);
        
		SwingUtilities.invokeLater(() -> {
            view.setVisible(true);
            controller.init();
        });
    }
}