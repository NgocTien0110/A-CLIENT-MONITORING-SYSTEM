package Server;



import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Vector;

/**
 * Server
 * Create by Đặng Ngọc Tiến
 * Date 12/19/2022 - 2:59 PM
 * Description: ...
 */
public class ServerData implements Runnable {
    private Socket socket;
    private ArrayList<Socket> listClient;
    private Vector<String> nameClient;
    private Map<String, Socket> map;

    public ServerData(Socket s, ArrayList<Socket> listClient, Vector<String> nameClient,
                      Map<String, Socket> map) {
        this.socket = s;
        this.listClient = listClient;
        this.nameClient = nameClient;
        this.map = map;
    }

    public void run() {
        try {
            BufferedReader brIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String s = brIn.readLine();
                String[] strs = s.split(",,");
                String infoMode = strs[0]; // mode
                String name = strs[1]; // name client
                String message = strs[2]; // message
                String path = strs[3]; // path
                // get time now
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();

               if (infoMode.equals("1")) { // 1 connect
                    if (!nameClient.contains(name)) {
                        // add name client
                        nameClient.add(name);

                        // hiển thị lại danh sách client
                        DashboardServer.mapSocket.put(name, socket);
                        DashboardServer.mapPath.put(name, path);
                        DashboardServer.jListClients.setListData(nameClient);
                        DashboardServer.addRowToJTable(path, "Connected", name, message);

                        // ghi vào file logs
                        String data = "{" + (DashboardServer.tableModel.getRowCount() + 1) + "," + path + "," + dateFormat.format(date).toString() + "," + "Connected" + "," + name + "," + message + "}";
                        WriteLogs wr = new WriteLogs();
                        wr.writeFile(String.valueOf(data), DashboardServer.path);

                        sendAClient(socket, message, "1", name);
                    } else {
                        listClient.remove(socket);
                        sendAClient(socket, "", "4", "server");
                    }
                } else if (infoMode.equals("2")) { // disconnect
                    // xóa client khỏi list
                    nameClient.remove(name);
                    listClient.remove(socket);

                    // hiển thị lại danh sách client
                    DashboardServer.mapSocket.remove(name);
                    DashboardServer.mapPath.remove(name);
                    DashboardServer.jListClients.setListData(nameClient);

                    // thêm vào table
                   DashboardServer.addRowToJTable(path, "Disconnected", name, message);

                    // ghi vào file logs
                    String data = "{" + (DashboardServer.tableModel.getRowCount() + 1) + "," + path + "," + dateFormat.format(date).toString() + "," + "Disconnected" + "," + name + "," + message + "}";
                    WriteLogs wr = new WriteLogs();
                    wr.writeFile(String.valueOf(data), DashboardServer.path);

                    sendAClient(socket, nameClient, "2", name);
                    socket.close(); // close socket
                    break;
                } else if (infoMode.equals("10")) { // created
                   DashboardServer.addRowToJTable(path, "Created", name, message);

                    // ghi vào file logs
                    String data = "{" + (DashboardServer.tableModel.getRowCount() + 1) + "," + path + "," + dateFormat.format(date).toString() + "," + "Created" + "," + name + "," + message + "}";
                    WriteLogs wr = new WriteLogs();
                    wr.writeFile(String.valueOf(data), DashboardServer.path);
                } else if (infoMode.equals("11")) { // deleted
                   DashboardServer.addRowToJTable(path, "Deleted", name, message);

                    // ghi vào file logs
                    String data = "{" + (DashboardServer.tableModel.getRowCount() + 1) + "," + path + "," + dateFormat.format(date).toString() + "," + "Deleted" + "," + name + "," + message + "}";
                    WriteLogs wr = new WriteLogs();
                    wr.writeFile(String.valueOf(data), DashboardServer.path);
                } else if (infoMode.equals("12")) { // modified
                   DashboardServer.addRowToJTable(path, "Modified", name, message);

                    // ghi vào file logs
                    String data = "{" + (DashboardServer.tableModel.getRowCount() + 1) + "," + path + "," + dateFormat.format(date).toString() + "," + "Modified" + "," + name + "," + message + "}";
                    WriteLogs wr = new WriteLogs();
                    wr.writeFile(String.valueOf(data), DashboardServer.path);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // gửi tin nhắn đến tất cả client
    public static void sendAClient(Socket s, Object message, String info, String name) throws IOException {
        String messages = info + "." + message + "." + name;
        PrintWriter pwOut = new PrintWriter(s.getOutputStream(), true);
        pwOut.println(messages);
    }

    // gửi tin nhắn đến cho 1 client
    public static void sendAllClient(ArrayList<Socket> listClient, Object message, String info, String name) throws IOException {
        String messages = info + "." + message + "." + name;
        PrintWriter pwOut = null;
        for (Socket s : listClient) { // gửi tin nhắn cho từng client cần thiết
            pwOut = new PrintWriter(s.getOutputStream(), true);
            pwOut.println(messages);
        }
    }

}
