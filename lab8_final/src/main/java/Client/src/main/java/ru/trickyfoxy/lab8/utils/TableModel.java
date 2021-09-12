package ru.trickyfoxy.lab8.utils;

import ru.trickyfoxy.lab8.collection.Route;
import ru.trickyfoxy.lab8.windows.LocaleMenu;
import ru.trickyfoxy.lab8.windows.MainWindow;
import ru.trickyfoxy.lab8.windows.InformationWindow;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class TableModel extends AbstractTableModel {

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    private List<Route> routes;
    private final String[] columnsHeaders = new String[]{
            "id",
            "name",
            "coordinates_x",
            "coordinates_y",
            "creationDate",
            "locationFrom_x",
            "locationFrom_y",
            "locationFrom_z",
            "locationFrom_name",
            "locationTo_x",
            "locationTo_y",
            "locationTo_name",
            "distance",
            "creator"
    };

    @Override
    public String getColumnName(int column) {
        return columnsHeaders[column];
    }

    @Override
    public int getRowCount() {
        return routes != null ? routes.size() : 0;
    }

    @Override
    public int getColumnCount() {
        return columnsHeaders.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (routes.get(rowIndex) != null) {
            Route route = routes.get(rowIndex);
            Object[] object = new Object[]{
                    route.getId(),
                    route.getName(),
                    route.getCoordinates().getX(),
                    route.getCoordinates().getX(),
                    route.getCreationDate(),
                    route.getFrom().getX().toString(),
                    route.getFrom().getY().toString(),
                    route.getFrom().getZ().toString(),
                    route.getFrom().getName(),
                    route.getTo().getX().toString(),
                    route.getTo().getY().toString(),
                    route.getTo().getName(),
                    route.getDistance(),
                    route.getCreator()
            };
            return object[columnIndex];
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        List<String> errors = new ArrayList<>();
        Route route = routes.get(rowIndex);
        if (MainWindow.getInstance().getLogin().equals(route.getCreator())) {
            try {
                switch (columnIndex) {
                    // 0 skip
                    case 1:
                        route.setName((String) aValue);
                        break;
                    case 2:
                        route.getCoordinates().setX(Float.parseFloat((String) aValue));
                        break;
                    case 3:
                        route.getCoordinates().setY(Long.parseLong((String) aValue));
                        break;
                    //4 -- skip
                    case 5:
                        route.getFrom().setX(Integer.parseInt((String) aValue));
                        break;
                    case 6:
                        route.getFrom().setY(Double.parseDouble((String) aValue));
                        break;
                    case 7:
                        route.getFrom().setZ(Float.parseFloat((String) aValue));
                        break;
                    case 8:
                        route.getFrom().setName((String) aValue);
                        break;
                    case 9:
                        route.getTo().setX(Integer.parseInt((String) aValue));
                        break;
                    case 10:
                        route.getTo().setY(Integer.parseInt((String) aValue));
                        break;
                    case 11:
                        route.getTo().setName((String) aValue);
                        break;
                    case 12:
                        route.setDistance(Float.parseFloat((String) aValue));
                        break;
                    // 13 -- skip
                    // TODO
                }
            } catch (Exception exception) {
                errors.add(LocaleMenu.getBundle().getString("wrongValues"));
                exception.printStackTrace();
            }
        }

        if (!errors.isEmpty()) {
            InformationWindow.getInstance().setVisible(true);
            InformationWindow.getInstance().setAlert("<html>" + String.join("<br>", errors) + "</html>");
        }
        if (MainWindow.getInstance().updateElement(route, route.getId())) {
            MainWindow.getInstance().refreshElements();
        } else {
            InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("notUpdated"));
            InformationWindow.getInstance().setVisible(true);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return !(columnIndex == 0 || columnIndex == 4 || columnIndex == 13);
    }
}
