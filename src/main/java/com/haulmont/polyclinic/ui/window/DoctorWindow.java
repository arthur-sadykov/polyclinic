/*
 * Copyright 2020 Arthur Sadykov.
 *
 * Тестовое задание Haulmont.
 * Cистема ввода и отображения информации о рецептах поликлиники.
 */
package com.haulmont.polyclinic.ui.window;

import com.haulmont.polyclinic.dao.DAO;
import com.haulmont.polyclinic.dao.DAOFactory;
import com.haulmont.polyclinic.dto.Doctor;
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
 * Форма для добавления или редактирования врача
 *
 * @author Arthur Sadykov
 * @version 1.0
 * @since 1.0
 */
public class DoctorWindow extends Window {

    private static final String CANCEL = "Отменить";
    private static final String FIRST_NAME = "Имя";
    private static final String LAST_NAME = "Фамилия";
    private static final Logger LOG =
            Logger.getLogger(DoctorWindow.class.getName());
    private static final String OK = "OK";
    private static final String PATRONYMIC = "Отчество";
    private static final String POLYCLINIC = "polyclinic";
    private static final String REQUIRED_FIELD = "Обязательное поле";
    private static final String SPECIALIZATION = "Специализация";
    private static final long serialVersionUID = 1L;
    private final Binder<Doctor> binder = new Binder<>();
    private TextField firstNameField;
    private TextField lastNameField;
    private TextField patronymicField;
    private final SaveListener saveListener;
    private TextField specializationField;

    /**
     * Создает форму для добавления или редактирования врача
     *
     * @param caption Заголовок формы
     * @param doctor Врач (при передаче null создается форма для добавления,
     * иначе - форма для редактирования)
     * @param saveListener Передает в основную форму информацию о добавлении или
     * обновлении сущности в хранилище данных
     */
    public DoctorWindow(String caption, Doctor doctor,
            SaveListener saveListener) {
        super(caption);
        this.saveListener = saveListener;
        firstNameField = new TextField(FIRST_NAME);
        firstNameField.setSizeFull();
        lastNameField = new TextField(LAST_NAME);
        lastNameField.setSizeFull();
        patronymicField = new TextField(PATRONYMIC);
        patronymicField.setSizeFull();
        specializationField = new TextField(SPECIALIZATION);
        specializationField.setSizeFull();
        Button okButton = new Button(OK, event -> {
            DAO<Doctor> doctorDAO =
                    DAOFactory.getInstance(POLYCLINIC).getDoctorDAO();
            doctorDAO.beginTransaction();
            ofNullable(doctor).ifPresentOrElse((d) -> {
                binder.writeBeanIfValid(doctor);
                doctorDAO.update(doctor);
                saveListener.save(doctor);
                close();
            }, () -> {
                Doctor d = new Doctor();
                binder.writeBeanIfValid(d);
                doctorDAO.add(d);
                saveListener.save(d);
                close();
            });
            doctorDAO.commitTransaction();
        });
        Button cancelButton = new Button(CANCEL, event -> {
            ofNullable( doctor).ifPresentOrElse((d) -> {
                binder.readBean(doctor);
            }, () -> {
                firstNameField.clear();
                lastNameField.clear();
                patronymicField.clear();
                specializationField.clear();
            });
        });
        binder.forField(firstNameField)
                .asRequired(REQUIRED_FIELD)
                .bind(Doctor::getFirstName, Doctor::setFirstName);
        binder.forField(lastNameField)
                .asRequired(REQUIRED_FIELD)
                .bind(Doctor::getLastName, Doctor::setLastName);
        binder.forField(patronymicField)
                .bind(Doctor::getPatronymic, Doctor::setPatronymic);
        binder.forField(specializationField)
                .asRequired(REQUIRED_FIELD)
                .bind(Doctor::getSpecialization, Doctor::setSpecialization);
        ofNullable(doctor).ifPresent((d) -> {
            binder.readBean(doctor);
        });
        binder.addStatusChangeListener((event) -> {
            boolean isValid = event.getBinder().isValid();
            okButton.setEnabled(isValid);
            cancelButton.setEnabled(ofNullable(doctor)
                    .map((d) -> {
                        return !(doctor.getFirstName()
                                 .matches(firstNameField.getValue())
                                 && doctor.getLastName()
                                 .matches(lastNameField.getValue())
                                 && doctor.getPatronymic()
                                 .matches(patronymicField.getValue())
                                 && doctor.getSpecialization()
                                 .matches(specializationField.getValue()));
                    }).orElse(!firstNameField.isEmpty()
                              || !lastNameField.isEmpty()
                              || !patronymicField.isEmpty()
                              || !specializationField.isEmpty())
            );
        });
        HorizontalLayout hl = new HorizontalLayout(okButton, cancelButton);
        VerticalLayout vl = new VerticalLayout(lastNameField, firstNameField,
                patronymicField, specializationField, hl);
        super.setContent(vl);
        super.center();
        super.setWidth(400, PIXELS);
        super.setHeight(400, PIXELS);
        super.setModal(true);
    }
}
