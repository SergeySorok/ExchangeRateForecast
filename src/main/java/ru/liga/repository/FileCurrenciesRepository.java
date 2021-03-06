package ru.liga.repository;

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


public class FileCurrenciesRepository implements CurrencyRepository {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final String SEPARATOR_COLUMN_NAME = ";";
    private static final String NOMINAL_COLUMN_NAME = "nominal";
    private static final String DATE_COLUMN_NAME = "data";
    private static final String CURS_COLUMN_NAME = "curs";
    private static int nominal_Column;
    private static int date_Column;
    private static int rate_Column;

    @Override
    public List<MyCurrency> getActualCurrencies(CurrencyFile currencyFile) {
        List<MyCurrency> listParse = null;
        try (var stream = FileCurrenciesRepository.class.getClassLoader().getResourceAsStream(currencyFile.getFilename());
             var reader = new BufferedReader(new InputStreamReader(stream));) {
            setColumnIndex(reader);
            listParse = reader.lines()
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
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return listParse;
    }

    @Override
    public List<MyCurrency> getPrognosisCurrencies(CurrencyFile currencyFile, LocalDate date, Algorithm algorithm) {
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
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rate;
    }

    private void setColumnIndex(BufferedReader bufferedReader) throws IOException {
        String[] listColumnName = bufferedReader.readLine().split(SEPARATOR_COLUMN_NAME);
        for (int i = 0; i < listColumnName.length; i++) {
            if (listColumnName[i].equalsIgnoreCase(NOMINAL_COLUMN_NAME)) {
                nominal_Column = i;
            }
            if (listColumnName[i].equalsIgnoreCase(DATE_COLUMN_NAME)) {
                date_Column = i;
            }
            if (listColumnName[i].equalsIgnoreCase(CURS_COLUMN_NAME)) {
                rate_Column = i;
            }
        }
    }
}
