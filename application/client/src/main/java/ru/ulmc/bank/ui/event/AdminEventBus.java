package ru.ulmc.bank.ui.event;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import ru.ulmc.bank.ui.AdminUI;

/**
 * Created by User on 01.04.2017.
 */
public class AdminEventBus implements SubscriberExceptionHandler {

    private final EventBus eventBus = new EventBus(this);

    public static void post(final Object event) {
        AdminUI.getEventBus().eventBus.post(event);
    }

    public static void register(final Object object) {
        AdminUI.getEventBus().eventBus.register(object);
    }

    public static void unregister(final Object object) {
        AdminUI.getEventBus().eventBus.unregister(object);
    }

    @Override
    public final void handleException(final Throwable exception,
                                      final SubscriberExceptionContext context) {
        exception.printStackTrace();
    }
}