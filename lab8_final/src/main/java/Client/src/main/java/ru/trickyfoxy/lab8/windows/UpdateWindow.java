package ru.trickyfoxy.lab8.windows;

import ru.trickyfoxy.lab8.collection.Coordinates;
import ru.trickyfoxy.lab8.collection.LocationFrom;
import ru.trickyfoxy.lab8.collection.LocationTo;
import ru.trickyfoxy.lab8.collection.Route;
import ru.trickyfoxy.lab8.utils.LanguageSwitchble;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class UpdateWindow extends JDialog implements LanguageSwitchble {
    private static volatile UpdateWindow instance;

    private final JButton updateButton = new JButton(LocaleMenu.getBundle().getString("update"));

    private Long elementId = null;

    private final JLabel idLabel = new JLabel("Id");
    private final JTextField idInput = new JTextField("", 5);

    private final JLabel nameLabel = new JLabel("Name");
    private final JTextField name = new JTextField("", 5);
    private final JLabel coordinateLabelX = new JLabel("Coordinate X");
    private final JLabel coordinateLabelY = new JLabel("Coordinate Y");
    private final JTextField coordinateX = new JTextField("", 5);
    private final JTextField coordinateY = new JTextField("", 5);
    private final JLabel dateLabel = new JLabel("Date");
    private final JTextField date = new JTextField("", 5);
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


    private UpdateWindow() {
        this.setBounds(300, 400, 350, 450);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        updateButton.addActionListener(e -> {
            Route route = new Route();
            List<String> errors = new ArrayList<>();
            try {
                route = new Route(
                        elementId,
                        name.getText(),
                        new Coordinates(Float.parseFloat(coordinateX.getText()), Long.parseLong(coordinateY.getText())),
                        new Date(),
                        new LocationFrom(Integer.parseInt(fromX.getText()), Double.parseDouble(fromY.getText()), Float.parseFloat(fromZ.getText()), fromName.getText()),
                        new LocationTo(Integer.parseInt(toX.getText()), Integer.parseInt(toY.getText()), toName.getText()),
                        Float.parseFloat(distance.getText())
                );
            } catch (NumberFormatException exception) {
                errors.add(LocaleMenu.getBundle().getString("wrongValues"));
            }
            System.out.println(errors);
            if (!errors.isEmpty()) {
                InformationWindow.getInstance().setVisible(true);
                InformationWindow.getInstance().setAlert("<html>" + String.join("<br>", errors) + "</html>");
            }
            if (MainWindow.getInstance().updateElement(route, elementId)) {
                MainWindow.getInstance().refreshElements();
                this.setVisible(false);
            } else {
                InformationWindow.getInstance().setAlert("<html>" + String.join("<br>", errors) + "<br><br>" + LocaleMenu.getBundle().getString("notUpdated") + "</html>");
                InformationWindow.getInstance().setVisible(true);
            }
        });

        Container main = this.getContentPane();
        main.setLayout(new BorderLayout());

        Container obligatoryFields = new Container();
        obligatoryFields.setLayout(new GridLayout(16, 2, 2, 2));

        obligatoryFields.add(idLabel);
        obligatoryFields.add(idInput);
        obligatoryFields.add(nameLabel);
        obligatoryFields.add(name);

        obligatoryFields.add(coordinateLabelX);
        obligatoryFields.add(coordinateX);
        obligatoryFields.add(coordinateLabelY);
        obligatoryFields.add(coordinateY);

        obligatoryFields.add(dateLabel);
        obligatoryFields.add(date);

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

        date.setEnabled(false);


        Container container1 = new Container();
        container1.setLayout(new FlowLayout(FlowLayout.RIGHT));
        container1.add(updateButton);

        main.add(obligatoryFields, BorderLayout.NORTH);
        main.add(container1, BorderLayout.SOUTH);
    }

    public static UpdateWindow getInstance() {
        UpdateWindow result = instance;
        if (result != null) {
            return result;
        }
        synchronized (UpdateWindow.class) {
            if (instance == null) {
                instance = new UpdateWindow();
            }
            return instance;
        }
    }

    public void setIdInput(String id) {
        this.idInput.setText(id);
        this.elementId = Long.parseLong(id);
        for (Route route : MainWindow.getInstance().getTableModel().getRoutes()) {
            if (route.getId() == Long.parseLong(id)) {
                idInput.setText(String.valueOf(route.getId()));
                name.setText(route.getName());
                coordinateX.setText(String.valueOf(route.getCoordinates().getX()));
                coordinateY.setText(String.valueOf(route.getCoordinates().getY()));
                date.setText(route.getCreationDate().toString());
                fromX.setText(String.valueOf(route.getFrom().getX()));
                fromY.setText(String.valueOf(route.getFrom().getY()));
                fromZ.setText(String.valueOf(route.getFrom().getZ()));
                fromName.setText(route.getFrom().getName());
                toX.setText(String.valueOf(route.getTo().getX()));
                toY.setText(String.valueOf(route.getTo().getY()));
                toName.setText(route.getTo().getName());
                distance.setText(String.valueOf(route.getDistance()));
                break;
            }
        }
    }

    public void disableStaticField() {
        idInput.setEnabled(false);
    }

    public void enableStaticField() {
        idInput.setEnabled(true);
    }

    @Override
    public void refreshLanguage(Locale locale) {
        ResourceBundle rb = ResourceBundle.getBundle("i18n/locales", locale);
        updateButton.setText(rb.getString("update"));
    }
}