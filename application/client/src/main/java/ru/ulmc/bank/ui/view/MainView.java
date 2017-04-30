package ru.ulmc.bank.ui.view;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
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



        TextField tf = new TextField();

        Button btn = new Button("Do somthing stupid");
        btn.addClickListener(event -> {
            controllers.getPublishingController().publish(tf.getValue());
        });
        ComponentContainer content = new CssLayout();
        content.addStyleName("view-content");
        content.setSizeFull();
        content.addComponent(new VerticalLayout(tf, btn));
        addComponent(content);
        setExpandRatio(content, 1.0f);
        //   new DashboardNavigator(content);
    }
}