package com.soa.views.dialog;

import com.soa.model.Producer;
import com.soa.model.User;
import com.soa.model.Watch;
import com.soa.model.enums.MovementEnum;
import com.soa.service.ProducerService;
import com.soa.service.WatchService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.server.VaadinSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class WatchDialog extends Dialog {

    private static final Logger logger = LoggerFactory.getLogger(WatchDialog.class.getName());

    private final WatchService watchService;
    private final ProducerService producerService;
    private final Binder<Watch> binder = new Binder<>(Watch.class);

    public WatchDialog(WatchService watchService,
                       ProducerService producerService,
                       Watch watch,
                       Runnable onSaveCallback) {

        this.watchService = watchService;
        this.producerService = producerService;

        setWidth("500px");
        setHeaderTitle("Dettagli Orologio");
        setModal(true);
        setDraggable(false);
        setResizable(false);

        TextField model = new TextField("Modello");
        model.setWidthFull();
        model.getStyle().set("font-size", "16px");

        TextField reference = new TextField("Reference");
        reference.setWidthFull();
        reference.getStyle().set("font-size", "16px");

        DatePicker purchaseDate = new DatePicker("Data di acquisto");
        purchaseDate.setWidthFull();

        ComboBox<Producer> producer = new ComboBox<>("Marchio");
        producer.setItems(producerService.findAll());
        producer.setItemLabelGenerator(Producer::getName);
        producer.setWidthFull();

        ComboBox<MovementEnum> movement = new ComboBox<>("Movimento");
        movement.setItems(Arrays.stream(MovementEnum.values()).toList());
        movement.setItemLabelGenerator(MovementEnum::name);
        movement.setWidthFull();

        Button save = new Button("Salva");
        Button cancel = new Button("Annulla", e -> close());

        HorizontalLayout buttons = new HorizontalLayout(save, cancel);

        VerticalLayout layout = new VerticalLayout(
                model,
                reference,
                purchaseDate,
                producer,
                movement,
                buttons
        );

        add(layout);

        binder.forField(model)
                .asRequired("Obbligatorio")
                .bind(Watch::getModel, Watch::setModel);
        binder.forField(movement)
                .asRequired("Obbligatorio")
                .bind(Watch::getMovement, Watch::setMovement);
        binder.forField(producer)
                .asRequired("Obbligatorio")
                .bind(Watch::getProducer, Watch::setProducer);
        binder.forField(reference)
                .asRequired("Obbligatorio")
                .bind(Watch::getReferenceId, Watch::setReferenceId);
        binder.forField(purchaseDate)
                .bind(Watch::getPurchaseDate, Watch::setPurchaseDate);
        binder.readBean(watch);

        save.addClickListener(e -> {
            try {
                binder.writeBean(watch);
                User currentUser = VaadinSession.getCurrent().getAttribute(User.class);
                if (currentUser !=null) {
                    watch.setUser(currentUser);
                }
                watchService.save(watch);
                onSaveCallback.run();
                close();
            } catch (ValidationException ex) {
                logger.error("Impossible to save watch. Error: {}", ex.getMessage());
                ex.printStackTrace();
            }
        });
    }
}
