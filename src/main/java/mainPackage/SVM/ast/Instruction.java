package mainPackage.SVM.ast;

public class Instruction {
    private final String name;
    private final String param1;
    private final String param2;
    private final String param3;
    private final Integer offset;

    public Instruction(String name, String param1, String param2, String param3, Integer offset) {
        this.name = name;
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.offset = offset;
    }

    public Integer getOffset() {
        return offset;
    }

    public String getName() {
        return name;
    }

    public String getParam1() {
        return param1;
    }

    public String getParam2() {
        return param2;
    }

    public String getParam3() {
        return param3;
    }
}
