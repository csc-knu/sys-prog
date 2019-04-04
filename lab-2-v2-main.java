import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // region option 1
        System.out.format("Do you want to run option 1 (not acceptable letters)? [Y/n]: ");
        String response = scanner.next();
        if (Objects.equals(response, "Y")) {
            try {
                System.out.format("Please enter the name of file with DFA: ");
                String pathname = scanner.next();
                DFA dfa = new DFA(pathname);

                Set<Character> notAcceptableLetters = dfa.getNotAcceptableLetters();
                System.out.println("Not acceptable letters:");
                printIterableInline(notAcceptableLetters, "\t", " ");
            } catch (FileNotFoundException ex) {
                System.out.println("Invalid file pathname.");
            }
        }
        // endregion

        // region option 2a
        System.out.format("Do you want to run option 2a (unreachable states)? [Y/n]: ");
        response = scanner.next();
        if (Objects.equals(response, "Y")) {
            try {
                System.out.format("Please enter the name of file with NFA: ");
                String pathname = scanner.next();
                NFA nfa = new NFA(pathname);

                Set<Integer> notReachableStates = nfa.getNotReachable();
                System.out.println("Not reachable states:");
                printIterableInline(notReachableStates, "\t", " ");
            } catch (FileNotFoundException ex) {
                System.out.println("Invalid file pathname");
            }
        }
        // endregion

        // region option 2b
        System.out.format("Do you want to run option 2b (dead states)? [Y/n]: ");
        response = scanner.next();
        if (Objects.equals(response, "Y")) {
            try {
                System.out.format("Please enter the name of file with NFA: ");
                String pathname = scanner.next();
                NFA nfa = new NFA(pathname);

                Set<Integer> deadStates = nfa.getDead();
                System.out.println("Dead states:");
                printIterableInline(deadStates, "\t", " ");
            } catch (FileNotFoundException ex) {
                System.out.println("Invalid file pathname");
            }
        }
        // endregion

        // region option 3
        System.out.format("Do you want to run option 3 (process word)? [Y/n]: ");
        response = scanner.next();
        if (Objects.equals(response, "Y")) {
            try {
                System.out.format("Please enter the name of file with NFA: ");
                String pathname = scanner.next();
                NFA nfa = new NFA(pathname);

                System.out.format("Enter the word to process: ");
                String word = scanner.next();

                Set<Integer> states = nfa.processWord(word);
                System.out.println("Processed word " + word + ", processing ended in the following states: ");
                printIterableInline(states, "\t", " ");
            } catch (FileNotFoundException ex) {
                System.out.println("Invalid file pathname");
            }
        }
        // endregion

        // region option 4
        System.out.format("Do you want to run option 4 (w0 ↦ ?∃ w1: w0w1?) [Y/n]: ");
        response = scanner.next();
        if (Objects.equals(response, "Y")) {
            try {
                System.out.format("Please enter the name of file with NFA: ");
                String pathname = scanner.next();
                NFA nfa = new NFA(pathname);

                System.out.format("Enter w0: ");
                String w0 = scanner.next();

                Set<Integer> afterW0 = nfa.processWord(w0);
                Set<Integer> reachableFromAfterW0 = nfa.getNotReachableFromStates(afterW0);
                reachableFromAfterW0.retainAll(nfa.finalStates);
                if (!reachableFromAfterW0.isEmpty()) {
                    System.out.println("There DOES exist w1 such that w0w1 is acceptable.");
                } else {
                    System.out.println("There does NOT exist w1 such that w0w1 is acceptable.");
                }
            } catch (FileNotFoundException ex) {
                System.out.println("Invalid file pathname");
            }
        }
        // endregion

        // region option 5
        System.out.format("Do you want to run option 5 (w0 ↦ ?∃ w1: w1w0?) [Y/n]: ");
        response = scanner.next();
        if (Objects.equals(response, "Y")) {
            try {
                System.out.format("Please enter the name of file with NFA: ");
                String pathname = scanner.next();
                NFA nfa = new NFA(pathname);

                System.out.format("Enter w0: ");
                String w0 = scanner.next();
                w0 = new StringBuilder(w0).reverse().toString();

                NFA infa = nfa.inverse();

                Set<Integer> beforeW0 = infa.processWordFromStates(w0, infa.finalStates);
                Set<Integer> beforeW0ReachableFrom = infa.getReachableFromStates(beforeW0);
                if (beforeW0ReachableFrom.contains(infa.startState)) {
                    System.out.println("There DOES exist w1 such that w1w0 is acceptable.");
                } else {
                    System.out.println("There does NOT exist w1 such that w1w0 is acceptable.");
                }
            } catch (FileNotFoundException ex) {
                System.out.println("Invalid file pathname");
            }
        }
        // endregion

        // region option 6
        System.out.format("Do you want to run option 6 (w1, w2 ↦ ?∃ w0: w1w0w2)? [Y/n]: ");
        response = scanner.next();
        if (Objects.equals(response, "Y")) {
            try {
                System.out.format("Please enter the name of file with NFA: ");
                String pathname = scanner.next();
                NFA nfa = new NFA(pathname);

                System.out.format("Enter w1: ");
                String w1 = scanner.next();
                Set<Integer> afterW1 = nfa.processWord(w1);
                Set<Integer> reachableFromAfterW1 = nfa.getReachableFromStates(afterW1);

                System.out.format("Enter w2: ");
                String w2 = scanner.next();
                w2 = new StringBuilder(w2).reverse().toString();
                NFA infa = nfa.inverse();
                Set<Integer> beforeW2 = infa.processWordFromStates(w2, infa.finalStates);

                reachableFromAfterW1.retainAll(beforeW2);
                if (!reachableFromAfterW1.isEmpty()) {
                    System.out.println("There DOES exist w0 such that w1w0w2 is acceptable.");
                } else {
                    System.out.println("There does NOT exist w0 such that w1w0w2 is acceptable.");
                }
            } catch (FileNotFoundException ex) {
                System.out.println("Invalid file pathname");
            }
        }
        // endregion

        // region option 7
        System.out.format("Do you want to run option 7 (w0 ↦ ?∃ w1, w2: w1w0w2)? [Y/n]: ");
        response = scanner.next();
        if (Objects.equals(response, "Y")) {
            try {
                System.out.format("Please enter the name of file with NFA: ");
                String pathname = scanner.next();
                NFA nfa = new NFA(pathname);

                Set<Integer> reachableStates = nfa.getReachable();

                System.out.format("Enter w0: ");
                String w0 = scanner.next();
                Set<Integer> afterW0 = nfa.processWordFromStates(w0, reachableStates);

                Set<Integer> reachableFromAfterW0 = nfa.getReachableFromStates(afterW0);
                reachableFromAfterW0.retainAll(nfa.finalStates);
                if (!reachableFromAfterW0.isEmpty()) {
                    System.out.println("There DOES exist w1, w2 such that w1w0w2 is acceptable.");
                } else {
                    System.out.println("There does NOT exist w1, w2 such that w1w0w2 is acceptable.");
                }
            } catch (FileNotFoundException ex) {
                System.out.println("Invalid file pathname");
            }
        }
        // endregion

        // option 8

        // region option 9
        System.out.format("Do you want to run option 9 (are all words of len k acceptable)? [Y/n]: ");
        response = scanner.next();
        if (Objects.equals(response, "Y")) {
            try {
                System.out.format("Please enter the name of file with NFA: ");
                String pathname = scanner.next();
                NFA nfa = new NFA(pathname);

                System.out.format("Enter k: ");
                int k = scanner.nextInt();

                try {
                    Set<Integer> statesAfterKCompleteSteps = nfa.makeCompleteSteps(k);
                    statesAfterKCompleteSteps.removeAll(nfa.finalStates);
                    if (statesAfterKCompleteSteps.isEmpty()) {
                        System.out.println("All words of length " + k + " ARE acceptable.");
                    } else {
                        System.out.println("NOT all words of length " + k + " are acceptable.");
                    }
                } catch (CompleteStepNotPossibleException ex) {
                    System.out.println(k + " complete steps are NOT possible.");
                }
            } catch (FileNotFoundException ex) {
                System.out.println("Invalid file pathname");
            }
        }
        // endregion

        // option 10

        // region option 11
        System.out.format("Do you want to run option 11 (minimize DFA)? [Y/n]: ");
        response = scanner.next();
        if (Objects.equals(response, "Y")) {
            try {
                String pathname = scanner.next();
                DFA dfa = new DFA(pathname);
                DFA mdfa = dfa.minimize();
            } catch (FileNotFoundException ex) {
                System.out.println("Invalid file pathname");
            }
        }
        // endregion

        // region option 12
        System.out.format("Do you want to run option 12 (determine NFA)? [Y/n]: ");
        response = scanner.next();
        if (Objects.equals(response, "Y")) {
            try {
                String pathname = scanner.next();
                NFA nfa = new NFA(pathname);
                DFA dfa = nfa.determine();
            } catch (FileNotFoundException ex) {
                System.out.println("Invalid file pathname");
            }
        }
        // endregion
    }

    static class CompleteStepNotPossibleException extends Exception {
        CompleteStepNotPossibleException(String message) {
            super(message);
        }
    }

    private static <T> void printIterableInline(Iterable<T> iterable, String indent, String separator) {
        System.out.print(indent);
        for (T t : iterable) {
            System.out.print(t + separator);
        }
        System.out.println();
    }

    static Scanner getScanner(String pathname) throws FileNotFoundException {
        File file = new File(pathname);

        if (!file.exists()) {
            System.out.format("File '%s' does not exist.%n", pathname);
        }

        if (!file.canRead()) {
            System.out.format("Cannot read file '%s'.%n", pathname);
        }

        return new Scanner(file);
    }
}
