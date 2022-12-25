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
                    // get time now
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    if (info.equals("2")) { // connect
                        // thêm vào table
                        Object[] obj = new Object[] { DashboardClient.tableModel.getRowCount() + 1,
                                DashboardClient.path,
                                dateFormat.format(date), "Connected",
                                DashboardClient.nameClient,
                                DashboardClient.nameClient + " connected to server!" };
                        DashboardClient.tableModel.addRow(obj);
                        DashboardClient.jtableClients.setModel(DashboardClient.tableModel);
                    } else { // disconnect
                        // thêm vào table
                        Object[] obj = new Object[] { DashboardClient.tableModel.getRowCount() + 1,
                                DashboardClient.path,
                                dateFormat.format(date), "Disconnected",
                                DashboardClient.nameClient,
                                DashboardClient.nameClient + " disconnected to server!" };
                        DashboardClient.tableModel.addRow(obj);
                        DashboardClient.jtableClients.setModel(DashboardClient.tableModel);
                    }
                } else if (info.equals("4")) {
                    DashboardClient.jButtonConnect.setText("Log-in");
                    DashboardClient.socket.close();
                    DashboardClient.socket = null;
                    JOptionPane.showMessageDialog(DashboardClient.window, "Someone used this username!");
                    break;
                } else if (info.equals("13")) { // thay đổi path
                    // get time now
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                    // đổi path trong giao diện
                    DashboardClient.path = line + "\\";
                    DashboardClient.jLabelPath.setText(line);

                    // thêm vào table
                    Object[] obj = new Object[] { DashboardClient.tableModel.getRowCount() + 1,
                            DashboardClient.path,
                            dateFormat.format(date), "Change path",
                            DashboardClient.nameClient,
                            "Server send change path" };
                    DashboardClient.tableModel.addRow(obj);
                    DashboardClient.jtableClients.setModel(DashboardClient.tableModel);

                    // đóng watch service và mở lại watch service mới
                    WatchFolder.watchService.close();
                    new Thread(new WatchFolder(this.socket)).start();

                    break;
                } else if (info.equals("5")) { // Die server
                    // get time now
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                    // thêm vào table
                    Object[] obj = new Object[] { DashboardClient.tableModel.getRowCount() + 1,
                            DashboardClient.path,
                            dateFormat.format(date), "Server die",
                            DashboardClient.nameClient,
                            "Server has been die" };
                    DashboardClient.tableModel.addRow(obj);
                    DashboardClient.jtableClients.setModel(DashboardClient.tableModel);

                    // set nut connect
                    DashboardClient.jButtonConnect.setText("Connect");
                    // thông báo server die
                    JOptionPane.showMessageDialog(DashboardClient.window, "Server disconnect, please connect againt");

                    // đóng socket và watch service
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

    // gửi thông tin đến server
    public static void sendData (Socket s, Object message, String info, String name, String path) throws IOException {
        String messages = info + ",," + message + ",," + name + ",," + path;
        PrintWriter pwOut = new PrintWriter(s.getOutputStream(), true);
        pwOut.println(messages);
    }
}
