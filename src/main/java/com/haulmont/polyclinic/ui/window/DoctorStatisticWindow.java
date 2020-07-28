/*
 * Copyright 2020 Arthur Sadykov.
 *
 * Тестовое задание Haulmont.
 * Cистема ввода и отображения информации о рецептах поликлиники.
 */
package com.haulmont.polyclinic.ui.window;

import com.haulmont.polyclinic.dao.DAOFactory;
import com.haulmont.polyclinic.dao.DoctorDAO;
import com.haulmont.polyclinic.dto.Doctor;
import com.vaadin.data.provider.DataProvider;
import static com.vaadin.data.provider.DataProvider.fromCallbacks;
import static com.vaadin.server.Sizeable.Unit.PIXELS;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.renderers.TextRenderer;
import java.util.List;
import java.util.logging.Logger;

/**
 * Форма статистики по врачам
 *
 * @author Arthur Sadykov
 * @version 1.0
 * @since 1.0
 */
public class DoctorStatisticWindow extends Window {

    private static final String DOCTOR = "Врач";
    private static final Logger LOG =
            Logger.getLogger(DoctorStatisticWindow.class.getName());
    private static final String POLYCLINIC = "polyclinic";
    private static final String RECIPES_ISSUED = "Выписано рецептов";
    private static final long serialVersionUID = 1L;
    private DataProvider<Doctor, ?> dataProvider;
    private final DoctorDAO doctorDAO;

    public DoctorStatisticWindow(String caption) {
        super(caption);
        doctorDAO = DAOFactory.getInstance(POLYCLINIC).getDoctorDAO();
        Grid<Doctor> grid = createDoctorStatisticTable();
        VerticalLayout vl = new VerticalLayout(grid);
        super.setWidth(800, PIXELS);
        super.setHeight(500, PIXELS);
        super.setModal(true);
        super.setContent(vl);
    }

    private Grid<Doctor> createDoctorStatisticTable() {
        Grid<Doctor> grid = new Grid<>();
        grid.setHeight("100%");
        grid.setWidth("100%");
        grid.addColumn((doctor) -> {
            return doctor.getLastName() + " "
                   + doctor.getFirstName() + " "
                   + doctor.getPatronymic();
        }, new TextRenderer()).setCaption(DOCTOR);
        grid.addColumn((doctor) -> {
            return doctorDAO.getIssuedRecipesNumber(doctor).toString();
        }, new TextRenderer()).setCaption(RECIPES_ISSUED);
        dataProvider =
                fromCallbacks(query -> {
                    List<Doctor> doctors = doctorDAO.getAll();
                    return doctors.stream();
                }, query -> doctorDAO.getAll().size());
        grid.setDataProvider(dataProvider);
        return grid;
    }
}
