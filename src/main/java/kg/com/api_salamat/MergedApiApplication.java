package kg.com.api_salamat;

import kg.com.api_salamat.controller.PriceController;
//import kg.com.api_salamat.service.BinanceWebSocketClient;
//import kg.com.api_salamat.service.BitgetWebSocketClient;
//import kg.com.api_salamat.service.MEXCWebSocketClient;
//import kg.com.api_salamat.service.OKXWebSocketClient;
//import kg.com.api_salamat.service.BitMartWebSocketClient;
import kg.com.api_salamat.service.PoloniexWebSocketClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@SpringBootApplication
public class MergedApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MergedApiApplication.class, args);
    }

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

    @Bean
    public PriceController priceController() throws Exception {
        return new PriceController();
    }

    @Bean
    public CommandLineRunner run(PriceController priceController) {
        return args -> {
//            List<String> binanceSymbolList = List.of(binanceSymbols.split(","));
//            List<String> mexcSymbolList = List.of(mexcSymbols.split(","));
//            List<String> okxSymbolList = List.of(okxSymbols.split(","));
//            List<String> bitmartSymbolList = List.of(bitmartSymbols.split(","));
//            List<String> bitgetSymbolList = List.of(bitgetSymbols.split(","));
            List<String> poloniexSymbolList = List.of(poloniexSymbols.split(","));


            try {
                // Подключение к Binance
//                for (String symbol : binanceSymbolList) {
//                    String depthUrl = String.format("wss://stream.binance.com:9443/ws/%s@depth", symbol);
//                    BinanceWebSocketClient depthClient = new BinanceWebSocketClient(new URI(depthUrl), priceController, symbol);
//                    depthClient.connect();
//
//                    String tickerUrl = String.format("wss://stream.binance.com:9443/ws/%s@ticker", symbol);
//                    BinanceWebSocketClient tickerClient = new BinanceWebSocketClient(new URI(tickerUrl), priceController, symbol);
//                    tickerClient.connect();
//                }
//
//                // Подключение к MEXC
//                for (String symbol : mexcSymbolList) {
//                    String wsUrl = "wss://wbs.mexc.com/ws";
//                    MEXCWebSocketClient client = new MEXCWebSocketClient(new URI(wsUrl), priceController, symbol);
//                    client.connect();
//                }
//
//                // Подключение к OKX
//                for (String symbol : okxSymbolList) {
//                    String wsUrl = "wss://ws.okx.com:8443/ws/v5/public";
//                    OKXWebSocketClient client = new OKXWebSocketClient(new URI(wsUrl), symbol, priceController);
//                    client.connectBlocking();
//                }

//                for (String symbol : bitgetSymbolList){
//                    String wsUrl = "wss://ws.bitget.com/v2/ws/public";
//                    BitgetWebSocketClient client = new BitgetWebSocketClient(new URI(wsUrl), symbol, priceController);
//                    client.connectBlocking();
//                }

//                for (String symbol : bitmartSymbolList) {
//                    String wsUrl = "wss://ws-manager-compress.bitmart.com/api?protocol=1.1";
//                    BitMartWebSocketClient client = new BitMartWebSocketClient(new URI(wsUrl), symbol, priceController);
//                    client.connect();
//                }

                for (String symbol : poloniexSymbolList){
                    String wsUrl = "wss://ws.poloniex.com/ws/public";
                    PoloniexWebSocketClient client = new PoloniexWebSocketClient(new URI(wsUrl), symbol, priceController);
                    client.connectBlocking();
                }

            } catch (URISyntaxException  e) {
                e.printStackTrace();
            }
        };
    }
}
