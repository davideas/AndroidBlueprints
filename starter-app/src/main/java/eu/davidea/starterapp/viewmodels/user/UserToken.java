package eu.davidea.starterapp.viewmodels.user;

import android.arch.persistence.room.Entity;

import java.util.Date;

/**
 * @author Davide Steduto
 * @since 04/09/2017
 */
@Entity
public class UserToken extends User {

    private Long userId;
    private String token;
    private Date lastLoginDate;

    public UserToken() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

}