package com.soa.views.dialog;

import com.soa.model.Watch;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WatchDetailDialog extends Dialog {

    private static final Logger logger = LoggerFactory.getLogger(WatchDetailDialog.class.getName());

    public WatchDetailDialog(Watch watch) {
        setWidth("500px");
        setHeaderTitle("Dettaglio Orologio");
        setModal(true);
        setDraggable(false);
        setResizable(false);

        VerticalLayout layout = getVerticalLayout(watch);
        Button closeBtn = new Button("Chiudi", buttonClickEvent -> close());
        HorizontalLayout buttons = new HorizontalLayout(closeBtn);
        layout.add(buttons);
        add(layout);
    }

    @NotNull
    private static VerticalLayout getVerticalLayout(Watch watch) {

        TextField model = new TextField("Modello");
        model.setValue(watch.getModel());
        model.setReadOnly(true);
        model.setWidthFull();

        TextField reference = new TextField("Referenza");
        reference.setWidthFull();
        reference.setReadOnly(true);
        reference.setValue(watch.getReferenceId());

        TextField producer = new TextField("Marchio");
        producer.setWidthFull();
        producer.setReadOnly(true);
        producer.setValue(watch.getProducer().getName());

        TextField movement = new TextField("Movimento");
        movement.setWidthFull();
        movement.setReadOnly(true);
        movement.setValue(watch.getMovement().getValue());

        TextField datePurchase = new TextField("Data di acquisto");
        datePurchase.setWidthFull();
        datePurchase.setReadOnly(true);
        datePurchase.setValue(String.valueOf(watch.getPurchaseDate()));

        return new VerticalLayout(model, reference, producer, movement, datePurchase);
    }
}
