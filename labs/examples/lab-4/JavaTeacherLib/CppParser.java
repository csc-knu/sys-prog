//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package JavaTeacherLib;

import JavaTeacherLib.MyLang;
import JavaTeacherLib.Node;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Stack;

public class CppParser {
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
    private int ungetLexemaCode;
    private static final int MAX_IDN = 32;
    private BufferedReader fs;

    public CppParser(MyLang lang0, String fname) throws IOException {
        this.lang = lang0;
        this.lexema = new byte[180];
        this.lexemaLen = 0;
        this.lexemaLine = 0;
        this.lexemaNumb = 0;
        this.errors = 0;
        this.ungetChar = 0;
        this.ungetLexemaCode = 0;
        this.lexemaPos = 0;
        this.parserUprTable = this.lang.getUprTable();
        this.scanerUprTable = new int[256];

        for(int ee = 0; ee < this.scanerUprTable.length; ++ee) {
            this.scanerUprTable[ee] = 0;
        }

        this.setClassLitera("()[];,:{}?~", 1);
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
        ss.push(Integer.valueOf(this.lang.getAxioma()));
        this.lexemaCode = this.CppScaner();

        while(true) {
            while(!ss.empty()) {
                int ssItem = ((Integer)ss.peek()).intValue();
                if(ssItem <= 0) {
                    int nontermcol = this.lang.indexNonterminal(ssItem);
                    int termCol;
                    if(this.lexemaCode == -1) {
                        termCol = this.lang.getTerminals().length;
                    } else {
                        termCol = this.lang.indexTerminal(this.lexemaCode);
                    }

                    if(this.parserUprTable[nontermcol][termCol] == 0) {
                        System.out.println("Синтаксична помилка: на вершині стека: " + this.lang.getLexemaText(ssItem) + " поточна лексема " + this.lang.getLexemaText(this.lexemaCode) + " " + this.getLexemaText());
                        System.out.println("\nПрограма має синтаксичні помилки");

                        try {
                            this.fs.close();
                            return;
                        } catch (Exception var12) {
                            return;
                        }
                    }

                    int numrole = 0;
                    int ee;
                    if(this.parserUprTable[nontermcol][termCol] < 0) {
                        ee = this.CppScaner();
                        this.setUngetLexema(ee);
                        switch(this.lang.getNonTerminals()[nontermcol]) {
                            case -2147483320:
                                if(this.lang.getLexemaText(ee).equals("}")) {
                                    numrole = 152;
                                } else {
                                    numrole = 151;
                                }
                                break;
                            case -2147483218:
                                if(ee != 268435481 && ee != 268435486 && ee != 268435487 && ee != 268435506 && ee != 268435488 && ee != 268435490 && ee != 268435479 && ee != 268435508 && ee != 268435482 && ee != 268435484 && ee != 268435492 && ee != 268435510) {
                                    numrole = 203;
                                } else {
                                    numrole = 202;
                                }
                                break;
                            case -2147483197:
                                if(ee != 268435481 && ee != 268435486 && ee != 268435487 && ee != 268435506 && ee != 268435488 && ee != 268435490 && ee != 268435479 && ee != 268435508 && ee != 268435482 && ee != 268435484 && ee != 268435492 && ee != 268435510) {
                                    numrole = 205;
                                } else {
                                    numrole = 204;
                                }
                                break;
                            case -2147483144:
                                if(ee != 268435716 && ee != 268435706) {
                                    numrole = 225;
                                } else {
                                    numrole = 224;
                                }
                                break;
                            case -2147483123:
                                if(ee == 268435615) {
                                    numrole = 234;
                                } else {
                                    numrole = 235;
                                }
                                break;
                            default:
                                System.out.println("\nПомилка: відсутня обробка:" + this.lang.getNonTerminals()[nontermcol] + this.lang.getLexemaText(this.lang.getNonTerminals()[nontermcol]));
                                return;
                        }
                    }

                    if(this.parserUprTable[nontermcol][termCol] > 0) {
                        numrole = this.parserUprTable[nontermcol][termCol];
                    }

                    ee = 0;
                    Node tmp = null;
                    Iterator pravilo = this.lang.getLanguarge().iterator();

                    while(pravilo.hasNext()) {
                        Node ii = (Node)pravilo.next();
                        ++ee;
                        if(numrole == ee) {
                            tmp = ii;
                            break;
                        }
                    }

                    int[] var14 = tmp.getRoole();
                    ss.pop();

                    for(int var15 = var14.length - 1; var15 > 0; --var15) {
                        ss.push(Integer.valueOf(var14[var15]));
                    }
                } else if(ssItem == this.lexemaCode) {
                    ss.pop();
                    this.lexemaCode = this.CppScaner();
                } else {
                    if(!this.lang.getLexemaText(ssItem).equals("else")) {
                        System.out.println("\nПропущена лексема :" + this.lang.getLexemaText(ssItem));
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
        this.lexemaCode = this.CppScaner();
        if(this.localrecursive(this.lang.getAxioma())) {
            System.out.println("\nПрограма синтаксично вірна");
        } else {
            while(true) {
                if(this.lexemaCode == -1) {
                    System.out.println("\nПрограма має синтаксичні помилки");
                    break;
                }

                this.lexemaCode = this.CppScaner();
            }
        }

        try {
            this.fs.close();
        } catch (Exception var7) {
            ;
        }
    }

    private boolean localrecursive(int nonterm) {
        int nontermcol = this.lang.indexNonterminal(nonterm);
        int termCol;
        if(this.lexemaCode == -1) {
            termCol = this.lang.getTerminals().length;
        } else {
            termCol = this.lang.indexTerminal(this.lexemaCode);
        }

        if(this.parserUprTable[nontermcol][termCol] == 0) {
            System.out.println("Синтаксична помилка: старт для  " + this.lang.getLexemaText(nonterm) + " поточна лексема " + this.lang.getLexemaText(this.lexemaCode) + " " + this.getLexemaText());
            System.out.println("\nПрограма має синтаксичні помилки");

            try {
                this.fs.close();
                return false;
            } catch (Exception var9) {
                return false;
            }
        } else {
            int numrole = 0;
            int iitmp;
            if(this.parserUprTable[nontermcol][termCol] < 0) {
                iitmp = this.CppScaner();
                this.setUngetLexema(iitmp);
                switch(this.lang.getNonTerminals()[nontermcol]) {
                    case -2147483320:
                        if(this.lang.getLexemaText(iitmp).equals("}")) {
                            numrole = 152;
                        } else {
                            numrole = 151;
                        }
                        break;
                    case -2147483218:
                        if(iitmp != 268435481 && iitmp != 268435486 && iitmp != 268435487 && iitmp != 268435506 && iitmp != 268435488 && iitmp != 268435490 && iitmp != 268435479 && iitmp != 268435508 && iitmp != 268435482 && iitmp != 268435484 && iitmp != 268435492 && iitmp != 268435510) {
                            numrole = 203;
                        } else {
                            numrole = 202;
                        }
                        break;
                    case -2147483197:
                        if(iitmp != 268435481 && iitmp != 268435486 && iitmp != 268435487 && iitmp != 268435506 && iitmp != 268435488 && iitmp != 268435490 && iitmp != 268435479 && iitmp != 268435508 && iitmp != 268435482 && iitmp != 268435484 && iitmp != 268435492 && iitmp != 268435510) {
                            numrole = 205;
                        } else {
                            numrole = 204;
                        }
                        break;
                    case -2147483144:
                        if(iitmp != 268435716 && iitmp != 268435706) {
                            numrole = 225;
                        } else {
                            numrole = 224;
                        }
                        break;
                    case -2147483123:
                        if(iitmp == 268435615) {
                            numrole = 234;
                        } else {
                            numrole = 235;
                        }
                        break;
                    default:
                        System.out.println("\nПомилка: відсутня обробка:" + this.lang.getNonTerminals()[nontermcol] + this.lang.getLexemaText(this.lang.getNonTerminals()[nontermcol]));
                        return false;
                }
            }

            if(this.parserUprTable[nontermcol][termCol] > 0) {
                numrole = this.parserUprTable[nontermcol][termCol];
            }

            iitmp = 0;
            Node tmp = null;
            Iterator pravilo = this.lang.getLanguarge().iterator();

            while(pravilo.hasNext()) {
                Node ii = (Node)pravilo.next();
                ++iitmp;
                if(numrole == iitmp) {
                    tmp = ii;
                    break;
                }
            }

            int[] var10 = tmp.getRoole();

            for(int var11 = 1; var11 < var10.length; ++var11) {
                if(var10[var11] > 0) {
                    if(var10[var11] == this.lexemaCode) {
                        this.lexemaCode = this.CppScaner();
                    } else {
                        if(!this.lang.getLexemaText(var10[var11]).equals("else")) {
                            System.out.println("\nПропущена лексема :" + this.lang.getLexemaText(var10[var11]));
                            return false;
                        }

                        var11 += 2;
                    }
                } else if(!this.localrecursive(var10[var11])) {
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

    public void setUngetLexema(int secLexema) {
        this.ungetLexemaCode = secLexema;
    }

    public String getLexemaText() {
        byte[] textLexema = new byte[this.lexemaLen];

        for(int ii = 0; ii < this.lexemaLen; ++ii) {
            textLexema[ii] = this.lexema[ii];
        }

        return new String(textLexema);
    }

    public int CppScaner() {
        byte q = 0;
        this.lexemaLen = 0;
        int litera = 0;
        boolean lexClass = false;
        byte lexemaClass = 0;
        int litConst = 0;
        String errorLoc = "^[]{},?: ; \t\n\u0000";
        int ee;
        if(this.ungetLexemaCode != 0) {
            ee = this.ungetLexemaCode;
            this.ungetLexemaCode = 0;
            return ee;
        } else {
            try {
                while(true) {
                    while(true) {
                        if(!this.fs.ready()) {
                            if(this.lexemaLen == 0) {
                                return -1;
                            }

                            switch(lexemaClass) {
                                case 1:
                                    ee = this.lang.getLexemaCode(this.lexema, this.lexemaLen, 0);
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
                                    this.lang.getLexemaCode(this.lexema, this.lexemaLen, 0);
                                default:
                                    return -1;
                            }
                        }

                        if(this.fileData == null) {
                            this.fileData = this.fs.readLine();
                            System.out.println(this.fileData);
                            ++this.lexemaLine;
                            this.lexemaPos = 0;
                        }

                        if(this.ungetChar != 0) {
                            litera = this.ungetChar;
                            this.ungetChar = 0;
                            break;
                        }

                        if(this.lexemaPos < this.fileData.length()) {
                            litera = this.fileData.charAt(this.lexemaPos);
                            ++this.lexemaPos;
                            break;
                        }

                        if(this.lexemaPos == this.fileData.length()) {
                            litera = 10;
                            ++this.lexemaPos;
                            break;
                        }

                        if(this.lexemaPos <= this.fileData.length()) {
                            break;
                        }

                        this.fileData = this.fs.readLine();
                        System.out.println(this.fileData);
                        ++this.lexemaLine;
                        this.lexemaPos = 0;
                        if(this.fileData.length() != 0) {
                            litera = this.fileData.charAt(this.lexemaPos);
                            ++this.lexemaPos;
                            break;
                        }
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
                                        return this.lang.getLexemaCode(this.lexema, this.lexemaLen, 0);
                                    case 2:
                                        this.lexema[this.lexemaLen++] = (byte)litera;
                                        lexemaClass = 1;
                                        q = 21;
                                        break;
                                    case 3:
                                        this.lexema[this.lexemaLen++] = (byte)litera;
                                        lexemaClass = 2;
                                        if(litera == 48) {
                                            q = 31;
                                        } else if(litera >= 49 && litera <= 57) {
                                            q = 35;
                                        }
                                }

                                if(q == 0) {
                                    this.lexema[this.lexemaLen++] = (byte)litera;
                                    lexemaClass = 5;
                                    if(litera == 35) {
                                        q = 120;
                                    } else if(litera == 46) {
                                        q = 45;
                                    } else if(litera == 47) {
                                        q = 50;
                                    } else if(litera == 34) {
                                        q = 60;
                                    } else if(litera == 39) {
                                        litConst = 0;
                                        q = 70;
                                    } else if(litera == 58) {
                                        q = 101;
                                    } else if(litera == 45) {
                                        q = 102;
                                    } else if(litera == 43) {
                                        q = 103;
                                    } else if(litera == 124) {
                                        q = 104;
                                    } else if(litera == 42) {
                                        q = 105;
                                    } else if(litera == 62) {
                                        q = 106;
                                    } else if(litera == 60) {
                                        q = 108;
                                    } else if(litera == 61) {
                                        q = 110;
                                    } else if(litera == 38) {
                                        q = 111;
                                    } else if(litera == 33) {
                                        q = 112;
                                    } else if(litera == 37) {
                                        q = 113;
                                    } else if(litera == 94) {
                                        q = 114;
                                    } else {
                                        q = 11;
                                        ++this.errors;
                                    }
                                }
                            }
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
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
                        case 38:
                        case 39:
                        case 47:
                        case 48:
                        case 49:
                        case 52:
                        case 55:
                        case 56:
                        case 57:
                        case 58:
                        case 59:
                        case 61:
                        case 63:
                        case 64:
                        case 65:
                        case 66:
                        case 67:
                        case 68:
                        case 69:
                        case 71:
                        case 72:
                        case 74:
                        case 77:
                        case 78:
                        case 81:
                        case 82:
                        case 83:
                        case 84:
                        case 85:
                        case 86:
                        case 87:
                        case 88:
                        case 89:
                        case 90:
                        case 91:
                        case 92:
                        case 93:
                        case 94:
                        case 95:
                        case 96:
                        case 97:
                        case 98:
                        case 99:
                        case 100:
                        case 115:
                        case 116:
                        case 117:
                        case 118:
                        case 119:
                        default:
                            break;
                        case 11:
                            if(errorLoc.indexOf(litera) >= 0) {
                                this.ungetChar = litera;
                                q = 0;
                            }
                            break;
                        case 21:
                            if(var9 != 2 && var9 != 3) {
                                this.ungetChar = litera;
                                ee = this.lang.getLexemaCode(this.lexema, this.lexemaLen, 0);
                                if(ee != -1) {
                                    return ee;
                                }

                                return 268435462;
                            }

                            this.lexema[this.lexemaLen++] = (byte)litera;
                            break;
                        case 31:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera >= 48 && litera <= 55) {
                                q = 34;
                            } else {
                                if(litera != 88 && litera != 120) {
                                    if(litera != 85 && litera != 117) {
                                        if(litera != 76 && litera != 108) {
                                            this.ungetChar = litera;
                                            --this.lexemaLen;
                                            return 268435464;
                                        }

                                        q = 36;
                                        continue;
                                    }

                                    q = 37;
                                    continue;
                                }

                                q = 32;
                            }
                            break;
                        case 32:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if((litera < 48 || litera > 57) && (litera < 65 || litera > 70) && (litera < 97 || litera > 102)) {
                                this.ungetChar = litera;
                                --this.lexemaLen;
                                q = 0;
                            } else {
                                q = 33;
                            }
                            break;
                        case 33:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if((litera < 48 || litera > 57) && (litera < 65 || litera > 70) && (litera < 97 || litera > 102)) {
                                if(litera != 76 && litera != 108) {
                                    if(litera != 85 && litera != 117) {
                                        this.ungetChar = litera;
                                        --this.lexemaLen;
                                        return 268435464;
                                    }

                                    q = 37;
                                    break;
                                }

                                q = 36;
                                break;
                            }

                            q = 33;
                            break;
                        case 34:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera >= 48 && litera <= 55) {
                                q = 34;
                            } else {
                                if(litera != 76 && litera != 108) {
                                    if(litera != 85 && litera != 117) {
                                        this.ungetChar = litera;
                                        --this.lexemaLen;
                                        return 268435464;
                                    }

                                    q = 37;
                                    continue;
                                }

                                q = 36;
                            }
                            break;
                        case 35:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera >= 48 && litera <= 57) {
                                q = 35;
                            } else if(litera == 46) {
                                q = 40;
                            } else {
                                if(litera != 69 && litera != 101) {
                                    if(litera != 76 && litera != 108) {
                                        if(litera != 85 && litera != 117) {
                                            this.ungetChar = litera;
                                            --this.lexemaLen;
                                            return 268435464;
                                        }

                                        q = 37;
                                        continue;
                                    }

                                    q = 36;
                                    continue;
                                }

                                q = 42;
                            }
                            break;
                        case 36:
                            if(litera != 85 && litera != 117) {
                                this.ungetChar = litera;
                                --this.lexemaLen;
                                return 268435464;
                            }

                            this.lexema[this.lexemaLen++] = (byte)litera;
                            return 268435470;
                        case 37:
                            if(litera != 76 && litera != 108) {
                                this.ungetChar = litera;
                                --this.lexemaLen;
                                return 268435464;
                            }

                            this.lexema[this.lexemaLen++] = (byte)litera;
                            return 268435470;
                        case 40:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera >= 48 && litera <= 57) {
                                q = 41;
                                break;
                            }

                            if(litera != 69 && litera != 101) {
                                this.ungetChar = litera;
                                --this.lexemaLen;
                                return 268435464;
                            }

                            q = 42;
                            break;
                        case 41:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera >= 48 && litera <= 57) {
                                q = 41;
                                break;
                            }

                            if(litera != 69 && litera != 101) {
                                this.ungetChar = litera;
                                --this.lexemaLen;
                                return 268435464;
                            }

                            q = 42;
                            break;
                        case 42:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera >= 48 && litera <= 57) {
                                q = 44;
                            } else {
                                if(litera != 43 && litera != 45) {
                                    this.ungetChar = litera;
                                    --this.lexemaLen;
                                    q = 0;
                                    continue;
                                }

                                q = 43;
                            }
                            break;
                        case 43:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera >= 48 && litera <= 57) {
                                q = 44;
                                break;
                            }

                            this.ungetChar = litera;
                            --this.lexemaLen;
                            q = 0;
                            break;
                        case 44:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera >= 48 && litera <= 57) {
                                q = 44;
                                break;
                            }

                            this.ungetChar = litera;
                            --this.lexemaLen;
                            return 268435464;
                        case 45:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera >= 48 && litera <= 57) {
                                q = 41;
                            } else {
                                if(litera != 69 && litera != 101) {
                                    if(litera != 46) {
                                        this.ungetChar = litera;
                                        --this.lexemaLen;
                                        return this.lang.getLexemaCode(this.lexema, this.lexemaLen, 0);
                                    }

                                    q = 46;
                                    continue;
                                }

                                q = 42;
                            }
                            break;
                        case 46:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera == 46) {
                                return this.lang.getLexemaCode(this.lexema, this.lexemaLen, 0);
                            }

                            this.ungetChar = litera;
                            --this.lexemaLen;
                            q = 0;
                            break;
                        case 50:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera == 47) {
                                q = 51;
                            } else {
                                if(litera != 42) {
                                    if(litera != 61) {
                                        this.ungetChar = litera;
                                        --this.lexemaLen;
                                    }

                                    return this.lang.getLexemaCode(this.lexema, this.lexemaLen, 0);
                                }

                                q = 53;
                            }
                            break;
                        case 51:
                            if(litera != 10) {
                                q = 51;
                            } else {
                                q = 0;
                            }
                            break;
                        case 53:
                            if(litera == 42) {
                                q = 54;
                            } else {
                                q = 53;
                            }
                            break;
                        case 54:
                            if(litera == 42) {
                                q = 54;
                            } else if(litera == 47) {
                                q = 0;
                            } else {
                                q = 53;
                            }
                            break;
                        case 60:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera == 34) {
                                return 268435466;
                            }

                            if(litera == 92) {
                                q = 62;
                            } else {
                                q = 60;
                            }
                            break;
                        case 62:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            q = 60;
                            break;
                        case 70:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera == 39 && litConst == 0) {
                                System.out.println("\n Довжина літерної константи == 0");
                                return 268435464;
                            }

                            if(litera == 39 && litConst <= 2) {
                                return 268435464;
                            }

                            if(litera == 39 && litConst > 2) {
                                System.out.println("\n Довжина літерної константи >= 2");
                                return 268435464;
                            }

                            if(litera == 92) {
                                q = 73;
                            } else {
                                ++litConst;
                                q = 70;
                            }
                            break;
                        case 73:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera != 116 && litera != 39 && litera != 34 && litera != 110 && litera != 114) {
                                if(litera != 88 && litera != 120) {
                                    if(litera < 48 && litera > 55) {
                                        ++litConst;
                                        q = 70;
                                        break;
                                    }

                                    q = 79;
                                    break;
                                }

                                q = 75;
                                break;
                            }

                            ++litConst;
                            q = 70;
                            break;
                        case 75:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if((litera < 65 || litera > 70) && (litera < 97 || litera > 102) && (litera < 48 || litera > 57)) {
                                ++litConst;
                                q = 70;
                                break;
                            }

                            q = 76;
                            break;
                        case 76:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera == 39) {
                                return 268435464;
                            }

                            if((litera < 65 || litera > 70) && (litera < 97 || litera > 102) && (litera < 48 || litera > 57)) {
                                ++litConst;
                                q = 70;
                                break;
                            }

                            ++litConst;
                            q = 70;
                            break;
                        case 79:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera == 39) {
                                return 268435464;
                            }

                            if(litera >= 48 && litera <= 55) {
                                q = 80;
                                break;
                            }

                            ++litConst;
                            q = 70;
                            break;
                        case 80:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera == 39) {
                                return 268435464;
                            }

                            if(litera >= 48 && litera <= 55) {
                                ++litConst;
                                q = 70;
                                break;
                            }

                            ++litConst;
                            q = 70;
                            break;
                        case 101:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera != 58) {
                                this.ungetChar = litera;
                                --this.lexemaLen;
                            }

                            return this.lang.getLexemaCode(this.lexema, this.lexemaLen, 0);
                        case 102:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera != 45 && litera != 61 && litera != 62) {
                                this.ungetChar = litera;
                                --this.lexemaLen;
                            }

                            return this.lang.getLexemaCode(this.lexema, this.lexemaLen, 0);
                        case 103:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera != 43 && litera != 61) {
                                this.ungetChar = litera;
                                --this.lexemaLen;
                            }

                            return this.lang.getLexemaCode(this.lexema, this.lexemaLen, 0);
                        case 104:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera != 124 && litera != 61) {
                                this.ungetChar = litera;
                                --this.lexemaLen;
                            }

                            return this.lang.getLexemaCode(this.lexema, this.lexemaLen, 0);
                        case 105:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera != 61) {
                                this.ungetChar = litera;
                                --this.lexemaLen;
                            }

                            return this.lang.getLexemaCode(this.lexema, this.lexemaLen, 0);
                        case 106:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera != 62) {
                                if(litera != 61) {
                                    this.ungetChar = litera;
                                    --this.lexemaLen;
                                }

                                return this.lang.getLexemaCode(this.lexema, this.lexemaLen, 0);
                            }

                            q = 107;
                            break;
                        case 107:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera != 61) {
                                this.ungetChar = litera;
                                --this.lexemaLen;
                            }

                            return this.lang.getLexemaCode(this.lexema, this.lexemaLen, 0);
                        case 108:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera != 60) {
                                if(litera != 61) {
                                    this.ungetChar = litera;
                                    --this.lexemaLen;
                                }

                                return this.lang.getLexemaCode(this.lexema, this.lexemaLen, 0);
                            }

                            q = 109;
                            break;
                        case 109:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera != 61) {
                                this.ungetChar = litera;
                                --this.lexemaLen;
                            }

                            return this.lang.getLexemaCode(this.lexema, this.lexemaLen, 0);
                        case 110:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera != 61) {
                                this.ungetChar = litera;
                                --this.lexemaLen;
                            }

                            return this.lang.getLexemaCode(this.lexema, this.lexemaLen, 0);
                        case 111:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera != 38 && litera != 61) {
                                this.ungetChar = litera;
                                --this.lexemaLen;
                            }

                            return this.lang.getLexemaCode(this.lexema, this.lexemaLen, 0);
                        case 112:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera != 61) {
                                this.ungetChar = litera;
                                --this.lexemaLen;
                            }

                            return this.lang.getLexemaCode(this.lexema, this.lexemaLen, 0);
                        case 113:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera != 61) {
                                this.ungetChar = litera;
                                --this.lexemaLen;
                            }

                            return this.lang.getLexemaCode(this.lexema, this.lexemaLen, 0);
                        case 114:
                            this.lexema[this.lexemaLen++] = (byte)litera;
                            if(litera != 61) {
                                this.ungetChar = litera;
                                --this.lexemaLen;
                            }

                            return this.lang.getLexemaCode(this.lexema, this.lexemaLen, 0);
                        case 120:
                            if(litera == 92) {
                                q = 121;
                            } else if(litera == 10) {
                                q = 0;
                                this.lexemaLen = 0;
                            } else {
                                q = 120;
                            }
                            break;
                        case 121:
                            if(litera == 10) {
                                q = 120;
                            } else {
                                q = 121;
                            }
                    }
                }
            } catch (IOException var8) {
                System.out.print("Помилка вводу-виводу: " + var8.toString());
                return -1;
            }
        }
    }
}
