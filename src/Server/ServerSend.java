package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Server
 * Create by Đặng Ngọc Tiến
 * Date 12/19/2022 - 3:12 PM
 * Description: ...
 */
public class ServerSend {
    ServerSend(ArrayList<Socket> listClient, Object message, String info, String name) throws IOException {
        String messages = info + "." + message + "." + name;
        PrintWriter pwOut = null;
        for (Socket s : listClient) { // gửi tin nhắn cho từng client cần thiết
            pwOut = new PrintWriter(s.getOutputStream(), true);
            pwOut.println(messages);
        }
    }

    ServerSend(Socket s, Object message, String info, String name) throws IOException {
        String messages = info + "." + message + "." + name;
        PrintWriter pwOut = new PrintWriter(s.getOutputStream(), true);
        pwOut.println(messages);
    }
}
