package ru.trickyfoxy.lab8.windows;

import ru.trickyfoxy.lab8.utils.UTF8Control;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocaleMenu extends JMenu {
    private Locale locale;
    private static ResourceBundle bundle = ResourceBundle.getBundle("i18n/locales", Locale.getDefault(), new UTF8Control());

    public LocaleMenu(String s) {
        super(s);
        String[][] languages = new String[][]{
                {"Русский", "ru", "RU"},
                {"Norsk", "no", "NO"},
                {"Italiano", "it", "IT"},
                {"Español (Honduras)", "es", "HN"}
        };
        for (String[] language : languages) {
            JMenuItem curLang = new JMenuItem(language[0]);
            curLang.addActionListener((actionEvent) -> {
                locale = new Locale(language[1], language[2]);
                bundle = ResourceBundle.getBundle("i18n/locales", locale, new UTF8Control());
                MainWindow.getInstance().refreshLanguage(locale);
                LoginWindow.getInstance().refreshLanguage(locale);
                AddWindow.getInstance().refreshLanguage(locale);
                UpdateWindow.getInstance().refreshLanguage(locale);
                RemoveByIdWindow.getInstance().refreshLanguage(locale);
                RemoveByDistanceWindow.getInstance().refreshLanguage(locale);
                ExecuteScriptWindow.getInstance().refreshLanguage(locale);
            });
            this.add(curLang);
        }

    }

    public static ResourceBundle getBundle() {
        return bundle;
    }
}
