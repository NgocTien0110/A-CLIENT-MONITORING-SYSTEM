package Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.InetAddress;

/**
 * Server
 * Create by Đặng Ngọc Tiến
 * Date 12/18/2022 - 3:40 PM
 * Description: ...
 */
public class InitServer{
    private JFrame window;
    private String address;
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

        try {
            address = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JPanel bodyIP = new JPanel();
        JLabel labelIp = new JLabel("IP: "+ address);
        labelIp.setFont(new Font("Serif", Font.PLAIN, 20));
        bodyIP.add(labelIp);

        JPanel bodyPort = new JPanel();
        JLabel labelPort = new JLabel("Port: ");
        labelPort.setFont(new Font("Serif", Font.PLAIN, 20));
        jtextport = new JTextField("2022");
        jtextport.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!((c >= '0') && (c <= '9') ||
                        (c == KeyEvent.VK_BACK_SPACE) ||
                        (c == KeyEvent.VK_DELETE))) {
                    evt.consume();
                }
            }
        });
        jtextport.setSize(new Dimension(100, 50));
        jtextport.setPreferredSize(new Dimension(200, 30));
        bodyPort.add(labelPort);
        bodyPort.add(jtextport);

        JPanel bodyButton = new JPanel();
        jbuttonStart = new JButton("Start");
        jbuttonStart.setPreferredSize(new Dimension(100, 30));
        jbuttonStart.addActionListener(this::actionPerformed);
        bodyButton.add(jbuttonStart);

        body.add(bodyIP);
        body.add(bodyPort);
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

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbuttonStart) {
            int port = Integer.parseInt(jtextport.getText());
            if (port > 0 && port < 65535) {
                System.out.println("Start");
                new DashboardServer(port);
                this.window.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Port must be in range 0 - 65535");
            }
        }
    }

//    public static void main(String[] args) {
//        new InitServer();
//    }
}
