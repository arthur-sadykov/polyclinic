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
 * Сущность "Пациент"
 *
 * @author Arthur Sadykov
 * @version 1.0
 * @since 1.0
 */
@javax.persistence.Entity
public class Patient implements Serializable, Entity {

    private static final Logger LOG = Logger.getLogger(Patient.class.getName());
    private static final long serialVersionUID = 1L;
    @Column(name = "first_name")
    private String firstName;
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "patient_id")
    private Long id;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "patronymic")
    private String patronymic;
    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * Создает сущность "Пациент"
     */
    public Patient() {
    }

    /**
     * Создает сущность "Пациент"
     *
     * @param firstName Имя пациента
     * @param lastName Фамилия пациента
     * @param patronymic Отчество пациента
     * @param phoneNumber Номер телефона пациента
     */
    public Patient(String firstName, String lastName, String patronymic,
            String phoneNumber) {
        this.id = null;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Создает сущность "Пациент"
     *
     * @param firstName Имя пациента
     * @param lastName Фамилия пациента
     * @param phoneNumber Номер телефона пациента
     */
    public Patient(String firstName, String lastName, String phoneNumber) {
        this(firstName, lastName, null, phoneNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Patient other = (Patient) obj;
        if (!Objects.equals(this.firstName, other.getFirstName())) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.getLastName())) {
            return false;
        }
        if (!Objects.equals(this.patronymic, other.getPatronymic())) {
            return false;
        }
        if (!Objects.equals(this.phoneNumber, other.getPhoneNumber())) {
            return false;
        }
        return Objects.equals(this.id, other.getId());
    }

    /**
     * Возвращает значение имени пациента
     *
     * @return Имя пациента
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Устанавливает значение имени пациента
     *
     * @param firstName Имя пациента
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Возвращает значение идентификатора пациента
     *
     * @return Идентификатор пациента
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Устанавливает значение идентификатора пациента
     *
     * @param id Идентификатор пациента
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Возвращает значение фамилии пациента
     *
     * @return Фамилия пациента
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Устанавливает значение фамилии пациента
     *
     * @param lastName Фамилия пациента
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Возвращает значение отчества пациента
     *
     * @return Отчество пациента
     */
    public String getPatronymic() {
        return patronymic;
    }

    /**
     * Устанавливает значение отчества пациента
     *
     * @param patronymic Отчество пациента
     */
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    /**
     * Возвращает значение номера телефона пациента
     *
     * @return Номер телефона пациента
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Устанавливает значение номера телефона пациента
     *
     * @param phoneNumber Номер телефона пациента
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.id);
        hash = 83 * hash + Objects.hashCode(this.firstName);
        hash = 83 * hash + Objects.hashCode(this.lastName);
        hash = 83 * hash + Objects.hashCode(this.patronymic);
        hash = 83 * hash + Objects.hashCode(this.phoneNumber);
        return hash;
    }

    @Override
    public String toString() {
        return lastName + " " + firstName + " " + patronymic;
    }
}
