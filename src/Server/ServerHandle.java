package Server;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private int porta;
    public static ArrayList<Socket> listaClient = null;
    public static Vector<String> nameClient = null; // thread security
    public static Map<String, Socket> map = null;
    public static ServerSocket ss = null;
    public static boolean flag = true;

    public ServerHandle(int porta) throws IOException {
        this.porta = porta;
    }

    public void run() {
        Socket s = null;
        listaClient = new ArrayList<Socket>();
        nameClient = new Vector<String>();
        map = new HashMap<String, Socket>(); // name to socket one on one map

        System.out.println("Máy chủ đã khởi động!");
        try {
            ss = new ServerSocket(porta);
            System.out.println(ss.getLocalSocketAddress());
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        while (flag) {
            try {
                s = ss.accept();
                listaClient.add(s);
                new Thread(new ServerReceive(s, listaClient, nameClient, map)).start();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Đóng máy chủ！");
            }
        }
    }

}
