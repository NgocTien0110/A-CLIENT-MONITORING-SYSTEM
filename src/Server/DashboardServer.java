package Server;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.Socket;
import java.util.Map;

/**
 * Server
 * Create by Đặng Ngọc Tiến
 * Date 12/18/2022 - 9:34 PM
 * Description: ...
 */
public class DashboardServer {
    private JFrame window;
    private JLabel jlabelIP;
    private JLabel jlabelPort;
    private JButton jbuttonLogs;
    private JButton jbuttonExit;
    private JTable jtableClients;
    private JTextField jtextSearch;
    private JButton jbuttonSearch;
    private DefaultTableModel tableModel;

//    private int port;
    private String path = "D:\\Code\\Monitor";
    private JList<String> jListClients;
    private Map<String, String> mapPath;
    private Map<String, Socket> mapSocket;

    public DashboardServer() {
        window = new JFrame("Server");
        window.setLayout(new BorderLayout());
        window.setBounds(300, 150, 1000, 500);

        JPanel header = new JPanel();
        header.setBackground(Color.getHSBColor(0.5f, 0.5f, 0.5f));
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
        jlabelIP = new JLabel("IP: 127.0.0.1");
        jlabelIP.setFont(new Font("Serif", Font.PLAIN, 20));
        jlabelPort = new JLabel("Port: 8888");
        jlabelPort.setFont(new Font("Serif", Font.PLAIN, 20));
        jbuttonLogs = new JButton("Load Logs");
        jbuttonExit = new JButton("Exit");
        bodyLeft.add(jlabelIP);
        bodyLeft.add(jlabelPort);
        bodyLeft.add(jbuttonLogs);
        bodyLeft.add(jbuttonExit);

        String column[] = {"STT", "Name", "Monitoring Directory", "Time", "Action", "Description"};
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
        jListClients.setPreferredSize(new Dimension(200, 0));
        JScrollPane scrollPane1 = new JScrollPane(jListClients);

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
        new DashboardServer();

    }
}
