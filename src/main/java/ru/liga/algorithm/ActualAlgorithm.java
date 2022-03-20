package ru.liga.algorithm;

import ru.liga.repository.MyCurrency;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

public class ActualAlgorithm implements Algorithm {

    @Override
    public List<MyCurrency> calculate(List<MyCurrency> currencies, LocalDate date) {

        LocalDate currentKnownDateFromFile = currencies.get(AlgorithmConstants.FIRST_LINE_NOMINAL)
                .getDate().plusDays(AlgorithmConstants.ONE_DAY);
        do {
            if (currentKnownDateFromFile.plusYears(AlgorithmConstants.LATEST_FORECAST_DATE).isBefore(date)) {
                throw new DateTimeException(AlgorithmConstants.ERROR_DATE_MESSAGE + date);
            }
            MyCurrency nextCurrency = new MyCurrency();
            nextCurrency.setNominalValue(currencies.get(AlgorithmConstants.FIRST_LINE_NOMINAL).getNominalValue());
            nextCurrency.setDate(currentKnownDateFromFile);
            LocalDate finalPivot = currentKnownDateFromFile;
            MyCurrency currencyAsOfDateMinusTwoYears = currencies.stream()
                    .dropWhile(x -> x.getDate().isAfter(finalPivot.minusYears(AlgorithmConstants.DATE_FOR_FORECAST_2_YEARS)))
                    .findFirst()
                    .orElseThrow();
            MyCurrency currencyAsOfDateMinusThreeYears = currencies.stream()
                    .dropWhile(x -> x.getDate().isAfter(finalPivot.minusYears(AlgorithmConstants.DATE_FOR_FORECAST_3_YEARS)))
                    .findFirst()
                    .orElseThrow();
            nextCurrency.setRate(currencyAsOfDateMinusTwoYears.getRate() / currencyAsOfDateMinusTwoYears.getNominalValue() + currencyAsOfDateMinusThreeYears.getRate() / currencyAsOfDateMinusThreeYears.getNominalValue());
            currencies.add(0, nextCurrency);
            currentKnownDateFromFile = currentKnownDateFromFile.plusDays(AlgorithmConstants.ONE_DAY);
        } while (currentKnownDateFromFile.isEqual(date));
        return currencies;
    }
}
