package ru.ulmc.bank.ui.view;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ulmc.bank.server.controller.Controllers;

@SpringComponent
public class MainView extends HorizontalLayout {
    private final Controllers controllers;

    @Autowired
    public MainView(Controllers controllers) {
        this.controllers = controllers;

        setSizeFull();
        addStyleName("mainview");

        addComponent(new NavigationMenu());

        ComponentContainer content = new CssLayout();
        content.addStyleName("view-content");
        content.setSizeFull();
        addComponent(content);
        setExpandRatio(content, 1.0f);

        Button btn = new Button("Do somthing stupid");
        btn.addClickListener(event -> {
            controllers.getCurrencyController().getCurrency("RUB");
        });
        addComponent(btn);
        //   new DashboardNavigator(content);
    }
}