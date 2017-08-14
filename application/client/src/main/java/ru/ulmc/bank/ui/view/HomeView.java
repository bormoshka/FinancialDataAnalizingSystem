package ru.ulmc.bank.ui.view;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.TextRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import elemental.json.JsonValue;
import ru.ulmc.bank.pojo.CurrencyPairDto;
import ru.ulmc.bank.server.controller.CurrencyController;

/**
 * Представление справочника валютных пар
 */
@SpringView(name = HomeView.NAME)
public class HomeView extends CustomComponent implements View {
    public static final String NAME = "home";
    public static final MenuSupport MENU_SUPPORT = new MenuSupport(NAME, "FX-JB", VaadinIcons.MONEY_EXCHANGE);

    private static final Logger LOG = LoggerFactory.getLogger(HomeView.class);

    @PostConstruct
    public void initComponent() {
        setSizeFull();

    }

}
