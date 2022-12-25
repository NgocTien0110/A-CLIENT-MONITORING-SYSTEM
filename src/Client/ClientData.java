package Client;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Client
 * Create by Đặng Ngọc Tiến
 * Date 12/19/2022 - 3:32 PM
 * Description: ...
 */
public class ClientData implements Runnable {
    private Socket socket;

    public ClientData(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader brIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Thread.sleep(500);
            while (brIn.readLine() != null) {
                String s = brIn.readLine();
                String[] strs = s.split("\\.");
                String info = strs[0];
                String name = strs[2], line = strs[1];
                if (info.equals("1")) {
                } else if (info.equals("2") || info.equals("3")) {
                    if (info.equals("2")) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();

                        Object[] obj = new Object[] { DashboardClient.tableModel.getRowCount() + 1,
                                DashboardClient.path,
                                dateFormat.format(date), "Connected",
                                DashboardClient.nameClient,
                                "(Notification) " + DashboardClient.nameClient + " connected to server!" };

                        String data = "{" + (DashboardClient.tableModel.getRowCount() + 1) + ","
                                + DashboardClient.path + "," +
                                dateFormat.format(date).toString() + "," + "Connected" + "," +
                                DashboardClient.nameClient + "," +
                                "(Notification) " + DashboardClient.nameClient + " connected to server!" + "}";

                        DashboardClient.tableModel.addRow(obj);
                        DashboardClient.jtableClients.setModel(DashboardClient.tableModel);

                    } else {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();

                        Object[] obj = new Object[] { DashboardClient.tableModel.getRowCount() + 1,
                                DashboardClient.path,
                                dateFormat.format(date), "Disconnected",
                                DashboardClient.nameClient,
                                "(Notification)" + DashboardClient.nameClient + " disconnected to server!" };

                        String data = "{" + (DashboardClient.tableModel.getRowCount() + 1) + ","
                                + DashboardClient.path + "," +
                                dateFormat.format(date).toString() + "," + "Disconnected" + "," +
                                DashboardClient.nameClient + "," +
                                "(Notification) " + DashboardClient.nameClient + " disconnected to server!" + "}";

                        DashboardClient.tableModel.addRow(obj);
                        DashboardClient.jtableClients.setModel(DashboardClient.tableModel);

                    }
                } else if (info.equals("4")) {
                    DashboardClient.jButtonConnect.setText("Log-in");
                    DashboardClient.socket.close();
                    DashboardClient.socket = null;
                    JOptionPane.showMessageDialog(DashboardClient.window, "Someone used this username!!!");
                    break;
                } else if (info.equals("13")) {
                    DashboardClient.path = line + "\\";
                    DashboardClient.jLabelPath.setText(line);

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                    Object[] obj = new Object[] { DashboardClient.tableModel.getRowCount() + 1,
                            DashboardClient.path,
                            dateFormat.format(date), "Change path",
                            DashboardClient.nameClient,
                            "(Notification) Server send change path" };

                    String data = "{" + (DashboardClient.tableModel.getRowCount() + 1) + ","
                            + DashboardClient.path + "," +
                            dateFormat.format(date).toString() + "," + "Change path" + "," +
                            DashboardClient.nameClient + "," +
                            "(Notification) Server send change path" + "}";

                    DashboardClient.tableModel.addRow(obj);
                    DashboardClient.jtableClients.setModel(DashboardClient.tableModel);
//                    WriteLogs wr = new WriteLogs();
//                    wr.writeFile(String.valueOf(data), DashboardClient.path, DashboardClient.nameClient);

                    WatchFolder.watchService.close();
                    new Thread(new WatchFolder(this.socket)).start();

                    break;
                } else if (info.equals("5")) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                    Object[] obj = new Object[] { DashboardClient.tableModel.getRowCount() + 1,
                            DashboardClient.path,
                            dateFormat.format(date), "Server die",
                            DashboardClient.nameClient,
                            "(Notification) Server has been die" };

                    String data = "{" + (DashboardClient.tableModel.getRowCount() + 1) + ","
                            + DashboardClient.path + "," +
                            dateFormat.format(date).toString() + "," + "Server die" + "," +
                            DashboardClient.nameClient + "," +
                            "(Notification) Server has been die" + "}";

                    DashboardClient.tableModel.addRow(obj);
                    DashboardClient.jtableClients.setModel(DashboardClient.tableModel);

                    DashboardClient.jButtonConnect.setText("Connect");
                    JOptionPane.showMessageDialog(DashboardClient.window, "Server disconnect, please connect againt");
                    WatchFolder.watchService.close();
                    DashboardClient.socket.close();
                    DashboardClient.socket = null;
                    break;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(DashboardClient.window, "Disconnect to server");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sendData (Socket s, Object message, String info, String name, String path) throws IOException {
        String messages = info + ",," + message + ",," + name + ",," + path;
        PrintWriter pwOut = new PrintWriter(s.getOutputStream(), true);
        pwOut.println(messages);
    }
}
