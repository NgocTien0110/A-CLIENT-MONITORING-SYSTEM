package Server;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
public class ServerReceive implements Runnable {
    private Socket socket;
    private ArrayList<Socket> listClient;
    private Vector<String> nameClient;
    private Map<String, Socket> map;

    public ServerReceive(Socket s, ArrayList<Socket> listClient, Vector<String> nameClient,
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
                    new ServerSend(listClient, line, "1", "");
                } else if (info.equals("2")) { // 2 para login
                    if (!nameClient.contains(line)) {
                        nameClient.add(line);
                        DashboardServer.mapSocket.put(line, socket);
                        DashboardServer.mapPath.put(line, path);
                        DashboardServer.jListClients.setListData(nameClient);
                        new ServerSend(listClient, name, "2", line);
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();

                        Object[] obj = new Object[] { DashboardServer.tableModel.getRowCount() + 1, path,
                                dateFormat.format(date), "Connected",
                                line, name };

                        String data = "{" + (DashboardServer.tableModel.getRowCount() + 1) + ","
                                + path + "," +
                                dateFormat.format(date).toString() + "," + "Connected" + "," +
                                line + "," + name + "}";

                        WriteFile wr = new WriteFile();
                        wr.writeFile(String.valueOf(data), DashboardServer.path);
                        DashboardServer.tableModel.addRow(obj);
                        DashboardServer.jtableClients.setModel(DashboardServer.tableModel);
                    } else {
                        listClient.remove(socket);
                        new ServerSend(socket, "", "4", "server");
                    }
                } else if (info.equals("3")) {

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                    Object[] obj = new Object[] { DashboardServer.tableModel.getRowCount() + 1, path,
                            dateFormat.format(date), "Disconnected",
                            line, name };

                    String data = "{" + (DashboardServer.tableModel.getRowCount() + 1) + ","
                            + path + "," +
                            dateFormat.format(date).toString() + "," + "Disconnected" + "," +
                            line + "," + name + "}";

                    WriteFile wr = new WriteFile();
                    wr.writeFile(String.valueOf(data), DashboardServer.path);
                    DashboardServer.tableModel.addRow(obj);
                    DashboardServer.jtableClients.setModel(DashboardServer.tableModel);

                    nameClient.remove(line);
                    listClient.remove(socket);
                    DashboardServer.mapSocket.remove(line);
                    DashboardServer.mapPath.remove(line);
                    DashboardServer.jListClients.setListData(nameClient);
                    new ServerSend(listClient, nameClient, "3", line);
                    socket.close();
                    break; // quebra de info
                } else if (info.equals("10")) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                    Object[] obj = new Object[] { DashboardServer.tableModel.getRowCount() + 1, path,
                            dateFormat.format(date), "Created", line, name };

                    String data = "{" + (DashboardServer.tableModel.getRowCount() + 1) + ","
                            + path + "," + dateFormat.format(date).toString() + "," + "Created" + "," +
                            line + "," + name + "}";

                    WriteFile wr = new WriteFile();
                    wr.writeFile(String.valueOf(data), DashboardServer.path);
                    DashboardServer.tableModel.addRow(obj);
                    DashboardServer.jtableClients.setModel(DashboardServer.tableModel);

                } else if (info.equals("11")) {

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                    Object[] obj = new Object[] { DashboardServer.tableModel.getRowCount() + 1, path,
                            dateFormat.format(date), "Deleted", line, name };

                    String data = "{" + (DashboardServer.tableModel.getRowCount() + 1) + ","
                            + path + "," + dateFormat.format(date).toString() + "," +
                            "Deleted" + "," + line + "," + name + "}";

                    WriteFile wr = new WriteFile();
                    wr.writeFile(String.valueOf(data), DashboardServer.path);
                    DashboardServer.tableModel.addRow(obj);
                    DashboardServer.jtableClients.setModel(DashboardServer.tableModel);

                } else if (info.equals("12")) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                    Object[] obj = new Object[] { DashboardServer.tableModel.getRowCount() + 1, path,
                            dateFormat.format(date), "Modified", line, name };

                    String data = "{" + (DashboardServer.tableModel.getRowCount() + 1) + ","
                            + path + "," + dateFormat.format(date).toString() + "," + "Modified" + "," +
                            line + "," + name + "}";

                    WriteFile wr = new WriteFile();
                    wr.writeFile(String.valueOf(data), DashboardServer.path);
                    DashboardServer.tableModel.addRow(obj);
                    DashboardServer.jtableClients.setModel(DashboardServer.tableModel);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
