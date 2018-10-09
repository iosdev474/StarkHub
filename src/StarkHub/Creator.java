package StarkHub;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

public class Creator implements Serializable {
    public static final String filename = "creator.ser";
    String name;
    Set<Channel> channels = new HashSet<Channel>();
    Set<Playlist> playlists = new HashSet<Playlist>();
    private Set<Channel> subscriptions = new HashSet<Channel>();
    private Set<String> notifications = new HashSet<String>();
    Vector<String> history = new Vector<String>();
    private int newNotifications;
    Boolean isPremium=false;
    /**
     * creates a new creator and saves it to creator.ser
     * also add two default playlists <b>Watch Later</b> and <b>Liked Videos</b>
     * @param name
     */
    Creator(String name) {
        Playlist watchLater = new Playlist("Watch Later");
        Playlist liked_videos = new Playlist("Liked Videos");
        Playlist history_videos = new Playlist("History");
        playlists.add(watchLater);
        playlists.add(liked_videos);
        playlists.add(history_videos);
        this.name = name;
        newNotifications=0;
        addNotification("Welcome to StarkHub");
        SaveAndLoadController.save(Creator.filename,this);
        System.out.println("saving creator");

    }

    /**
     * getter methods
     * return respective iterators
     * @return Iterator
     */
    Iterator getChannelListIterator(){
        return channels.iterator();
    }

    Iterator getPlaylistIterator(){
        return playlists.iterator();
    }

    Iterator getNotificationIterator(){
        return notifications.iterator();
    }

    Iterator getSubscriptionIterator(){
        return subscriptions.iterator();
    }

    /**
     * subscribe channel
     * @param channel is added to subscriptions
     */
    void subscribe(Channel channel){
        subscriptions.add(channel);
        save();
    }

    /**
     * unsubscribe channel
     * @param channel is removed to subscriptions
     */
    void unsubscribe(Channel channel){
        subscriptions.remove(channel);
        save();
    }

    void addPlaylist(Playlist playlist){
        playlists.add(playlist);
        save();
    }

    void addNotification(String notif) {
        if(notifications.add(notif))
            newNotifications++;
        save();
    }

    void addHistory(String string){
        history.add(string);
        save();
    }

    void clearHistory(){
        history.clear();
        save();
    }

    int getNewNotifications(){
        return newNotifications;
    }

    void resetNewNotifications(){
        newNotifications=0;
        save();
    }

    void clearNotification(){
        notifications.clear();
        newNotifications=0;
        save();
    }
    /**
     * add channel to creator
     * @param channel
     * @return
     */
    Channel addChannel(Channel channel) {
        channels.add(channel);
        save();
        return channel;
    }

    /**
     * add video to its respective channel
     * @param video
     * @return
     */
    Channel addVideo(Video video){
        for (Channel channel:channels) {
            if(channel.getName().equals(video.channel)){
                channel.addVideo(video);
                save();
                return channel;
            }
        }
        return null;
    }

    /**
     * remove video from its respective channel
     * @param video
     */
    void removeVideo(Video video){
        for (Channel ch:channels) {
            if(ch.getName().equals(video.channel)){
                ch.removeVideo(video);
                save();
                break;
            }
        }
    }

    /**
     * saves the creator to creator.ser
     */
    public void save(){ SaveAndLoadController.save(Creator.filename,this); }
}
