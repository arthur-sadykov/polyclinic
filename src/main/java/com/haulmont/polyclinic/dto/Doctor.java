/*
 * Copyright 2020 Arthur Sadykov.
 *
 * Тестовое задание Haulmont.
 * Cистема ввода и отображения информации о рецептах поликлиники.
 */
package com.haulmont.polyclinic.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;

/**
 * Сущность "Врач"
 *
 * @author Arthur Sadykov
 * @version 1.0
 * @since 1.0
 */
@javax.persistence.Entity
public class Doctor implements Serializable, Entity {

    private static final Logger LOG = Logger.getLogger(Doctor.class.getName());
    private static final long serialVersionUID = 1L;
    @Column(name = "first_name")
    private String firstName;
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "doctor_id")
    private Long id;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "patronymic")
    private String patronymic;
    @Column(name = "specialization")
    private String specialization;

    /**
     * Создает сущность "Врач"
     */
    public Doctor() {
    }

    /**
     * Создает сущность "Врач"
     *
     * @param firstName Имя
     * @param lastName Фамилия
     * @param patronymic Отчество
     * @param specialization Специализация
     */
    public Doctor(String firstName, String lastName, String patronymic,
            String specialization) {
        this.id = null;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.specialization = specialization;
    }

    /**
     * Создает сущность "Врач"
     *
     * @param firstName Имя
     * @param lastName Фамилия
     * @param specialization Специализация
     */
    public Doctor(String firstName, String lastName, String specialization) {
        this(firstName, lastName, null, specialization);
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null) {
            return false;
        }
        if (getClass() != otherObject.getClass()) {
            return false;
        }
        final Doctor other = (Doctor) otherObject;
        if (!Objects.equals(this.firstName, other.getFirstName())) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.getLastName())) {
            return false;
        }
        if (!Objects.equals(this.patronymic, other.getPatronymic())) {
            return false;
        }
        if (!Objects.equals(this.specialization, other.getSpecialization())) {
            return false;
        }
        return Objects.equals(this.id, other.getId());
    }

    /**
     * Возвращает значение имени врача
     *
     * @return Имя врача
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Устанавливает значение имени врача
     *
     * @param firstName Имя врача
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Возвращает значение идентификатора врача
     *
     * @return Идентификатор врача
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Устанавливает значение идентификатора врача
     *
     * @param id Идентификатор врача
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Возвращает значение фамилии врача
     *
     * @return Фамилия врача
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Устанавливает значение фамилии врача
     *
     * @param lastName Фамилия врача
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Возвращает значение отчества врача
     *
     * @return Отчество врача
     */
    public String getPatronymic() {
        return patronymic;
    }

    /**
     * Устанавливает значение отчества врача
     *
     * @param patronymic Отчество врача
     */
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    /**
     * Возвращает значение специализации врача
     *
     * @return Специализация врача
     */
    public String getSpecialization() {
        return specialization;
    }

    /**
     * Устанавливает значение специализации врача
     *
     * @param specialization Специализация врача
     */
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.id);
        hash = 67 * hash + Objects.hashCode(this.firstName);
        hash = 67 * hash + Objects.hashCode(this.lastName);
        hash = 67 * hash + Objects.hashCode(this.patronymic);
        hash = 67 * hash + Objects.hashCode(this.specialization);
        return hash;
    }

    @Override
    public String toString() {
        return lastName + " " + firstName + " " + patronymic;
    }
}
