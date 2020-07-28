/*
 * Copyright 2020 Arthur Sadykov.
 *
 * Тестовое задание Haulmont.
 * Cистема ввода и отображения информации о рецептах поликлиники.
 */
package com.haulmont.polyclinic.ui;

import com.haulmont.polyclinic.dao.DAO;
import com.haulmont.polyclinic.dao.DAOFactory;
import static com.haulmont.polyclinic.dao.DAOFactory.getInstance;
import com.haulmont.polyclinic.dao.RecipeDAO;
import com.haulmont.polyclinic.dto.Doctor;
import com.haulmont.polyclinic.dto.Patient;
import com.haulmont.polyclinic.dto.Recipe;
import com.haulmont.polyclinic.dto.Recipe.Priority;
import static com.haulmont.polyclinic.dto.Recipe.Priority.Cito;
import static com.haulmont.polyclinic.dto.Recipe.Priority.Normal;
import static com.haulmont.polyclinic.dto.Recipe.Priority.Statim;
import com.haulmont.polyclinic.ui.window.DoctorStatisticWindow;
import com.haulmont.polyclinic.ui.window.DoctorWindow;
import com.haulmont.polyclinic.ui.window.PatientWindow;
import com.haulmont.polyclinic.ui.window.RecipeWindow;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Binder;
import com.vaadin.data.provider.DataProvider;
import static com.vaadin.data.provider.DataProvider.fromCallbacks;
import com.vaadin.server.Page.UriFragmentChangedEvent;
import com.vaadin.server.VaadinRequest;
import static com.vaadin.ui.Alignment.TOP_RIGHT;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import static com.vaadin.ui.UI.getCurrent;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.TextRenderer;
import java.util.List;
import static java.util.Optional.ofNullable;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import org.vaadin.sliderpanel.SliderPanel;
import org.vaadin.sliderpanel.SliderPanelBuilder;
import static org.vaadin.sliderpanel.SliderPanelStyles.COLOR_WHITE;
import static org.vaadin.sliderpanel.client.SliderMode.RIGHT;
import static org.vaadin.sliderpanel.client.SliderTabPosition.MIDDLE;

/**
 * Основная форма системы
 *
 * @author Arthur Sadykov
 * @version 1.0
 * @since 1.0
 */
@Theme("mytheme")
public class MainUI extends UI {

    private static final String ADD = "Добавить";
    private static final String ADD_DOCTOR = "Добавление сведений о новом "
                                             + "враче";
    private static final String ADD_PATIENT = "Добавление сведений о новом "
                                              + "пациенте";
    private static final String ADD_RECIPE = "Добавление сведений о новом "
                                             + "рецепте";
    private static final String APPLY = "Применить";
    private static final String CREATION_DATE = "Дата создания";
    private static final String DELETE = "Удалить";
    private static final String DESCRIPTION = "Описание";
    private static final String DOCTOR = "Врач";
    private static final String DOCTORS = "Врачи";
    private static final String EDIT = "Изменить";
    private static final String EDIT_DOCTOR = "Редактирование сведений о "
                                              + "враче";
    private static final String EDIT_PATIENT = "Редактирование сведений о "
                                               + "пациенте";
    private static final String EDIT_RECIPE = "Редактирование сведений о "
                                              + "рецепте";
    private static final String FIRST_NAME = "Имя";
    private static final String LAST_NAME = "Фамилия";
    private static final Logger LOG = getLogger(MainUI.class.getName());
    private static final String PATIENT = "Пациент";
    private static final String PATIENTS = "Пациенты";
    private static final String PATRONYMIC = "Отчество";
    private static final String PHONE_NUMBER = "Номер телефона";
    private static final String POLYCLINIC = "polyclinic";
    private static final String PRIORITY = "Приоритет";
    private static final String RECIPES = "Рецепты";
    private static final String REQUIRED_FIELD = "Обязательное поле";
    private static final String RESET = "Сбросить";
    private static final String SPECIALIZATION = "Специализация";
    private static final String VALIDITY_PERIOD = "Срок действия, сут";
    private static final long serialVersionUID = 1L;
    private DAO<Doctor> doctorDAO;
    private DAO<Patient> patientDAO;
    private RecipeDAO recipeDAO;

