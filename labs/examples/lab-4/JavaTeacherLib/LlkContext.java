package JavaTeacherLib;

import java.util.Iterator;
import java.util.LinkedList;

public class LlkContext {
    private LinkedList<int[]> llkContext = new LinkedList<>();

    LlkContext() {
    }

    boolean wordInContext(int[] word) {
        Iterator i$ = this.llkContext.iterator();

        int len;
        do {
            int[] word1;
            do {
                if (!i$.hasNext()) return false;

                word1 = (int[])i$.next();
            } while (word.length != word1.length);

            len = 0;
            while (len < word.length && word[len] == word1[len]) ++len;
        } while (len != word.length);

        return true;
    }

    int[] getWord(int index) {
        return this.llkContext == null ? null : (index < this.llkContext.size() && index >= 0 ? this.llkContext.get(index) : null);
    }

    int minLengthWord() {
        int minlen = 99;
        for (int[] word : this.llkContext) if (minlen > word.length) minlen = word.length;
        return minlen;
    }

    int calcWords() {
        return this.llkContext.size();
    }

    boolean addWord(int[] word) {
        int len = word.length;
        Iterator i$ = this.llkContext.iterator();

        int ii;
        int[] tmp;
        do {
            do {
                if (!i$.hasNext()) {
                    this.llkContext.add(word);
                    return true;
                }

                tmp = (int[])i$.next();
            } while (tmp.length != len);

            ii = 0;
            while (ii < tmp.length && tmp[ii] == word[ii]) ++ii;
        } while (ii != tmp.length);

        return false;
    }
}
