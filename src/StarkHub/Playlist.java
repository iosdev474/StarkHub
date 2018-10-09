package StarkHub;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Playlist implements Serializable {
    Set<Video> videos = new HashSet<Video>();
    private String name;

    Playlist(String name){
        this.name = name;
    }

    String getName(){
        return name;
    }

    void addVideo(Video video) {
        videos.add(video);
    }

    void removeVideo(Video video){
        videos.remove(video);
    }

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
        if(this == obj)     // == check reference but equals check internal of objects
            return true;
        if(obj == null || obj.getClass()!= this.getClass())
            return false;
        Playlist playlist = (Playlist) obj;
        return this.name.equals(playlist.name);
    }
}
