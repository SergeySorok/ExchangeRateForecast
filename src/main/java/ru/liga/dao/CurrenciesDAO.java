package ru.liga.dao;

import ru.liga.algorithm.Algorithm;
import ru.liga.currency.CurrencyFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class CurrenciesDAO {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final String SEPARATOR_COLUMN_NAME = ";";
    private static int nominal_Column;
    private static int date_Column;
    private static int rate_Column;

    private List<MyCurrency> getActualCurrencies(CurrencyFile currencyFile) throws IOException {
        var stream = CurrenciesDAO.class.getClassLoader().getResourceAsStream(currencyFile.getFilename());
        var reader = new BufferedReader(new InputStreamReader(stream));
        setColumnIndex(reader);
        List<MyCurrency> listParse = reader.lines()
                .skip(1)
                .map(s -> s.split(";"))
                .map(x -> {
                    MyCurrency myCurrency = new MyCurrency();
                    myCurrency.setNominalValue(Double.parseDouble(x[nominal_Column]));
                    LocalDate date = LocalDate.parse(x[date_Column], DATE_FORMAT);
                    myCurrency.setDate(date);
                    double rate = parseRate(x);
                    myCurrency.setRate(rate);
                    return myCurrency;
                })
                .collect(Collectors.toList());
        return listParse;
    }

    public List<MyCurrency> getPrognosisCurrencies(CurrencyFile currencyFile, LocalDate date, Algorithm algorithm) throws IOException {
        List<MyCurrency> listParse = getActualCurrencies(currencyFile);
        algorithm.calculate(listParse, date);
        return listParse;
    }

    private double parseRate(String[] x) {
        double rate = 0;
        try {
            rate = NumberFormat.getInstance(new Locale("RU"))
                    .parse(x[rate_Column].replace("\"", ""))
                    .doubleValue();
        } catch (ParseException e) {e.printStackTrace();}
        return rate;
    }

    private void setColumnIndex(BufferedReader bufferedReader) throws IOException {
        String[] listColumnName = bufferedReader.readLine().split(SEPARATOR_COLUMN_NAME);
        for (int i = 0; i < listColumnName.length; i++) {
            if (listColumnName[i].equalsIgnoreCase("nominal")) {
                nominal_Column = i;
            }
            if (listColumnName[i].equalsIgnoreCase("data")) {
                date_Column = i;
            }
            if (listColumnName[i].equalsIgnoreCase("curs")) {
                rate_Column = i;
            }

        }
    }

}
