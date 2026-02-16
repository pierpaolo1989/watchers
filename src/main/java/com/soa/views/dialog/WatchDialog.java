package com.soa.views.dialog;

import com.soa.model.Watch;
import com.soa.service.WatchService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public class WatchDialog extends Dialog {

    private final WatchService watchService;
    private final Binder<Watch> binder = new Binder<>(Watch.class);

    public WatchDialog(WatchService watchService,
                       Watch watch,
                       Runnable onSaveCallback) {

        this.watchService = watchService;

        setWidth("400px");

        TextField referenceId = new TextField("Reference ID");
        DatePicker purchaseDate = new DatePicker("Purchase Date");

        Button save = new Button("Salva");
        Button cancel = new Button("Annulla", e -> close());

        HorizontalLayout buttons = new HorizontalLayout(save, cancel);

        VerticalLayout layout = new VerticalLayout(
                referenceId,
                purchaseDate,
                buttons
        );

        add(layout);

        // Binding campi
        binder.forField(referenceId)
                .asRequired("Obbligatorio")
                .bind(Watch::getReferenceId, Watch::setReferenceId);

        binder.forField(purchaseDate)
                .bind(Watch::getPurchaseDate, Watch::setPurchaseDate);

        binder.readBean(watch);

        save.addClickListener(e -> {
            try {
                binder.writeBean(watch);
                //watchService.save(watch);
                onSaveCallback.run();
                close();
            } catch (ValidationException ex) {
                ex.printStackTrace();
            }
        });
    }
}
