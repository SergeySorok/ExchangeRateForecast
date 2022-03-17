package ru.liga.algorithm;

import ru.liga.dao.MyCurrency;

import java.time.LocalDate;
import java.util.List;

public interface Algorithm  {
    List<MyCurrency> calculate(List<MyCurrency> courses, LocalDate date);
}
