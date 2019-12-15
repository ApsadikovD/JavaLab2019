package ru.javalab.shop.listeners;

import ru.javalab.di.ApplicationContext;
import ru.javalab.di.ApplicationContextReflectionBased;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextServletListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        ApplicationContext context = new ApplicationContextReflectionBased();
        servletContext.setAttribute("context", context);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
