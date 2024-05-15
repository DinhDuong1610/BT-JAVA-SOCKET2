package B1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 12345);
        BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        while (true) {
            String message = fromServer.readLine();
            System.out.println(message);
        }
    }
}