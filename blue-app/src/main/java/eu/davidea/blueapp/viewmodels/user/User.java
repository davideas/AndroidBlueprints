package eu.davidea.blueapp.viewmodels.user;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import eu.davidea.blueapp.viewmodels.enums.EnumAuthority;
import eu.davidea.blueapp.viewmodels.enums.EnumUserStatus;


/**
 * @author Davide
 * @since 27/08/2017
 */
@Entity
public class User {

    @PrimaryKey
    protected Long id;
    protected Date creDate;
    protected Date modDate;
    protected String username;
    protected String nickname;
    protected String firstname;
    protected String lastname;
    protected String email;
    protected boolean termsAccepted;
    protected EnumAuthority authority;
    protected EnumUserStatus status;
    protected Date lastPasswordChangeDate;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreDate() {
        return creDate;
    }

    public void setCreDate(Date creDate) {
        this.creDate = creDate;
    }

    public Date getModDate() {
        return modDate;
    }

    public void setModDate(Date modDate) {
        this.modDate = modDate;
    }

    public String getName() {
        if (firstname != null && !firstname.isEmpty() && lastname != null && !lastname.isEmpty()) {
            return firstname + " " + lastname;
        }
        return getUsername();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isTermsAccepted() {
        return termsAccepted;
    }

    public void setTermsAccepted(boolean termsAccepted) {
        this.termsAccepted = termsAccepted;
    }

    public EnumAuthority getAuthority() {
        return authority;
    }

    public void setAuthority(EnumAuthority authority) {
        this.authority = authority;
    }

    public EnumUserStatus getStatus() {
        return status;
    }

    public void setStatus(EnumUserStatus status) {
        this.status = status;
    }

    public Date getLastPasswordChangeDate() {
        return lastPasswordChangeDate;
    }

    public void setLastPasswordChangeDate(Date lastPasswordChangeDate) {
        this.lastPasswordChangeDate = lastPasswordChangeDate;
    }

}