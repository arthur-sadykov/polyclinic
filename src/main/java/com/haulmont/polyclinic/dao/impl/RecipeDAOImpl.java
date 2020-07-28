/*
 * Copyright 2020 Arthur Sadykov.
 *
 * Тестовое задание Haulmont.
 * Cистема ввода и отображения информации о рецептах поликлиники.
 */
package com.haulmont.polyclinic.dao.impl;

import com.haulmont.polyclinic.dao.RecipeDAO;
import com.haulmont.polyclinic.dto.Recipe;
import java.util.List;
import static java.util.Optional.ofNullable;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * Реализация интерфейса RecipeDAO для сущности "Рецепт"
 *
 * @author Arthur Sadykov
 * @version 1.0
 * @since 1.0
 */
public class RecipeDAOImpl implements RecipeDAO {

    private static final Logger LOG =
            getLogger(RecipeDAOImpl.class.getName());
    private final EntityManager em;

    /**
     * Конструктор
     *
     * @param em EntityManager - основной интерфейс ORM, который служит для
     * управления персистентными сущностями.
     */
    public RecipeDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void add(Recipe recipe) {
        ofNullable(recipe).ifPresent(em::persist);
    }

    @Override
    public void beginTransaction() {
        em.getTransaction().begin();
    }

    @Override
    public void commitTransaction() {
        em.getTransaction().commit();
    }

    @Override
    public void delete(Long id) {
        Recipe recipe = get(id);
        ofNullable(recipe).ifPresent(em::remove);
    }

    @Override
    public List<Recipe> findAll(String patient, Recipe.Priority priority,
            String description) {
        TypedQuery<Recipe> query =
                em.createQuery("SELECT r FROM Recipe r WHERE EXISTS "
                               + "(SELECT 1 FROM Patient p WHERE "
                               + "(p.firstName LIKE concat('%', :patient, '%') "
                               + "OR p.lastName LIKE concat('%', :patient, '%') "
                               + "OR p.patronymic LIKE concat('%', :patient, '%')) "
                               + "AND r.priority LIKE :priority "
                               + "AND r.description "
                               + "LIKE concat('%', :description, '%') "
                               + "AND r.patientId = p.id)",
                        Recipe.class);
        query.setParameter("patient", patient);
        query.setParameter("priority", priority);
        query.setParameter("description", description);
        return query.getResultList();
    }

    @Override
    public Recipe get(Long id) {
        return em.find(Recipe.class, id);
    }

    @Override
    public List<Recipe> getAll() {
        TypedQuery<Recipe> query = em.createQuery("SELECT r FROM Recipe r",
                Recipe.class);
        return query.getResultList();
    }

    @Override
    public void update(Recipe recipe) {
        ofNullable(recipe).ifPresent(em::merge);
    }
}
