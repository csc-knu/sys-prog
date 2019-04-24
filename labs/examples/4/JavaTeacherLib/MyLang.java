package JavaTeacherLib;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedList;

public class MyLang {
    private int axiom;
    private boolean create;
    private int LLK;
    private LinkedList<Node> language;
    private LinkedList<TableNode> lexemsTable;
    private int[] terminals;
    private int[] nonTerminals;
    private int[] epsilonTerminals;
    private LlkContext[] termLanguage;
    private LlkContext[] firstK;
    private LlkContext[] followK;
    private LinkedList<LlkContext>[] LocalKContext;
    private int[][] uprTable;

    public MyLang(String fileLang, int llk1) {
        this.LLK = llk1;
        this.create = false;
        this.language = new LinkedList<>();
        this.lexemsTable = new LinkedList<>();
        this.readGrammar(fileLang);
        if (this.create) {
            Iterator iterator = this.language.iterator();
            if (iterator.hasNext()) this.axiom = ((Node)iterator.next()).getRule()[0];
            
            this.terminals = this.createTerminals();
            this.nonTerminals = this.createNonterminals();
            this.termLanguage = this.createTerminalLang();
        }
    }

    public boolean isCreate() {
        return this.create;
    }

    public void setLocalKContext(LinkedList<LlkContext>[] localK) {
        this.LocalKContext = localK;
    }

    private LinkedList<LlkContext>[] getLocalKContext() {
        return this.LocalKContext;
    }

    public void leftRecursionTrace() {
        LinkedList lang = this.getLanguage();
        this.getTerminals();
        int[] epsilon = this.getEpsilonNonTerminals();

        for (Object o : lang) {
            Node i$ = (Node) o;
            int[] tmp = i$.getRule();
            if (tmp.length == 1) i$.setTag(1);
            else if (i$.getRule()[1] > 0) i$.setTag(1);
            else i$.setTag(0);
        }

        boolean isRecursion = false;
        Iterator var13 = lang.iterator();

        while (true) {
            Node var14;
            do {
                if (!var13.hasNext()) {
                    if (!isRecursion) System.out.println("В граматиці ліворекурсивні нетермінали відсутні ");
                    return;
                }

                var14 = (Node)var13.next();
            } while (var14.getTag() == 1);

            int[] role = var14.getRule();

            for (int ii = 1; ii < role.length && role[ii] <= 0; ++ii) {
                MyLang.TmpList tree = new MyLang.TmpList(null, var14, ii);
                if(tree.searchLeftRecursion(this)) {
                    tree.destroy();
                    isRecursion = true;
                    break;
                }

                tree.destroy();

                int ii1 = 0;
                while(ii1 < epsilon.length && epsilon[ii1] != role[ii]) ++ii1;

                if(ii1 == epsilon.length) break;
            }
        }
    }

    public void rightRecursionTrace() {
        LinkedList lang = this.getLanguage();
        this.getTerminals();
        int[] epsilon = this.getEpsilonNonTerminals();
        Iterator i$ = lang.iterator();

        Node tmp2;
        int[] role;
        while(i$.hasNext()) {
            tmp2 = (Node)i$.next();
            role = tmp2.getRule();
            if(role.length == 1) tmp2.setTag(1);
            else if(role[role.length - 1] > 0) tmp2.setTag(1);
            else tmp2.setTag(0);
        }

        boolean isRecursion = false;
        i$ = lang.iterator();

        while(true) {
            do {
                if (!i$.hasNext()) {
                    if (!isRecursion) System.out.println("В граматиці праворекурсивні нетермінали відсутні ");
                    return;
                }

                tmp2 = (Node)i$.next();
            } while (tmp2.getTag() == 1);

            role = tmp2.getRule();

            for (int ii = role.length - 1; ii > 0 && role[ii] <= 0; --ii) {
                MyLang.TmpList tree = new MyLang.TmpList(null, tmp2, ii);
                if (tree.searchRightRecursion(this)) {
                    tree.destroy();
                    isRecursion = true;
                    break;
                }

                tree.destroy();

                int ii1 = 0;
                while (ii1 < epsilon.length && epsilon[ii1] != role[ii]) ++ii1;

                if(ii1 == epsilon.length) break;
            }
        }
    }

    public LinkedList<LlkContext>[] createLocalK() {
        LinkedList[] localK;
        LlkContext[] termLang = this.getLlkTrmContext();
        LlkContext[] first = this.getFirstK();
        int[] term = this.getTerminals();
        int[] nonterm = this.getNonTerminals();
        localK = new LinkedList[nonterm.length];
        LlkContext[] tmpLlk = new LlkContext[40];
        int[] mult = new int[40];
        int[] maxmult = new int[40];

        int ii;
        for (ii = 0; ii < nonterm.length; ++ii) localK[ii] = null;

        ii = 0;
        while (ii < nonterm.length && nonterm[ii] != this.axiom) ++ii;

        LlkContext result = new LlkContext();
        result.addWord(new int[0]);
        localK[ii] = new LinkedList();
        localK[ii].add(result);
        int iter = 0;

        boolean count;
        do {
            ++iter;
            System.out.println("Побудова множини Local: ітерація " + iter);
            count = false;

            label151:
            for(ii = 0; ii < nonterm.length; ++ii) {
                Iterator i$ = this.language.iterator();

                while(true) {
                    int ii1;
                    Node tmp;
                    do {
                        if(!i$.hasNext()) continue label151;

                        tmp = (Node)i$.next();

                        ii1 = 0;
                        while (ii1 < nonterm.length && nonterm[ii1] != tmp.getRule()[0]) ++ii1;
                    } while (localK[ii1] == null);

                    int indexLeft = ii1;
                    int[] role = tmp.getRule();

                    for (ii1 = 1; ii1 < role.length; ++ii1)
                        if (role[ii1] == nonterm[ii]) {
                            int multcount = 0;

                            int j1;
                            for (int j = ii1 + 1; j < role.length; ++j)
                                if (role[j] >= 0) {
                                    j1 = 0;
                                    while (j1 < term.length && term[j1] != role[j]) ++j1;

                                    tmpLlk[multcount++] = termLang[j1];
                                } else {
                                    j1 = 0;
                                    while (j1 < nonterm.length && nonterm[j1] != role[j]) ++j1;

                                    tmpLlk[multcount++] = first[j1];
                                }

                            for (Object o : localK[indexLeft]) {
                                LlkContext tmp1 = (LlkContext) o;
                                tmpLlk[multcount] = tmp1;

                                for (j1 = 0; j1 < multcount + 1; ++j1) {
                                    mult[j1] = 0;
                                    maxmult[j1] = tmpLlk[j1].calcWords();
                                }

                                int realCalc = 0;

                                for (j1 = 0; j1 < multcount + 1; ++j1)
                                    if (j1 == 0) realCalc = tmpLlk[j1].minLengthWord();
                                    else {
                                        int minLength = tmpLlk[j1].minLengthWord();
                                        if (realCalc >= this.getLlkConst()) break;

                                        realCalc += minLength;
                                    }

                                realCalc = j1;
                                result = new LlkContext();

                                do {
                                    int[] llkWord = this.newWord(this.getLlkConst(), tmpLlk, mult, realCalc);
                                    result.addWord(llkWord);
                                } while (this.newCalcIndex(mult, maxmult, realCalc));

                                if (localK[ii] == null) {
                                    localK[ii] = new LinkedList();
                                    localK[ii].add(result);
                                    count = true;
                                } else if (!this.belongToLlkContext(localK[ii], result)) {
                                    localK[ii].add(result);
                                    count = true;
                                }
                            }
                        }
                }
            }
        } while (count);

        return localK;
    }

