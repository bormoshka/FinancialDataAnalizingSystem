package ru.ulmc.bank.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ulmc.bank.server.config.UserSession;
import ru.ulmc.bank.server.controller.AuthenticationController;
import ru.ulmc.bank.ui.data.Text;
import ru.ulmc.bank.ui.data.provider.RuText;
import ru.ulmc.bank.ui.event.AdminEventBus;

import javax.servlet.annotation.WebServlet;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@SpringUI(path = "/admin/*")
@Title("Администрирование АСУК")
@Theme("bank")
public class AdminUI extends UI {

    private static Text text = new RuText();
    private final AdminEventBus adminEventBus = new AdminEventBus();
    @Autowired
    private AuthenticationController authenticationController;
    @Autowired
    private UserSession userSession;

    public static AdminEventBus getEventBus() {
        return ((AdminUI) getCurrent()).adminEventBus;
    }

    public static Text getTextProvider() {
        return text;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {

    }

    @WebServlet(urlPatterns = {"/admin/*", "/VAADIN/*"}, name = "AdminServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = AdminUI.class, productionMode = false)
    public static class AdminServlet extends SpringVaadinServlet {

    }

}
