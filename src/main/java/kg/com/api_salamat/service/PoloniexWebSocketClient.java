package kg.com.api_salamat.service;

import kg.com.api_salamat.controller.PriceController;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;

public class PoloniexWebSocketClient extends WebSocketClient {

    private final String symbol;
    private final PriceController priceController;

    public PoloniexWebSocketClient(URI serverUri, String symbol, PriceController priceController) {
        super(serverUri);
        this.symbol = symbol;
        this.priceController = priceController;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to Poloniex for symbol: " + symbol);
        String subscriptionMessage = String.format("{\"event\": \"subscribe\", \"channel\": [\"ticker\"], \"symbols\": \"%s\"}", symbol);
        String subscriptionMessageDepth = "{ \"event\": \"subscribe\", \"channel\": [\"book\"], \"symbols\": [\"" + symbol + "\"] }";
        send(subscriptionMessageDepth);
    }

    @Override
    public void onMessage(String message) {
        JSONObject json = new JSONObject(message);
        System.out.println(message);
        if (json.has("bid") && json.has("ask") && json.has("bidQuantity") && json.has("askQuantity")) {
            double buyPrice = json.getDouble("bids");
            double sellPrice = json.getDouble("asks");
            double buyVolume = json.getDouble("askQuantity");
            double sellVolume = json.getDouble("askQuantity");

            double profit = sellPrice - buyPrice;
            double spread = (profit / buyPrice) * 100;

            // Обновляем данные
            priceController.updateData(symbol, "Poloniex", buyPrice, sellPrice, profit, spread, buyVolume, sellVolume);
        }
    }


    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected from Poloniex: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}
