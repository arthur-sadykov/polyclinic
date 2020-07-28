/*
 * Copyright 2020 Arthur Sadykov.
 *
 * Тестовое задание Haulmont.
 * Cистема ввода и отображения информации о рецептах поликлиники.
 */
package com.haulmont.polyclinic.dao.impl;

import com.haulmont.polyclinic.dao.DAO;
import com.haulmont.polyclinic.dto.Patient;
import java.util.List;
import static java.util.Optional.ofNullable;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * Реализация интерфейса DAO для сущности "Пациент"
 *
 * @author Arthur Sadykov
 * @version 1.0
 * @since 1.0
 */
public class PatientDAOImpl implements DAO<Patient> {

    private static final Logger LOG =
            Logger.getLogger(PatientDAOImpl.class.getName());
    private final EntityManager em;

    /**
     * Конструктор
     *
     * @param em EntityManager - основной интерфейс ORM, который служит для
     * управления персистентными сущностями.
     */
    public PatientDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void add(Patient patient) {
        ofNullable(patient).ifPresent(em::persist);
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
        Patient patient = get(id);
        ofNullable(patient).ifPresent(em::remove);
    }

    @Override
    public Patient get(Long id) {
        return em.find(Patient.class, id);
    }

    @Override
    public List<Patient> getAll() {
        TypedQuery<Patient> query = em.createQuery("SELECT p FROM Patient p",
                Patient.class);
        return query.getResultList();
    }

    @Override
    public void update(Patient patient) {
        ofNullable(patient).ifPresent(em::merge);
    }
}
