//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package JavaTeacherLib;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Stack;

public class PascalParser {
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
    private int ungetLexemCode;
    private static final int MAX_IDN = 32;
    private BufferedReader fs;
    private int maxLenRecord;

    public PascalParser(MyLang lang0, String fname) throws IOException {
        this.lang = lang0;
        this.lexema = new byte[180];
        this.lexemaLen = 0;
        this.lexemaLine = 0;
        this.lexemaNumb = 0;
        this.errors = 0;
        this.ungetChar = 0;
        this.ungetLexemCode = 0;
        this.lexemaPos = 0;
        this.parserUprTable = this.lang.getUprTable();
        this.scanerUprTable = new int[256];
        this.maxLenRecord = 0;

        for(int ee = 0; ee < this.scanerUprTable.length; ++ee) {
            this.scanerUprTable[ee] = 0;
        }

        this.setClassLitera("()[];,+-^=*", 1);
        this.setClassLitera("abcdefghijklmnopqrstuvwxyz_", 2);
        this.setClassLitera("ABCDEFGHIJKLMNOPQRSTUVWXYZ", 2);
        this.setClassLitera("0123456789", 3);

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
        this.lexemaCode = this.pascalScaner();

        while(true) {
            while(!ss.empty()) {
                int ssItem = ((Integer)ss.peek()).intValue();
                if(ssItem > 0) {
                    if(ssItem == this.lexemaCode) {
                        ss.pop();
                        this.lexemaCode = this.pascalScaner();
                    } else {
                        if(!this.lang.getLexemText(ssItem).equals("else")) {
                            System.out.println("\nПропущена лексема :" + this.lang.getLexemText(ssItem));

                            do {
                                this.lexemaCode = this.pascalScaner();
                            } while(this.lexemaCode != -1);

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
                } else {
                    int nontermcol = this.lang.indexNonTerminal(ssItem);
                    int termCol;
                    if(this.lexemaCode == -1) {
                        termCol = this.lang.getTerminals().length;
                    } else {
                        termCol = this.lang.indexTerminal(this.lexemaCode);
                    }

                    if(this.parserUprTable[nontermcol][termCol] == 0) {
                        System.out.println("Синтаксична помилка: на вершині стека: " + this.lang.getLexemText(ssItem) + " поточна лексема " + this.lang.getLexemText(this.lexemaCode) + " " + this.getLexemText());

                        do {
                            this.lexemaCode = this.pascalScaner();
                        } while(this.lexemaCode != -1);

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

                    for(int var16 = var15.length - 1; var16 > 0; --var16) {
                        ss.push(Integer.valueOf(var15[var16]));
                    }
                }
            }

            if(this.lexemaCode != -1) {
                System.out.println("Логічний кінець програми знайдено раніше ніж кінець файла ");
            } else {
                System.out.println("Програма синтаксично вірна");
            }

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
        this.lexemaCode = this.pascalScaner();
        if(this.localrecursive(this.lang.getAxiom())) {
            System.out.println("\nПрограма синтаксично вірна");
        } else {
            while(true) {
                if(this.lexemaCode == -1) {
                    System.out.println("\nПрограма має синтаксичні помилки");
                    break;
                }

                this.lexemaCode = this.pascalScaner();
            }
        }

        try {
            this.fs.close();
        } catch (Exception var7) {
        }
    }

    private boolean localrecursive(int nonterm) {
        int nontermcol = this.lang.indexNonTerminal(nonterm);
        int termCol;
        if(this.lexemaCode == -1) {
            termCol = this.lang.getTerminals().length;
        } else {
            termCol = this.lang.indexTerminal(this.lexemaCode);
        }

        if(this.parserUprTable[nontermcol][termCol] == 0) {
            System.out.println("Синтаксична помилка: старт для  " + this.lang.getLexemText(nonterm) + " поточна лексема " + this.lang.getLexemText(this.lexemaCode) + " " + this.getLexemText());
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

            for(int var12 = 1; var12 < var11.length; ++var12) {
                if(var11[var12] > 0) {
                    if(var11[var12] == this.lexemaCode) {
                        this.lexemaCode = this.pascalScaner();
                    } else {
                        if(!this.lang.getLexemText(var11[var12]).equals("else")) {
                            System.out.println("\nПропущена лексема :" + this.lang.getLexemText(var11[var12]));
                            return false;
                        }

                        var12 += 2;
                    }
                } else if(!this.localrecursive(var11[var12])) {
                    return false;
                }
            }

            return true;
        }
    }

    private void setClassLitera(String uprString, int initCode) {
        for(int ii = 0; ii < uprString.length(); ++ii) {
            this.scanerUprTable[uprString.charAt(ii)] = initCode;
        }

    }

    private void setUngetLexema(int secLexema) {
        this.ungetLexemCode = secLexema;
    }

    public String getLexemText() {
        byte[] textLexema = new byte[this.lexemaLen];

        for(int ii = 0; ii < this.lexemaLen; ++ii) {
            textLexema[ii] = this.lexema[ii];
        }

        return new String(textLexema);
    }

    public int pascalScaner() {
        byte q = 0;
        this.lexemaLen = 0;
        int litera = 0;
        boolean lexClass = false;
        byte lexemaClass = 0;
        boolean litConst = false;
        String errorLoc = "^[]{},?: ; \t\n\u0000";

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
                        if(this.lexemaPos < this.fileData.length()) {
                            litera = this.fileData.charAt(this.lexemaPos);
                        }

                        if(this.lexemaPos == this.fileData.length()) {
                            litera = 10;
                        }

                        ++this.lexemaPos;
                    }

                    litera &= 255;
                    int var9 = this.scanerUprTable[litera];
                    switch(q) {
                        case 0:
                            if(litera == 10) {
                                ++this.lexemaLine;
                            } else if(litera != 9 && litera != 13 && litera != 32 && litera != 8) {
                                ++this.lexemaNumb;
                                this.lexemaLen = 0;
                                switch(var9) {
                                    case 1:
                                        this.lexema[this.lexemaLen++] = (byte)litera;
                                        return this.lang.getLexemCode(this.lexema, this.lexemaLen);
                                    case 2:
                                        this.lexema[this.lexemaLen++] = (byte)litera;
                                        lexemaClass = 1;
                                        q = 21;
                                        break;
                                    case 3:
                                        this.lexema[this.lexemaLen++] = (byte)litera;
                                        lexemaClass = 2;
                                        q = 31;
                                }

                                if(q == 0) {
                                    this.lexema[this.lexemaLen++] = (byte)litera;
                                    lexemaClass = 5;
                                    if(litera == 46) {
                                        q = 4;
                                    } else if(litera == 58) {
                                        q = 5;
                                    } else if(litera == 47) {
                                        q = 6;
                                    } else if(litera == 62) {
                                        q = 7;
                                    } else if(litera == 60) {
                                        q = 8;
                                    } else if(litera == 33) {
                                        q = 9;
                                    } else if(litera == 39) {
                                        q = 10;
                                    } else {
                                        q = 11;
                                        ++this.errors;
                                    }
                                }
                            }
                        case 1:
                        case 2:
                        case 3:
                        case 12:
                        case 13:
                        case 14:
                        case 15:
                        case 16:
                        case 17:
                        case 18:
                        case 19:
                        case 20:
                        case 22:
                        case 23:
                        case 24:
                        case 25:
                        case 26:
                        case 27:
                        case 28:
                        case 29:
                        case 30:
                        case 36:
                        case 37:
                        case 38:
                        case 39:
                        case 40:
                        case 41:
                        case 42:
                        case 43:
                        case 44:
                        case 45:
                        case 46:
                        case 47:
                        case 48:
                        case 49:
                        case 50:
                        case 51:
                        case 52:
                        case 53:
                        case 54:
                        case 55:
                        case 56:
                        case 57:
                        case 58:
                        case 59:
                        case 60:
                        default:
                            break;
                        case 4:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera >= 48 && litera <= 57) {
                                q = 32;
                                break;
                            }

                            if(litera == 46) {
                                return 268435741;
                            }

                            this.ungetChar = litera;
                            --this.lexemaLen;
                            return 268435464;
                        case 5:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera == 61) {
                                return 268435577;
                            }

                            this.ungetChar = litera;
                            --this.lexemaLen;
                            return 268435530;
                        case 6:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera != 42) {
                                this.ungetChar = litera;
                                --this.lexemaLen;
                                return 268435710;
                            }

                            q = 61;
                            break;
                        case 7:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera == 61) {
                                return 268435683;
                            }

                            this.ungetChar = litera;
                            --this.lexemaLen;
                            return 268435681;
                        case 8:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera == 61) {
                                return 268435684;
                            }