    @Override
    @SuppressWarnings("deprecation")
    protected void init(VaadinRequest request) {
        DAOFactory daoFactory = getInstance(POLYCLINIC);
        doctorDAO = daoFactory.getDoctorDAO();
        patientDAO = daoFactory.getPatientDAO();
        recipeDAO = daoFactory.getRecipeDAO();
        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        tabSheet.addSelectedTabChangeListener((event) -> {
            String selectedTabName = event.getTabSheet().getSelectedTab()
                    .getCaption();
            getCurrent().getPage().setUriFragment(selectedTabName);
        });
        DoctorTab doctorTab = new DoctorTab();
        PatientTab patientTab = new PatientTab();
        RecipeTab recipeTab = new RecipeTab();
        tabSheet.addComponents(doctorTab, patientTab, recipeTab);
        VerticalLayout root = new VerticalLayout(tabSheet);
        root.setSizeFull();
        setContent(root);
        getPage().addUriFragmentChangedListener(
                (UriFragmentChangedEvent source) -> {
            String uriFragment = source.getUriFragment();
            switch (uriFragment) {
                case DOCTORS:
                    tabSheet.setSelectedTab(0);
                    break;
                case PATIENTS:
                    tabSheet.setSelectedTab(1);
                    break;
                case RECIPES:
                    tabSheet.setSelectedTab(2);
                    break;
                default:
                    tabSheet.setSelectedTab(0);
            }
            getCurrent().getPage().setTitle(uriFragment);
        });
        getPage().setUriFragment(DOCTORS);
        getPage().setTitle(DOCTORS);
    }

    /*
        Вкладка "Врачи"
     */
    private class DoctorTab extends VerticalLayout {

        private static final String CANNOT_DELETE_DOCTOR =
                "Нельзя удалить запись врача, на которую есть ссылка в таблице "
                + "\"Рецепты\"";
        private static final String DOCTOR_STATISTIC = "Статистика по врачам";
        private static final String SHOW_STATISTIC = "Показать статистику";
        private static final long serialVersionUID = 1L;
        private DataProvider<Doctor, ?> dataProvider;
        private final Grid<Doctor> doctorsTable;

        DoctorTab() {
            super.setCaption(DOCTORS);
            super.setSizeFull();
            HorizontalLayout hl = new HorizontalLayout();
            doctorsTable = createDoctorsTable();
            super.addComponent(doctorsTable);
            super.setExpandRatio(doctorsTable, 7);
            Button addButton = new Button(ADD, event -> {
                addWindow(new DoctorWindow(ADD_DOCTOR, null, (doctor) -> {
                    doctorsTable.select((Doctor) doctor);
                    dataProvider.refreshAll();
                    return doctor;
                }));
            });
            Button changeButton = new Button(EDIT, event -> {
                addWindow(new DoctorWindow(EDIT_DOCTOR,
                        doctorsTable.getSelectionModel()
                                .getFirstSelectedItem()
                                .orElse(null), (doctor) -> {
                    doctorsTable.select((Doctor) doctor);
                    dataProvider.refreshAll();
                    return doctor;
                }));
            });
            changeButton.setEnabled(false);
            Button deleteButton = new Button(DELETE, event -> {
                doctorsTable.getSelectionModel().getFirstSelectedItem()
                        .ifPresent((doctor) -> {
                            try {
                                doctorDAO.beginTransaction();
                                doctorDAO.delete(doctor.getId());
                                doctorDAO.commitTransaction();
                            } catch (Exception e) {
                                Notification.show(CANNOT_DELETE_DOCTOR);
                                return;
                            }
                            int lastIndex = dataProvider.size(null) - 1;
                            if (lastIndex >= 0) {
                                ofNullable(doctorDAO.getAll().get(lastIndex))
                                        .ifPresent((d) -> {
                                            doctorsTable.select(d);
                                        });
                            }
                            dataProvider.refreshAll();
                        });
            });
            deleteButton.setEnabled(false);
            Button showStatisticButton = new Button(SHOW_STATISTIC);
            showStatisticButton.setEnabled(dataProvider.size(null) > 0);
            showStatisticButton.addClickListener((event) -> {
                addWindow(new DoctorStatisticWindow(DOCTOR_STATISTIC));
            });
            hl.addComponents(addButton, changeButton, deleteButton,
                    showStatisticButton);
            doctorsTable.getSelectionModel().addSelectionListener((event) -> {
                event.getFirstSelectedItem().ifPresentOrElse((doctor) -> {
                    changeButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                }, () -> {
                    changeButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                });
            });
            dataProvider.addDataProviderListener((event) -> {
                boolean isRowSelected = event.getSource().size(null) > 0
                                        && !doctorsTable.getSelectedItems()
                                .isEmpty();
                changeButton.setEnabled(isRowSelected);
                deleteButton.setEnabled(isRowSelected);
                showStatisticButton.setEnabled(event.getSource().size(null) > 0);
            });
            super.addComponent(hl);
        }

