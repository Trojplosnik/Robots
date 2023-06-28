package gui.window;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import language.LanguageTranslator;
import model.log.LogChangeListener;
import model.log.LogEntry;
import model.log.LogWindowSource;

import static language.LanguageTranslator.TRANSLATOR;


public class LogWindow extends JInternalFrame implements LogChangeListener, Observer
{
    private final LogWindowSource m_logSource;
    private final TextArea m_logContent;

    public LogWindow(LogWindowSource logSource)
    {
        super(TRANSLATOR.translate("work_protocol"), true, true, true, true);
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);

        TRANSLATOR.addObserver(this);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
    }

    private void updateLogContent()
    {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all())
        {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }
    
    @Override
    public void onLogChanged()
    {
        EventQueue.invokeLater(this::updateLogContent);
    }


    private boolean areEqual(Object o1, Object o2) {
        if (o1 == null)
            return o2 == null;
        return o1.equals(o2);
    }


    @Override
    public void update(Observable o, Object key) {
        if (areEqual(TRANSLATOR, o)) {
            if (areEqual(LanguageTranslator.KEY_CHANGE_LANGUAGE, key))
                setTitle(TRANSLATOR.translate("work_protocol"));
        }
    }
}