    public void printLocalK() {
        LinkedList[] localK = this.getLocalKContext();
        this.getTerminals();
        int[] nonterm = this.getNonTerminals();

        for(int ii = 0; ii < nonterm.length; ++ii) {
            System.out.println("Контекст Local для нетермінала " + this.getLexemText(nonterm[ii]));

            for (Object o : localK[ii]) {
                LlkContext tmp = (LlkContext) o;
                tmp.calcWords();
                System.out.println("{ ");

                for (int ii1 = 0; ii1 < tmp.calcWords(); ++ii1) {
                    int[] word = tmp.getWord(ii1);
                    if (word.length == 0) System.out.print("Е-слово");
                    else for (int i : word) System.out.print(this.getLexemText(i) + " ");
                    System.out.println();
                }

                System.out.println("}");
            }
        }

    }

    public boolean llkCondition() {
        boolean upr = true;
        int count = 0;
        LinkedList[] localK = this.getLocalKContext();
        LlkContext[] firstTerm = this.getLlkTrmContext();
        LlkContext[] firstNonterm = this.getFirstK();
        int[] term = this.getTerminals();
        int[] nonterm = this.getNonTerminals();
        LlkContext[] tmpLlk = new LlkContext[40];
        int[] mult = new int[40];
        int[] maxmult = new int[40];
        Iterator i$ = this.getLanguage().iterator();

        label219:
        while(i$.hasNext()) {
            Node tmp = (Node)i$.next();
            ++count;
            int[] role = tmp.getRule();
            int count1 = 0;
            Iterator i$1 = this.getLanguage().iterator();

            label217:
            while(true) {
                int[] role1;
                do {
                    if (!i$1.hasNext()) continue label219;

                    Node tmp1 = (Node)i$1.next();
                    ++count1;
                    if (tmp == tmp1) continue label219;

                    role1 = tmp1.getRule();
                } while(role[0] != role1[0]);

                int ii = 0;
                while (ii < nonterm.length && role[0] != nonterm[ii]) ++ii;

                Iterator i$2 = localK[ii].iterator();

                while (true) {
                    if (!i$2.hasNext()) continue label217;

                    LlkContext locTmp = (LlkContext) i$2.next();
                    int countMult = 0;

                    int j1;
                    int j2;
                    for (j1 = 1; j1 < role.length; ++j1)
                        if (role[j1] > 0) {
                            j2 = 0;
                            while (j2 < term.length && term[j2] != role[j1]) ++j2;

                            tmpLlk[countMult++] = firstTerm[j2];
                        } else {
                            j2 = 0;
                            while (j2 < nonterm.length && nonterm[j2] != role[j1]) ++j2;

                            tmpLlk[countMult++] = firstNonterm[j2];
                        }

                    tmpLlk[countMult++] = locTmp;

                    for (j1 = 0; j1 < countMult; ++j1) {
                        mult[j1] = 0;
                        maxmult[j1] = tmpLlk[j1].calcWords();
                    }

                    int realCalc = 0;

                    int minLength;
                    for (j1 = 0; j1 < countMult; ++j1)
                        if (j1 == 0) realCalc = tmpLlk[j1].minLengthWord();
                        else {
                            minLength = tmpLlk[j1].minLengthWord();
                            if (realCalc >= this.getLlkConst()) break;

                            realCalc += minLength;
                        }

                    realCalc = j1;
                    LlkContext result = new LlkContext();

                    int[] llkWord;
                    do {
                        llkWord = this.newWord(this.getLlkConst(), tmpLlk, mult, realCalc);
                        result.addWord(llkWord);
                    } while (this.newCalcIndex(mult, maxmult, realCalc));

                    countMult = 0;

                    for (j1 = 1; j1 < role1.length; ++j1)
                        if (role1[j1] > 0) {
                            j2 = 0;
                            while (j2 < term.length && term[j2] != role1[j1]) ++j2;

                            tmpLlk[countMult++] = firstTerm[j2];
                        } else {
                            j2 = 0;
                            while (j2 < nonterm.length && nonterm[j2] != role1[j1]) ++j2;

                            tmpLlk[countMult++] = firstNonterm[j2];
                        }

                    tmpLlk[countMult++] = locTmp;

                    for (j1 = 0; j1 < countMult; ++j1) {
                        mult[j1] = 0;
                        maxmult[j1] = tmpLlk[j1].calcWords();
                    }

                    realCalc = 0;

                    for (j1 = 0; j1 < countMult; ++j1)
                        if (j1 == 0) realCalc = tmpLlk[j1].minLengthWord();
                        else {
                            minLength = tmpLlk[j1].minLengthWord();
                            if (realCalc >= this.getLlkConst()) break;

                            realCalc += minLength;
                        }

                    realCalc = j1;
                    LlkContext result1 = new LlkContext();

                    do {
                        llkWord = this.newWord(this.getLlkConst(), tmpLlk, mult, realCalc);
                        result1.addWord(llkWord);
                    } while (this.newCalcIndex(mult, maxmult, realCalc));

                    for (j1 = 0; j1 < result.calcWords(); ++j1)
                        if (result1.wordInContext(result.getWord(j1))) {
                            System.out.println("Пара правил (" + count + "," + count1 + ") не задовольняють LL(" + this.getLlkConst() + ")-умові");
                            upr = false;
                            break;
                        }
                }
            }
        }

        if (upr) System.out.println("Граматика задовольняє LL(" + this.getLlkConst() + ")-умові");

        return upr;
    }

    private boolean belongToLlkContext(LinkedList<LlkContext> list, LlkContext llk) {
        int llkCount = llk.calcWords();
        Iterator i$ = list.iterator();

        int ii;
        do {
            LlkContext tmp;
            do {
                if(!i$.hasNext()) return false;

                tmp = (LlkContext)i$.next();
            } while(llkCount != tmp.calcWords());

            tmp.calcWords();

            ii = 0;
            while (ii < llkCount && tmp.wordInContext(llk.getWord(ii))) ++ii;
        } while(ii != llkCount);

        return true;
    }

    int indexNonTerminal(int ssItem) {
        for (int ii = 0; ii < this.nonTerminals.length; ++ii) if (ssItem == this.nonTerminals[ii]) return ii;
        return 0;
    }

    int indexTerminal(int ssItem) {
        for (int ii = 0; ii < this.terminals.length; ++ii) if (ssItem == this.terminals[ii]) return ii;
        return 0;
    }

