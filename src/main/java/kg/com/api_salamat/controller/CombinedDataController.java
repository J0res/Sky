package kg.com.api_salamat.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class CombinedDataController {

    private final List<BinanceRecord> binanceData = new CopyOnWriteArrayList<>();
    private final List<MEXCRecord> mexcData = new CopyOnWriteArrayList<>();

    @GetMapping("/combined-data")
    public CombinedData getCombinedData() {
        return new CombinedData(binanceData, mexcData);
    }

    public void addBinanceData(BinanceRecord record) {
        if (binanceData.size() >= 20) {
            binanceData.remove(0);
        }
        binanceData.add(record);
    }

    public void addMEXCData(MEXCRecord record) {
        if (mexcData.size() >= 20) {
            mexcData.remove(0);
        }
        mexcData.add(record);
    }

    public static class CombinedData {
        private List<BinanceRecord> binanceData;
        private List<MEXCRecord> mexcData;

        public CombinedData(List<BinanceRecord> binanceData, List<MEXCRecord> mexcData) {
            this.binanceData = binanceData;
            this.mexcData = mexcData;
        }

        // Getters and setters
    }

    public static class BinanceRecord {
        private String symbol;
        private double bidSum;
        private double askSum;
        private String bidDetails;
        private String askDetails;
        private String eventType;

        // Constructors, getters, and setters
    }

    public static class MEXCRecord {
        private String symbol;
        private double high24h;
        private double low24h;
        private String volume24h;
        private String amount24h;
        private String eventType;

        // Constructors, getters, and setters
    }
}
