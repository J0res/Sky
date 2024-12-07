package kg.com.api_salamat.util;

public class SymbolFormatter {

    /**
     * Форматирует символ для указанной биржи.
     *
     * @param exchange Имя биржи (например, Binance, OKX, MEXC, Poloniex)
     * @param symbol   Символ монеты в базовом формате (например, BTCUSDT)
     * @return Символ, отформатированный для биржи
     */
    public static String formatSymbol(String exchange, String symbol) {
        switch (exchange.toLowerCase()) {
            case "okx":
                return symbol.replace("USDT", "-USDT");
            case "poloniex":
                return symbol.replace("USDT", "_USDT");
            case "mexc":
            case "bitget":
            case "binance":
                return symbol; // Без изменений
            default:
                throw new IllegalArgumentException("Unknown exchange: " + exchange);
        }
    }
}

