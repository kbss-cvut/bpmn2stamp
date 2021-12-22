package console;

public class BatExecutionFile implements ExecutionFile {

    private final String executionCommand;

    public BatExecutionFile(String executionCommand) {
        this.executionCommand = executionCommand;
    }

    @Override
    public String getExecutionCommand() {
        return executionCommand;
    }

    @Override
    public String getFileName() {
        return "converter";
    }

    @Override
    public String getExtension() {
        return "bpmn";
    }
}
