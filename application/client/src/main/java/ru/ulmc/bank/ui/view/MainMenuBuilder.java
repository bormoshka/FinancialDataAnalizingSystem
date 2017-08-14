package ru.ulmc.bank.ui.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Sizeable;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Меню верхней понели.
 */
public class MainMenuBuilder {
    private VerticalLayout verticalLayout;

    private LinkedList<MenuBar.MenuItem> lastItems = new LinkedList<>();
    private Map<String, MenuBar.MenuItem> viewNameToItem = new HashMap<>();
    private Map<String, MenuSupport> viewNameToMenuSupport = new HashMap<>();

    private MenuBar lastActionsBar;

    private MenuBar mainMenu;
    private MenuBar rightMenu;

    private boolean recentMenuBarShown = false;

    private SpringNavigator navigator;

    public MainMenuBuilder(SpringNavigator navigator) {
        this.navigator = navigator;
        mainMenu = new MenuBar();
        mainMenu.addStyleName("square-and-flat");
        mainMenu.setWidth(100, Sizeable.Unit.PERCENTAGE);
        mainMenu.setAutoOpen(true);

        lastActionsBar = new MenuBar();
        lastActionsBar.addStyleName("recent-menu");
        // lastActionsBar.setHeight("26px");
        lastActionsBar.setVisible(recentMenuBarShown);
        lastActionsBar.setWidth(100, Sizeable.Unit.PERCENTAGE);
        setupMainMenuItems();
    }

    public MainMenuBuilder addReferenceGroup() {
        setupReferenceMenu();
        return this;
    }

    public MainMenuBuilder addOtherGroups() {
        setupMenuItems();
        return this;
    }

    public MainMenuBuilder addRightMenu(String userName, final UI ui) {
        rightMenu = new MenuBar();
        rightMenu.addStyleName("square-and-flat");
        setupRightMenuItems(userName, ui);
        return this;
    }

    public AbstractLayout build() {
        setupFinalLayout();
        disableInitialStateMenuItem();
        return verticalLayout;
    }

    private void setupFinalLayout() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.setWidth(100, Sizeable.Unit.PERCENTAGE);
        gridLayout.setColumns(2);
        gridLayout.setRows(1);
        gridLayout.addComponent(mainMenu);
        gridLayout.addComponent(rightMenu);
        gridLayout.setComponentAlignment(rightMenu, Alignment.MIDDLE_RIGHT);
        gridLayout.setColumnExpandRatio(0, 0.8f);
        //gridLayout.markAsDirtyRecursive();
        verticalLayout = new VerticalLayout(gridLayout, lastActionsBar);
        verticalLayout.setStyleName("no-padding");
        verticalLayout.setSpacing(false);
    }

    private void disableInitialStateMenuItem() {
        if (navigator == null || navigator.getState() == null)
            return;
        viewNameToItem.get(navigator.getState()).setEnabled(false);
    }

    private void setupMainMenuItems() {
        MenuBar.MenuItem brand = mainMenu.addItem("FXP", VaadinIcons.MONEY_EXCHANGE, null);
        brand.setEnabled(false);
        brand.setStyleName("brand");
        brand.setDescription("Высокотехнологичное, инновационное приложение по работе с курсами валют");
    }

    private void setupMenuItems() {
        mainMenu.addItem("Menu 2", null, null);
        mainMenu.addItem("Menu 3", null, null);
    }

    private void setupRightMenuItems(String userName, final UI ui) {
        MenuBar.MenuItem user = rightMenu.addItem(userName, VaadinIcons.USER, null);
        user.setEnabled(false);
        user.setDescription("Your username");

        MenuBar.MenuItem recentMenu = rightMenu.addItem("", VaadinIcons.CLOCK, this::toggleRecentMenuBar);
        recentMenu.setCheckable(true);
        recentMenu.setChecked(recentMenuBarShown);
        recentMenu.setDescription("Отображение последних переходов");

        MenuBar.MenuItem exit = rightMenu.addItem("Log out", VaadinIcons.SIGN_OUT,
                selectedItem -> ui.getPage().setLocation("logout"));
    }

    private void toggleRecentMenuBar(MenuBar.MenuItem switcher) {
        recentMenuBarShown = switcher.isChecked();
        lastActionsBar.setVisible(recentMenuBarShown);
        addToLastItemsMenuBar(null);
    }

    private void setupReferenceMenu() {
        MenuBar.MenuItem refMenu = mainMenu.addItem("Справочники", VaadinIcons.BOOK, null);

        MenuBar.Command cmd = this::addToLastItemsMenuBar;
       // createSubmenuItem(refMenu, CurrencyPairsView.MENU_SUPPORT);
        //createSubmenuItem(refMenu, SymbolsView.MENU_SUPPORT);

        refMenu.addItem("Reference 3", cmd);
        refMenu.addItem("Reference 4", cmd);
        refMenu.addItem("Reference 5", cmd);
        refMenu.addItem("Reference 6", cmd);
        refMenu.addItem("Reference 7", cmd);
    }

    private void createSubmenuItem(MenuBar.MenuItem parent, MenuSupport menuSupport) {
        viewNameToItem.put(menuSupport.getName(), parent.addItem(menuSupport.getTitle(), item -> {
            commonMenuActions(item, menuSupport);
        }));
        viewNameToMenuSupport.put(menuSupport.getName(), menuSupport);
    }

    public MenuSupport getMenuSupport(String viewName) {
        return viewNameToMenuSupport.get(viewName);
    }

    private void commonMenuActions(MenuBar.MenuItem item, MenuSupport menuSupport) {
        if (menuSupport.getName().equals(navigator.getState())) {
            return;
        }
        viewNameToItem.get(navigator.getState()).setEnabled(true);
        item.setEnabled(false);

        navigator.navigateTo(menuSupport.getName());
        navigator.getCurrentView().getViewComponent().getParent().setCaption(menuSupport.getTitle());
        addToLastItemsMenuBar(item);
    }

    private void addToLastItemsMenuBar(MenuBar.MenuItem item) {
        if (item != null) {
            lastItems.remove(findCandidate(lastItems, item));
        }
        if (recentMenuBarShown) {
            lastActionsBar.removeItems();
            for (MenuBar.MenuItem i : lastItems) {
                lastActionsBar.addItem(i.getText(), i.getIcon(), i.getCommand());
            }
        }
        if (item != null) {
            lastItems.add(0, item);
        }
        while (lastItems.size() > 12) {
            lastItems.remove(lastItems.size() - 1);
        }
    }

    private MenuBar.MenuItem findCandidate(List<MenuBar.MenuItem> children, MenuBar.MenuItem candidate) {
        for (MenuBar.MenuItem item : children) {
            if (item.getText().equals(candidate.getText())) {
                return item;
            }
        }
        return null;
    }
}
