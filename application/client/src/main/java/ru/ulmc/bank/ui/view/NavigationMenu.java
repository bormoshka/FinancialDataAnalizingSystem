package ru.ulmc.bank.ui.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import ru.ulmc.bank.dao.entity.system.User;
import ru.ulmc.bank.ui.AppUI;
import ru.ulmc.bank.ui.data.Text;
import ru.ulmc.bank.ui.event.UiEventBus;
import ru.ulmc.bank.ui.event.UiEvents;

/**
 * Created by User on 01.04.2017.
 */
public class NavigationMenu extends CustomComponent {

    public static final String ID = "navigation-menu";
    private static final String STYLE_VISIBLE = "valo-menu-visible";
    private MenuBar.MenuItem settingsItem;

    private Text text;

    public NavigationMenu() {
        text = AppUI.getTextProvider();
        setPrimaryStyleName("valo-menu");
        setId(ID);
        setSizeUndefined();

        setCompositionRoot(buildContent());
    }

    private Component buildContent() {
        final CssLayout menuContent = new CssLayout();
        menuContent.addStyleName("sidebar");
        menuContent.addStyleName(ValoTheme.MENU_PART);
        menuContent.addStyleName("no-vertical-drag-hints");
        menuContent.addStyleName("no-horizontal-drag-hints");
        menuContent.setWidth(null);
        menuContent.setHeight("100%");

        menuContent.addComponent(buildTitle());
        menuContent.addComponent(buildToggleButton());
        menuContent.addComponent(buildMenuItems());

        return menuContent;
    }

    private Component buildTitle() {
        Label logo = new Label(text.appShortNameHtml(), ContentMode.HTML);
        logo.setSizeUndefined();
        HorizontalLayout logoWrapper = new HorizontalLayout(logo);
        logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        logoWrapper.addStyleName("valo-menu-title");
        return logoWrapper;
    }

    private User getCurrentUser() {
        return (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
    }

    private Component buildToggleButton() {
        Button valoMenuToggleButton = new Button("Menu", (Button.ClickListener) event -> {
            if (getCompositionRoot().getStyleName().contains(STYLE_VISIBLE)) {
                getCompositionRoot().removeStyleName(STYLE_VISIBLE);
            } else {
                getCompositionRoot().addStyleName(STYLE_VISIBLE);
            }
        });
        valoMenuToggleButton.setIcon(FontAwesome.LIST);
        valoMenuToggleButton.addStyleName("valo-menu-toggle");
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_SMALL);
        return valoMenuToggleButton;
    }

    private Component buildMenuItems() {
        Button logoutBtn = new Button(text.logout(), VaadinIcons.EXIT);
        logoutBtn.addClickListener((Button.ClickListener) event -> UiEventBus.post(new UiEvents.UserLoggedOutEvent()));
        VerticalLayout vl = new VerticalLayout();
        vl.addComponent(new Button("Menu Item 1"));
        vl.addComponent(new Button("Menu Item 2"));
        vl.addComponent(new Button("Menu Item 3"));
        vl.addComponent(logoutBtn);
        return vl;
    }
}