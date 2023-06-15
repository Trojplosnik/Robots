package model.log;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Что починить:
 * 1. Этот класс порождает утечку ресурсов (связанные слушатели оказываются
 * удерживаемыми в памяти)
 * 2. Этот класс хранит активные сообщения лога, но в такой реализации он
 * их лишь накапливает. Надо же, чтобы количество сообщений в логе было ограничено
 * величиной m_iQueueLength (т.е. реально нужна очередь сообщений
 * ограниченного размера)
 */
public class LogWindowSource {
    private int m_iQueueLength;

    private final ArrayList<LogEntry> m_messages;
    private final ArrayList<LogChangeListener> m_listeners;
    private static final Object syncKey = new Object();

    private volatile LogChangeListener[] m_activeListeners;

    public LogWindowSource(int iQueueLength) {
        m_iQueueLength = iQueueLength;
        m_messages = new ArrayList<>(iQueueLength);
        m_listeners = new ArrayList<>();
    }

    public void registerListener(LogChangeListener listener) {
        synchronized (syncKey) {
            m_listeners.add(listener);
        }
    }

    public void unregisterListener(LogChangeListener listener) {
        synchronized (syncKey) {
            m_listeners.remove(listener);
        }
    }

//    public void append(LogLevel logLevel, String strMessage) {
//        LogEntry entry = new LogEntry(logLevel, strMessage);
//        synchronized (syncKey) {
//            if (m_messages.size() >= m_iQueueLength) {
//                m_messages.remove(0);
//            }
//            m_messages.add(entry);
//            for (LogChangeListener listener : m_listeners) {
//                listener.onLogChanged();
//            }
//        }
//    }

    public void append(LogLevel logLevel, String strMessage)
    {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        if (m_messages.size() >= m_iQueueLength) {
            m_messages.remove(0);
        }
        synchronized (syncKey){
        m_messages.add(entry);
        }

        LogChangeListener [] activeListeners = m_activeListeners;
        if (activeListeners == null)
        {
            synchronized (m_listeners)
            {
                if (m_activeListeners == null)
                {
                    activeListeners = m_listeners.toArray(new LogChangeListener [0]);
                    m_activeListeners = activeListeners;
                }
            }
        }
        assert activeListeners != null;
        for (LogChangeListener listener : activeListeners)
        {
            listener.onLogChanged();
        }
    }

    public int size() {
        synchronized (syncKey) {
            return m_messages.size();
        }
    }

    public Iterable<LogEntry> range(int startFrom, int count) {
        synchronized (syncKey) {
            if (startFrom < 0 || startFrom >= m_messages.size()) {
                return Collections.emptyList();
            }
            int indexTo = Math.min(startFrom + count, m_messages.size());
            return m_messages.subList(startFrom, indexTo);
        }
    }

    public Iterable<LogEntry> all() {
        synchronized (syncKey) {
            return m_messages;
        }
    }
}