                            this.ungetChar = litera;
                            --this.lexemaLen;
                            return 268435680;
                        case 9:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera == 61) {
                                return 268435682;
                            }

                            this.ungetChar = litera;
                            --this.lexemaLen;
                            q = 11;
                            break;
                        case 10:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera == 39) {
                                return 268435472;
                            }
                            break;
                        case 11:
                            if(errorLoc.indexOf(litera) >= 0) {
                                this.ungetChar = litera;
                                q = 0;
                            }
                            break;
                        case 21:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(var9 != 2 && var9 != 3) {
                                this.ungetChar = litera;
                                --this.lexemaLen;
                                ee = this.lang.getLexemCode(this.lexema, this.lexemaLen);
                                if(ee == -1) {
                                    return 268435470;
                                }

                                return ee;
                            }
                            break;
                        case 31:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera >= 48 && litera <= 57) {
                                break;
                            }

                            if(litera != 46) {
                                this.ungetChar = litera;
                                --this.lexemaLen;
                                return 268435468;
                            }

                            q = 32;
                            break;
                        case 32:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera >= 48 && litera <= 57) {
                                break;
                            }

                            if(litera != 101 && litera != 69) {
                                this.ungetChar = litera;
                                --this.lexemaLen;
                                return 268435468;
                            }

                            q = 33;
                            break;
                        case 33:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera >= 48 && litera <= 57) {
                                q = 35;
                            } else {
                                if(litera != 43 && litera > 45) {
                                    q = 11;
                                    continue;
                                }

                                q = 34;
                            }
                            break;
                        case 34:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera >= 48 && litera <= 57) {
                                q = 35;
                                break;
                            }

                            q = 11;
                            this.ungetChar = litera;
                            --this.lexemaLen;
                            break;
                        case 35:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera >= 48 && litera <= 57) {
                                break;
                            }

                            this.ungetChar = litera;
                            --this.lexemaLen;
                            return 268435468;
                        case 61:
                            if(litera == 42) {
                                q = 62;
                            }
                            break;
                        case 62:
                            if(litera == 47) {
                                q = 0;
                                this.lexemaLen = 0;
                            } else {
                                q = 61;
                            }
                    }
                }

                this.fileData = null;
            }

            if(this.lexemaLen == 0) {
                return -1;
            } else {
                switch(lexemaClass) {
                    case 1:
                        ee = this.lang.getLexemCode(this.lexema, this.lexemaLen);
                        if(ee != -1) {
                            return ee;
                        }

                        return 268435462;
                    case 2:
                        return 268435464;
                    case 3:
                        return -1;
                    case 4:
                        return 268435464;
                    case 5:
                        this.lang.getLexemCode(this.lexema, this.lexemaLen);
                    default:
                        return -1;
                }
            }
        } catch (IOException var8) {
            System.out.print("Помилка вводу-виводу: " + var8.toString());
            return -1;
        }
    }
}
