package Server;

import java.io.*;

/**
 * Server
 * Create by Đặng Ngọc Tiến
 * Date 12/19/2022 - 4:29 PM
 * Description: ...
 */
public class WriteLogs {
    public void writeFile(String value, String pathDirectory) {
        String PATH = pathDirectory + "\\";
        String directoryName = PATH;
        String fileName = "logs.txt";

        File directory = new File(directoryName);
        if (!directory.exists()) {
            directory.mkdir();
        }

        try {
            FileOutputStream fos = new FileOutputStream(directoryName + "\\" + fileName + "\\", true);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
            bufferedWriter.append(value + "\r\n");
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
