package configuration;

import javax.swing.*;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.io.Serializable;

public class FrameState implements Serializable {
    private final String title;

    private final boolean closed;

    private final boolean icon;

    private final int x;

    private final int y;

    private final int width;

    private final int height;


    public FrameState(String title, boolean closed, boolean icon,
                      int x, int y, int width, int height) {
        this.title = title;
        this.closed = closed;
        this.x = x;
        this.y= y;
        this.width = width;
        this.height = height;
        this.icon = icon;
    }

    public FrameState(JInternalFrame frame) {
        title = frame.getTitle();
        closed = frame.isClosed();
        x = frame.getX();
        y= frame.getY();
        width = frame.getWidth();
        height = frame.getHeight();
        icon = frame.isIcon();
    }

    public FrameState() {
        title = null;
        closed = true;
        x = 0;
        y= 0;
        width = 0;
        height = 0;
        icon = true;
    }

    public void restoreFrame(JInternalFrame frame) {
        frame.setTitle(title);
        frame.setBounds(x, y, width, height);
        try {
            frame.setClosed(closed);
            frame.setIcon(icon);
        } catch (PropertyVetoException e) {
            // just ignore
        }

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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


}
