package ru.liga.service;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;

/**
 * Подсчет количества дней между последней датой из БД и актуальной датой.
 */
public class DayService {

    /**
     * Подсчет количества дней между последней датой из БД и актуальной датой.
     * @return Количество дней целым числом между последней датой из БД и актуальной датой.
     */
    public static int actualNumberOfDays() {
        LocalDate localDate = LocalDate.now();
        LocalDate localDateBase = LocalDate.of(2022, Month.FEBRUARY, 1);
        return (int) localDateBase.until(localDate, ChronoUnit.DAYS);
    }
}
