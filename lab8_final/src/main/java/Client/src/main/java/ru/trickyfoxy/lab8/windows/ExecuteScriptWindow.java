package ru.trickyfoxy.lab8.windows;


import ru.trickyfoxy.lab8.Client;
import ru.trickyfoxy.lab8.commands.CommandsManager;
import ru.trickyfoxy.lab8.exceptions.TimeoutConnectionException;
import ru.trickyfoxy.lab8.utils.LanguageSwitchble;
import ru.trickyfoxy.lab8.utils.ReadWriteInterface;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.ResourceBundle;

public class ExecuteScriptWindow extends JDialog implements LanguageSwitchble {

    private static volatile ExecuteScriptWindow instance;

    public static ExecuteScriptWindow getInstance() {
        ExecuteScriptWindow result = instance;
        if (result != null) {
            return result;
        }
        synchronized (ExecuteScriptWindow.class) {
            if (instance == null) {
                instance = new ExecuteScriptWindow();
            }
            return instance;
        }
    }

    private final JButton execute = new JButton(LocaleMenu.getBundle().getString("execute"));
    private final JLabel label = new JLabel(LocaleMenu.getBundle().getString("pathScript"));
    private final JTextField textField = new JTextField("/Users/deevroman/IdeaProjects/JavaLabs/lab8_final/a.script");

    private ExecuteScriptWindow() {
        this.setBounds(300, 400, 350, 130);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        BorderLayout borderLayout = new BorderLayout();
        Container container = this.getContentPane();
        container.setLayout(borderLayout);


        execute.addActionListener(e -> {
            Path path = Paths.get(textField.getText());
            Writer outputOfCommand = new StringWriter();
            try {
                CommandsManager.getInstance().loop(new ReadWriteInterface(new FileReader(path.toFile()), outputOfCommand, false), Client.getInstance().connector);
            } catch (IOException ioException) {
                InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("fileNotFound"));
                InformationWindow.getInstance().setVisible(true);
                return;
            } catch (TimeoutConnectionException timeoutConnectionException) {
                timeoutConnectionException.printStackTrace();
            }
            InformationWindow.getInstance().setAlert(outputOfCommand.toString());
            InformationWindow.getInstance().setVisible(true);
        });

        Container container1 = new Container();
        container1.setLayout(new FlowLayout(FlowLayout.RIGHT));
        container1.add(execute);

        container.add(label, BorderLayout.NORTH);
        container.add(textField, BorderLayout.CENTER);
        container.add(container1, BorderLayout.SOUTH);
    }

    @Override
    public void refreshLanguage(Locale locale) {
        ResourceBundle rb = ResourceBundle.getBundle("i18n/locale", locale);
        label.setText(rb.getString("pathScript"));
        execute.setText(rb.getString("execute"));
    }
}
