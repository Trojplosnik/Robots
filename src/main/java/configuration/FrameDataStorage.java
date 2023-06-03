package configuration;

import java.awt.Rectangle;
import java.io.Serializable;

public class FrameDataStorage implements Serializable {
    String title;

    boolean Closed;

    boolean Icon;
    Rectangle bounds;

    public FrameDataStorage(String title, boolean Closed, boolean Icon,  Rectangle  bounds) {
        this.title = title;
        this.Closed = Closed;
        this.bounds = bounds;
        this.Icon = Icon;
    }

    public String getTitle() {
        return title;
    }

    public boolean isClosed() {
        return Closed;
    }

    public boolean isIcon() {
        return Icon;
    }

    public Rectangle getBounds() {
        return bounds;
    }




}
