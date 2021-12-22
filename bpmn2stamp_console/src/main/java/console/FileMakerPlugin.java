package console;

public class FileMakerPlugin {

    public static final String JAVA_EXECUTION_COMMAND = "java -jar \"asd\"" ;

    public static void main(String[] args) {
        BatExecutionFile batExecutionFile = new BatExecutionFile(JAVA_EXECUTION_COMMAND + " %*");

        ExecutionFileMaker.writeExecutionFile(batExecutionFile);
    }

}
