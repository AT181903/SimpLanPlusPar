package mainPackage.SimpLanPlus.utils.errors;

public class CustomError {

    public final String msg;

    public Boolean isWarning;

    public CustomError(String msg) {
        this(msg, false);
    }

    public CustomError(String msg, Boolean isWarning) {
        this.msg = msg;
        this.isWarning = isWarning;
    }

    @Override
    public String toString() {
        return msg;
    }
}
