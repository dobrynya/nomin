package org.nomin.context;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import static org.junit.Assert.*;

/**
 * Tests SpringContext.
 * @author Dmitry Dobrynin
 *         Date: 23.11.2010 Time: 17:45:24
 */
public class SpringContextTest {
    @Test
    public void testGetResource() throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        SpringContext nominContext = new SpringContext(ctx);
        assertEquals("My Resource", nominContext.getResource("myResource"));
        assertNull(nominContext.getResource("Non-existent"));
        nominContext.setBeanFactory(ctx);
    }
}
