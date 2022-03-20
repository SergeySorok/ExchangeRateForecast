package ru.liga.repository;

import ru.liga.algorithm.Algorithm;
import ru.liga.currency.CurrencyFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface CurrencyRepository {
    List<MyCurrency> getActualCurrencies(CurrencyFile currencyFile) throws IOException;
    List<MyCurrency> getPrognosisCurrencies(CurrencyFile currencyFile, LocalDate date, Algorithm algorithm) throws IOException;
}
