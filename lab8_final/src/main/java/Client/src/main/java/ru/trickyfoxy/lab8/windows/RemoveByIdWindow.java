package ru.trickyfoxy.lab8.windows;


import ru.trickyfoxy.lab8.utils.LanguageSwitchble;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class RemoveByIdWindow extends JDialog implements LanguageSwitchble {
    private static volatile RemoveByIdWindow instance;

    private JLabel id = new JLabel("ID");
    private JTextField idField = new JTextField("", 5);
    private JButton remove = new JButton(LocaleMenu.getBundle().getString("remove"));

    private RemoveByIdWindow() {
        this.setBounds(300, 400, 197, 113);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        remove.addActionListener(e -> {
            try {
                if (MainWindow.getInstance().removeElement(Long.parseLong(idField.getText()), MainWindow.RemovingType.REMOVE)) {
                    MainWindow.getInstance().refreshElements();
                    this.setVisible(false);
                } else {
                    InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("notDeleted_exist"));
                    InformationWindow.getInstance().setVisible(true);
                }
            } catch (Exception exception) {
                InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("errorId"));
                InformationWindow.getInstance().setVisible(true);
            }
        });

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(2, 1));

        Container container2 = new Container();
        container2.setLayout(new GridLayout(1, 2));
        container2.add(id, BorderLayout.CENTER);
        container2.add(idField, BorderLayout.EAST);

        Container container1 = new Container();
        container1.setLayout(new FlowLayout(FlowLayout.RIGHT));
        container1.add(remove);

        container.add(container2);
        container.add(container1);
    }

    public static RemoveByIdWindow getInstance() {
        RemoveByIdWindow result = instance;
        if (result != null) {
            return result;
        }
        synchronized (RemoveByIdWindow.class) {
            if (instance == null) {
                instance = new RemoveByIdWindow();
            }
            return instance;
        }
    }

    public void refreshLanguage(Locale locale) {
        ResourceBundle rb = ResourceBundle.getBundle("i18n/locale", locale);
        remove.setText(rb.getString("remove"));
    }
}
