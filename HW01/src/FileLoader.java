import java.util.HashSet;

public class FileLoader {
    public static void main(String[] args) {
        String destStorage = System.getProperty("user.dir");
        if (args.length == 0) {
            System.out.println("Array of arguments is empty");
        }
        HashSet<String> urlSet = new HashSet<>();
        for (int i = 0; i < args.length; i++) {
            if (FileHelper.isFileCorrect(args[i])) {
                if (!urlSet.contains(args[i])) {
                    urlSet.add(args[i]);
                } else {
                    System.out.println("The file (" + args[i] + ") will not be downloaded because it repeats");
                }
            } else {
                System.out.println("The file (" + args[i] + ") is invalid");
            }
        }

        if (args.length != 0) {
            System.out.println("Start downloading");
            urlSet.stream().forEach((url) -> new LoaderThread(destStorage, url));
        }

    }
}
