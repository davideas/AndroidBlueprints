package eu.davidea.starterapp.viewmodels.message;

import android.arch.lifecycle.ViewModel;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import eu.davidea.starterapp.viewmodels.enums.EnumMessageStatus;

/**
 * @author Davide
 * @since 24/09/2017
 */
@Entity
public class Message extends ViewModel {

    @PrimaryKey
    private Long id;
    private Long userId;
    private Long threadId; // Conversation identifier
    private Date creDate;
    private String message;
    private EnumMessageStatus status;

    public Message() {
    }

    @Ignore
    public Message(Long id, Long userId, Long threadId) {
        this.creDate = new Date();
        this.status = EnumMessageStatus.RECEIVED;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public Date getCreDate() {
        return creDate;
    }

    public void setCreDate(Date creDate) {
        this.creDate = creDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EnumMessageStatus getStatus() {
        return status;
    }

    public void setStatus(EnumMessageStatus status) {
        this.status = status;
    }

}