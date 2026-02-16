package com.soa.views.home;

import com.soa.model.User;
import com.soa.model.Watch;
import com.soa.service.WatchService;
import com.soa.views.dialog.WatchDialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
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
        Button addButton = new Button("Aggiungi Orologio", e -> openDialog(new Watch()));
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout toolbar = new HorizontalLayout(addButton);
        toolbar.setWidthFull();
        toolbar.setJustifyContentMode(JustifyContentMode.END);

        add(toolbar);
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
                .setHeader("Producer")
                .setAutoWidth(true);

        grid.addColumn(w -> w.getUser().getEmail())
                .setHeader("User")
                .setAutoWidth(true);

        grid.addColumn(Watch::getPurchaseDate)
                .setHeader("Purchase Date")
                .setAutoWidth(true);

        // Colonna con pulsanti
        grid.addComponentColumn(watch -> {
            Button edit = new Button("Modifica", e -> editWatch(watch));
            Button delete = new Button("Elimina", e -> deleteWatch(watch));

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
