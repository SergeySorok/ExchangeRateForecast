package ru.liga.algorithm;

import ru.liga.currency.CurrencyFile;
import ru.liga.repository.MyCurrency;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface Algorithm  {
    List<MyCurrency> calculate(List<MyCurrency> courses, LocalDate date);
}
