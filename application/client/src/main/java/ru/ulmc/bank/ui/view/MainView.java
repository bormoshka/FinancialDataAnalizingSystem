package ru.ulmc.bank.ui.view;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import ru.ulmc.bank.dao.entity.system.User;
import ru.ulmc.bank.pojo.ldap.LdapAttribute;
import ru.ulmc.bank.server.config.UserSession;
import ru.ulmc.bank.server.controller.Controllers;

import java.util.Collection;

@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MainView extends HorizontalLayout {
    private final Controllers controllers;


    @Autowired
    public MainView(Controllers controllers) {
        this.controllers = controllers;
    }

    public void initView(UserSession userSession) {
        setSizeFull();
        addStyleName("mainview");

        addComponent(new NavigationMenu());

        Grid<User> userGrid = new Grid<>();
        userGrid.addColumn(User::getLogin).setCaption("Имя пользователя");
        userGrid.addColumn(User::getFullName).setCaption("Полное имя");
        userGrid.addColumn(User::getRoles).setCaption("Роли");
        userGrid.addColumn(User::isEnabled).setCaption("Активен");
        userGrid.setResponsive(true);
        userGrid.setWidth("100%");
        userGrid.setHeight("160px");

        userGrid.setDataProvider(DataProvider.ofItems(userSession.getUser()));

        Collection<LdapAttribute> attrsMap = controllers.getLdapController().getUserAttributes(
                userSession.getUser().getLogin());
        Grid<LdapAttribute> attributesGrid = new Grid<>();
        attributesGrid.addColumn(LdapAttribute::getId).setCaption("Атрибут");
        attributesGrid.addColumn(LdapAttribute::getValue).setCaption("Значение");
        attributesGrid.setResponsive(true);
        attributesGrid.setWidth("100%");
        attributesGrid.setHeight("400px");

        attributesGrid.setDataProvider(DataProvider.fromStream(attrsMap.stream()));

        ComponentContainer content = new CssLayout();
        content.addStyleName("view-content");
        content.setSizeFull();
        content.addComponent(
                new VerticalLayout(
                        new Label("Локальные данные о текущем пользователе"),
                        userGrid,
                        new Label("Атрибуты пользователя из LDAP"),
                        attributesGrid));
        addComponent(content);
        setExpandRatio(content, 1.0f);
    }
}