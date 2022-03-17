package ru.liga.calculate;


public enum Period {
    TOMORROW(1),
    WEEK(7),
    MONTH(30);

    private final int calculationPeriod;

    Period(int calculationPeriod) {
        this.calculationPeriod = calculationPeriod;
    }

    public int getCalculationPeriod() {
        return calculationPeriod;
    }
}
