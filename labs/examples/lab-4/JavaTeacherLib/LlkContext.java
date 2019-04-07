//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package JavaTeacherLib;

import java.util.Iterator;
import java.util.LinkedList;

public class LlkContext {
    private LinkedList<int[]> llkContext = new LinkedList();

    public LlkContext() {
    }

    public boolean wordInContext(int[] word) {
        Iterator i$ = this.llkContext.iterator();

        int len;
        do {
            int[] word1;
            do {
                if(!i$.hasNext()) {
                    return false;
                }

                word1 = (int[])i$.next();
            } while(word.length != word1.length);

            for(len = 0; len < word.length && word[len] == word1[len]; ++len) {
                ;
            }
        } while(len != word.length);

        return true;
    }

    public int[] getWord(int index) {
        return this.llkContext == null?null:(index < this.llkContext.size() && index >= 0?(int[])this.llkContext.get(index):null);
    }

    public int minLengthWord() {
        int minlen = 99;
        Iterator i$ = this.llkContext.iterator();

        while(i$.hasNext()) {
            int[] word = (int[])i$.next();
            if(minlen > word.length) {
                minlen = word.length;
            }
        }

        return minlen;
    }

    public int calcWords() {
        return this.llkContext.size();
    }

    public boolean addWord(int[] word) {
        int len = word.length;
        Iterator i$ = this.llkContext.iterator();

        int ii;
        int[] tmp;
        do {
            do {
                if(!i$.hasNext()) {
                    this.llkContext.add(word);
                    return true;
                }

                tmp = (int[])i$.next();
            } while(tmp.length != len);

            for(ii = 0; ii < tmp.length && tmp[ii] == word[ii]; ++ii) {
                ;
            }
        } while(ii != tmp.length);

        return false;
    }
}
