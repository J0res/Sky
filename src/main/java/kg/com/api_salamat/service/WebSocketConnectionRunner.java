package kg.com.api_salamat.service;

import kg.com.api_salamat.controller.PriceController;
import kg.com.api_salamat.util.SymbolFormatter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;

@Component
public class WebSocketConnectionRunner implements CommandLineRunner {

    private final PriceController priceController;

    public WebSocketConnectionRunner(PriceController priceController) {
        this.priceController = priceController;
    }

    @Override
    public void run(String... args) {
        List<String> symbols = List.of("BTCUSDT", "ETHUSDT");

        symbols.forEach(symbol -> {
            try {
                // OKX
                String okxSymbol = SymbolFormatter.formatSymbol("OKX", symbol); // Обрабатываем символ
                System.out.println("Connecting to OKX with symbol: " + okxSymbol);
                new OKXWebSocketClient(new URI("wss://ws.okx.com:8443/ws/v5/public"), okxSymbol, priceController).connectBlocking();

                // Binance
                String binanceSymbol = SymbolFormatter.formatSymbol("Binance", symbol); // Обрабатываем символ
                System.out.println("Connecting to Binance with symbol: " + binanceSymbol);
                new BinanceWebSocketClient(new URI("wss://stream.binance.com:9443/ws"), binanceSymbol, priceController).connectBlocking();

                // MEXC
                String mexcSymbol = SymbolFormatter.formatSymbol("MEXC", symbol); // Обрабатываем символ
                System.out.println("Connecting to MEXC with symbol: " + mexcSymbol);
                new MEXCWebSocketClient(new URI("wss://wbs.mexc.com/ws"), mexcSymbol, priceController).connectBlocking();
//dasd
//                // Poloniex
//                String poloniexSymbol = SymbolFormatter.formatSymbol("Poloniex", symbol); // Обрабатываем символ
//                System.out.println("Connecting to Poloniex with symbol: " + poloniexSymbol);
//                new PoloniexWebSocketClient(new URI("https://api.poloniex.com/markets/ticker24h"), poloniexSymbol, priceController).connectBlocking();

                // Bitget
                String bitgetSymbol = SymbolFormatter.formatSymbol("Bitget", symbol);
                System.out.println("Connecting to Bitget with symbol: " + bitgetSymbol);
                new BitgetWebSocketClient(new URI("wss://ws.bitget.com/v2/ws/public"), bitgetSymbol, priceController).connectBlocking();

            } catch (Exception e) {
                System.err.println("Error connecting to WebSocket for symbol: " + symbol);
                e.printStackTrace();
            }
        });
    }
}
