package ru.liga.dao;

import ru.liga.algorithm.AverageAlgorithm;
import ru.liga.calculate.CalculationType;
import ru.liga.currency.Currency;
import ru.liga.service.DayService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Формирует лист с курсами валют.
 */

public class CurrenciesDAO {

    /**
     * Считывает БД и формирует лист с курсами.
     *
     * @param currency Путь к БД.
     * @return Лист с курсами валют.
     * @throws FileNotFoundException
     * @throws ParseException
     */
    public static List<Double> getCurrenciesDataBase(Currency currency) throws IOException {
        File file = new File(currency.getFilePath());
        List<Double> listParse = new ArrayList<>();

        try (Scanner fileScanner = new Scanner(file)) {
            if (fileScanner.hasNextLine()) {
                fileScanner.nextLine();
            }
            while (fileScanner.hasNextLine()) {
                String[] s = fileScanner.nextLine().split(";");

                double d = NumberFormat.getInstance(new Locale("RU")).parse(s[1]).doubleValue();
                listParse.add(d);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return listParse;
    }

    /**
     * Считывает БД и формирует лист с курсами с определенным в параметрах количеством записей.
     *
     * @param currency Путь к БД.
     * @param quantity Необходимое количество записей для анализа.
     * @return Лист с необходимым количеством записей.
     * @throws FileNotFoundException
     * @throws ParseException
     */
    public static List<Double> getLastCurrenciesDataBase(Currency currency, int quantity) throws FileNotFoundException, ParseException {
        File file = new File(currency.getFilePath());
        List<Double> listParse = new ArrayList<>();
        try (Scanner fileScanner = new Scanner(file)) {
            if (fileScanner.hasNextLine()) {
                fileScanner.nextLine();
            }
            for (int i = 0; i < quantity; i++) {

                if (fileScanner.hasNextLine()) {
                    String[] s = fileScanner.nextLine().split(";");
                    double d = NumberFormat.getInstance(new Locale("RU")).parse(s[1]).doubleValue();
                    listParse.add(d);
                } else {
                    break;
                }
            }
        }
        return listParse;
    }


    /**
     * Считывает БД (+ создает записи на актуальную дату) и формирует лист с курсами с определенным в параметрах
     * количеством записей.
     *
     * @param currency Путь к БД.
     * @param quantity Необходимое количество записей для анализа.
     * @return Лист с необходимым количеством записей на актуальную дату.
     * @throws FileNotFoundException
     * @throws ParseException
     */
    public static List<Double> getActualCurrencies(Currency currency, int quantity) throws FileNotFoundException, ParseException {
        List<Double> listParse = getLastCurrenciesDataBase(currency, quantity);
        Collections.reverse(listParse);
        List<Double> listActualPeriod = new ArrayList<>();

        for (int i = 0; i < DayService.actualNumberOfDays(); i++) {
            double doubleListParse = AverageAlgorithm.INSTANCE.calculate(listParse);
            listParse.add(doubleListParse);
            listActualPeriod.add(doubleListParse);
            if (listParse.size() > quantity) {
                listParse.remove(0);
            }
            if (listActualPeriod.size() > quantity) {
                listActualPeriod.remove(0);
            }
        }
        return listActualPeriod;
    }

    /**
     * На основании актуализации информации из БД формирует лист с курсами с определенным в параметрах
     * количеством записей.
     *
     * @param calculationType Период прогноза.
     * @param currency        Путь к БД.
     * @param quantity        Необходимое количество записей для анализа.
     * @return Лист с прогнозируемым курсом.
     * @throws FileNotFoundException
     * @throws ParseException
     */
    public static List<Double> getPrognosisCurrencies(CalculationType calculationType, Currency currency, int quantity) throws FileNotFoundException, ParseException {
        List<Double> listParse = getActualCurrencies(currency, quantity);
        List<Double> listNextPeriod = new ArrayList<>();
        int intPeriod = calculationType.calculationPeriod - DayService.actualNumberOfDays();

        for (int i = 0; i < intPeriod; i++) {
            double doubleListParse = AverageAlgorithm.INSTANCE.calculate(listParse);
            listParse.add(doubleListParse);
            listNextPeriod.add(doubleListParse);
            if (listParse.size() > quantity) {
                listParse.remove(0);
            }
            if (intPeriod < listNextPeriod.size()) {
                listNextPeriod.remove(0);
            }
        }
        return listNextPeriod;
    }

}
