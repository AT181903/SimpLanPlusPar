package mainPackage.SimpLanPlus.utils.errors;

public class CustomException {

    public CustomException(String message) {
        System.err.println(message);
        System.exit(0);
    }
}
