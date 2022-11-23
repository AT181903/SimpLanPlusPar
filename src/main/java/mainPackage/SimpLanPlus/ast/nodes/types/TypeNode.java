package mainPackage.SimpLanPlus.ast.nodes.types;

import mainPackage.SimpLanPlus.ast.nodes.Node;

public abstract class TypeNode implements Node {
    public Integer getReferenceLevel(){
        return 1;
    }
}
