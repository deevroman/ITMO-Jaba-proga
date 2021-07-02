package ru.trickyfoxy.lab8.graphics;

import ru.trickyfoxy.lab8.windows.MainWindow;
import ru.trickyfoxy.lab8.windows.UpdateWindow;
import ru.trickyfoxy.lab8.windows.InformationWindow;
import ru.trickyfoxy.lab8.collection.Route;
import ru.trickyfoxy.lab8.windows.LocaleMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.*;
import static ru.trickyfoxy.lab8.graphics.Geomic.pointInSegment;

public class GraphPanel extends JPanel {

//    private Image img = null;
    private final Timer timer = new Timer(10, arg0 -> repaint());
    private final int padding = 25;
    private final int labelPadding = 25;
    private final Color gridColor = new Color(200, 200, 200, 200);
    private final Color backgroundColor = Color.WHITE;
    private int scaleTicks = 0;
    private java.util.List<Route> routes = new ArrayList<>();
    private final JPopupMenu menu;
    private final JMenuItem remove;
    private final JMenuItem update;
    Map<String, Color> colors;
    Map<Route, Shapes> lines;

    public void paintWithAnimation() {
        this.painting = true;
        repaint();
    }

    private boolean painting = false;

    public GraphPanel() {
//        try {
//            ClassLoader classLoader = getClass().getClassLoader();
//            img = ImageIO.read(classLoader.getResource("light.png"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        menu = new JPopupMenu();
        update = new JMenuItem(LocaleMenu.getBundle().getString("update"));
        remove = new JMenuItem(LocaleMenu.getBundle().getString("remove"));
        menu.add(update);
        menu.add(remove);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((double) getWidth() - 2 * padding - labelPadding) / (getMaxX() - getMinX());
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (getMaxY() - getMinY());

        // background
        g2.setColor(backgroundColor);
        g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding, getHeight() - 2 * padding - labelPadding);
        g2.setColor(Color.BLACK);

        // y axis
        int pointWidth = 4;
        int numberDivisions = 10;
        for (int i = 0; i < numberDivisions + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / numberDivisions + padding + labelPadding);
            int y1 = y0;
            if (routes.size() > 0) {
                g2.setColor(gridColor);
                g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
                g2.setColor(Color.BLACK);
                String yLabel = ((int) ((getMinY() + (getMaxY() - getMinY()) * ((i * 1.0) / numberDivisions)) * 100)) / 100.0 + "";
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }

        // x axis
        for (int i = 0; i < numberDivisions + 1; i++) {
            int x0 = i * (getWidth() - padding * 2 - labelPadding) / (numberDivisions) + padding + labelPadding;
            int x1 = x0;
            int y0 = getHeight() - padding - labelPadding;
            int y1 = y0 - pointWidth;
            if ((i % ((int) ((numberDivisions / 20.0)) + 1)) == 0) {
                g2.setColor(gridColor);
                g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
                g2.setColor(Color.BLACK);
                String xLabel = ((int) ((getMinX() + (getMaxX() - getMinX()) * ((i * 1.0) / numberDivisions)) * 100)) / 100.0 + "";
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(xLabel);
                g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }

        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);


