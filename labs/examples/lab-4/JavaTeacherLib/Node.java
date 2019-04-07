//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package JavaTeacherLib;

import JavaTeacherLib.LlkContext;

public class Node {
    private int[] roole;
    private int teg;
    private LlkContext firstFollowK;
    private LlkContext firstKforRoole;

    public Node(int[] roole1, int len) {
        this.roole = new int[len];

        for(int ii = 0; ii < len; ++ii) {
            this.roole[ii] = roole1[ii];
        }

        this.teg = 0;
        this.firstFollowK = null;
        this.firstKforRoole = null;
    }

    public void addFirstFollowK(LlkContext rezult) {
        this.firstFollowK = rezult;
    }

    public LlkContext getFirstFollowK() {
        return this.firstFollowK;
    }

    public void addFirstKforRoole(LlkContext rezult) {
        this.firstKforRoole = rezult;
    }

    public LlkContext getFirstKforRoole() {
        return this.firstKforRoole;
    }

    public int[] getRoole() {
        return this.roole;
    }

    public int getTeg() {
        return this.teg;
    }

    public void setTeg(int teg1) {
        this.teg = teg1;
    }
}
