package Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Client
 * Create by Đặng Ngọc Tiến
 * Date 12/18/2022 - 11:53 PM
 * Description: ...
 */
public class DashboardClient {
    private JFrame window;
    private JLabel jLabelIP;
    private JLabel jLabelPort;
    private JLabel jLabelName;
    private JLabel jLabelPath;
    private JButton jButtonChooseFile;
    private JButton jButtonConnect;
    private JButton jButtonLogs;
    private JButton jButtonExit;
    private JTextField jtextSearch;
    private JButton jbuttonSearch;
    private JTable jtableClients;
    private DefaultTableModel tableModel;

    public DashboardClient() {
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
        labelName.setFont(new Font("Serif", Font.PLAIN, 14));
        jLabelName = new JLabel("Tiến");
        jLabelName.setFont(new Font("Serif", Font.PLAIN, 14));
        bodyName.add(labelName);
        bodyName.add(jLabelName);

        JPanel bodyIP = new JPanel();
        JLabel labelIp = new JLabel("IP: ");
        labelIp.setFont(new Font("Serif", Font.PLAIN, 14));
        jLabelIP = new JLabel("172.0.0.1");
        jLabelIP.setFont(new Font("Serif", Font.PLAIN, 14));
        bodyIP.add(labelIp);
        bodyIP.add(jLabelIP);

        JPanel bodyPort = new JPanel();
        JLabel labelPort = new JLabel("Port: ");
        labelPort.setFont(new Font("Serif", Font.PLAIN, 14));
        jLabelPort = new JLabel("8888");
        jLabelPort.setFont(new Font("Serif", Font.PLAIN, 14));
        bodyPort.add(labelPort);
        bodyPort.add(jLabelPort);

        JPanel bodyPath = new JPanel();
        JLabel labelPath = new JLabel("Path: ");
        labelPath.setFont(new Font("Serif", Font.PLAIN, 14));
        jLabelPath = new JLabel("C:\\Users\\Admin\\Desktop\\");
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

    public static void main(String[] args) {
        new DashboardClient();
    }
}
