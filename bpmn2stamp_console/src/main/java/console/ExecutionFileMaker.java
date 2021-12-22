package console;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This class contains methods for preparing executable files to run main application
 */
public class ExecutionFileMaker {

    /**
     * Creates passed {@link ExecutionFile execution file} on disk.
     * @param fileToCreate file to create
     */
    public static void writeExecutionFile(ExecutionFile fileToCreate) {
        String filePath = fileToCreate.getFileName() + "." + fileToCreate.getExtension();

        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            byte[] strToBytes = fileToCreate.getExecutionCommand().getBytes();
            outputStream.write(strToBytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
