package Server;

import javax.swing.*;
import java.awt.*;

/**
 * Server
 * Create by Đặng Ngọc Tiến
 * Date 12/18/2022 - 3:40 PM
 * Description: ...
 */
public class InitServer {
    private JFrame window;
    private JTextField jtextport;
    private JButton jbuttonStart;

    public InitServer() {
        window = new JFrame("Server");
        window.setLayout(new BorderLayout());
        window.setBounds(500, 200, 450, 300);
        window.setResizable(false);

        JPanel header = new JPanel();
        header.setBackground(Color.getHSBColor(0.5f, 0.5f, 0.5f));

        JLabel title = new JLabel("Server");
        title.setFont(new Font("Serif", Font.PLAIN, 30));
        header.add(title);
        window.add(header, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        JPanel bodyIP = new JPanel();
        JLabel labelIp = new JLabel("IP: 127.0.0.1");
        labelIp.setFont(new Font("Serif", Font.PLAIN, 20));
        bodyIP.add(labelIp);

        JPanel bodyPort = new JPanel();
        JLabel labelPort = new JLabel("Port: ");
        labelPort.setFont(new Font("Serif", Font.PLAIN, 20));
        jtextport = new JTextField("8888");
        jtextport.setSize(new Dimension(100, 50));
        jtextport.setPreferredSize(new Dimension(200, 30));
        bodyPort.add(labelPort);
        bodyPort.add(jtextport);

        JPanel bodyButton = new JPanel();
        jbuttonStart = new JButton("Start");
        jbuttonStart.setPreferredSize(new Dimension(100, 30));
        bodyButton.add(jbuttonStart);

        body.add(bodyIP);
        body.add(bodyPort);
        body.add(bodyButton);

        window.add(body, BorderLayout.CENTER);

        JPanel footer = new JPanel();
        footer.setBackground(Color.getHSBColor(0.5f, 0.5f, 0.5f));

        JLabel footerLabel = new JLabel("Đặng Ngọc Tiến - 20127641");
        footerLabel.setFont(new Font("Serif", Font.PLAIN, 14));
        footer.add(footerLabel);
        window.add(footer, BorderLayout.SOUTH);

        window.setVisible(true);
    }

    public static void main(String[] args) {
        new InitServer();
    }
}
