package ru.ulmc.bank.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ulmc.bank.ui.event.UiEventBus;
import ru.ulmc.bank.ui.event.UiEvents;
import ru.ulmc.bank.ui.view.BoardView;

import javax.servlet.annotation.WebServlet;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@SpringUI(path = "/*")
@Title("Табло \"Курсы Валют\"")
@Theme("bank")
public class DashboardUI extends UI {

    private final UiEventBus uiEventBus = new UiEventBus();
    @Autowired
    private BoardView boardView;

    public static UiEventBus getDashboardEventbus() {
        return ((DashboardUI) getCurrent()).uiEventBus;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        UiEventBus.register(this);
        Responsive.makeResponsive(this);
        Page.getCurrent().addBrowserWindowResizeListener(
                (Page.BrowserWindowResizeListener) event -> UiEventBus.post(new UiEvents.BrowserResizeEvent()));
        setContent(boardView);
    }

    @WebServlet(urlPatterns = {"/*", "/VAADIN/*"}, name = "AppServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = DashboardUI.class, productionMode = false)
    public static class AppServlet extends SpringVaadinServlet {

    }

}
