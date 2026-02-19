package com.soa.views.home;

import com.soa.model.User;
import com.soa.model.Watch;
import com.soa.service.WatchService;
import com.soa.views.dialog.WatchDialog;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@PageTitle("Home")
@Route("home")
public class HomeView extends VerticalLayout {

    private final WatchService watchService;
    private final Grid<Watch> grid = new Grid<>(Watch.class, false);

    public HomeView(WatchService watchService) {
        this.watchService = watchService;

        setSizeFull();
        configureToolbar();
        H2 header = new H2("Lista Orologi");
        add(header);

        configureGrid();
        add(grid);

        loadData();
    }

    private void configureToolbar() {
        Button addButton = new Button(new Icon(VaadinIcon.PLUS), e -> openDialog(new Watch()));
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button logoutButton = new Button(new Icon(VaadinIcon.SIGN_OUT), e -> logout());
        logoutButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout toolbar = new HorizontalLayout(addButton, logoutButton);
        toolbar.setWidthFull();
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        add(toolbar);
    }

    private void logout() {
        VaadinSession.getCurrent().setAttribute(User.class, null);
        VaadinSession.getCurrent().close();
        UI.getCurrent().navigate("/");
    }

    private void openDialog(Watch watch) {
        WatchDialog dialog = new WatchDialog(watchService, watch, this::loadData);
        dialog.open();
    }

    private void configureGrid() {

        grid.addColumn(Watch::getId)
                .setHeader("ID")
                .setAutoWidth(true);

        grid.addColumn(Watch::getReferenceId)
                .setHeader("Reference")
                .setAutoWidth(true);

        grid.addColumn(w -> w.getProducer().getName())
                .setHeader("Marchio")
                .setAutoWidth(true);

        grid.addColumn(w -> w.getProducer().getName())
                .setHeader("Modello")
                .setAutoWidth(true);

        grid.addColumn(Watch::getPurchaseDate)
                .setHeader("Data di acquisto")
                .setAutoWidth(true);

        // Colonna con pulsanti
        grid.addComponentColumn(watch -> {
            Button edit = new Button(new Icon(VaadinIcon.EDIT), e -> editWatch(watch));
            edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            Button delete = new Button(new Icon(VaadinIcon.TRASH), e -> deleteWatch(watch));
            delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
            delete.getElement().setProperty("title", "Cancella");
            edit.getElement().setProperty("title", "Modifica");
            return new HorizontalLayout(edit, delete);
        }).setHeader("Azioni");

        grid.setSizeFull();
    }

    private void loadData() {
        User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
        if (currentUser != null) {
            grid.setItems(watchService.findAll(currentUser.getEmail()));
        } else {
            grid.setItems(watchService.findAll());
        }
    }

    private void editWatch(Watch watch) {
        Notification.show("Modifica watch ID: " + watch.getId());
        // Qui puoi aprire una Dialog o navigare a una EditView
    }

    private void deleteWatch(Watch watch) {
        watchService.delete(watch.getId());
        loadData();
        Notification.show("Orologio eliminato");
    }
}
