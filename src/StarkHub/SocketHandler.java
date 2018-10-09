package StarkHub;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketHandler implements Runnable {

    ServerSocket ss;
    NetworkManager netManager;

    SocketHandler(NetworkManager netManager){
        this.netManager = netManager;
        try {
            ss = new ServerSocket(8080);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true){
            try {
                System.out.println("Server Started Listening");
                Socket s = ss.accept();
                String ip = s.getRemoteSocketAddress().toString();
                if(netManager.addClient((ip.substring(ip.indexOf("/") + 1, ip.indexOf(":"))))){
                    System.out.println("New client added to myClientList");
                    System.out.println("Total No of Clients:"+netManager.getNumberofClients());
                }
                System.out.println("Connected with " + s.getRemoteSocketAddress().toString());
                ClientHandler clientHandler = new ClientHandler(netManager,s);
                Thread thread = new Thread(clientHandler);
                thread.start();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}