        private Grid<Doctor> createDoctorsTable() {
            Grid<Doctor> grid = new Grid<>();
            grid.setHeight("100%");
            grid.setWidth("100%");
            grid.addColumn(Doctor::getLastName).setCaption(LAST_NAME);
            grid.addColumn(Doctor::getFirstName).setCaption(FIRST_NAME);
            grid.addColumn(Doctor::getPatronymic).setCaption(PATRONYMIC);
            grid.addColumn(Doctor::getSpecialization).setCaption(SPECIALIZATION);
            dataProvider = fromCallbacks(query -> {
                List<Doctor> doctors = doctorDAO.getAll();
                return doctors.stream();
            }, query -> doctorDAO.getAll().size());
            grid.setDataProvider(dataProvider);
            return grid;
        }
    }

    /*
        Вкладка "Пациенты"
     */
    private class PatientTab extends VerticalLayout {

        private static final String CANNOT_DELETE_PATIENT =
                "Нельзя удалить запись пациента, на которую есть ссылка в "
                + "таблице \"Рецепты\"";
        private static final long serialVersionUID = 1L;
        private DataProvider<Patient, ?> dataProvider;

        PatientTab() {
            super.setCaption(PATIENTS);
            super.setSizeFull();
            HorizontalLayout hl = new HorizontalLayout();
            Grid<Patient> patientsTable = createPatientsTable();
            super.addComponent(patientsTable);
            super.setExpandRatio(patientsTable, 5);
            Button addButton = new Button(ADD, event -> {
                addWindow(new PatientWindow(ADD_PATIENT, null, (patient) -> {
                    patientsTable.select((Patient) patient);
                    dataProvider.refreshAll();
                    return patient;
                }));
            });
            Button changeButton = new Button(EDIT, event -> {
                addWindow(new PatientWindow(EDIT_PATIENT,
                        patientsTable.getSelectionModel()
                                .getFirstSelectedItem()
                                .orElse(null), (patient) -> {
                    patientsTable.select((Patient) patient);
                    dataProvider.refreshAll();
                    return patient;
                }));
            });
            changeButton.setEnabled(false);
            Button deleteButton = new Button(DELETE, event -> {
                patientsTable.getSelectionModel()
                        .getFirstSelectedItem().ifPresent(patient -> {
                            try {
                                patientDAO.beginTransaction();
                                patientDAO.delete(patient.getId());
                                patientDAO.commitTransaction();
                            } catch (Exception e) {
                                Notification.show(CANNOT_DELETE_PATIENT);
                                return;
                            }
                            int lastIndex = dataProvider.size(null) - 1;
                            if (lastIndex >= 0) {
                                ofNullable(patientDAO.getAll().get(lastIndex))
                                        .ifPresent((p) -> {
                                            patientsTable.select(p);
                                        });
                            }
                            dataProvider.refreshAll();
                        });
            });
            deleteButton.setEnabled(false);
            patientsTable.getSelectionModel().addSelectionListener(event -> {
                event.getFirstSelectedItem().ifPresentOrElse(patient -> {
                    changeButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                }, () -> {
                    changeButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                });
            });
            dataProvider.addDataProviderListener((event) -> {
                boolean isRowSelected = event.getSource().size(null) > 0
                                        && !patientsTable.getSelectedItems()
                                .isEmpty();
                changeButton.setEnabled(isRowSelected);
                deleteButton.setEnabled(isRowSelected);
            });
            hl.addComponents(addButton, changeButton, deleteButton);
            super.addComponent(hl);
        }

