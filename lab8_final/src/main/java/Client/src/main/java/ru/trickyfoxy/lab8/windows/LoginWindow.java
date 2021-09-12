package ru.trickyfoxy.lab8.windows;

import ru.trickyfoxy.lab8.Client.Client;
import ru.trickyfoxy.lab8.utils.LanguageSwitchble;
import ru.trickyfoxy.lab8.utils.UTF8Control;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginWindow extends JFrame implements LanguageSwitchble {

    private static LoginWindow instance;

    public static LoginWindow getInstance() {
        if (instance == null) {
            instance = new LoginWindow();
        }
        return instance;
    }

    private final JTextField hostInput = new JTextField("localhost", 5);
    private final JTextField portInput = new JTextField(String.valueOf(Client.getPORT()), 5);
    private final JLabel hostLabel = new JLabel(LocaleMenu.getBundle().getString("enterHost"));
    private final JLabel portLabel = new JLabel(LocaleMenu.getBundle().getString("enterPort"));
    private final JButton signInButton = new JButton(LocaleMenu.getBundle().getString("signIn"));
    private final JButton regButton = new JButton(LocaleMenu.getBundle().getString("reg"));
    private final JLabel loginLabel = new JLabel(LocaleMenu.getBundle().getString("enterLogin"));
    private final JLabel passwordLabel = new JLabel(LocaleMenu.getBundle().getString("enterPassword"));
    private final JTextField loginInput = new JTextField("kek", 5);
    private final JTextField passwordInput = new JTextField("lol", 5);

    public static void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    private LoginWindow() {
        this.setSize(350, 180);
        centreWindow(this);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        signInButton.addActionListener(e -> {
            try {
                String loginInputText = loginInput.getText();
                String password = passwordInput.getText();
                String host = hostInput.getText();
                int port = Integer.parseInt(portInput.getText());
                Client.setHOST(host);
                Client.setPORT(port);
                Client.getInstance().init();
                if (Client.getInstance().authUser(loginInputText, password)) {
                    MainWindow.getInstance().setLogin(loginInputText, password);
                    this.setVisible(false);
                    MainWindow.getInstance().setVisible(true);
                    MainWindow.getInstance().refreshElements();
                } else {
                    InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("wrongLoginOrPassword"));
                    InformationWindow.getInstance().setVisible(true);
                }
            } catch (Exception exception) {
                InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("notConnectWithServer"));
                InformationWindow.getInstance().setVisible(true);
                exception.printStackTrace();
            }
        });

        regButton.addActionListener(e -> {
            try {
                String loginInputText = loginInput.getText();
                String password = passwordInput.getText();
                String host = hostInput.getText();
                int port = Integer.parseInt(portInput.getText());
                Client.setHOST(host);
                Client.setPORT(port);
                Client.getInstance().init();
                if (Client.getInstance().regUser(loginInputText, password)) {
                    InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("registerOk"));
                    InformationWindow.getInstance().setVisible(true);
                } else {
                    InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("wrongLoginOrPassword"));
                    InformationWindow.getInstance().setVisible(true);
                }
            } catch (Exception exception) {
                InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("cannot"));
                InformationWindow.getInstance().setVisible(true);
                exception.printStackTrace();
            }
        });

        Container container = this.getContentPane();
        GridLayout gridLayout = new GridLayout(5, 2, 2, 2);
        container.setLayout(gridLayout);

        container.add(hostLabel);
        container.add(hostInput);
        container.add(portLabel);
        container.add(portInput);
        container.add(loginLabel);
        container.add(loginInput);
        container.add(passwordLabel);
        container.add(passwordInput);
        container.add(signInButton);
        container.add(regButton);
    }

    @Override
    public void refreshLanguage(Locale locale) {
        ResourceBundle rb = ResourceBundle.getBundle("i18n/locales", locale, new UTF8Control());
        signInButton.setText(rb.getString("signIn"));
        regButton.setText(rb.getString("reg"));
        loginLabel.setText(rb.getString("enterLogin"));
        passwordLabel.setText(rb.getString("password"));
    }
}