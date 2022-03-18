package ru.liga.currency;


public enum CurrencyFile {
    AMD("AMD_F01_02_2005_T05_03_2022.csv"),
    BGN("BGN_F01_02_2005_T05_03_2022.csv"),
    EUR("EUR_F01_02_2005_T05_03_2022.csv"),
    USD("USD_F01_02_2005_T05_03_2022.csv"),
    TRY("TRY_F01_02_2005_T05_03_2022.csv");

    public static String[] parseCurrency(String currency) {
        String[] currencyArray = currency.split (",");
        return currencyArray;
    }

    public static CurrencyFile parseCurrencyType(String s) {
        for (CurrencyFile currency : CurrencyFile.values()) {
            if (currency.name().equalsIgnoreCase(s)) {
                return currency;
            }
        }
        return null;
    }

    public final String filename;

    CurrencyFile(String fileName) {
        this.filename = fileName;
    }

    public String getFilename() {
        return filename;
    }
}
