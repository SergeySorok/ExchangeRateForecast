package ru.liga;

import ru.liga.calculate.CalculationType;
import ru.liga.currency.Currency;
import ru.liga.service.RateService;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Scanner;

/**
 * Предоставляет по запросу прогнозируемый курс валюты на определенную дату или период.
 */
public class ExchangeRateForecast {
    public static boolean DEBUG = true;
    /**
     * Предоставляет по запросу прогнозируемые курс валюты на определенную дату.
     *
     * @param args Строка.
     * @throws FileNotFoundException
     * @throws ParseException
     */
    public static void main(String[] args) throws FileNotFoundException, ParseException {
        Scanner scan = new Scanner(System.in);
        args = scan.nextLine().split(" ");
        String currencyString = args[1];
        Currency currency = parseCurrency(currencyString);
        if (currency == null) {
            System.out.println("Невозможно обработать валюту " + currencyString);
            return;
        }

        String calculateTypeString = args[2];
        CalculationType calculateType = parseCalculateType(calculateTypeString);
        if (calculateType == null) {
            System.out.println("Невозможно обработать дату " + calculateTypeString);
            return;
        }
        System.out.println(RateService.calculateRate(currency, calculateType));

    }

    /**
     * Определеяет тип валюты и предоставляет строку с адресом к БД.
     *
     * @param s Строка с валютой.
     * @return Адрес к БД.
     */
    private static Currency parseCurrency(String s) {
        for (Currency currency : Currency.values()) {
            if (currency.name().equalsIgnoreCase(s)) {
                return currency;
            }
        }
        return null;
    }

    /**
     * Определеяет период, на который необходимо предоставить информацию.
     *
     * @param s Строка с периодом.
     * @return Период.
     */
    private static CalculationType parseCalculateType(String s) {
        for (CalculationType calculateType : CalculationType.values()) {
            if (calculateType.name().equalsIgnoreCase(s)) {
                return calculateType;
            }
        }
        return null;
    }
}
