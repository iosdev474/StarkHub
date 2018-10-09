package StarkHub;


import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ClientHandler implements Runnable {
    private String ip;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Socket socket;
    private NetworkManager netManager;
    private Packet packet;

    /**
     * evaluate the packet recieved by clientHandler
     * then act accordingly
     * ex:
     * 1. <code>packet.id = 0</code>
     *    update myself like comments,VideoSet etc
     * 2. <code>packet.id = 1</code>
     *    client has requested something based over<code>packet.task</code>
     */
    @Override
    public void run(){
        System.out.println("id:"+packet.id);
        System.out.println("task:"+packet.task);
        if(packet.id==0){
            //update yourself
            switch (packet.task){
                case 0:{
                    Iterator channelIterator = Main.getCreator().channels.iterator();
                    while(channelIterator.hasNext()){
                        Channel channel = (Channel)channelIterator.next();
                        Iterator videoIterator = channel.getVideoIterator();
                        while(videoIterator.hasNext()){
                            Video video = (Video)videoIterator.next();
                            if(video.equals(packet.video)) {
                                video.views++;
                                Main.getCreator().save();
                                Set<Channel> channels = new HashSet<Channel>();
                                channels.add(channel);
                                Packet packet = new Packet(0,channels);
                                netManager.generatePacket(packet);
                                System.out.println("NETMANAGER Video Count :) :) :)"+video.views);
                            }
                        }
                    }
                    break;
                }
                case 1:{
                    Creator creator = Main.getCreator();
                    Iterator creatorSubscribtions = creator.getSubscriptionIterator();
                    Iterator channelIterator = packet.cha.iterator();
                    while(channelIterator.hasNext()) {
                        Channel ch = (Channel)channelIterator.next();
                        while(creatorSubscribtions.hasNext()){
                            Channel subscribedChannel = (Channel) creatorSubscribtions.next();
                            if(subscribedChannel==null){
                                continue;
                            }
                            if(subscribedChannel.getName().equals(ch.getName())){
                                creator.addNotification(subscribedChannel.getName()+" channel updated!");
                            }
                        }
                        netManager.addChanneltoList(ch);
                    }
                    netManager.save();
                    netManager.generatePacket(packet);
                    System.out.println("Recieved ttl="+packet.ttl);
                    break;
                }
                case 2:{
                    Iterator channelIterator = Main.getCreator().channels.iterator();
                    while(channelIterator.hasNext()){
                        Channel channel = (Channel)channelIterator.next();
                        Iterator videoIterator = channel.getVideoIterator();
                        while(videoIterator.hasNext()){
                            Video video = (Video)videoIterator.next();
                            if(video.equals(packet.video)) {
                                if (video.utc <= packet.video.utc) {
                                    if(packet.like)
                                        video.likes++;
                                    else
                                        video.likes--;
                                    video.utc=packet.video.utc;
                                    Set<Channel> channels = new HashSet<Channel>();
                                    channels.add(channel);
                                    Packet packet = new Packet(0,channels);
                                    netManager.generatePacket(packet);
                                    System.out.println("NETMANAGER Video Liked :) :) :)"+video.likes);
                                }
                            }
                        }
                    }
                    break;
                }
                case 3:{
                    Iterator channelIterator = Main.getCreator().channels.iterator();
                    while(channelIterator.hasNext()){
                        Channel channel = (Channel)channelIterator.next();
                        Iterator videoIterator = channel.getVideoIterator();
                        System.out.println("AtChannel:"+channel.getName());
                        while(videoIterator.hasNext()){
                            Video video = (Video)videoIterator.next();
                            System.out.println("AtVideo:"+video.fileName);
                            if(video.equals(packet.video)) {
                                System.out.println("video is equal");
                                if (video.utc <= packet.video.utc) {
                                    System.out.println("utc is charm");
                                    video.addComment(packet.data);
                                    video.utc=packet.video.utc;
                                    Set<Channel> channels = new HashSet<Channel>();
                                    channels.add(channel);
                                    Packet packet = new Packet(0,channels);
                                    netManager.generatePacket(packet);
                                    System.out.println("NETMANAGER Comment :| :| :|");
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }


        //he need something
        else if(packet.id==1){
            switch (packet.task){
                case 0:{
                    break;
                }
                case 1:{
                    Packet packet = netManager.createChannelListPacket();
                    try {
                        objectOutputStream.writeObject(packet);
                        System.out.println("Send packet to client");
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Unable to write object");
                    }
                    break;
                }
            }
        }
    }

    /**
     * recieve packet from client connected
     * @param netManager
     * @param socket
     * @throws IOException
     * @throws ClassNotFoundException
     */
    ClientHandler(NetworkManager netManager, Socket socket) throws IOException, ClassNotFoundException {
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        packet = (Packet)objectInputStream.readObject();
        System.out.println("Recieved a packet in clientHandler constructor");
        this.ip = socket.getRemoteSocketAddress().toString();
        this.socket = socket;
        this.netManager = netManager;
    }
}