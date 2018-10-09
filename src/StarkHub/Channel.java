package StarkHub;

import java.io.Serializable;
import java.util.*;

public class Channel implements Serializable {
    private Long subscribers;
    private Set<Playlist> playlists = new HashSet<Playlist>();
    private Vector<String> posts = new Vector<String>();
    private String name;
    private Set<Video> videos = new HashSet<Video>();
    private String Description;
    private Double uptime;
    private long utc;

    /**
     * Creates a new channel
     * and set utc to current utc and subscribers to 0
     * @param name
     * @param Description
     */
    Channel(String name,String Description){
        this.name = name;
        this.Description = Description;
        this.uptime = 0d;
        this.utc=new Date().getTime();
        this.subscribers = 0l;
    }

    /**
     * add playlist to channel
     * @param playlist is added
     */
    void addPlaylist(Playlist playlist){
        playlists.add(playlist);
    }

    /**
     * add video to <code>videos set</code>
     * @param video is added
     */
    void addVideo(Video video) {
        videos.add(video);
    }

    /**
     *
     * @return channel's uptime
     */
    public Double getUptime() {
        return uptime;
    }

    /**
     *
     * @return channel's subscriber
     */
    public Long getSubscribers() {
        return subscribers;
    }

    /**
     *
     * @return channel's utc
     */
    public long getUtc() {
        return utc;
    }

    /**
     *
     * @return channel's description
     */
    public String getDescription() {
        return Description;
    }

    /**
     *
     * @return channel's name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return channel's playlist Set iterator
     */
    public Iterator getPlaylistsIterator() {               //todo change return to iterator
        return playlists.iterator();
    }

    /**
     *
     * @return channel's videos Set iterator
     */
    public Iterator getVideoIterator() {
        return videos.iterator();
    }

    /**
     *
     * @return channel's posts Set iterator
     */
    public Iterator getPostsIterator() {
        return posts.iterator();
    }

    /**
     * remove video from its respective channel
     * @param video is removed
     */
    void removeVideo(Video video){
        Iterator it = videos.iterator();
        while(it.hasNext()){
            Video v = (Video)it.next();
            if(v.fileName.equals(video.fileName)){
                videos.remove(v);
                break;
            }
        }
    }

    /**
     * creates a new playlist to channel
     * @param playlist is removed
     */
    void createPlaylist(Playlist playlist){}

    /**
     * adds video to given playlist
     * @param video is added
     * @param playlist to it
     */
    void addVideoToPlaylist(Video video, Playlist playlist){}

    /**
     * @return hashcode of a video
     */
    @Override
    public int hashCode(){
        int hash=0;
        String shash = SHA256Handler.getSha256(name);
        for (int i = 0; i < shash.length(); i++) {
            hash+=shash.charAt(i);
        }
        return hash;
    }

    /**
     * @param obj
     * @return <b>true</b> if object matches or else <b>false</b>
     */
    @Override
    public boolean equals(Object obj){
        if(this == obj)     //== check reference but equals check internal of objects
            return true;
        if(obj == null || obj.getClass()!= this.getClass())
            return false;
        Channel channel = (Channel) obj;
        return this.name.equals(channel.name);
    }
}
