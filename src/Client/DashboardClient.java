package Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Client
 * Create by Đặng Ngọc Tiến
 * Date 12/18/2022 - 11:53 PM
 * Description: ...
 */
public class DashboardClient {
    public static JFrame window;
    private JLabel jLabelIP;
    private JTextField jTextFieldIP;
    private JLabel jLabelPort;
    private JTextField jTextFieldPort;
    private JLabel jLabelName;
    public static JLabel jLabelPath;
//    private JButton jButtonChooseFile;
    public static JButton jButtonConnect;
//    private JButton jButtonLogs;
    private JButton jButtonExit;
    private JTextField jtextSearch;
    private JButton jbuttonSearch;
    public static JTable jtableClients;
    public static DefaultTableModel tableModel;
    public static Socket socket = null;
    public static String path = "D:\\Code\\Monitor";
    private String globalIp;
    private int globaPort;
    public static String nameClient;


    public DashboardClient(String ip, int port, String name) {
        if (socket != null && socket.isConnected()) {
            JOptionPane.showMessageDialog(window, "Connected!");
        } else {
            try {
                socket = new Socket(ip, port);
                nameClient = name;
                globalIp = ip;
                port = port;
                ClientData.sendData(socket, name, "2", "Connected", path);
                new Thread(new ClientData(socket)).start();

            } catch (Exception e2) {
                JOptionPane.showMessageDialog(window, "Can't connect check ip and port");
            }
        }
        init(ip, port, name);
        new Thread(new WatchFolder(socket)).start();
    }

