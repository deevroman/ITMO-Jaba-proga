package ru.trickyfoxy.lab8;


import ru.trickyfoxy.lab8.windows.LoginWindow;
import ru.trickyfoxy.lab8.windows.MainWindow;

import java.awt.*;

public class ClientApp {
    public static void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    public static void main(String[] args) {
        LoginWindow.getInstance().setVisible(true);
    }
}