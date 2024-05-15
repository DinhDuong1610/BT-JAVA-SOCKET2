package B1;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(12345);
        while (true) {
            Socket socket = serverSocket.accept();
            DataOutputStream toClient = new DataOutputStream(socket.getOutputStream());
            TimeOutputStream timeOutputStream = new TimeOutputStream(toClient,1000);
            timeOutputStream.start();
        }
    }
}