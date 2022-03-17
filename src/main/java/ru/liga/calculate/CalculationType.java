package ru.liga.calculate;

import ru.liga.service.DayService;

/**
 * Предоставляет количество дней, для которых осуществляется прогноз
 */
public enum CalculationType {
    TOMORROW(1 + DayService.actualNumberOfDays()),
    WEEK(7 + DayService.actualNumberOfDays());

    public final int calculationPeriod;

    CalculationType(int calculationPeriod) {
        this.calculationPeriod = calculationPeriod;
    }

    public int getCalculationPeriod() {
        return calculationPeriod;
    }
}
