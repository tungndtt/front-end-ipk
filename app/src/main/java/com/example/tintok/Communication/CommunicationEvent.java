package com.example.tintok.Communication;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class CommunicationEvent {
    public static final String EVENT_GET_CHAT = "get_chat";
    public static final String EVENT_GET_NOTI = "get_notification";
    public static final String EVENT_GET_POST = "get_post";
    public static final String EVENT_NEW_MASSAGE = "new_message";
    public static final String EVENT_NEW_NOTI = "new_notification";
    public static final String EVENT_NEW_POST = "new_post";

    public static final String SEND_COMMENT = "SEND_COMMENT";
    public static final String NEW_COMMENT = "NEW_COMMENT";
    public static final String JOIN_POST = "JOIN_POST";
    public static final String LEAVE_POST = "LEAVE_POST";

    public static final String COMMENT_NOTIFICATION = "COMMENT_NOTIFICATION";
    public static final String LIKE_NOTIFICATION = "LIKE_NOTIFICATION";

    public static final String JOIN_CHAT_ROOM = "JOIN_CHAT_ROOM";
    public static final String LEAVE_CHAT_ROOM = "LEAVE_CHAT_ROOM";

}
