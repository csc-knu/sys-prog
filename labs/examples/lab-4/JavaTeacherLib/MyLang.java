//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package JavaTeacherLib;

import JavaTeacherLib.LlkContext;
import JavaTeacherLib.Node;
import JavaTeacherLib.TableNode;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedList;

public class MyLang {
    private int axioma;
    private boolean create;
    private int LLK;
    private LinkedList<Node> language;
    private LinkedList<TableNode> lexemaTable;
    private int[] terminals;
    private int[] nonterminals;
    private int[] epsilonNerminals;
    private LlkContext[] termLanguarge;
    private LlkContext[] firstK;
    private LlkContext[] followK;
    private LinkedList<LlkContext>[] LocalkContext;
    private int[][] uprTable;

    public MyLang(String fileLang, int llk1) {
        this.LLK = llk1;
        this.create = false;
        this.language = new LinkedList();
        this.lexemaTable = new LinkedList();
        this.readGrammat(fileLang);
        if(this.create) {
            Iterator i$ = this.language.iterator();
            if(i$.hasNext()) {
                Node tmp = (Node)i$.next();
                int[] ii = tmp.getRoole();
                this.axioma = ii[0];
            }

            this.terminals = this.createTerminals();
            this.nonterminals = this.createNonterminals();
            this.termLanguarge = this.createTerminalLang();
        }
    }

    public boolean isCreate() {
        return this.create;
    }

    public void setLocalkContext(LinkedList<LlkContext>[] localK) {
        this.LocalkContext = localK;
    }

    public LinkedList<LlkContext>[] getLocalkContext() {
        return this.LocalkContext;
    }

    public void leftRecusionTrace() {
        LinkedList lang = this.getLanguarge();
        int[] term = this.getTerminals();
        int[] epsilon = this.getEpsilonNonterminals();
        Iterator count = lang.iterator();

        while(count.hasNext()) {
            Node i$ = (Node)count.next();
            int[] tmp = i$.getRoole();
            if(tmp.length == 1) {
                i$.setTeg(1);
            } else if(i$.getRoole()[1] > 0) {
                i$.setTeg(1);
            } else {
                i$.setTeg(0);
            }
        }

        int var12 = 0;
        boolean isRecurtion = false;
        Iterator var13 = lang.iterator();

        while(true) {
            while(true) {
                Node var14;
                do {
                    if(!var13.hasNext()) {
                        if(!isRecurtion) {
                            System.out.println("В граматиці ліворекурсивні нетермінали відсутні ");
                        }

                        return;
                    }

                    var14 = (Node)var13.next();
                    ++var12;
                } while(var14.getTeg() == 1);

                int[] role = var14.getRoole();

                for(int ii = 1; ii < role.length && role[ii] <= 0; ++ii) {
                    MyLang.TmpList tree = new MyLang.TmpList((MyLang.TmpList)null, var14, ii);
                    if(tree.searchLeftRecursion(this)) {
                        tree.destroy();
                        isRecurtion = true;
                        break;
                    }

                    tree.destroy();

                    int ii1;
                    for(ii1 = 0; ii1 < epsilon.length && epsilon[ii1] != role[ii]; ++ii1) {
                        ;
                    }

                    if(ii1 == epsilon.length) {
                        break;
                    }
                }
            }
        }
    }

    public void rigthRecusionTrace() {
        LinkedList lang = this.getLanguarge();
        int[] term = this.getTerminals();
        int[] epsilon = this.getEpsilonNonterminals();
        boolean count = false;
        Iterator i$ = lang.iterator();

        Node tmp2;
        int[] role;
        while(i$.hasNext()) {
            tmp2 = (Node)i$.next();
            role = tmp2.getRoole();
            if(role.length == 1) {
                tmp2.setTeg(1);
            } else if(role[role.length - 1] > 0) {
                tmp2.setTeg(1);
            } else {
                tmp2.setTeg(0);
            }
        }

        int var12 = 0;
        boolean isRecurtion = false;
        i$ = lang.iterator();

        while(true) {
            while(true) {
                do {
                    if(!i$.hasNext()) {
                        if(!isRecurtion) {
                            System.out.println("В граматиці праворекурсивні нетермінали відсутні ");
                        }

                        return;
                    }

                    tmp2 = (Node)i$.next();
                    ++var12;
                } while(tmp2.getTeg() == 1);

                role = tmp2.getRoole();

                for(int ii = role.length - 1; ii > 0 && role[ii] <= 0; --ii) {
                    MyLang.TmpList tree = new MyLang.TmpList((MyLang.TmpList)null, tmp2, ii);
                    if(tree.searchRigthRecursion(this)) {
                        tree.destroy();
                        isRecurtion = true;
                        break;
                    }

                    tree.destroy();

                    int ii1;
                    for(ii1 = 0; ii1 < epsilon.length && epsilon[ii1] != role[ii]; ++ii1) {
                        ;
                    }

                    if(ii1 == epsilon.length) {
                        break;
                    }
                }
            }
        }
    }

    public LinkedList<LlkContext>[] createLocalK() {
        LinkedList[] localK = null;
        LlkContext[] termLang = this.getLlkTrmContext();
        LlkContext[] first = this.getFirstK();
        int[] term = this.getTerminals();
        int[] nonterm = this.getNonTerminals();
        localK = new LinkedList[nonterm.length];
        LlkContext[] tmpLlk = new LlkContext[40];
        int[] mult = new int[40];
        int[] maxmult = new int[40];

        int ii;
        for(ii = 0; ii < nonterm.length; ++ii) {
            localK[ii] = null;
        }

        for(ii = 0; ii < nonterm.length && nonterm[ii] != this.axioma; ++ii) {
            ;
        }

        LlkContext rezult = new LlkContext();
        rezult.addWord(new int[0]);
        localK[ii] = new LinkedList();
        localK[ii].add(rezult);
        int iter = 0;

        boolean count;
        do {
            ++iter;
            System.out.println("Побудова множини Local: ітерація " + iter);
            count = false;

            label151:
            for(ii = 0; ii < nonterm.length; ++ii) {
                int nomrole = 0;
                Iterator i$ = this.language.iterator();

                while(true) {
                    int ii1;
                    Node tmp;
                    do {
                        if(!i$.hasNext()) {
                            continue label151;
                        }

                        tmp = (Node)i$.next();
                        ++nomrole;

                        for(ii1 = 0; ii1 < nonterm.length && nonterm[ii1] != tmp.getRoole()[0]; ++ii1) {
                            ;
                        }
                    } while(localK[ii1] == null);

                    int indexLeft = ii1;
                    int[] role = tmp.getRoole();

                    for(ii1 = 1; ii1 < role.length; ++ii1) {
                        if(role[ii1] == nonterm[ii]) {
                            int multcount = 0;

                            int j1;
                            for(int j = ii1 + 1; j < role.length; ++j) {
                                if(role[j] >= 0) {
                                    for(j1 = 0; j1 < term.length && term[j1] != role[j]; ++j1) {
                                        ;
                                    }

                                    tmpLlk[multcount++] = termLang[j1];
                                } else {
                                    for(j1 = 0; j1 < nonterm.length && nonterm[j1] != role[j]; ++j1) {
                                        ;
                                    }

                                    tmpLlk[multcount++] = first[j1];
                                }
                            }

                            Iterator i$1 = localK[indexLeft].iterator();

                            while(i$1.hasNext()) {
                                LlkContext tmp1 = (LlkContext)i$1.next();
                                tmpLlk[multcount] = tmp1;

                                for(j1 = 0; j1 < multcount + 1; ++j1) {
                                    mult[j1] = 0;
                                    maxmult[j1] = tmpLlk[j1].calcWords();
                                }

                                int realCalc = 0;

                                for(j1 = 0; j1 < multcount + 1; ++j1) {
                                    if(j1 == 0) {
                                        realCalc = tmpLlk[j1].minLengthWord();
                                    } else {
                                        int minLength = tmpLlk[j1].minLengthWord();
                                        if(realCalc >= this.getLlkConst()) {
                                            break;
                                        }

                                        realCalc += minLength;
                                    }
                                }

                                realCalc = j1;
                                rezult = new LlkContext();

                                do {
                                    int[] llkWord = this.newWord(this.getLlkConst(), tmpLlk, mult, realCalc);
                                    rezult.addWord(llkWord);
                                } while(this.newCalcIndex(mult, maxmult, realCalc));

                                if(localK[ii] == null) {
                                    localK[ii] = new LinkedList();
                                    localK[ii].add(rezult);
                                    count = true;
                                } else if(!this.belongToLlkContext(localK[ii], rezult)) {
                                    localK[ii].add(rezult);
                                    count = true;
                                }
                            }
                        }
                    }
                }
            }
        } while(count);

        return localK;
    }

