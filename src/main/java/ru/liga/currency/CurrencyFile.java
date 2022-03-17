package ru.liga.currency;


public enum CurrencyFile {
    AMD("AMD_F01_02_2005_T05_03_2022.csv"),
    BGN("BGN_F01_02_2005_T05_03_2022.csv"),
    EUR("EUR_F01_02_2005_T05_03_2022.csv"),
    USD("USD_F01_02_2005_T05_03_2022.csv"),
    TRY("TRY_F01_02_2005_T05_03_2022.csv");


    public final String filename;

    CurrencyFile(String fileName) {
        this.filename = fileName;
    }

    public String getFilename() {
        return filename;
    }
}
