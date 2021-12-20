package mapper.org2bbo;

public class Warning {

    private Object instance;
    private String message;
    private Throwable throwable;

    public static Warning create(Object instance, String message, Object... args) {
        String msg = String.format(message, args);
        return new Warning(instance, msg);
    }

    public Warning(Object instance, String message) {
        this.instance = instance;
        this.message = message;
    }

    public Warning(Object instance, String message, Throwable throwable) {
        this.instance = instance;
        this.message = message;
        this.throwable = throwable;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
