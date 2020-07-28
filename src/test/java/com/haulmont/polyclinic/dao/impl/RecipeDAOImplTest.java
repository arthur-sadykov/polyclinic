/*
 * Copyright 2020 Arthur Sadykov.
 *
 * Тестовое задание Haulmont.
 * Cистема ввода и отображения информации о рецептах поликлиники.
 */
package com.haulmont.polyclinic.dao.impl;

import com.haulmont.polyclinic.dao.DAO;
import com.haulmont.polyclinic.dao.DAOFactory;
import com.haulmont.polyclinic.dao.RecipeDAO;
import com.haulmont.polyclinic.dto.Doctor;
import com.haulmont.polyclinic.dto.Patient;
import com.haulmont.polyclinic.dto.Recipe;
import static com.haulmont.polyclinic.dto.Recipe.Priority.Cito;
import static com.haulmont.polyclinic.dto.Recipe.Priority.Normal;
import static com.haulmont.polyclinic.dto.Recipe.Priority.Statim;
import java.time.LocalDate;
import static java.time.LocalDate.now;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 *
 * @author Arthur Sadykov
 */
@Disabled
public class RecipeDAOImplTest {
    private static final Logger LOG =
            Logger.getLogger(RecipeDAOImplTest.class.getName());

    private static final String POLYCLINIC = "polyclinicTest";
    private static Doctor doctor1;
    private static Doctor doctor2;
    private static Doctor doctor3;
    private static EntityManager em;
    private static Patient patient1;
    private static Patient patient2;
    private static Patient patient3;
    private static DAO<Patient> patientDao;
    private static RecipeDAO recipeDAO;

    public static Stream<Arguments> provideRecipeArguments() {
        return Stream.of(
                arguments("Рецепт 1", patient1.getId(), doctor1.getId(),
                        now(), 10, Normal),
                arguments("Рецепт 2", patient2.getId(), doctor2.getId(),
                        now(), 10, Cito),
                arguments("Рецепт 3", patient3.getId(), doctor3.getId(),
                        now(), 10, Statim));
    }

    @BeforeAll
    public static void setUp() {
        DAOFactory daoFactory = DAOFactory.getInstance(POLYCLINIC);
        recipeDAO = daoFactory.getRecipeDAO();
        patientDao = daoFactory.getPatientDAO();
        em = daoFactory.getEntityManager();
        doctor1 = new Doctor("Дарвин", "Евгений", "Александрович",
                "Офтальмология");
        doctor2 = new Doctor("Ушанов", "Николай", "Витальевич", "Аллергология");
        doctor3 = new Doctor("Соловьев", "Игорь", "Васильевич", "Травматология");
        patient1 = new Patient("Мартынов", "Николай", "Павлович", "87747578747");
        patient2 =
                new Patient("Алексеева", "Раиса", "Максимовна", "89365588342");
        patient3 = new Patient("Наумова", "Анастасия", "Дмитриевна",
                "81128932448");
        em.getTransaction().begin();
        em.persist(doctor1);
        em.persist(doctor2);
        em.persist(doctor3);
        em.persist(patient1);
        em.persist(patient2);
        em.persist(patient3);
        em.getTransaction().commit();
    }

    @AfterAll
    public static void tearDown() {
        em.getTransaction().begin();
        em.remove(doctor1);
        em.remove(doctor2);
        em.remove(doctor3);
        em.remove(patient1);
        em.remove(patient2);
        em.remove(patient3);
        em.getTransaction().commit();
        em.close();
    }

    @ParameterizedTest
    @MethodSource("provideRecipeArguments")
    public void addShouldSaveRecipe(String description,
            Long patientId, Long doctorId, LocalDate creationDate,
            Integer validityPeriod, Recipe.Priority priority) {
        Recipe recipe = new Recipe(description, patientId, doctorId,
                creationDate, validityPeriod, priority);
        em.getTransaction().begin();
        recipeDAO.add(recipe);
        em.getTransaction().commit();
        Recipe r = recipeDAO.get(recipe.getId());
        assertNotNull(r);
        assertEquals(recipe, r);
        em.getTransaction().begin();
        em.remove(recipe);
        em.getTransaction().commit();
    }

    @ParameterizedTest
    @MethodSource("provideRecipeArguments")
    public void deleteShouldDeleteRecipe(String description,
            Long patientId, Long doctorId, LocalDate creationDate,
            Integer validityPeriod, Recipe.Priority priority) {
        Recipe recipe = new Recipe(description, patientId, doctorId,
                creationDate, validityPeriod, priority);
        assertNotNull(recipe);
        em.getTransaction().begin();
        recipeDAO.add(recipe);
        em.getTransaction().commit();
        Long id = recipe.getId();
        assertNotNull(recipeDAO.get(id));
        em.getTransaction().begin();
        recipeDAO.delete(id);
        em.getTransaction().commit();
        Recipe r = recipeDAO.get(id);
        assertNull(r);
    }


