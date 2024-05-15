package B2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    private static ArrayList<ClientHandler> Handler_Clients = new ArrayList<>();

    private Socket Handler_Socket;
    private DataInputStream Handler_Datain;
    private DataOutputStream Handler_Dataout;
    private BufferedReader Handler_Reader;
    private BufferedWriter Handler_Writer;

    private String Handler_ClientName;

    public ClientHandler(Socket clientValue1) {
        try {
            // setup client socket
            Handler_Socket = clientValue1;
            Handler_Clients.add(this); 	// can we just add(socket) only? -- no due to we have to take whole Handler object later!

            // setup input
            Handler_Datain = new DataInputStream(clientValue1.getInputStream());
            Handler_Reader = new BufferedReader(new InputStreamReader(Handler_Datain));

            // setup output
            Handler_Dataout = new DataOutputStream(clientValue1.getOutputStream());
            Handler_Writer = new BufferedWriter(new OutputStreamWriter(Handler_Dataout));

            // setup Handler_ClientName
            Handler_ClientName = Handler_Reader.readLine();
            System.out.println(Handler_ClientName + " has joined the chat!");
            broadcastMessage(Handler_ClientName + " has joined!");

        } catch (IOException e) {
            closeEverything(Handler_Socket, Handler_Datain, Handler_Dataout, Handler_Reader, Handler_Writer);
        }
    }


    @Override
    public void run() {
        String msgFromClient;
        try {
            while (Handler_Socket.isConnected()) {
                msgFromClient = Handler_Reader.readLine();
                broadcastMessage(msgFromClient);
            }
        } catch (IOException e) {
            closeEverything(Handler_Socket, Handler_Datain, Handler_Dataout, Handler_Reader, Handler_Writer);
        }
    }


    public void broadcastMessage(String msgToBroad) {
        // we use try inside the for loop due to if 1 client isn't connected, we can still broadcasr message(s) to other clients afterward
        for (ClientHandler i: Handler_Clients) {
            try {
                if (!i.Handler_ClientName.equals(Handler_ClientName)) {
                    i.Handler_Writer.write(msgToBroad);
                    i.Handler_Writer.newLine();
                    i.Handler_Writer.flush();
                }
            } catch (IOException e) {
                closeEverything(Handler_Socket, Handler_Datain, Handler_Dataout, Handler_Reader, Handler_Writer);
            }
        }
    }


    public void removeHandler() {
        Handler_Clients.remove(this);
        broadcastMessage(Handler_ClientName + " has left the chat!");
    }


    public void closeEverything(Socket sk1, DataInputStream datain, DataOutputStream dataout, BufferedReader bfread, BufferedWriter bfwrite) {
        removeHandler();


        try {
            if (sk1 != null) {
                sk1.close();
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

            System.out.println(Handler_ClientName + " has left the chat!, Currently online: " + Handler_Clients.size());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}