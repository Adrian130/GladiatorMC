package xyz.velium.uhc.util;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class JavaUtils {

    public static <T> List<List<T>> chopList(List<T> list, int size) {
        List<List<T>> parts = new ArrayList<>();
        int listSize = list.size();
        for (int i = 0; i < listSize; i += size) {
            parts.add(new ArrayList<>(list.subList(i, Math.min(listSize, i + size))));
        }
        return parts;
    }

    public static int getRandomIntInRange(int min, int max) {
        return (int) (Math.random() * ((max - min) + 1)) + min;
    }

    public static int[] getInventoryPlaces(int amount) {
        switch (amount) {
            case 0:
                return new int[]{};
            case 1:
                return new int[]{4};
            case 2:
                return new int[]{3, 5};
            case 3:
                return new int[]{2, 4, 6};
            case 4:
                return new int[]{1, 3, 5, 7};
            case 5:
                return new int[]{0, 2, 4, 6, 8};
            case 6:
                return new int[]{1, 2, 3, 5, 6, 7};
            case 7:
                return new int[]{0, 1, 2, 4, 6, 7, 8};
            case 8:
                return new int[]{0, 1, 2, 3, 5, 6, 7, 8};
            case 9:
                return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
            default:
                throw new UnsupportedOperationException("Amount cannot be greater than 9");
        }
    }

    public static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
        return (path.delete());
    }

    public static boolean isNumeric(String string) {
        try {
            @SuppressWarnings("unused")
            Double d = Double.parseDouble(string);
        } catch (NumberFormatException nfe) {
            @SuppressWarnings("unused")
            double d;
            return false;
        }
        return true;
    }

    public static void setField(Object change, String name, Object to) {
        try {
            Field field = change.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(change, to);
            field.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
