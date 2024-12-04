package kg.com.api_salamat.service;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;

public abstract class BaseWebSocketClient extends WebSocketClient {
    protected final String symbol;

    public BaseWebSocketClient(URI serverUri, String symbol) {
        super(serverUri);
        this.symbol = symbol;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to WebSocket for symbol: " + symbol);
        subscribe();
    }

    @Override
    public void onMessage(String message) {
        JSONObject json = new JSONObject(message);
        handleMessage(json);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.printf("Connection closed for %s with code %d: %s%n", symbol, code, reason);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("Error for " + symbol + ": " + ex.getMessage());
    }

    protected abstract void subscribe();

    protected abstract void handleMessage(JSONObject json);
}

