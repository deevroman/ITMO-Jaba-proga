package ru.trickyfoxy.lab8.windows;

import ru.trickyfoxy.lab8.utils.LanguageSwitchble;
import ru.trickyfoxy.lab8.utils.UTF8Control;
import ru.trickyfoxy.lab8.collection.Coordinates;
import ru.trickyfoxy.lab8.collection.LocationFrom;
import ru.trickyfoxy.lab8.collection.LocationTo;
import ru.trickyfoxy.lab8.collection.Route;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class AddWindow extends JDialog  implements LanguageSwitchble {
    private static volatile AddWindow instance;

    private final JButton addButton = new JButton(LocaleMenu.getBundle().getString("add"));
    private final JButton addIfMax = new JButton(LocaleMenu.getBundle().getString("add_if_max"));
    private final JButton addIfMin = new JButton(LocaleMenu.getBundle().getString("add_if_min"));

    private final JLabel nameLabel = new JLabel("Name");
    private final JTextField name = new JTextField("", 5);
    private final JLabel coordinateLabelX = new JLabel("Coordinate X");
    private final JLabel coordinateLabelY = new JLabel("Coordinate Y");
    private final JTextField x = new JTextField("", 5);
    private final JTextField y = new JTextField("", 5);
    private final JLabel fromLabelX = new JLabel("From X");
    private final JLabel fromLabelY = new JLabel("From Y");
    private final JLabel fromLabelZ = new JLabel("From Z");
    private final JLabel fromLabelName = new JLabel("From name");
    private final JTextField fromX = new JTextField("", 5);
    private final JTextField fromY = new JTextField("", 5);
    private final JTextField fromZ = new JTextField("", 5);
    private final JTextField fromName = new JTextField("", 5);
    private final JLabel toLabelX = new JLabel("To X");
    private final JLabel toLabelY = new JLabel("To Y");
    private final JLabel toLabelName = new JLabel("To name");
    private final JTextField toX = new JTextField("", 5);
    private final JTextField toY = new JTextField("", 5);
    private final JTextField toName = new JTextField("", 5);
    private final JLabel distanceLabel = new JLabel("Distance");
    private final JTextField distance = new JTextField("", 5);


    private void addCommand(MainWindow.AddingType type) {
        Route route = new Route();
        List<String> errors = new ArrayList<>();
        try {
            route = new Route(
                    1L,
                    name.getText(),
                    new Coordinates(Float.parseFloat(x.getText()), Long.parseLong(y.getText())),
                    new Date(),
                    new LocationFrom(Integer.parseInt(fromX.getText()), Double.parseDouble(fromY.getText()), Float.parseFloat(fromZ.getText()), fromName.getText()),
                    new LocationTo(Integer.parseInt(toX.getText()), Integer.parseInt(toY.getText()), toName.getText()),
                    Long.parseLong(distance.getText())
            );
        } catch (NumberFormatException exception) {
            errors.add(LocaleMenu.getBundle().getString("wrongValues"));
        }
//        if (Route.checkNull()) {
//            errors.add(LocaleMenu.getBundle().getString("checkNull"));
//        }
        if (!errors.isEmpty()) {
            InformationWindow.getInstance().setVisible(true);
            InformationWindow.getInstance().setAlert("<html>" + String.join("<br>", errors) + "</html>");
        }
        if (MainWindow.getInstance().addElement(route, type)) {
            this.setVisible(false);
            MainWindow.getInstance().log("Элемент добавлен");
        } else {
            this.setVisible(false);
            MainWindow.getInstance().log("Элемент не добавлен");
        }
    }


    private AddWindow() {
        this.setBounds(300, 400, 300, 448);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        addButton.addActionListener(e -> addCommand(MainWindow.AddingType.ADD));
        addIfMin.addActionListener(e -> addCommand(MainWindow.AddingType.ADD_IF_MIN));
        addIfMax.addActionListener(e -> addCommand(MainWindow.AddingType.ADD_IF_MAX));

        Container main = this.getContentPane();
        main.setLayout(new BorderLayout());

        Container obligatoryFields = new Container();
        obligatoryFields.setLayout(new GridLayout(12, 2, 2, 2));
        obligatoryFields.add(nameLabel);
        obligatoryFields.add(name);

        obligatoryFields.add(coordinateLabelX);
        obligatoryFields.add(x);
        obligatoryFields.add(coordinateLabelY);
        obligatoryFields.add(y);

        obligatoryFields.add(fromLabelX);
        obligatoryFields.add(fromX);
        obligatoryFields.add(fromLabelY);
        obligatoryFields.add(fromY);
        obligatoryFields.add(fromLabelZ);
        obligatoryFields.add(fromZ);
        obligatoryFields.add(fromLabelName);
        obligatoryFields.add(fromName);

        obligatoryFields.add(toLabelX);
        obligatoryFields.add(toX);
        obligatoryFields.add(toLabelY);
        obligatoryFields.add(toY);
        obligatoryFields.add(toLabelName);
        obligatoryFields.add(toName);
        obligatoryFields.add(distanceLabel);
        obligatoryFields.add(distance);


        Container container1 = new Container();
        container1.setLayout(new GridLayout(3, 1));
        container1.add(addButton);
        container1.add(addIfMax);
        container1.add(addIfMin);

        main.add(obligatoryFields, BorderLayout.NORTH);
        main.add(container1, BorderLayout.SOUTH);
    }

    public static AddWindow getInstance() {
        AddWindow result = instance;
        if (result != null) {
            return result;
        }
        synchronized (AddWindow.class) {
            if (instance == null) {
                instance = new AddWindow();
            }
            return instance;
        }
    }

    public void refreshLanguage(Locale locale) {
        ResourceBundle rb = ResourceBundle.getBundle("i18n/locales", locale, new UTF8Control());
        addButton.setText(rb.getString("add"));
        addIfMin.setText(rb.getString("add_if_min"));
        addIfMax.setText(rb.getString("add_if_max"));
    }
}