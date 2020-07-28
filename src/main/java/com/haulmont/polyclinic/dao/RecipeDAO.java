/*
 * Copyright 2020 Arthur Sadykov.
 *
 * Тестовое задание Haulmont.
 * Cистема ввода и отображения информации о рецептах поликлиники.
 */
package com.haulmont.polyclinic.dao;

import com.haulmont.polyclinic.dto.Recipe;
import java.util.List;

/**
 * Интерфейс объекта доступа к данным сущности "Рецепт"
 *
 * @author Arthur Sadykov
 * @version 1.0
 * @since 1.0
 */
public interface RecipeDAO extends DAO<Recipe> {

    /**
     * Возвращает отфильтрованный с учетом параметров метода, список рецептов,
     * находящихся в хранилище данных
     *
     * @param patient Имя, фамилия или отчество пациента(или все вместе)
     * @param priority Приоритет рецепта. Рецепт может иметь один из
     * приоритетов: Normal(Нормальный), Cito (Срочный), Statim (Немедленный)
     * @param description Описание рецепта
     * @return Список рецептов
     */
    List<Recipe> findAll(String patient, Recipe.Priority priority,
            String description);
}