    int getLexemCode(byte[] lexem, int lexemLen) {
        Iterator i$ = this.lexemsTable.iterator();

        String ss;
        int ii;
        TableNode tmp;
        do {
            do {
                if (!i$.hasNext()) return -1;

                tmp = (TableNode)i$.next();
                ss = tmp.getLexemText();
            } while (ss.length() != lexemLen);

            ii = 0;
            while (ii < ss.length() && ss.charAt(ii) == (char)lexem[ii]) ++ii;
        } while (ii != ss.length());

        return tmp.getLexemCode();
    }

    public void printTerminals() {
        System.out.println("СПИСОК ТЕРМІНАЛІВ:");
        if (this.terminals != null) for (int ii = 0; ii < this.terminals.length; ++ii) System.out.println("" + (ii + 1) + "  " + this.terminals[ii] + "\t " + this.getLexemText(this.terminals[ii]));
    }

    public void printNonTerminals() {
        System.out.println("СПИСОК НЕТЕРМІНАЛІВ:");
        if (this.nonTerminals != null) for (int ii = 0; ii < this.nonTerminals.length; ++ii)
            System.out.println((ii + 1) + "  " + this.nonTerminals[ii] + "\t " + this.getLexemText(this.nonTerminals[ii]));
    }

    int[][] getUprTable() {
        return this.uprTable;
    }

    public void setUprTable(int[][] upr) {
        this.uprTable = upr;
    }

    private LlkContext[] getFirstK() {
        return this.firstK;
    }

    public void setFirstK(LlkContext[] first) {
        this.firstK = first;
    }

    private LlkContext[] getFollowK() {
        return this.followK;
    }

    public void setFollowK(LlkContext[] follow) {
        this.followK = follow;
    }

    private void printRoole(Node nod) {
        int[] role = nod.getRule();

        for (int ii = 0; ii < role.length; ++ii)
            if (ii == 0) System.out.print(this.getLexemText(role[ii]) + " -> ");
            else System.out.print(" " + this.getLexemText(role[ii]));

        System.out.println();
    }

    public boolean createNonProdRools() {
        if(this.getNonTerminals().length == 0) return true;
        else {
            int[] prodTmp = new int[this.getNonTerminals().length];
            int count = 0;
            Iterator i$ = this.language.iterator();

            Node tmp;
            while(i$.hasNext()) {
                tmp = (Node)i$.next();
                tmp.setTag(0);
            }

            int ii;
            boolean upr;
            int[] rule1;
            label117:
            do {
                upr = false;
                i$ = this.language.iterator();

                while(true) {
                    int ii1;
                    do {
                        do {
                            if (!i$.hasNext()) continue label117;

                            tmp = (Node)i$.next();
                            rule1 = tmp.getRule();
                        } while (tmp.getTag() == 1);

                        for (ii = 1; ii < rule1.length; ++ii)
                            if (rule1[ii] <= 0) {
                                ii1 = 0;
                                while (ii1 < count && prodTmp[ii1] != rule1[ii]) ++ii1;

                                if (ii1 == count) break;
                            }
                    } while (ii != rule1.length);

                    ii1 = 0;
                    while (ii1 < count && prodTmp[ii1] != rule1[0]) ++ii1;

                    if (ii1 == count) prodTmp[count++] = rule1[0];

                    tmp.setTag(1);
                    upr = true;
                }
            } while (upr);

            if (count == prodTmp.length) {
                System.out.print("В граматиці непродуктивні правила відсутні\n");
                return true;
            } else {
                System.out.print("Непродуктивні правила: \n");
                i$ = this.language.iterator();

                while(true) {
                    do {
                        if (!i$.hasNext()) return false;

                        tmp = (Node)i$.next();
                    } while (tmp.getTag() == 1);

                    rule1 = tmp.getRule();

                    for (ii = 0; ii < rule1.length; ++ii) {
                        if (ii == 1) System.out.print(" ::= ");

                        System.out.print(this.getLexemText(rule1[ii]) + " ");
                        if (rule1.length == 1) System.out.print(" ::= ");
                    }

                    System.out.println();
                }
            }
        }
    }

    public boolean createNonDosNonTerminals() {
        int[] nonTerminals = this.getNonTerminals();
        int[] dosNonTerminals = new int[nonTerminals.length];
        int count = 0;

        Iterator i$ = this.language.iterator();
        Node tmp;
        if(i$.hasNext()) {
            tmp = (Node)i$.next();
            dosNonTerminals[0] = tmp.getRule()[0];
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
                int[] rule1;
                do {
                    if (!i$.hasNext()) continue label109;

                    tmp = (Node)i$.next();
                    rule1 = tmp.getRule();

                    ii = 0;
                    while (ii < count && dosNonTerminals[ii] != rule1[0]) ++ii;
                } while (ii == count);

                for (ii = 1; ii < rule1.length; ++ii)
                    if (rule1[ii] < 0) {
                        ii1 = 0;
                        while (ii1 < count && dosNonTerminals[ii1] != rule1[ii]) ++ii1;

                        if (ii1 == count) {
                            dosNonTerminals[count] = rule1[ii];
                            upr = true;
                            ++count;
                        }
                    }
            }
        } while(upr);

