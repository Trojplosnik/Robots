package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import model.log.Logger;
import model.state.GameModel;

import javax.swing.JFrame;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JInternalFrame;

import static language.LanguageTranslator.TRANSLATOR;
import static configuration.SaveLoad.SAVELOAD;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame {
    private static final JDesktopPane desktopPane = new JDesktopPane();


    private static final GameModel robotModel = new GameModel();

    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);

        createWindows();

        setJMenuBar(new MenuBar(this));

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExitEvent();
            }
        });
    }

    static protected void confirmExitEvent() {
        String[] options = {TRANSLATOR.translate("yes"), TRANSLATOR.translate("no")};
        int exit = JOptionPane.showOptionDialog(null, TRANSLATOR.translate("exit_confirm"),
                TRANSLATOR.translate("exit"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[1]);
        if (exit == 0){
            SAVELOAD.save(desktopPane.getAllFrames());
            System.exit(0);
        }
    }

    protected GameWindow createGameWindow() {
        GameWindow gameWindow = new GameWindow(robotModel, TRANSLATOR.translate("game_field"));
        gameWindow.setSize(600, 600);
        int x = (this.getWidth() - gameWindow.getWidth()) / 2;
        int y = (this.getHeight() - gameWindow.getHeight()) / 2;
        gameWindow.setLocation(x, y);
        return gameWindow;
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), TRANSLATOR.translate("work_protocol"));
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        Logger.debug("The protocol works");
        setMinimumSize(logWindow.getSize());
        return logWindow;
    }

    protected RobotsPositionWindow createRobotsPositionWindow() {
        RobotsPositionWindow posWindow = new RobotsPositionWindow(robotModel, TRANSLATOR.translate("coordinates"));
        posWindow.setLocation(350, 10);
        posWindow.setSize(300, 800);
        return posWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        for (JInternalFrame currentFrame : desktopPane.getAllFrames()) {
            if (currentFrame.getTitle().equals(frame.getTitle()))
                return;
        }
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    protected void createWindows() {
        JInternalFrame[] frames = SAVELOAD.load(robotModel);
        if (frames != null){
            for (JInternalFrame frame : frames) {
                addWindow(frame);
                if (frame.getTitle().equals(TRANSLATOR.translate("work_protocol")))
                    setMinimumSize(frame.getSize());

            }
        }
        else {
            addWindow(createLogWindow());
            addWindow(createGameWindow());
            addWindow(createRobotsPositionWindow());
        }
    }


    //    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
// 
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
// 
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        return menuBar;
//    }
}
