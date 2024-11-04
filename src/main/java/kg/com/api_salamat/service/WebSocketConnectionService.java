//package kg.com.api_salamat.service;
//
//import kg.com.api_salamat.controller.PriceController;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//import java.net.URI;
//import java.net.URISyntaxException;
//
//@Service
//public class WebSocketConnectionService {
//
//    @Async
//    public void connectToBinance(PriceController priceController, String symbol) {
//        try {
//            String depthUrl = String.format("wss://stream.binance.com:9443/ws/%s@depth", symbol);
//            BinanceWebSocketClient depthClient = new BinanceWebSocketClient(new URI(depthUrl), priceController, symbol);
//            depthClient.connect();
//
//            String tickerUrl = String.format("wss://stream.binance.com:9443/ws/%s@ticker", symbol);
//            BinanceWebSocketClient tickerClient = new BinanceWebSocketClient(new URI(tickerUrl), priceController, symbol);
//            tickerClient.connect();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Async
//    public void connectToMEXC(PriceController priceController, String symbol) {
//        try {
//            String wsUrl = "wss://wbs.mexc.com/ws";
//            MEXCWebSocketClient client = new MEXCWebSocketClient(new URI(wsUrl), priceController, symbol);
//            client.connect();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Async
//    public void connectToOKX(PriceController priceController, String symbol) {
//        try {
//            String wsUrl = "wss://ws.okx.com:8443/ws/v5/public";
//            OKXWebSocketClient client = new OKXWebSocketClient(new URI(wsUrl), symbol, priceController);
//            client.connectBlocking();
//        } catch (URISyntaxException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//}
//