        int var12 = nonTerminals.length - count;
        if (var12 == 0) {
            System.out.println("В граматиці недосяжних нетерміналів немає");
            return true;
        } else {
            int[] nonDosNonTerminals = new int[var12];
            var12 = 0;

            for (ii = 0; ii < nonTerminals.length; ++ii) {
                ii1 = 0;
                while (ii1 < count && nonTerminals[ii] != dosNonTerminals[ii1]) ++ii1;

                if (ii1 == count) nonDosNonTerminals[var12++] = nonTerminals[ii];
            }

            for (ii = 0; ii < nonDosNonTerminals.length; ++ii)
                System.out.println("Недосяжний нетермінал: " + this.getLexemText(nonDosNonTerminals[ii]) + "\n ");

            return false;
        }
    }

    public boolean leftRecursNonTerminal() {
        int[] controlSet = new int[this.getNonTerminals().length];
        int[] nonTrm = this.getNonTerminals();
        int[] eps = this.getEpsilonNonTerminals();
        boolean upr;
        boolean upr1 = false;

        for (int i : nonTrm) {
            int count = 0;
            int count1 = 1;
            upr = false;
            controlSet[count] = i;

            do {
                Iterator i$ = this.language.iterator();

                label94:
                while (true) {
                    int[] rule1;
                    do {
                        if (!i$.hasNext()) break label94;

                        Node tmp = (Node) i$.next();
                        rule1 = tmp.getRule();
                    } while (rule1[0] != controlSet[count]);

                    int ii1;
                    for (ii1 = 1; ii1 < rule1.length && rule1[ii1] <= 0 && rule1[ii1] != controlSet[0]; ++ii1) {
                        int ii2 = 0;
                        while (ii2 < count1 && rule1[ii1] != controlSet[ii2]) ++ii2;

                        if (ii2 == count1) controlSet[count1++] = rule1[ii1];

                        if (eps == null) break;

                        ii2 = 0;
                        while (ii2 < eps.length && rule1[ii1] != eps[ii2]) ++ii2;

                        if (ii2 == eps.length) break;
                    }

                    if (ii1 != rule1.length && rule1[ii1] == controlSet[0]) {
                        System.out.print("Нетермінал: " + this.getLexemText(controlSet[0]) + " ліворекурсивний \n");
                        upr = true;
                        upr1 = true;
                        break;
                    }
                }

                if (upr) break;

                ++count;
            } while (count < count1);
        }

        if (!upr1) {
            System.out.print("В граматиці відсутні ліворекурсивні нетермінали \n");
            return false;
        } else return true;
    }

    public boolean rightRecursNonTerminal() {
        int[] controlSet = new int[this.getNonTerminals().length];
        int[] nonTerminals = this.getNonTerminals();
        int[] eps = this.getEpsilonNonTerminals();
        boolean upr;
        boolean upr1 = false;

        for (int i : nonTerminals) {
            int count = 0;
            int count1 = 1;
            upr = false;
            controlSet[count] = i;

            do {
                Iterator i$ = this.language.iterator();

                label94:
                while (true) {
                    int[] rule1;
                    do {
                        if (!i$.hasNext()) break label94;

                        Node tmp = (Node) i$.next();
                        rule1 = tmp.getRule();
                    } while (rule1[0] != controlSet[count]);

                    int ii1;
                    for (ii1 = rule1.length - 1; ii1 > 0 && rule1[ii1] <= 0 && rule1[ii1] != controlSet[0]; --ii1) {
                        int ii2 = 0;
                        while (ii2 < count1 && rule1[ii1] != controlSet[ii2]) ++ii2;

                        if (ii2 == count1) controlSet[count1++] = rule1[ii1];

                        if (eps == null) break;

                        ii2 = 0;
                        while (ii2 < eps.length && rule1[ii1] != eps[ii2]) ++ii2;

                        if (ii2 == eps.length) break;
                    }

                    if (ii1 != 0 && rule1[ii1] == controlSet[0]) {
                        System.out.print("Нетермінал: " + this.getLexemText(controlSet[0]) + " праворекурсивний \n");
                        upr = true;
                        upr1 = true;
                        break;
                    }
                }

                if (upr) break;

                ++count;
            } while (count < count1);
        }

        if (!upr1) {
            System.out.print("В граматиці відсутні праворекурсивні нетермінали \n");
            return false;
        } else return true;
    }

    public LlkContext[] firstK() {
        int[] llkWord;
        boolean upr;
        int[] term = this.getTerminals();
        int[] nonterm = this.getNonTerminals();
        LlkContext[] llkTrmContext = this.getLlkTrmContext();
        LlkContext[] result = new LlkContext[nonterm.length];
        LlkContext[] tmpLlk = new LlkContext[40];
        int[] mult = new int[40];
        int[] maxmult = new int[40];
        int iter = 0;

        for (int ii = 0; ii < result.length; ++ii) result[ii] = new LlkContext();

        label125:
        do {
            upr = false;
            PrintStream var10000 = System.out;
            StringBuilder var10001 = (new StringBuilder()).append("Ітерація пошуку множини FirstK ");
            ++iter;
            var10000.println(var10001.append(iter).toString());
            Iterator i$ = this.language.iterator();

            while(true) while (true) {
                if (!i$.hasNext()) continue label125;

                Node tmp = (Node) i$.next();
                int[] tmpRule = tmp.getRule();

                int ii = 0;
                while (ii < nonterm.length && tmpRule[0] != nonterm[ii]) ++ii;

                if (tmpRule.length == 1) {
                    if (result[ii].addWord(new int[0])) upr = true;
                } else {
                    int ii0;
                    int ii1;
                    for (ii0 = 1; ii0 < tmpRule.length; ++ii0)
                        if (tmpRule[ii0] > 0) {
                            ii1 = 0;
                            while (ii1 < term.length && term[ii1] != tmpRule[ii0]) ++ii1;

                            tmpLlk[ii0 - 1] = llkTrmContext[ii1];
                        } else {
                            ii1 = 0;
                            while (ii1 < nonterm.length && nonterm[ii1] != tmpRule[ii0]) ++ii1;

                            if (result[ii1].calcWords() == 0) break;

                            tmpLlk[ii0 - 1] = result[ii1];
                        }

                    if (ii0 == tmpRule.length) {
                        int multCount = tmpRule.length - 1;

                        for (ii1 = 0; ii1 < multCount; ++ii1) {
                            mult[ii1] = 0;
                            maxmult[ii1] = tmpLlk[ii1].calcWords();
                        }

                        int realCalc = 0;

                        for (ii1 = 0; ii1 < multCount; ++ii1)
                            if (ii1 == 0) realCalc = tmpLlk[ii1].minLengthWord();
                            else {
                                int minLength = tmpLlk[ii1].minLengthWord();
                                if (realCalc >= this.getLlkConst()) break;

                                realCalc += minLength;
                            }

                        realCalc = ii1;

                        do {
                            llkWord = this.newWord(this.getLlkConst(), tmpLlk, mult, realCalc);
                            if (result[ii].addWord(llkWord)) upr = true;

                        } while (this.newCalcIndex(mult, maxmult, realCalc));
                    }
                }
            }
        } while (upr);

        System.out.println("Кінець пошуку множин FIRSTk");
        return result;
    }

    public LlkContext[] followK() {
        LinkedList lan = this.getLanguage();
        LlkContext[] firstK = this.getFirstK();
        int[] term = this.getTerminals();
        int[] nonterm = this.getNonTerminals();
        LlkContext[] llkTrmContext = this.getLlkTrmContext();
        LlkContext[] result = new LlkContext[nonterm.length];
        LlkContext[] tmpLlk = new LlkContext[40];
        int[] mult = new int[40];
        int[] maxmult = new int[40];
        int iter = 0;

        for(int ii = 0; ii < result.length; ++ii) result[ii] = new LlkContext();

        int axiom = this.getAxiom();

        int ii = 0;
        while (ii < nonterm.length && nonterm[ii] != axiom) ++ii;

        result[ii].addWord(new int[0]);

        boolean upr;
        label149:
        do {
            upr = false;
            PrintStream var10000 = System.out;
            StringBuilder var10001 = (new StringBuilder()).append("Ітерація побудово множини FollowK ");
            ++iter;
            var10000.println(var10001.append(iter).toString());
            Iterator i$ = lan.iterator();

            while (true) {
                int[] tmpRule;
                do {
                    if (!i$.hasNext()) continue label149;

                    Node tmp = (Node)i$.next();
                    tmpRule = tmp.getRule();

                    ii = 0;
                    while (ii < nonterm.length && tmpRule[0] != nonterm[ii]) ++ii;

                    if(ii == nonterm.length) return null;
                } while (result[ii].calcWords() == 0);

                int leftItem = ii;

                for(int jj = 1; jj < tmpRule.length; ++jj)
                    if (tmpRule[jj] <= 0) {
                        int multCount = 0;

                        int ii1;
                        for (int ii0 = jj + 1; ii0 < tmpRule.length; ++ii0)
                            if (tmpRule[ii0] > 0) {
                                ii1 = 0;
                                while (ii1 < term.length && term[ii1] != tmpRule[ii0]) ++ii1;

                                tmpLlk[multCount++] = llkTrmContext[ii1];
                            } else {
                                ii1 = 0;
                                while (ii1 < nonterm.length && nonterm[ii1] != tmpRule[ii0]) ++ii1;

                                tmpLlk[multCount++] = firstK[ii1];
                            }

                        tmpLlk[multCount++] = result[leftItem];

                        for (ii1 = 0; ii1 < multCount; ++ii1) {
                            mult[ii1] = 0;
                            maxmult[ii1] = tmpLlk[ii1].calcWords();
                        }

                        int realCalc = 0;

                        for (ii1 = 0; ii1 < multCount; ++ii1)
                            if (ii1 == 0) realCalc = tmpLlk[ii1].minLengthWord();
                            else {
                                int minLength = tmpLlk[ii1].minLengthWord();
                                if (realCalc >= this.getLlkConst()) break;

                                realCalc += minLength;
                            }

                        realCalc = ii1;

                        ii1 = 0;
                        while (ii1 < nonterm.length && nonterm[ii1] != tmpRule[jj]) ++ii1;

                        do {
                            int[] llkWord = this.newWord(this.getLlkConst(), tmpLlk, mult, realCalc);
                            if (result[ii1].addWord(llkWord)) upr = true;

                        } while (this.newCalcIndex(mult, maxmult, realCalc));
                    }
            }
        } while (upr);

        System.out.println("Кінець пошуку множин FollowK");
        return result;
    }

    public void firstFollowK() {
        int[] term = this.getTerminals();
        int[] nonterm = this.getNonTerminals();
        LlkContext[] llkTrmContext = this.getLlkTrmContext();
        LlkContext[] tmpLlk = new LlkContext[40];
        int[] mult = new int[40];
        int[] maxmult = new int[40];

        for (Node tmp : this.getLanguage()) {
            int[] tmpRule = tmp.getRule();
            int multCount = 0;

            int ii1;
            for (int ii = 1; ii < tmpRule.length; ++ii) {
                if (tmpRule[ii] > 0) {
                    ii1 = 0;
                    while (ii1 < term.length && term[ii1] != tmpRule[ii]) ++ii1;

                    tmpLlk[multCount] = llkTrmContext[ii1];
                } else {
                    ii1 = 0;
                    while (ii1 < nonterm.length && nonterm[ii1] != tmpRule[ii]) ++ii1;

                    tmpLlk[multCount] = this.firstK[ii1];
                }

                ++multCount;
            }

            ii1 = 0;
            while (ii1 < nonterm.length && nonterm[ii1] != tmpRule[0]) ++ii1;

            tmpLlk[multCount++] = this.followK[ii1];

            for (ii1 = 0; ii1 < multCount; ++ii1) {
                mult[ii1] = 0;
                maxmult[ii1] = tmpLlk[ii1].calcWords();
            }

            int realCalc = 0;

            for (ii1 = 0; ii1 < multCount; ++ii1)
                if (ii1 == 0) realCalc = tmpLlk[ii1].minLengthWord();
                else {
                    int minLength = tmpLlk[ii1].minLengthWord();
                    if (realCalc >= this.getLlkConst()) break;

                    realCalc += minLength;
                }

            realCalc = ii1;
            LlkContext result = new LlkContext();

            do {
                int[] llkWord = this.newWord(this.getLlkConst(), tmpLlk, mult, realCalc);
                result.addWord(llkWord);
            } while (this.newCalcIndex(mult, maxmult, realCalc));

            tmp.addFirstFollowK(result);
        }

        System.out.println("Множини FirstK(w)+ FollowK(A): А->w побудовані ");
    }

    public void firstKForRule() {
        int[] term = this.getTerminals();
        int[] nonterm = this.getNonTerminals();
        LlkContext[] llkTrmContext = this.getLlkTrmContext();
        LlkContext[] tmpLlk = new LlkContext[40];
        int[] mult = new int[40];
        int[] maxmult = new int[40];

        for (Node tmp : this.getLanguage()) {
            int[] tmpRule = tmp.getRule();
            LlkContext result;
            if (tmpRule.length == 1) {
                result = new LlkContext();
                result.addWord(new int[0]);
                tmp.addFirstKForRoole(result);
            } else {
                int var18 = 0;

                int ii1;
                for (int ii = 1; ii < tmpRule.length; ++var18) {
                    if (tmpRule[ii] > 0) {
                        ii1 = 0;
                        while (ii1 < term.length && term[ii1] != tmpRule[ii]) ++ii1;

                        tmpLlk[var18] = llkTrmContext[ii1];
                    } else {
                        ii1 = 0;
                        while (ii1 < nonterm.length && nonterm[ii1] != tmpRule[ii]) ++ii1;

                        tmpLlk[var18] = this.firstK[ii1];
                    }

                    ++ii;
                }

                for (ii1 = 0; ii1 < var18; ++ii1) {
                    mult[ii1] = 0;
                    maxmult[ii1] = tmpLlk[ii1].calcWords();
                }

                int realCalc = 0;

                for (ii1 = 0; ii1 < var18; ++ii1)
                    if (ii1 == 0) realCalc = tmpLlk[ii1].minLengthWord();
                    else {
                        int minLength = tmpLlk[ii1].minLengthWord();
                        if (realCalc >= this.getLlkConst()) break;

                        realCalc += minLength;
                    }

                realCalc = ii1;
                result = new LlkContext();

                do {
                    int[] llkWord = this.newWord(this.getLlkConst(), tmpLlk, mult, realCalc);
                    result.addWord(llkWord);
                } while (this.newCalcIndex(mult, maxmult, realCalc));

                tmp.addFirstKForRoole(result);
            }
        }

        System.out.println("Множини FirstK(w): А->w побудовані ");
    }

    public void printFirstContextForRule() {
        this.getNonTerminals();
        int number = 0;
        Iterator i$ = this.getLanguage().iterator();

        while (true) {
            LlkContext resultForRule;
            do {
                if (!i$.hasNext()) return;

                Node tmp = (Node)i$.next();
                ++number;
                resultForRule = tmp.getFirstKForRoole();
            } while (resultForRule == null);

            System.out.println("FirstK(W), W - права частина правила " + number);

            for (int ii = 0; ii < resultForRule.calcWords(); ++ii) {
                int[] word = resultForRule.getWord(ii);
                if (word.length == 0) System.out.print("Е-слово");
                else for (int i : word) System.out.print(this.getLexemText(i) + " ");

                System.out.println();
            }
        }
    }

    public void printFirstKContext() {
        int[] nonterm = this.getNonTerminals();
        LlkContext[] firstContext = this.getFirstK();
        if (firstContext != null) for (int j = 0; j < nonterm.length; ++j) {
            System.out.println("FirstK-контекст для нетермінала: " + this.getLexemText(nonterm[j]));
            LlkContext tmp = firstContext[j];

            for (int ii = 0; ii < tmp.calcWords(); ++ii) {
                int[] word = tmp.getWord(ii);
                if (word.length == 0) System.out.print("Е-слово");
                else for (int i : word) System.out.print(this.getLexemText(i) + " ");

                System.out.println();
            }
        }
    }

    public void printFollowKContext() {
        int[] nonterm = this.getNonTerminals();
        LlkContext[] followContext = this.getFollowK();

        for (int j = 0; j < nonterm.length; ++j) {
            System.out.println("FollowK-контекст для нетермінала: " + this.getLexemText(nonterm[j]));
            LlkContext tmp = followContext[j];

            for (int ii = 0; ii < tmp.calcWords(); ++ii) {
                int[] word = tmp.getWord(ii);
                if (word.length == 0) System.out.print("Е-слово");
                else for (int i : word) System.out.print(this.getLexemText(i) + " ");

                System.out.println();
            }
        }

    }

    public void printFirstFollowK() {
        int count = 0;

        for (Node tmp : this.getLanguage()) {
            ++count;
            System.out.print("Правило №" + count + " ");
            this.printRoole(tmp);
            LlkContext loc = tmp.getFirstFollowK();

            for (int ii1 = 0; ii1 < loc.calcWords(); ++ii1) {
                int[] word = loc.getWord(ii1);
                if (word.length == 0) System.out.print("Е-слово");
                else for (int i : word) System.out.print(this.getLexemText(i) + " ");

                System.out.println();
            }
        }

    }

    public void printFirstFollowForRoole() {
        byte[] readline = new byte[80];
        int rooleNumber = 0;

        while (true) {
            System.out.println("Введіть номер правила або end");

            try {
                int textLen = System.in.read(readline);
                String StrTmp = new String(readline, 0, textLen, StandardCharsets.ISO_8859_1);
                if (StrTmp.trim().equals("end")) return;

                rooleNumber = Integer.parseInt(StrTmp.trim());
            } catch (Exception var12) {
                System.out.println("Невірний код дії, повторіть: ");
            }

            int var13 = 0;

            for (Node tmp : this.getLanguage()) {
                ++var13;
                if (var13 == rooleNumber) {
                    this.printRoole(tmp);
                    LlkContext loc = tmp.getFirstFollowK();

                    for (int ii1 = 0; ii1 < loc.calcWords(); ++ii1) {
                        int[] word = loc.getWord(ii1);
                        if (word.length == 0) System.out.print("Е-слово");
                        else for (int i : word) System.out.print(this.getLexemText(i) + " ");

                        System.out.println();
                    }
                    break;
                }
            }
        }
    }

    public boolean strongLlkCondition() {
        int firstProductionRuleNumber = 0;
        for (Node firstProductionRule : this.getLanguage()) {
            ++firstProductionRuleNumber;

            int[] firstProductionRuleLexemCodes = firstProductionRule.getRule();
            LlkContext firstProductionRuleContext = firstProductionRule.getFirstFollowK();

            int secondProductionRuleNumber = 0;
            for (Node SecondProductionRule : this.getLanguage()) {
                ++secondProductionRuleNumber;
                // check only (i, j) pairs with i > j:
                if (firstProductionRuleNumber == secondProductionRuleNumber) break;

                int[] secondProductionRuleLexemCodes = SecondProductionRule.getRule();
                if (firstProductionRuleLexemCodes[0] == secondProductionRuleLexemCodes[0]) {
                    LlkContext secondProductionRuleContext = SecondProductionRule.getFirstFollowK();

                    for (int wordNumber = 0; wordNumber < firstProductionRuleContext.calcWords(); ++wordNumber)
                        if (secondProductionRuleContext.wordInContext(firstProductionRuleContext.getWord(wordNumber))) {
                            System.out.println(
                                    "пара " + this.getLexemText(firstProductionRuleLexemCodes[0]) + "-правил " +
                                    "(" + secondProductionRuleNumber + ", " + firstProductionRuleNumber + ") " +
                                    "не задовольняє сильну LL(" + this.getLlkConst() + ")-умову");
                            return false;
                        }
                }
            }
        }

        System.out.println("Граматика задовольняє сильну LL(" + this.getLlkConst() + ")-умову");
        return true;
    }

    public int[][] createUprTable() {
        int[] term = this.getTerminals();
        int[] nonterm = this.getNonTerminals();
        if(this.LLK != 1) {
            System.out.println("Спроба побудувати таблицю управління для k=" + this.LLK);
            return null;
        } else {
            int[][] upr = new int[nonterm.length][term.length + 1];

            int ii;
            for (int ii1 = 0; ii1 < nonterm.length; ++ii1) for (ii = 0; ii < term.length + 1; ++ii) upr[ii1][ii] = 0;

            int rowLine = 0;

            for (Node tmp : this.getLanguage()) {
                ++rowLine;
                int[] role = tmp.getRule();
                int rowIndex = this.indexNonTerminal(role[0]);
                LlkContext cont = tmp.getFirstFollowK();

                for (ii = 0; ii < cont.calcWords(); ++ii) {
                    int[] word = cont.getWord(ii);
                    int colIndex;
                    if (word.length == 0) colIndex = this.getTerminals().length;
                    else colIndex = this.indexTerminal(word[0]);

                    if (upr[rowIndex][colIndex] != 0) {
                        System.out.println("При побудові таблиці управління для нетермінала " + this.getLexemText(nonterm[rowIndex]) + " порушшено LL(1)-властивість");
                        return null;
                    }

                    upr[rowIndex][colIndex] = rowLine;
                }
            }

            System.out.println("Таблиця управління LL(1)-аналізу побудована ");
            return upr;
        }
    }

    public int[][] createUprTableWithCollision() {
        int[] term = this.getTerminals();
        int[] nonterm = this.getNonTerminals();
        if (this.LLK != 1) {
            System.out.println("Спроба побудувати таблицю управління для k=" + this.LLK);
            return null;
        } else {
            int[][] upr = new int[nonterm.length][term.length + 1];

            int ii;
            for (int ii1 = 0; ii1 < nonterm.length; ++ii1) for (ii = 0; ii < term.length + 1; ++ii) upr[ii1][ii] = 0;

            int rowline = 0;

            for (Node tmp : this.getLanguage()) {
                ++rowline;
                int[] role = tmp.getRule();
                int rowindex = this.indexNonTerminal(role[0]);
                LlkContext cont = tmp.getFirstFollowK();

                for (ii = 0; ii < cont.calcWords(); ++ii) {
                    int[] word = cont.getWord(ii);
                    int var16;
                    if (word.length == 0) var16 = this.getTerminals().length;
                    else var16 = this.indexTerminal(word[0]);

                    if (upr[rowindex][var16] != 0) upr[rowindex][var16] = -1;
                    else upr[rowindex][var16] = rowline;
                }
            }

            System.out.println("Таблиця управління LL(1)-аналізу побудована ");
            return upr;
        }
    }

    private boolean newCalcIndex(int[] mult, int[] maxmult, int realCalc) {
        for (int ii = realCalc - 1; ii >= 0; --ii) {
            if (mult[ii] + 1 != maxmult[ii]) {
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
        for (ii = 0; ii < realCalc; ++ii) {
            int[] wordtmp = tmp[ii].getWord(mult[ii]);

            for (int word1 = 0; word1 < wordtmp.length && llkTmp != LLK; ++word1) word[llkTmp++] = wordtmp[word1];

            if (llkTmp == LLK) break;
        }

        int[] var10 = new int[llkTmp];

        for (ii = 0; ii < llkTmp; ++ii) var10[ii] = word[ii];

        return var10;
    }

    private int getLlkConst() {
        return this.LLK;
    }

    private int getLexemCode(String lexem, int lexemClass) {
        Iterator i$ = this.lexemsTable.iterator();

        TableNode tmp;
        do {
            if (!i$.hasNext()) return 0;

            tmp = (TableNode)i$.next();
        } while (!tmp.getLexemText().equals(lexem) || (tmp.getLexemCode() & -16777216) != lexemClass);

        return tmp.getLexemCode();
    }

    private LlkContext[] getLlkTrmContext() {
        return this.termLanguage;
    }

    private LlkContext[] createTerminalLang() {
        LlkContext[] cont = new LlkContext[this.terminals.length];

        for (int ii = 0; ii < this.terminals.length; ++ii) {
            int[] trmWord = new int[]{this.terminals[ii]};
            cont[ii] = new LlkContext();
            cont[ii].addWord(trmWord);
        }

        return cont;
    }

    String getLexemText(int code) {
        Iterator i$ = this.lexemsTable.iterator();

        TableNode tmp;
        do {
            if (!i$.hasNext()) return null;

            tmp = (TableNode)i$.next();
        } while (tmp.getLexemCode() != code);

        return tmp.getLexemText();
    }

    int[] getTerminals() {
        return this.terminals;
    }

    int[] getNonTerminals() {
        return this.nonTerminals;
    }

    private int[] getEpsilonNonTerminals() {
        return this.epsilonTerminals;
    }

    LinkedList<Node> getLanguage() {
        return this.language;
    }

    public void printEpsilonNonTerminals() {
        System.out.println("СПИСОК E-терміналів:");
        if (this.epsilonTerminals != null) for (int epsilonTerminal : this.epsilonTerminals)
            System.out.println("" + epsilonTerminal + "\t" + this.getLexemText(epsilonTerminal));
    }

    public void setEpsilonNonTerminals(int[] eps) {
        this.epsilonTerminals = eps;
    }

    public int[] createEpsilonNonTerminals() {
        int[] terminal = new int[this.nonTerminals.length];
        int count = 0;
        Iterator result = this.language.iterator();

        Node tmp;
        while (result.hasNext()) {
            tmp = (Node)result.next();
            tmp.setTag(0);
        }

        boolean upr;
        int ii;
        label78:
        do {
            upr = false;
            result = this.language.iterator();

            while(true) {
                int[] role;
                do {
                    if (!result.hasNext()) continue label78;

                    tmp = (Node)result.next();
                    role = tmp.getRule();

                    for (ii = 1; ii < role.length && role[ii] <= 0; ++ii) {
                        int ii1 = 0;
                        while (ii1 < count && terminal[ii1] != role[ii]) ++ii1;

                        if (ii1 == count) break;
                    }
                } while (ii != role.length);

                int ii2 = 0;
                while (ii2 < count && terminal[ii2] != role[0]) ++ii2;

                if (ii2 == count) {
                    terminal[count++] = role[0];
                    upr = true;
                }
            }
        } while (upr);

        int[] var10 = new int[count];

        for (ii = 0; ii < count; ++ii) var10[ii] = terminal[ii];

        return var10;
    }

    public void printGrammar() {
        int count = 0;

        for (Node tmp : this.language) {
            int[] roole = tmp.getRule();
            ++count;
            System.out.print("" + count + " ");

            for (int ii = 0; ii < roole.length; ++ii) {
                if (ii == 1) System.out.print(" -> ");

                System.out.print(this.getLexemText(roole[ii]) + " ");
                if (roole.length == 1) System.out.print(" -> ");
            }

            System.out.println();
        }

    }

    private int[] createTerminals() {
        int count = 0;
        Iterator i$ = this.lexemsTable.iterator();

        TableNode tmp;
        while(i$.hasNext()) {
            tmp = (TableNode)i$.next();
            if(tmp.getLexemCode() > 0) ++count;
        }

        int[] terminal = new int[count];
        count = 0;
        i$ = this.lexemsTable.iterator();

        while(i$.hasNext()) {
            tmp = (TableNode)i$.next();
            if(tmp.getLexemCode() > 0) {
                terminal[count] = tmp.getLexemCode();
                ++count;
            }
        }

        return terminal;
    }

    private int[] createNonterminals() {
        int count = 0;
        Iterator i$ = this.lexemsTable.iterator();

        TableNode tmp;
        while(i$.hasNext()) {
            tmp = (TableNode)i$.next();
            if(tmp.getLexemCode() < 0) ++count;
        }

        int[] nonTerminal = new int[count];
        count = 0;
        i$ = this.lexemsTable.iterator();

        while(i$.hasNext()) {
            tmp = (TableNode)i$.next();
            if(tmp.getLexemCode() < 0) {
                nonTerminal[count] = tmp.getLexemCode();
                ++count;
            }
        }

        return nonTerminal;
    }

    private void setAxiom(int axiom0) {
        this.axiom = axiom0;
    }

    int getAxiom() {
        return this.axiom;
    }

    private void readGrammar(String filename) {
        char[] lexem = new char[180];
        int[] rule = new int[80];

        BufferedReader s;
        try {
            s = new BufferedReader(new FileReader(filename.trim()));
        } catch (FileNotFoundException var24) {
            System.out.print("Файл:" + filename.trim() + " не відкрито\n");
            this.create = false;
            return;
        }

        for (int i = 0; i < lexem.length; ++i) lexem[i] = 0;

        int[] var27 = new int[80];

        int line;
        for (line = 0; line < var27.length; ++line) var27[line] = 0;

        int pos = 0;
        byte q = 0;
        int posRule = 0;
        line = 0;
        String readline = null;
        int readPos = 0;
        int readlen = 0;

        try {
            int newLexemCode;
            TableNode nodeTmp;
            Node nod;
            while (s.ready()) {
                if (readline == null || readPos >= readlen) {
                    readline = s.readLine();
                    if (line == 0 && readline.charAt(0) == '\ufeff') readline = readline.substring(1);

                    readlen = readline.length();
                    ++line;
                }

                for (readPos = 0; readPos < readlen; ++readPos) {
                    char letter = readline.charAt(readPos);
                    String e;
                    boolean strTmp;
                    Iterator iterator;
                    TableNode iterator1;
                    switch (q) {
                        case 0:
                            if (letter == 32 || letter == 9 || letter == 13 || letter == 10 || letter == 8) break;

                            if (readPos == 0 && posRule > 0 && (letter == 33 || letter == 35)) {
                                nod = new Node(rule, posRule);
                                this.language.add(nod);
                                if (letter == 33) {
                                    posRule = 1;
                                    break;
                                }

                                posRule = 0;
                            }

                            byte var26 = 0;
                            pos = var26 + 1;
                            lexem[var26] = letter;
                            if (letter == 35) q = 1;
                            else if (letter == 92) {
                                --pos;
                                q = 3;
                            } else q = 2;
                            break;
                        case 1:
                            lexem[pos++] = letter;
                            if(letter != 35 && readPos != 0) break;

                            e = new String(lexem, 0, pos);
                            nodeTmp = new TableNode(e, -2147483648);
                            strTmp = true;
                            iterator = this.lexemsTable.iterator();

                            while (iterator.hasNext()) {
                                iterator1 = (TableNode)iterator.next();
                                if (nodeTmp.equals(iterator1)) {
                                    strTmp = false;
                                    break;
                                }
                            }

                            if (strTmp) this.lexemsTable.add(nodeTmp);

                            newLexemCode = this.getLexemCode(e, -2147483648);
                            rule[posRule++] = newLexemCode;
                            pos = 0;
                            q = 0;
                            break;
                        case 2:
                            if(letter == 92) {
                                --pos;
                                q = 3;
                            } else {
                                if(letter != 32 && readPos != 0 && letter != 35 && letter != 13 && letter != 9) {
                                    lexem[pos++] = letter;
                                    continue;
                                }

                                e = new String(lexem, 0, pos);
                                nodeTmp = new TableNode(e, 268435456);
                                strTmp = true;
                                iterator = this.lexemsTable.iterator();

                                while(iterator.hasNext()) {
                                    iterator1 = (TableNode)iterator.next();
                                    if(nodeTmp.equals(iterator1)) {
                                        strTmp = false;
                                        break;
                                    }
                                }

                                if(strTmp) this.lexemsTable.add(nodeTmp);

                                newLexemCode = this.getLexemCode(e, 268435456);
                                rule[posRule++] = newLexemCode;
                                pos = 0;
                                q = 0;
                                --readPos;
                            }
                            break;
                        case 3:
                            lexem[pos++] = letter;
                            q = 2;
                    }
                }
            }

            if (pos != 0) {
                int var29;
                if (q == 1) var29 = -2147483648;
                else var29 = 268435456;

                String var31 = new String(lexem, 0, pos);
                nodeTmp = new TableNode(var31, var29);
                boolean var30 = true;

                for (TableNode tmp : this.lexemsTable)
                    if (nodeTmp.equals(tmp)) {
                        var30 = false;
                        break;
                    }

                if (var30) this.lexemsTable.add(nodeTmp);

                newLexemCode = this.getLexemCode(var31, var29);
                rule[posRule++] = newLexemCode;
            }

            if (posRule > 0) {
                nod = new Node(rule, posRule);
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
            for (MyLang.TmpList tmpLst = this; tmpLst != null; tmpLst = tmpLst.treeFather) if (tmpLst.treeNode.getRule()[0] == tmp.getRule()[0]) return true;
            return false;
        }

        private boolean searchLeftRecursion(MyLang lang) {
            int[] epsilon = lang.getEpsilonNonTerminals();
            MyLang.TmpList tmpLst = this;
            while (tmpLst.treeFather != null) tmpLst = tmpLst.treeFather;

            Node root = tmpLst.treeNode;
            if (root.getRule()[0] == this.treeNode.getRule()[this.treePos]) {
                System.out.println("Ліворекурсивний вивод");
                this.printLeftRecursion(lang);
                return true;
            } else {
                Iterator i$ = lang.getLanguage().iterator();

                while(true) {
                    Node tmp;
                    int[] role;
                    do {
                        do do {
                            if (!i$.hasNext()) return false;

                            tmp = (Node) i$.next();
                        } while (tmp.getTag() == 1); while (this.roleInTree(tmp));

                        role = tmp.getRule();
                    } while (role[0] != this.treeNode.getRule()[this.treePos]);

                    for (int ii = 1; ii < role.length && role[ii] <= 0; ++ii) {
                        MyLang.TmpList tree1 = MyLang.this.new TmpList(this, tmp, ii);
                        if (tree1.searchLeftRecursion(lang)) {
                            tree1.destroy();
                            return true;
                        }

                        tree1.destroy();

                        int ii1 = 0;
                        while (ii1 < epsilon.length && epsilon[ii1] != role[ii]) ++ii1;

                        if (ii1 == epsilon.length) break;
                    }
                }
            }
        }

        private void printLeftRecursion(MyLang lang) {
            if (this.treeFather != null) {
                MyLang.TmpList tmp = this.treeFather;
                tmp.printLeftRecursion(lang);
            }

            int[] tmpRole = this.treeNode.getRule();

            int ii;
            for (ii = 0; ii < tmpRole.length; ++ii)
                if (ii == 0) System.out.print(lang.getLexemText(tmpRole[ii]) + " -> ");
                else System.out.print(lang.getLexemText(tmpRole[ii]) + " ");

            System.out.println();

            for (ii = 1; ii < this.treePos; ++ii) System.out.println(lang.getLexemText(tmpRole[ii]) + " =>* epsilon");
        }

        private boolean searchRightRecursion(MyLang lang) {
            int[] epsilon = lang.getEpsilonNonTerminals();

            MyLang.TmpList tmpLst = this;
            while (tmpLst.treeFather != null) tmpLst = tmpLst.treeFather;

            Node root = tmpLst.treeNode;
            if (root.getRule()[0] == this.treeNode.getRule()[this.treePos]) {
                System.out.println("\nПраворекурсивний вивод:");
                this.printRightRecursion(lang);
                return true;
            } else {
                Iterator i$ = lang.getLanguage().iterator();

                while (true) {
                    Node tmp;
                    int[] rule;
                    do {
                        do {
                            if (!i$.hasNext()) return false;

                            tmp = (Node) i$.next();
                        } while (tmp.getTag() == 1 && this.roleInTree(tmp));

                        rule = tmp.getRule();
                    } while (rule.length <= 1 && rule[rule.length - 1] > 0 && rule[0] != this.treeNode.getRule()[this.treePos]);

                    for (int ii = rule.length - 1; ii > 0 && rule[ii] <= 0; --ii) {
                        MyLang.TmpList tree1 = MyLang.this.new TmpList(this, tmp, ii);
                        if (tree1.searchRightRecursion(lang)) {
                            tree1.destroy();
                            return true;
                        }

                        tree1.destroy();

                        int ii1 = 0;
                        while (ii1 < epsilon.length && epsilon[ii1] != rule[ii]) ++ii1;

                        if (ii1 == epsilon.length) break;
                    }
                }
            }
        }

        private void printRightRecursion(MyLang lang) {
            if (this.treeFather != null) {
                MyLang.TmpList tmp = this.treeFather;
                tmp.printRightRecursion(lang);
            }

            int[] tmpRule = this.treeNode.getRule();

            for (int i = 0; i < tmpRule.length; ++i)
                if (i == 0) System.out.print(lang.getLexemText(tmpRule[i]) + " -> ");
                else System.out.print(lang.getLexemText(tmpRule[i]) + " ");

            System.out.println();

            for (int i = tmpRule.length - 1; i > this.treePos; --i)
                System.out.println(lang.getLexemText(tmpRule[i]) + " =>* epsilon");
        }
    }
}