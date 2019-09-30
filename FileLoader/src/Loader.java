import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Loader extends Thread {
    private String link;

    public Loader(String link) {
        this.link = link;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            String fileName = "file" + link.hashCode() + "." + MimeHelper.getExtension(connection.getContentType());
            FileOutputStream fileOutputStream = new FileOutputStream(
                    new File(fileName));

            byte data[] = new byte[1024];
            int byteCount;
            int totalByteCount = 0;
            while ((byteCount = inputStream.read(data, 0, 1024)) != -1) {
                fileOutputStream.write(data, 0, byteCount);
                totalByteCount += byteCount;
            }

            System.out.println("Loaded: " + link + ", Size: " + totalByteCount / 1024 + "KB" + ", Saved name: " + fileName);

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
