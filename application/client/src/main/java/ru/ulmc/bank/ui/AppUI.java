package ru.ulmc.bank.ui;

import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.*;
import com.vaadin.shared.ui.window.WindowRole;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.security.VaadinSecurity;

import ru.ulmc.bank.core.common.exception.AuthenticationException;
import ru.ulmc.bank.dao.entity.system.User;
import ru.ulmc.bank.server.config.UserSession;
import ru.ulmc.bank.server.controller.Controllers;
import ru.ulmc.bank.ui.common.MessageWindow;
import ru.ulmc.bank.ui.data.Text;
import ru.ulmc.bank.ui.data.provider.RuText;
import ru.ulmc.bank.ui.event.UiEventBus;
import ru.ulmc.bank.ui.event.UiEvents;
import ru.ulmc.bank.ui.view.HomeView;
import ru.ulmc.bank.ui.view.LoginView;
import ru.ulmc.bank.ui.view.MainMenuBuilder;

import javax.servlet.ServletException;
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
    private final static Logger LOGGER = Logger.getLogger(AppUI.class.getName());
    private static Text text = new RuText();
    private final UiEventBus uiEventBus = new UiEventBus();
    @Autowired
    private Controllers controllers;
    @Autowired
    private SpringViewProvider viewProvider;
    @Autowired
    private VaadinSecurity vaadinSecurity;
    @Autowired
    private SpringNavigator navigator;

    private MainMenuBuilder menu;
    private Panel content = new Panel();
    private VerticalLayout topLevelLayout = new VerticalLayout();

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
        if (isUserAuthenticated()) {
            topLevelLayout.addStyleName("no-padding");
            topLevelLayout.setSpacing(false);
            topLevelLayout.setSizeFull();
            //content.addStyleName("content-padding");
            setContent(topLevelLayout);
            navigator.init(this, content);
            navigator.addProvider(viewProvider);
            if (navigator.getState() == null || navigator.getState().isEmpty()) {
                navigator.navigateTo(HomeView.NAME);
            } else {
                navigator.navigateTo(navigator.getState());
            }
            initMenu();
            initContent();
        } else {
            setContent(new LoginView(vaadinSecurity));
        }
    }

    private void initContent() {
        //content.addStyleName("content-body");
        content.setSizeFull();
        content.setCaption(menu.getMenuSupport(navigator.getState()).getTitle());
        VerticalLayout vl = new VerticalLayout(content);
        vl.setSpacing(false);
        vl.setSizeFull();
        topLevelLayout.addComponent(vl);
        topLevelLayout.setExpandRatio(vl, 10);
    }

    private void initMenu() {
        menu = new MainMenuBuilder(navigator);
        AbstractLayout menuBar = menu.addReferenceGroup()
                .addOtherGroups()
                .addRightMenu(getUserName(), this)
                .build();
        topLevelLayout.addComponent(menuBar);
        topLevelLayout.setExpandRatio(menuBar, 0);

    }

    private boolean isUserAuthenticated() {
        return isAuthEnabled()
                && vaadinSecurity.isAuthenticated()
                && !vaadinSecurity.isAuthenticatedAnonymously();
    }

    private boolean isAuthEnabled() {
        return vaadinSecurity.getAuthentication() != null;
    }

    private String getUserName() {
        String userName = "unknown";
        try {
            if (vaadinSecurity.getAuthentication() != null) {
                userName = vaadinSecurity.getAuthentication().getName();
            } else {
                LOGGER.warn("Аутентификация отключена");
            }
        } catch (Exception ex) {
            LOGGER.error("Не удалось получить имя пользователя", ex);
        }

        return userName;
    }

    @WebServlet(urlPatterns = {"/*", "/VAADIN/*"}, name = "AppServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = AppUI.class, productionMode = false)
    public static class AppServlet extends SpringVaadinServlet {
        @Override
        protected void servletInitialized() throws ServletException {
            super.servletInitialized();
            getService().setSystemMessagesProvider((SystemMessagesProvider) systemMessagesInfo -> {
                CustomizedSystemMessages messages = new CustomizedSystemMessages();
                // Don't show any messages, redirect immediately to the session expired URL
                messages.setSessionExpiredNotificationEnabled(false);
                // Don't show any message, reload the page instead
                messages.setCommunicationErrorNotificationEnabled(false);
                return messages;
            });
        }
    }

}
