import java.util.HashSet;

public class FileLoader {
    public static void main(String[] args) {
        HashSet<String> linkSet = new HashSet<>();
        for (int i = 0; i < args.length; i++) {
            if (!linkSet.contains(args[i])) {
                linkSet.add(args[i]);
                new Loader(args[i]).start();
            } else {
                System.out.println("File duplicate: " + args[i]);
            }
        }
    }
}
