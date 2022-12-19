package Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Client
 * Create by Đặng Ngọc Tiến
 * Date 12/19/2022 - 3:28 PM
 * Description: ...
 */
public class ClientSend {
    ClientSend(Socket s, Object message, String info, String name, String path) throws IOException {
        String messages = info + ",," + message + ",," + name + ",," + path;
        PrintWriter pwOut = new PrintWriter(s.getOutputStream(), true);
        pwOut.println(messages);
    }
}
