package org.nomin.context;

import java.util.Hashtable;
import javax.naming.*;
import org.nomin.core.NominException;
import static java.text.MessageFormat.format;

/**
 * Provides access to the JNDI registry.
 * @author Dmitry Dobrynin
 *         Date: 23.11.2010 Time: 17:00:10
 */
public class JndiContext implements Context {
    private InitialContext ctx;

    public JndiContext() { this(new Hashtable()); }

    public JndiContext(Hashtable parameters) {
        try { ctx = new InitialContext(parameters); }
        catch (NamingException e) { throw new NominException("Could not create an InitialContext!", e); }
    }

    public Object getResource(String resource) {
        try { return ctx.lookup(resource); }
        catch (NamingException e) { throw new NominException(format("Could not look up {0}!", resource), e); }
    }
}
