package com.soa.service;

import com.soa.model.Role;
import com.soa.model.User;
import com.soa.repository.UserRepository;
import com.soa.views.home.HomeView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.VaadinSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    public record AuthorizedRoute(String route, String name, Class<? extends Component> view) {}

    public static class AuthException extends Exception {
        public AuthException(String message) {
            super(message);
        }
    }

    private final UserRepository userRepository;

    public void authenticate(String username, String password) throws AuthException {
        User user = userRepository.getByUsername(username);

        if (user != null && user.checkPassword(password)) {
            // Salva l'utente in sessione
            VaadinSession.getCurrent().setAttribute(User.class, user);

            // Configura le rotte autorizzate
            createRoutes(user.getRole());
        } else {
            throw new AuthException("Credenziali non valide");
        }
    }

    private void createRoutes(Role role) {
        // Aggiunge quelle permesse
        getAuthorizedRoutes(role).forEach(route ->
                RouteConfiguration.forSessionScope().setRoute(route.route(), route.view())
        );
    }

    public List<AuthorizedRoute> getAuthorizedRoutes(Role role) {
        var routes = new ArrayList<AuthorizedRoute>();

        switch (role) {
            case USER -> routes.add(new AuthorizedRoute("home", "Home", HomeView.class));
            case ADMIN -> {
                routes.add(new AuthorizedRoute("home", "Home", HomeView.class));
                // Qui puoi aggiungere rotte extra per admin, esempio:
                // routes.add(new AuthorizedRoute("admin", "Admin Dashboard", AdminView.class));
            }
        }

        return routes;
    }
}
