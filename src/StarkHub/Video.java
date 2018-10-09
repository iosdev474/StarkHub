package StarkHub;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;


public class Video implements Serializable {
    public String videoPath;
    public String fileName;
    public String thumbnailPath;
    public String channel;
    public long likes;
    public long views;
    private Set<String> tags = new HashSet<String>();
    public Set<String> comments = new HashSet<>();
    public String IP;
    public String IP2;
    public String IP3;
    public long utc;
    String description;
    String langauge;
    String category;
    LocalDate dateRecorded;

    Iterator getCommentsIterator(){
        return comments.iterator();
    }

    Iterator getTagIterator(){
        return tags.iterator();
    }
    /**
     * creates a new video with following parameters
     *
     * @param channel
     * @param videoPath
     * @param fileName
     * @param thumbnailPath
     * @param tags
     */
    Video(String channel, String videoPath, String fileName, String thumbnailPath, Set<String> tags, String description, String langauge, String category, LocalDate dateRecorded, int count, String ...IP){  //todo add ip for each video and cache
        this.channel = channel;
        this.videoPath = videoPath;
        this.fileName = fileName;
        this.thumbnailPath = thumbnailPath;
        this.tags.addAll(tags);
        this.likes = 0;
        this.views = 0;
        this.utc=new Date().getTime();
        this.description=description;
        this.langauge=langauge;
        this.category=category;
        this.dateRecorded=dateRecorded;
        this.IP=IP[0];
        if(count==3) {
            this.IP = IP[1];
            this.IP = IP[2];
        }
    }

    void print(){
        System.out.println("Channel:"+channel);
        System.out.println("videoPath:"+videoPath);
        System.out.println("fileName:"+fileName);
        System.out.println("thumbnailPath:"+thumbnailPath);
        System.out.println("tags:"+tags.size());
        System.out.println("likes:"+likes);
        System.out.println("views:"+views);
        System.out.println("IP:"+IP);
        System.out.println("utc:"+utc);
        System.out.println("description:"+description);
        System.out.println("langauge:"+langauge);
        System.out.println("category:"+category);
        System.out.println("RecordedOn:"+dateRecorded);
    }

    void addComment(String comment){
        comments.add(comment);
    }

    /**
     * @return hashcode of a video
     */
    @Override
    public int hashCode(){
        int hash=0;
        String shash = SHA256Handler.getSha256(fileName+videoPath);
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
        Video tempVideo = (Video) obj;
        return this.fileName.equals(tempVideo.fileName) && this.videoPath.equals(tempVideo.videoPath);
    }

}