     public void init(String ip, int port, String name) {
        window = new JFrame("Client");
        window.setLayout(new BorderLayout());
        window.setBounds(300, 150, 1000, 500);

        JPanel header = new JPanel();
        header.setBackground(Color.getHSBColor(0.5f, 0.5f, 0.5f));

        JLabel title = new JLabel("Client");
        title.setFont(new Font("Serif", Font.PLAIN, 30));
        header.add(title);
        window.add(header, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setLayout(new BorderLayout());

        JPanel bodyLeft = new JPanel();
        bodyLeft.setBorder(BorderFactory.createTitledBorder("Information"));
        bodyLeft.setLayout(new GridLayout(8, 1, 10, 10));
        bodyLeft.setPreferredSize(new Dimension(200, 0));

        jButtonConnect = new JButton("Disconnect");
        jButtonConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (socket == null) {
                    try {
                        globalIp = jTextFieldIP.getText();
                        globaPort = Integer.parseInt(jTextFieldPort.getText());
                        socket = new Socket(globalIp, globaPort);
                        jButtonConnect.setText("Disconnec");

                        ClientData.sendData(socket, nameClient, "2", "Connected", path);
                        new Thread(new ClientData(socket)).start();
                        new Thread(new WatchFolder(socket)).start();

                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();

                        Object[] obj = new Object[] { tableModel.getRowCount() + 1, path,
                                dateFormat.format(date), "Connected",
                                nameClient,
                                "(Notification) " + nameClient + " connected to server!" };

                        String data = "{" + (DashboardClient.tableModel.getRowCount() + 1) + ","
                                + DashboardClient.path + "," +
                                dateFormat.format(date).toString() + "," + "Connected" + "," +
                                DashboardClient.nameClient + "," +
                                "(Notification) " + DashboardClient.nameClient + " connected to the server!" + "}";

//                        WriteLogs wr = new WriteLogs();
//                        wr.writeFile(String.valueOf(data), DashboardClient.path, DashboardClient.nameClient);
                        tableModel.addRow(obj);
                        jtableClients.setModel(tableModel);
                    } catch (Exception e2) {
                        JOptionPane.showMessageDialog(window, "Can't connect check ip and port");
                    }
                } else if (socket != null && socket.isConnected()) {
                    try {
                        ClientData.sendData(socket, nameClient, "3", "Disconnected", path);
                        jButtonConnect.setText("Connect");
                        WatchFolder.watchService.close();
                        socket.close();
                        socket = null;
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();

                        Object[] obj = new Object[] { tableModel.getRowCount() + 1, path,
                                dateFormat.format(date), "Disconnected",
                                nameClient,
                                "(Notification) " + nameClient + " disconnected to server!" };

                        String data = "{" + (DashboardClient.tableModel.getRowCount() + 1) + ","
                                + DashboardClient.path + "," +
                                dateFormat.format(date).toString() + "," + "Disconnected" + "," +
                                DashboardClient.nameClient + "," +
                                "(Notification) " + DashboardClient.nameClient + " disconnected to the server!" + "}";

//                        WriteLogs wr = new WriteLogs();
//                        wr.writeFile(String.valueOf(data), DashboardClient.path, DashboardClient.nameClient);
                        tableModel.addRow(obj);
                        jtableClients.setModel(tableModel);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        JPanel bodyName = new JPanel();
        JLabel labelName = new JLabel("Name: ");
        labelName.setFont(new Font("Serif", Font.PLAIN, 16));
        jLabelName = new JLabel(name);
        jLabelName.setFont(new Font("Serif", Font.PLAIN, 16));
        bodyName.add(labelName);
        bodyName.add(jLabelName);

        JPanel bodyIP = new JPanel();
        JLabel labelIp = new JLabel("IP: ");
        labelIp.setFont(new Font("Serif", Font.PLAIN, 16));
        jTextFieldIP = new JTextField(ip);
        jTextFieldIP.setFont(new Font("Serif", Font.PLAIN, 16));
        jTextFieldIP.setPreferredSize(new Dimension(130, 20));
//        jLabelIP = new JLabel(ip);
//        jLabelIP.setFont(new Font("Serif", Font.PLAIN, 16));
        bodyIP.add(labelIp);
        bodyIP.add(jTextFieldIP);

        JPanel bodyPort = new JPanel();
        JLabel labelPort = new JLabel("Port: ");
        labelPort.setFont(new Font("Serif", Font.PLAIN, 16));
        jTextFieldPort = new JTextField(String.valueOf(port));
        jTextFieldPort.setFont(new Font("Serif", Font.PLAIN, 16));
        jTextFieldPort.setPreferredSize(new Dimension(100, 20));
//        jLabelPort = new JLabel(String.valueOf(port));
//        jLabelPort.setFont(new Font("Serif", Font.PLAIN, 16));
        bodyPort.add(labelPort);
        bodyPort.add(jTextFieldPort);

        JPanel bodyPath = new JPanel();
        JLabel labelPath = new JLabel("Path: ");
        labelPath.setFont(new Font("Serif", Font.PLAIN, 14));
        jLabelPath = new JLabel(path);
        jLabelPath.setFont(new Font("Serif", Font.PLAIN, 14));
        bodyPath.add(labelPath);
        bodyPath.add(jLabelPath);

//        jButtonChooseFile = new JButton("Choose File");
//        jButtonLogs = new JButton("Load Logs");
        jButtonExit = new JButton("Exit");
        jButtonExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (socket != null && socket.isConnected()) {
                    try {
                        ClientData.sendData(socket, nameClient, "3", "Disconnected", path);
                        WatchFolder.watchService.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                System.exit(0);
            }
        });



        bodyLeft.add(jButtonConnect);
        bodyLeft.add(bodyName);
        bodyLeft.add(bodyIP);
        bodyLeft.add(bodyPort);
        bodyLeft.add(bodyPath);
//        bodyLeft.add(jButtonChooseFile);
//        bodyLeft.add(jButtonLogs);
        bodyLeft.add(jButtonExit);

        String column[] = {"STT", "Monitoring directory", "Time", "Action", "Name Client", "Description"};
        JPanel bodyCenter = new JPanel();
        bodyCenter.setLayout(new BorderLayout());
        bodyCenter.setBorder(BorderFactory.createTitledBorder("Monitoring Clients"));
        JPanel bodyCenterSearch = new JPanel();
        bodyCenterSearch.setLayout(new BoxLayout(bodyCenterSearch, BoxLayout.X_AXIS));
        jtextSearch = new JTextField();
        jtextSearch.setPreferredSize(new Dimension(200, 30));
        jbuttonSearch = new JButton("Search");
        jbuttonSearch.setPreferredSize(new Dimension(100, 30));

        bodyCenterSearch.add(jtextSearch);
        bodyCenterSearch.add(jbuttonSearch);
        bodyCenter.add(bodyCenterSearch, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(column, 0){
            public Class getColumnClass(int column) {
                Class returnValue;
                if ((column >= 0) && (column < getColumnCount())) {
                    returnValue = getValueAt(0, column).getClass();
                } else {
                    returnValue = Object.class;
                }
                return returnValue;
            }
        };
        jtableClients = new JTable(tableModel);
        jtableClients.setRowHeight(30);

        jtableClients.setModel(tableModel);
        jtableClients.setAutoCreateRowSorter(true);
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
        jtableClients.setRowSorter(sorter);
        TableColumnModel columnModel = jtableClients.getColumnModel();
        jtableClients.getColumnModel().getColumn(0).setPreferredWidth(30);
        jtableClients.getColumnModel().getColumn(1).setPreferredWidth(150);
        jtableClients.getColumnModel().getColumn(2).setPreferredWidth(130);
        jtableClients.getColumnModel().getColumn(3).setPreferredWidth(100);
        jtableClients.getColumnModel().getColumn(4).setPreferredWidth(100);
        jtableClients.getColumnModel().getColumn(5).setPreferredWidth(300);
        JScrollPane scrollPane = new JScrollPane(jtableClients);
        jbuttonSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = jtextSearch.getText();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        bodyCenter.add(scrollPane, BorderLayout.CENTER);

        body.add(bodyLeft, BorderLayout.WEST);
        body.add(bodyCenter, BorderLayout.CENTER);

        window.add(body, BorderLayout.CENTER);

        JPanel footer = new JPanel();
        footer.setBackground(Color.getHSBColor(0.5f, 0.5f, 0.5f));
        JLabel footerLabel = new JLabel("Copyright by Đặng Ngọc Tiến - 20127641");
        footerLabel.setFont(new Font("Serif", Font.PLAIN, 14));
        footer.add(footerLabel);

        window.add(footer, BorderLayout.SOUTH);
        window.setVisible(true);
        window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (socket != null && socket.isConnected()) {
                    try {
                        ClientData.sendData(socket, nameClient, "3", "Disconnected", path);
                        WatchFolder.watchService.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                System.exit(0);
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jButtonConnect) {
//            if (socket == null) {
//                connect();
//            } else {
//                JOptionPane.showMessageDialog(window, "You are connected");
//            }
        }  else if (e.getSource() == jButtonExit) {
//            if (socket != null) {
//                new ClientSend(socket, nameClient, "3", "Exit", path);
//                try {
//                    socket.close();
//                } catch (IOException ex) {
//                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//            System.exit(0);
        }
    }

//    public static void main(String[] args) {
//        new DashboardClient();
//    }
}
