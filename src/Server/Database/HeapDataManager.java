package Server.Database;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.BiFunction;

import Model.DFile;
import Model.Group;
import Model.Message;
import Model.Recipient;
import Model.UserPair;
import Rules.Relationship;

public class HeapDataManager {
    private HeapDataManager() {}
    
    public static void addGroupMember(long groupID, long userID) {
        final var user = Data.users.get(userID);
        Data.groups.compute(groupID, (Long id, Group group) -> {
            return group.addMember(user);
        });
        Data.groupsOf.compute(userID, (uid, groupsList) -> {
            if (groupsList == null) groupsList = new HashSet<Long>();
            groupsList.add(groupID);
            return groupsList;
        });
    }

    public static void addRelationship(UserPair pair) {
        long userA = pair.getUserA();
        long userB = pair.getUserB();
        Data.relationships.put(pair, pair);
        var addFriend = new BiFunction<Long, HashSet<Long>, HashSet<Long>>() {
            @Override
            public HashSet<Long> apply(Long userID, HashSet<Long> friendsList) {
                if (friendsList == null) friendsList = new HashSet<Long>();
                long friendID = userA ^ userB ^ userID;
                if (Relationship.FRIEND.equals(pair.getStatus()))
                    friendsList.add(friendID);
                else
                    friendsList.remove(friendID);
                return friendsList;
            }
        };
        Data.friendsOf.compute(userA, addFriend);
        Data.friendsOf.compute(userB, addFriend);
    }

    public static void addFile(DFile file, String path) {
        Data.files.put(file.getFileID(), file);
        
        String parent = file.getParentID(); 
        //major bug found in old file insertion code
        //but since I didn't use fileTree anywhere other than database loader, it was never noticed
        if (parent != null) {
            if (!Data.fileTree.containsKey(parent))
                Data.fileTree.put(parent, new ArrayList<String>());
            else
                Data.fileTree.get(parent).add(file.getFileID());
        }

        Data.filesOf.compute(file.getUploaderID(), (uid, filesList) -> {
            if (filesList == null) filesList = new HashSet<String>();
            filesList.add(file.getFileID());
            return filesList;
        });
        if (path != null) {
            Data.idToPath.put(file.getFileID(), path);
            Data.pathToID.put(path, file.getFileID());
        }
    }

    public static void addMessage(Message message, boolean addRecipient) {
        Data.messages.put(message.getMessageID(), message);
        long receiverID = message.getReceiverID();
        if (addRecipient) {
            Data.recipients.compute(receiverID, (Long id, Recipient recipient) -> {
                return recipient.addMessage(message);
            });
        }
    }
}
