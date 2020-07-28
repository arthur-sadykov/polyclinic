/*
 * Copyright 2020 Arthur Sadykov.
 *
 * Тестовое задание Haulmont.
 * Cистема ввода и отображения информации о рецептах поликлиники.
 */
package com.haulmont.polyclinic.dto;

/**
 * Интерфейс "Сущность"
 * @author Arthur Sadykov
 */
public interface Entity {

    /**
     * Возвращает идентификатор сущности
     * @return Идентификатор сущности
     */
    Long getId();

    /**
     * Устанавливает значение идентификатора сущности
     * @param id Идентификатор сущности
     */
    void setId(Long id);
}
