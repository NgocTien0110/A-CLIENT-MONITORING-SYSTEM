package Server;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Server
 * Create by Đặng Ngọc Tiến
 * Date 12/18/2022 - 9:34 PM
 * Description: ...
 */
public class DashboardServer {
    public JFrame window;
    private JLabel jlabelIP;
    private JLabel jlabelPort;
    private JButton jbuttonLogs;
    private JButton jbuttonExit;
    public static JTable jtableClients;
    private JTextField jtextSearch;
    private JButton jbuttonSearch;
    public static DefaultTableModel tableModel;

//    private int port;
    private String address;
    public static String path = "D:\\Code\\Monitor\\Server";
    public static JList<String> jListClients;
    public static Map<String, String> mapPath = new HashMap<String, String>();;
    public static Map<String, Socket> mapSocket = new HashMap<String, Socket>();;

    public DashboardServer(int port) {
        if (ServerHandle.ss != null && !ServerHandle.ss.isClosed()) {
            JOptionPane.showMessageDialog(window, "Server is running!");
        } else {
            if (port != 0) {
                try {
                    ServerHandle.flag = true;
                    address = InetAddress.getLocalHost().getHostAddress();
                    new Thread(new ServerHandle(port)).start();
                    path = Paths.get(".").normalize().toAbsolutePath().toString();
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(window, "Cannot start server");
                }
            }
        }
        init(port);
    }

    public void init(int port) {
        window = new JFrame("Server");
        window.setLayout(new BorderLayout());
        window.setBounds(300, 150, 1000, 500);

        JPanel header = new JPanel();
        header.setBackground(Color.getHSBColor(0.5f, 0.5f, 0.5f));

        JLabel title = new JLabel("Server");
        title.setFont(new Font("Serif", Font.PLAIN, 30));
        header.add(title);
        window.add(header, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setLayout(new BorderLayout());

        JPanel bodyLeft = new JPanel();
        bodyLeft.setBorder(BorderFactory.createTitledBorder("Server"));
        bodyLeft.setPreferredSize(new Dimension(150, 0));
        bodyLeft.setLayout(new GridLayout(4, 1,30,30));


        try {
            address = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        jlabelIP = new JLabel("IP: " + address);
        jlabelIP.setFont(new Font("Serif", Font.PLAIN, 16));
        jlabelPort = new JLabel("Port: " + port);
        jlabelPort.setFont(new Font("Serif", Font.PLAIN, 16));
        jbuttonLogs = new JButton("Load Logs");
        jbuttonExit = new JButton("Exit");
        jbuttonExit.addActionListener(e -> {
            ServerHandle.flag = false;
            System.exit(0);
        });
        bodyLeft.add(jlabelIP);
        bodyLeft.add(jlabelPort);
        bodyLeft.add(jbuttonLogs);
        bodyLeft.add(jbuttonExit);

        String column[] = {"STT","Monitoring directory", "Time", "Action", "Name Client", "Description"};
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

        tableModel = new DefaultTableModel(column, 0);
        jtableClients = new JTable(tableModel);
        jtableClients.setRowHeight(30);
        jtableClients.getColumnModel().getColumn(0).setPreferredWidth(50);
        jtableClients.getColumnModel().getColumn(1).setPreferredWidth(100);
        jtableClients.getColumnModel().getColumn(2).setPreferredWidth(200);
        jtableClients.getColumnModel().getColumn(3).setPreferredWidth(100);
        jtableClients.getColumnModel().getColumn(4).setPreferredWidth(100);
        jtableClients.getColumnModel().getColumn(5).setPreferredWidth(200);
        JScrollPane scrollPane = new JScrollPane(jtableClients);
        bodyCenter.add(scrollPane, BorderLayout.CENTER);

        JPanel bodyRight = new JPanel();
        bodyRight.setLayout(new BorderLayout());
        bodyRight.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));
        bodyRight.setPreferredSize(new Dimension(200, 0));
        bodyRight.setBorder(BorderFactory.createTitledBorder("Clients"));

        jListClients = new JList<>();
        JScrollPane scrollPane1 = new JScrollPane(jListClients);
        jListClients.setPreferredSize(new Dimension(200, 0));
        jListClients.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    JList source = (JList) event.getSource();
                    String selected = source.getSelectedValue().toString();

                    JFileChooser myfileChooser = new JFileChooser();
                    myfileChooser.setDialogTitle("select folder");
                    if (Files.isDirectory(Paths.get(mapPath.get(selected)))) {
                        myfileChooser.setCurrentDirectory(new File(mapPath.get(selected)));
                    }
                    int findresult = myfileChooser.showOpenDialog(window);
                    if (findresult == myfileChooser.APPROVE_OPTION) {
                        String pathClient = myfileChooser.getCurrentDirectory().getAbsolutePath();
                        try {
                            new ServerSend(mapSocket.get(selected), pathClient, "13", "Server");
                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            Date date = new Date();

                            Object[] obj = new Object[] { tableModel.getRowCount() + 1, pathClient,
                                    dateFormat.format(date), "Change path",
                                    selected,
                                    "Change path monitoring systtem" };

                            String data = "{" + (tableModel.getRowCount() + 1) + ","
                                    + pathClient + "," +
                                    dateFormat.format(date).toString() + "," + "Change path" + "," +
                                    selected + "," +
                                    "Change path monitoring systtem" + "}";

                            WriteFile wr = new WriteFile();
                            wr.writeFile(String.valueOf(data), path);
                            tableModel.addRow(obj);
                            jtableClients.setModel(tableModel);

                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                    }
                    System.out.println(selected);
                }
            }
        });


        bodyRight.add(scrollPane1, BorderLayout.CENTER);

        body.add(bodyLeft, BorderLayout.WEST);
        body.add(bodyCenter, BorderLayout.CENTER);
        body.add(bodyRight, BorderLayout.EAST);
        window.add(body, BorderLayout.CENTER);

        JPanel footer = new JPanel();
        footer.setBackground(Color.getHSBColor(0.5f, 0.5f, 0.5f));
        JLabel footerLabel = new JLabel("Copyright by Đặng Ngọc Tiến - 20127641");
        footerLabel.setFont(new Font("Serif", Font.PLAIN, 14));
        footer.add(footerLabel);

        window.add(footer, BorderLayout.SOUTH);
        jtableClients.setModel(tableModel);

        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new DashboardServer(8888);

    }
}
