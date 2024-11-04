//package kg.com.api_salamat.service;
//
//import kg.com.api_salamat.controller.PriceController;
//import org.java_websocket.client.WebSocketClient;
//import org.java_websocket.handshake.ServerHandshake;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.net.URI;
//
//
//public class MEXCWebSocketClient extends WebSocketClient {
//
//    private final String symbol;
//    private final PriceController priceController;
//
//    public MEXCWebSocketClient(URI serverUri, PriceController priceController, String symbol) {
//        super(serverUri);
//        this.priceController = priceController;
//        this.symbol = symbol;
//    }
//
//    @Override
//    public void onOpen(ServerHandshake handshakedata) {
//        System.out.println("Connected to MEXC stream for symbol: " + symbol);
//        sendUpdateRequest();
//    }
//
//    @Override
//    public void onMessage(String message) {
//        JSONObject json = new JSONObject(message);
//
//        try {
//            if (json.has("d")) {
//                JSONObject data = json.getJSONObject("d");
//
//                if (data.has("s") && data.has("p")) {
//                    handleMiniTickerUpdate(data);
//                } else if (data.has("asks") && data.has("bids")) {
//                    handleDepthUpdate(data);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void handleMiniTickerUpdate(JSONObject data) {
//        String symbol = data.getString("s");
//        double high24h = Double.parseDouble(data.optString("h", "0.0"));
//        double low24h = Double.parseDouble(data.optString("l", "0.0"));
//        double volume24h = Double.parseDouble(data.optString("v", "0.0"));
//        double lastPriceInUSDT = Double.parseDouble(data.optString("p", "0.0"));
//
//        double volume24hInUSDT = volume24h * lastPriceInUSDT;
//
//        priceController.updateMEXCData(
//                symbol,
//                0.0,      // averageBidPrice - обновляется отдельно
//                0.0,      // averageAskPrice - обновляется отдельно
//                0.0,      // bidSumUSDT - обновляется отдельно
//                0.0,      // askSumUSDT - обновляется отдельно
//                high24h,
//                low24h,
//                volume24hInUSDT
//        );
//    }
//
//    private void handleDepthUpdate(JSONObject data) {
//        JSONArray asks = data.getJSONArray("asks");
//        JSONArray bids = data.getJSONArray("bids");
//
//        double bidSum = 0.0;
//        double askSum = 0.0;
//        double bidTotalPrice = 0.0;
//        double askTotalPrice = 0.0;
//
//        for (int i = 0; i < bids.length(); i++) {
//            JSONObject bid = bids.getJSONObject(i);
//            double bidPrice = bid.getDouble("p");
//            double bidQuantity = bid.getDouble("v");
//            double bidValue = bidPrice * bidQuantity;
//
//            if (bidValue >= 1000 && bidValue <= 2000) {
//                bidSum = bidQuantity;
//                bidTotalPrice = bidValue;
//                break;
//            }
//        }
//
//        for (int i = 0; i < asks.length(); i++) {
//            JSONObject ask = asks.getJSONObject(i);
//            double askPrice = ask.getDouble("p");
//            double askQuantity = ask.getDouble("v");
//            double askValue = askPrice * askQuantity;
//
//            if (askValue >= 1000 && askValue <= 2000) {
//                askSum = askQuantity;
//                askTotalPrice = askValue;
//                break;
//            }
//        }
//
//        double averageBidPrice = bidSum > 0 ? bidTotalPrice / bidSum : 0.0;
//        double averageAskPrice = askSum > 0 ? askTotalPrice / askSum : 0.0;
//
//        if (bidTotalPrice > 0 && askTotalPrice > 0) {
//            priceController.updateMEXCData(
//                    symbol,
//                    averageBidPrice,
//                    averageAskPrice,
//                    bidTotalPrice,
//                    askTotalPrice,
//                    0.0,   // high24h - обновляется отдельно
//                    0.0,   // low24h - обновляется отдельно
//                    0.0    // volume24hInBaseCurrency - обновляется отдельно
//            );
//        }
//    }
//
//    public void sendUpdateRequest() {
//        String subscriptionMessageDepth = "{ \"method\":\"SUBSCRIPTION\", \"params\":[\"spot@public.limit.depth.v3.api@" + symbol + "@20\"] }";
//        this.send(subscriptionMessageDepth);
//
//        String subscriptionMessageTicker = "{ \"method\":\"SUBSCRIPTION\", \"params\":[\"spot@public.miniTicker.v3.api@" + symbol + "@UTC+8\"] }";
//        this.send(subscriptionMessageTicker);
//    }
//
//    @Override
//    public void onClose(int code, String reason, boolean remote) {
//        System.out.printf("Connection closed for %s with exit code %d additional info: %s%n", symbol, code, reason);
//        if (code == 1006) {
//            reconnectWithBackoff();
//        }
//    }
//
//    public void reconnectWithBackoff() {
//        int retryCount = 0;
//        int maxRetries = 5;
//        long waitTime = 1000; // 1 секунда
//
//        while (retryCount < maxRetries) {
//            try {
//                Thread.sleep(waitTime);
//                connectBlocking(); // метод для переподключения
//                break;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            retryCount++;
//            waitTime *= 2; // Экспоненциальная задержка
//        }
//    }
//
//    @Override
//    public void onError(Exception ex) {
//        System.err.println("An error occurred for " + symbol + ": " + ex.getMessage());
//    }
//}
