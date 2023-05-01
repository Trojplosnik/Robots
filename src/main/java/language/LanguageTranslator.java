package language;


import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageTranslator {

    private final static String DEFAULT_LANGUAGE = "ru";

    private static ResourceBundle rb;
    public LanguageTranslator()
    {
        rb = ResourceBundle.getBundle("locale", Locale.of(DEFAULT_LANGUAGE));
    }

    public static final LanguageTranslator TRANSLATOR = new LanguageTranslator();



    public void changeLanguage(String language) {
        try {
            rb = ResourceBundle.getBundle("locale", Locale.of(language));
        }
        catch (Exception e)
        {
            rb = ResourceBundle.getBundle("locale", Locale.of(DEFAULT_LANGUAGE));
        }
    }

    public String translate(String msg) {
        return rb.getString(msg);
    }
}
