package hu.eke.colorlist;

/**
 * Created by szugyi on 15/11/15.
 */
public class ColorItem {
    private String color;
    private String value;

    public ColorItem(String color, String value) {
        this.color = color;
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return color;
    }
}
