package ru.liga.algorithm;

import ru.liga.dao.MyCurrency;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

public class ActualAlgorithm implements Algorithm {
    @Override
    public List<MyCurrency> calculate(List<MyCurrency> currencies, LocalDate date) {
        if (currencies == null || currencies.isEmpty()) {
            return null;
        }
        for (LocalDate pivot = currencies.get(0).getDate().plusDays(1); pivot.isBefore(date) || pivot.isEqual(date); pivot = pivot.plusDays(1)) {
            if (pivot.plusYears(2).isBefore(date)) {
                throw new DateTimeException("Вы ввели слишком познюю дату: " + date);
            }
            MyCurrency nextCurrency = new MyCurrency();
            nextCurrency.setNominalValue(currencies.get(0).getNominalValue());
            nextCurrency.setDate(pivot);
            LocalDate finalPivot = pivot;
            MyCurrency currency1 = currencies.stream()
                    .dropWhile(x -> x.getDate().isAfter(finalPivot.minusYears(2)))
                    .findAny()
                    .get();
            MyCurrency currency2 = currencies.stream()
                    .dropWhile(x -> x.getDate().isAfter(finalPivot.minusYears(3)))
                    .findAny()
                    .get();
            nextCurrency.setRate(currency1.getRate() / currency1.getNominalValue() + currency2.getRate() / currency2.getNominalValue());
            currencies.add(0, nextCurrency);
        }
        return currencies;
    }
}
