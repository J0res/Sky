package kg.com.api_salamat.service;

import kg.com.api_salamat.controller.PriceController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Component
@Order(1)
public class WebSocketConnectionRunner implements CommandLineRunner {

    @Autowired
    private PriceController priceController;

//    @Value("${binance.symbols}")
//    private String binanceSymbols;
//
//    @Value("${mexc.symbols}")
//    private String mexcSymbols;
//
//    @Value("${okx.symbols}")
//    private String okxSymbols;

//    @Value("${bitget.symbols}")
//    private String bitgetSymbols;

//    @Value("${bitmart.symbols}")
//    private String bitmartSymbols;

    @Value("${poloniex.symbols}")
    private String poloniexSymbols;

    @Override
    public void run(String... args) {
//        List<String> binanceSymbolList = List.of(binanceSymbols.split(","));
//        List<String> mexcSymbolList = List.of(mexcSymbols.split(","));
//        List<String> okxSymbolList = List.of(okxSymbols.split(","));
//        List<String>  bitmartSymbolList = List.of( bitmartSymbols.split(","));
//        List<String> bitgetSymbolList = List.of(bitgetSymbols.split(","));
        List<String> poloniexSymbolList = List.of(poloniexSymbols.split(","));

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(50);

        try {
//            // Подключение к Binance
//            for (String symbol : binanceSymbolList) {
//                executor.execute(() -> {
//                    try {
//                        String depthUrl = String.format("wss://stream.binance.com:9443/ws/%s@depth", symbol);
//                        BinanceWebSocketClient depthClient = new BinanceWebSocketClient(new URI(depthUrl), priceController, symbol);
//                        depthClient.connect();
//
//                        String tickerUrl = String.format("wss://stream.binance.com:9443/ws/%s@ticker", symbol);
//                        BinanceWebSocketClient tickerClient = new BinanceWebSocketClient(new URI(tickerUrl), priceController, symbol);
//                        tickerClient.connect();
//                    } catch (URISyntaxException e) {
//                        e.printStackTrace();
//                    }
//                });
//            }

            // Подключение к MEXC
//            for (String symbol : mexcSymbolList) {
//                executor.execute(() -> {
//                    try {
//                        String wsUrl = "wss://wbs.mexc.com/ws";
//                        MEXCWebSocketClient client = new MEXCWebSocketClient(new URI(wsUrl), priceController, symbol);
//                        client.connect();
//                    } catch (URISyntaxException e) {
//                        e.printStackTrace();
//                    }
//                });
//            }

            // Подключение к OKX
//            for (String symbol : okxSymbolList) {
//                executor.execute(() -> {
//                    try {
//                        String wsUrl = "wss://ws.okx.com:8443/ws/v5/public";
//                        OKXWebSocketClient client = new OKXWebSocketClient(new URI(wsUrl), symbol, priceController);
//                        client.connectBlocking();
//                    } catch (URISyntaxException | InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                });
//            }

            // Подключение к Bitget
//            for (String symbol : bitgetSymbolList) {
//                executor.execute(() -> {
//                    try {
//                        String wsUrl = "wss://ws.bitget.com/v2/ws/public";
//                        BitgetWebSocketClient client = new BitgetWebSocketClient(new URI(wsUrl), symbol, priceController);
//                        client.connectBlocking();
//                    } catch (URISyntaxException | InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                });
//            }

//            for (String symbol : bitmartSymbolList) {
//                executor.execute(() -> {
//                    try {
//                        String wsUrl = "wss://ws-manager-compress.bitmart.com/api?protocol=1.1";
//                        BitMartWebSocketClient client = new BitMartWebSocketClient(new URI(wsUrl), symbol, priceController);
//                        client.connect();
//                    } catch (URISyntaxException e) {
//                        e.printStackTrace();
//                    }
//                });
//            }

            for (String symbol : poloniexSymbolList){
                executor.execute(() -> {
                    try {
                        String wsUrl = "wss://ws.poloniex.com/ws/public";
                        PoloniexWebSocketClient client = new PoloniexWebSocketClient(new URI(wsUrl),symbol,priceController);
                        client.connectBlocking();
                    } catch (URISyntaxException | InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
