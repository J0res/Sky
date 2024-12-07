package kg.com.api_salamat.service;

import kg.com.api_salamat.controller.PriceController;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;

public class BitgetWebSocketClient extends WebSocketClient {

    private final String symbol;
    private final PriceController priceController;

    public BitgetWebSocketClient(URI serverUri, String symbol, PriceController priceController) {
        super(serverUri);
        this.symbol = symbol;
        this.priceController = priceController;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to Bitget for symbol: " + symbol);
        String subscriptionMessage = String.format(
                "{\"op\":\"subscribe\",\"args\":[{\"instType\":\"SPOT\",\"channel\":\"ticker\",\"instId\":\"%s\"}]}",
                symbol);
        send(subscriptionMessage);
    }

    @Override
    public void onMessage(String message) {
        try {
            JSONObject json = new JSONObject(message);
            if (json.has("data")) {
                JSONObject data = json.getJSONArray("data").getJSONObject(0);

                double buyPrice = data.getDouble("bidPr"); // Лучшая цена покупки
                double sellPrice = data.getDouble("askPr"); // Лучшая цена продажи
                double buyVolume = data.getDouble("bidSz"); // Объем покупки
                double sellVolume = data.getDouble("askSz"); // Объем продажи

                // Расчёт прибыли и спреда
                double profit = sellPrice - buyPrice;
                double spread = (profit / buyPrice) * 100;

                // Передача данных в контроллер
                priceController.updateData(symbol, "Bitget", buyPrice, sellPrice, profit, spread, buyVolume, sellVolume);
            }
        } catch (Exception e) {
            System.err.println("Error processing Bitget message: " + e.getMessage());
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected from Bitget: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}
