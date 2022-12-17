package Client;

import java.io.*;
import java.net.Socket;

/**
 * Client
 * Create by Đặng Ngọc Tiến
 * Date 12/17/2022 - 1:30 PM
 * Description: ...
 */
public class Main {
    public static void main(String arg[])
    {
        try
        {
            Socket s = new Socket("localhost",3200);
            System.out.println(s.getPort());

            InputStream is=s.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(is));

            OutputStream os=s.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

            String sentMessage="";
            String receivedMessage;

            System.out.println("Talking to Server");

            do
            {
                DataInputStream din=new DataInputStream(System.in);
                sentMessage=din.readLine();
                bw.write(sentMessage);
                bw.newLine();
                bw.flush();

                if (sentMessage.equalsIgnoreCase("quit"))
                    break;
                else
                {
                    receivedMessage=br.readLine();
                    System.out.println("Received : " + receivedMessage);
                }

            }
            while(true);

            bw.close();
            br.close();
        }
        catch(IOException e)
        {
            System.out.println("There're some error");
        }
    }
}
