package com.soa.views.registration;

import com.soa.service.UserService;
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


@PageTitle("Registration")
@Route("/registration")
public class RegistrationView extends VerticalLayout {

    private EmailField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Button loginButton;
    private Binder<LoginData> binder;
    private UserService userService;

    public RegistrationView(UserService userService) {
        createUI();
        setupValidation();
        setupEventHandlers(userService);
    }

    private void createUI() {
        // Configurazione layout principale
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle().set("background-color", "#f5f5f5");

        // Container per il form
        Div loginCard = new Div();
        loginCard.addClassName("login-card");
        loginCard.getStyle()
                .set("background", "white")
                .set("padding", "2rem")
                .set("border-radius", "8px")
                .set("box-shadow", "0 2px 10px rgba(0,0,0,0.1)")
                .set("width", "400px")
                .set("max-width", "90%");

        // Titolo
        H2 title = new H2("Sign up");
        title.getStyle().set("text-align", "center").set("margin", "0 0 2rem 0");

        // Form layout
        FormLayout formLayout = new FormLayout();

        // Campo email
        emailField = new EmailField("Email");
        emailField.setPlaceholder("inserisci la tua email");
        emailField.setWidthFull();
        emailField.setRequired(true);
        emailField.setErrorMessage("Inserisci un'email valida");

        // Campo password
        passwordField = new PasswordField("Password");
        passwordField.setPlaceholder("inserisci la password");
        passwordField.setWidthFull();
        passwordField.setRequired(true);
        passwordField.setMinLength(6);
        passwordField.setErrorMessage("La password deve essere di almeno 6 caratteri");

        // Campo conferma password
        confirmPasswordField = new PasswordField("Conferma Password");
        confirmPasswordField.setPlaceholder("inserisci la password");
        confirmPasswordField.setWidthFull();
        confirmPasswordField.setRequired(true);
        confirmPasswordField.setMinLength(6);
        confirmPasswordField.setErrorMessage("Le due password devono coincidere");

        // Button login
        loginButton = new Button("Registrati");
        loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        loginButton.setWidthFull();
        loginButton.getStyle().set("margin-top", "1rem");

        Anchor anchor = new Anchor("login", "Hai già un account? Vai alla login");
        anchor.getStyle()
                .set("display", "block")
                .set("text-align", "center")
                .set("margin-top", "1rem")
                .set("color", "#0d6efd")
                .set("text-decoration", "none");

        // Aggiungi hover effect (opzionale)
        anchor.getElement().getStyle().set("cursor", "pointer");

        // Aggiungi componenti al form
        formLayout.add(emailField, passwordField, confirmPasswordField);

        // Aggiungi tutto al card
        loginCard.add(title, formLayout, loginButton, anchor);

        // Aggiungi il card al layout principale
        add(loginCard);
    }

    private void setupValidation() {
        binder = new Binder<>(LoginData.class);

        // Validazione email
        binder.forField(emailField)
                .withValidator(new EmailValidator("Inserisci un'email valida"))
                .bind(LoginData::getEmail, LoginData::setEmail);

        // Validazione password
        binder.forField(passwordField)
                .withValidator(password -> password != null && password.length() >= 6,
                        "La password deve essere di almeno 6 caratteri")
                .bind(LoginData::getPassword, LoginData::setPassword);
    }

    private void setupEventHandlers(UserService userService) {
        // Click del button
        loginButton.addClickListener(event -> performLogin(userService));

        // Enter key sul form
        Shortcuts.addShortcutListener(this, () -> performLogin(userService), Key.ENTER);

        // Abilita/disabilita button in base alla validità
        binder.addStatusChangeListener(event -> {
            loginButton.setEnabled(binder.isValid());
        });
    }

    private void performLogin(UserService userService) {
        try {
            LoginData loginData = new LoginData();
            binder.writeBean(loginData);

            userService.registerUser(loginData.getEmail(), loginData.getPassword());

            Notification.show("Registrazione completata!", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            // Redirect alla login dopo registrazione
            UI.getCurrent().navigate("login");

        } catch (IllegalArgumentException e) {
            Notification.show(e.getMessage(), 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        } catch (ValidationException e) {
            Notification.show("Controlla i dati inseriti", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    // Classe per il data binding
    public static class LoginData {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}