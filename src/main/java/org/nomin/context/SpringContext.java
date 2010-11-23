package org.nomin.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;

/**
 * Provides access to the Spring container.
 * @author Dmitry Dobrynin
 *         Date: 23.11.2010 Time: 16:56:33
 */
public class SpringContext implements Context, BeanFactoryAware {
    private BeanFactory beanFactory;

    public SpringContext(BeanFactory beanFactory) { this.beanFactory = beanFactory; }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException { this.beanFactory = beanFactory; }

    public Object getResource(String resource) {
        return beanFactory.containsBean(resource) ? beanFactory.getBean(resource) : null;
    }
}
