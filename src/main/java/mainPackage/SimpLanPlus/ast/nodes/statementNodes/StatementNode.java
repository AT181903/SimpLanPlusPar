package mainPackage.SimpLanPlus.ast.nodes.statementNodes;

import mainPackage.SimpLanPlus.ast.nodes.Node;

public abstract class StatementNode implements Node {
    private String endBlockLabel;

    public void setEndBlockLabel(String endBlockLabel) {
        this.endBlockLabel = endBlockLabel;
    }

    public String getEndBlockLabel() {
        return endBlockLabel;
    }
}
