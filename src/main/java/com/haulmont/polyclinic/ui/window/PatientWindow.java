/*
 * Copyright 2020 Arthur Sadykov.
 *
 * Тестовое задание Haulmont.
 * Cистема ввода и отображения информации о рецептах поликлиники.
 */
package com.haulmont.polyclinic.ui.window;

import com.haulmont.polyclinic.dao.DAO;
import com.haulmont.polyclinic.dao.DAOFactory;
import com.haulmont.polyclinic.dto.Patient;
import com.haulmont.polyclinic.util.SaveListener;
import com.vaadin.data.Binder;
import static com.vaadin.server.Sizeable.Unit.PIXELS;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import static java.util.Optional.ofNullable;
import java.util.logging.Logger;

/**
 * Форма для добавления или редактирования пациента
 *
 * @author Arthur Sadykov
 * @version 1.0
 * @since 1.0
 */
public class PatientWindow extends Window {

    private static final String CANCEL = "Отменить";
    private static final String FIRST_NAME = "Имя";
    private static final String LAST_NAME = "Фамилия";
    private static final Logger LOG =
            Logger.getLogger(PatientWindow.class.getName());
    private static final String OK = "OK";
    private static final String PATRONYMIC = "Отчество";
    private static final String PHONE_NUMBER = "Номер телефона";
    private static final String POLYCLINIC = "polyclinic";
    private static final String REQUIRED_FIELD = "Обязательное поле";
    private static final long serialVersionUID = 1L;
    private final Binder<Patient> binder = new Binder<>();
    private final TextField firstNameField;
    private final TextField lastNameField;
    private final TextField patronymicField;
    private final TextField phoneNumberField;

    /**
     * Создает форму для добавления или редактирования пациента
     *
     * @param caption Заголовок формы
     * @param patient Пациент (при передаче null создается форма для добавления,
     * иначе - форма для редактирования)
     * @param saveListener Передает в основную форму информацию о добавлении или
     * обновлении сущности в хранилище данных
     */
    public PatientWindow(String caption, Patient patient,
            SaveListener saveListener) {
        super(caption);
        firstNameField = new TextField(FIRST_NAME);
        firstNameField.setSizeFull();
        lastNameField = new TextField(LAST_NAME);
        lastNameField.setSizeFull();
        patronymicField = new TextField(PATRONYMIC);
        patronymicField.setSizeFull();
        phoneNumberField = new TextField(PHONE_NUMBER);
        phoneNumberField.setSizeFull();
        Button okButton = new Button(OK, event -> {
            DAO<Patient> patientDAO =
                    DAOFactory.getInstance(POLYCLINIC).getPatientDAO();
            patientDAO.beginTransaction();
            ofNullable(patient).ifPresentOrElse((p) -> {
                binder.writeBeanIfValid(patient);
                patientDAO.update(patient);
                saveListener.save(patient);
                close();
            }, () -> {
                Patient p = new Patient();
                binder.writeBeanIfValid(p);
                patientDAO.add(p);
                saveListener.save(p);
                close();
            });
            patientDAO.commitTransaction();
        });
        Button cancelButton = new Button(CANCEL, event -> {
            ofNullable(patient)
                    .ifPresentOrElse((p) -> {
                        binder.readBean(patient);
                    }, () -> {
                        firstNameField.clear();
                        lastNameField.clear();
                        patronymicField.clear();
                        phoneNumberField.clear();
                    });
        });
        binder.addStatusChangeListener((event) -> {
            boolean isValid = event.getBinder().isValid();
            okButton.setEnabled(isValid);
            cancelButton.setEnabled(!firstNameField.isEmpty()
                                    || !lastNameField.isEmpty()
                                    || !patronymicField.isEmpty()
                                    || !phoneNumberField.isEmpty());
        });
        binder.forField(firstNameField)
                .asRequired(REQUIRED_FIELD)
                .bind(Patient::getFirstName, Patient::setFirstName);
        binder.forField(lastNameField)
                .asRequired(REQUIRED_FIELD)
                .bind(Patient::getLastName, Patient::setLastName);
        binder.forField(patronymicField)
                .bind(Patient::getPatronymic, Patient::setPatronymic);
        binder.forField(phoneNumberField)
                .asRequired(REQUIRED_FIELD)
                .bind(Patient::getPhoneNumber, Patient::setPhoneNumber);
        ofNullable(patient).ifPresent((p) -> {
            binder.readBean(patient);
        });
        HorizontalLayout hl = new HorizontalLayout(okButton, cancelButton);
        VerticalLayout vl = new VerticalLayout(lastNameField, firstNameField,
                patronymicField, phoneNumberField, hl);
        super.setContent(vl);
        super.center();
        super.setWidth(400, PIXELS);
        super.setHeight(400, PIXELS);
        super.setModal(true);
    }
}
