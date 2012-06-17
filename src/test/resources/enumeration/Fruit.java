package enumeration;

import java.awt.*;

public enum Fruit {

    APPLE(Color.RED),
    ORANGE(Color.ORANGE),
    BANANA(Color.YELLOW),
    PEAR(Color.GREEN);

    private Fruit(final Color color) {
        this.color = color;
    }

    private Color color;

    public Color getColor() {
        return color;
    }
}
