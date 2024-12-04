package kg.com.api_salamat.controller;

import org.springframework.stereotype.Controller;

@Controller
public class PriceController {

    // Метод для обновления данных с OKX
    public void updateOKXData(String symbol, Double avgBidPrice, Double avgAskPrice,
                              Double bidSumUSDT, Double askSumUSDT,
                              Double high24h, Double low24h, Double volume24hInUSDT) {
        // Обработка данных для OKX
        if (symbol != null) {
            System.out.println("OKX: " + symbol);
        }
        if (avgBidPrice != null) {
            System.out.println("Average Bid Price: " + avgBidPrice);
        }
        if (avgAskPrice != null) {
            System.out.println("Average Ask Price: " + avgAskPrice);
        }
        if (bidSumUSDT != null) {
            System.out.println("Bid Total USDT: " + bidSumUSDT);
        }
        if (askSumUSDT != null) {
            System.out.println("Ask Total USDT: " + askSumUSDT);
        }
        if (high24h != null) {
            System.out.println("24h High: " + high24h);
        }
        if (low24h != null) {
            System.out.println("24h Low: " + low24h);
        }
        if (volume24hInUSDT != null) {
            System.out.println("24h Volume (USDT): " + volume24hInUSDT);
        }
    }

    // Метод для обновления данных с MEXC
    public void updateMEXCData(String symbol, Double avgBidPrice, Double avgAskPrice,
                               Double bidSumUSDT, Double askSumUSDT,
                               Double high24h, Double low24h, Double volume24hInUSDT) {
        // Аналогичная обработка данных для MEXC
        if (symbol != null) {
            System.out.println("MEXC: " + symbol);
        }
        // Аналогично выводим другие данные
    }

    // Метод для обновления данных с Bitget
    public void updateBitgetData(String symbol, Double avgBidPrice, Double avgAskPrice,
                                 Double bidSumUSDT, Double askSumUSDT,
                                 Double high24h, Double low24h, Double volume24hInUSDT) {
        // Обработка данных для Bitget
        if (symbol != null) {
            System.out.println("Bitget: " + symbol);
        }
        // Аналогичные выводы для других данных
    }

    // Метод для обработки любых других бирж
    public void updateOtherExchangeData(String symbol, Double avgBidPrice, Double avgAskPrice,
                                        Double bidSumUSDT, Double askSumUSDT,
                                        Double high24h, Double low24h, Double volume24hInUSDT) {
        // Логика для обработки данных других бирж
        if (symbol != null) {
            System.out.println("Other Exchange: " + symbol);
        }
        // Аналогичные выводы для других данных
    }
}

