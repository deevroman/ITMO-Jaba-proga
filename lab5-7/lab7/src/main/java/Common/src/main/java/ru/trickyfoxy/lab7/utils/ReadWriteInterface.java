package ru.trickyfoxy.lab7.utils;

import ru.trickyfoxy.lab7.collection.Coordinates;
import ru.trickyfoxy.lab7.collection.LocationFrom;
import ru.trickyfoxy.lab7.collection.LocationTo;
import ru.trickyfoxy.lab7.collection.Route;
import ru.trickyfoxy.lab7.exceptions.InvalidInputException;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Scanner;

public class ReadWriteInterface {

    private Reader reader;

    public Writer getWriter() {
        return writer;
    }

    private final Writer writer;
    private Scanner scanner;
    private ObjectOutputStream objectWriter;


    public boolean isInteractive() {
        return interactive;
    }

    private boolean interactive;

    public String getName() {
        return name;
    }

    private String name;

    public ReadWriteInterface(Writer writer) {
        this.writer = writer;
    }

    public ReadWriteInterface(Reader reader, Writer writer, boolean interactive) {
        this.reader = reader;
        this.writer = writer;
        this.scanner = new Scanner(reader);
        this.interactive = interactive;
        this.name = "";
    }

    public ReadWriteInterface(Reader reader, Writer writer, boolean interactive, String name) {
        this.reader = reader;
        this.writer = writer;
        this.scanner = new Scanner(reader);
        this.interactive = interactive;
        this.name = name;
    }

    public void write(String message, boolean flush) throws IOException {
//        if (objectWriter != null){
//            objectWriter.writeObject(message);
//            return;
//        }
        writer.write(message);
        if (flush) {
            writer.flush();
        }
    }

    public void write(String message) throws IOException {
        write(message, true);
    }

    public void writeln(String message) throws IOException {
        write(message + "\n", true);
    }

    public void writeln(String message, boolean flush) throws IOException {
        write(message + "\n", flush);
    }

    public String read() {
        return scanner.next();
    }

    public String read(String message) throws IOException {
        if (interactive) {
            writeln(message);
        }
        return scanner.nextLine();
    }

    public String readString() {
        try {
            return scanner.next();
        } catch (Exception e) {
            return null;
        }
    }

    public String readString(boolean readSpace) {
        try {
            String str = scanner.next();
            if (readSpace) {
                if (!interactive) {
                    try {
                        scanner.nextLine();
                    } catch (Exception e) {
                        return str;
                    }
                }
            }
            return str;
        } catch (Exception e) {
            return null;
        }
    }

    public String readLine() {
        return scanner.nextLine();
    }


    public String readWithMessageAndLimit(String message, boolean nullable) throws IOException {
        String result = "";
        do {
            if (result == null) {
                writeln("");
            }
            if (interactive) {
                writeln(message);
            }
            result = scanner.nextLine();
            result = result.isEmpty() ? null : result;
        } while (interactive && !nullable && result == null);
        if (!interactive && result == null) {
            throw new InvalidInputException("Получена пустая строка в поле, которое не может быть null");
        }
        return result;
    }

    public String readWithMessageAndLimit(String message, int min, int max) throws IOException {
        String result;
        do {
            result = readWithMessageAndLimit(message, false);
        } while (!numberInInterval(Double.parseDouble(result), min, max));
        return result;
    }

    public static boolean numberInInterval(double s, int min, int max) {
        return min <= s && s <= max;
    }

    public Route readRoute() throws ClassCastException, InvalidInputException, NumberFormatException, IOException {
        Route route = new Route();
        readLine();
        route.setName(readName());
        route.setCoordinates(readCoordinates());
        route.setFrom(readLocationFrom());
        route.setTo(readLocationTo());
        route.setDistance(readDistance());
        return route;
    }

    public Coordinates readCoordinates() throws NumberFormatException, IOException {
        Coordinates coordinates = new Coordinates();

        while (true) {
            try {
                coordinates.setX(Float.parseFloat(read("Введите X координату локации (вещественное число): ")));
            } catch (NumberFormatException e) {
                writeln("Неправильный формат числа");
                if (!interactive) {
                    throw e;
                }
                continue;
            } catch (InvalidInputException e) {
                writeln(e.getMessage());
                if (!interactive) {
                    throw e;
                }
                continue;
            }
            break;
        }
        while (true) {
            try {
                coordinates.setY(Long.parseLong(read("Введите Y координату локации (целое число): ")));
            } catch (NumberFormatException e) {
                writeln("Неправильный формат числа");
                if (!interactive) {
                    throw e;
                }
                continue;
            } catch (InvalidInputException e) {
                writeln(e.getMessage());
                if (!interactive) {
                    throw e;
                }
                continue;
            }
            break;
        }
        return coordinates;
    }

