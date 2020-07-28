/*
 * Copyright 2020 Arthur Sadykov.
 *
 * Тестовое задание Haulmont.
 * Cистема ввода и отображения информации о рецептах поликлиники.
 */
package com.haulmont.polyclinic.dao;

import com.haulmont.polyclinic.dto.Doctor;

/**
 * Интерфейс объекта доступа к данным сущности "Врач"
 *
 * @author Arthur Sadykov
 * @version 1.0
 * @since 1.0
 */
public interface DoctorDAO extends DAO<Doctor> {

    Long getIssuedRecipesNumber(Doctor doctor);
}
