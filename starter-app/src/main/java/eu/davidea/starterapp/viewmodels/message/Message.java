package eu.davidea.starterapp.viewmodels.message;

import android.arch.lifecycle.ViewModel;

import java.util.Date;

import eu.davidea.starterapp.viewmodels.enums.EnumMessageStatus;

/**
 * @author Davide
 * @since 24/09/2017
 */
public class Message extends ViewModel {

    private Long id;
    private Long userId;
    private Long threadId; // Conversation identifier
    private Date creDate;
    private String message;
    private EnumMessageStatus status;

}