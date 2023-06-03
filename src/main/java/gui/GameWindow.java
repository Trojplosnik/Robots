package gui;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import static language.LanguageTranslator.TRANSLATOR;

public class GameWindow extends JInternalFrame
{
    public GameWindow()
    {
        super(TRANSLATOR.translate("game_field"), true, true, true, true);
        GameVisualizer m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}
