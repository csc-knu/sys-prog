package JavaTeacherLib;

class TableNode {
    private String lexemText;
    private int lexemCode;
    private static int counter = 0;

    TableNode(String lexem, int lexemType) {
        this.lexemText = lexem;
        this.lexemCode = lexemType | counter++;
    }

    String getLexemText() {
        return this.lexemText;
    }

    int getLexemCode() {
        return this.lexemCode;
    }

    boolean equals(TableNode node1) {
        return node1 != null && (this == node1 || this.lexemText.equals(node1.lexemText));
    }
}
