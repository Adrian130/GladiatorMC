package xyz.velium.uhc.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum BorderSize {

    B2000(2000), B1500(1500), B1000(1000), B500(500), B100(100), B50(50), B25(25);

    private int size;

    BorderSize(int size) {
        this.size = size;
    }

    public static BorderSize getBorderByNumber(int number, int starting) {
        List<BorderSize> borderSizes = new ArrayList<>();
        borderSizes.addAll(Arrays.asList(BorderSize.values()));
        int border = number + starting;
        return borderSizes.get(border);
    }

    public int getSize() {
        return size;
    }
}
