/*
 * Copyright 2020 Arthur Sadykov.
 *
 * Тестовое задание Haulmont.
 * Cистема ввода и отображения информации о рецептах поликлиники.
 */
package com.haulmont.polyclinic.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;

/**
 * Сущность "Рецепт"
 *
 * @author Arthur Sadykov
 * @version 1.0
 * @since 1.0
 */
@javax.persistence.Entity
public class Recipe implements Serializable, Entity {

    private static final Logger LOG = Logger.getLogger(Recipe.class.getName());
    private static final long serialVersionUID = 1L;
    @Column(name = "creation_date")
    private LocalDate creationDate;
    @Column(name = "description")
    private String description;
//    @ManyToOne(targetEntity = Doctor.class)
//    @JoinColumn(name = "doctor_id")
    @Column(name = "doctor_id")
    private Long doctorId;
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "recipe_id")
    private Long id;
    //    @ManyToOne(targetEntity = Patient.class)
//    @JoinColumn(name = "patient_id")
    @Column(name = "patient_id")
    private Long patientId;
    @Column(name = "priority")
    private Priority priority;
    @Column(name = "validity_period")
    private Integer validityPeriod;

    /**
     * Создает сущность "Рецепт"
     */
    public Recipe() {
    }

    /**
     * Создает сущность "Рецепт"
     *
     * @param description Описание рецепта
     * @param patientId Идентификатор пациента
     * @param doctorId Идентификатор врача
     * @param creationDate Дата создания рецепта
     * @param validityPeriod Срок действия рецепта
     * @param priority Приоритет рецепта
     */
    public Recipe(String description, Long patientId, Long doctorId,
            LocalDate creationDate, Integer validityPeriod,
            Priority priority) {
        this.id = null;
        this.description = description;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.creationDate = creationDate;
        this.validityPeriod = validityPeriod;
        this.priority = priority;
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
        final Recipe other = (Recipe) obj;
        if (!Objects.equals(this.description, other.getDescription())) {
            return false;
        }
        if (!Objects.equals(this.id, other.getId())) {
            return false;
        }
        if (!Objects.equals(this.patientId, other.getPatientId())) {
            return false;
        }
        if (!Objects.equals(this.doctorId, other.getDoctorId())) {
            return false;
        }
        if (!Objects.equals(this.creationDate, other.getCreationDate())) {
            return false;
        }
        if (!Objects.equals(this.validityPeriod, other.getValidityPeriod())) {
            return false;
        }
        return this.priority == other.getPriority();
    }

    /**
     * Возвращает значение даты создания рецепта
     *
     * @return Дата создания рецепта
     */
    public LocalDate getCreationDate() {
        return creationDate;
    }

    /**
     * Устанавливает значение даты создания рецепта
     *
     * @param creationDate Дата создания рецепта
     */
    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Возвращает значение атрибута описания рецепта
     *
     * @return Описание рецепта
     */
    public String getDescription() {
        return description;
    }

    /**
     * Устанавливает значение атрибута описания рецепта
     *
     * @param description Описание рецепта
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Возвращает значение id врача
     *
     * @return Идентификатор врача
     */
    public Long getDoctorId() {
        return doctorId;
    }

    /**
     * Устанавливает значение id врача
     *
     * @param doctorId Идентификатор врача
     */
    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    /**
     * Возвращает значение id рецепта
     *
     * @return Идентификатор рецепта
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Устанавливает значение id рецепта
     *
     * @param id Идентификатор рецепта
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Возвращает значение id пациента
     *
     * @return Идентификатор пациента
     */
    public Long getPatientId() {
        return patientId;
    }

    /**
     * Устанавливает значение id пациента
     *
     * @param patientId Идентификатор пациента
     */
    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    /**
     * Возвращает значение атрибута приоритета рецепта
     *
     * @return Приоритет рецепта
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * Устанавливает значение атрибута приоритета рецепта
     *
     * @param priority Приоритет рецепта
     */
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    /**
     * Возвращает значение атрибута срока действия рецепта
     *
     * @return Срок действия рецепта в часах
     */
    public Integer getValidityPeriod() {
        return validityPeriod;
    }

    /**
     * Устанавливает значение атрибута срока действия рецепта
     *
     * @param validityPeriod Срок действия рецепта в часах
     */
    public void setValidityPeriod(Integer validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.id);
        hash = 37 * hash + Objects.hashCode(this.description);
        hash = 37 * hash + Objects.hashCode(this.patientId);
        hash = 37 * hash + Objects.hashCode(this.doctorId);
        hash = 37 * hash + Objects.hashCode(this.creationDate);
        hash = 37 * hash + Objects.hashCode(this.validityPeriod);
        hash = 37 * hash + Objects.hashCode(this.priority);
        return hash;
    }

    @Override
    public String toString() {
        return "Recipe{" + "id=" + id + ", description=" + description
               + ", patientId=" + patientId + ", doctorId=" + doctorId
               + ", creationDate=" + creationDate + ", validityPeriod="
               + validityPeriod + ", priority=" + priority + '}';
    }

    /**
     * Приоритет рецепта
     */
    public enum Priority {
        /**
         * Нормальный приоритет рецепта
         */
        Normal("Нормальный"),
        /**
         * Срочный приоритет рецепта
         */
        Cito("Срочный"),
        /**
         * Немедленный приоритет рецепта
         */
        Statim("Немедленный");
        private final String name;

        private Priority(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
