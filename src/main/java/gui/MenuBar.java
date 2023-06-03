package gui;

import log.Logger;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import static language.LanguageTranslator.TRANSLATOR;


public class MenuBar extends JMenuBar {

    private final MainApplicationFrame mainApp;
    public MenuBar(MainApplicationFrame mainApplicationFrame) {
        mainApp = mainApplicationFrame;
        add(createExitBottom());
        add(createLookAndFeelMenu());
        add(createTestMenu());
        add(createAdditionalMenu());
    }



    private JMenu createAdditionalMenu() {
        JMenu additionalMenu = new JMenu(TRANSLATOR.translate("additional"));
        additionalMenu.setMnemonic(KeyEvent.VK_A);
        additionalMenu.getAccessibleContext()
                .setAccessibleDescription(TRANSLATOR.translate("additional_description"));

        JMenuItem gameWindow = new JMenuItem(TRANSLATOR.translate("open_game_window"), KeyEvent.VK_G);
        gameWindow.addActionListener((event) -> {
            mainApp.addWindow(mainApp.createGameWindow());
        });
        mainApp.invalidate();
        additionalMenu.add(gameWindow);

        JMenuItem logWindow = new JMenuItem(TRANSLATOR.translate("open_log_window"), KeyEvent.VK_L);
        logWindow.addActionListener((event) -> mainApp.addWindow(mainApp.createLogWindow()));
        mainApp.invalidate();
        additionalMenu.add(logWindow);
        return additionalMenu;
    }


    private JMenu createExitBottom() {
        JMenu filesMenu = new JMenu(TRANSLATOR.translate("files"));
        filesMenu.setMnemonic(KeyEvent.VK_E);
        filesMenu.getAccessibleContext().setAccessibleDescription(TRANSLATOR.translate("files_description"));
        JMenuItem exit = new JMenuItem(TRANSLATOR.translate("exit"), KeyEvent.VK_ESCAPE);
        filesMenu.add(exit);
        return filesMenu;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(mainApp);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }



    private JMenu createLookAndFeelMenu() {
        JMenu lookAndFeelMenu = new JMenu(TRANSLATOR.translate("display_mode"));
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                TRANSLATOR.translate("display_mode_managing"));

        JMenuItem systemLookAndFeel = new JMenuItem(TRANSLATOR.translate("system_scheme"), KeyEvent.VK_S);
        systemLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            mainApp.invalidate();
        });
        lookAndFeelMenu.add(systemLookAndFeel);

        JMenuItem crossplatformLookAndFeel = new JMenuItem(TRANSLATOR.translate("universal_scheme"), KeyEvent.VK_S);
        crossplatformLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            mainApp.invalidate();
        });
        lookAndFeelMenu.add(crossplatformLookAndFeel);


        JMenuItem nimbusLookAndFeel = new JMenuItem(TRANSLATOR.translate("nimbus_scheme"), KeyEvent.VK_S);
        nimbusLookAndFeel.addActionListener((event) -> {
            setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            mainApp.invalidate();
        });
        lookAndFeelMenu.add(nimbusLookAndFeel);

        return lookAndFeelMenu;
    }

    private JMenu createTestMenu() {
        JMenu testMenu = new JMenu(TRANSLATOR.translate("tests"));
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(TRANSLATOR.translate("tests_commands"));
        {
            JMenuItem addLogMessageItem = new JMenuItem(TRANSLATOR.translate("log_message"), KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> Logger.debug("New line"));
            testMenu.add(addLogMessageItem);
        }
        return testMenu;
    }

}