    @Test
    public void findAllShouldReturnEmptyListIfAtLeastOneSearchCriteriaNotFound() {
        Recipe recipe1 = new Recipe("Рецепт 1", patient1.getId(), doctor1
                .getId(), now(), 10, Normal);
        Recipe recipe2 = new Recipe("Рецепт 2", patient2.getId(), doctor2
                .getId(), now(), 10, Cito);
        Recipe recipe3 = new Recipe("Рецепт 3", patient3.getId(), doctor3
                .getId(), now(), 10, Statim);
        assertNotNull(recipe1);
        assertNotNull(recipe2);
        assertNotNull(recipe3);
        em.getTransaction().begin();
        recipeDAO.add(recipe1);
        recipeDAO.add(recipe2);
        recipeDAO.add(recipe3);
        em.getTransaction().commit();
        List<Recipe> recipes = recipeDAO.findAll("Washington", Normal,
                "Some description");
        assertNotNull(recipes);
        assertEquals(0, recipes.size());
        recipes = recipeDAO.findAll(patient3.getLastName(), Statim,
                "Another description");
        assertNotNull(recipes);
        assertEquals(0, recipes.size());
        recipes = recipeDAO.findAll("Washington", Cito, "Рецепт 2");
        assertNotNull(recipes);
        assertEquals(0, recipes.size());
        em.getTransaction().begin();
        em.remove(recipe1);
        em.remove(recipe2);
        em.remove(recipe3);
        em.getTransaction().commit();
    }

    @Test
    public void findAllShouldReturnListIfPatientOrDescriptionOrBothAreEmpty() {
        Recipe recipe1 = new Recipe("Рецепт 1", patient1.getId(), doctor1
                .getId(), now(), 10, Normal);
        Recipe recipe2 = new Recipe("Рецепт 2", patient2.getId(), doctor2
                .getId(), now(), 10, Cito);
        Recipe recipe3 = new Recipe("Рецепт 3", patient3.getId(), doctor3
                .getId(), now(), 10, Statim);
        assertNotNull(recipe1);
        assertNotNull(recipe2);
        assertNotNull(recipe3);
        em.getTransaction().begin();
        recipeDAO.add(recipe1);
        recipeDAO.add(recipe2);
        recipeDAO.add(recipe3);
        em.getTransaction().commit();
        List<Recipe> recipes = recipeDAO.findAll("", Normal,
                "Рецепт 1");
        assertNotNull(recipes);
        assertEquals(1, recipes.size());
        recipes = recipeDAO.findAll(patient3.getLastName(), Statim,
                "");
        assertNotNull(recipes);
        assertEquals(1, recipes.size());
        recipes = recipeDAO.findAll("", Cito, "");
        assertNotNull(recipes);
        assertEquals(1, recipes.size());
        em.getTransaction().begin();
        em.remove(recipe1);
        em.remove(recipe2);
        em.remove(recipe3);
        em.getTransaction().commit();
    }
    @Test
    public void findAllShouldReturnListOfRecipesIfAllSearchCriteriaFulfilled() {
        Recipe recipe1 = new Recipe("Рецепт 1", patient1.getId(), doctor1
                .getId(), now(), 10, Normal);
        Recipe recipe2 = new Recipe("Рецепт 2", patient2.getId(), doctor2
                .getId(), now(), 10, Cito);
        Recipe recipe3 = new Recipe("Рецепт 3", patient3.getId(), doctor3
                .getId(), now(), 10, Statim);
        assertNotNull(recipe1);
        assertNotNull(recipe2);
        assertNotNull(recipe3);
        em.getTransaction().begin();
        recipeDAO.add(recipe1);
        recipeDAO.add(recipe2);
        recipeDAO.add(recipe3);
        em.getTransaction().commit();
        List<Recipe> recipes = recipeDAO.findAll(patient1.getLastName(), Normal,
                "Рецепт 1");
        assertNotNull(recipes);
        assertEquals(1, recipes.size());
        recipes = recipeDAO.findAll(patient2.getLastName(), Cito,
                "Рецепт 2");
        assertNotNull(recipes);
        assertEquals(1, recipes.size());
        recipes = recipeDAO.findAll(patient3.getLastName(), Statim, "Рецепт 3");
        assertNotNull(recipes);
        assertEquals(1, recipes.size());
        em.getTransaction().begin();
        em.remove(recipe1);
        em.remove(recipe2);
        em.remove(recipe3);
        em.getTransaction().commit();
    }

    public void getAllShouldReturnListOfPersistedEntities() {
        Recipe recipe1 = new Recipe("Рецепт 1", patient1.getId(), doctor1
                .getId(), now(), 13, Normal);
        Recipe recipe2 = new Recipe("Рецепт 2", patient2.getId(), doctor2
                .getId(), now(), 14, Cito);
        Recipe recipe3 = new Recipe("Рецепт 3", patient3.getId(), doctor3
                .getId(), now(), 156, Statim);
        assertNotNull(recipe1);
        assertNotNull(recipe2);
        assertNotNull(recipe3);
        em.getTransaction().begin();
        recipeDAO.add(recipe1);
        recipeDAO.add(recipe2);
        recipeDAO.add(recipe3);
        em.getTransaction().commit();
        List<Recipe> recipes = recipeDAO.getAll();
        assertNotNull(recipes);
        assertEquals(3, recipes.size());
        em.getTransaction().begin();
        em.remove(recipe1);
        em.remove(recipe2);
        em.remove(recipe3);
        em.getTransaction().commit();
    }
}
