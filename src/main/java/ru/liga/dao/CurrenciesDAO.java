package ru.liga.dao;

import ru.liga.algorithm.Algorithm;
import ru.liga.currency.CurrencyFile;

import java.io.BufferedReader;
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

    public List<MyCurrency> getActualCurrencies(CurrencyFile currencyFile) {
        var stream = CurrenciesDAO.class.getClassLoader().getResourceAsStream(currencyFile.getFilename());
        assert stream != null;
        var reader = new BufferedReader(new InputStreamReader(stream));
        List<MyCurrency> listParse = reader.lines()
                .skip(1)
                .map(s -> s.split(";"))
                .map(x -> {
                    MyCurrency myCurrency = new MyCurrency();
                    myCurrency.setNominalValue(Double.parseDouble(x[0]));
                    LocalDate date = LocalDate.parse(x[1], DATE_FORMAT);
                    myCurrency.setDate(date);
                    try {
                        double rate = NumberFormat.getInstance(new Locale("RU"))
                                .parse(x[2].replace("\"", ""))
                                .doubleValue();
                        myCurrency.setRate(rate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return myCurrency;
                })
                .collect(Collectors.toList());
        return listParse;
    }

    public List<MyCurrency> getPrognosisCurrencies(CurrencyFile currencyFile, LocalDate date, Algorithm algorithm) {
        List<MyCurrency> listParse = getActualCurrencies(currencyFile);
        algorithm.calculate(listParse, date);
        return listParse;
    }

}
