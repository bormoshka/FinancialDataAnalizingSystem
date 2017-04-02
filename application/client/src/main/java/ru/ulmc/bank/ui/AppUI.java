package ru.ulmc.bank.ui;

import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.*;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ulmc.bank.core.common.exception.AuthenticationException;
import ru.ulmc.bank.dao.entity.system.User;
import ru.ulmc.bank.server.controller.AuthenticationController;
import ru.ulmc.bank.ui.data.Text;
import ru.ulmc.bank.ui.data.provider.RuText;
import ru.ulmc.bank.ui.event.UiEventBus;
import ru.ulmc.bank.ui.event.UiEvents;
import ru.ulmc.bank.ui.view.LoginView;
import ru.ulmc.bank.ui.view.MainView;

import javax.servlet.annotation.WebServlet;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@SpringUI
@Title("Система Управления Котировками")
@Theme("bank")
public class AppUI extends UI {

    @Autowired
    private AuthenticationController authenticationController;

    private static Text text = new RuText();

    private final UiEventBus uiEventBus = new UiEventBus();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        UiEventBus.register(this);
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);
        updateContent();
        Page.getCurrent().addBrowserWindowResizeListener(
                (Page.BrowserWindowResizeListener) event -> UiEventBus.post(new UiEvents.BrowserResizeEvent()));
    }

    private void updateContent() {
        User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
        if (user != null) {
            setContent(new MainView());
            removeStyleName("loginview");
            //getNavigator().navigateTo(getNavigator().getState());
        } else {
            setContent(new LoginView());
            addStyleName("loginview");
        }
    }

    @Subscribe
    public void userLoginRequested(UiEvents.UserLoginRequestedEvent event) {
        try {
            User user = authenticationController.authenticate(event.getUserName(), event.getPassword());
            VaadinSession.getCurrent().setAttribute(User.class.getName(), user);
            updateContent();
        } catch (AuthenticationException e) {
            Notification notification = new Notification(text.authErrorHeader());
            if (e.isSystemFault()) {
                notification.setDescription(text.authErrorSystemFault(e.getMessage()));
            } else {
                notification.setDescription(text.authErrorBaseText());
            }
            notification.setHtmlContentAllowed(true);
            notification.setStyleName("tray dark small closable userName-help");
            notification.setPosition(Position.BOTTOM_CENTER);
            notification.setDelayMsec(10000);
            notification.show(Page.getCurrent());
        }
    }

    @Subscribe
    public void userLoggedOut(UiEvents.UserLoggedOutEvent event) {
        // When the user logs out, current VaadinSession gets closed and the
        // page gets reloaded on the userName screen. Do notice the this doesn't
        // invalidate the current HttpSession.
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
    }

    public static UiEventBus getDashboardEventbus() {
        return ((AppUI) getCurrent()).uiEventBus;
    }


    public static Text getTextProvider() {
        return text;
    }

    @WebServlet(urlPatterns = "/*", name = "AppServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = AppUI.class, productionMode = false)
    public static class AppServlet extends VaadinServlet {

    }

}
