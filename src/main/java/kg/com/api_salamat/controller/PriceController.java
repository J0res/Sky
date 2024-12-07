package kg.com.api_salamat.controller;

import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import kg.com.api_salamat.service.TelegramService;

@Controller
public class PriceController {
    private final TelegramService telegramService;
    public PriceController(TelegramService telegramService) {
        this.telegramService = telegramService;
    }
    private static final class PriceData {
        double buyPrice;
        double sellPrice;
        double buyVolume;
        double sellVolume;

        public PriceData(double buyPrice, double sellPrice, double buyVolume, double sellVolume) {
            this.buyPrice = buyPrice;
            this.sellPrice = sellPrice;
            this.buyVolume = buyVolume;
            this.sellVolume = sellVolume;
        }
    }

    private final Map<String, ConcurrentHashMap<String, PriceData>> data = new ConcurrentHashMap<>();

    public synchronized void updateData(String symbol, String exchange, double buyPrice, double sellPrice,
                                        double profit, double spread, double buyVolume, double sellVolume) {
        data.putIfAbsent(symbol, new ConcurrentHashMap<>());
        data.get(symbol).put(exchange, new PriceData(buyPrice, sellPrice, buyVolume, sellVolume));

        System.out.printf("Update received: Symbol=%s, Exchange=%s, BuyPrice=%.2f, SellPrice=%.2f, " +
                "BuyVolume=%.6f, SellVolume=%.6f%n", symbol, exchange, buyPrice, sellPrice, buyVolume, sellVolume);

        compareAndSendBestOpportunities(symbol);
    }

    private void compareAndSendBestOpportunities(String symbol) {
        ConcurrentHashMap<String, PriceData> symbolData = data.get(symbol);
        if (symbolData == null || symbolData.size() < 2) return;

        String bestBuyExchange = null;
        String bestSellExchange = null;

        double bestBuyPrice = Double.MAX_VALUE;
        double bestSellPrice = Double.MIN_VALUE;
        //dawdawd
        double bestBuyVolume = 0.0;
        double bestSellVolume = 0.0;

        for (Map.Entry<String, PriceData> entry : symbolData.entrySet()) {
            String exchange = entry.getKey();
            PriceData priceData = entry.getValue();

            if (priceData.buyPrice < bestBuyPrice) {
                bestBuyPrice = priceData.buyPrice;
                bestBuyExchange = exchange;
                bestBuyVolume = priceData.buyVolume;
            }

            if (priceData.sellPrice > bestSellPrice) {
                bestSellPrice = priceData.sellPrice;
                bestSellExchange = exchange;
                bestSellVolume = priceData.sellVolume;
            }
        }

        if (bestBuyExchange != null && bestSellExchange != null) {
            double profit = bestSellPrice - bestBuyPrice;
            double spread = (profit / bestBuyPrice) * 100;

            String message = String.format(
                    "💸 *%s -> %s | %s*\n\n" +
                            "📈 *Покупка:*\nЦена: %.2f USDT\nОбъём: %.6f\n\n" +
                            "📉 *Продажа:*\nЦена: %.2f USDT\nОбъём: %.6f\n\n" +
                            "💰 *Прибыль:* %.2f USDT\n📊 *Спред:* %.2f%%\n",
                    bestBuyExchange, bestSellExchange, symbol,
                    bestBuyPrice, bestBuyVolume,
                    bestSellPrice, bestSellVolume,
                    profit, spread
            );

            System.out.println(message); // Для проверки в консоли

            //telegramService.sendMessage(message); //Для телеги
        }
    }
}
