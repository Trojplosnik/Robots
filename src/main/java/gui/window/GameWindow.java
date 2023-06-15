package gui.window;

import gui.GameVisualizer;
import model.state.Calculator;
import model.state.GameModel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;


public class GameWindow extends JInternalFrame implements Observer {
    private final GameModel robotModel;

    public GameWindow(GameModel model, String title) {
        super(title, true, true, true, true);

        robotModel = model;
        robotModel.addObserver(this);

        GameVisualizer gameVisualizer = new GameVisualizer(robotModel);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(gameVisualizer, BorderLayout.CENTER);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                robotModel.setTargetPosition(e.getPoint());
                repaint();
            }
        });
        getContentPane().add(panel);
        pack();
    }

    private boolean areEqual(Object o1, Object o2) {
        if (o1 == null)
            return o2 == null;
        return o1.equals(o2);
    }

    @Override
    public void update(Observable o, Object key) {
        if (areEqual(robotModel, o)) {
            if (areEqual(GameModel.KEY_MODEL_UPDATE, key))
                EventQueue.invokeLater(this::repaint);
        }
    }

}
