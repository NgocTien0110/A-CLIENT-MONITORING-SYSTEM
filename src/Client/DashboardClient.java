package Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Client
 * Create by Đặng Ngọc Tiến
 * Date 12/18/2022 - 11:53 PM
 * Description: ...
 */
public class DashboardClient {
    public static JFrame window;
    private JLabel jLabelIP;
    private JLabel jLabelPort;
    private JLabel jLabelName;
    public static JLabel jLabelPath;
    private JButton jButtonChooseFile;
    public static JButton jButtonConnect;
    private JButton jButtonLogs;
    private JButton jButtonExit;
    private JTextField jtextSearch;
    private JButton jbuttonSearch;
    public static JTable jtableClients;
    public static DefaultTableModel tableModel;
    public static Socket socket = null;
    public static String path = "D:\\Code\\Monitor\\Client";
    private String globalIp;
    private int port;
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
                new ClientSend(socket, name, "2", "Connected", path);
                new Thread(new ClientReceive(socket)).start();

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
        bodyLeft.setPreferredSize(new Dimension(300, 0));

        jButtonConnect = new JButton("Connect");

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
        jLabelIP = new JLabel(ip);
        jLabelIP.setFont(new Font("Serif", Font.PLAIN, 16));
        bodyIP.add(labelIp);
        bodyIP.add(jLabelIP);

        JPanel bodyPort = new JPanel();
        JLabel labelPort = new JLabel("Port: ");
        labelPort.setFont(new Font("Serif", Font.PLAIN, 16));
        jLabelPort = new JLabel(String.valueOf(port));
        jLabelPort.setFont(new Font("Serif", Font.PLAIN, 16));
        bodyPort.add(labelPort);
        bodyPort.add(jLabelPort);

        JPanel bodyPath = new JPanel();
        JLabel labelPath = new JLabel("Path: ");
        labelPath.setFont(new Font("Serif", Font.PLAIN, 14));
        jLabelPath = new JLabel(path);
        jLabelPath.setFont(new Font("Serif", Font.PLAIN, 14));
        bodyPath.add(labelPath);
        bodyPath.add(jLabelPath);

        jButtonChooseFile = new JButton("Choose File");
        jButtonLogs = new JButton("Load Logs");
        jButtonExit = new JButton("Exit");

        bodyLeft.add(jButtonConnect);
        bodyLeft.add(bodyName);
        bodyLeft.add(bodyIP);
        bodyLeft.add(bodyPort);
        bodyLeft.add(bodyPath);
        bodyLeft.add(jButtonChooseFile);
        bodyLeft.add(jButtonLogs);
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
        jtableClients.getColumnModel().getColumn(0).setPreferredWidth(50);
        jtableClients.getColumnModel().getColumn(1).setPreferredWidth(100);
        jtableClients.getColumnModel().getColumn(2).setPreferredWidth(200);
        jtableClients.getColumnModel().getColumn(3).setPreferredWidth(100);
        jtableClients.getColumnModel().getColumn(4).setPreferredWidth(100);
        jtableClients.getColumnModel().getColumn(5).setPreferredWidth(200);
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
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jButtonConnect) {
//            if (socket == null) {
//                connect();
//            } else {
//                JOptionPane.showMessageDialog(window, "You are connected");
//            }
        } else if (e.getSource() == jButtonChooseFile) {
//            if (socket != null) {
//                JFileChooser fileChooser = new JFileChooser();
//                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//                int result = fileChooser.showOpenDialog(window);
//                if (result == JFileChooser.APPROVE_OPTION) {
//                    path = fileChooser.getSelectedFile().getAbsolutePath();
//                    jLabelPath.setText(path);
//                    new ClientSend(socket, nameClient, "2", "Connected", path);
//                }
//            } else {
//                JOptionPane.showMessageDialog(window, "You are not connected");
//            }
        } else if (e.getSource() == jButtonLogs) {
//            if (socket != null) {
//                new ClientSend(socket, nameClient, "4", "Load Logs", path);
//            } else {
//                JOptionPane.showMessageDialog(window, "You are not connected");
//            }
        } else if (e.getSource() == jButtonExit) {
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