    public void printLocalk() {
        LinkedList[] localK = this.getLocalkContext();
        int[] term = this.getTerminals();
        int[] nonterm = this.getNonTerminals();

        for(int ii = 0; ii < nonterm.length; ++ii) {
            System.out.println("Контекст Local для нетермінала " + this.getLexemaText(nonterm[ii]));
            Iterator i$ = localK[ii].iterator();

            while(i$.hasNext()) {
                LlkContext tmp = (LlkContext)i$.next();
                int count = tmp.calcWords();
                System.out.println("{ ");

                for(int ii1 = 0; ii1 < tmp.calcWords(); ++ii1) {
                    int[] word = tmp.getWord(ii1);
                    if(word.length == 0) {
                        System.out.print("Е-слово");
                    } else {
                        for(int ii2 = 0; ii2 < word.length; ++ii2) {
                            System.out.print(this.getLexemaText(word[ii2]) + " ");
                        }
                    }

                    System.out.println();
                }

                System.out.println("} ");
            }
        }

    }

    public boolean llkCondition() {
        boolean upr = true;
        int count = 0;
        LinkedList[] localK = this.getLocalkContext();
        LlkContext[] firstTerm = this.getLlkTrmContext();
        LlkContext[] firstNonterm = this.getFirstK();
        int[] term = this.getTerminals();
        int[] nonterm = this.getNonTerminals();
        LlkContext[] tmpLlk = new LlkContext[40];
        int[] mult = new int[40];
        int[] maxmult = new int[40];
        Iterator i$ = this.getLanguarge().iterator();

        label219:
        while(i$.hasNext()) {
            Node tmp = (Node)i$.next();
            ++count;
            int[] role = tmp.getRoole();
            int count1 = 0;
            Iterator i$1 = this.getLanguarge().iterator();

            label217:
            while(true) {
                int[] role1;
                do {
                    if(!i$1.hasNext()) {
                        continue label219;
                    }

                    Node tmp1 = (Node)i$1.next();
                    ++count1;
                    if(tmp == tmp1) {
                        continue label219;
                    }

                    role1 = tmp1.getRoole();
                } while(role[0] != role1[0]);

                int ii;
                for(ii = 0; ii < nonterm.length && role[0] != nonterm[ii]; ++ii) {
                    ;
                }

                Iterator i$2 = localK[ii].iterator();

                while(true) {
                    while(true) {
                        if(!i$2.hasNext()) {
                            continue label217;
                        }

                        LlkContext loctmp = (LlkContext)i$2.next();
                        int counMult = 0;

                        int j1;
                        int j2;
                        for(j1 = 1; j1 < role.length; ++j1) {
                            if(role[j1] > 0) {
                                for(j2 = 0; j2 < term.length && term[j2] != role[j1]; ++j2) {
                                    ;
                                }

                                tmpLlk[counMult++] = firstTerm[j2];
                            } else {
                                for(j2 = 0; j2 < nonterm.length && nonterm[j2] != role[j1]; ++j2) {
                                    ;
                                }

                                tmpLlk[counMult++] = firstNonterm[j2];
                            }
                        }

                        tmpLlk[counMult++] = loctmp;

                        for(j1 = 0; j1 < counMult; ++j1) {
                            mult[j1] = 0;
                            maxmult[j1] = tmpLlk[j1].calcWords();
                        }

                        int realCalc = 0;

                        int minLength;
                        for(j1 = 0; j1 < counMult; ++j1) {
                            if(j1 == 0) {
                                realCalc = tmpLlk[j1].minLengthWord();
                            } else {
                                minLength = tmpLlk[j1].minLengthWord();
                                if(realCalc >= this.getLlkConst()) {
                                    break;
                                }

                                realCalc += minLength;
                            }
                        }

                        realCalc = j1;
                        LlkContext result = new LlkContext();

                        int[] llkWord;
                        do {
                            llkWord = this.newWord(this.getLlkConst(), tmpLlk, mult, realCalc);
                            result.addWord(llkWord);
                        } while(this.newCalcIndex(mult, maxmult, realCalc));

                        counMult = 0;

                        for(j1 = 1; j1 < role1.length; ++j1) {
                            if(role1[j1] > 0) {
                                for(j2 = 0; j2 < term.length && term[j2] != role1[j1]; ++j2) {
                                    ;
                                }

                                tmpLlk[counMult++] = firstTerm[j2];
                            } else {
                                for(j2 = 0; j2 < nonterm.length && nonterm[j2] != role1[j1]; ++j2) {
                                    ;
                                }

                                tmpLlk[counMult++] = firstNonterm[j2];
                            }
                        }

                        tmpLlk[counMult++] = loctmp;

                        for(j1 = 0; j1 < counMult; ++j1) {
                            mult[j1] = 0;
                            maxmult[j1] = tmpLlk[j1].calcWords();
                        }

                        realCalc = 0;

                        for(j1 = 0; j1 < counMult; ++j1) {
                            if(j1 == 0) {
                                realCalc = tmpLlk[j1].minLengthWord();
                            } else {
                                minLength = tmpLlk[j1].minLengthWord();
                                if(realCalc >= this.getLlkConst()) {
                                    break;
                                }

                                realCalc += minLength;
                            }
                        }

                        realCalc = j1;
                        LlkContext result1 = new LlkContext();

                        do {
                            llkWord = this.newWord(this.getLlkConst(), tmpLlk, mult, realCalc);
                            result1.addWord(llkWord);
                        } while(this.newCalcIndex(mult, maxmult, realCalc));

                        for(j1 = 0; j1 < result.calcWords(); ++j1) {
                            if(result1.wordInContext(result.getWord(j1))) {
                                System.out.println("Пара правил (" + count + "," + count1 + ") не задовільняють LL(" + this.getLlkConst() + ")- умові");
                                upr = false;
                                break;
                            }
                        }
                    }
                }
            }
        }

        if(upr) {
            System.out.println("Граматика задовільняює LL(" + this.getLlkConst() + ")- умові");
        }

        return upr;
    }

    private boolean belongToLlkContext(LinkedList<LlkContext> list, LlkContext llk) {
        int llkCount = llk.calcWords();
        Iterator i$ = list.iterator();

        int ii;
        do {
            LlkContext tmp;
            do {
                if(!i$.hasNext()) {
                    return false;
                }

                tmp = (LlkContext)i$.next();
            } while(llkCount != tmp.calcWords());

            int tmpCount = tmp.calcWords();

            for(ii = 0; ii < llkCount && tmp.wordInContext(llk.getWord(ii)); ++ii) {
                ;
            }
        } while(ii != llkCount);

        return true;
    }

    public int indexNonterminal(int ssItem) {
        for(int ii = 0; ii < this.nonterminals.length; ++ii) {
            if(ssItem == this.nonterminals[ii]) {
                return ii;
            }
        }

        return 0;
    }

    public int indexTerminal(int ssItem) {
        for(int ii = 0; ii < this.terminals.length; ++ii) {
            if(ssItem == this.terminals[ii]) {
                return ii;
            }
        }

        return 0;
    }

