package ru.liga.algorithm;

import ru.liga.dao.MyCurrency;

import java.time.LocalDate;
import java.util.List;


public final class AverageAlgorithm implements Algorithm {

    @Override
    public List<MyCurrency> calculate(List<MyCurrency> currencies, LocalDate date) {
        if (currencies == null || currencies.isEmpty()) {
            return null;
        }
        var average = currencies.stream()
                .mapToDouble(x -> x.getRate() / x.getNominalValue())
                .average()
                .getAsDouble();
        for (var pivot = currencies.get(0).getDate(); pivot.isBefore(date) || pivot.isEqual(date); pivot = pivot.plusDays(1)) {
            var nextCurrency = new MyCurrency();
            nextCurrency.setDate(pivot);
            nextCurrency.setRate(average);
            currencies.add(0, nextCurrency);
        }
        return currencies;
    }
}
