package ru.liga.currency;

import ru.liga.ExchangeRateForecast;

import javax.swing.plaf.ColorUIResource;

/**
 * Предоставляет путь к БД с типом валюты
 */
public enum Currency {
    EUR("EUR_F01_02_2002_T01_02_2022.csv"),
    USD("USD_F01_02_2002_T01_02_2022.csv"),
    TRY("TRY_F01_02_2002_T01_02_2022.csv");


    public final String filePath;

    Currency(String fileName) {
        if (ExchangeRateForecast.DEBUG) {
            this.filePath = "C:\\Users\\serg-\\Documents\\" + fileName;
        }
        else {
            this.filePath = fileName;
        }
    }

    public String getFilePath() {
        return filePath;
    }
}
