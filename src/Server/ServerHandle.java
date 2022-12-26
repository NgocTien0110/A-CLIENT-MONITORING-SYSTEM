package Server;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Server
 * Create by Đặng Ngọc Tiến
 * Date 12/19/2022 - 11:09 AM
 * Description: ...
 */
public class ServerHandle implements Runnable {
    private int portServer;
    public static ArrayList<Socket> listaClient = null;
    public static Vector<String> nameClient = null;
    public static Map<String, Socket> map = null;
    public static ServerSocket ss = null;
    public static boolean flag = true;

    public ServerHandle(int portSv) throws IOException {
        this.portServer = portSv;
    }

    public void run() {
        Socket s = null;
        listaClient = new ArrayList<Socket>();  // danh sách client
        nameClient = new Vector<String>();  // danh sách tên client
        map = new HashMap<String, Socket>(); // name to socket one on one map

        System.out.println("Server is running...");
        try {
            ss = new ServerSocket(portServer);
            System.out.println(ss.getLocalSocketAddress());
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        while (flag) {
            try {
                s = ss.accept();
                new Thread(new ServerData(s, listaClient, nameClient, map)).start();
                listaClient.add(s);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Close server");
            }
        }
    }

}
