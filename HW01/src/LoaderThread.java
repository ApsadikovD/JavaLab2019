import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoaderThread extends Thread {
    private String url;
    private String fileName;
    private String destStorage;

    public LoaderThread(String destStorage, String url) {
        this.destStorage = destStorage;
        this.url = url;
        this.fileName = FileHelper.getFileName(destStorage, FileHelper.getFileNameFromUrl(url));
        this.start();
    }

    @Override
    public void run() {
        try {
            URL url = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            String pathName = destStorage + "/" + fileName;
            FileOutputStream fileOutputStream = new FileOutputStream(new File(pathName));
            byte data[] = new byte[1024];
            int byteCount;
            int totalByteCount = 0;
            while ((byteCount = inputStream.read(data, 0, 1024)) != -1) {
                fileOutputStream.write(data, 0, byteCount);
                totalByteCount += byteCount;
            }

            System.out.println("Loaded: " + this.url + ", Size: " + totalByteCount / 1024 + "KB" + ", Saved name: " + fileName);

            connection.disconnect();
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
        } catch (IOException e) {
            System.out.println(e);
            System.out.println("Something went wrong");
        }
    }
}
