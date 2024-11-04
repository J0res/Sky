package kg.com.api_salamat.service;

import kg.com.api_salamat.controller.PriceController;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

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
        System.out.println("Connected to Poloniex stream for symbol: " + symbol);
        sendUpdateRequest();
    }

    @Override
    public void onMessage(String message) {
        JSONObject json = new JSONObject(message);
        //System.out.println(message);
        try {
            if (json.has("data")) {
                handleDataUpdate(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDataUpdate(JSONObject json) {
        JSONArray dataArray = json.getJSONArray("data");
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject data = dataArray.getJSONObject(i);
            if (data.has("close")) {
                handleTickerUpdate(data);
            }
            if (data.has("asks") && data.has("bids")) {
                handleDepthUpdate(data);
            }
        }
    }

    private void handleDepthUpdate(JSONObject data) {
        JSONArray asks = data.getJSONArray("asks");
        JSONArray bids = data.getJSONArray("bids");

        double bidSum = 0.0;
        double askSum = 0.0;
        double bidTotalPrice = 0.0;
        double askTotalPrice = 0.0;

        for (int i = 0; i < bids.length(); i++) {
            JSONArray bid = bids.getJSONArray(i);
            double bidPrice = bid.getDouble(0);
            double bidQuantity = bid.getDouble(1);
            double bidValue = bidPrice * bidQuantity;

            if (bidValue >= 1000 && bidValue <= 2000) { //Вот тут еще условие
                bidSum = bidQuantity;
                bidTotalPrice = bidValue;
                break;
            }
        }

        for (int i = 0; i < asks.length(); i++) {
            JSONArray ask = asks.getJSONArray(i);
            double askPrice = ask.getDouble(0);
            double askQuantity = ask.getDouble(1);
            double askValue = askPrice * askQuantity;

            if (askValue >= 1000 && askValue <= 2000) { //Вот тут еще условие
                askSum = askQuantity;
                askTotalPrice = askValue;
                break;
            }
        }

        double averageBidPrice = bidSum > 0 ? bidTotalPrice / bidSum : 0.0;
        double averageAskPrice = askSum > 0 ? askTotalPrice / askSum : 0.0;

        if (bidTotalPrice > 0 && askTotalPrice > 0) {
            priceController.updatePoloniexData(
                    symbol,
                    averageBidPrice,
                    averageAskPrice,
                    bidTotalPrice,
                    askTotalPrice,
                    0.0,   // high24h - обновляется отдельно
                    0.0,   // low24h - обновляется отдельно
                    0.0    // volume24hInBaseCurrency - обновляется отдельно
            );
        }
    }

    private void handleTickerUpdate(JSONObject data) {
        try {
            double high24h = data.getDouble("high");
            double low24h = data.getDouble("low");
            double volume24hInBaseCurrency = data.getDouble("amount");
            double lastPriceInUSDT = data.getDouble("close");

            double volume24hInUSDT = volume24hInBaseCurrency * lastPriceInUSDT;

            priceController.updatePoloniexData(
                    symbol,
                    0.0,      // averageBidPrice - обновляется отдельно
                    0.0,      // averageAskPrice - обновляется отдельно
                    0.0,      // bidSumUSDT - обновляется отдельно
                    0.0,      // askSumUSDT - обновляется отдельно
                    high24h,
                    low24h,
                    volume24hInUSDT
            );
        } catch (Exception e) {
            System.err.println("Error processing ticker update: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendUpdateRequest() {
        String subscriptionMessageTicker = "{ \"event\": \"subscribe\", \"channel\": [\"ticker\"], \"symbols\": [\"" + symbol + "\"] }";
        String subscriptionMessageDepth = "{ \"event\": \"subscribe\", \"channel\": [\"book\"], \"symbols\": [\"" + symbol + "\"] }";
        this.send(subscriptionMessageTicker);
        this.send(subscriptionMessageDepth);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.printf("Connection closed for %s with exit code %d additional info: %s%n", symbol, code, reason);
        if (code == 1006) {
            reconnectWithBackoff();
        }
    }

    public void reconnectWithBackoff() {
        int retryCount = 0;
        int maxRetries = 5;
        long waitTime = 1000; // 1 секунда

        while (retryCount < maxRetries) {
            try {
                Thread.sleep(waitTime);
                connectBlocking(); // метод для переподключения
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retryCount++;
            waitTime *= 2; // Экспоненциальная задержка
        }
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("An error occurred for " + symbol + ": " + ex.getMessage());
    }
}

