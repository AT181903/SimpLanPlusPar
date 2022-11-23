package mainPackage.SimpLanPlus.utils.tools;

import mainPackage.SimpLanPlus.ast.nodes.Node;

public class SimpLanPlusLib {
    private static int labelsCount = 0;

    // Check if "a" type is less equal of "b" type
    public static boolean isSametype(Node a, Node b) {
        return a.getClass().equals(b.getClass()); //||
    }

    // Generate fresh label
    public static String generateFreshLabel(String key) {
        return key + (labelsCount++);
    }
}