        private Grid<Patient> createPatientsTable() {
            Grid<Patient> grid = new Grid<>();
            grid.setHeight("100%");
            grid.setWidth("100%");
            grid.addColumn(Patient::getLastName).setCaption(LAST_NAME);
            grid.addColumn(Patient::getFirstName).setCaption(FIRST_NAME);
            grid.addColumn(Patient::getPatronymic).setCaption(PATRONYMIC);
            grid.addColumn(Patient::getPhoneNumber).setCaption(PHONE_NUMBER);
            dataProvider =
                    fromCallbacks(query -> {
                        return patientDAO.getAll().stream();
                    }, guery -> {
                        return patientDAO.getAll().size();
                    });
            grid.setDataProvider(dataProvider);
            return grid;
        }
    }

    /*
        Вкладка "Рецепты"
     */
    private class RecipeTab extends VerticalLayout {

        private static final String FILTER = "Фильтр";
        private static final long serialVersionUID = 1L;
        private DataProvider<Recipe, ?> dataProvider;
        private Grid<Recipe> recipesTable;

        RecipeTab() {
            super.setCaption(RECIPES);
            super.setSizeFull();
            HorizontalLayout hl = new HorizontalLayout();
            recipesTable = createRecipesTable();
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.setSizeFull();
            SliderPanel filterPanel = createFilterPanel();
            horizontalLayout.addComponents(recipesTable, filterPanel);
            horizontalLayout.setComponentAlignment(filterPanel, TOP_RIGHT);
            recipesTable.setWidth("100%");
            recipesTable.setSizeFull();
            horizontalLayout.setExpandRatio(recipesTable, 1);
            super.addComponent(horizontalLayout);
            super.setExpandRatio(horizontalLayout, 7);
            Button addButton = new Button(ADD, event -> {
                addWindow(new RecipeWindow(ADD_RECIPE, null, (recipe) -> {
                    recipesTable.select((Recipe) recipe);
                    dataProvider.refreshAll();
                    return recipe;
                }));
            });
            Button changeButton = new Button(EDIT, event -> {
                addWindow(new RecipeWindow(EDIT_RECIPE,
                        recipesTable.getSelectionModel()
                                .getFirstSelectedItem()
                                .orElse(null), (recipe) -> {
                    recipesTable.select((Recipe) recipe);
                    dataProvider.refreshAll();
                    return recipe;
                }));
            });
            changeButton.setEnabled(false);
            Button deleteButton = new Button(DELETE, event -> {
                recipesTable.getSelectionModel().getFirstSelectedItem()
                        .ifPresent(recipe -> {
                            recipeDAO.beginTransaction();
                            recipeDAO.delete(recipe.getId());
                            recipeDAO.commitTransaction();
                            int lastIndex = dataProvider.size(null) - 1;
                            if (lastIndex >= 0) {
                                ofNullable(recipeDAO.getAll().get(lastIndex))
                                        .ifPresent((r) -> {
                                            recipesTable.select(r);
                                        });
                            }
                            dataProvider.refreshAll();
                        });
            });
            deleteButton.setEnabled(false);
            recipesTable.getSelectionModel().addSelectionListener(event -> {
                event.getFirstSelectedItem().ifPresentOrElse(patient -> {
                    changeButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                }, () -> {
                    changeButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                });
            });
            dataProvider.addDataProviderListener((event) -> {
                boolean isRowSelected = event.getSource().size(null) > 0
                                        && !recipesTable.getSelectedItems()
                                .isEmpty();
                changeButton.setEnabled(isRowSelected);
                deleteButton.setEnabled(isRowSelected);
            });
            hl.addComponents(addButton, changeButton, deleteButton);
            super.addComponent(hl);
        }

