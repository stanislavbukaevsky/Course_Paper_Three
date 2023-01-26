package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.repositories.NotificationTaskRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;
    private final NotificationTaskRepository notificationTaskRepository;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, NotificationTaskRepository notificationTaskRepository) {
        this.telegramBot = telegramBot;
        this.notificationTaskRepository = notificationTaskRepository;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            // Process your updates here
            String textMessage = update.message().text();
            long chatId = update.message().chat().id();

            switch (textMessage) {
                case "/start":
                    sendMessage(chatId, "Привет, милый друг!");
                    break;
                case "/end":
                    sendMessage(chatId, "Всего хорошего!");
                    break;
                case "/help":
                    sendMessage(chatId, "/start - Запуск приложения " + "\n/end - Завершение сеанса");
                    break;
                default:
                    saveMessage(chatId, textMessage);
                    sendMessage(chatId, "Я сохранил твое сообщение!");
                    break;
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void sendMessage(long chatId, String textMessage) {
        SendResponse sendResponse = telegramBot.execute(new SendMessage(chatId, textMessage));
    }

    private void saveMessage(long chatId, String message) {
        Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)(\\s)([\\W+]+)");
        Matcher matcher = pattern.matcher(message);

        if (matcher.matches()) {
            NotificationTask notificationTask = new NotificationTask();
            String dateAndTime = matcher.group(1);
            String textMessage = matcher.group(3);
            String userName = matcher.group(5);
            LocalDateTime localDateTime = LocalDateTime.parse(dateAndTime, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
            notificationTask.setChatId(chatId);
            notificationTask.setDateAndTime(localDateTime);
            notificationTask.setTextMessage(textMessage);
            notificationTask.setUserName(userName);
            notificationTaskRepository.save(notificationTask);
            logger.info("Сообщение сохранено: " + notificationTask);
        }
    }

    @Scheduled(cron = "${scheduled.time}")
    public void run() {
        LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Collection<NotificationTask> notificationTasks = notificationTaskRepository.findNotificationTasksByDateAndTime(localDateTime);

        for (NotificationTask notificationTask : notificationTasks) {
            sendMessage(notificationTask.getChatId(), "У тебя есть новое задание: " + notificationTask.getTextMessage());
            logger.info("У тебя есть новое задание!");
        }

    }


}
