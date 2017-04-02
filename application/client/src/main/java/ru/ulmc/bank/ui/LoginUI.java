package ru.ulmc.bank.ui;

import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.window.WindowRole;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ulmc.bank.core.common.exception.AuthenticationException;
import ru.ulmc.bank.dao.entity.system.User;
import ru.ulmc.bank.server.controller.AuthenticationController;
import ru.ulmc.bank.ui.common.MessageWindow;
import ru.ulmc.bank.ui.data.Text;
import ru.ulmc.bank.ui.data.provider.RuText;
import ru.ulmc.bank.ui.event.AuthEventBus;
import ru.ulmc.bank.ui.event.UiEvents;
import ru.ulmc.bank.ui.view.LoginView;

import javax.servlet.annotation.WebServlet;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@SpringUI(path = "/auth/*")
@Title("Система Управления Котировками")
@Theme("bank")
public class LoginUI extends UI {

    @Autowired
    private AuthenticationController authenticationController;

    private static Text text = new RuText();

    private final AuthEventBus authEventBus = new AuthEventBus();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        AuthEventBus.register(this);
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);
        setContent(new LoginView());
        addStyleName("loginview");
        Page.getCurrent().addBrowserWindowResizeListener(
                (Page.BrowserWindowResizeListener) event -> AuthEventBus.post(new UiEvents.BrowserResizeEvent()));
    }

    @Subscribe
    public void userLoginRequested(UiEvents.UserLoginRequestedEvent event) {
        boolean success = false;
        try {
            User user = authenticationController.authenticate(event.getUserName(), event.getPassword());
            VaadinSession.getCurrent().setAttribute(User.class.getName(), user);
            success = true;
            //todo: redirect
        } catch (AuthenticationException e) {

            final MessageWindow window = new MessageWindow(text.authErrorHeader());
            window.setText(e.isSystemFault() ?
                    text.authErrorSystemFault(e.getMessage()) :
                    text.authErrorBaseText());
            window.setAssistiveRole(WindowRole.ALERTDIALOG);

            addWindow(window);

        } finally {
            AuthEventBus.post(new UiEvents.UserLoginResponseEvent(success));
        }
    }

    @Subscribe
    public void userLoggedOut(UiEvents.UserLoggedOutEvent event) {
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
    }

    public static AuthEventBus getEventBus() {
        return ((LoginUI) getCurrent()).authEventBus;
    }


    public static Text getTextProvider() {
        return text;
    }

    @WebServlet(urlPatterns = {"/auth/*", "/VAADIN/*"}, name = "AuthServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = LoginUI.class, productionMode = false)
    public static class AuthServlet extends SpringVaadinServlet {
        {
            setServiceUrlPath("/auth/*");
        }
    }

}
