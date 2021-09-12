package ru.trickyfoxy.lab8.windows;

import javax.swing.*;
import java.awt.*;

public class InformationWindow extends JDialog {

    private static volatile InformationWindow instance;
    private JLabel alert = new JLabel("Alert");

    private JButton ok = new JButton("Ok");

    private InformationWindow() {
        this.setBounds(300, 400, 525, 200);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        BorderLayout borderLayout = new BorderLayout();
        Container container = this.getContentPane();
        container.setLayout(borderLayout);

        ok.addActionListener(e -> this.setVisible(false));

        Container container1 = new Container();
        container1.setLayout(new FlowLayout(FlowLayout.RIGHT));
        container1.add(ok);

        Container container2 = new Container();
        container2.setLayout(new FlowLayout(FlowLayout.RIGHT));
        Container container3 = new Container();
        container3.setLayout(new FlowLayout(FlowLayout.RIGHT));

        container.add(container2, BorderLayout.EAST);
        container.add(container3, BorderLayout.WEST);
        container.add(alert, BorderLayout.CENTER);
        container.add(container1, BorderLayout.SOUTH);
    }

    public static InformationWindow getInstance() {
        InformationWindow result = instance;
        if (result != null) {
            return result;
        }
        synchronized (InformationWindow.class) {
            if (instance == null) {
                instance = new InformationWindow();
            }
            return instance;
        }
    }

    public void setAlert(String s) {
        alert.setText(s);
    }

}