    public int getLexemaCode(byte[] lexema, int lexemaLen, int lexemaClass) {
        Iterator i$ = this.lexemaTable.iterator();

        String ss;
        int ii;
        TableNode tmp;
        do {
            do {
                if(!i$.hasNext()) {
                    return -1;
                }

                tmp = (TableNode)i$.next();
                ss = tmp.getLexemaText();
            } while(ss.length() != lexemaLen);

            for(ii = 0; ii < ss.length() && ss.charAt(ii) == (char)lexema[ii]; ++ii) {
                ;
            }
        } while(ii != ss.length());

        return tmp.getLexemaCode();
    }

    public void printTerminals() {
        System.out.println("СПИСОК ТЕРМІНАЛІВ:");
        if(this.terminals != null) {
            for(int ii = 0; ii < this.terminals.length; ++ii) {
                System.out.println("" + (ii + 1) + "  " + this.terminals[ii] + "\t " + this.getLexemaText(this.terminals[ii]));
            }

        }
    }

    public void printNonterminals() {
        System.out.println("СПИСОК НЕТЕРМІНАЛІВ:");
        if(this.nonterminals != null) {
            for(int ii = 0; ii < this.nonterminals.length; ++ii) {
                System.out.println("" + (ii + 1) + "  " + this.nonterminals[ii] + "\t " + this.getLexemaText(this.nonterminals[ii]));
            }

        }
    }

    public int[][] getUprTable() {
        return this.uprTable;
    }

    public void setUprTable(int[][] upr) {
        this.uprTable = upr;
    }

    public LlkContext[] getFirstK() {
        return this.firstK;
    }

    public void setFirstK(LlkContext[] first) {
        this.firstK = first;
    }

    public LlkContext[] getFollowK() {
        return this.followK;
    }

    public void setFollowK(LlkContext[] follow) {
        this.followK = follow;
    }

    public void prpintRoole(Node nod) {
        int[] role = nod.getRoole();

        for(int ii = 0; ii < role.length; ++ii) {
            if(ii == 0) {
                System.out.print(this.getLexemaText(role[ii]) + " -> ");
            } else {
                System.out.print(" " + this.getLexemaText(role[ii]));
            }
        }

        System.out.println();
    }

    public boolean createNonProdRools() {
        if(this.getNonTerminals().length == 0) {
            return true;
        } else {
            int[] prodtmp = new int[this.getNonTerminals().length];
            int count = 0;
            Iterator i$ = this.language.iterator();

            Node tmp;
            while(i$.hasNext()) {
                tmp = (Node)i$.next();
                tmp.setTeg(0);
            }

            int ii;
            boolean upr;
            int[] rool1;
            label117:
            do {
                upr = false;
                i$ = this.language.iterator();

                while(true) {
                    int ii1;
                    do {
                        do {
                            if(!i$.hasNext()) {
                                continue label117;
                            }

                            tmp = (Node)i$.next();
                            rool1 = tmp.getRoole();
                        } while(tmp.getTeg() == 1);

                        for(ii = 1; ii < rool1.length; ++ii) {
                            if(rool1[ii] <= 0) {
                                for(ii1 = 0; ii1 < count && prodtmp[ii1] != rool1[ii]; ++ii1) {
                                    ;
                                }

                                if(ii1 == count) {
                                    break;
                                }
                            }
                        }
                    } while(ii != rool1.length);

                    for(ii1 = 0; ii1 < count && prodtmp[ii1] != rool1[0]; ++ii1) {
                        ;
                    }

                    if(ii1 == count) {
                        prodtmp[count++] = rool1[0];
                    }

                    tmp.setTeg(1);
                    upr = true;
                }
            } while(upr);

            if(count == prodtmp.length) {
                System.out.print("В граматиці непродуктивні правила відсутні\n");
                return true;
            } else {
                System.out.print("Непродуктивні правила: \n");
                i$ = this.language.iterator();

                while(true) {
                    do {
                        if(!i$.hasNext()) {
                            return false;
                        }

                        tmp = (Node)i$.next();
                    } while(tmp.getTeg() == 1);

                    rool1 = tmp.getRoole();

                    for(ii = 0; ii < rool1.length; ++ii) {
                        if(ii == 1) {
                            System.out.print(" ::= ");
                        }

                        System.out.print(this.getLexemaText(rool1[ii]) + " ");
                        if(rool1.length == 1) {
                            System.out.print(" ::= ");
                        }
                    }

                    System.out.println("");
                }
            }
        }
    }

    public boolean createNonDosNeterminals() {
        int[] nonTerminals = this.getNonTerminals();
        int[] dosnerminals = new int[nonTerminals.length];
        int count = 0;
        boolean iter = false;
        if(nonTerminals == null) {
            return true;
        } else {
            Iterator i$ = this.language.iterator();
            Node tmp;
            if(i$.hasNext()) {
                tmp = (Node)i$.next();
                dosnerminals[0] = tmp.getRoole()[0];
                count = 1;
            }

            boolean upr;
            int ii;
            int ii1;
            label109:
            do {
                upr = false;
                i$ = this.language.iterator();

                while(true) {
                    int[] rool1;
                    do {
                        if(!i$.hasNext()) {
                            continue label109;
                        }

                        tmp = (Node)i$.next();
                        rool1 = tmp.getRoole();

                        for(ii = 0; ii < count && dosnerminals[ii] != rool1[0]; ++ii) {
                            ;
                        }
                    } while(ii == count);

                    for(ii = 1; ii < rool1.length; ++ii) {
                        if(rool1[ii] < 0) {
                            for(ii1 = 0; ii1 < count && dosnerminals[ii1] != rool1[ii]; ++ii1) {
                                ;
                            }

                            if(ii1 == count) {
                                dosnerminals[count] = rool1[ii];
                                upr = true;
                                ++count;
                            }
                        }
                    }
                }
            } while(upr);

            int var12 = nonTerminals.length - count;
            if(var12 == 0) {
                System.out.println("В граматиці недосяжних нетерміналів немає");
                return true;
            } else {
                int[] nonDosNeterminals = new int[var12];
                var12 = 0;

                for(ii = 0; ii < nonTerminals.length; ++ii) {
                    for(ii1 = 0; ii1 < count && nonTerminals[ii] != dosnerminals[ii1]; ++ii1) {
                        ;
                    }

                    if(ii1 == count) {
                        nonDosNeterminals[var12++] = nonTerminals[ii];
                    }
                }

                for(ii = 0; ii < nonDosNeterminals.length; ++ii) {
                    System.out.println("Недосяжний нетермінал: " + this.getLexemaText(nonDosNeterminals[ii]) + "\n ");
                }

                return false;
            }
        }
    }

    public boolean leftRecursNonnerminal() {
        int[] controlSet = new int[this.getNonTerminals().length];
        int[] nontrm = this.getNonTerminals();
        int[] eps = this.getEpsilonNonterminals();
        boolean upr = false;
        boolean upr1 = false;

        for(int ii = 0; ii < nontrm.length; ++ii) {
            int count = 0;
            int count1 = 1;
            upr = false;
            controlSet[count] = nontrm[ii];

            do {
                Iterator i$ = this.language.iterator();

                label94:
                while(true) {
                    int[] rool1;
                    do {
                        if(!i$.hasNext()) {
                            break label94;
                        }

                        Node tmp = (Node)i$.next();
                        rool1 = tmp.getRoole();
                    } while(rool1[0] != controlSet[count]);

                    int ii1;
                    for(ii1 = 1; ii1 < rool1.length && rool1[ii1] <= 0 && rool1[ii1] != controlSet[0]; ++ii1) {
                        int ii2;
                        for(ii2 = 0; ii2 < count1 && rool1[ii1] != controlSet[ii2]; ++ii2) {
                            ;
                        }

                        if(ii2 == count1) {
                            controlSet[count1++] = rool1[ii1];
                        }

                        if(eps == null) {
                            break;
                        }

                        for(ii2 = 0; ii2 < eps.length && rool1[ii1] != eps[ii2]; ++ii2) {
                            ;
                        }

                        if(ii2 == eps.length) {
                            break;
                        }
                    }

                    if(ii1 != rool1.length && rool1[ii1] == controlSet[0]) {
                        System.out.print("Нетермінал: " + this.getLexemaText(controlSet[0]) + " ліворекурсивний \n");
                        upr = true;
                        upr1 = true;
                        break;
                    }
                }

                if(upr) {
                    break;
                }

                ++count;
            } while(count < count1);
        }

        if(!upr1) {
            System.out.print("В граматиці відсутні ліворекурсивні нетермінали \n");
            return false;
        } else {
            return true;
        }
    }

