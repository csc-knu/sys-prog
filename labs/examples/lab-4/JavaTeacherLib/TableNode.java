package JavaTeacherLib;

public class TableNode {
    String lexemaText;
    int lexemacode;
    static int numarator = 0;

    public TableNode(String lexema, int lexemaType) {
        this.lexemaText = lexema;
        this.lexemacode = lexemaType | numarator++;
    }

    public String getLexemaText() {
        return this.lexemaText;
    }

    public int getLexemaCode() {
        return this.lexemacode;
    }

    public boolean equals(TableNode node1) {
        return node1 == null?false:(this == node1?true:this.lexemaText.equals(node1.lexemaText));
    }
}
