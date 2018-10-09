//package StarkHub;
//
//import java.io.*;
//import java.net.ServerSocket;
//import java.net.Socket;
//
//public class initialServer {
//    static ServerSocket ss;
//    static boolean flag = false;
//
//    public static void main(String[] args) {
//        try {
//            ss = new ServerSocket(1234);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        while (true){
//            try {
//
//
//
//                Runtime r = Runtime.getRuntime();
//                Process p = r.exec("cd src/StarkHub/ && python -m SimpleHTTPServer 8090");
//
//                InputStream in = p.getInputStream();
//                BufferedInputStream buf = new BufferedInputStream(in);
//                InputStreamReader inread = new InputStreamReader(buf);
//                BufferedReader bufferedreader = new BufferedReader(inread);
//
//                String line;
//                while ((line = bufferedreader.readLine()) != null) {
//                    System.out.println(line);
//                }
//                while(true){}
//
//
////                System.out.println("running");
////                Socket s = ss.accept();
////                ObjectOutputStream oout = new ObjectOutputStream(s.getOutputStream());
////                ObjectInputStream oin = new ObjectInputStream(s.getInputStream());
////                System.out.println("CONNECTED WITH "+s.getInetAddress());
////                DataInputStream in = new DataInputStream(s.getInputStream());
////                DataOutputStream out = new DataOutputStream(s.getOutputStream());
////                out.writeUTF("/Users/iosdev747/Desktop/Untitled.mp4");
//
//
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
package StarkHub;

import java.io.*;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class initialServer {
    private static class StreamGobbler implements Runnable {
        private InputStream inputStream;
        private Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .forEach(consumer);
        }
    }

    public static void main(String[] args){
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("bash","-c","python -m SimpleHTTPServer 8090");
        builder.directory(new File(System.getProperty("user.home")));
        Process process = null;
        try {
            process = builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        int exitCode = 0;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert exitCode == 0;
    }
}