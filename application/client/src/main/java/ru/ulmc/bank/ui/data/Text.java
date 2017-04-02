package ru.ulmc.bank.ui.data;

/**
 * Created by User on 02.04.2017.
 */
public interface Text {
    String userName();
    String password();
    String singIn();
    String logout();
    String appFullName();
    String appShortNameHtml();
    String authErrorBaseText();
    String authErrorHeader();
    String authErrorSystemFault(String errorText);

}
