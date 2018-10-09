package StarkHub;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class NetworkManager implements Runnable{

    private String myIp;
    private Thread socketThread;
    private SocketHandler socketHandler;
    private Set<Channel> cha = new HashSet<Channel>();
    private Set<String> myClients = new HashSet<String>();
    private String serverIP;

    /**
     *
     * Handles all networking stuff
     *
     * It works as follows:
     * 1. First connects to a server using serverIP to join with whole network
     * 2. Then request for Updates on network from server
     * 3. Starts itself as server and listen for clients
     *      SocketHandler Manages it
     * 4. If client connects then ClientHandler handles all client queries.
     *
     * @param serverIP
     */
    NetworkManager(String serverIP){
        InetAddress ipAddr = null;
        try {
            ipAddr = InetAddress.getLocalHost();
            myIp=ipAddr.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            myIp="127.0.0.1";
        }
        System.out.println("IP:"+myIp);
        Thread t = new Thread(this);
        t.start();
        this.serverIP = serverIP;
    }

    /**
     * @return netManager's IP<i>your ip</i>
     */
    public String getMyIP(){ return myIp; }

    /**
     * @return Netmanager.channel iterator
     */
    public Iterator getChannelListIterator(){ return cha.iterator(); }

    public Packet createChannelListPacket(){
        Packet packet = new Packet(-1,cha);
        return packet;
    }

    public boolean addClient(String ip){
        return myClients.add(ip);
    }

    public int getNumberofClients(){
        return myClients.size();
    }

    public void save(){
        SaveAndLoadController.save("channel.ser",cha);
    }

    public void addChanneltoList(Channel channel){
        cha.remove(channel);
        cha.add(channel);
    }

    public void removeChannelfromList(Channel channel){
        //todo function defination
        cha.remove(channel);
    }

    public boolean isChannelNull(){
        return cha == null;
    }
    @Override
    public void run() {
        this.cha = (Set<Channel>) SaveAndLoadController.load("channel.ser");
        System.out.println("loaded channel list which is "+(this.cha==null?"Null":"Good"));

        try {
            Socket s = new Socket(serverIP,8080);
            String ip = s.getRemoteSocketAddress().toString();
            if( ip.substring(ip.indexOf("/") + 1, ip.indexOf(":")).equals("0.0.0.1") ){
            }
            else if(this.myClients.add(ip.substring(ip.indexOf("/") + 1, ip.indexOf(":")))){
                System.out.println("New client added to myClientList");
            }
            System.out.println("Total No of Clients:"+myClients.size());

            if (this.cha == null) {                                     //recieve Channel List from Server
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());
                Packet packet = new Packet(1,1);
                objectOutputStream.writeObject(packet);
                packet=(Packet)objectInputStream.readObject();
                this.cha = new HashSet<Channel>();
                this.cha.addAll(packet.cha);
                System.out.println("Got list from server");
                SaveAndLoadController.save("channel.ser",this.cha);
                System.out.println("Saved new channel list");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        if (this.cha == null || this.cha.size() == 0) {                     //server also haven't any Channel List
            this.cha = new HashSet<Channel>();
            System.out.println("Recieved Nothing");
        }

        this.socketHandler = new SocketHandler(this);
        this.socketThread = new Thread(this.socketHandler);
        this.socketThread.start();
    }

    /**
     *
     * Iterate over Client List and send the same packet
     * recieved by current object(of Netmanager) to all clients
     *
     * @param packet
     */
    void generatePacket(Packet packet) {
        if(packet.ttl==0){
            System.out.println("Packet died ;-(");                          //hops reached to 0 : Drop packet
            return;
        }
        packet.ttl--;
        System.out.println("Generate Packet");
        System.out.println("Total No of Clients:"+myClients.size());
        Iterator it = myClients.iterator();                                 //iterate over Client List
        while(it.hasNext()) {
            try {
                String str = (String) it.next();
                Socket s = new Socket(str,8080);

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());

                objectOutputStream.writeObject(packet);

                System.out.println("Send packet to " + str);
                s.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}