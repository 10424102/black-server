package org.projw.blackserver.config.json;

public class Views {
    public interface UserSummary{}
    public interface UserDetails extends UserSummary{}
    public interface ActivitySummary{}
    public interface ActivityDetails extends ActivitySummary, UserSummary{}
    public interface Post extends PostComment, UserSummary{}
    public interface Group{}
    public interface Game{}
    public interface Notification extends FriendshipApplication{}
    public interface FriendshipApplication extends UserSummary{}
    public interface PostComment extends UserSummary{}
}
