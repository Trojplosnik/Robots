package gui;

import log.Logger;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static language.LanguageTranslator.TRANSLATOR;

public class MenuBar extends JMenuBar {
    public MenuBar() {
        add(createExitBottom());
        add(createLookAndFeelMenu());
        add(createTestMenu());
    }

    private JMenuItem createExitBottom() {
        JMenuItem exit = new JMenuItem(TRANSLATOR.translate("exit"), KeyEvent.VK_ESCAPE);
        exit.addActionListener((event) -> MainApplicationFrame.confirmExitEvent());
        exit.setMaximumSize(exit.getPreferredSize());
        return exit;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
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
            this.invalidate();
        });
        lookAndFeelMenu.add(systemLookAndFeel);

        JMenuItem crossplatformLookAndFeel = new JMenuItem(TRANSLATOR.translate("universal_scheme"), KeyEvent.VK_S);
        crossplatformLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        });
        lookAndFeelMenu.add(crossplatformLookAndFeel);
        return lookAndFeelMenu;
    }

    private JMenu createTestMenu() {
        JMenu testMenu = new JMenu(TRANSLATOR.translate("tests"));
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(TRANSLATOR.translate("tests_commands"));

        {
            JMenuItem addLogMessageItem = new JMenuItem(TRANSLATOR.translate("log_message"), KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> Logger.debug(TRANSLATOR.translate("new_line")));
            testMenu.add(addLogMessageItem);
        }
        return testMenu;
    }

}
