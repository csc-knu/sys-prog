/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sysprogrammingmainalgorithm;

/**
 *
 * @author VVN
 */
import java.lang.*;
import java.util.*;
import java.io.*;
import java.nio.charset.Charset;
import JavaTeacherLib.*;

public class SysProgrammingMainAlgorithm {

    public static void main(String[] args) {
        byte [] readline=new byte [80];
        boolean result;
        String fileName;
        MyLang testLang=null;
        int codeAction, llk=1, textLen;
        String [] menu= { "*1.  Прочитати граматику з файла  ",
                " 2.  Лабораторна робота. Клас будує студент",
                " 3.  Надрукувати граматику",
                "*4.  Побудувати списки терміналів та нетерміналів",
                "*5.  Пошук непродуктивних нетерміналів",
                "*6.  Пошук недосяжних нетерміналів",
                "*7.  Побудова списку епсілон-нетерміналів",
                " 8.  Друк списку епсілон-нетерміналів",
                " 9.  Пошук ліворекурсивних нетерміналів",
                " 10. Пошук різних ліворекурсивних виводів мінімальної довжини",
                " 11. Пошук праворекурсивних нетерміналів",
                " 12. Пошук різних праворекурсивних виводів мінімальної довжини",
                "*13. Побудувати множини FirstK(A), A-нетермінал",
                " 14. Вивести на термінал множини FirstK(A), A-нетермінал",
                "*15. Побудувати множини FollowK(A), A-нетермінал",
                " 16. Вивести на термінал множини FollowK(A), A-нетермінал",
                "*17. Побудувати множини FirstK(w) + FollowK(A) для правила А->w",
                " 18. Вивести на термінал FirstK(w) + FollowK(A) для всіх правил А->w",
                " 19. Вивести на термінал FirstK(w) + FollowK(A) для вибраних правил А->w",
                "*20. Перевірка сильної LL(1)-властивості",
                " 21. Побудова таблиці LL(1)-синтаксичного аналізатора",
                " 22. Синтаксичний аналізатор. Клас будує студент",
                "*23. Побудувати множини FirstK(w) для правила , A->W",
                " 24. Друк множини FirstK(w) для правила , A->W",
                "*25. Побудувати множини LocalK(A), A-нетермінал",
                " 26. Вивести на термінал множини LocalK(A), A-нетермінал",
                "*27. Перевірка LL(k)-властивості, k>1",
                " 28. Вихід з системи"
        };
        do  {
            codeAction=0;
            String upr;
            for (String ss: menu) System.out.println(ss); // вивести меню
            System.out.println("Введіть код дії або end:");
            do {  // цикл перебору даних
                try {
                    textLen=System.in.read(readline);
                    upr = new String (readline,0,textLen, "ISO-8859-1");
                    if (upr.trim().equals("end") ) return;
                    codeAction=new Integer (upr.trim());
                }
                catch(Exception ee)
                { System.out.println ("Невірний код дії, повторіть: ");
                    continue;
                }
                if (codeAction >=1  &&  codeAction<=menu.length ) {
                    if (menu [codeAction-1].substring(0, 1).equals("+"))  {
                        System.out.println("Елемент меню " +codeAction+" повторно виконати неможливо");
                        continue ;
                    }
                    int itmp;
                    for (itmp=0; itmp < codeAction-1; itmp++)
                        if (menu[itmp].substring(0, 1).equals("*")) break;
                    if (itmp !=codeAction-1) {
                    //    System.out.println ("Виконайте попередні елементи меню, що позначені * : ");
                    //    continue ;
                    }
                    break;
                }
                else {
                    System.out.println ("Невірний код дії, повторіть: ");
                    continue ;
                }
            }  while (true);
            // перевірка на виконання усіх попередніх дій
            result=false;
            switch (codeAction) {
                case 1: //1. Прочитати граматику з файла",
                    System.out.print ("Введіть ім'я файлу граматики:");
                    try {
                        textLen=System.in.read(readline);
                        fileName = new String (readline,0,textLen, "ISO-8859-1");
                        fileName = fileName.trim();
                    }
                    catch(Exception ee)
                    { System.out.println ("Системна помилка: "+ee.toString());
                        return;
                    }
                    System.out.print ("Введіть значення параметра k : ");
                    try {
                        textLen=System.in.read(readline);
                        String llkText = new String (readline,0,textLen, "ISO-8859-1");
                        llkText = llkText.trim();
                        llk=Integer.parseInt(llkText);
                    }
                    catch(Exception ee)
                    { System.out.println ("Системна помилка: "+ee.toString());
                        return;
                    }
                    testLang = new MyLang (fileName,llk);
                    if (!testLang.isCreate()) break;  //не створили об'єкт
                    System.out.println ("Граматика прочитана успішно");
                    result=true;
                    for (int jj=0;  jj<menu.length; jj++) {
                        if (menu [jj].substring(0, 1).equals(" ")) continue;
                        menu [jj]=menu [jj].replace(menu [jj].charAt(0), '*') ;
                    }
                    break;
                case 2: //2. Лабораторна робота студента
                    break;
                case 3:  // Надрукувати граматику
                    testLang.printGramma();
                    break;
                case 4:  // надрукувати список терміналів та нетерміналів
                    testLang.printTerminals();
                    testLang.printNonterminals();
                    result=true;
                    break;
                case 5: // вивести непродуктивні правила
                    result=testLang.createNonProdRools();
                    break;
                case 6: // недосяжні нетермінали
                    result=testLang.createNonDosNeterminals();
                    break;
                case 7:  //Побудова списку епсілон-нетерміналів
                    int [] epsilon=testLang.createEpsilonNonterminals ();
                    testLang.setEpsilonNonterminals (epsilon);
                    result=true;
                    break;
                case 8: //Друк списку епсілон-нетерміналів
                    testLang.printEpsilonNonterminals();
                    break;
                case 9:    //Пошук ліворекурсивних нетерміналів"
                    testLang.leftRecursNonnerminal();
                    break;
                case 10:  //Пошук різних ліворекурсивних виводів мінімальної довжини"
                    testLang.leftRecusionTrace();
                    break;
                case 11:  //Пошук праворекурсивних нетерміналів"
                    testLang.rightRecursNonnerminal();
                    break;
                case 12:  //Пошук різних праворекурсивних виводів мінімальної довжини"
                    testLang.rigthRecusionTrace();
                    break;
                case 13:  //Побудувати множини FirstK
                    LlkContext [] firstContext = testLang.firstK();
                    testLang.setFirstK(firstContext);
                    result=true;
                    break;
                case 14:  //Друк множини FirstK
                    testLang.printFirstkContext ( );
                    break;
                case 15:  //Побудувати множини FollowK
                    LlkContext [] followContext = testLang.followK();
                    testLang.setFollowK(followContext);
                    result=true;
                    break;
                case 16:  //Друк множини FollowK
                    testLang.printFollowkContext ( );
                    break;
                case 17:  //Побудувати множини FirstK(w) + FollowK(A) для правила А->w
                    testLang.firstFollowK ( );
                    result=true;
                    break;
                case 18:  //Друк множини FirstK(w) + FollowK(A) для правила А->w
                    testLang.printFirstFollowK( );
                    break;
                case 19:  //Друк множини FirstK(w) + FollowK(A) для вибраних правил А->w
                    testLang.printFirstFollowForRoole();
                    break;
                case 20:  //Перевірка сильної LL(k)-властивості",
                    result=testLang. strongLlkCondition () ;
                    break;
                case 21:  //Побудова таблиці LL(1)-синтаксичного аналізатора
                    int [][] uprTable=testLang.createUprTable ();
                    testLang.setUprTable(uprTable);
                    break;
                case 22: // PASCAL
                    break;

                case 23: //"*23. Побудувати множини FirstK(w) для правила , A->W",
                    testLang.firstKforRoole();
                    result=true;
                    break;
                case 24: //24. Друк множини FirstK(w) для правила , A->W",
                    testLang.printFirstContextForRoole();
                    result=true;
                    break;
                case 25: // 23. Побудувати множини LocalK(A), A-нетермінал
                    LinkedList <LlkContext> [] Localk=testLang.createLocalK();
                    testLang.setLocalkContext(Localk);
                    result=true;
                    break;
                case 26: // 24. Вивести на термінал множини LocalK(A), A-нетермінал
                    testLang.printLocalk();
                    result=true;
                    break;
                case 27: // 25. Перевірка LL(k)-властивості, k>1
                    result= testLang.llkCondition();
                    result=true;
                    break;
                case 28: // rtrtrtr
                    return;

            }  // кінець switch
            // блокуємо елемент обробки
            if (result) // функція виконана успішно
                if (menu [codeAction-1].substring(0, 1).equals("*"))
                    menu [codeAction-1]=menu [codeAction-1].replace('*', '+') ;
        } while (true);  //глобальний цикл  обробки

    }  // кінець main

    static void tesrReadWrite(String fname)
    {  String readline;
        BufferedReader s;
        BufferedWriter bw;
        try {
            s = new BufferedReader(new FileReader(fname));
            bw = new BufferedWriter (new FileWriter("c:\\rez.txt"));
            // s=new FileInputStream (fname);
            //s=new FileInputStream ("C:\\Eclipse\\C1.txt");
            //s=new FileInputStream ("C:\\test1.txt");
            while ( s.ready() ) {
                readline= s.readLine();
                System.out.println(readline);
                //System.out.println("Read Line");
                //bw.write(readline, 0,readline.length() );
                //bw.write((int)'\r'); bw.flush();
                //System.out.println("Print Line");
            }

            //bw.close();
        }
        catch(Exception ee)
        {
            System.out.print("File: " +fname + "not found\n");
            //return;
        }
    }
}