        if (!painting) {
            colors = new HashMap<>();
            lines = new HashMap<>();
            for (Route route : routes) {
                double x1 = (route.getFrom().getX() - getMinX()) * xScale + padding + labelPadding;
                double y1 = (getMaxY() - route.getFrom().getY()) * yScale + padding;
                double x2 = (route.getTo().getX() - getMinX()) * xScale + padding + labelPadding;
                double y2 = (getMaxY() - route.getTo().getY()) * yScale + padding;

                String user = route.getCreator();
                if (!colors.containsKey(user)) {
                    colors.put(user, new Color(user.hashCode()*user.hashCode()*user.hashCode()));
                }
                g2.setColor(colors.get(user));

                lines.put(route, (new LineShape(g2, x1, y1, x2, y2)).process());
            }
        } else {
            startAnimation(g2);
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                super.mouseClicked(me);
                for (Map.Entry<Route, Shapes> pair : lines.entrySet()) {
                    if (pointInSegment(pair.getValue().getCenterX(),
                            pair.getValue().getCenterY(),
                            pair.getValue().getCenterX2(),
                            pair.getValue().getCenterY2(),
                            me.getPoint().x,
                            me.getPoint().y)) {
                        if (SwingUtilities.isLeftMouseButton(me)) {
                            InformationWindow.getInstance().setAlert(pair.getKey().printHTML());
                            InformationWindow.getInstance().setVisible(true);
                            InformationWindow.getInstance().pack();
                        } else {
                            for (ActionListener al : update.getActionListeners()) {
                                update.removeActionListener(al);
                            }
                            for (ActionListener al : remove.getActionListeners()) {
                                remove.removeActionListener(al);
                            }
                            update.addActionListener(e -> {
                                UpdateWindow.getInstance().setIdInput(String.valueOf(pair.getKey().getId()));
                                UpdateWindow.getInstance().disableStaticField();
                                UpdateWindow.getInstance().setVisible(true);
                            });
                            remove.addActionListener(e -> {
                                if (MainWindow.getInstance().removeElement(pair.getKey().getId(), MainWindow.RemovingType.REMOVE)) {
                                    MainWindow.getInstance().refreshElements();
                                } else {
                                    InformationWindow.getInstance().setAlert(LocaleMenu.getBundle().getString("notDeleted"));
                                    InformationWindow.getInstance().setVisible(true);
                                }
                            });
                            if (!menu.isVisible()) {
                                menu.show(me.getComponent(), me.getX(), me.getY());
                            }
                        }
                    }
                }
            }
        });
    }

    public void startAnimation(Graphics2D g2) {
        painting = true;
        timer.start();
//        double mx = sqrt(getWidth() * getWidth() + getHeight() * getHeight());
//        if (scaleTicks == 0) {
//            backgroundColor = Color.DARK_GRAY;
//        }
//        if (scaleTicks < getWidth() /*!backgroundColor.equals(Color.WHITE)*/) {
//            g2.drawImage(
//                    img.getScaledInstance((int) max(img.getWidth(null) - scaleTicks * 50.0, 1),
//                            (int) max(img.getHeight(null) - scaleTicks * 50.0, 1),
//                            Image.SCALE_FAST),
//                    x, 60 + scaleTicks * 5, null);
//            double width = mx - scaleTicks;
//            (new PointShape(g2, -width, (float) max((mx - width - padding), 0), (int) width)).process();
//            backgroundColor = new Color(min(backgroundColor.getRed() + 1, 255),
//                    min(backgroundColor.getGreen() + 1, 255),
//                    min(backgroundColor.getBlue() + 1, 255),
//                    backgroundColor.getAlpha());
//            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

//            int w = getWidth();
//            int h = getHeight();
//
//            int x_padding = padding + labelPadding;
//            int y_padding = padding;
//
//            w -= x_padding + 2 * padding;
//            h -= y_padding + labelPadding + padding;
//
//            Color color1 = new Color(200, 200, 200, 200);
//            Color color2 = backgroundColor;
//            GradientPaint gp = new GradientPaint(x_padding + scaleTicks, y_padding, color1, x_padding + w, 0, color2);
//            g2.setPaint(gp);
//            g2.fillRect(x_padding + scaleTicks, y_padding, w, h);
//
//            gp = new GradientPaint(x_padding, y_padding, color2, x_padding + scaleTicks, y_padding, color1);
//            g2.setPaint(gp);
//            g2.fillRect(x_padding, y_padding, scaleTicks, h);
//            scaleTicks += 30;

        double mx = 1000;
        if (scaleTicks > mx) {
            scaleTicks = (int) mx;
        }
        double xScale = ((double) getWidth() - 2 * padding - labelPadding) / (getMaxX() - getMinX());
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (getMaxY() - getMinY());

        colors = new HashMap<>();
        lines = new HashMap<>();

        for (Route route : routes) {
            double x1 = (route.getFrom().getX() - getMinX()) * xScale + padding + labelPadding;
            double y1 = (getMaxY() - route.getFrom().getY()) * yScale + padding;
            double x2 = (route.getTo().getX() - getMinX()) * xScale + padding + labelPadding;
            double y2 = (getMaxY() - route.getTo().getY()) * yScale + padding;


            String user = route.getCreator();
            if (!colors.containsKey(user)) {
                colors.put(user, new Color(user.hashCode()*user.hashCode()*user.hashCode()));
            }
            g2.setColor(colors.get(user));

            x2 = x1 + (x2 - x1) * scaleTicks / mx;
            y2 = y1 + (y2 - y1) * scaleTicks / mx;
            lines.put(route, (new LineShape(g2, x1, y1, x2, y2)).process());
        }
        scaleTicks += 30;
        if (scaleTicks >= mx) {
            painting = false;
            scaleTicks = 0;
            timer.stop();
        }
    }

    private Float getMinX() {
        float minX = 11.0f;
        if (routes.stream().map(v -> v.getFrom().getX()).min(Integer::compareTo).isPresent()) {
            minX = routes.stream().map(v -> v.getFrom().getX()).min(Integer::compareTo).get();
        }
        if (routes.stream().map(v -> v.getTo().getX()).min(Integer::compareTo).isPresent()) {
            minX = min(minX, routes.stream().map(v -> v.getTo().getX()).min(Integer::compareTo).get());
        }
        return minX - 10;
    }

    private Float getMaxX() {
        float maxX = -9.0f;
        if (routes.stream().map(v -> v.getFrom().getX()).max(Integer::compareTo).isPresent()) {
            maxX = routes.stream().map(v -> v.getFrom().getX()).max(Integer::compareTo).get();
        }
        if (routes.stream().map(v -> v.getTo().getX()).max(Integer::compareTo).isPresent()) {
            maxX = max(maxX, routes.stream().map(v -> v.getTo().getX()).max(Integer::compareTo).get());
        }
        return maxX + 10;
    }

    private float getMinY() {
        Double minY = 11.0;
        if (routes.stream().map(v -> v.getFrom().getY()).min(Double::compareTo).isPresent()) {
            minY = routes.stream().map(v -> v.getFrom().getY()).min(Double::compareTo).get();
        }
        if (routes.stream().map(v -> v.getTo().getY()).min(Integer::compareTo).isPresent()) {
            minY = min(minY, routes.stream().map(v -> v.getTo().getY()).min(Integer::compareTo).get());
        }
        return (float) (minY - 10);
    }

    private float getMaxY() {
        Double maxY = -9.0;
        if (routes.stream().map(v -> v.getFrom().getY()).max(Double::compareTo).isPresent()) {
            maxY = routes.stream().map(v -> v.getFrom().getY()).max(Double::compareTo).get();
        }
        if (routes.stream().map(v -> v.getTo().getY()).max(Integer::compareTo).isPresent()) {
            maxY = max(maxY, routes.stream().map(v -> v.getTo().getY()).max(Integer::compareTo).get());
        }
        return (float) (maxY + 10);
    }

    public void setRoutes(java.util.List<Route> routes) {
        this.routes = routes;
        invalidate();
        this.repaint();
    }

    public java.util.List<Route> getRoutes() {
        return routes;
    }

}
