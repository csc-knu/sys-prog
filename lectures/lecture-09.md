{% include mathjax %}

[Назад на головну](../README.md)

Зміст:

- [Синтаксичний аналіз без повернення назад](#синтаксичний-аналіз-без-повернення-назад)

	- [$$LL(k)$$-граматики](#llk-граматики)

	- [Розподілені $$LL(1)$$-граматики](#розподілені-ll1-граматики)

	- [Алгоритм пошуку $$\text{First}_k$$](#алгоритм-пошуку-textfirst_k)

	- [Сильні $$LL(k)$$-граматики](#сильні-llk-граматики)

	- [Алгоритм пошуку $$\text{Follow}_k$$](#алгоритм-пошуку-textfollow_k)

	- [$$\varepsilon$$-нетермінали](#varepsilon-нетермінали)

- [Контрольні запитання](#контрольні-запитання)

## Синтаксичний аналіз без повернення назад

При виводу слова $$\omega$$ в $$G$$ на кожному кроку безпосереднього виведення
коли ми беремо до уваги виділений нами нетермінал (в залежності від стратегії
виведення), виникає питання, яку альтернативу для $$A_i$$ використати. З точки зору
практики, нас цікавить така стратегія виведення $$\omega$$ в граматиці $$G$$, коли кожний
наступний крок безпосереднього виведення наближав би нас до мети. Ця стратегія
дасть можливість виконати виведення $$\omega$$ в $$G$$ за час пропорційний $$O(n)$$, де $$n = |\omega|$$.

Зрозуміло, що не маючи інформації про структуру $$\omega$$, досягнути вибраної нами
мети в більшості випадків неможливо. Але ж тримати інформацію про весь
слово $$\omega$$ також недопустимо. З точки зору практики, отримати потрібний
результат розумно при наявності локальної інформації, наприклад, $$k$$ поточних
вхідних лексем програми ($$k$$ &mdash; наперед фіксоване число) достатньо для
організації виведення $$\omega$$ в $$G$$ за час пропорційний $$O(n)$$. З точки зору
синтаксичного аналізу слова $$\omega$$ мова ведеться про наступну ситуацію:

![img-15](img/img-15.png)

Зафіксуємо стратегію виводу: далі будемо розглядати лише лівосторонню
стратегію виводу $$\omega$$ в $$G$$. Тоді:

- $$S \Rightarrow^\star \omega_1 A \omega_2$$ ($$A$$ &mdash; перший зліва направо нетермінал);

- $$\omega_1$$ &mdash; термінальна частина слова $$\omega$$, яку вже виведено (проаналізована
	частина слова);

- результат $$\omega_3$$, який потрібно ще вивести, виводиться з слова $$A \omega_2$$;

- щоб зробити вірний крок виведення (без повернення назад) нам було б
	достатньо $$k$$ поточних вхідних символів з непроаналізованої частини програми
	$$\omega_3$$.

Сформульовані нами умови забезпечує клас $$LL(k)$$-граматик.

### $$LL(k)$$-граматики

КС-граматика $$G = \left\langle N, \Sigma, P, S \right\rangle$$ називається 
_$$LL(k)$$-граматикою_ для деякого фіксованого $$k$$, якщо для двох лівосторонніх виводів виду:

1. $$S \Rightarrow^\star \omega_1 A \omega_2 \Rightarrow \omega_1 \alpha \omega_2 \Rightarrow^\star \omega_1 x$$;

2. $$S \Rightarrow^\star \omega_1 A \omega_2 \Rightarrow \omega_1 \beta \omega_2 \Rightarrow^\star \omega_1 y$$;

то з $$\text{First}_k (x) = \text{First}_k (y)$$ випливає, що $$\alpha = \beta$$, де
$$A \mapsto \alpha \mid \beta$$.

Неформально, граматика $$G$$ буде $$LL(k)$$-граматикою, якщо для слова
$$\omega_1 A \omega_2 \in (N \cup \Sigma)^\star$$ достатньо $$k$$ перших символів (за умови, 
що вони існують) решти непроаналізованого слова щоб визначити, що з $$A \omega_2$$ існує не 
більше однієї альтернативи виведення слова, що починається з $$\omega$$ та продовжується
наступними $$k$$ термінальними символами.

$$
\text{First}_k(\alpha) = \left\{ \omega \mid \alpha \Rightarrow^\star \omega x,\vert\omega\vert = k \right\} \cup \left\{ \omega \mid \alpha \Rightarrow^\star \omega, \vert\omega\vert < k \right\}.
$$

Сформулюємо основні твердження стосовно класу $$LL(k)$$-граматик:

1. Не існує алгоритма, який перевіряє належність КС-граматики класу $$LL(k)$$-граматик.

2. Існує алгоритм, який для конкретного $$k$$ перевіряє, чи є задана граматика 
	$$LL(k)$$-граматикою.

3. Якщо граматика є $$LL(k)$$-граматикою, то вона є $$LL(k + p)$$-граматикою, $$(p \ge 1)$$.

4. Клас $$LL(k)$$-граматик &mdash; це підклас КС-граматик, який не покриває його.

Продемонструємо на **прикладі** справедливість останнього твердження. Розглянемо
граматику $$G$$ з наступною схемою $$P$$: $$S \mapsto S a \mid b$$.

Мова, яку породжує наведена вище граматика $$L(G) = \{ ba^i, i = 0, 1, \ldots \}$$. Візьмемо
виведення наступного слова $$S \Rightarrow^{i+1} b a^i$$; за означенням $$LL(k)$$-граматики
$$A = S,$$ $$\omega_2 = a^i,$$ $$\alpha = S a,$$ $$\beta = b,$$ тоді для $$i \ge k$$ маємо

$$
\text{First}_k (S a a^i) = \text{First}_k (b a^i) = \left\{b a^{k - 1}\right\}.
$$

Таким чином, КС-граматика $$G$$ не може бути $$LL(k)$$-граматикою для жодного $$k$$. Як
результат, КС-граматика $$G$$, яка має ліворекурсивний нетермінал $$A$$ (нетермінал $$A$$
називається _ліворекурсивним_, якщо в граматиці $$G$$ існує вивід виду 
$$A \Rightarrow^\star A \omega$$), не може бути $$LL(k)$$-граматикою.

### Розподілені $$LL(1)$$-граматики

З практичної точки зору в більшості випадків ми будемо користуватися
$$LL(1)$$-граматиками. У класі $$LL(1)$$-граматик існує один цікавий підклас &mdash; це
розподілені $$LL(1)$$-граматики.

$$LL(1)$$-граматика називаються _розподіленою_, якщо вона задовольняє наступним умовам:

- у схемі $$P$$ граматики відсутні $$\varepsilon$$-правила 
	(правила вигляду $$A \mapsto \varepsilon$$);

- для нетермінала $$A$$ праві частини $$A$$-правила починаються різними терміналами.

Зауважимо, що $$\text{First}_k (\omega_1 \omega_2) = \text{First}_k (\omega_1) \oplus_k \text{First}_k (\omega_2)$$, де $$\oplus_k$$ &mdash; бінарна операція над словарними множинами (мовами) визначена наступним чином:

$$
L_1 \oplus_k L_2 = \left\{ \omega \mid \omega \omega_1 = x y, \vert\omega\vert = k \right\} \cup  \left\{ \omega \mid \omega = x y, \vert\omega\vert < k \right\}, \quad x \in L_1, y \in L_2.
$$

Звідси маємо наступний тривіальний висновок: якщо $$\omega = \alpha_1 \alpha_2 \ldots \alpha_p$$, де $$\alpha_i \in (N \cup \Sigma)$$, то

$$
\text{First}_k (\omega) = \text{First}_k (\alpha_1) \oplus_k \text{First}_k (\alpha_2) \oplus_k \ldots \oplus_k \text{First}_k (\alpha_p)
$$

Для подальшого аналізу означення $$LL(k)$$-граматики розглянемо алгоритм
обчислення функції $$\text{First}_k (\alpha)$$, $$\alpha \in (N \cup \Sigma)$$.

### Алгоритм пошуку $$\text{First}_k$$

Очевидно, що якщо $$\alpha_i \in \Sigma$$, то $$\text{First}_k (\alpha_i) = \{\alpha_i\}$$ при $$k > 0$$. Розглянемо алгоритм пошуку $$\text{First}_k (A_i)$$, $$A_i \in N$$.

**Алгоритм [пошуку $$\text{First}_k(A_i)$$, $$A_i \in N$$]**: визначимо значення функції $$F_i(x)$$ для кожного $$x \in (N \cup \Sigma)$$:

1. $$F_i (a) = \{a\}$$ для всіх $$a \in \Sigma$$, $$i \ge 0$$.

2. $$F_0(A_i) = \left\{ \omega \mid \omega \in \Sigma^{\star k}: A_i \mapsto \omega x, \vert\omega\vert = k \right\} \cup \left\{ \omega \mid \omega \in \Sigma^{\star k}: A_i \mapsto \omega, \vert\omega\vert < k \right\}$$.

3. $$F_n(A) = F_{n - 1}(A_i) \cup \left\{ \omega \mid \omega \in \Sigma^{\star k}: \omega \in F_{n - 1} (\alpha_1) \oplus_k \ldots \oplus F_{n - 1} (\alpha_p), A_i \mapsto \alpha_1 \ldots \alpha_p \right\}$$.

4. $$F_m(A_i) = F_{m + 1}(A_i) = \ldots$$ для всіх $$A_i \in N$$.

Очевидно, що:

- послідовність $$F_0 (A_i) \subseteq F_1(A_i) \subseteq \ldots$$ &mdash; монотонно зростаюча

- $$F_n(A_i) \subseteq \Sigma^{\star k}$$ &mdash; послідовність обмежена зверху.

Тоді покладемо $$\text{First}_k(A_i) = F_m(A_i)$$ для кожного $$A_i \in N$$.

**Приклад:** знайти множину $$\text{First}_k (A_i)$$ для нетерміналів граматики з наступною 
схемою правил: 

$$
\begin{align*}
	S &\mapsto BA, \\
	A &\mapsto +BA \mid \varepsilon, \\
	B &\mapsto DC, \\
	C &\mapsto \times DC \mid \varepsilon, \\
	D &\mapsto (S).
\end{align*}
$$

Нехай $$k = 2$$, тоді маємо наступну таблицю:

&nbsp; | $$S$$ | $$A$$ | $$B$$ | $$C$$ | $$D$$
------ | ----- | ----- | ----- | ----- | -----
$$F_0$$ | $$\varnothing$$ | $$\{\varepsilon\}$$ | $$\varnothing$$ | $$\{\varepsilon\}$$, $$\{a\}$$
$$F_1$$ | $$\varnothing$$ | $$\{\varepsilon\}$$ | $$\{a\}$$ | $$\{\varepsilon, \times a\}$$ | $$\{a\}$$
$$F_2$$ | $$\{a\}$$ | $$\{\varepsilon, +a\}$$ | $$\{a, a\times\}$$ | $$\{\varepsilon, \times a\}$$ | $$\{a\}$$
$$F_3$$ | $$\{a, a+, a\times\}$$ | $$\{\varepsilon,+a\}$$ | $$\{a, a\times\}$$ | $$\{\varepsilon, \times a\}$$ | $$\{a, (a\}$$
$$F_4$$ | $$\{a, a+, a\times\}$$ | $$\{\varepsilon,+a\}$$ | $$\{a, a\times, (a\}$$ | $$\{\varepsilon, \times a, \times(\}$$ | $$\{a, (a\}$$
$$F_5$$ | $$\{a, a+, a\times, (a\}$$ | $$\{\varepsilon,+a,+(\}$$ | $$\{a, a\times, (a\}$$ | $$\{\varepsilon, \times a, \times(\}$$ | $$\{a, (a\}$$
$$F_6$$ | $$\{a, a+, a\times, (a\}$$ | $$\{\varepsilon,+a,+(\}$$ | $$\{a, a\times, (a\}$$ | $$\{\varepsilon, \times a, \times(\}$$ | $$\{a, (a, ((\}$$
$$F_7$$ | $$\{a, a+, a\times, (a\}$$ | $$\{\varepsilon,+a,+(\}$$ | $$\{a, a\times, (a, ((\}$$ | $$\{\varepsilon, \times a, \times(\}$$ | $$\{a, (a, ((\}$$
$$F_8$$ | $$\{a, a+, a\times, (a, ((\}$$ | $$\{\varepsilon,+a,+(\}$$ | $$\{a, a\times, (a, ((\}$$ | $$\{\varepsilon, \times a, \times(\}$$ | $$\{a, (a, ((\}$$
$$F_9$$ | $$\{a, a+, a\times, (a, ((\}$$ | $$\{\varepsilon,+a,+(\}$$ | $$\{a, a\times, (a, ((\}$$ | $$\{\varepsilon, \times a, \times(\}$$ | $$\{a, (a, ((\}$$

Скористаємося означенням $$\text{First}_k(\alpha)$$ сформулюємо необхідні й достатні умови, за 
яких КС-граматика буде $$LL(k)$$-граматикою: для довільного виводу в граматиці $$G$$ вигляду 
$$S \Rightarrow^\star \omega_1 A \omega_2$$ та правила $$A \mapsto \alpha \mid \beta$$:

$$
\text{First}_k(\alpha \omega_2) \cap \text{First}_k (\beta \omega_2) = \varnothing.
$$

Вище сформульована умова для $$LL(k)$$-граматик може бути перефразована з урахуванням 
визначення множини $$\text{First}_k$$: для довільного виводу в граматиці $$G$$ вигляду
$$S \Rightarrow^\star \omega_1 A \omega_2$$ та правила $$A \mapsto \alpha \mid \beta$$:

$$
\text{First}_k(\alpha \cdot L) \cap \text{First}_k (\beta \cdot L) = \varnothing, \quad L = \text{First}_k(\omega_2).
$$

Оскільки $$L \subseteq \Sigma^{\star k}$$, то остання умова є конструктивною умовою і може 
бути використана для перевірки, чи КС-граматика є $$LL(k)$$-граматикою для фіксованого $$k$$.

### Сильні $$LL(k)$$-граматики

КС-граматика називається _сильною $$LL(k)$$-граматикою_, якщо для $$A$$-правила вигляду 
$$A \mapsto \alpha \mid \beta$$ виконується умова:

$$
\text{First}_k (\alpha \cdot \text{Follow}_k (A)) \cap \text{First}_k (\beta \cdot \text{Follow}_k (A)) = \varnothing,
$$

де $$\text{Follow}_k(\alpha)$$, $$\alpha \in (N \cup \Sigma)^\star$$ визначається так:

$$
\text{Follow}_k (\alpha) = \left\{ \omega \mid S \Rightarrow^\star \omega_1 \alpha \omega_2, \omega \in \text{First}_k(\omega_2) \right\}.
$$

Операції $$\text{First}_k$$ та $$\text{Follow}_k$$ можна узагальнити для словарної множини $$L$$, тоді:

$$
\begin{align*}
	\text{First}_k (L) &= \left\{ \omega \mid \forall \alpha_i \in L: \omega \in \text{First}_k (\alpha_i) \right\}. \\
	\text{Follow}_k (L) &= \left\{ \omega \mid \forall \alpha_i \in L: S \Rightarrow^\star \omega_1 \alpha_i \omega_2, \omega \in \text{First}_k (\omega_2) \right\}.
\end{align*}
$$

Без доведення зафіксуємо наступні твердження:

- кожна $$LL(1)$$-граматика є сильною $$LL(1)$$-граматикою;

- існують $$LL(k)$$-граматики $$(k > 1)$$, які не є сильними $$LL(k)$$-граматиками.

На **прикладі** продемонструємо останнє твердження. Нехай граматика $$G$$ визначена
наступними правилами: $$S \mapsto aAaa \mid bAba$$, $$A \mapsto b \mid \varepsilon$$.

Відповідні множини $$\text{First}_2(S) = \{ab, aa, bb\}$$, $$\text{First}_2(A) = \{b, \varepsilon\}$$, $$\text{Follow}_2(A) = \{aa, ba\}$$, $$\text{Follow}_2(S) = \{\varepsilon\}$$.

Перевіримо умову для сильної $$LL(2)$$-граматики:

1. виконаємо перевірку $$LL(2)$$-умови для правила $$S \mapsto aAaa \mid bAba$$:

	$$
	\begin{multline*}
	\text{First}_2(aAaa \cdot \text{Follow}_2(S)) \cap \text{First}_2(bAba \cdot \text{Follow}_2(S)) = \\
	= (\text{First}_2(aAaa) \oplus_2 \text{Follow}_2(S)) \cap (\text{First}_2(bAba) \oplus_2 \text{Follow}_2(S)) = \\
	= (\{ab, aa\} \oplus_2 \{\varepsilon\}) \cap (\{bb\} \oplus_2 \{\varepsilon\}) = \{ab,aa\}\cap \{bb\} = \varnothing.
	\end{multline*}
	$$

2. виконаємо перевірку $$LL(2)$$-умови для правила $$A \mapsto b \mid \varepsilon$$

	$$
	\text{First}_2(b \cdot \text{Follow}_2(A)) \cap \text{First}_2(\varepsilon \cdot \text{Follow}_2(A)) = \\
	= \{ba,bb\}\cap\{aa,ba\}=\{ba\}.
	$$

**Висновок:** вище наведена граматика не є сильною $$LL(2)$$-граматикою. Перевіримо
цю ж граматику на властивість $$LL(2)$$-граматики. Тут ми маємо два різні варіанти
виводу з $$S$$:

1. $$S \Rightarrow^\star aAaa$$: $$\text{First}_2(b \cdot aa) \cap \text{First}_2(\varepsilon \cdot aa) = \{ba\} \cap \{aa\} = \varnothing$$.

2. $$S \Rightarrow^\star bAba$$: $$\text{First}_2(b \cdot ba) \cap \text{First}_2(\varepsilon \cdot ba) = \{bb\} \cap \{ba\} = \varnothing$$.

Висновок: наведена вище граматика є $$LL(2)$$-граматикою.

### Алгоритм пошуку $$\text{Follow}_k$$

**Алгоритм [обчислення $$\text{Follow}_k (A_i)$$, $$A_i \in N$$]:** будемо розглядати всілякі 
дерева, які можна побудувати, починаючи з аксіоми $$S$$:

1. $$\sigma_0(S, S) = \{\varepsilon\}$$. Очевидно, за 0 кроків ми виведемо $$S$$, після якої 
	знаходиться $$\varepsilon$$. У інших випадках $$\sigma_0(S, A_i)$$ &mdash; невизначено, 
	$$A_i \in (N \setminus \{S\})$$.

2. $$\sigma_1(S, A_i) = \sigma_0(S, A_i) \cup \left\{ \omega \middle| S \mapsto \omega_1 A_i \omega_2, \omega \in \text{First}_k(\omega_2) \right\}$$. 
	В інших випадках $$\sigma_1(S, A_i)$$ &mdash; невизначено.

3. $$\sigma_n(S, A_i) = \sigma_{n - 1}(S, A_i) \cup \left\{ \omega \middle| A_j \mapsto \omega_1 A_i \omega_2, \omega \in \text{First}_k(\omega_2 \cdot \sigma_{n - 1}(S, A_j)) \right\}$$.
	В інших випадках $$\sigma_n(S, A_i)$$ &mdash; невизначено.

Настане крок $$m$$, коли $$\sigma_m(S, A_i) = \sigma_{m + 1}(S, A_i) = \ldots $$, $$\forall A_i \in N$$.

Тоді покладемо $$\text{Follow}_l(A_i) = \sigma_m(S, A_i)$$, $$\forall A_i \in N$$.

Очевидно, що:

- послідовність $$\sigma_0(S, A_i) \subseteq \sigma_1(S, A_i) \subseteq \ldots$$ монотонно 
	зростаюча;

- $$\sigma_n(S, A_i) \subseteq \Sigma^{\star k}$$ &mdash; послідовність обмежена зверху.

До того, як перевірити граматику на $$LL(k)$$-властивість необхідно перевірити
її на наявність ліворекурсивних нетерміналів та спробувати уникнути лівої рекурсії.

### $$\varepsilon$$-нетермінали

Нетермінал $$A_i$$ КС-граматики $$G$$ називається _$$\varepsilon$$-нетерміналом_, якщо
$$A_i \Rightarrow^\star \varepsilon$$.

**Алгоритм [пошуку $$\varepsilon$$-нетерміналів]:**

1. $$S_0 = \{A_i \mid A_i \mapsto \varepsilon \}$$.

2. $$S_1 = S_0 \cup \{ A_i \mid A_i \mapsto \alpha_1 \alpha_2 \ldots \alpha_p, \alpha_j \in S_0, j = \overline{1..p} \}$$.

3. $$S_n = S_{n-1} \cup \{ A_i \mid A_i \mapsto \alpha_1 \alpha_2 \ldots \alpha_p, \alpha_j \in S_{n-1}, j = \overline{1..p} \}$$.

4. $$S_m = S_{m + 1} = \ldots$$.

Тоді множина $$S_m$$ &mdash; множина $$\varepsilon$$-нетерміналів.

**Приклад.** Для граматики $$G$$ з схемою правил $$P$$ знайдемо множину $$\varepsilon$$-нетерміналів: 

$$
\begin{align*}
	S &\mapsto aBD \mid D \mid AC \mid b, \\
	A &\mapsto SCB \mid SABC \mid CbD \mid \varepsilon, \\
	B &\mapsto CA \mid d, \\
	C &\mapsto ADC \mid a \mid \varepsilon, \\
	D &\mapsto EaC \mid SC, \\
	E &\mapsto BCS \mid a.
\end{align*}
$$

1. $$S_0 = \{A, C\}$$.

2. $$S_1 = \{A, C\} \cup \{B, S\}$$.

3. $$S_2 = \{A, B, C, S\} \cup \{D\}$$.

4. $$S_3 = \{A, B, C, S, D\} \cup \{E\}$$.

5. $$S_4 = \{A, B, C, S, D, E\} \cup \{E\}$$.

Таким чином, множина $$\varepsilon$$-нетерміналів для наведеної вище граматики &mdash; $$\{S, A, B, C, D, E\}$$.

## Контрольні запитання

1. 
	<!---->

2. 
	<!---->

3. 
	<!---->

4. 
	<!---->

5. 
	<!---->

6. 
	<!---->

7.  
	<!---->

8. 
	<!---->

(_традиційні_ відповіді можна переглянути у коментарях у вихідному коді цієї сторінки)

[Назад на головну](../README.md)