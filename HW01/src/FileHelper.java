import java.io.File;

public class FileHelper {
    private static String getUniqueFileName(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        String name = fileName.substring(0, fileName.lastIndexOf("."));
        int copyBracketsStartIndex = name.lastIndexOf(" (");
        int copyBracketsFinishIndex = name.lastIndexOf(")");
        if (copyBracketsStartIndex != -1 && copyBracketsFinishIndex != -1) {
            String copyNumber = name.substring(copyBracketsStartIndex + 2, copyBracketsFinishIndex);
            if (isInt(copyNumber)) {
                return name.substring(0, copyBracketsStartIndex)
                        + " (" + (Integer.valueOf(copyNumber) + 1) + ")."
                        + extension;
            }
        }
        return fileName.substring(0, fileName.lastIndexOf(".")) + " (1)." + extension;
    }

    private static boolean isInt(String number) {
        return number.matches("^[0-9]+$");
    }

    public static String getFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1, url.length());
    }

    public static boolean isFileCorrect(String fileName) {
        return fileName.lastIndexOf(".") != -1;
    }

    public static synchronized String getFileName(String destStorage, String fileName) {
        File file = new File(destStorage + "/" + fileName);
        if (file.exists()) {
            return getFileName(destStorage, getUniqueFileName(fileName));
        }
        return fileName;
    }
}
