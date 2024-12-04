package kg.com.api_salamat;

import kg.com.api_salamat.bot.SalamatBot;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@SpringBootApplication
public class MergedApiApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MergedApiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Инициализация TelegramBotsApi
        TelegramBotsApi botsApi = new TelegramBotsApi();

        // Регистрация бота
        try {
            botsApi.registerBot(new SalamatBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
