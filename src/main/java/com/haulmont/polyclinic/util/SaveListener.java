/*
 * Copyright 2020 Arthur Sadykov.
 *
 * Тестовое задание Haulmont.
 * Cистема ввода и отображения информации о рецептах поликлиники.
 */
package com.haulmont.polyclinic.util;

import com.haulmont.polyclinic.dto.Entity;

/**
 *
 * @author Arthur Sadykov
 */
/**
 * Слушатель добавления или обновления сущности в хранилище
 * данных
 *
 * @author Arthur Sadykov
 * @version 1.0
 * @since 1.0
 */
@FunctionalInterface
public interface SaveListener {

    /**
     * Возвращает ссылку на сущность при ее добавлении или обновлении 
     * хранилище данных
     *
     * @param entity Ссылка на хранимую сущность, которая добавляется или
     * обновляется
     * @return Добавленная или обновленная сущность
     */
    public Entity save(Entity entity);
}
