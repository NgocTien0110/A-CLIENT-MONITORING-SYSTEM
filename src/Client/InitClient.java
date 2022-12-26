package Client;

import Server.DashboardServer;

import javax.swing.*;
import java.awt.*;

/**
 * Client
 * Create by Đặng Ngọc Tiến
 * Date 12/18/2022 - 3:41 PM
 * Description: ...
 */
public class InitClient {
    private JFrame window;
    private JTextField jtextIP;
    private JTextField jtextPort;
    private JTextField jtextName;
    private JButton jbuttonConnect;

    public InitClient() {
        window = new JFrame("Client");
        window.setLayout(new BorderLayout());
        window.setBounds(500, 200, 450, 300);
        window.setResizable(false);

        JPanel header = new JPanel();
        header.setBackground(Color.getHSBColor(0.5f, 0.5f, 0.5f));

        JLabel title = new JLabel("Client");
        title.setFont(new Font("Serif", Font.PLAIN, 30));
        header.add(title);
        window.add(header, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        JPanel bodyIP = new JPanel();
        JLabel labelIp = new JLabel("IP: ");
        labelIp.setFont(new Font("Serif", Font.PLAIN, 20));
        jtextIP = new JTextField();
        jtextIP.setSize(new Dimension(100, 50));
        jtextIP.setPreferredSize(new Dimension(200, 30));
        bodyIP.add(labelIp);
        bodyIP.add(jtextIP);

        JPanel bodyPort = new JPanel();
        JLabel labelPort = new JLabel("Port: ");
        labelPort.setFont(new Font("Serif", Font.PLAIN, 20));
        jtextPort = new JTextField("2022");
        jtextPort.setSize(new Dimension(100, 50));
        jtextPort.setPreferredSize(new Dimension(200, 30));
        bodyPort.add(labelPort);
        bodyPort.add(jtextPort);

        JPanel bodyName = new JPanel();
        JLabel labelName = new JLabel("Name: ");
        labelName.setFont(new Font("Serif", Font.PLAIN, 20));
        jtextName = new JTextField();
        jtextName.setSize(new Dimension(100, 50));
        jtextName.setPreferredSize(new Dimension(200, 30));
        bodyName.add(labelName);
        bodyName.add(jtextName);

        JPanel bodyButton = new JPanel();
        jbuttonConnect = new JButton("Connect");
        jbuttonConnect.addActionListener(e -> {
            String ip = jtextIP.getText();
            int port = Integer.parseInt(jtextPort.getText());
            String name = jtextName.getText();
            if (ip.equals("") || port == 0 || name.equals("")) {
                JOptionPane.showMessageDialog(null, "Please enter all information");
            } else {
                new DashboardClient(ip, port, name);
                this.window.dispose();
            }
        });

        jbuttonConnect.setPreferredSize(new Dimension(100, 30));
        bodyButton.add(jbuttonConnect);

        body.add(bodyIP);
        body.add(bodyPort);
        body.add(bodyName);
        body.add(bodyButton);

        window.add(body, BorderLayout.CENTER);

        JPanel footer = new JPanel();
        footer.setBackground(Color.getHSBColor(0.5f, 0.5f, 0.5f));

        JLabel footerLabel = new JLabel("Copyright by Đặng Ngọc Tiến - 20127641");
        footerLabel.setFont(new Font("Serif", Font.PLAIN, 14));
        footer.add(footerLabel);
        window.add(footer, BorderLayout.SOUTH);

        window.setVisible(true);
    }

//    public static void main(String[] args) {
//        new InitClient();
//    }
}
