package com.soa.views.login;

import com.soa.service.AuthService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Login")
@Route("login")
public class LoginView extends VerticalLayout {

    private EmailField emailField;
    private PasswordField passwordField;
    private Button loginButton;
    private Binder<LoginData> binder;

    public LoginView(AuthService authService) {
        createUI();
        setupValidation();
        setupEventHandlers(authService);
    }

    private void createUI() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle().set("background-color", "#f5f5f5");

        Div loginCard = new Div();
        loginCard.addClassName("login-card");
        loginCard.getStyle()
                .set("background", "white")
                .set("padding", "2rem")
                .set("border-radius", "8px")
                .set("box-shadow", "0 2px 10px rgba(0,0,0,0.1)")
                .set("width", "400px")
                .set("max-width", "90%");

        H2 title = new H2("Accedi");
        title.getStyle().set("text-align", "center").set("margin", "0 0 2rem 0");

        FormLayout formLayout = new FormLayout();

        emailField = new EmailField("Email");
        emailField.setPlaceholder("Inserisci la tua email");
        emailField.setWidthFull();
        emailField.setRequired(true);

        passwordField = new PasswordField("Password");
        passwordField.setPlaceholder("Inserisci la password");
        passwordField.setWidthFull();
        passwordField.setRequired(true);

        loginButton = new Button("Accedi");
        loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        loginButton.setWidthFull();
        loginButton.setEnabled(false); // disabilitato finché i campi non sono validi
        loginButton.getStyle().set("margin-top", "1rem");

        Anchor registerLink = new Anchor("registration", "Non sei registrato? Registrati");
        registerLink.getStyle()
                .set("display", "block")
                .set("text-align", "center")
                .set("margin-top", "1rem")
                .set("color", "#0d6efd")
                .set("text-decoration", "none")
                .set("cursor", "pointer");

        formLayout.add(emailField, passwordField);
        loginCard.add(title, formLayout, loginButton, registerLink);
        add(loginCard);
    }

    private void setupValidation() {
        binder = new Binder<>(LoginData.class);

        binder.forField(emailField)
                .withValidator(new EmailValidator("Inserisci un'email valida"))
                .bind(LoginData::getEmail, LoginData::setEmail);

        binder.forField(passwordField)
                .withValidator(password -> password != null && password.length() >= 6,
                        "La password deve essere di almeno 6 caratteri")
                .bind(LoginData::getPassword, LoginData::setPassword);

        binder.addStatusChangeListener(event -> loginButton.setEnabled(binder.isValid()));
    }

    private void setupEventHandlers(AuthService authService) {
        loginButton.addClickListener(event -> performLogin(authService));

        // ENTER fa login
        Shortcuts.addShortcutListener(this, () -> performLogin(authService), Key.ENTER);
    }

    private void performLogin(AuthService authService) {
        try {
            LoginData loginData = new LoginData();
            binder.writeBean(loginData);

            if (authenticateUser(authService, loginData.getEmail(), loginData.getPassword())) {
                showNotification("Login effettuato con successo!", NotificationVariant.LUMO_SUCCESS);
                UI.getCurrent().navigate("home"); // o "dashboard"
            } else {
                showNotification("Email o password non validi", NotificationVariant.LUMO_ERROR);
            }

        } catch (ValidationException e) {
            showNotification("Controlla i dati inseriti", NotificationVariant.LUMO_ERROR);
        }
    }

    private boolean authenticateUser(AuthService authService, String email, String password) {
        try {
            authService.authenticate(email, password);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void showNotification(String message, NotificationVariant variant) {
        Notification notification = Notification.show(message, 3000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(variant);
    }

    // DTO per binding
    public static class LoginData {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
