package ru.liga.algorithm;

import java.util.List;

/**
 * Алгоритм прогнозирования курса
 */
public final class AverageAlgorithm implements Algorithm {
    public static final AverageAlgorithm INSTANCE = new AverageAlgorithm();

    private AverageAlgorithm() {
    }

    /**
     * Алгоритм прогнозирования курса на основе данных с предыдущими курсами
     *
     * @param courses Принимает данные с курсами валют
     * @return Прогнозируемый курс на основании среднего значения
     */
    public double calculate(List<Double> courses) {
        if (courses == null || courses.isEmpty()) {
            return 0;
        }
        double averageCourses = 0;
        for (double d : courses) {
            averageCourses += d;
        }
        averageCourses /= courses.size();
        return averageCourses;
    }
}
