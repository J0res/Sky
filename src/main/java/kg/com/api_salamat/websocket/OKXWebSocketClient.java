//package kg.com.api_salamat.websocket;
//
//import kg.com.api_salamat.service.BaseWebSocketClient;
//import kg.com.api_salamat.controller.PriceController;
//import kg.com.api_salamat.utils.SymbolFormatter;
//import org.json.JSONObject;
//
//import java.net.URI;
//
//public class OKXWebSocketClient extends BaseWebSocketClient {
//    private final PriceController priceController;
//    private final SymbolFormatter symbolFormatter;
//
//    public OKXWebSocketClient(URI serverUri, String symbol, PriceController priceController, SymbolFormatter symbolFormatter) {
//        super(serverUri, symbol);
//        this.priceController = priceController;
//        this.symbolFormatter = symbolFormatter;
//    }
//
//    @Override
//    protected void subscribe() {
//        String formattedSymbol = symbolFormatter.formatSymbol(symbol, "OKX");
//        String subscriptionMessage = "{ \"op\": \"subscribe\", \"args\": [{ \"channel\": \"tickers\", \"instId\": \"" + formattedSymbol + "\" }] }";
//        send(subscriptionMessage);
//    }
//
//    @Override
//    protected void handleMessage(JSONObject json) {
//        // Реализуйте обработку данных OKX
//    }
//}
//
