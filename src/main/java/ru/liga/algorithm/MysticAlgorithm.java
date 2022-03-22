package ru.liga.algorithm;

import ru.liga.repository.MyCurrency;

import java.time.LocalDate;
import java.util.List;

public class MysticAlgorithm implements Algorithm {
    private static final LocalDate FULL_MOON_DATE_NUMBER_ONE = LocalDate.parse("2022-02-16");
    private static final LocalDate FULL_MOON_DATE_NUMBER_TWO = LocalDate.parse("2022-01-18");
    private static final LocalDate FULL_MOON_DATE_NUMBER_THREE = LocalDate.parse("2021-12-19");
    private static final int INTEREST_FOR_CALCULATION = 10;

    @Override
    public List<MyCurrency> calculate(List<MyCurrency> currencies, LocalDate date) {

        double newMoonRate = parseNewMoonExchangeRate(currencies);
        double doubleRate = 0;
        LocalDate currentKnownDateFromFile = currencies.get(AlgorithmConstants.FIRST_LINE_NOMINAL)
                .getDate().plusDays(AlgorithmConstants.ONE_DAY);
        do {
            MyCurrency nextCurrency = new MyCurrency();
            nextCurrency.setNominalValue(0);
            nextCurrency.setDate(currentKnownDateFromFile);
            if (doubleRate == 0) {
                nextCurrency.setRate(newMoonRate);
            } else {
                nextCurrency.setRate((Math.random() * (((doubleRate + doubleRate / INTEREST_FOR_CALCULATION) - (doubleRate - doubleRate / INTEREST_FOR_CALCULATION)) + 1)) + (doubleRate - doubleRate / INTEREST_FOR_CALCULATION));
            }
            doubleRate = nextCurrency.getRate();
            currencies.add(0, nextCurrency);
            currentKnownDateFromFile = currentKnownDateFromFile.plusDays(AlgorithmConstants.ONE_DAY);
        } while (!currentKnownDateFromFile.isEqual(date));

        return currencies;
    }

    private double parseNewMoonExchangeRate(List<MyCurrency> currencies) {

        MyCurrency getCurrencyFullMoonDateNumberOne = getForDate(currencies, FULL_MOON_DATE_NUMBER_ONE);
        MyCurrency getCurrencyFullMoonDateNumberTwo = getForDate(currencies, FULL_MOON_DATE_NUMBER_TWO);
        MyCurrency getCurrencyFullMoonDateNumberThree = getForDate(currencies, FULL_MOON_DATE_NUMBER_THREE);

        double d = ((getCurrencyFullMoonDateNumberOne.getRate() + getCurrencyFullMoonDateNumberTwo.getRate() + getCurrencyFullMoonDateNumberThree.getRate()) / 3) / currencies.get(0).getNominalValue();

        return d;
    }

    private MyCurrency getForDate(List<MyCurrency> currencies, LocalDate date) {
        MyCurrency currency = currencies.stream()
                .dropWhile(x -> x.getDate().isAfter(date))
                .findFirst()
                .orElseThrow();
        return currency;
    }
}

