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
//public class BinanceWebSocketClient extends WebSocketClient {
//
//    private final String symbol;
//    private final PriceController priceController;
//
//    public BinanceWebSocketClient(URI serverUri, PriceController priceController, String symbol) {
//        super(serverUri);
//        this.priceController = priceController;
//        this.symbol = symbol;
//    }
//
//    @Override
//    public void onOpen(ServerHandshake handshakedata) {
//        System.out.println("Connected to Binance stream: " + this.getURI());
//        sendUpdateRequest();
//    }
//
//    @Override
//    public void onMessage(String message) {
//        JSONObject json = new JSONObject(message);
//
//        if (json.has("e")) {
//            String eventType = json.getString("e");
//
//            if (eventType.equals("depthUpdate")) {
//                handleDepthUpdate(json);
//            } else if (eventType.equals("24hrTicker")) {
//                handle24HrTickerUpdate(json);
//            }
//        }
//    }
//
//    private void handleDepthUpdate(JSONObject json) {
//        String symbol = json.getString("s");
//        JSONArray bidArray = json.getJSONArray("b");
//        JSONArray askArray = json.getJSONArray("a");
//
//        double bidSum = 0.0;
//        double askSum = 0.0;
//        double bidTotalPrice = 0.0;
//        double askTotalPrice = 0.0;
//
//        for (int i = 0; i < bidArray.length(); i++) {
//            JSONArray bid = bidArray.getJSONArray(i);
//            double bidPrice = bid.getDouble(0);
//            double bidQuantity = bid.getDouble(1);
//            double bidValue = bidPrice * bidQuantity;
//
//            if (bidValue >= 1000 && bidValue <= 2000) {
//                bidSum = bidQuantity;
//                bidTotalPrice = bidValue;
//                break;
//            }
//        }
//
//        for (int i = 0; i < askArray.length(); i++) {
//            JSONArray ask = askArray.getJSONArray(i);
//            double askPrice = ask.getDouble(0);
//            double askQuantity = ask.getDouble(1);
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
//            priceController.updateBinanceData(
//                    symbol,
//                    averageBidPrice,
//                    averageAskPrice,
//                    bidTotalPrice,
//                    askTotalPrice,
//                    0.0,
//                    0.0,
//                    0.0
//            );
//        }
//    }
//
//    private void handle24HrTickerUpdate(JSONObject json) {
//        String symbol = json.getString("s");
//        double high24h = json.getDouble("h");
//        double low24h = json.getDouble("l");
//        double volume24hInUSDT = json.getDouble("q");
//
//        priceController.updateBinanceData(
//                symbol,
//                0.0,
//                0.0,
//                0.0,
//                0.0,
//                high24h,
//                low24h,
//                volume24hInUSDT
//        );
//    }
//
//    public void sendUpdateRequest() {
//        String subscriptionMessageTicker = "{ \"method\": \"SUBSCRIBE\", \"params\": [\"" + symbol + "@ticker\"], \"id\": 1 }";
//        String subscriptionMessageDepth = "{ \"method\": \"SUBSCRIBE\", \"params\": [\"" + symbol + "@depth\"], \"id\": 2 }";
//        this.send(subscriptionMessageTicker);
//        this.send(subscriptionMessageDepth);
//    }
//
//    @Override
//    public void onClose(int code, String reason, boolean remote) {
//        System.out.println("Connection closed with exit code " + code + " additional info: " + reason);
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
//        System.err.println("An error occurred: " + ex.getMessage());
//    }
//}
