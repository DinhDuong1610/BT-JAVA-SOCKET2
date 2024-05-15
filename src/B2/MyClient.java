package B2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyClient {

    private Socket Client_Socket;
    private DataInputStream Client_Datain;
    private DataOutputStream Client_Dataout;
    private BufferedReader Client_Reader;
    private BufferedWriter Client_Writer;

    private String Client_Name;

    public MyClient(Socket sk1, String str1) {
        try {
            // setup client
            Client_Socket = sk1;
            Client_Name = str1;

            // setup input
            Client_Datain = new DataInputStream(sk1.getInputStream());
            Client_Reader = new BufferedReader(new InputStreamReader(Client_Datain));

            // setup output
            Client_Dataout = new DataOutputStream(sk1.getOutputStream());
            Client_Writer = new BufferedWriter(new OutputStreamWriter(Client_Dataout));
        } catch (IOException e) {
            closeEverything(Client_Socket, Client_Datain, Client_Dataout, Client_Reader, Client_Writer);
        }
    }


    public void sendMessage() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // we have this 3 lines because username!
                    Client_Writer.write(Client_Name);
                    Client_Writer.newLine();
                    Client_Writer.flush();

                    Scanner scanner = new Scanner(System.in);
                    String msgToSend;
                    while (Client_Socket.isConnected()) {
                        msgToSend = scanner.nextLine();
                        Client_Writer.write("[" + Client_Name + "]: " + msgToSend);
                        Client_Writer.newLine();
                        Client_Writer.flush();

                    }

                    scanner.close();
                } catch (IOException e) {
                    System.out.println("sendMessage method break, check your server connection!");
                    closeEverything(Client_Socket, Client_Datain, Client_Dataout, Client_Reader, Client_Writer);
                }
            }
        });

        thread.start();
    }


    public void receiveMessage() {
        String msgToReceive;

        try {
            while (Client_Socket.isConnected()) {
                msgToReceive = Client_Reader.readLine();
                System.out.println(msgToReceive);
            }
        } catch (IOException e) {
            System.out.println("reveiveMessage method break, check your server connection!");
            closeEverything(Client_Socket, Client_Datain, Client_Dataout, Client_Reader, Client_Writer);
        }
    }


    public void closeEverything(Socket sk2, DataInputStream datain, DataOutputStream dataout, BufferedReader bfread, BufferedWriter bfwrite) {
        try {
            if (sk2 != null) {
                sk2.close();
            }
            if (datain != null) {
                datain.close();
            }
            if (dataout != null) {
                dataout.close();
            }
            if (bfread != null) {
                bfread.close();
            }
            if (bfwrite != null) {
                bfwrite.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) throws IOException {

        // scanner first otherwise it will priority each client!
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String username = scanner.nextLine();

        Socket socket = new Socket("localhost", 1234);
        MyClient client = new MyClient(socket, username);
        client.sendMessage();
        client.receiveMessage();

        scanner.close();
    }
}