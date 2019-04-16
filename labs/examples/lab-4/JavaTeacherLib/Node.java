package JavaTeacherLib;

class Node {
    private int[] rule;
    private int teg;
    private LlkContext firstFollowK;
    private LlkContext firstKForRoole;

    Node(int[] roole1, int len) {
        this.rule = new int[len];

        System.arraycopy(roole1, 0, this.rule, 0, len);

        this.teg = 0;
        this.firstFollowK = null;
        this.firstKForRoole = null;
    }

    void addFirstFollowK(LlkContext result) {
        this.firstFollowK = result;
    }

    LlkContext getFirstFollowK() {
        return this.firstFollowK;
    }

    void addFirstKForRoole(LlkContext result) {
        this.firstKForRoole = result;
    }

    LlkContext getFirstKForRoole() {
        return this.firstKForRoole;
    }

    int[] getRule() {
        return this.rule;
    }

    int getTeg() {
        return this.teg;
    }

    void setTeg(int teg1) {
        this.teg = teg1;
    }
}
