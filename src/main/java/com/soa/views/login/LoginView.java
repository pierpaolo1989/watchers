package com.soa.views.login;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
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
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.Anchor; // aggiungi in cima agli import
import org.vaadin.lineawesome.LineAwesomeIconUrl;


@PageTitle("Login")
@Route("/login")
public class LoginView extends VerticalLayout {

    private EmailField emailField;
    private PasswordField passwordField;
    private Button loginButton;
    private Binder<LoginData> binder;

    public LoginView() {
        createUI();
        setupValidation();
        setupEventHandlers();
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
        H2 title = new H2("Accedi");
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

        // Button login
        loginButton = new Button("Accedi");
        loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        loginButton.setWidthFull();
        loginButton.getStyle().set("margin-top", "1rem");

        // Link per registrazione
        Anchor registerLink = new Anchor("registration", "Non sei registrato? Registrati");
        registerLink.getStyle()
                .set("display", "block")
                .set("text-align", "center")
                .set("margin-top", "1rem")
                .set("color", "#0d6efd")
                .set("text-decoration", "none");

        // Aggiungi hover effect (opzionale)
        registerLink.getElement().getStyle().set("cursor", "pointer");

        // Aggiungi componenti al form
        formLayout.add(emailField, passwordField);

        // Aggiungi tutto al card
        loginCard.add(title, formLayout, loginButton, registerLink);

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

    private void setupEventHandlers() {
        // Click del button
        loginButton.addClickListener(event -> performLogin());

        // Enter key sul form
        Shortcuts.addShortcutListener(this, this::performLogin, Key.ENTER);

        // Abilita/disabilita button in base alla validità
        binder.addStatusChangeListener(event -> {
            loginButton.setEnabled(binder.isValid());
        });
    }

    private void performLogin() {
        try {
            LoginData loginData = new LoginData();
            binder.writeBean(loginData);

            // Simula autenticazione (sostituisci con la tua logica)
            if (authenticateUser(loginData.getEmail(), loginData.getPassword())) {
                Notification.show("Login effettuato con successo!", 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                // Reindirizza alla dashboard o pagina principale
                UI.getCurrent().navigate("dashboard");

            } else {
                Notification.show("Email o password non validi", 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }

        } catch (ValidationException e) {
            Notification.show("Controlla i dati inseriti", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private boolean authenticateUser(String email, String password) {
        // TODO: Implementa qui la tua logica di autenticazione
        // Esempio: chiamata a servizio, database, ecc.

        // Per ora, login demo
        return "admin@example.com".equals(email) && "password123".equals(password);
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