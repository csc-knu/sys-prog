package sysprogrammingmainalgorithm;

import java.lang.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import JavaTeacherLib.*;

public class SysProgrammingMainAlgorithm {

    public static void main(String[] args) {
        byte [] readline = new byte [80];
        boolean result;
        String fileName;
        MyLang testLang = null;
        int codeAction, llk, textLen;
        String [] menu = {
                "*1.  Прочитати граматику з файла",
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
                "*13. Побудувати множини FirstK(A), A -- нетермінал",
                " 14. Вивести на термінал множини FirstK(A), A -- нетермінал",
                "*15. Побудувати множини FollowK(A), A -- нетермінал",
                " 16. Вивести на термінал множини FollowK(A), A -- нетермінал",
                "*17. Побудувати множини FirstK(w) + FollowK(A) для правила А -> w",
                " 18. Вивести на термінал FirstK(w) + FollowK(A) для всіх правил А -> w",
                " 19. Вивести на термінал FirstK(w) + FollowK(A) для вибраних правил А -> w",
                "*20. Перевірка сильної LL(k)-властивості",
                " 21. Побудова таблиці LL(k)-синтаксичного аналізатора",
                " 22. Синтаксичний аналізатор. Клас будує студент",
                "*23. Побудувати множини FirstK(w) для правила A -> w",
                " 24. Друк множини FirstK(w) для правила , A -> w",
                "*25. Побудувати множини LocalK(A), A -- нетермінал",
                " 26. Вивести на термінал множини LocalK(A), A -- нетермінал",
                "*27. Перевірка LL(k)-властивості, k > 1",
                " 28. Вихід з системи"
        };

        do {
            String upr;
            for (String ss: menu) System.out.println(ss);  // вивести меню
            System.out.print("Введіть код дії або end: ");
            do {  // цикл перебору даних
                try {
                    textLen = System.in.read(readline);
                    upr = new String (readline,0, textLen, StandardCharsets.ISO_8859_1);
                    if (upr.trim().equals("end") ) return;
                    codeAction = Integer.parseInt(upr.trim());
                } catch (Exception ee) {
                    System.out.print("Невірний код дії, повторіть: ");
                    continue;
                }

                if (codeAction >= 1 && codeAction <= menu.length) {
                    if (menu[codeAction - 1].substring(0, 1).equals("+"))  {
                        System.out.println("Елемент меню " + codeAction + " повторно виконати неможливо");
                        continue;
                    }

                    int itmp;
                    for (itmp=0; itmp < codeAction - 1; itmp++)
                        if (menu[itmp].substring(0, 1).equals("*")) break;
                    if (itmp != codeAction-1) {
                        System.out.print("Виконайте попередні елементи меню, що позначені *: ");
                        continue ;
                    }
                    break;
                } else {
                    System.out.print("Невірний код дії, повторіть: ");
                }
            } while (true);

            // перевірка на виконання усіх попередніх дій
            result = false;
            switch (codeAction) {
                case 1:  // прочитати граматику з файла",
                    System.out.print("Введіть ім'я файлу граматики:");
                    try {
                        textLen = System.in.read(readline);
                        fileName = new String (readline,0, textLen, StandardCharsets.ISO_8859_1);
                        fileName = fileName.trim();
                    } catch (Exception ee) {
                        System.out.println("Системна помилка: " + ee.toString());
                        return;
                    }

                    System.out.print("Введіть значення параметра k: ");
                    try {
                        textLen = System.in.read(readline);
                        String llkText = new String (readline,0, textLen, StandardCharsets.ISO_8859_1);
                        llkText = llkText.trim();
                        llk = Integer.parseInt(llkText);
                    } catch(Exception ee) {
                        System.out.println ("Системна помилка: " + ee.toString());
                        return;
                    }

                    testLang = new MyLang(fileName, llk);
                    if (!testLang.isCreate()) break;  // не створили об'єкт
                    System.out.println ("Граматика прочитана успішно");
                    result = true;
                    for (int jj = 0; jj < menu.length; jj++) {
                        if (menu[jj].substring(0, 1).equals(" ")) continue;
                        menu[jj] = menu[jj].replace(menu[jj].charAt(0), '*') ;
                    }
                    break;
                case 2:  // лабораторна робота студента
                    break;
                case 3:  // надрукувати граматику
                    testLang.printGrammar();
                    break;
                case 4:  // надрукувати список терміналів та нетерміналів
                    testLang.printTerminals();
                    testLang.printNonTerminals();
                    result = true;
                    break;
                case 5:  // вивести непродуктивні правила
                    result = testLang.createNonProdRools();
                    break;
                case 6:  // недосяжні нетермінали
                    result = testLang.createNonDosNonTerminals();
                    break;
                case 7:  // побудова списку епсілон-нетерміналів
                    int [] epsilon = testLang.createEpsilonNonTerminals();
                    testLang.setEpsilonNonTerminals(epsilon);
                    result = true;
                    break;
                case 8:  // друк списку епсілон-нетерміналів
                    testLang.printEpsilonNonTerminals();
                    break;
                case 9:  // пошук ліворекурсивних нетерміналів
                    result = testLang.leftRecursNonTerminal();
                    break;
                case 10:  // пошук різних ліворекурсивних виводів мінімальної довжини
                    testLang.leftRecursionTrace();
                    break;
                case 11:  // пошук праворекурсивних нетерміналів
                    result = testLang.rightRecursNonTerminal();
                    break;
                case 12:  // пошук різних праворекурсивних виводів мінімальної довжини
                    testLang.rightRecursionTrace();
                    break;
                case 13:  // побудувати множини FirstK(A), A -- нетермінал
                    LlkContext [] firstContext = testLang.firstK();
                    testLang.setFirstK(firstContext);
                    result = true;
                    break;
                case 14:  // друк множини FirstK(A), A -- нетермінал
                    testLang.printFirstKContext();
                    break;
                case 15:  // побудувати множини FollowK(A), A -- нетермінал
                    LlkContext [] followContext = testLang.followK();
                    testLang.setFollowK(followContext);
                    result = true;
                    break;
                case 16:  // друк множини FollowK(A), A -- нетермінал
                    testLang.printFollowKContext();
                    break;
                case 17:  // побудувати множини FirstK(w) + FollowK(A) для правила А -> w
                    testLang.firstFollowK();
                    result = true;
                    break;
                case 18:  // друк множини FirstK(w) + FollowK(A) для правила А -> w
                    testLang.printFirstFollowK();
                    break;
                case 19:  // друк множини FirstK(w) + FollowK(A) для вибраних правил А -> w
                    testLang.printFirstFollowForRoole();
                    break;
                case 20:  // перевірка сильної LL(k)-властивості
                    result = testLang.strongLlkCondition();
                    break;
                case 21:  // побудова таблиці LL(1)-синтаксичного аналізатора
                    int [][] uprTable = testLang.createUprTable();
                    testLang.setUprTable(uprTable);
                    break;
                case 22:  // PASCAL
                    break;
                case 23:  // побудувати множини FirstK(w) для правила A -> w
                    testLang.firstKForRule();
                    result = true;
                    break;
                case 24:  // друк множини FirstK(w) для правила A -> w
                    testLang.printFirstContextForRule();
                    result = true;
                    break;
                case 25:  // побудувати множини LocalK(A), A -- нетермінал
                    LinkedList <LlkContext> [] LocalK = testLang.createLocalK();
                    testLang.setLocalKContext(LocalK);
                    result = true;
                    break;
                case 26:  // вивести на термінал множини LocalK(A), A -- нетермінал
                    testLang.printLocalK();
                    result = true;
                    break;
                case 27:  // перевірка LL(k)-властивості, k > 1
                    result = testLang.llkCondition();
                    break;
                case 28:  // return
                    return;

            }  // кінець switch
            // блокуємо елемент обробки
            if (result)  // функція виконана успішно
                if (menu[codeAction - 1].substring(0, 1).equals("*"))
                    menu[codeAction - 1] = menu[codeAction - 1].replace('*', '+') ;
        } while (true);  // глобальний цикл  обробки
    }  // кінець main
}