    public boolean rightRecursNonnerminal() {
        int[] controlSet = new int[this.getNonTerminals().length];
        int[] nontrm = this.getNonTerminals();
        int[] eps = this.getEpsilonNonterminals();
        boolean upr = false;
        boolean upr1 = false;

        for(int ii = 0; ii < nontrm.length; ++ii) {
            int count = 0;
            int count1 = 1;
            upr = false;
            controlSet[count] = nontrm[ii];

            do {
                Iterator i$ = this.language.iterator();

                label94:
                while(true) {
                    int[] rool1;
                    do {
                        if(!i$.hasNext()) {
                            break label94;
                        }

                        Node tmp = (Node)i$.next();
                        rool1 = tmp.getRoole();
                    } while(rool1[0] != controlSet[count]);

                    int ii1;
                    for(ii1 = rool1.length - 1; ii1 > 0 && rool1[ii1] <= 0 && rool1[ii1] != controlSet[0]; --ii1) {
                        int ii2;
                        for(ii2 = 0; ii2 < count1 && rool1[ii1] != controlSet[ii2]; ++ii2) {
                            ;
                        }

                        if(ii2 == count1) {
                            controlSet[count1++] = rool1[ii1];
                        }

                        if(eps == null) {
                            break;
                        }

                        for(ii2 = 0; ii2 < eps.length && rool1[ii1] != eps[ii2]; ++ii2) {
                            ;
                        }

                        if(ii2 == eps.length) {
                            break;
                        }
                    }

                    if(ii1 != 0 && rool1[ii1] == controlSet[0]) {
                        System.out.print("Нетермінал: " + this.getLexemaText(controlSet[0]) + " праворекурсивний \n");
                        upr = true;
                        upr1 = true;
                        break;
                    }
                }

                if(upr) {
                    break;
                }

                ++count;
            } while(count < count1);
        }

        if(!upr1) {
            System.out.print("В граматиці відсутні праворекурсивні нетермінали \n");
            return false;
        } else {
            return true;
        }
    }

