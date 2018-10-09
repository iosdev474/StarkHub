package StarkHub;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Packet implements Serializable {
    public int ttl;
    public int id;
    public Video video;
    public int task;
    String data;
    Boolean like;
    public Set<Channel> cha = new HashSet<Channel>();

    /**
     * creates packet containing a single video
     * @param id
     * @param video
     */
    Packet(int id, Video video, Boolean like) {           //like a video
        this.task=2;
        this.ttl = 64;
        this.id = id;
        this.video = video;
        this.like = like;
    }

    Packet(int id,Video video,String data){
        this.task = 3;
        this.ttl = 64;
        this.id = id;
        this.video = video;
        this.data = data;
    }

    Packet(int id,Video video){                     //view
        this.task = 0;
        this.ttl = 64;
        this.id = id;
        this.video = video;
    }

    /**
     * creates packet containing set of channel
     * @param id
     * @param cha
     */
    Packet(int id, Set<Channel> cha){
        this.task=1;
        this.ttl=64;
        this.id = id;
        this.cha.addAll(cha);
    }

    /**
     * creates a packet to request a particular task
     * @param id
     * @param task
     */
    Packet(int id, int task){
        this.id=id;
        this.task=task;
    }
}
