package com.chatapp.chatapp.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class RedisChatRepository {

    private final StringRedisTemplate redisTemplate;

    public RedisChatRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public boolean createRoom(String roomId, Map<String, String> metadata) {

        String allRoomsKey = RedisKeyBuilder.allChatRoomsKey();

        // Atomic uniqueness check
        Long added = redisTemplate.opsForSet().add(allRoomsKey, roomId);

        if (added == null || added == 0) {
            return false; // room already exists
        }

        // Store metadata as Hash
        redisTemplate.opsForHash()
                .putAll(RedisKeyBuilder.chatRoomKey(roomId), metadata);

        return true;
    }

    public boolean roomExists(String roomId) {
        return Boolean.TRUE.equals(
                redisTemplate.opsForSet()
                        .isMember(RedisKeyBuilder.allChatRoomsKey(), roomId)
        );
    }

    public boolean isParticipantMember(String roomId, String participant) {
        return Boolean.TRUE.equals(
                redisTemplate.opsForSet()
                        .isMember(RedisKeyBuilder.participantsKey(roomId), participant)
        );
    }


    public void addParticipant(String roomId, String participant) {
        redisTemplate.opsForSet()
                .add(RedisKeyBuilder.participantsKey(roomId), participant);
    }

    public Set<String> getParticipants(String roomId) {
        return redisTemplate.opsForSet()
                .members(RedisKeyBuilder.participantsKey(roomId));
    }

    public void saveMessage(String roomId, String messageJson) {
        redisTemplate.opsForList()
                .rightPush(RedisKeyBuilder.messagesKey(roomId), messageJson);
    }

    public List<String> getLastMessages(String roomId, int limit) {
        return redisTemplate.opsForList()
                .range(RedisKeyBuilder.messagesKey(roomId), -limit, -1);
    }

    public void publishMessage(String roomId, String messageJson) {
        redisTemplate.convertAndSend(
                RedisKeyBuilder.channelKey(roomId),
                messageJson
        );
    }
    public boolean deleteRoom(String roomId) {

        Long removed = redisTemplate.opsForSet()
                .remove(RedisKeyBuilder.allChatRoomsKey(), roomId);

        if (removed == null || removed == 0) {
            return false; // room does not exist
        }

        redisTemplate.delete(RedisKeyBuilder.chatRoomKey(roomId));
        redisTemplate.delete(RedisKeyBuilder.participantsKey(roomId));
        redisTemplate.delete(RedisKeyBuilder.messagesKey(roomId));

        return true;
    }

}

