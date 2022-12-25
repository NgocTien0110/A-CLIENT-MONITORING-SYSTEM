package Server;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
                String info = strs[0]; // numbet
                String line = strs[1]; // name client
                String name = strs[2]; // message
                String path = strs[3]; // path

                if (info.equals("1")) {
                    sendAllClient(listClient, line, "1", "");
                } else if (info.equals("2")) { // 2 para login
                    if (!nameClient.contains(line)) {
                        nameClient.add(line);
                        // thêm client vào map
                        DashboardServer.mapSocket.put(line, socket);
                        DashboardServer.mapPath.put(line, path);
                        DashboardServer.jListClients.setListData(nameClient);
                        sendAllClient(listClient, name, "2", line);
                        // get time now
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();

                        Object[] obj = new Object[] { DashboardServer.tableModel.getRowCount() + 1, path,
                                dateFormat.format(date), "Connected",
                                line, name };

                        String data = "{" + (DashboardServer.tableModel.getRowCount() + 1) + ","
                                + path + "," +
                                dateFormat.format(date).toString() + "," + "Connected" + "," +
                                line + "," + name + "}";
                        // ghi vào file logs
                        WriteLogs wr = new WriteLogs();
                        wr.writeFile(String.valueOf(data), DashboardServer.path);

                        // hiển thị trên bảng Dashboard
                        DashboardServer.tableModel.addRow(obj);
                        DashboardServer.jtableClients.setModel(DashboardServer.tableModel);
                    } else {
                        listClient.remove(socket);
                        sendAClient(socket, "", "4", "server");
                    }
                } else if (info.equals("3")) { // disconnect
                    // get time now
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                    Object[] obj = new Object[] { DashboardServer.tableModel.getRowCount() + 1, path,
                            dateFormat.format(date), "Disconnected",
                            line, name };

                    String data = "{" + (DashboardServer.tableModel.getRowCount() + 1) + ","
                            + path + "," +
                            dateFormat.format(date).toString() + "," + "Disconnected" + "," +
                            line + "," + name + "}";
                    // ghi vào file logs
                    WriteLogs wr = new WriteLogs();
                    wr.writeFile(String.valueOf(data), DashboardServer.path);

                    // hiển thị trên bảng Dashboard
                    DashboardServer.tableModel.addRow(obj);
                    DashboardServer.jtableClients.setModel(DashboardServer.tableModel);

                    // xóa client khỏi list
                    nameClient.remove(line);
                    listClient.remove(socket);

                    // hiển thị lại danh sách client
                    DashboardServer.mapSocket.remove(line);
                    DashboardServer.mapPath.remove(line);
                    DashboardServer.jListClients.setListData(nameClient);
                    sendAllClient(listClient, nameClient, "3", line);
                    socket.close();
                    break; // quebra de info
                } else if (info.equals("10")) { // created
                    // get time now
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                    Object[] obj = new Object[] { DashboardServer.tableModel.getRowCount() + 1, path,
                            dateFormat.format(date), "Created", line, name };

                    String data = "{" + (DashboardServer.tableModel.getRowCount() + 1) + ","
                            + path + "," + dateFormat.format(date).toString() + "," + "Created" + "," +
                            line + "," + name + "}";

                    WriteLogs wr = new WriteLogs();
                    wr.writeFile(String.valueOf(data), DashboardServer.path);
                    DashboardServer.tableModel.addRow(obj);
                    DashboardServer.jtableClients.setModel(DashboardServer.tableModel);

                } else if (info.equals("11")) { // deleted
                    // get time now
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                    Object[] obj = new Object[] { DashboardServer.tableModel.getRowCount() + 1, path,
                            dateFormat.format(date), "Deleted", line, name };

                    String data = "{" + (DashboardServer.tableModel.getRowCount() + 1) + ","
                            + path + "," + dateFormat.format(date).toString() + "," +
                            "Deleted" + "," + line + "," + name + "}";

                    // ghi vào file logs
                    WriteLogs wr = new WriteLogs();
                    wr.writeFile(String.valueOf(data), DashboardServer.path);

                    // hiển thị trên bảng Dashboard
                    DashboardServer.tableModel.addRow(obj);
                    DashboardServer.jtableClients.setModel(DashboardServer.tableModel);

                } else if (info.equals("12")) { // modified
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                    Object[] obj = new Object[] { DashboardServer.tableModel.getRowCount() + 1, path,
                            dateFormat.format(date), "Modified", line, name };

                    String data = "{" + (DashboardServer.tableModel.getRowCount() + 1) + ","
                            + path + "," + dateFormat.format(date).toString() + "," + "Modified" + "," +
                            line + "," + name + "}";

                    // ghi vào file logs
                    WriteLogs wr = new WriteLogs();
                    wr.writeFile(String.valueOf(data), DashboardServer.path);

                    // hiển thị trên bảng Dashboard
                    DashboardServer.tableModel.addRow(obj);
                    DashboardServer.jtableClients.setModel(DashboardServer.tableModel);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendAClient(Socket s, Object message, String info, String name) throws IOException {
        String messages = info + "." + message + "." + name;
        PrintWriter pwOut = new PrintWriter(s.getOutputStream(), true);
        pwOut.println(messages);
    }

    public static void sendAllClient(ArrayList<Socket> listClient, Object message, String info, String name) throws IOException {
        String messages = info + "." + message + "." + name;
        PrintWriter pwOut = null;
        for (Socket s : listClient) { // gửi tin nhắn cho từng client cần thiết
            pwOut = new PrintWriter(s.getOutputStream(), true);
            pwOut.println(messages);
        }
    }
}
