package configuration;

import java.awt.Rectangle;
import java.io.Serializable;

public class FrameDataStorage implements Serializable {
    private final String title;

    private final boolean Closed;

    private final boolean icon;
    private final Rectangle bounds;


    public FrameDataStorage(String title, boolean Closed, boolean Icon, Rectangle  bounds) {
        this.title = title;
        this.Closed = Closed;
        this.bounds = bounds;
        this.icon = Icon;

    }

    public String getTitle() {
        return title;
    }

    public boolean isClosed() {
        return Closed;
    }

    public boolean isIcon() {
        return icon;
    }

    public Rectangle getBounds() {
        return bounds;
    }




}
