package ru.liga.dao;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MyCurrency {
    private double nominalValue;
    private LocalDate date;
    private double rate;
}
