package Client;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Client
 * Create by Đặng Ngọc Tiến
 * Date 12/19/2022 - 4:15 PM
 * Description: ...
 */
public class WatchFolder implements Runnable {
    public static WatchService watchService;
    private Socket s;

    public WatchFolder(Socket s) {
        this.s = s;
    }

    public void dispose() throws IOException {
        watchService.close();
    }

    @Override
    public void run() {
        try {
            //System.out.println("Watching directory for changes");
            // STEP1: Create a watch service
            watchService = FileSystems.getDefault().newWatchService();

            // STEP2: Get the path of the directory which you want to monitor.
            Path directory = Path.of(DashboardClient.path);

            // STEP3: Register the directory with the watch service
            WatchKey watchKey = directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

            // STEP4: Poll for events
            while (true) {
                for (WatchEvent<?> event : watchKey.pollEvents()) {

                    // STEP5: Get file name from even context
                    WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;

                    Path fileName = pathEvent.context();

                    // STEP6: Check type of event.
                    WatchEvent.Kind<?> kind = event.kind();

                    // STEP7: Perform necessary action with the event
                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        System.out.println("File Created: " + fileName);
                        // get time now
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();

                        // tạo object để thêm vào table
                        Object[] obj = new Object[] { DashboardClient.tableModel.getRowCount() + 1,
                                DashboardClient.path,
                                dateFormat.format(date), "Created",
                                DashboardClient.nameClient,
                                "A new file is created : " + fileName };
                        DashboardClient.tableModel.addRow(obj);
                        DashboardClient.jtableClients.setModel(DashboardClient.tableModel);

                        // gửi data lên server
                        ClientData.sendData(s, DashboardClient.nameClient, "10", "A new file is created : " + fileName,
                                DashboardClient.path);
                    }

                    if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                        System.out.println("A file has been deleted: " + fileName);
                        // get time now
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();

                        // tạo object để thêm vào table
                        Object[] obj = new Object[] { DashboardClient.tableModel.getRowCount() + 1,
                                DashboardClient.path,
                                dateFormat.format(date), "Deleted",
                                DashboardClient.nameClient,
                                "A file has been deleted : " + fileName };
                        DashboardClient.tableModel.addRow(obj);
                        DashboardClient.jtableClients.setModel(DashboardClient.tableModel);

                        // gửi data lên server
                        ClientData.sendData(s, DashboardClient.nameClient, "11", "A file has been deleted : " + fileName,
                                DashboardClient.path);
                    }
                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        System.out.println("A file has been modified: " + fileName);
                        // get time now
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();

                        // tạo object để thêm vào table
                        Object[] obj = new Object[] { DashboardClient.tableModel.getRowCount() + 1,
                                DashboardClient.path,
                                dateFormat.format(date), "Modified",
                                DashboardClient.nameClient,
                                "A file has been modified : " + fileName };
                        DashboardClient.tableModel.addRow(obj);
                        DashboardClient.jtableClients.setModel(DashboardClient.tableModel);

                        // gửi data lên server
                        ClientData.sendData(s, DashboardClient.nameClient, "12", "A file has been modified : " + fileName,
                                DashboardClient.path);
                    }

                }
                // STEP8: Reset the key
                boolean valid = watchKey.reset();
                if (!valid) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
