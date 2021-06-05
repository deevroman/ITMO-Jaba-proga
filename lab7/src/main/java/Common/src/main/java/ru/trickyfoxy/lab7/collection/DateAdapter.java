package ru.trickyfoxy.lab7.collection;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Класс адаптер для записи/чтения даты в формате yyyy-MM-dd в XML файле
 */
public class DateAdapter extends XmlAdapter<String, Date> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public String marshal(Date v) throws Exception {
        synchronized (dateFormat) {
            return dateFormat.format(v);
        }
    }

    @Override
    public Date unmarshal(String v) throws Exception {
        synchronized (dateFormat) {
            return dateFormat.parse(v);
        }
    }
}