package com.supinfo.beunreal_user_service.service;

import com.supinfo.beunreal_user_service.UuidProvider;
import com.supinfo.beunreal_user_service.dao.FriendRequestDao;
import com.supinfo.beunreal_user_service.dao.UserDao;
import com.supinfo.beunreal_user_service.model.FriendRequest;
import com.supinfo.beunreal_user_service.model.FriendRequestStatus;
import com.supinfo.beunreal_user_service.model.KafkaMessage;
import com.supinfo.beunreal_user_service.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService {

    private final UserDao userDao;
    private final FriendRequestDao friendRequestDao;
    private final UuidProvider uuidProvider;

    public void addFriend(KafkaMessage kafkaMessage) {
        Map<String, String> request = kafkaMessage.getRequest();
        String friendId = request.get("friendId");

        FriendRequest friendRequest = FriendRequest.builder()
                .id(uuidProvider.generateUuid())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .recipientId(friendId)
                .senderId(kafkaMessage.getAuthenticatedUser().getId())
                .status(FriendRequestStatus.PENDING)
                .build();

        friendRequestDao.save(friendRequest);

        User authenticatedUser = kafkaMessage.getAuthenticatedUser();

        Set<String> pendingFriendRequestIds = authenticatedUser.getPendingFriendRequestIds();
        pendingFriendRequestIds.add(friendRequest.getId());
        authenticatedUser.setPendingFriendRequestIds(pendingFriendRequestIds);

        userDao.save(authenticatedUser);

        Optional<User> optionalFriend = userDao.findById(friendRequest.getSenderId());

        if (optionalFriend.isEmpty()) {
            log.warn("User with id : " + friendRequest.getSenderId() + " does not exist");
            return;
        }

        User friend = optionalFriend.get();

        Set<String> pendingFriendRequestIds1 = friend.getPendingFriendRequestIds();
        pendingFriendRequestIds1.add(friendRequest.getId());
        friend.setPendingFriendRequestIds(pendingFriendRequestIds1);

        userDao.save(friend);

        log.info("FriendRequest has been send from : " + kafkaMessage.getAuthenticatedUser().getUsername() + " to : " + friendId);
    }

    public void deleteFriend(KafkaMessage kafkaMessage) {
        Map<String, String> request = kafkaMessage.getRequest();
        String friendId = request.get("friendId");

        User authenticatedUser = kafkaMessage.getAuthenticatedUser();

        List<String> friendIds = authenticatedUser.getFriendIds();
        friendIds.remove(friendId);
        authenticatedUser.setFriendIds(friendIds);

        userDao.save(authenticatedUser);

        Optional<User> optionalUser = userDao.findById(friendId);
        if (optionalUser.isEmpty()) {
            log.warn("User with id : " + friendId + " does not exist");
            return;
        }

        User friend = optionalUser.get();
        List<String> friendIds1 = friend.getFriendIds();
        friendIds1.remove(authenticatedUser.getId());
        friend.setFriendIds(friendIds1);

        userDao.save(friend);

        log.info("Friend with id : " + friendId + " has been removed successfully");
    }

    public void acceptFriendRequest(KafkaMessage kafkaMessage) {
        Map<String, String> request = kafkaMessage.getRequest();
        String requestId = request.get("requestId");

        Optional<FriendRequest> optionalFriendRequest = friendRequestDao.findById(requestId);
        if (optionalFriendRequest.isEmpty()) {
            log.warn("FriendRequest with id : " + requestId + " does not exist");
            return;
        }

        FriendRequest friendRequest = optionalFriendRequest.get();
        friendRequest.setStatus(FriendRequestStatus.ACCEPTED);

        friendRequestDao.save(friendRequest);

        User authenticatedUser = kafkaMessage.getAuthenticatedUser();

        Set<String> pendingFriendRequestIds = authenticatedUser.getPendingFriendRequestIds();
        pendingFriendRequestIds.remove(requestId);
        authenticatedUser.setPendingFriendRequestIds(pendingFriendRequestIds);

        List<String> friendIds = authenticatedUser.getFriendIds();
        friendIds.add(friendRequest.getSenderId());
        authenticatedUser.setFriendIds(friendIds);

        userDao.save(authenticatedUser);

        Optional<User> optionalFriend = userDao.findById(friendRequest.getSenderId());

        if (optionalFriend.isEmpty()) {
            log.warn("User with id : " + friendRequest.getSenderId() + " does not exist");
            return;
        }

        User friend = optionalFriend.get();

        Set<String> pendingFriendRequestIds1 = friend.getPendingFriendRequestIds();
        pendingFriendRequestIds1.remove(requestId);
        friend.setPendingFriendRequestIds(pendingFriendRequestIds1);

        List<String> friendIds1 = friend.getFriendIds();
        friendIds1.add(friendRequest.getRecipientId());
        friend.setFriendIds(friendIds1);

        userDao.save(friend);

        log.info("User with id : " + authenticatedUser.getId() + " is now friend with user id : " + friend.getId());
    }

    public void rejectFriendRequest(KafkaMessage kafkaMessage) {
        Map<String, String> request = kafkaMessage.getRequest();
        String requestId = request.get("requestId");

        Optional<FriendRequest> optionalFriendRequest = friendRequestDao.findById(requestId);
        if (optionalFriendRequest.isEmpty()) {
            log.warn("FriendRequest with id : " + requestId + " does not exist");
            return;
        }

        FriendRequest friendRequest = optionalFriendRequest.get();
        friendRequest.setStatus(FriendRequestStatus.REJECTED);

        friendRequestDao.save(friendRequest);

        User authenticatedUser = kafkaMessage.getAuthenticatedUser();

        Set<String> pendingFriendRequestIds = authenticatedUser.getPendingFriendRequestIds();
        pendingFriendRequestIds.remove(requestId);
        authenticatedUser.setPendingFriendRequestIds(pendingFriendRequestIds);

        userDao.save(authenticatedUser);

        Optional<User> optionalFriend = userDao.findById(friendRequest.getSenderId());

        if (optionalFriend.isEmpty()) {
            log.warn("User with id : " + friendRequest.getSenderId() + " does not exist");
            return;
        }

        User friend = optionalFriend.get();

        Set<String> pendingFriendRequestIds1 = friend.getPendingFriendRequestIds();
        pendingFriendRequestIds1.remove(requestId);
        friend.setPendingFriendRequestIds(pendingFriendRequestIds1);

        userDao.save(friend);

        log.info("User with id : " + authenticatedUser.getId() + " has rejected the friend request with id : " + requestId);
    }

    public void cancelFriendRequest(KafkaMessage kafkaMessage) {
        Map<String, String> request = kafkaMessage.getRequest();
        String requestId = request.get("requestId");

        Optional<FriendRequest> optionalFriendRequest = friendRequestDao.findById(requestId);
        if (optionalFriendRequest.isEmpty()) {
            log.warn("FriendRequest with id : " + requestId + " does not exist");
            return;
        }

        FriendRequest friendRequest = optionalFriendRequest.get();

        friendRequestDao.delete(friendRequest);

        User authenticatedUser = kafkaMessage.getAuthenticatedUser();

        Set<String> pendingFriendRequestIds = authenticatedUser.getPendingFriendRequestIds();
        pendingFriendRequestIds.remove(requestId);
        authenticatedUser.setPendingFriendRequestIds(pendingFriendRequestIds);

        userDao.save(authenticatedUser);

        Optional<User> optionalFriend = userDao.findById(friendRequest.getSenderId());

        if (optionalFriend.isEmpty()) {
            log.warn("User with id : " + friendRequest.getSenderId() + " does not exist");
            return;
        }

        User friend = optionalFriend.get();

        Set<String> pendingFriendRequestIds1 = friend.getPendingFriendRequestIds();
        pendingFriendRequestIds1.remove(requestId);
        friend.setPendingFriendRequestIds(pendingFriendRequestIds1);

        userDao.save(friend);

        log.info("User with id : " + authenticatedUser.getId() + " has canceled the friend request with id : " + requestId);
    }

}
