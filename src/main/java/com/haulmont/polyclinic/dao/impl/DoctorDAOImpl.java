/*
 * Copyright 2020 Arthur Sadykov.
 *
 * Тестовое задание Haulmont.
 * Cистема ввода и отображения информации о рецептах поликлиники.
 */
package com.haulmont.polyclinic.dao.impl;

import com.haulmont.polyclinic.dao.DoctorDAO;
import com.haulmont.polyclinic.dto.Doctor;
import java.util.List;
import static java.util.Optional.ofNullable;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * Реализация интерфейса DAO для сущности "Врач"
 *
 * @author Arthur Sadykov
 * @version 1.0
 * @since 1.0
 */
public class DoctorDAOImpl implements DoctorDAO{

    private static final Logger LOG =
            Logger.getLogger(DoctorDAOImpl.class.getName());
    private final EntityManager em;

    /**
     * Конструктор
     *
     * @param em EntityManager - основной интерфейс ORM, который служит для
     * управления персистентными сущностями.
     */
    public DoctorDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void add(Doctor doctor) {
        ofNullable(doctor).ifPresent(em::persist);
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
        Doctor doctor = get(id);
        ofNullable(doctor).ifPresent(em::remove);
    }

    @Override
    public Doctor get(Long id) {
        return em.find(Doctor.class, id);
    }

    @Override
    public List<Doctor> getAll() {
        TypedQuery<Doctor> query = em.createQuery("SELECT d FROM Doctor d",
                Doctor.class);
        return query.getResultList();
    }

    @Override
    public Long getIssuedRecipesNumber(Doctor doctor) {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(r) "
                + "FROM Recipe r, Doctor d "
                + "WHERE r.doctorId = d.id "
                + "AND d.id = :doctorId",
                Long.class);
        query.setParameter("doctorId", doctor.getId());
        return query.getSingleResult();
    }

    @Override
    public void update(Doctor doctor) {
        ofNullable(doctor).ifPresent(em::merge);
    }
}
