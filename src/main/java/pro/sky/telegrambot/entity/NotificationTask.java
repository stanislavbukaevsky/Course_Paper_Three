package pro.sky.telegrambot.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class NotificationTask {
    @Id
    @GeneratedValue
    private Long primaryKey;
    private Long chatId;
    private String userName;
    private String textMessage;
    private LocalDateTime dateAndTime;

    public NotificationTask() {
    }

    public Long getPrimaryKey() {
        return this.primaryKey;
    }

    public Long getChatId() {
        return this.chatId;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getTextMessage() {
        return this.textMessage;
    }

    public LocalDateTime getDateAndTime() {
        return this.dateAndTime;
    }

    public void setPrimaryKey(Long primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return Objects.equals(primaryKey, that.primaryKey) && Objects.equals(chatId, that.chatId) && Objects.equals(userName, that.userName) && Objects.equals(textMessage, that.textMessage) && Objects.equals(dateAndTime, that.dateAndTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(primaryKey, chatId, userName, textMessage, dateAndTime);
    }

    @Override
    public String toString() {
        return "Дата: " + dateAndTime + ". Текстовое сообщение: " + textMessage + ". Имя: " + userName;
    }
}
