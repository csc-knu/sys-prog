package JavaTeacherLib;

class Node {
    private int[] rule;
    private int tag;
    private LlkContext firstFollowK;
    private LlkContext firstKForRule;

    Node(int[] otherRule, int len) {
        this.rule = new int[len];
        System.arraycopy(otherRule, 0, this.rule, 0, len);
        this.tag = 0;
        this.firstFollowK = null;
        this.firstKForRule = null;
    }

    void addFirstFollowK(LlkContext result) {
        this.firstFollowK = result;
    }

    LlkContext getFirstFollowK() {
        return this.firstFollowK;
    }

    void addFirstKForRoole(LlkContext result) {
        this.firstKForRule = result;
    }

    LlkContext getFirstKForRoole() {
        return this.firstKForRule;
    }

    int[] getRule() {
        return this.rule;
    }

    int getTag() {
        return this.tag;
    }

    void setTag(int otherTag) {
        this.tag = otherTag;
    }
}
