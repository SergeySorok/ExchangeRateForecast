package ru.liga.algorithm;

import ru.liga.exception.CommandLineException;
import ru.liga.repository.MyCurrency;

import java.time.LocalDate;
import java.util.List;

import static ru.liga.algorithm.AlgorithmConstants.ERROR_DATE_MESSAGE;

public class ActualAlgorithm implements Algorithm {

    @Override
    public List<MyCurrency> calculate(List<MyCurrency> currencies, LocalDate date) {

        LocalDate currentKnownDateFromFile = currencies.get(AlgorithmConstants.FIRST_LINE_NOMINAL)
                .getDate().plusDays(AlgorithmConstants.ONE_DAY);
        do {
            if (currentKnownDateFromFile.plusYears(AlgorithmConstants.LATEST_FORECAST_DATE).isBefore(date)) {
                throw new CommandLineException(ERROR_DATE_MESSAGE);
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
        } while (!currentKnownDateFromFile.isEqual(date));
        return currencies;
    }
}