    public LocationFrom readLocationFrom() throws NumberFormatException, IOException {
        LocationFrom locationFrom = new LocationFrom();
        while (true) {
            try {
                locationFrom.setX(Integer.parseInt(read("Введите X координату начала (целое число): ")));
            } catch (NumberFormatException e) {
                writeln("Неправильный формат числа");
                if (!interactive) {
                    throw e;
                }
                continue;
            } catch (InvalidInputException e) {
                writeln(e.getMessage());
                if (!interactive) {
                    throw e;
                }
                continue;
            }
            break;
        }
        while (true) {
            try {
                locationFrom.setY(Double.parseDouble(read("Введите Y координану начала (вещественное число): ")));
            } catch (NumberFormatException e) {
                writeln("Неправильный формат числа");
                if (!interactive) {
                    throw e;
                }
                continue;
            } catch (InvalidInputException e) {
                writeln(e.getMessage());
                if (!interactive) {
                    throw e;
                }
                continue;
            }
            break;
        }
        while (true) {
            try {
                locationFrom.setZ(Float.parseFloat(read("Введите Z координату начала (вещественное число): ")));
            } catch (NumberFormatException e) {
                writeln("Неправильный формат числа");
                if (!interactive) {
                    throw e;
                }

                continue;
            } catch (InvalidInputException e) {
                writeln(e.getMessage());
                if (!interactive) {
                    throw e;
                }
                continue;
            }
            break;
        }
        while (true) {
            try {
                locationFrom.setName(read("Введите название координаты начала: "));
            } catch (NumberFormatException e) {
                writeln("Неправильный формат числа");
                if (!interactive) {
                    throw e;
                }
                continue;
            } catch (InvalidInputException e) {
                writeln(e.getMessage());
                if (!interactive) {
                    throw e;
                }
                continue;
            }
            break;
        }
        return locationFrom;
    }

    public LocationTo readLocationTo() throws NumberFormatException, IOException {
        LocationTo locationTo = new LocationTo();
        while (true) {
            try {
                locationTo.setX(Integer.parseInt(read("Введите X координату конца (вещественное число): ")));
            } catch (NumberFormatException e) {
                writeln("Неправильный формат числа");
                if (!interactive) {
                    break;
                }
                continue;
            } catch (InvalidInputException e) {
                writeln(e.getMessage());
                if (!interactive) {
                    break;
                }
                continue;
            }
            break;
        }
        while (true) {
            try {
                locationTo.setY(Integer.parseInt(read("Введите Y координату конца (вещесетвенное число): ")));
            } catch (NumberFormatException e) {
                writeln("Неправильный формат числа");
                if (!interactive) {
                    break;
                }
                continue;
            } catch (InvalidInputException e) {
                writeln(e.getMessage());
                if (!interactive) {
                    break;
                }
                continue;
            }
            break;
        }
        while (true) {
            try {
                locationTo.setName(read("Введите название координаты конца: "));
            } catch (NumberFormatException e) {
                writeln("Неправильный формат числа");
                if (!interactive) {
                    break;
                }
                continue;
            } catch (InvalidInputException e) {
                writeln(e.getMessage());
                if (!interactive) {
                    break;
                }
                continue;
            }
            break;
        }
        return locationTo;
    }

    public float readDistance() throws NumberFormatException, IOException {
        while (true) {
            try {
                float f = Float.parseFloat(read("Введите distance (число, больше 1): "));
                if (f <= 1) {
                    throw new InvalidInputException("Число должно быть больше 1");
                }
                return f;
            } catch (NumberFormatException e) {
                writeln("Неправильный формат числа");
                if (!interactive) {
                    break;
                }
            } catch (InvalidInputException e) {
                writeln(e.getMessage());
                if (!interactive) {
                    break;
                }
            }
        }
        return 0;
    }

    public String readName() throws NumberFormatException, IOException {
        while (true) {
            try {
                return read("Введите имя: ");
            } catch (NumberFormatException e) {
                writeln("Неправильный формат числа");
            } catch (InvalidInputException e) {
                writeln(e.getMessage());
            }
        }
    }

}
