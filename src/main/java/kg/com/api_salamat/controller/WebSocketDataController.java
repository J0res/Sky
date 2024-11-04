package kg.com.api_salamat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class WebSocketDataController {

    private final List<Record> history = new CopyOnWriteArrayList<>();

    @GetMapping("/merged-data")
    public List<Record> getMergedData() {
        return new CopyOnWriteArrayList<>(history);
    }

    @PostMapping("/merged-data")
    public void addMergedData(@RequestBody Record record) {
        if (history.size() >= 20) {
            history.remove(0);
        }
        history.add(record);
    }

    public void addLatestData(Record record) {
        if (history.size() >= 20) {
            history.remove(0);
        }
        history.add(record);
    }

    public static class Record {
        private String symbol;
        private double bidSum;
        private double askSum;
        private String eventType;
        private double high24h;
        private double low24h;
        private String volume24h;
        private String amount24h;

        // Constructors, getters, and setters
    }
}
