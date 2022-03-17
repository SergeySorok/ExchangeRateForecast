package ru.liga.service;

import ru.liga.calculate.CalculationType;
import ru.liga.currency.Currency;
import ru.liga.dao.CurrenciesDAO;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RateService {
    private RateService() {

    }

    /**
     * Формирует результат запросы - дата и курс.
     * @param currency Путь к БД.
     * @param calculationType Период прогноза.
     * @return возвращает строку с результатом даты и курса.
     * @throws FileNotFoundException
     * @throws ParseException
     */
    public static String calculateRate(Currency currency, CalculationType calculationType)
            throws FileNotFoundException, ParseException {

        int intPeriod = calculationType.calculationPeriod - DayService.actualNumberOfDays();
        DateTimeFormatter dataFormat = DateTimeFormatter.ofPattern("EE dd.MM.yyyy");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < intPeriod; i++) {
            LocalDate localDate = LocalDate.now().plusDays(i+1);
            StringBuilder dateString = new StringBuilder(dataFormat.format(localDate));
            List<Double> rate = CurrenciesDAO.getPrognosisCurrencies(calculationType, currency, 7);

            result.append(String.format("%s - %.2f", dateString, rate.get(i))).append("\n");
        }
        return result.toString();
    }
}


