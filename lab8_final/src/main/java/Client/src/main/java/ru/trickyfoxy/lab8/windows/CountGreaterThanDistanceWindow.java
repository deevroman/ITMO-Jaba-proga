package ru.trickyfoxy.lab8.windows;


import ru.trickyfoxy.lab8.utils.LanguageSwitchble;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class CountGreaterThanDistanceWindow extends JDialog implements LanguageSwitchble {
    private static volatile CountGreaterThanDistanceWindow instance;

    private JLabel distance = new JLabel("Distance");
    private JTextField distanceField = new JTextField("", 5);
    private JButton remove = new JButton(LocaleMenu.getBundle().getString("counting"));

    private CountGreaterThanDistanceWindow() {
        this.setBounds(300, 400, 197, 113);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        remove.addActionListener(e -> {
            try {
                MainWindow.getInstance().getCountGreaterThanDistance(Float.parseFloat(distanceField.getText()));
                this.setVisible(false);
            } catch (Exception exception) {
                InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("errorDistance"));
                InformationWindow.getInstance().setVisible(true);
            }
        });

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(2, 1));

        Container container2 = new Container();
        container2.setLayout(new GridLayout(1, 2));
        container2.add(distance, BorderLayout.CENTER);
        container2.add(distanceField, BorderLayout.EAST);

        Container container1 = new Container();
        container1.setLayout(new FlowLayout(FlowLayout.RIGHT));
        container1.add(remove);

        container.add(container2);
        container.add(container1);
    }

    public static CountGreaterThanDistanceWindow getInstance() {
        CountGreaterThanDistanceWindow result = instance;
        if (result != null) {
            return result;
        }
        synchronized (CountGreaterThanDistanceWindow.class) {
            if (instance == null) {
                instance = new CountGreaterThanDistanceWindow();
            }
            return instance;
        }
    }

    public void refreshLanguage(Locale locale) {
        ResourceBundle rb = ResourceBundle.getBundle("i18n/locales", locale);
        remove.setText(rb.getString("counting"));
    }
}
