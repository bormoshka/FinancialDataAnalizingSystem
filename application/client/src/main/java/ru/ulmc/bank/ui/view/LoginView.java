package ru.ulmc.bank.ui.view;

import com.google.common.eventbus.Subscribe;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ulmc.bank.server.controller.Controllers;
import ru.ulmc.bank.ui.AppUI;
import ru.ulmc.bank.ui.data.Text;
import ru.ulmc.bank.ui.event.UiEventBus;
import ru.ulmc.bank.ui.event.UiEvents;

public class LoginView extends VerticalLayout {
    private static final Logger logger = LoggerFactory.getLogger(LoginView.class);
    private Text text;

    private Button signIn;

    private Controllers controllers;

    public LoginView(Controllers controllers) {
        this.controllers = controllers;
        text = AppUI.getTextProvider();
        setSizeFull();
        UiEventBus.register(this);
        Component loginForm = buildLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
        Button btn = new Button("Do somthing stupid");
        btn.addClickListener(event -> {
            controllers.getCurrencyController().getCurrency("RUB");
        });
        addComponent(btn);
    }

    private Component buildLoginForm() {
        VerticalLayout vl = new VerticalLayout();
        vl.setSpacing(true);
        vl.addStyleName("userName-panel");
        vl.addComponent(buildFields());

        Panel panel = new Panel(vl);
        panel.setWidth("400px");
        Responsive.makeResponsive(panel);
        return panel;
    }

    private Component buildFields() {
        VerticalLayout fields = new VerticalLayout();
        fields.setSpacing(true);
        fields.addStyleName("no-top-padding");
        fields.addStyleName("fields");

        Label title = new Label(text.appFullName());
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_LIGHT);


        TextField username = new TextField(text.userName());
        username.setIcon(VaadinIcons.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        username.setWidth("100%");
        username.focus();

        PasswordField password = new PasswordField(text.password());
        password.setIcon(VaadinIcons.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        password.setWidth("100%");

        signIn = new Button(text.singIn());
        signIn.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signIn.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        signIn.setWidth("100%");
        signIn.setDisableOnClick(true);
        fields.addComponents(title, username, password, signIn);

        signIn.addClickListener((Button.ClickListener) event -> {
            UiEventBus.post(new UiEvents.UserLoginRequestedEvent(username.getValue(), password.getValue()));
        });
        return fields;
    }

    @Subscribe
    public void userAction(UiEvents.UserLoginResponseEvent event) {
        signIn.setEnabled(true);
    }

}