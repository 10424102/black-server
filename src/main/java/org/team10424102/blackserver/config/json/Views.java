package org.team10424102.blackserver.config.json;

public class Views {
    public interface UserSummary{}
    public interface UserDetails extends UserSummary{}
    public interface ActivitySummary{}
    public interface ActivityDetails extends ActivitySummary, UserSummary{}
    public interface Post extends UserSummary{}
    public interface Group{}
    public interface Game{}
}
