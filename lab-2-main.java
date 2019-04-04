package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class Main {
    public static class DeterministicAutomata {
        Set<Character> alphabet; // <= 26
        Set<Integer> states;
        int startState;
        Set<Integer> finalStates;
        Map<Integer, Map<Character, Integer>> transitionFunction;

        private DeterministicAutomata(DeterministicAutomata deterministicAutomata) {
            alphabet = new HashSet<>(deterministicAutomata.alphabet);
            states = new HashSet<>(deterministicAutomata.states);
            startState = deterministicAutomata.startState;
            finalStates = new HashSet<>(deterministicAutomata.finalStates);
            transitionFunction = new HashMap<>(deterministicAutomata.transitionFunction);
            for (int from : transitionFunction.keySet())
                transitionFunction.put(from, new HashMap<>(deterministicAutomata.transitionFunction.get(from)));
        }

        private DeterministicAutomata(Scanner fileScanner) {
            String preAlphabet = "abcdefghijklmnopqrstuvwxyz";
            int alphabetSize = fileScanner.nextInt(); // <= 26
            alphabet = new HashSet<>();
            for (int i = 0; i < alphabetSize; ++i)
                alphabet.add(preAlphabet.charAt(i));

            int numberOfStates = fileScanner.nextInt();
            states = new HashSet<>(numberOfStates);
            for (int i = 0; i < numberOfStates; ++i)
                states.add(i);

            startState = fileScanner.nextInt();

            int numberOfFinalStates = fileScanner.nextInt();
            finalStates = new HashSet<>(numberOfFinalStates);
            for (int i = 0; i < numberOfFinalStates; ++i)
                finalStates.add(fileScanner.nextInt());

            transitionFunction = new HashMap<>(numberOfStates);
            for (int state : states)
                transitionFunction.put(state, new HashMap<>());

            while (fileScanner.hasNext())
                transitionFunction.get(fileScanner.nextInt()).put(fileScanner.next().charAt(0), fileScanner.nextInt());
        }

        private DeterministicAutomata(DeterministicAutomata deterministicAutomata, Set<Integer> subStates) {
            alphabet = deterministicAutomata.alphabet;
            states = new HashSet<>(deterministicAutomata.states);
            states.retainAll(subStates);
            // note that we implicitly ask for startState being in subStates
            startState = deterministicAutomata.startState;
            finalStates = new HashSet<>(deterministicAutomata.finalStates);
            finalStates.retainAll(subStates);
            transitionFunction = new HashMap<>();
            for (int from : deterministicAutomata.transitionFunction.keySet()) {
                if (subStates.contains(from)) {
                    transitionFunction.put(from, new HashMap<>());
                    for (char via : deterministicAutomata.transitionFunction.get(from).keySet()) {
                        int to = deterministicAutomata.transitionFunction.get(from).get(via);
                        if (subStates.contains(to))
                            transitionFunction.get(from).put(via, to);
                    }
                }
            }
        }
    }

    public static class Automata {
        Set<Character> alphabet; // <= 26
        Set<Integer> states;
        int startState;
        Set<Integer> finalStates;
        Map<Integer, Map<Character, Set<Integer>>> transitionFunction;

        private Automata(DeterministicAutomata deterministicAutomata) {
            alphabet = new HashSet<>(deterministicAutomata.alphabet);
            states = new HashSet<>(deterministicAutomata.states);
            startState = deterministicAutomata.startState;
            finalStates = new HashSet<>(deterministicAutomata.finalStates);
            transitionFunction = new HashMap<>();
            for (int from : deterministicAutomata.transitionFunction.keySet()) {
                Map<Character, Set<Integer>> emptyMap = new HashMap<>();
                transitionFunction.put(from, emptyMap);
                for (char via : deterministicAutomata.transitionFunction.get(from).keySet()) {
                    Set<Integer> toSet = new HashSet<>(deterministicAutomata.transitionFunction.get(from).get(via));
                    transitionFunction.get(from).put(via, toSet);
                }
            }
        }
    }

    // note that start state and final states are incorrect
    private static Automata inverseDeterministicAutomata(DeterministicAutomata deterministicAutomata) {
        Automata inversedAutomata = new Automata(deterministicAutomata);

        inversedAutomata.transitionFunction = new HashMap<>();
        for (int state : inversedAutomata.states) inversedAutomata.transitionFunction.put(state, new HashMap<>());

        for (int from : deterministicAutomata.states) {
            for (char via : deterministicAutomata.transitionFunction.get(from).keySet()) {
                int to = deterministicAutomata.transitionFunction.get(from).get(via);
                try {
                    inversedAutomata.transitionFunction.get(to).get(via).add(from);
                } catch (NullPointerException ex) {
                    inversedAutomata.transitionFunction.get(to).put(via, new HashSet<>());
                    inversedAutomata.transitionFunction.get(to).get(via).add(from);
                }
            }
        }

        return inversedAutomata;
    }

    private static DeterministicAutomata readDeterministicAutomata(String pathname) throws FileNotFoundException {
        Scanner fileScanner = getScanner(pathname);
        DeterministicAutomata deterministicAutomata = new DeterministicAutomata(fileScanner);
        fileScanner.close();
        return deterministicAutomata;
    }

    private static Scanner getScanner(String pathname) throws FileNotFoundException {
        File file = new File(pathname);

        if (!file.exists())
            System.out.format("File '%s' does not exist.%n", pathname);

        if (!file.canRead())
            System.out.format("Cannot read file '%s'.%n", pathname);

        return new Scanner(file);
    }

    private static Set<Set<Integer>> getNotDistinguishableStates(DeterministicAutomata deterministicAutomata) {
        Set<Set<Integer>> P = new HashSet<>();
        P.add(deterministicAutomata.finalStates);
        Set<Integer> notFinalStates = new HashSet<>(deterministicAutomata.states);
        notFinalStates.removeAll(deterministicAutomata.finalStates);
        P.add(notFinalStates);
        Set<Set<Integer>> W = new HashSet<>();
        W.add(deterministicAutomata.finalStates);

        while (!W.isEmpty()) {
            Set<Integer> A = new HashSet<>(W.iterator().next());
            for (char letter : deterministicAutomata.alphabet) {
                Set<Integer> X = new HashSet<>();
                for (int state : deterministicAutomata.states)
                    if (A.contains(deterministicAutomata.transitionFunction.get(state).get(letter)))
                        X.add(state);

                Set<Set<Integer>> newP = new HashSet<>(P);

                for (Set<Integer> Y : P) {
                    Set<Integer> YCapX = new HashSet<>(Y);
                    YCapX.retainAll(X);
                    Set<Integer> YSetMinusX = new HashSet<>(Y);
                    YSetMinusX.removeAll(X);

                    if (!YCapX.isEmpty() && !YSetMinusX.isEmpty()) {
                        newP.remove(Y);
                        newP.add(YCapX);
                        newP.add(YSetMinusX);
                        if (W.contains(Y)) {
                            W.remove(Y);
                            W.add(YCapX);
                            W.add(YSetMinusX);
                        } else {
                            if (YCapX.size() <= YSetMinusX.size())
                                W.add(YCapX);
                            else
                                W.add(YSetMinusX);
                        }
                    }
                }

                P = new HashSet<>(newP);
            }
            W.remove(A);
        }

        return P;
    }

    private static Set<Integer> getNotReachableStates(DeterministicAutomata deterministicAutomata) {
        return getNotReachableStates(deterministicAutomata, deterministicAutomata.startState);
    }

    private static Set<Integer> getNotReachableStates(DeterministicAutomata deterministicAutomata, int startState) {
        Set<Integer> states = new HashSet<>(deterministicAutomata.states);
        states.removeAll(getReachableStates(deterministicAutomata, startState));
        return states;
    }

    private static Set<Integer> getReachableStates(DeterministicAutomata deterministicAutomata, int startState) {
        Set<Integer> reachableStates = new HashSet<>();

        Map<Integer, Boolean> used = new HashMap<>();
        for (int state : deterministicAutomata.states)
            used.put(state, false);

        dfs(deterministicAutomata, startState, used);

        for (int state : deterministicAutomata.states)
            if (used.get(state))
                reachableStates.add(state);

        return reachableStates;
    }

    private static void dfs(DeterministicAutomata deterministicAutomata, int startState, Map<Integer, Boolean> used) {
        used.put(startState, true);
        for (char via : deterministicAutomata.transitionFunction.get(startState).keySet()) {
            Integer to = deterministicAutomata.transitionFunction.get(startState).get(via);
            if (!used.get(to))
                dfs(deterministicAutomata, to, used);
        }
    }

    private static Set<Integer> getDeadStates(DeterministicAutomata deterministicAutomata) {
        Set<Integer> states = new HashSet<>(deterministicAutomata.states);
        states.removeAll(getNotDeadStates(deterministicAutomata));
        return states;
    }

    private static void bfs(Automata automata, Set<Integer> startStates, Map<Integer, Boolean> used) {
        Queue<Integer> queue = new LinkedList<>(startStates);

        while (!queue.isEmpty()) {
            Integer from = queue.element();
            used.put(from, true);

            for (char via : automata.transitionFunction.get(from).keySet())
                for (int to : automata.transitionFunction.get(from).get(via))
                    if (!used.get(to))
                        queue.add(to);

            queue.remove();
        }
    }

    private static Set<Integer> getNotDeadStates(DeterministicAutomata deterministicAutomata) {
        Set<Integer> notDeadStates = new HashSet<>();

        Automata inverseAutomata = inverseDeterministicAutomata(deterministicAutomata);
        Map<Integer, Boolean> used = new HashMap<>();
        for (int state : inverseAutomata.states)
            used.put(state, false);

        bfs(inverseAutomata, inverseAutomata.finalStates, used);

        for (int state : deterministicAutomata.states)
            if (used.get(state))
                notDeadStates.add(state);

        return notDeadStates;
    }

    private static DeterministicAutomata minimizeDeterministicAutomata(DeterministicAutomata deterministicAutomata) {
        Set<Integer> remainingStates = new HashSet<>(deterministicAutomata.states);
        remainingStates.removeAll(getNotReachableStates(deterministicAutomata));
        remainingStates.removeAll(getDeadStates(deterministicAutomata));
        DeterministicAutomata remainingDeterministicAutomata =
                new DeterministicAutomata(deterministicAutomata, remainingStates);

        Set<Set<Integer>> notDistinguishableStates = getNotDistinguishableStates(remainingDeterministicAutomata);
        DeterministicAutomata minimizedDeterministicAutomata =
                new DeterministicAutomata(remainingDeterministicAutomata);

        for (Set<Integer> equivalenceClass : notDistinguishableStates) {
            Integer generalElement = equivalenceClass.iterator().next();

            Set<Integer> states = new HashSet<>(minimizedDeterministicAutomata.states);
            for (int state : states)
                if (equivalenceClass.contains(state) && !Objects.equals(state, generalElement))
                    minimizedDeterministicAutomata.states.remove(state);

            Map<Integer, Map<Character, Integer>> transitionFunction =
                    new HashMap<>(minimizedDeterministicAutomata.transitionFunction);
            for (int from : minimizedDeterministicAutomata.transitionFunction.keySet())
                transitionFunction.put(from,
                        new HashMap<>(minimizedDeterministicAutomata.transitionFunction.get(from)));

            for (int from : transitionFunction.keySet()) {
                if (equivalenceClass.contains(from)) {
                    for (char via : transitionFunction.get(from).keySet())
                        minimizedDeterministicAutomata.transitionFunction.get(generalElement)
                                .put(via, transitionFunction.get(from).get(via));

                    if (from != generalElement)
                        minimizedDeterministicAutomata.transitionFunction.remove(from);
                }
            }

            transitionFunction = new HashMap<>(minimizedDeterministicAutomata.transitionFunction);
            for (int from : minimizedDeterministicAutomata.transitionFunction.keySet())
                transitionFunction.put(from,
                        new HashMap<>(minimizedDeterministicAutomata.transitionFunction.get(from)));

            for (int from : transitionFunction.keySet())
                for (char via : transitionFunction.get(from).keySet())
                    if (equivalenceClass.contains(transitionFunction.get(from).get(via)))
                        minimizedDeterministicAutomata.transitionFunction.get(from).put(via, generalElement);
        }

        return minimizedDeterministicAutomata;
    }

    private static boolean dfsEquivalent(DeterministicAutomata deterministicAutomata1,
                                         DeterministicAutomata deterministicAutomata2, int from1, int from2,
                                         Map<Integer, Integer> permutation1, Map<Integer, Integer> permutation2) {
        permutation1.put(from1, from2);
        permutation2.put(from2, from1);

        for (char via : deterministicAutomata1.transitionFunction.get(from1).keySet()) {
            int to1 = deterministicAutomata1.transitionFunction.get(from1).get(via);

            if (!deterministicAutomata2.transitionFunction.get(from2).containsKey(via))
                return false;
            int to2 = deterministicAutomata2.transitionFunction.get(from2).get(via);

            if (Objects.equals(permutation1.get(to1), -1) && !Objects.equals(permutation2.get(to2), -1) ||
                    !Objects.equals(permutation1.get(to1), -1) && Objects.equals(permutation2.get(to2), -1))
                return false;

            if (Objects.equals(permutation1.get(to1), -1) && Objects.equals(permutation2.get(to2), -1))
                if (!dfsEquivalent(deterministicAutomata1, deterministicAutomata2, to1, to2, permutation1, permutation2))
                    return false;
        }

        for (char via : deterministicAutomata2.transitionFunction.get(from2).keySet()) {
            if (!deterministicAutomata1.transitionFunction.get(from1).containsKey(via))
                return false;
            int to1 = deterministicAutomata1.transitionFunction.get(from1).get(via);

            int to2 = deterministicAutomata2.transitionFunction.get(from2).get(via);

            if (Objects.equals(permutation1.get(to1), -1) && !Objects.equals(permutation2.get(to2), -1) ||
                    !Objects.equals(permutation1.get(to1), -1) && Objects.equals(permutation2.get(to2), -1))
                return false;

            if (Objects.equals(permutation1.get(to1), -1) && Objects.equals(permutation2.get(to2), -1))
                if (!dfsEquivalent(deterministicAutomata1, deterministicAutomata2, to1, to2, permutation1, permutation2))
                    return false;
        }

        return true;
    }

    private static boolean equivalent(DeterministicAutomata deterministicAutomata1,
                                      DeterministicAutomata deterministicAutomata2) {
        Map<Integer, Integer> permutation1 = new HashMap<>();
        for (int state : deterministicAutomata1.states)
            permutation1.put(state, -1);

        Map<Integer, Integer> permutation2 = new HashMap<>();
        for (int state : deterministicAutomata2.states)
            permutation2.put(state, -1);

        return dfsEquivalent(deterministicAutomata1, deterministicAutomata2, deterministicAutomata1.startState,
                deterministicAutomata2.startState, permutation1, permutation2);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);

        System.out.format("Do you want to run option 13 (DFA equivalence)? [Y/n]: ");
        if (Objects.equals(scanner.next(), "Y")) {
            System.out.format("Please enter the name of file with first DFA: ");
            DeterministicAutomata deterministicAutomata1 = readDeterministicAutomata(scanner.next());
            DeterministicAutomata minimizedDeterministicAutomata1 =
                    minimizeDeterministicAutomata(deterministicAutomata1);

            System.out.format("Please enter the name of file with second DFA: ");
            DeterministicAutomata deterministicAutomata2 = readDeterministicAutomata(scanner.next());
            DeterministicAutomata minimizedDeterministicAutomata2 =
                    minimizeDeterministicAutomata(deterministicAutomata2);

            if (equivalent(minimizedDeterministicAutomata1, minimizedDeterministicAutomata2))
                System.out.println("The two deterministic automatons provided ARE equivalent.");
            else
                System.out.println("The two deterministic automatons provided are NOT equivalent.");
        }
    }
}