        private SliderPanel createFilterPanel() {
            Binder<Recipe> binder = new Binder<>(Recipe.class);
            TextField patientTextField = new TextField(PATIENT);
            ComboBox<Priority> priorityComboBox = new ComboBox<>(PRIORITY);
            priorityComboBox.setItems(Normal, Cito, Statim);
            binder.forField(priorityComboBox)
                    .asRequired(REQUIRED_FIELD)
                    .bind(Recipe::getPriority, Recipe::setPriority);
            TextField descriptionTextField = new TextField(DESCRIPTION);
            Button applyButton = new Button(APPLY);
            Button resetButton = new Button(RESET);
            resetButton.addClickListener((event) -> {
                patientTextField.clear();
                priorityComboBox.clear();
                descriptionTextField.clear();
                List<Recipe> recipes = recipeDAO.getAll();
                recipesTable.setItems(recipes);
            });
            HorizontalLayout buttonBar = new HorizontalLayout();
            buttonBar.addComponents(applyButton, resetButton);
            VerticalLayout vl = new VerticalLayout();
            vl.addComponents(patientTextField, priorityComboBox,
                    descriptionTextField, buttonBar);
            SliderPanel filterPanel =
                    new SliderPanelBuilder(vl).caption(FILTER)
                            .mode(RIGHT)
                            .tabPosition(MIDDLE)
                            .style(COLOR_WHITE)
                            .build();
            applyButton.addClickListener(event -> {
                String patient = patientTextField.getValue();
                Priority priority = priorityComboBox.getValue();
                String description = descriptionTextField.getValue();
                List<Recipe> recipes =
                        recipeDAO.findAll(patient, priority, description);
                recipesTable.setItems(recipes);
            });
            applyButton.setEnabled(false);
            binder.addStatusChangeListener((event) -> {
                boolean isValid = event.getBinder().isValid();
                applyButton.setEnabled(isValid);
            });
            return filterPanel;
        }

        private Grid<Recipe> createRecipesTable() {
            Grid<Recipe> grid = new Grid<>();
            grid.setHeight("100%");
            grid.setWidth("100%");
            grid.addColumn(Recipe::getDescription).setCaption(DESCRIPTION);
            Grid.Column<Recipe, Long> doctorColumn =
                    grid.addColumn(Recipe::getDoctorId).setCaption(DOCTOR);
            doctorColumn.setRenderer((id) -> {
                return ofNullable(doctorDAO.get(id))
                        .map((doctor) -> {
                            return doctor.getLastName() + " "
                                   + doctor.getFirstName() + " "
                                   + doctor.getPatronymic();
                        }).orElse(null);
            }, new TextRenderer());
            Grid.Column<Recipe, Long> patientColumn =
                    grid.addColumn(Recipe::getPatientId).setCaption(PATIENT);
            patientColumn.setRenderer((id) -> {
                return ofNullable(patientDAO.get(id))
                        .map(patient -> {
                            return patient.getLastName() + " "
                                   + patient.getFirstName() + " "
                                   + patient.getPatronymic();
                        }).orElse(null);
            }, new TextRenderer());
            grid.addColumn(Recipe::getCreationDate).setCaption(CREATION_DATE);
            grid.addColumn(Recipe::getValidityPeriod)
                    .setCaption(VALIDITY_PERIOD);
            grid.addColumn(Recipe::getPriority).setCaption(PRIORITY);
            dataProvider =
                    fromCallbacks(query -> {
                        return recipeDAO.getAll().stream();
                    }, query -> {
                        return recipeDAO.getAll().size();
                    });
            grid.setDataProvider(dataProvider);
            return grid;
        }
    }
}
