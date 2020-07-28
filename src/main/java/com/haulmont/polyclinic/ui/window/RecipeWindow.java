/*
 * Copyright 2020 Arthur Sadykov.
 *
 * Тестовое задание Haulmont.
 * Cистема ввода и отображения информации о рецептах поликлиники.
 */
package com.haulmont.polyclinic.ui.window;

import com.haulmont.polyclinic.dao.DAO;
import com.haulmont.polyclinic.dao.DAOFactory;
import com.haulmont.polyclinic.dao.RecipeDAO;
import com.haulmont.polyclinic.dto.Doctor;
import com.haulmont.polyclinic.dto.Patient;
import com.haulmont.polyclinic.dto.Recipe;
import com.haulmont.polyclinic.util.SaveListener;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import static com.vaadin.server.Sizeable.Unit.PIXELS;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import static java.util.EnumSet.allOf;
import static java.util.Optional.ofNullable;
import java.util.logging.Logger;

/**
 * Форма для добавления или редактирования рецепта
 *
 * @author Arthur Sadykov
 * @version 1.0
 * @since 1.0
 */
public class RecipeWindow extends Window {

    private static final String CANCEL = "Отменить";
    private static final String CREATION_DATE = "Дата создания";
    private static final String DESCRIPTION = "Описание";
    private static final String DOCTOR = "Врач";
    private static final Logger LOG =
            Logger.getLogger(RecipeWindow.class.getName());
    private static final String MUST_BE_INTEGER =
            "Должно быть введено целое число";
    private static final String OK = "OK";
    private static final String PATIENT = "Пациент";
    private static final String POLYCLINIC = "polyclinic";
    private static final String PRIORITY = "Приоритет";
    private static final String REQUIRED_FIELD = "Обязательное поле";
    private static final String VALIDITY_PERIOD = "Срок действия, сут";
    private static final long serialVersionUID = 1L;
    private final Binder<Recipe> binder = new Binder<>(Recipe.class);
    private final DateField creationDateField;
    private final TextArea descriptionArea;
    private final ComboBox<Doctor> doctorCombo;
    private final ComboBox<Patient> patientCombo;
    private final ComboBox<Recipe.Priority> priorityCombo;
    private final TextField validityPeriodField;

    /**
     * Создает форму для добавления или редактирования рецепта
     *
     * @param caption Заголовок формы
     * @param recipe Рецепт (при передаче null создается форма для добавления,
     * иначе - форма для редактирования)
     * @param saveListener Передает в основную форму информацию о добавлении или
     * обновлении сущности в хранилище данных
     */
    public RecipeWindow(String caption, Recipe recipe,
            SaveListener saveListener) {
        super(caption);
        DAOFactory daoFactory = DAOFactory.getInstance(POLYCLINIC);
        DAO<Doctor> doctorDAO = daoFactory.getDoctorDAO();
        DAO<Patient> patientDAO = daoFactory.getPatientDAO();
        RecipeDAO recipeDAO = daoFactory.getRecipeDAO();
        descriptionArea = new TextArea(DESCRIPTION);
        descriptionArea.setSizeFull();
        patientCombo = new ComboBox<>(PATIENT);
        patientCombo.setSizeFull();
        patientCombo.setItems(patientDAO.getAll());
        doctorCombo = new ComboBox<>(DOCTOR);
        doctorCombo.setSizeFull();
        doctorCombo.setItems(doctorDAO.getAll());
        creationDateField = new DateField(CREATION_DATE);
        creationDateField.setSizeFull();
        validityPeriodField = new TextField(VALIDITY_PERIOD);
        validityPeriodField.setSizeFull();
        priorityCombo = new ComboBox<>(PRIORITY);
        priorityCombo.setSizeFull();
        priorityCombo.setItems(allOf(Recipe.Priority.class));
        Button okButton = new Button(OK, event -> {
            recipeDAO.beginTransaction();
            ofNullable(recipe).ifPresentOrElse((r) -> {
                binder.writeBeanIfValid(recipe);
                recipeDAO.update(recipe);
                saveListener.save(recipe);
                close();
            }, () -> {
                Recipe r = new Recipe();
                binder.writeBeanIfValid(r);
                recipeDAO.add(r);
                saveListener.save(r);
                close();
            });
            recipeDAO.commitTransaction();
        });
        Button cancelButton = new Button(CANCEL, event -> {
            ofNullable(recipe).ifPresentOrElse((r) -> {
                binder.readBean(recipe);
            }, () -> {
                descriptionArea.clear();
                doctorCombo.clear();
                patientCombo.clear();
                creationDateField.clear();
                validityPeriodField.clear();
                priorityCombo.clear();
            });
        });
        binder.addStatusChangeListener((event) -> {
            boolean isValid = event.getBinder().isValid();
            okButton.setEnabled(isValid);
            cancelButton.setEnabled(descriptionArea.isEmpty()
                                    || !doctorCombo.isEmpty()
                                    || !patientCombo.isEmpty()
                                    || !creationDateField.isEmpty()
                                    || !validityPeriodField.isEmpty()
                                    || !priorityCombo.isEmpty());
        });
        binder.forField(descriptionArea)
                .asRequired(REQUIRED_FIELD)
                .bind(Recipe::getDescription, Recipe::setDescription);
        binder.forField(patientCombo)
                .asRequired(REQUIRED_FIELD)
                .withConverter((patient) -> {
                    return ofNullable(patient)
                            .map(Patient::getId)
                            .orElse(null);
                }, (id) -> {
                    return ofNullable(id)
                            .map(patientDAO::get)
                            .orElse(null);
                })
                .bind(Recipe::getPatientId, Recipe::setPatientId);
        binder.forField(doctorCombo)
                .asRequired(REQUIRED_FIELD)
                .withConverter((doctor) -> {
                    return ofNullable(doctor)
                            .map(Doctor::getId)
                            .orElse(null);
                }, (id) -> {
                    return ofNullable(id)
                            .map(doctorDAO::get)
                            .orElse(null);
                })
                .bind(Recipe::getDoctorId, Recipe::setDoctorId);
        binder.forField(creationDateField)
                .asRequired(REQUIRED_FIELD)
                .bind(Recipe::getCreationDate, Recipe::setCreationDate);
        binder.forField(validityPeriodField)
                .asRequired(REQUIRED_FIELD)
                .withConverter(new StringToIntegerConverter(MUST_BE_INTEGER))
                .bind(Recipe::getValidityPeriod, Recipe::setValidityPeriod);
        binder.forField(priorityCombo)
                .asRequired(REQUIRED_FIELD)
                .bind(Recipe::getPriority, Recipe::setPriority);
        ofNullable(recipe)
                .ifPresent((r) -> {
                    binder.readBean(recipe);
                });
        HorizontalLayout hl = new HorizontalLayout(okButton, cancelButton);
        VerticalLayout vl = new VerticalLayout(descriptionArea, doctorCombo,
                patientCombo, creationDateField, validityPeriodField,
                priorityCombo, hl);
        super.setContent(vl);
        super.center();
        super.setWidth(400, PIXELS);
        super.setHeight(700, PIXELS);
        super.setModal(true);
    }
}
