package JavaTeacherLib;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Stack;

public class PL0Parser {
    private int[] scanerUprTable;
    private int[][] parserUprTable;
    private MyLang lang;
    private byte[] lexema;
    private int lexemaCode;
    private int lexemaLine;
    private int lexemaPos;
    private int lexemaLen;
    private int lexemaNumb;
    private int errors;
    private int ungetChar;
    private String fileData;
    private BufferedReader fs;
    private int maxLenRecord;

    public PL0Parser(MyLang lang0, String fname) throws IOException {
        this.lang = lang0;
        this.lexema = new byte[180];
        this.lexemaLen = 0;
        this.lexemaLine = 0;
        this.lexemaNumb = 0;
        this.errors = 0;
        this.ungetChar = 0;
        this.lexemaPos = 0;
        this.parserUprTable = this.lang.getUprTable();
        this.scanerUprTable = new int[256];

        for(int ee = 0; ee < this.scanerUprTable.length; ++ee) this.scanerUprTable[ee] = 0;

        this.setClassLitera(".;,+-()*=", 1);
        this.setClassLitera("abcdefghijklmnopqrstuvwxyz_", 2);
        this.setClassLitera("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 2);
        this.setClassLitera("0123456789", 3);
        this.maxLenRecord = 0;

        try {
            this.fs = new BufferedReader(new FileReader(fname));
        } catch (Exception var4) {
            System.out.println("Файл програми " + fname + " не відкрито");
            throw new IOException();
        }
    }

    public void parserStack() {
        Stack ss = new Stack();
        ss.push(Integer.valueOf(this.lang.getAxiom()));
        this.lexemaCode = this.pl0Scaner();

        while(true) {
            while(!ss.empty()) {
                int ssItem = ((Integer)ss.peek()).intValue();
                if(ssItem > 0) if (ssItem == this.lexemaCode) {
                    ss.pop();
                    this.lexemaCode = this.pl0Scaner();
                } else {
                    if (!this.lang.getLexemText(ssItem).equals("else")) {
                        System.out.println("\nПропущена лексема :" + this.lang.getLexemText(ssItem) + "код на вході:" + this.lexemaCode);

                        do this.lexemaCode = this.pl0Scaner(); while (this.lexemaCode != -1);

                        System.out.println("\nПрограма має синтаксичні помилки");

                        try {
                            this.fs.close();
                            return;
                        } catch (Exception var11) {
                            return;
                        }
                    }

                    ss.pop();
                    ss.pop();
                }
                else {
                    int nontermcol = this.lang.indexNonTerminal(ssItem);
                    int termCol;
                    if(this.lexemaCode == -1) termCol = this.lang.getTerminals().length;
                    else termCol = this.lang.indexTerminal(this.lexemaCode);

                    if(this.parserUprTable[nontermcol][termCol] == 0) {
                        System.out.println("Синтаксична помилка: на вершині стека: " + this.lang.getLexemText(ssItem) + " поточна лексема " + this.lexemaCode + " " + this.getLexemText());

                        do this.lexemaCode = this.pl0Scaner(); while(this.lexemaCode != -1);

                        System.out.println("\nПрограма має синтаксичні помилки");

                        try {
                            this.fs.close();
                            return;
                        } catch (Exception var12) {
                            return;
                        }
                    }

                    boolean numrole = false;
                    int var14 = this.parserUprTable[nontermcol][termCol];
                    int ee = 0;
                    Node tmp = null;
                    Iterator pravilo = this.lang.getLanguage().iterator();

                    while(pravilo.hasNext()) {
                        Node ii = (Node)pravilo.next();
                        ++ee;
                        if(var14 == ee) {
                            tmp = ii;
                            break;
                        }
                    }

                    int[] var15 = tmp.getRule();
                    ss.pop();

                    for(int var16 = var15.length - 1; var16 > 0; --var16) ss.push(Integer.valueOf(var15[var16]));
                }
            }

            if(this.lexemaCode != -1) System.out.println("Логічний кінець програми знайдено раніше ніж кінець файла ");
            else System.out.println("Програма синтаксично вірна");

            try {
                this.fs.close();
                this.fs = null;
                return;
            } catch (Exception var13) {
                return;
            }
        }
    }

    public void parserRecursive() {
        int[][] uprTable = this.lang.getUprTable();
        int[] term = this.lang.getTerminals();
        int[] nonterm = this.lang.getNonTerminals();
        this.lexemaCode = this.pl0Scaner();
        if(this.localrecursive(this.lang.getAxiom())) System.out.println("\nПрограма синтаксично вірна");
        else while (true) {
            if (this.lexemaCode == -1) {
                System.out.println("\nПрограма має синтаксичні помилки");
                break;
            }

            this.lexemaCode = this.pl0Scaner();
        }

        try {
            this.fs.close();
        } catch (Exception var7) {
        }
    }

    private boolean localrecursive(int nonterm) {
        int nontermcol = this.lang.indexNonTerminal(nonterm);
        int termCol;
        if(this.lexemaCode == -1) termCol = this.lang.getTerminals().length;
        else termCol = this.lang.indexTerminal(this.lexemaCode);

        if(this.parserUprTable[nontermcol][termCol] == 0) {
            System.out.println("Синтаксична помилка: старт для  " + this.lang.getLexemText(nonterm) + " поточна лексема " + this.lang.getLexemText(this.lexemaCode) + " " + this.getLexemText());

            do this.lexemaCode = this.pl0Scaner(); while(this.lexemaCode != -1);

            System.out.println("\nПрограма має синтаксичні помилки");

            try {
                this.fs.close();
                return false;
            } catch (Exception var9) {
                return false;
            }
        } else {
            boolean numrole = false;
            int var10 = this.parserUprTable[nontermcol][termCol];
            int iitmp = 0;
            Node tmp = null;
            Iterator pravilo = this.lang.getLanguage().iterator();

            while(pravilo.hasNext()) {
                Node ii = (Node)pravilo.next();
                ++iitmp;
                if(var10 == iitmp) {
                    tmp = ii;
                    break;
                }
            }

            int[] var11 = tmp.getRule();

            for(int var12 = 1; var12 < var11.length; ++var12)
                if (var11[var12] > 0) if (var11[var12] == this.lexemaCode) this.lexemaCode = this.pl0Scaner();
                else {
                    if (!this.lang.getLexemText(var11[var12]).equals("else")) {
                        System.out.println("\nПропущена лексема :" + this.lang.getLexemText(var11[var12]));
                        return false;
                    }

                    var12 += 2;
                }
                else if (!this.localrecursive(var11[var12])) return false;

            return true;
        }
    }

    private void setClassLitera(String uprString, int initCode) {
        for(int ii = 0; ii < uprString.length(); ++ii) this.scanerUprTable[uprString.charAt(ii)] = initCode;

    }

    public String getLexemText() {
        byte[] textLexema = new byte[this.lexemaLen];

        for(int ii = 0; ii < this.lexemaLen; ++ii) textLexema[ii] = this.lexema[ii];

        return new String(textLexema);
    }

    public int pl0Scaner() {
        byte q = 0;
        this.lexemaLen = 0;
        int litera = 0;
        boolean lexClass = false;
        byte lexemaClass = 0;
        boolean litConst = false;

        try {
            int ee;
            while(this.fs.ready() || this.lexemaPos <= this.maxLenRecord) {
                if(this.fileData == null) {
                    this.fileData = this.fs.readLine();
                    System.out.println(this.fileData);
                    ++this.lexemaLine;
                    this.lexemaPos = 0;
                    this.maxLenRecord = this.fileData.length();
                }

                while(this.ungetChar != 0 || this.lexemaPos <= this.maxLenRecord) {
                    if(this.ungetChar != 0) {
                        litera = this.ungetChar;
                        this.ungetChar = 0;
                    } else {
                        if(this.lexemaPos < this.fileData.length()) litera = this.fileData.charAt(this.lexemaPos);

                        if(this.lexemaPos == this.fileData.length()) litera = 10;

                        ++this.lexemaPos;
                    }

                    litera &= 255;
                    int var8 = this.scanerUprTable[litera];
                    switch(q) {
                        case 0:
                            if(litera == 10) ++this.lexemaLine;
                            else if(litera != 9 && litera != 13 && litera != 32 && litera != 8) {
                                ++this.lexemaNumb;
                                this.lexemaLen = 0;
                                switch(var8) {
                                    case 1:
                                        this.lexema[this.lexemaLen++] = (byte)litera;
                                        return this.lang.getLexemCode(this.lexema, this.lexemaLen);
                                    case 2:
                                        this.lexema[this.lexemaLen++] = (byte)litera;
                                        q = 2;
                                        break;
                                    case 3:
                                        this.lexema[this.lexemaLen++] = (byte)litera;
                                        q = 3;
                                }

                                if(q == 0) {
                                    this.lexema[this.lexemaLen++] = (byte)litera;
                                    lexemaClass = 5;
                                    if(litera == 58) q = 4;
                                    else if(litera == 62) q = 5;
                                    else if(litera == 60) q = 6;
                                    else if(litera == 33) q = 7;
                                    else if(litera == 47) q = 8;
                                    else {
                                        q = 11;
                                        ++this.errors;
                                    }
                                }
                            }
                            break;
                        case 2:
                            if(var8 != 2 && var8 != 3) {
                                this.ungetChar = litera;
                                ee = this.lang.getLexemCode(this.lexema, this.lexemaLen);
                                if(ee != -1) return ee;

                                return 268435560;
                            }

                            this.lexema[this.lexemaLen++] = (byte)litera;
                            break;
                        case 3:
                            if(var8 != 3) {
                                this.ungetChar = litera;
                                return 268435562;
                            }

                            this.lexema[this.lexemaLen++] = (byte)litera;
                            break;
                        case 4:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera == 61) return 268435499;

                            this.ungetChar = litera;
                            --this.lexemaLen;
                            q = 11;
                            break;
                        case 5:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera == 61) return 268435529;

                            this.ungetChar = litera;
                            --this.lexemaLen;
                            return 268435528;
                        case 6:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera == 61) return 268435530;

                            this.ungetChar = litera;
                            --this.lexemaLen;
                            return 268435527;
                        case 7:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera == 61) return 268435526;

                            this.ungetChar = litera;
                            --this.lexemaLen;
                            q = 11;
                            break;
                        case 8:
                            if(litera != 42) {
                                this.ungetChar = litera;
                                return 268435551;
                            }

                            q = 81;
                            break;
                        case 11:
                            return -1;
                        case 81:
                            if(litera == 42) q = 82;
                            break;
                        case 82:
                            if(litera == 47) {
                                this.lexemaLen = 0;
                                q = 0;
                            } else q = 81;
                    }
                }

                this.fileData = null;
            }

            if(this.lexemaLen == 0) return -1;
            else switch (lexemaClass) {
                case 1:
                    ee = this.lang.getLexemCode(this.lexema, this.lexemaLen);
                    return ee;
                case 2:
                    return 268435560;
                case 3:
                    return 268435562;
                case 5:
                    this.lang.getLexemCode(this.lexema, this.lexemaLen);
                case 4:
                default:
                    return -1;
            }
        } catch (IOException var7) {
            System.out.print("Помилка вводу-виводу: " + var7.toString());
            return -1;
        }
    }
}
