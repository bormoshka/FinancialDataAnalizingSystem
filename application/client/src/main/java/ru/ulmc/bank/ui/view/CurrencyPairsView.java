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
@SpringView(name = CurrencyPairsView.NAME)
public class CurrencyPairsView extends CustomComponent implements View {
    public static final String NAME = "cpv";
    public static final MenuSupport MENU_SUPPORT = new MenuSupport(NAME, "Настройка валютных пар", VaadinIcons.COIN_PILES);

    private static final Logger LOG = LoggerFactory.getLogger(CurrencyPairsView.class);
    private final Grid<CurrencyPairDto> grid = new Grid<>();
    private VerticalLayout layout;

    @Autowired
    private CurrencyController currencyController;
    private Button btnDelete;
    private Button btnSave;
    private Button btnCancel;
    private Set<CurrencyPairDto> selectedItems = new HashSet<>();

    @PostConstruct
    public void initComponent() {
        setSizeFull();
        setupRoot();
        initGrid();
        initControls();
    }

    private void setupRoot() {
        layout = new VerticalLayout(grid);
        layout.setSpacing(true);
        layout.setMargin(false);
        layout.setSizeFull();
        layout.setExpandRatio(grid, 10);
        setCompositionRoot(layout);
    }

    private void initGrid() {
        grid.setSizeFull();

        MultiSelectionModel<CurrencyPairDto> selectionModel
                = (MultiSelectionModel<CurrencyPairDto>) grid.setSelectionMode(Grid.SelectionMode.MULTI);
        selectionModel.addSelectionListener(this::toggleDeleteBtn);
        HeaderRow header = grid.prependHeaderRow();
        header.join(
                grid.addColumn(CurrencyPairDto::getBase)
                        .setCaption("ISO1"),
                grid.addColumn(CurrencyPairDto::getQuoted)
                        .setCaption("ISO2")
        ).setText("Валютная пара (ISO1/ISO2)");

      /*  header.join(
                grid.addColumn(row -> row.getFirst()).setCaption("B1/S1"),
                grid.addColumn(row -> row.getSecond()).setCaption("B2/S2"),
                grid.addColumn(row -> row.getThird()).setCaption("B3/S3")
        ).setText("Коэффициенты");*/

        header.join(
                grid.addColumn(CurrencyPairDto::getCreateDate).setCaption("Создан")
                        .setRenderer(new DateRenderer(DateFormat.getDateTimeInstance()))
                        .setExpandRatio(2),
                grid.addColumn(CurrencyPairDto::getChangeDate).setCaption("Изменен")
                        .setRenderer(new DateRenderer(DateFormat.getDateTimeInstance()))
                        .setExpandRatio(2)
        ).setText("Дата");

        header.join(
                grid.addColumn(CurrencyPairDto::getCreatorName).setCaption("Создал").setExpandRatio(2),
                grid.addColumn(CurrencyPairDto::getModifierName).setCaption("Изменил").setExpandRatio(2)
        ).setText("ФИО");

        grid.addColumn(CurrencyPairDto::getActive)
                .setCaption("Активность")
                .setRenderer(getBooleanTxtRender())
                .setExpandRatio(2);

        grid.addColumn(CurrencyPairDto::getAvailable)
                .setCaption("Доступность")
                .setRenderer(getBooleanTxtRender())
                .setExpandRatio(2);

        grid.getEditor().setEnabled(true);
        grid.setDataProvider(DataProvider.fromCallbacks(
                query -> currencyController.getAllCurrencyPairs().stream(),
                query -> currencyController.getAllCurrencyPairs().size())); //todo: demo only! ref this!
    }

    private TextRenderer getBooleanTxtRender() {
        return new TextRenderer("Нет") {
            @Override
            public JsonValue encode(Object value) {
                return super.encode(value == null || !Boolean.valueOf(value.toString()) ? "Нет" : "Да");
            }
        };
    }

    private void initControls() {
        btnSave = createButton("Сохранить", this::onSave);
        btnDelete = createButton("Удалить", this::onDelete);
        btnCancel = createButton("Отменить", this::onCancel);
        initShortKeys();

        HorizontalLayout hl = new HorizontalLayout(btnSave, btnDelete, btnCancel);
        layout.addComponent(hl);
        layout.setComponentAlignment(hl, Alignment.MIDDLE_CENTER);
        layout.setExpandRatio(hl, 0);
    }

    private void initShortKeys() {
        btnSave.setClickShortcut(ShortcutAction.KeyCode.S, ShortcutAction.ModifierKey.CTRL, ShortcutAction.ModifierKey.SHIFT);
    }

    private void onSave(Button.ClickEvent event) {
        UI.getCurrent().addWindow(new Window());
        resetState();
    }

    private void onDelete(Button.ClickEvent event) {
        getUI().access(() -> {
            currencyController.removeCurrencyPairs(selectedItems);
            resetState();
        });
    }

    private void onCancel(Button.ClickEvent event) {
        resetState();
    }

    private void resetState() {
        grid.getSelectionModel().deselectAll();
        grid.getDataProvider().refreshAll(); // or  grid.getDataProvider().refreshItem(); ?
        btnSave.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(false);
    }

    private Button createButton(String title, Button.ClickListener clickListener) {
        Button btn = new Button(title);
        btn.setEnabled(false);
        btn.setDisableOnClick(true);
        btn.addClickListener(clickListener);
        return btn;
    }

    private void toggleDeleteBtn(SelectionEvent<CurrencyPairDto> event) {
        selectedItems.addAll(event.getAllSelectedItems());
        btnDelete.setEnabled(!selectedItems.isEmpty());

    }
}
