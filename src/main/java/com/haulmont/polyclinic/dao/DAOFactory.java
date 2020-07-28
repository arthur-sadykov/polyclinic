/*
 * Copyright 2020 Arthur Sadykov.
 *
 * Тестовое задание Haulmont.
 * Cистема ввода и отображения информации о рецептах поликлиники.
 */
package com.haulmont.polyclinic.dao;

import com.haulmont.polyclinic.dao.impl.DoctorDAOImpl;
import com.haulmont.polyclinic.dao.impl.PatientDAOImpl;
import com.haulmont.polyclinic.dao.impl.RecipeDAOImpl;
import com.haulmont.polyclinic.dto.Patient;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Arthur Sadykov
 */
public class DAOFactory {

    private static final Logger LOG =
            Logger.getLogger(DAOFactory.class.getName());
    private static EntityManager em;
    private static String pun;

    public static DAOFactory getInstance(String persistenceUnitName) {
        pun = persistenceUnitName;
        return DAOFactoryHolder.INSTANCE;
    }

    private DAOFactory() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(pun);
        em = emf.createEntityManager();
    }

    public DoctorDAO getDoctorDAO() {
        return new DoctorDAOImpl(em);
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public DAO<Patient> getPatientDAO() {
        return new PatientDAOImpl(em);
    }

    public RecipeDAO getRecipeDAO() {
        return new RecipeDAOImpl(em);
    }

    private static class DAOFactoryHolder {

        private static final DAOFactory INSTANCE = new DAOFactory();

        private DAOFactoryHolder() {
        }
    }
}
