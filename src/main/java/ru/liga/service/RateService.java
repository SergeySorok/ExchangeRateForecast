package ru.liga.service;

import ru.liga.algorithm.Algorithm;
import ru.liga.calculate.Period;
import ru.liga.currency.CurrencyFile;
import ru.liga.repository.CurrencyRepository;
import ru.liga.repository.FileCurrenciesRepository;
import ru.liga.repository.MyCurrency;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RateService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("EE dd.MM.yyyy");
    private static final int LATEST_CURRENT_LINE = 0;
    private final CurrencyRepository currencyRepository = new FileCurrenciesRepository();

    public String calculateRate(CurrencyFile currency, Period period, Algorithm algorithm) {
        StringBuilder result = new StringBuilder();
        LocalDate toDate = LocalDate.now().plusDays(period.getCalculationPeriod());
        List<MyCurrency> currencies = currencyRepository.getPrognosisCurrencies(currency, toDate, algorithm);
        for (int i = period.getCalculationPeriod() - 1; i >= 0; i--) {
            String dateString = currencies.get(i).getDate().format(DATE_FORMAT);
            result.append(String.format("%s - %.2f", dateString, currencies.get(i).getRate())).append("\n");
        }
        return result.toString();
    }

    public List<Double> calculateRateGraph(CurrencyFile currency, Period period, Algorithm algorithm) {
        FileCurrenciesRepository fileCurrenciesRepository = new FileCurrenciesRepository();
        LocalDate toDate = LocalDate.now().plusDays(period.getCalculationPeriod());
        List<MyCurrency> currencies = fileCurrenciesRepository.getPrognosisCurrencies(currency, toDate, algorithm);
        List<Double> doubleList = new ArrayList<>();
        for (int i = period.getCalculationPeriod() - 1; i >= 0; i--) {
            doubleList.add(currencies.get(i).getRate());
        }
        return doubleList;
    }

    public String calculateRate(CurrencyFile currency, LocalDate date, Algorithm algorithm) {
        FileCurrenciesRepository fileCurrenciesRepository = new FileCurrenciesRepository();
        List<MyCurrency> rate = fileCurrenciesRepository.getPrognosisCurrencies(currency, date, algorithm);
        return String.format("%s - %.2f", rate.get(LATEST_CURRENT_LINE).getDate().format(DATE_FORMAT), rate.get(LATEST_CURRENT_LINE).getRate());
    }
}


