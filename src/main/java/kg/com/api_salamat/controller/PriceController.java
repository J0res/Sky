package kg.com.api_salamat.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@Data
public class PriceController {

    private ConcurrentHashMap<String, PriceData> priceDataMap = new ConcurrentHashMap<>();

    public void updateBinanceData(String symbol, double averageBidPrice, double averageAskPrice, double bidSumUSDT, double askSumUSDT, double high24h, double low24h, double volume24hInUSDT) {
        if ((bidSumUSDT >= 1000 && bidSumUSDT <= 2000) || (askSumUSDT >= 1000 && askSumUSDT <= 2000)) {
            updateData(symbol, "BINANCE", averageBidPrice, averageAskPrice, bidSumUSDT, askSumUSDT, high24h, low24h, volume24hInUSDT);
        }
    }

    public void updateMEXCData(String symbol, double averageBidPrice, double averageAskPrice, double bidSumUSDT, double askSumUSDT, double high24h, double low24h, double volume24hInUSDT) {
        if ((bidSumUSDT >= 1000 && bidSumUSDT <= 2000) || (askSumUSDT >= 1000 && askSumUSDT <= 2000)) {
            updateData(symbol, "MEXC", averageBidPrice, averageAskPrice, bidSumUSDT, askSumUSDT, high24h, low24h, volume24hInUSDT);
        }
    }
    public void updateOKXData(String symbol, double averageBidPrice, double averageAskPrice, double bidSumUSDT, double askSumUSDT, double high24h, double low24h, double volume24hInUSDT) {
        if ((bidSumUSDT >= 1000 && bidSumUSDT <= 2000) || (askSumUSDT >= 1000 && askSumUSDT <= 2000)) {
            updateData(symbol, "OKX", averageBidPrice, averageAskPrice, bidSumUSDT, askSumUSDT, high24h, low24h, volume24hInUSDT);
        }
    }

    public void updateBitGetData(String symbol, double averageBidPrice, double averageAskPrice, double bidSumUSDT, double askSumUSDT, double high24h, double low24h, double volume24hInUSDT) {
        if ((bidSumUSDT >= 1000 && bidSumUSDT <= 2000) || (askSumUSDT >= 1000 && askSumUSDT <= 2000)) {
            updateData(symbol, "Bitget", averageBidPrice, averageAskPrice, bidSumUSDT, askSumUSDT, high24h, low24h, volume24hInUSDT);
        }
    }

    public void updateBitMartData(String symbol, double averageBidPrice, double averageAskPrice, double bidSumUSDT, double askSumUSDT, double high24h, double low24h, double volume24hInUSDT) {
        if ((bidSumUSDT >= 1000 && bidSumUSDT <= 2000) || (askSumUSDT >= 1000 && askSumUSDT <= 2000)) {
            updateData(symbol, "BitMart", averageBidPrice, averageAskPrice, bidSumUSDT, askSumUSDT, high24h, low24h, volume24hInUSDT);
        }
    }

    public void updatePoloniexData(String symbol, double averageBidPrice, double averageAskPrice, double bidSumUSDT, double askSumUSDT, double high24h, double low24h, double volume24hInUSDT) {
        if ((bidSumUSDT >= 1000 && bidSumUSDT <= 2000) || (askSumUSDT >= 1000 && askSumUSDT <= 2000)) {
            updateData(symbol, "Poloniex", averageBidPrice, averageAskPrice, bidSumUSDT, askSumUSDT, high24h, low24h, volume24hInUSDT);
        }
    }

    private void updateData(String symbol, String exchange, double averageBidPrice, double averageAskPrice, double bidSumUSDT, double askSumUSDT, double high24h, double low24h, double volume24hInUSDT) {
        if ((bidSumUSDT >= 1000 && bidSumUSDT <= 2000) || (askSumUSDT >= 1000 && askSumUSDT <= 2000)) {
            PriceData data = priceDataMap.getOrDefault(symbol + "_" + exchange, new PriceData(symbol, exchange, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));

            if (averageBidPrice != 0.0) {
                data.setAverageBidPrice(BigDecimal.valueOf(averageBidPrice));
                data.setBidSumUSDT(BigDecimal.valueOf(bidSumUSDT));
            }
            if (averageAskPrice != 0.0) {
                data.setAverageAskPrice(BigDecimal.valueOf(averageAskPrice));
                data.setAskSumUSDT(BigDecimal.valueOf(askSumUSDT));
            }
            if (high24h != 0.0 || low24h != 0.0 || volume24hInUSDT != 0.0) {
                data.setHigh24h(BigDecimal.valueOf(high24h));
                data.setLow24h(BigDecimal.valueOf(low24h));
                data.setVolume24h(BigDecimal.valueOf(volume24hInUSDT).setScale(2, RoundingMode.HALF_UP));
            }

            priceDataMap.put(symbol + "_" + exchange, data);
            System.out.printf("Updated %s data: %s%n", exchange, data);
        }
    }

    @GetMapping("/average-prices")
    public Map<String, Map<String, PriceData>> getAveragePrices() {
        Map<String, Map<String, PriceData>> groupedData = new HashMap<>();

        for (PriceData priceData : priceDataMap.values()) {
            groupedData
                    .computeIfAbsent(priceData.getSymbol(), k -> new HashMap<>())
                    .put(priceData.getExchange(), priceData);
        }

        return groupedData;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PriceData {
        private String symbol;
        private String exchange;
        private BigDecimal averageBidPrice;
        private BigDecimal averageAskPrice;
        private BigDecimal bidSumUSDT;
        private BigDecimal askSumUSDT;
        private BigDecimal high24h;
        private BigDecimal low24h;
        private BigDecimal volume24h;

        @Override
        public String toString() {
            DecimalFormat formatter = new DecimalFormat("#,###.00");

            return String.format(
                    "Symbol: %s, Exchange: %s, AvgBidPrice: %s, AvgAskPrice: %s, BidSumUSDT: %s, AskSumUSDT: %s, High24h: %s, Low24h: %s, Volume24h: %s",
                    symbol,
                    exchange,
                    formatter.format(averageBidPrice.setScale(2, RoundingMode.HALF_UP)),
                    formatter.format(averageAskPrice.setScale(2, RoundingMode.HALF_UP)),
                    formatter.format(bidSumUSDT.setScale(2, RoundingMode.HALF_UP)),
                    formatter.format(askSumUSDT.setScale(2, RoundingMode.HALF_UP)),
                    formatter.format(high24h.setScale(2, RoundingMode.HALF_UP)),
                    formatter.format(low24h.setScale(2, RoundingMode.HALF_UP)),
                    formatter.format(volume24h.setScale(2, RoundingMode.HALF_UP))
            );
        }
    }
}
