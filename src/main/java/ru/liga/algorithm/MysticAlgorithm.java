package ru.liga.algorithm;

import ru.liga.repository.MyCurrency;

import java.time.LocalDate;
import java.util.List;

public class MysticAlgorithm implements Algorithm {
    //курсы трех последних дат полнолуний 16.02.2022, 18.01.2022, 19.12.2021
    private static final LocalDate date1 = LocalDate.parse("2022-02-16");
    private static final LocalDate date2 = LocalDate.parse("2022-01-18");
    private static final LocalDate date3 = LocalDate.parse("2021-12-19");
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
        } while (currentKnownDateFromFile.isEqual(date));

        return currencies;
    }

    private double parseNewMoonExchangeRate(List<MyCurrency> currencies) {

        MyCurrency myCurrency1 = getForDate(currencies, date1);
        MyCurrency myCurrency2 = getForDate(currencies, date2);
        MyCurrency myCurrency3 = getForDate(currencies, date3);

        double d = ((myCurrency1.getRate() + myCurrency2.getRate() + myCurrency3.getRate()) / 3) / currencies.get(0).getNominalValue();

        return d;
    }

    private MyCurrency getForDate(List<MyCurrency> currencies, LocalDate date) {
        MyCurrency myCurrency1 = currencies.stream()
                .dropWhile(x -> x.getDate().isAfter(date))
                .findFirst()
                .get();
        return myCurrency1;
    }
}

