package configuration;

import javax.swing.*;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.io.Serializable;

public class FrameState implements Serializable {
    private final String title;

    private final boolean closed;

    private final boolean icon;
    private final Rectangle bounds;


    public FrameState(String title, boolean closed, boolean icon, Rectangle  bounds) {
        this.title = title;
        this.closed = closed;
        this.bounds = bounds;
        this.icon = icon;
    }

    public FrameState(JInternalFrame frame) {
        title = frame.getTitle();
        closed = frame.isClosed();
        bounds = frame.getBounds();
        icon = frame.isIcon();

    }

    public void restoreFrame(JInternalFrame frame) throws PropertyVetoException {
        frame.setTitle(title);
        frame.setBounds(bounds);
        frame.setClosed(closed);
        frame.setIcon(icon);
    }

    public String getTitle() {
        return title;
    }

    public boolean isClosed() {
        return closed;
    }

    public boolean isIcon() {
        return icon;
    }

    public Rectangle getBounds() {
        return bounds;
    }




}