    public LlkContext[] firstK() {
        int[] llkWord = new int[10];
        boolean upr = false;
        int[] term = this.getTerminals();
        int[] nonterm = this.getNonTerminals();
        LlkContext[] llkTrmContext = this.getLlkTrmContext();
        LlkContext[] rezult = new LlkContext[nonterm.length];
        LlkContext[] tmpLlk = new LlkContext[40];
        int[] mult = new int[40];
        int[] maxmult = new int[40];
        int iter = 0;

        int ii;
        for(ii = 0; ii < rezult.length; ++ii) {
            rezult[ii] = new LlkContext();
        }

        label125:
        do {
            upr = false;
            boolean prav = false;
            PrintStream var10000 = System.out;
            StringBuilder var10001 = (new StringBuilder()).append("Ітерація пошуку множини FirstK ");
            ++iter;
            var10000.println(var10001.append(iter).toString());
            Iterator i$ = this.language.iterator();

            while(true) {
                while(true) {
                    if(!i$.hasNext()) {
                        continue label125;
                    }

                    Node tmp = (Node)i$.next();
                    int[] tmprole = tmp.getRoole();

                    for(ii = 0; ii < nonterm.length && tmprole[0] != nonterm[ii]; ++ii) {
                        ;
                    }

                    if(tmprole.length == 1) {
                        if(rezult[ii].addWord(new int[0])) {
                            upr = true;
                        }
                    } else {
                        int ii0;
                        int ii1;
                        for(ii0 = 1; ii0 < tmprole.length; ++ii0) {
                            if(tmprole[ii0] > 0) {
                                for(ii1 = 0; ii1 < term.length && term[ii1] != tmprole[ii0]; ++ii1) {
                                    ;
                                }

                                tmpLlk[ii0 - 1] = llkTrmContext[ii1];
                            } else {
                                for(ii1 = 0; ii1 < nonterm.length && nonterm[ii1] != tmprole[ii0]; ++ii1) {
                                    ;
                                }

                                if(rezult[ii1].calcWords() == 0) {
                                    break;
                                }

                                tmpLlk[ii0 - 1] = rezult[ii1];
                            }
                        }

                        if(ii0 == tmprole.length) {
                            int multCount = tmprole.length - 1;

                            for(ii1 = 0; ii1 < multCount; ++ii1) {
                                mult[ii1] = 0;
                                maxmult[ii1] = tmpLlk[ii1].calcWords();
                            }

                            int realCalc = 0;

                            for(ii1 = 0; ii1 < multCount; ++ii1) {
                                if(ii1 == 0) {
                                    realCalc = tmpLlk[ii1].minLengthWord();
                                } else {
                                    int minLength = tmpLlk[ii1].minLengthWord();
                                    if(realCalc >= this.getLlkConst()) {
                                        break;
                                    }

                                    realCalc += minLength;
                                }
                            }

                            realCalc = ii1;

                            while(true) {
                                llkWord = this.newWord(this.getLlkConst(), tmpLlk, mult, realCalc);
                                if(rezult[ii].addWord(llkWord)) {
                                    upr = true;
                                }

                                if(!this.newCalcIndex(mult, maxmult, realCalc)) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } while(upr);

        System.out.println("Кінець пошуку множин FIRSTk");
        return rezult;
    }

    public LlkContext[] followK() {
        LinkedList lan = this.getLanguarge();
        LlkContext[] firstK = this.getFirstK();
        int[] term = this.getTerminals();
        int[] nonterm = this.getNonTerminals();
        LlkContext[] llkTrmContext = this.getLlkTrmContext();
        LlkContext[] rezult = new LlkContext[nonterm.length];
        LlkContext[] tmpLlk = new LlkContext[40];
        int[] mult = new int[40];
        int[] maxmult = new int[40];
        int iter = 0;

        int ii;
        for(ii = 0; ii < rezult.length; ++ii) {
            rezult[ii] = new LlkContext();
        }

        int aaxioma = this.getAxioma();

        for(ii = 0; ii < nonterm.length && nonterm[ii] != aaxioma; ++ii) {
            ;
        }

        rezult[ii].addWord(new int[0]);

        boolean upr;
        label149:
        do {
            upr = false;
            boolean prav = false;
            PrintStream var10000 = System.out;
            StringBuilder var10001 = (new StringBuilder()).append("Ітерація побудово множини FollowK ");
            ++iter;
            var10000.println(var10001.append(iter).toString());
            Iterator i$ = lan.iterator();

            while(true) {
                int[] tmprole;
                do {
                    if(!i$.hasNext()) {
                        continue label149;
                    }

                    Node tmp = (Node)i$.next();
                    tmprole = tmp.getRoole();

                    for(ii = 0; ii < nonterm.length && tmprole[0] != nonterm[ii]; ++ii) {
                        ;
                    }

                    if(ii == nonterm.length) {
                        return null;
                    }
                } while(rezult[ii].calcWords() == 0);

                int leftItem = ii;

                for(int jj = 1; jj < tmprole.length; ++jj) {
                    if(tmprole[jj] <= 0) {
                        int multCount = 0;

                        int ii1;
                        for(int ii0 = jj + 1; ii0 < tmprole.length; ++ii0) {
                            if(tmprole[ii0] > 0) {
                                for(ii1 = 0; ii1 < term.length && term[ii1] != tmprole[ii0]; ++ii1) {
                                    ;
                                }

                                tmpLlk[multCount++] = llkTrmContext[ii1];
                            } else {
                                for(ii1 = 0; ii1 < nonterm.length && nonterm[ii1] != tmprole[ii0]; ++ii1) {
                                    ;
                                }

                                tmpLlk[multCount++] = firstK[ii1];
                            }
                        }

                        tmpLlk[multCount++] = rezult[leftItem];

                        for(ii1 = 0; ii1 < multCount; ++ii1) {
                            mult[ii1] = 0;
                            maxmult[ii1] = tmpLlk[ii1].calcWords();
                        }

                        int realCalc = 0;

                        for(ii1 = 0; ii1 < multCount; ++ii1) {
                            if(ii1 == 0) {
                                realCalc = tmpLlk[ii1].minLengthWord();
                            } else {
                                int minLength = tmpLlk[ii1].minLengthWord();
                                if(realCalc >= this.getLlkConst()) {
                                    break;
                                }

                                realCalc += minLength;
                            }
                        }

                        realCalc = ii1;

                        for(ii1 = 0; ii1 < nonterm.length && nonterm[ii1] != tmprole[jj]; ++ii1) {
                            ;
                        }

                        while(true) {
                            int[] llkWord = this.newWord(this.getLlkConst(), tmpLlk, mult, realCalc);
                            if(rezult[ii1].addWord(llkWord)) {
                                upr = true;
                            }

                            if(!this.newCalcIndex(mult, maxmult, realCalc)) {
                                break;
                            }
                        }
                    }
                }
            }
        } while(upr);

        System.out.println("Кінець пошуку множин FollowK");
        return rezult;
    }

    public void firstFollowK() {
        int[] term = this.getTerminals();
        int[] nonterm = this.getNonTerminals();
        LlkContext[] llkTrmContext = this.getLlkTrmContext();
        LlkContext[] tmpLlk = new LlkContext[40];
        int[] mult = new int[40];
        int[] maxmult = new int[40];
        int roleNumb = 0;
        Iterator i$ = this.getLanguarge().iterator();

        while(i$.hasNext()) {
            Node tmp = (Node)i$.next();
            ++roleNumb;
            int[] tmprole = tmp.getRoole();
            int multCount = 0;

            int ii1;
            for(int ii = 1; ii < tmprole.length; ++ii) {
                if(tmprole[ii] > 0) {
                    for(ii1 = 0; ii1 < term.length && term[ii1] != tmprole[ii]; ++ii1) {
                        ;
                    }

                    tmpLlk[multCount] = llkTrmContext[ii1];
                } else {
                    for(ii1 = 0; ii1 < nonterm.length && nonterm[ii1] != tmprole[ii]; ++ii1) {
                        ;
                    }

                    tmpLlk[multCount] = this.firstK[ii1];
                }

                ++multCount;
            }

            for(ii1 = 0; ii1 < nonterm.length && nonterm[ii1] != tmprole[0]; ++ii1) {
                ;
            }

            tmpLlk[multCount++] = this.followK[ii1];

            for(ii1 = 0; ii1 < multCount; ++ii1) {
                mult[ii1] = 0;
                maxmult[ii1] = tmpLlk[ii1].calcWords();
            }

            int realCalc = 0;

            for(ii1 = 0; ii1 < multCount; ++ii1) {
                if(ii1 == 0) {
                    realCalc = tmpLlk[ii1].minLengthWord();
                } else {
                    int minLength = tmpLlk[ii1].minLengthWord();
                    if(realCalc >= this.getLlkConst()) {
                        break;
                    }

                    realCalc += minLength;
                }
            }

            realCalc = ii1;
            LlkContext rezult = new LlkContext();

            do {
                int[] llkWord = this.newWord(this.getLlkConst(), tmpLlk, mult, realCalc);
                rezult.addWord(llkWord);
            } while(this.newCalcIndex(mult, maxmult, realCalc));

            tmp.addFirstFollowK(rezult);
        }

        System.out.println("Множини FirstK(w)+ FollowK(A): А->w побудовані ");
    }

    public void firstKforRoole() {
        int[] term = this.getTerminals();
        int[] nonterm = this.getNonTerminals();
        LlkContext[] llkTrmContext = this.getLlkTrmContext();
        LlkContext[] tmpLlk = new LlkContext[40];
        int[] mult = new int[40];
        int[] maxmult = new int[40];
        int roleNumb = 0;
        Iterator i$ = this.getLanguarge().iterator();

        while(true) {
            while(i$.hasNext()) {
                Node tmp = (Node)i$.next();
                ++roleNumb;
                int[] tmprole = tmp.getRoole();
                boolean multCount = false;
                LlkContext rezult;
                if(tmprole.length == 1) {
                    rezult = new LlkContext();
                    rezult.addWord(new int[0]);
                    tmp.addFirstKforRoole(rezult);
                } else {
                    int var18 = 0;

                    int ii1;
                    for(int ii = 1; ii < tmprole.length; ++var18) {
                        if(tmprole[ii] > 0) {
                            for(ii1 = 0; ii1 < term.length && term[ii1] != tmprole[ii]; ++ii1) {
                                ;
                            }

                            tmpLlk[var18] = llkTrmContext[ii1];
                        } else {
                            for(ii1 = 0; ii1 < nonterm.length && nonterm[ii1] != tmprole[ii]; ++ii1) {
                                ;
                            }

                            tmpLlk[var18] = this.firstK[ii1];
                        }

                        ++ii;
                    }

                    for(ii1 = 0; ii1 < var18; ++ii1) {
                        mult[ii1] = 0;
                        maxmult[ii1] = tmpLlk[ii1].calcWords();
                    }

                    int realCalc = 0;

                    for(ii1 = 0; ii1 < var18; ++ii1) {
                        if(ii1 == 0) {
                            realCalc = tmpLlk[ii1].minLengthWord();
                        } else {
                            int minLength = tmpLlk[ii1].minLengthWord();
                            if(realCalc >= this.getLlkConst()) {
                                break;
                            }

                            realCalc += minLength;
                        }
                    }

                    realCalc = ii1;
                    rezult = new LlkContext();

                    do {
                        int[] llkWord = this.newWord(this.getLlkConst(), tmpLlk, mult, realCalc);
                        rezult.addWord(llkWord);
                    } while(this.newCalcIndex(mult, maxmult, realCalc));

                    tmp.addFirstKforRoole(rezult);
                }
            }

            System.out.println("Множини FirstK(w): А->w побудовані ");
            return;
        }
    }

    public void printFirstContextForRoole() {
        int[] nonterm = this.getNonTerminals();
        int number = 0;
        Iterator i$ = this.getLanguarge().iterator();

        while(true) {
            LlkContext rezultForRoole;
            do {
                if(!i$.hasNext()) {
                    return;
                }

                Node tmp = (Node)i$.next();
                ++number;
                rezultForRoole = tmp.getFirstKforRoole();
            } while(rezultForRoole == null);

            System.out.println("FirstK(W), W - права частина правила " + number);

            for(int ii = 0; ii < rezultForRoole.calcWords(); ++ii) {
                int[] word = rezultForRoole.getWord(ii);
                if(word.length == 0) {
                    System.out.print("Е-слово");
                } else {
                    for(int ii1 = 0; ii1 < word.length; ++ii1) {
                        System.out.print(this.getLexemaText(word[ii1]) + " ");
                    }
                }

                System.out.println();
            }
        }
    }

    public void printFirstkContext() {
        int[] nonterm = this.getNonTerminals();
        LlkContext[] firstContext = this.getFirstK();
        if(firstContext != null) {
            for(int j = 0; j < nonterm.length; ++j) {
                System.out.println("FirstK-контекст для нетермінала: " + this.getLexemaText(nonterm[j]));
                LlkContext tmp = firstContext[j];

                for(int ii = 0; ii < tmp.calcWords(); ++ii) {
                    int[] word = tmp.getWord(ii);
                    if(word.length == 0) {
                        System.out.print("Е-слово");
                    } else {
                        for(int ii1 = 0; ii1 < word.length; ++ii1) {
                            System.out.print(this.getLexemaText(word[ii1]) + " ");
                        }
                    }

                    System.out.println();
                }
            }

        }
    }

    public void printFollowkContext() {
        int[] nonterm = this.getNonTerminals();
        LlkContext[] followContext = this.getFollowK();

        for(int j = 0; j < nonterm.length; ++j) {
            System.out.println("FollowK-контекст для нетермінала: " + this.getLexemaText(nonterm[j]));
            LlkContext tmp = followContext[j];

            for(int ii = 0; ii < tmp.calcWords(); ++ii) {
                int[] word = tmp.getWord(ii);
                if(word.length == 0) {
                    System.out.print("Е-слово");
                } else {
                    for(int ii1 = 0; ii1 < word.length; ++ii1) {
                        System.out.print(this.getLexemaText(word[ii1]) + " ");
                    }
                }

                System.out.println();
            }
        }

    }

    public void printFirstFollowK() {
        int count = 0;
        Iterator i$ = this.getLanguarge().iterator();

        while(i$.hasNext()) {
            Node tmp = (Node)i$.next();
            ++count;
            System.out.print("Правило №" + count + " ");
            this.prpintRoole(tmp);
            LlkContext loc = tmp.getFirstFollowK();

            for(int ii1 = 0; ii1 < loc.calcWords(); ++ii1) {
                int[] word = loc.getWord(ii1);
                if(word.length == 0) {
                    System.out.print("Е-слово");
                } else {
                    for(int ii2 = 0; ii2 < word.length; ++ii2) {
                        System.out.print(this.getLexemaText(word[ii2]) + " ");
                    }
                }

                System.out.println();
            }
        }

    }

    public void printFirstFollowForRoole() {
        boolean count = false;
        byte[] readline = new byte[80];
        int rooleNumber = 0;

        while(true) {
            while(true) {
                System.out.println("Введіть номер правила або end");

                try {
                    int textLen = System.in.read(readline);
                    String StrTmp = new String(readline, 0, textLen, "ISO-8859-1");
                    if(StrTmp.trim().equals("end")) {
                        return;
                    }

                    rooleNumber = (new Integer(StrTmp.trim())).intValue();
                } catch (Exception var12) {
                    System.out.println("Невірний код дії, повторіть: ");
                }

                int var13 = 0;
                Iterator i$ = this.getLanguarge().iterator();

                while(i$.hasNext()) {
                    Node tmp = (Node)i$.next();
                    ++var13;
                    if(var13 == rooleNumber) {
                        this.prpintRoole(tmp);
                        LlkContext loc = tmp.getFirstFollowK();

                        for(int ii1 = 0; ii1 < loc.calcWords(); ++ii1) {
                            int[] word = loc.getWord(ii1);
                            if(word.length == 0) {
                                System.out.print("Е-слово");
                            } else {
                                for(int ii2 = 0; ii2 < word.length; ++ii2) {
                                    System.out.print(this.getLexemaText(word[ii2]) + " ");
                                }
                            }

                            System.out.println();
                        }
                        break;
                    }
                }
            }
        }
    }

    public boolean strongLlkCondition() {
        boolean upr = true;
        int count = 0;
        Iterator i$ = this.getLanguarge().iterator();

        label46:
        while(i$.hasNext()) {
            Node tmp = (Node)i$.next();
            ++count;
            int[] role = tmp.getRoole();
            LlkContext cont = tmp.getFirstFollowK();
            int count1 = 0;
            Iterator i$1 = this.getLanguarge().iterator();

            while(true) {
                while(true) {
                    Node tmp1;
                    int[] role1;
                    do {
                        if(!i$1.hasNext()) {
                            continue label46;
                        }

                        tmp1 = (Node)i$1.next();
                        ++count1;
                        if(tmp == tmp1) {
                            continue label46;
                        }

                        role1 = tmp1.getRoole();
                    } while(role[0] != role1[0]);

                    LlkContext cont1 = tmp1.getFirstFollowK();

                    for(int ii = 0; ii < cont.calcWords(); ++ii) {
                        if(cont1.wordInContext(cont.getWord(ii))) {
                            System.out.println("" + role[0] + " " + this.getLexemaText(role[0]) + " -пара правил (" + count + "," + count1 + ") не задовольняють сильній LL(" + this.getLlkConst() + ")- умові");
                            upr = false;
                            break;
                        }
                    }
                }
            }
        }

        if(upr) {
            System.out.println("Граматика задовільняює сильній LL(" + this.getLlkConst() + ")- умові");
        }

        return upr;
    }

    public int[][] createUprTable() {
        int[] term = this.getTerminals();
        int[] nonterm = this.getNonTerminals();
        if(this.LLK != 1) {
            System.out.println("Спроба побудувати таблицю управління для k=" + this.LLK);
            return (int[][])null;
        } else {
            int[][] upr = new int[nonterm.length][term.length + 1];

            int ii;
            for(int ii1 = 0; ii1 < nonterm.length; ++ii1) {
                for(ii = 0; ii < term.length + 1; ++ii) {
                    upr[ii1][ii] = 0;
                }
            }

            int rowline = 0;
            Iterator i$ = this.getLanguarge().iterator();

            while(i$.hasNext()) {
                Node tmp = (Node)i$.next();
                ++rowline;
                int[] role = tmp.getRoole();
                int rowindex = this.indexNonterminal(role[0]);
                LlkContext cont = tmp.getFirstFollowK();

                for(ii = 0; ii < cont.calcWords(); ++ii) {
                    int[] word = cont.getWord(ii);
                    int colindex;
                    if(word.length == 0) {
                        colindex = this.getTerminals().length;
                    } else {
                        colindex = this.indexTerminal(word[0]);
                    }

                    if(upr[rowindex][colindex] != 0) {
                        System.out.println("При побудові таблиці управління для нетермінала " + this.getLexemaText(nonterm[rowindex]) + " порушшено LL(1)-властивість");
                        return (int[][])null;
                    }

                    upr[rowindex][colindex] = rowline;
                }
            }

            System.out.println("Таблиця управління LL(1)-аналізу побудована ");
            return upr;
        }
    }

    public int[][] createUprTableWithCollision() {
        int[] term = this.getTerminals();
        int[] nonterm = this.getNonTerminals();
        if(this.LLK != 1) {
            System.out.println("Спроба побудувати таблицю управління для k=" + this.LLK);
            return (int[][])null;
        } else {
            int[][] upr = new int[nonterm.length][term.length + 1];

            int ii;
            for(int ii1 = 0; ii1 < nonterm.length; ++ii1) {
                for(ii = 0; ii < term.length + 1; ++ii) {
                    upr[ii1][ii] = 0;
                }
            }

            int rowline = 0;
            Iterator i$ = this.getLanguarge().iterator();

            while(i$.hasNext()) {
                Node tmp = (Node)i$.next();
                ++rowline;
                int[] role = tmp.getRoole();
                int rowindex = this.indexNonterminal(role[0]);
                LlkContext cont = tmp.getFirstFollowK();

                for(ii = 0; ii < cont.calcWords(); ++ii) {
                    int[] word = cont.getWord(ii);
                    boolean colindex = false;
                    int var16;
                    if(word.length == 0) {
                        var16 = this.getTerminals().length;
                    } else {
                        var16 = this.indexTerminal(word[0]);
                    }

                    if(upr[rowindex][var16] != 0) {
                        upr[rowindex][var16] = -1;
                    } else {
                        upr[rowindex][var16] = rowline;
                    }
                }
            }

            System.out.println("Таблиця управління LL(1)-аналізу побудована ");
            return upr;
        }
    }

    private boolean newCalcIndex(int[] mult, int[] maxmult, int realCalc) {
        for(int ii = realCalc - 1; ii >= 0; --ii) {
            if(mult[ii] + 1 != maxmult[ii]) {
                ++mult[ii];
                return true;
            }

            mult[ii] = 0;
        }

        return false;
    }

    private int[] newWord(int LLK, LlkContext[] tmp, int[] mult, int realCalc) {
        int[] word = new int[LLK];
        int llkTmp = 0;

        int ii;
        for(ii = 0; ii < realCalc; ++ii) {
            int[] wordtmp = tmp[ii].getWord(mult[ii]);

            for(int word1 = 0; word1 < wordtmp.length && llkTmp != LLK; ++word1) {
                word[llkTmp++] = wordtmp[word1];
            }

            if(llkTmp == LLK) {
                break;
            }
        }

        int[] var10 = new int[llkTmp];

        for(ii = 0; ii < llkTmp; ++ii) {
            var10[ii] = word[ii];
        }

        return var10;
    }

    public int getLlkConst() {
        return this.LLK;
    }

    private int getLexemaCode(String lexema, int lexemaClass) {
        Iterator i$ = this.lexemaTable.iterator();

        TableNode tmp;
        do {
            if(!i$.hasNext()) {
                return 0;
            }

            tmp = (TableNode)i$.next();
        } while(!tmp.getLexemaText().equals(lexema) || (tmp.getLexemaCode() & -16777216) != lexemaClass);

        return tmp.getLexemaCode();
    }

    public LlkContext[] getLlkTrmContext() {
        return this.termLanguarge;
    }

    private LlkContext[] createTerminalLang() {
        LlkContext[] cont = new LlkContext[this.terminals.length];

        for(int ii = 0; ii < this.terminals.length; ++ii) {
            int[] trmWord = new int[]{this.terminals[ii]};
            cont[ii] = new LlkContext();
            cont[ii].addWord(trmWord);
        }

        return cont;
    }

    public String getLexemaText(int code) {
        Iterator i$ = this.lexemaTable.iterator();

        TableNode tmp;
        do {
            if(!i$.hasNext()) {
                return null;
            }

            tmp = (TableNode)i$.next();
        } while(tmp.getLexemaCode() != code);

        return tmp.getLexemaText();
    }

    public int[] getTerminals() {
        return this.terminals;
    }

    public int[] getNonTerminals() {
        return this.nonterminals;
    }

    public int[] getEpsilonNonterminals() {
        return this.epsilonNerminals;
    }

    public LinkedList<Node> getLanguarge() {
        return this.language;
    }

    public void printEpsilonNonterminals() {
        System.out.println("СПИСОК E-терміналів:");
        if(this.epsilonNerminals != null) {
            for(int ii = 0; ii < this.epsilonNerminals.length; ++ii) {
                System.out.println("" + this.epsilonNerminals[ii] + "\t" + this.getLexemaText(this.epsilonNerminals[ii]));
            }

        }
    }

    public void setEpsilonNonterminals(int[] eps) {
        this.epsilonNerminals = eps;
    }

    public int[] createEpsilonNonterminals() {
        int[] terminal = new int[this.nonterminals.length];
        int count = 0;
        Iterator rezult = this.language.iterator();

        Node tmp;
        while(rezult.hasNext()) {
            tmp = (Node)rezult.next();
            tmp.setTeg(0);
        }

        boolean upr;
        int ii;
        label78:
        do {
            upr = false;
            rezult = this.language.iterator();

            while(true) {
                int[] role;
                do {
                    if(!rezult.hasNext()) {
                        continue label78;
                    }

                    tmp = (Node)rezult.next();
                    role = tmp.getRoole();

                    for(ii = 1; ii < role.length && role[ii] <= 0; ++ii) {
                        int ii1;
                        for(ii1 = 0; ii1 < count && terminal[ii1] != role[ii]; ++ii1) {
                            ;
                        }

                        if(ii1 == count) {
                            break;
                        }
                    }
                } while(ii != role.length);

                int ii2;
                for(ii2 = 0; ii2 < count && terminal[ii2] != role[0]; ++ii2) {
                    ;
                }

                if(ii2 == count) {
                    terminal[count++] = role[0];
                    upr = true;
                }
            }
        } while(upr);

        int[] var10 = new int[count];

        for(ii = 0; ii < count; ++ii) {
            var10[ii] = terminal[ii];
        }

        return var10;
    }

    public void printGramma() {
        int count = 0;
        Iterator i$ = this.language.iterator();

        while(i$.hasNext()) {
            Node tmp = (Node)i$.next();
            int[] roole = tmp.getRoole();
            ++count;
            System.out.print("" + count + " ");

            for(int ii = 0; ii < roole.length; ++ii) {
                if(ii == 1) {
                    System.out.print(" -> ");
                }

                System.out.print(this.getLexemaText(roole[ii]) + " ");
                if(roole.length == 1) {
                    System.out.print(" -> ");
                }
            }

            System.out.println("");
        }

    }

    private int[] createTerminals() {
        int count = 0;
        Iterator i$ = this.lexemaTable.iterator();

        TableNode tmp;
        while(i$.hasNext()) {
            tmp = (TableNode)i$.next();
            if(tmp.getLexemaCode() > 0) {
                ++count;
            }
        }

        int[] terminal = new int[count];
        count = 0;
        i$ = this.lexemaTable.iterator();

        while(i$.hasNext()) {
            tmp = (TableNode)i$.next();
            if(tmp.getLexemaCode() > 0) {
                terminal[count] = tmp.getLexemaCode();
                ++count;
            }
        }

        return terminal;
    }

    private int[] createNonterminals() {
        int count = 0;
        Iterator i$ = this.lexemaTable.iterator();

        TableNode tmp;
        while(i$.hasNext()) {
            tmp = (TableNode)i$.next();
            if(tmp.getLexemaCode() < 0) {
                ++count;
            }
        }

        int[] nonterminal = new int[count];
        count = 0;
        i$ = this.lexemaTable.iterator();

        while(i$.hasNext()) {
            tmp = (TableNode)i$.next();
            if(tmp.getLexemaCode() < 0) {
                nonterminal[count] = tmp.getLexemaCode();
                ++count;
            }
        }

        return nonterminal;
    }

    private void setAxioma(int axiom0) {
        this.axioma = axiom0;
    }

    public int getAxioma() {
        return this.axioma;
    }

    private void readGrammat(String fname) {
        char[] lexema = new char[180];
        int[] roole = new int[80];

        BufferedReader s;
        try {
            s = new BufferedReader(new FileReader(fname.trim()));
        } catch (FileNotFoundException var24) {
            System.out.print("Файл:" + fname.trim() + " не відкрито\n");
            this.create = false;
            return;
        }

        for(int pravilo = 0; pravilo < lexema.length; ++pravilo) {
            lexema[pravilo] = 0;
        }

        int[] var27 = new int[80];

        int line;
        for(line = 0; line < var27.length; ++line) {
            var27[line] = 0;
        }

        int pos = 0;
        boolean poslex = false;
        byte q = 0;
        boolean leftLexema = false;
        int posRoole = 0;
        line = 0;
        String readline = null;
        int readpos = 0;
        int readlen = 0;

        try {
            int newLexemaCode;
            TableNode nodeTmp;
            Node nod;
            while(s.ready()) {
                if(readline == null || readpos >= readlen) {
                    readline = s.readLine();
                    if(line == 0 && readline.charAt(0) == '\ufeff') {
                        readline = readline.substring(1);
                    }

                    readlen = readline.length();
                    ++line;
                }

                for(readpos = 0; readpos < readlen; ++readpos) {
                    char litera = readline.charAt(readpos);
                    String e;
                    boolean strTmp;
                    Iterator ii;
                    TableNode i$;
                    switch(q) {
                        case 0:
                            if(litera == 32 || litera == 9 || litera == 13 || litera == 10 || litera == 8) {
                                break;
                            }

                            if(readpos == 0 && posRoole > 0 && (litera == 33 || litera == 35)) {
                                nod = new Node(roole, posRoole);
                                this.language.add(nod);
                                if(litera == 33) {
                                    posRoole = 1;
                                    break;
                                }

                                posRoole = 0;
                            }

                            byte var26 = 0;
                            pos = var26 + 1;
                            lexema[var26] = litera;
                            if(litera == 35) {
                                q = 1;
                            } else if(litera == 92) {
                                --pos;
                                q = 3;
                            } else {
                                q = 2;
                            }
                            break;
                        case 1:
                            lexema[pos++] = litera;
                            if(litera != 35 && readpos != 0) {
                                break;
                            }

                            e = new String(lexema, 0, pos);
                            nodeTmp = new TableNode(e, -2147483648);
                            strTmp = true;
                            ii = this.lexemaTable.iterator();

                            while(ii.hasNext()) {
                                i$ = (TableNode)ii.next();
                                if(nodeTmp.equals(i$)) {
                                    strTmp = false;
                                    break;
                                }
                            }

                            if(strTmp) {
                                this.lexemaTable.add(nodeTmp);
                            }

                            newLexemaCode = this.getLexemaCode(e, -2147483648);
                            roole[posRoole++] = newLexemaCode;
                            pos = 0;
                            q = 0;
                            break;
                        case 2:
                            if(litera == 92) {
                                --pos;
                                q = 3;
                            } else {
                                if(litera != 32 && readpos != 0 && litera != 35 && litera != 13 && litera != 9) {
                                    lexema[pos++] = litera;
                                    continue;
                                }

                                e = new String(lexema, 0, pos);
                                nodeTmp = new TableNode(e, 268435456);
                                strTmp = true;
                                ii = this.lexemaTable.iterator();

                                while(ii.hasNext()) {
                                    i$ = (TableNode)ii.next();
                                    if(nodeTmp.equals(i$)) {
                                        strTmp = false;
                                        break;
                                    }
                                }

                                if(strTmp) {
                                    this.lexemaTable.add(nodeTmp);
                                }

                                newLexemaCode = this.getLexemaCode(e, 268435456);
                                roole[posRoole++] = newLexemaCode;
                                pos = 0;
                                q = 0;
                                --readpos;
                            }
                            break;
                        case 3:
                            lexema[pos++] = litera;
                            q = 2;
                    }
                }
            }

            if(pos != 0) {
                int var29;
                if(q == 1) {
                    var29 = -2147483648;
                } else {
                    var29 = 268435456;
                }

                String var31 = new String(lexema, 0, pos);
                nodeTmp = new TableNode(var31, var29);
                boolean var30 = true;
                Iterator var32 = this.lexemaTable.iterator();

                while(var32.hasNext()) {
                    TableNode tmp = (TableNode)var32.next();
                    if(nodeTmp.equals(tmp)) {
                        var30 = false;
                        break;
                    }
                }

                if(var30) {
                    this.lexemaTable.add(nodeTmp);
                }

                newLexemaCode = this.getLexemaCode(var31, var29);
                roole[posRoole++] = newLexemaCode;
            }

            if(posRoole > 0) {
                nod = new Node(roole, posRoole);
                this.language.add(nod);
            }

            this.create = true;
        } catch (IOException var25) {
            System.out.println(var25.toString());
            this.create = false;
        }

    }

    private class TmpList {
        MyLang.TmpList treeFather;
        Node treeNode;
        int treePos;

        private TmpList(MyLang.TmpList tmpFather, Node tmpNode, int tmpPos) {
            this.treeFather = tmpFather;
            this.treeNode = tmpNode;
            this.treePos = tmpPos;
        }

        private void destroy() {
            this.treeFather = null;
            this.treeNode = null;
            this.treePos = 0;
        }

        private boolean roleInTree(Node tmp) {
            MyLang.TmpList tmpLst = null;

            for(tmpLst = this; tmpLst != null; tmpLst = tmpLst.treeFather) {
                if(tmpLst.treeNode.getRoole()[0] == tmp.getRoole()[0]) {
                    return true;
                }
            }

            return false;
        }

        private boolean searchLeftRecursion(MyLang lang) {
            int[] epsilon = lang.getEpsilonNonterminals();
            MyLang.TmpList tmpLst = null;

            for(tmpLst = this; tmpLst.treeFather != null; tmpLst = tmpLst.treeFather) {
                ;
            }

            Node root = tmpLst.treeNode;
            if(root.getRoole()[0] == this.treeNode.getRoole()[this.treePos]) {
                System.out.println("Ліворекурсивний вивод");
                this.printLeftRecurion(lang);
                return true;
            } else {
                int count = 0;
                Iterator i$ = lang.getLanguarge().iterator();

                while(true) {
                    Node tmp;
                    int[] role;
                    do {
                        do {
                            do {
                                if(!i$.hasNext()) {
                                    return false;
                                }

                                tmp = (Node)i$.next();
                                ++count;
                            } while(tmp.getTeg() == 1);
                        } while(this.roleInTree(tmp));

                        role = tmp.getRoole();
                    } while(role[0] != this.treeNode.getRoole()[this.treePos]);

                    for(int ii = 1; ii < role.length && role[ii] <= 0; ++ii) {
                        MyLang.TmpList tree1 = MyLang.this.new TmpList(this, tmp, ii);
                        if(tree1.searchLeftRecursion(lang)) {
                            tree1.destroy();
                            return true;
                        }

                        tree1.destroy();

                        int ii1;
                        for(ii1 = 0; ii1 < epsilon.length && epsilon[ii1] != role[ii]; ++ii1) {
                            ;
                        }

                        if(ii1 == epsilon.length) {
                            break;
                        }
                    }
                }
            }
        }

        private void printLeftRecurion(MyLang lang) {
            if(this.treeFather != null) {
                MyLang.TmpList tmp = this.treeFather;
                tmp.printLeftRecurion(lang);
            }

            int[] tmpRole = this.treeNode.getRoole();

            int ii;
            for(ii = 0; ii < tmpRole.length; ++ii) {
                if(ii == 0) {
                    System.out.print(lang.getLexemaText(tmpRole[ii]) + " -> ");
                } else {
                    System.out.print(lang.getLexemaText(tmpRole[ii]) + " ");
                }
            }

            System.out.println();

            for(ii = 1; ii < this.treePos; ++ii) {
                System.out.println(lang.getLexemaText(tmpRole[ii]) + " =>* epsilon");
            }

        }

        private boolean searchRigthRecursion(MyLang lang) {
            int[] epsilon = lang.getEpsilonNonterminals();

            MyLang.TmpList tmpLst;
            for(tmpLst = this; tmpLst.treeFather != null; tmpLst = tmpLst.treeFather) {
                ;
            }

            Node root = tmpLst.treeNode;
            if(root.getRoole()[0] == this.treeNode.getRoole()[this.treePos]) {
                System.out.println("\nПраворекурсивний вивод:");
                this.printRigthRecurion(lang);
                return true;
            } else {
                int count = 0;
                Iterator i$ = lang.getLanguarge().iterator();

                while(true) {
                    Node tmp;
                    int[] role;
                    do {
                        do {
                            do {
                                do {
                                    do {
                                        if(!i$.hasNext()) {
                                            return false;
                                        }

                                        tmp = (Node)i$.next();
                                        ++count;
                                    } while(tmp.getTeg() == 1);
                                } while(this.roleInTree(tmp));

                                role = tmp.getRoole();
                            } while(role.length <= 1);
                        } while(role[role.length - 1] > 0);
                    } while(role[0] != this.treeNode.getRoole()[this.treePos]);

                    for(int ii = role.length - 1; ii > 0 && role[ii] <= 0; --ii) {
                        MyLang.TmpList tree1 = MyLang.this.new TmpList(this, tmp, ii);
                        if(tree1.searchRigthRecursion(lang)) {
                            tree1.destroy();
                            return true;
                        }

                        tree1.destroy();

                        int ii1;
                        for(ii1 = 0; ii1 < epsilon.length && epsilon[ii1] != role[ii]; ++ii1) {
                            ;
                        }

                        if(ii1 == epsilon.length) {
                            break;
                        }
                    }
                }
            }
        }

        private void printRigthRecurion(MyLang lang) {
            if(this.treeFather != null) {
                MyLang.TmpList tmp = this.treeFather;
                tmp.printRigthRecurion(lang);
            }

            int[] tmpRole = this.treeNode.getRoole();

            int ii;
            for(ii = 0; ii < tmpRole.length; ++ii) {
                if(ii == 0) {
                    System.out.print(lang.getLexemaText(tmpRole[ii]) + " -> ");
                } else {
                    System.out.print(lang.getLexemaText(tmpRole[ii]) + " ");
                }
            }

            System.out.println();

            for(ii = tmpRole.length - 1; ii > this.treePos; --ii) {
                System.out.println(lang.getLexemaText(tmpRole[ii]) + " =>* epsilon");
            }

        }
    }
}
