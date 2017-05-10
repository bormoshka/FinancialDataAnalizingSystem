package ru.ulmc.bank.ui;

import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.*;
import com.vaadin.shared.ui.window.WindowRole;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ulmc.bank.core.common.exception.AuthenticationException;
import ru.ulmc.bank.dao.entity.system.User;
import ru.ulmc.bank.server.config.UserSession;
import ru.ulmc.bank.server.controller.Controllers;
import ru.ulmc.bank.ui.common.MessageWindow;
import ru.ulmc.bank.ui.data.Text;
import ru.ulmc.bank.ui.data.provider.RuText;
import ru.ulmc.bank.ui.event.UiEventBus;
import ru.ulmc.bank.ui.event.UiEvents;
import ru.ulmc.bank.ui.view.LoginView;
import ru.ulmc.bank.ui.view.MainView;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@SpringUI(path = "/*")
@Title("Система Управления Котировками")
@Theme("bank")
public class AppUI extends UI {

    private static Text text = new RuText();
    private final UiEventBus uiEventBus = new UiEventBus();
    @Autowired
    private Controllers controllers;
    @Autowired
    private MainView mainView;
    @Autowired
    private UserSession userSession;
    @Autowired
    private SpringNavigator navigator;

    public static UiEventBus getDashboardEventbus() {
        return ((AppUI) getCurrent()).uiEventBus;
    }

    public static Text getTextProvider() {
        return text;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        UiEventBus.register(this);
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);
        setupUiBasedOnUserStatus();
        Page.getCurrent().addBrowserWindowResizeListener(event -> UiEventBus.post(new UiEvents.BrowserResizeEvent()));
        //setNavigator(navigator);

    }

    private void setupUiBasedOnUserStatus() {
        if (userSession.isAuthenticated()) {
            mainView.initView(userSession);
            setContent(mainView);
            // getNavigator().navigateTo(getNavigator().getState());
        } else {
            setContent(new LoginView(controllers));
        }
    }

    @Subscribe
    public void userLoginRequested(UiEvents.UserLoginRequestedEvent event) {
        try {
            final HttpServletRequest request = ((VaadinServletRequest) VaadinService.getCurrentRequest())
                    .getHttpServletRequest();
            User user = controllers.getAuthenticationController().authenticate(event.getUserName(), event.getPassword(), request);
            userSession.setUser(user);
            VaadinSession.getCurrent().setAttribute(User.class.getName(), user);
            Page.getCurrent().reload();
        } catch (AuthenticationException ex) {
            final MessageWindow window = new MessageWindow(text.authErrorHeader());
            window.setText(ex.isSystemFault() ?
                    text.authErrorSystemFault(ex.getMessage()) :
                    text.authErrorBaseText());
            window.setAssistiveRole(WindowRole.ALERTDIALOG);
            addWindow(window);
        } finally {
            UiEventBus.post(new UiEvents.UserLoginResponseEvent());
        }
    }

    @Subscribe
    public void userLoggedOut(UiEvents.UserLoggedOutEvent event) {
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
    }

    @WebServlet(urlPatterns = {"/*", "/VAADIN/*"}, name = "AppServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = AppUI.class, productionMode = false)
    public static class AppServlet extends SpringVaadinServlet {

    }

}
