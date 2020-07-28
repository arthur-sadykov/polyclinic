/*
 * Copyright 2020 Arthur Sadykov.
 *
 * Тестовое задание Haulmont.
 * Cистема ввода и отображения информации о рецептах поликлиники.
 */
package com.haulmont.polyclinic.dao;

import java.util.List;

/**
 * Интерфейс объекта доступа к данным
 *
 * @param <T> Тип сущности
 * @author Arthur Sadykov
 * @version 1.0
 * @since 1.0
 */
public interface DAO<T> {

    /**
     * Добавляет сущность в хранилище данных
     *
     * @param t Сущность
     */
    void add(T t);

    void beginTransaction();

    void commitTransaction();

    /**
     * Удаляет сущность из хранилища данных
     *
     * @param id Идентификатор сущности
     */
    void delete(Long id);

    /**
     * Возвращает ссылку на сущность
     *
     * @param id Идентификатор сущности
     * @return Ссылка на сущность
     */
    T get(Long id);

    /**
     * Возвращает список сущностей, находящихся в хранилище данных
     *
     * @return Список сущностей предметной области
     */
    List<T> getAll();

    /**
     * Обновляет значения полей сущности
     *
     * @param t Ссылка на сущность
     */
    void update(T t);
}
