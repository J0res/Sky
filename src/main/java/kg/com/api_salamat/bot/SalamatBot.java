package kg.com.api_salamat.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class SalamatBot extends TelegramLongPollingBot {

    private final String botToken = "7898662011:AAGrKL0X236YYkDwpzuDQVue5D1FnAY29_s";
    private final String botUsername = "Sa1amat_bot"; // Имя вашего бота

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        // Проверяем, что обновление содержит сообщение
        if (update.hasMessage()) {
            Message message = update.getMessage();

            // Обрабатываем команды
            if (message.hasText()) {
                String text = message.getText();
                if (text.equals("/start")) {
                    sendResponse(message.getChatId(), "Добро пожаловать в бота!");
                } else if (text.equals("/help")) {
                    sendResponse(message.getChatId(), "Команды: /start, /help");
                } else {
                    sendResponse(message.getChatId(), "Я вас не понимаю. Попробуйте /help для списка команд.");
                }
            }
        }
    }

    private void sendResponse(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(text);

        try {
            execute(sendMessage); // Отправляем сообщение
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
