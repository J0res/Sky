package kg.com.api_salamat.service;

import kg.com.api_salamat.controller.PriceController;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;

public class MEXCWebSocketClient extends WebSocketClient {

    private final String symbol;
    private final PriceController priceController;

    public MEXCWebSocketClient(URI serverUri, String symbol, PriceController priceController) {
        super(serverUri);
        this.symbol = symbol;
        this.priceController = priceController;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to MEXC for symbol: " + symbol);
        String subscriptionMessage = String.format("{\"method\":\"SUBSCRIPTION\",\"params\":[\"spot@public.bookTicker.v3.api@" + symbol + "\"]}");
        send(subscriptionMessage);
    }

    @Override
    public void onMessage(String message) {
        JSONObject json = new JSONObject(message);

        if (json.has("b") && json.has("a") && json.has("B") && json.has("A")) {
            double buyPrice = json.getDouble("b");
            double sellPrice = json.getDouble("a");
            double buyVolume = json.getDouble("B");
            double sellVolume = json.getDouble("A");

            double profit = sellPrice - buyPrice;
            double spread = (profit / buyPrice) * 100;

            // Обновляем данные
            priceController.updateData(symbol, "MEXC", buyPrice, sellPrice, profit, spread, buyVolume, sellVolume);
        }
    }


    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}
