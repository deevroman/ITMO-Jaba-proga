package ru.trickyfoxy.lab8.windows;

import ru.trickyfoxy.lab8.Client;
import ru.trickyfoxy.lab8.utils.TableModel;
import ru.trickyfoxy.lab8.collection.Route;
import ru.trickyfoxy.lab8.commands.*;
import ru.trickyfoxy.lab8.exceptions.TimeoutConnectionException;
import ru.trickyfoxy.lab8.graphics.GraphPanel;
import ru.trickyfoxy.lab8.utils.LanguageSwitchble;
import ru.trickyfoxy.lab8.utils.ServerAnswer;
import ru.trickyfoxy.lab8.utils.ServerAnswerStatus;
import ru.trickyfoxy.lab8.utils.UTF8Control;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainWindow extends JFrame implements LanguageSwitchble {
    private static MainWindow instance;

    public String getLogin() {
        return login;
    }

    private static JLabel username;
    private final JTable table;

    public TableModel getTableModel() {
        return tableModel;
    }

    private final TableModel tableModel;
    private final JMenu fileMenu;
    private final JMenu localeMenu;
    private final JMenu helpMenu;
    JMenuItem exitItem;
    JMenuItem logoutItem;
    private final JButton addButton;
    private final JButton clear;
    private final JButton executeScript;
    private final JButton info;
    private final JButton removeById;
    private final JButton removeByDistance;
    private final JButton countGreaterThanDistanceButton;
    private final JButton updateId;
    private final JButton show;
    private final JLabel logTextPane;
    private final GraphPanel graphPanel;


    private Boolean boolId = false;
    private Boolean boolName = false;
    private Boolean boolX = false;
    private Boolean boolY = false;
    private Boolean boolDate = false;
    private Boolean fromX = false;
    private Boolean fromY = false;
    private Boolean fromZ = false;
    private Boolean fromName = false;
    private Boolean toX = false;
    private Boolean toY = false;
    private Boolean toName = false;
    private Boolean boolDistance = false;
    private Boolean boolCreator = false;

    private String login;
    private String pass;

    public void log(String str) {
        logTextPane.setText(str + "\n"); // todo время
    }

    public MainWindow() throws HeadlessException {
        Locale locale = Locale.getDefault();
        ResourceBundle rb = ResourceBundle.getBundle("i18n/locales", locale, new UTF8Control());

        Container outerContainer = this.getContentPane();
        outerContainer.setLayout(new BorderLayout());
        Container container = new Container();
        container.setLayout(new BorderLayout());
        username = new JLabel("");
        container.add(username, BorderLayout.NORTH);


        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        fileMenu = new JMenu(rb.getString("menuFile"));

        localeMenu = new LocaleMenu(rb.getString("localeMenu"));
        fileMenu.add(localeMenu);

        logoutItem = new JMenuItem(rb.getString("menuLogOut"));
        logoutItem.addActionListener(event -> {
            MainWindow.getInstance().refresh(new ArrayList<>());
            MainWindow.getInstance().setLogin("", "");
            LoginWindow.getInstance().setVisible(true);
        });
        fileMenu.add(logoutItem);


        exitItem = new JMenuItem(rb.getString("menuExit"));
        exitItem.addActionListener(event -> System.exit(0));
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);

        helpMenu = new JMenu(rb.getString("helpMenu"));
        JMenuItem helpItem = new JMenuItem(rb.getString("helpMenu"));
        helpItem.addActionListener(event -> {
            InformationWindow.getInstance().setAlert(rb.getString("help"));
            InformationWindow.getInstance().setVisible(true);
        });
        helpMenu.add(helpItem);
        menuBar.add(helpMenu);


        tableModel = new TableModel();

        table = new JTable(tableModel);
        table.setVisible(true);

        Container tableCont = new Container();
        tableCont.setLayout(new BorderLayout());
        Container filterCont = new Container();
        filterCont.setLayout(new GridLayout(1, 13));

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField xField = new JTextField();
        JTextField yField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField fromXField = new JTextField();
        JTextField fromYField = new JTextField();
        JTextField fromZField = new JTextField();
        JTextField fromNameField = new JTextField();
        JTextField toXField = new JTextField();
        JTextField toYField = new JTextField();
        JTextField toNameField = new JTextField();
        JTextField distanceField = new JTextField();
        JTextField creatorField = new JTextField();

        filterCont.add(idField);
        filterCont.add(nameField);
        filterCont.add(xField);
        filterCont.add(yField);
        filterCont.add(dateField);
        filterCont.add(fromXField);
        filterCont.add(fromYField);
        filterCont.add(fromZField);
        filterCont.add(fromNameField);
        filterCont.add(toXField);
        filterCont.add(toYField);
        filterCont.add(toNameField);
        filterCont.add(distanceField);
        filterCont.add(creatorField);

        logTextPane = new JLabel();

        idField.addActionListener(e -> {
            try {
                if (!idField.getText().equals("")) {
                    String id = idField.getText();
                    ArrayList<Route> routes = Client.getInstance().getAllElements().stream().filter(Route -> String.valueOf(Route.getId()).contains(id)).collect(Collectors.toCollection(ArrayList::new));
                    refresh(routes);
                } else {
                    refresh(Client.getInstance().getAllElements());
                }
            } catch (Exception exception) {
                InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("wrongValues"));
                InformationWindow.getInstance().setVisible(true);
            }
        });
        nameField.addActionListener(e -> {
            try {
                if (!nameField.getText().equals("")) {
                    String name = nameField.getText();
                    ArrayList<Route> routes = Client.getInstance().getAllElements().stream().filter(Route -> String.valueOf(Route.getName()).contains(name)).collect(Collectors.toCollection(ArrayList::new));
                    refresh(routes);
                } else {
                    refresh(Client.getInstance().getAllElements());
                }
            } catch (Exception exception) {
                InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("wrongValues"));
                InformationWindow.getInstance().setVisible(true);
            }
        });
        xField.addActionListener(e -> {
            try {
                if (!xField.getText().equals("")) {
                    String x = xField.getText();
                    ArrayList<Route> routes = Client.getInstance().getAllElements().stream().filter(Route -> String.valueOf(Route.getCoordinates().getX()).contains(x)).collect(Collectors.toCollection(ArrayList::new));
                    refresh(routes);
                } else {
                    refresh(Client.getInstance().getAllElements());
                }
            } catch (Exception exception) {
                InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("wrongValues"));
                InformationWindow.getInstance().setVisible(true);
            }
        });
        yField.addActionListener(e -> {
            try {
                if (!yField.getText().equals("")) {
                    String y = yField.getText();
                    ArrayList<Route> routes = Client.getInstance().getAllElements().stream().filter(Route -> String.valueOf(Route.getCoordinates().getY()).contains(y)).collect(Collectors.toCollection(ArrayList::new));
                    refresh(routes);
                } else {
                    refresh(Client.getInstance().getAllElements());
                }
            } catch (Exception exception) {
                InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("wrongValues"));
                InformationWindow.getInstance().setVisible(true);
            }
        });
        dateField.addActionListener(e -> {
            try {
                if (!dateField.getText().equals("")) {
                    String date = dateField.getText();
                    ArrayList<Route> routes = Client.getInstance().getAllElements().stream().filter(Route -> String.valueOf(Route.getCreationDate()).contains(date)).collect(Collectors.toCollection(ArrayList::new));
                    refresh(routes);
                } else {
                    refresh(Client.getInstance().getAllElements());
                }
            } catch (Exception exception) {
                InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("wrongValues"));
                InformationWindow.getInstance().setVisible(true);
            }
        });
        fromXField.addActionListener(e -> {
            try {
                if (!fromXField.getText().equals("")) {
                    String fromX = fromXField.getText();
                    ArrayList<Route> routes = Client.getInstance().getAllElements().stream().filter(Route -> String.valueOf(Route.getFrom().getX()).contains(fromX)).collect(Collectors.toCollection(ArrayList::new));
                    refresh(routes);
                } else {
                    refresh(Client.getInstance().getAllElements());
                }
            } catch (Exception exception) {
                InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("wrongValues"));
                InformationWindow.getInstance().setVisible(true);
            }
        });
        fromYField.addActionListener(e -> {
            try {
                if (!fromYField.getText().equals("")) {
                    String fromY = fromYField.getText();
                    ArrayList<Route> routes = Client.getInstance().getAllElements().stream().filter(Route -> String.valueOf(Route.getFrom().getY()).contains(fromY)).collect(Collectors.toCollection(ArrayList::new));
                    refresh(routes);
                } else {
                    refresh(Client.getInstance().getAllElements());
                }
            } catch (Exception exception) {
                InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("wrongValues"));
                InformationWindow.getInstance().setVisible(true);
            }
        });
        fromZField.addActionListener(e -> {
            try {
                if (!fromZField.getText().equals("")) {
                    String fromZ = fromZField.getText();
                    ArrayList<Route> routes = Client.getInstance().getAllElements().stream().filter(Route -> String.valueOf(Route.getFrom().getZ()).contains(fromZ)).collect(Collectors.toCollection(ArrayList::new));
                    refresh(routes);
                } else {
                    refresh(Client.getInstance().getAllElements());
                }
            } catch (Exception exception) {
                InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("wrongValues"));
                InformationWindow.getInstance().setVisible(true);
            }
        });
        fromNameField.addActionListener(e -> {
            try {
                if (!fromNameField.getText().equals("")) {
                    String fromName = fromNameField.getText();
                    ArrayList<Route> routes = Client.getInstance().getAllElements().stream().filter(Route -> String.valueOf(Route.getFrom().getName()).contains(fromName)).collect(Collectors.toCollection(ArrayList::new));
                    refresh(routes);
                } else {
                    refresh(Client.getInstance().getAllElements());
                }
            } catch (Exception exception) {
                InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("wrongValues"));
                InformationWindow.getInstance().setVisible(true);
            }
        });
        toXField.addActionListener(e -> {
            try {
                if (!toXField.getText().equals("")) {
                    String toX = toXField.getText();
                    ArrayList<Route> routes = Client.getInstance().getAllElements().stream().filter(Route -> String.valueOf(Route.getTo().getX()).contains(toX)).collect(Collectors.toCollection(ArrayList::new));
                    refresh(routes);
                } else {
                    refresh(Client.getInstance().getAllElements());
                }
            } catch (Exception exception) {
                InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("wrongValues"));
                InformationWindow.getInstance().setVisible(true);
            }
        });
        toYField.addActionListener(e -> {
            try {
                if (!toYField.getText().equals("")) {
                    String toY = toYField.getText();
                    ArrayList<Route> routes = Client.getInstance().getAllElements().stream().filter(Route -> String.valueOf(Route.getFrom().getY()).contains(toY)).collect(Collectors.toCollection(ArrayList::new));
                    refresh(routes);
                } else {
                    refresh(Client.getInstance().getAllElements());
                }
            } catch (Exception exception) {
                InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("wrongValues"));
                InformationWindow.getInstance().setVisible(true);
            }
        });
        toNameField.addActionListener(e -> {
            try {
                if (!toNameField.getText().equals("")) {
                    String toName = toNameField.getText();
                    ArrayList<Route> routes = Client.getInstance().getAllElements().stream().filter(Route -> String.valueOf(Route.getFrom().getName()).contains(toName)).collect(Collectors.toCollection(ArrayList::new));
                    refresh(routes);
                } else {
                    refresh(Client.getInstance().getAllElements());
                }
            } catch (Exception exception) {
                InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("wrongValues"));
                InformationWindow.getInstance().setVisible(true);
            }
        });
        distanceField.addActionListener(e -> {
            try {
                if (!distanceField.getText().equals("")) {
                    String distance = distanceField.getText();
                    ArrayList<Route> routes = Client.getInstance().getAllElements().stream().filter(Route -> String.valueOf(Route.getDistance()).contains(distance)).collect(Collectors.toCollection(ArrayList::new));
                    refresh(routes);
                } else {
                    refresh(Client.getInstance().getAllElements());
                }
            } catch (Exception exception) {
                InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("wrongValues"));
                InformationWindow.getInstance().setVisible(true);
            }
        });
        creatorField.addActionListener(e -> {
            try {
                if (!distanceField.getText().equals("")) {
                    String creator = distanceField.getText();
                    ArrayList<Route> routes = Client.getInstance().getAllElements().stream().filter(Route -> String.valueOf(Route.getDistance()).contains(creator)).collect(Collectors.toCollection(ArrayList::new));
                    refresh(routes);
                } else {
                    refresh(Client.getInstance().getAllElements());
                }
            } catch (Exception exception) {
                InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("wrongValues"));
                InformationWindow.getInstance().setVisible(true);
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 1));
        panel.setPreferredSize(new Dimension(1300, 200));
        panel.add(new JScrollPane(table));
        tableCont.add(panel, BorderLayout.CENTER);
        tableCont.add(filterCont, BorderLayout.SOUTH);

        JPanel panel1 = new JPanel();
        panel1.setPreferredSize(new Dimension(1300, 230));
        panel1.setLayout(new GridLayout(1, 1));
        panel1.add(tableCont);

        container.add(panel1, BorderLayout.SOUTH);

        addButton = new JButton(rb.getString("add"));
        clear = new JButton(rb.getString("clear"));
        executeScript = new JButton(rb.getString("execute_script"));
        info = new JButton(rb.getString("info"));
        removeById = new JButton(rb.getString("remove_by_id"));
        removeByDistance = new JButton(rb.getString("remove_greater"));
        countGreaterThanDistanceButton = new JButton(rb.getString("countGreaterThanDistanceButton"));
        updateId = new JButton(rb.getString("update_id"));
        show = new JButton(rb.getString("show"));


        Container buttons = new Container();
        buttons.setLayout(new GridLayout(12, 1, 2, 2));
        buttons.setVisible(true);
        buttons.add(addButton);
        buttons.add(clear);
        buttons.add(executeScript);
        buttons.add(info);
        buttons.add(removeById);
        buttons.add(removeByDistance);
        buttons.add(countGreaterThanDistanceButton);
        buttons.add(updateId);
        buttons.add(show);
        buttons.add(logTextPane);

        graphPanel = new GraphPanel();

        container.add(buttons, BorderLayout.WEST);
        container.add(graphPanel, BorderLayout.CENTER);
        outerContainer.add(container, BorderLayout.CENTER);
        outerContainer.add(new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(20, 1);
            }
        }, BorderLayout.WEST);
        outerContainer.add(new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(20, 1);
            }
        }, BorderLayout.EAST);
        outerContainer.add(new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(1, 1);
            }
        }, BorderLayout.SOUTH);
        this.setBounds(0, 0, 1480, 920);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        addButton.addActionListener(e -> AddWindow.getInstance().setVisible(true));
        clear.addActionListener(e -> {
            if (MainWindow.getInstance().clearElements()) {
                InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("cleared"));
                InformationWindow.getInstance().setVisible(true);
            } else {
                InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("cannot"));
                InformationWindow.getInstance().setVisible(true);
            }
        });
        info.addActionListener(e -> getInfoAboutCollection());
        updateId.addActionListener(e -> UpdateWindow.getInstance().setVisible(true));

        show.addActionListener(e -> {
            refreshElements();
            log("Обновлено");
        });
        executeScript.addActionListener(e -> ExecuteScriptWindow.getInstance().setVisible(true));
        removeById.addActionListener(e -> RemoveByIdWindow.getInstance().setVisible(true));
        removeByDistance.addActionListener(e -> RemoveByDistanceWindow.getInstance().setVisible(true));
        countGreaterThanDistanceButton.addActionListener(e -> CountGreaterThanDistanceWindow.getInstance().setVisible(true));

        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = table.columnAtPoint(e.getPoint());
                String name = table.getColumnName(col);
                ArrayList<Route> routes = new ArrayList<>();
                switch (name) {
                    case "id":
                        if (boolId)
                            routes = tableModel.getRoutes().stream().sorted(Comparator.comparing(Route::getId)).collect(Collectors.toCollection(ArrayList::new));
                        else
                            routes = tableModel.getRoutes().stream().sorted(Comparator.comparing(Route::getId).reversed()).collect(Collectors.toCollection(ArrayList::new));
                        boolId = !boolId;
                        break;
                    case "name":
                        if (boolName)
                            routes = tableModel.getRoutes().stream().sorted(Comparator.comparing(Route::getName)).collect(Collectors.toCollection(ArrayList::new));
                        else
                            routes = tableModel.getRoutes().stream().sorted(Comparator.comparing(Route::getName).reversed()).collect(Collectors.toCollection(ArrayList::new));
                        boolName = !boolName;
                        break;
                    case "coordinates_x":
                        if (boolX)
                            routes = tableModel.getRoutes().stream().sorted(Comparator.comparing((Route o) -> o.getCoordinates().getX())).collect(Collectors.toCollection(ArrayList::new));
                        else
                            routes = tableModel.getRoutes().stream().sorted(Comparator.comparing((Route o) -> o.getCoordinates().getX()).reversed()).collect(Collectors.toCollection(ArrayList::new));
                        boolX = !boolX;
                        break;
                    case "coordinates_y":
                        if (boolY)
                            routes = tableModel.getRoutes().stream().sorted(Comparator.comparing((Route o) -> o.getCoordinates().getY())).collect(Collectors.toCollection(ArrayList::new));
                        else
                            routes = tableModel.getRoutes().stream().sorted(Comparator.comparing((Route o) -> o.getCoordinates().getY()).reversed()).collect(Collectors.toCollection(ArrayList::new));
                        boolY = !boolY;
                        break;
                    case "creationDate":
                        if (boolDate)
                            routes = tableModel.getRoutes().stream().sorted(Comparator.comparing(Route::getCreationDate)).collect(Collectors.toCollection(ArrayList::new));
                        else
                            routes = tableModel.getRoutes().stream().sorted(Comparator.comparing(Route::getCreationDate).reversed()).collect(Collectors.toCollection(ArrayList::new));
                        boolDate = !boolDate;
                        break;

                    case "locationFrom_x":
                        if (fromX)
                            routes = tableModel.getRoutes().stream().sorted(Comparator.comparing((Route o) -> o.getFrom().getX())).collect(Collectors.toCollection(ArrayList::new));
                        else
                            routes = tableModel.getRoutes().stream().sorted(Comparator.comparing((Route o) -> o.getFrom().getX()).reversed()).collect(Collectors.toCollection(ArrayList::new));
                        fromX = !fromX;
                        break;
                    case "locationFrom_y":
                        if (fromY)
                            routes = tableModel.getRoutes().stream().sorted(Comparator.comparing((Route o) -> o.getFrom().getY())).collect(Collectors.toCollection(ArrayList::new));
                        else
                            routes = tableModel.getRoutes().stream().sorted(Comparator.comparing((Route o) -> o.getFrom().getY()).reversed()).collect(Collectors.toCollection(ArrayList::new));
                        fromY = !fromY;
                        break;
                    case "locationFrom_z":
                        if (fromZ)
                            routes = tableModel.getRoutes().stream().sorted(Comparator.comparing((Route o) -> o.getFrom().getZ())).collect(Collectors.toCollection(ArrayList::new));
                        else
                            routes = tableModel.getRoutes().stream().sorted(Comparator.comparing((Route o) -> o.getFrom().getZ()).reversed()).collect(Collectors.toCollection(ArrayList::new));
                        fromZ = !fromZ;
                        break;
                    case "locationFrom_name":
                        if (fromName)
                            routes = tableModel.getRoutes().stream().sorted(Comparator.comparing((Route o) -> o.getFrom().getName())).collect(Collectors.toCollection(ArrayList::new));
                        else
                            routes = tableModel.getRoutes().stream().sorted(Comparator.comparing((Route o) -> o.getFrom().getName()).reversed()).collect(Collectors.toCollection(ArrayList::new));
                        fromName = !fromName;
                        break;
                    case "locationTo_x":
                        if (toX)
                            routes = tableModel.getRoutes().stream().sorted(Comparator.comparing((Route o) -> o.getTo().getX())).collect(Collectors.toCollection(ArrayList::new));
                        else
                            routes = tableModel.getRoutes().stream().sorted(Comparator.comparing((Route o) -> o.getTo().getX()).reversed()).collect(Collectors.toCollection(ArrayList::new));
                        toX = !toX;
                        break;
                    case "locationTo_y":
                        if (toY)
                            routes = tableModel.getRoutes().stream().sorted(Comparator.comparing((Route o) -> o.getTo().getY())).collect(Collectors.toCollection(ArrayList::new));
                        else
                            routes = tableModel.getRoutes().stream().sorted(Comparator.comparing((Route o) -> o.getTo().getY()).reversed()).collect(Collectors.toCollection(ArrayList::new));
                        toY = !toY;
                        break;
                    case "locationTo_name":
                        if (toName)
                            routes = tableModel.getRoutes().stream().sorted(Comparator.comparing((Route o) -> o.getTo().getName())).collect(Collectors.toCollection(ArrayList::new));
                        else
                            routes = tableModel.getRoutes().stream().sorted(Comparator.comparing((Route o) -> o.getTo().getName()).reversed()).collect(Collectors.toCollection(ArrayList::new));
                        toName = !toName;
                        break;
                    case "distance":
                        if (boolDistance)
                            routes = tableModel.getRoutes().stream().sorted(Comparator.comparing(Route::getDistance)).collect(Collectors.toCollection(ArrayList::new));
                        else
                            routes = tableModel.getRoutes().stream().sorted(Comparator.comparing(Route::getDistance).reversed()).collect(Collectors.toCollection(ArrayList::new));
                        boolDistance = !boolDistance;
                        break;
                    case "creator":
                        if (boolCreator)
                            routes = tableModel.getRoutes().stream().sorted(Comparator.comparing(Route::getCreator)).collect(Collectors.toCollection(ArrayList::new));
                        else
                            routes = tableModel.getRoutes().stream().sorted(Comparator.comparing(Route::getCreator).reversed()).collect(Collectors.toCollection(ArrayList::new));
                        boolCreator = !boolCreator;
                        break;
                }
                refresh(routes);
            }
        });
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = table.rowAtPoint(e.getPoint());
                    Route remove = tableModel.getRoutes().get(row);
                    if (MainWindow.getInstance().removeElement(remove.getId(), RemovingType.REMOVE)) {
                        log(LocaleMenu.getBundle().getString("deleted"));
                    } else {
                        InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("notDeleted"));
                        InformationWindow.getInstance().setVisible(true);
                    }
                }
            }
        });
    }


    public static MainWindow getInstance() {
        if (instance == null) {
            instance = new MainWindow();
        }
        return instance;
    }


    public void setLogin(String login, String pass) {
        log("Вы успешно зашли");
        Color color = new Color(login.hashCode() * login.hashCode() * login.hashCode());
        String hex = Integer.toHexString(color.getRGB() & 0xffffff);
        if (hex.length() < 6) {
            hex = "0" + hex;
        }
        hex = "#" + hex;
        username.setText("<html>" + login + " <font color='" + hex + "'>■</font>" + "</html>");
        this.login = login;
        this.pass = pass;
    }

    public void refreshElements() {
        try {
            ArrayList<Route> allElements = Client.getInstance().getAllElements();
            refresh(allElements);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum AddingType {
        ADD,
        ADD_IF_MIN,
        ADD_IF_MAX
    }

    public boolean addElement(Route r, AddingType type) {
        log("");
        Command cmd;
        switch (type) {
            case ADD:
                cmd = new Add();
                break;
            case ADD_IF_MIN:
                cmd = new AddIfMin();
                break;
            case ADD_IF_MAX:
                cmd = new AddIfMax();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        cmd.setRoute(r);
        try {
            Client.getInstance().connector.reconnect();
            Client.getInstance().connector.sendCommand(cmd);
            ServerAnswer answer = (ServerAnswer) Client.getInstance().connector.receiver();
            if (answer.status == ServerAnswerStatus.OK) {
                return true;
            } else {
                log(answer.message);
                return false;
            }
        } catch (IOException | TimeoutConnectionException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateElement(Route r, Long id) {
        log("");
        Update update = new Update();
        update.setRoute(r);
        update.setArgument(String.valueOf(id));
        try {
            Client.getInstance().connector.reconnect();
            Client.getInstance().connector.sendCommand(update);
            ServerAnswer answer = (ServerAnswer) Client.getInstance().connector.receiver();
            if (answer.status == ServerAnswerStatus.OK) {
                return true;
            } else {
                log(answer.message);
                return false;
            }
        } catch (IOException | TimeoutConnectionException e) {
            e.printStackTrace();
        }
        return false;
    }

    public enum RemovingType {
        REMOVE,
        REMOVE_ANY_BY_DISTANCE,
        REMOVE_GREATER
    }

    public boolean removeElement(Long arg, RemovingType type) {
        log("");
        Command cmd;
        switch (type) {
            case REMOVE:
                cmd = new RemoveById();
                break;
            case REMOVE_ANY_BY_DISTANCE:
                cmd = new RemoveAnyByDistance();
                break;
            case REMOVE_GREATER:
                cmd = new RemoveGreater();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        cmd.setArgument(String.valueOf(arg));
        try {
            Client.getInstance().connector.reconnect();
            Client.getInstance().connector.sendCommand(cmd);
            ServerAnswer answer = (ServerAnswer) Client.getInstance().connector.receiver();
            if (answer.status == ServerAnswerStatus.OK) {
                return true;
            } else {
                log(answer.message);
                return false;
            }
        } catch (IOException | TimeoutConnectionException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean clearElements() {
        log("");
        Command cmd = new Clear();
        try {
            Client.getInstance().connector.reconnect();
            Client.getInstance().connector.sendCommand(cmd);
            ServerAnswer answer = (ServerAnswer) Client.getInstance().connector.receiver();
            if (answer.status == ServerAnswerStatus.OK) {
                return true;
            } else {
                log(answer.message);
                return false;
            }
        } catch (IOException | TimeoutConnectionException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void getInfoAboutCollection() {
        log("");
        Command cmd = new Info();
        try {
            Client.getInstance().connector.reconnect();
            Client.getInstance().connector.sendCommand(cmd);
            ServerAnswer answer = (ServerAnswer) Client.getInstance().connector.receiver();
            if (answer.status == ServerAnswerStatus.OK) {
                String[] split = answer.message.split("\n");
                InformationWindow.getInstance().setAlert("<html>" + LocaleMenu.getBundle().getString("dateOfCreation") + split[0] + "<br>"
                        + LocaleMenu.getBundle().getString("sizeOfStorage") + split[1] + "<br>"
                        + LocaleMenu.getBundle().getString("typeOfCollection") + split[2] + "</html>");
                InformationWindow.getInstance().setVisible(true);
            } else {
                log(answer.message);
            }
        } catch (IOException | TimeoutConnectionException e) {
            e.printStackTrace();
        }
    }

    public void getCountGreaterThanDistance(Float distance){
        log("");
        Command cmd = new CountGreaterThanDistance();
        cmd.setArgument(String.valueOf(distance));
        try {
            Client.getInstance().connector.reconnect();
            Client.getInstance().connector.sendCommand(cmd);
            ServerAnswer answer = (ServerAnswer) Client.getInstance().connector.receiver();
            if (answer.status == ServerAnswerStatus.OK) {
                InformationWindow.getInstance().setAlert("<html>" + LocaleMenu.getBundle().getString("countGreaterThanDistance") + answer.message + "</html>");
                InformationWindow.getInstance().setVisible(true);
            } else {
                log(answer.message);
            }
        } catch (IOException | TimeoutConnectionException e) {
            e.printStackTrace();
        }
    }

    public void refresh(ArrayList<Route> routes) {
        table.repaint();
        graphPanel.setRoutes(routes);
        tableModel.setRoutes(routes);
        tableModel.fireTableDataChanged();
        graphPanel.paintWithAnimation();
    }

    @Override
    public void refreshLanguage(Locale locale) {
        ResourceBundle rb = ResourceBundle.getBundle("i18n/locales", locale, new UTF8Control());
        fileMenu.setText(rb.getString("menuFile"));
        localeMenu.setText(rb.getString("localeMenu"));
        addButton.setText(rb.getString("add"));
        clear.setText(rb.getString("clear"));
        executeScript.setText(rb.getString("execute_script"));
        info.setText(rb.getString("info"));
        removeById.setText(rb.getString("remove_by_id"));
        removeByDistance.setText(rb.getString("remove_greater"));
        updateId.setText(rb.getString("update_id"));
        show.setText(rb.getString("show"));
        logoutItem.setText(rb.getString("menuLogOut"));
        exitItem.setText(rb.getString("menuExit"));
    }

}
