package ru.trickyfoxy.lab8.windows;


import ru.trickyfoxy.lab8.utils.LanguageSwitchble;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class RemoveByDistanceWindow extends JDialog implements LanguageSwitchble {
    private static volatile RemoveByDistanceWindow instance;

    private JLabel distance = new JLabel("Distance");
    private JTextField distanceField = new JTextField("", 5);
    private JButton remove = new JButton(LocaleMenu.getBundle().getString("remove"));

    private enum RemoveType {
        BY_DISTANCE,
        GREATER_DISTANCE
    }

    private RemoveType[] types = new RemoveType[]{RemoveType.BY_DISTANCE, RemoveType.GREATER_DISTANCE};
    private JComboBox<RemoveType> removeTypes = new JComboBox<>(types);

    private RemoveByDistanceWindow() {
        this.setBounds(300, 400, 197, 113);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        remove.addActionListener(e -> {
            try {
                MainWindow.RemovingType type;
                switch (removeTypes.getSelectedIndex()) {
                    case 0:
                        type = MainWindow.RemovingType.REMOVE_ANY_BY_DISTANCE;
                        break;
                    case 1:
                        type = MainWindow.RemovingType.REMOVE_GREATER;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + removeTypes.getSelectedIndex());
                }
                if (MainWindow.getInstance().removeElement(Long.parseLong(distanceField.getText()), type)) {
                    MainWindow.getInstance().refreshElements();
                    this.setVisible(false);
                } else {
                    InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("notDeleted"));
                    InformationWindow.getInstance().setVisible(true);
                }
            } catch (Exception exception) {
                InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("errorId"));
                InformationWindow.getInstance().setVisible(true);
            }
        });

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(3, 1));

        Container container3 = new Container();
        container3.setLayout(new GridLayout(1, 1));
        container3.add(removeTypes, BorderLayout.CENTER);

        Container container2 = new Container();
        container2.setLayout(new GridLayout(1, 2));
        container2.add(distance, BorderLayout.CENTER);
        container2.add(distanceField, BorderLayout.EAST);

        Container container1 = new Container();
        container1.setLayout(new FlowLayout(FlowLayout.RIGHT));
        container1.add(remove);

        container.add(container3);
        container.add(container2);
        container.add(container1);
    }

    public static RemoveByDistanceWindow getInstance() {
        RemoveByDistanceWindow result = instance;
        if (result != null) {
            return result;
        }
        synchronized (RemoveByDistanceWindow.class) {
            if (instance == null) {
                instance = new RemoveByDistanceWindow();
            }
            return instance;
        }
    }

    public void refreshLanguage(Locale locale) {
        ResourceBundle rb = ResourceBundle.getBundle("i18n/locale", locale);
        remove.setText(rb.getString("remove"));
    }
}
