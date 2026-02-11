package com.chatapp.chatapp.repository;
public class RedisKeyBuilder {

    private static final String CHATROOM_PREFIX = "chatroom:";
    private static final String PARTICIPANTS_SUFFIX = ":participants";
    private static final String MESSAGES_SUFFIX = ":messages";
    private static final String CHANNEL_SUFFIX = ":channel";
    private static final String CHATROOMS_SET = "chatrooms";

    public static String chatRoomKey(String roomId) {
        return CHATROOM_PREFIX + roomId;
    }

    public static String participantsKey(String roomId) {
        return CHATROOM_PREFIX + roomId + PARTICIPANTS_SUFFIX;
    }

    public static String messagesKey(String roomId) {
        return CHATROOM_PREFIX + roomId + MESSAGES_SUFFIX;
    }

    public static String channelKey(String roomId) {
        return CHATROOM_PREFIX + roomId + CHANNEL_SUFFIX;
    }

    public static String allChatRoomsKey() {
        return CHATROOMS_SET;
    }
}
