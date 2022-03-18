package ru.liga.calculate;


public enum Period {
    TOMORROW(1),
    WEEK(7),
    MONTH(30);

    public static Period parseCalculateType(String s) {
        for (Period calculateType : Period.values()) {
            if (calculateType.name().equalsIgnoreCase(s)) {
                return calculateType;
            }
        }
        return null;
    }

    private final int calculationPeriod;

    Period(int calculationPeriod) {
        this.calculationPeriod = calculationPeriod;
    }

    public int getCalculationPeriod() {
        return calculationPeriod;
    }
}
