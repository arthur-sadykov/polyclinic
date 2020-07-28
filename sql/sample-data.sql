/*
 * Copyright 2020 Arthur Sadykov.
 *
 * Тестовое задание Haulmont.
 * Cистема ввода и отображения информации о рецептах поликлиники.
 */
/* Таблица patient*/
INSERT INTO patient (patient_id, last_name, first_name, patronymic, phone_number) 
VALUES (NULL, 'Калмыков', 'Александр', 'Николаевич', '83428393928');

INSERT INTO patient (patient_id, last_name, first_name, patronymic, phone_number)
VALUES (NULL, 'Шарафутдинов', 'Альберт', 'Альфредович', '85438423915');

INSERT INTO patient (patient_id, last_name, first_name, patronymic, phone_number)
VALUES (NULL, 'Суслов', 'Алексей', 'Павлович', '88293718278');

INSERT INTO patient (patient_id, last_name, first_name, patronymic, phone_number)
VALUES (NULL, 'Щелчков', 'Денис', 'Борисович', '82719365777');

INSERT INTO patient (patient_id, last_name, first_name, patronymic, phone_number)
VALUES (NULL, 'Андреева', 'Виктория', 'Георгиевна', '84727837289');

/* Таблица doctor*/
INSERT INTO doctor (doctor_id, last_name, first_name, patronymic, specialization)
VALUES (NULL, 'Полонский', 'Сергей', 'Николаевич', 'Аллергология');

INSERT INTO doctor (doctor_id, last_name, first_name, patronymic, specialization)
VALUES (NULL, 'Терентьев', 'Василий', 'Дмитриевич', 'Травматология');

INSERT INTO doctor (doctor_id, last_name, first_name, patronymic, specialization)
VALUES (NULL, 'Наумов', 'Дмитрий', 'Анатольевич', 'Офтальмология');

INSERT INTO doctor (doctor_id, last_name, first_name, patronymic, specialization)
VALUES (NULL, 'Соловьев', 'Игорь', 'Михайлович', 'Хирургия');

INSERT INTO doctor (doctor_id, last_name, first_name, patronymic, specialization)
VALUES (NULL, 'Смольников', 'Руслан', 'Владимирович', 'Эндокринология');

/* Таблица recipe*/
INSERT INTO recipe (recipe_id, description, patient_id, doctor_id, creation_date,
    validity_period, priority)
VALUES (NULL, 'Rp: Aerosoli Beclomethasoni 250 mkg D.S. По 1 вдох х 2 раза в день.',
    '2', '3', '2019-04-12', 25, 2);

INSERT INTO recipe (recipe_id, description, patient_id, doctor_id, creation_date,
    validity_period, priority)
VALUES (NULL, 'Tab. Metoprololi succinatis 0,05 D.t.d. N 50 S. 1 таб. утром после 
    завтрака под контролем пульса', '1', '4', '2019-06-28', 7, 1);

INSERT INTO recipe (recipe_id, description, patient_id, doctor_id, creation_date,
    validity_period, priority)
VALUES (NULL, 'Tab Nitroglycerini 0,0005 D.t.d. N 50 S. Под язык при приступе 
    стенокардии по 1 таб.', '3', '2', '2019-04-18', 22, 2);

INSERT INTO recipe (recipe_id, description, patient_id, doctor_id, creation_date,
    validity_period, priority)
VALUES (NULL, 'Rp: «Actilyse» 0,05 D.t.d. N 2 S. Растворить в 50 мл воды, вводить 
    по схеме: струйно 15 мл, далее капельно 85-100 мл в течение часа.', '4', '2', 
    '2019-05-11', 26, 0);

INSERT INTO recipe (recipe_id, description, patient_id, doctor_id, creation_date,
    validity_period, priority)
VALUES (NULL, ' Rp: Tab. Ademetionini 0,4 D.t.d. N 40 S. По 1 табл. 3 раза в день',
    '2', '2', '2019-02-23', 15, 1);





