import java.io.FileNotFoundException;
import java.util.*;

class DFA {
    Set<Character> alphabet;

    Set<Integer> states;

    // states.contains(startState);
    Integer startState;

    // states.containsAll(finalStates);
    Set<Integer> finalStates;

    // states.containsAll(transitionFunctions.keySet();
    // alphabet.containsAll(new HashSet<Character>(transitionFunctions.values()));
    Map<Integer, Map<Character, Integer>> transitionFunction;

    DFA() {
        alphabet = new HashSet<>();
        states = new HashSet<>();
        startState = 0;
        finalStates = new HashSet<>();
        transitionFunction = new HashMap<>();
    }

    private DFA(Set<Character> alphabet_, Set<Integer> states_, Integer startState_, Set<Integer> finalStates_,
                Map<Integer, Map<Character, Integer>> transitionFunction_) {
        alphabet = new HashSet<>(alphabet_);
        states = new HashSet<>(states_);
        startState = startState_;
        finalStates = new HashSet<>(finalStates_);
        transitionFunction = new HashMap<>(transitionFunction_);
        for (Integer fromState : transitionFunction_.keySet()) {
            transitionFunction.put(fromState, new HashMap<>());
            for (Character viaLetter : transitionFunction_.get(fromState).keySet()) {
                Integer toState = transitionFunction_.get(fromState).get(viaLetter);
                transitionFunction.get(fromState).put(viaLetter, toState);
            }
        }
    }

    private DFA(DFA dfa) {
        this(dfa.alphabet, dfa.states, dfa.startState, dfa.finalStates, dfa.transitionFunction);
    }

    DFA(NFA nfa) {
        this(nfa.determine());
    }

    private DFA(Scanner fileScanner) {
        String preAlphabet = "abcdefghijklmnopqrstuvwxyz";
        int alphabetSize = fileScanner.nextInt(); // <= 26
        alphabet = new HashSet<>();
        for (int i = 0; i < alphabetSize; ++i) {
            alphabet.add(preAlphabet.charAt(i));
        }

        int numberOfStates = fileScanner.nextInt();
        states = new HashSet<>(numberOfStates);
        for (int i = 0; i < numberOfStates; ++i) {
            states.add(i);
        }

        startState = fileScanner.nextInt();

        int numberOfFinalStates = fileScanner.nextInt();
        finalStates = new HashSet<>(numberOfFinalStates);
        for (int i = 0; i < numberOfFinalStates; ++i) {
            finalStates.add(fileScanner.nextInt());
        }

        transitionFunction = new HashMap<>(numberOfStates);
        for (Integer state : states) {
            transitionFunction.put(state, new HashMap<>());
        }

        while (fileScanner.hasNext()) {
            Integer fromState = fileScanner.nextInt();
            Character viaLetter = fileScanner.next().charAt(0);
            Integer toState = fileScanner.nextInt();
            transitionFunction.get(fromState).put(viaLetter, toState);
        }
    }

    DFA(String pathname) throws FileNotFoundException {
        this(Main.getScanner(pathname));
    }

    private NFA inverse() {
        NFA nfa = new NFA();
        nfa.alphabet = new HashSet<>(alphabet);
        nfa.states = new HashSet<>(states);
        nfa.startState = startState;
        nfa.finalStates = new HashSet<>(finalStates);

        nfa.transitionFunction = new HashMap<>();
        for (Integer fromState : states) {
            nfa.transitionFunction.put(fromState, new HashMap<>());
        }
        for (Integer fromState : states) {
            for (Character viaLetter : transitionFunction.get(fromState).keySet()) {
                Integer toState = transitionFunction.get(fromState).get(viaLetter);
                if (!nfa.transitionFunction.get(toState).keySet().contains(viaLetter)) {
                    nfa.transitionFunction.get(toState).put(viaLetter, new HashSet<>());
                }
                nfa.transitionFunction.get(toState).get(viaLetter).add(fromState);
            }
        }
        return nfa;
    }

    DFA minimize() {
        // TODO
        return new DFA();
    }

    private Map<Integer, Boolean> dfs(Map<Integer, Boolean> used) {
        return dfsFromState(used, startState);
    }

    private Map<Integer, Boolean> dfsFromState(Map<Integer, Boolean> used, Integer fromState) {
        used.put(fromState, true);
        for (Character viaLetter : transitionFunction.get(fromState).keySet()) {
            Integer toState = transitionFunction.get(fromState).get(viaLetter);
            if (!used.get(toState)) {
                dfsFromState(used, toState);
            }
        }
        return used;
    }

    private Map<Integer, Boolean> dfsFromStates(Map<Integer, Boolean> used, Set<Integer> fromStates) {
        for (Integer fromState : fromStates) {
            dfsFromState(used, fromState);
        }
        return used;
    }

    private Map<Integer, Boolean> dfs() {
        Map<Integer, Boolean> used = new HashMap<>();
        for (Integer state : states) {
            used.put(state, false);
        }
        return dfs(used);
    }

    private Map<Integer, Boolean> dfsFromState(Integer fromState) {
        Map<Integer, Boolean> used = new HashMap<>();
        for (Integer state : states) {
            used.put(state, false);
        }
        return dfsFromState(used, fromState);
    }

    Map<Integer, Boolean> dfsFromStates(Set<Integer> fromStates) {
        Map<Integer, Boolean> used = new HashMap<>();
        for (Integer state : states) {
            used.put(state, false);
        }
        return dfsFromStates(used, fromStates);
    }

    Map<Integer, Boolean> bfs() {
        return bfsFromState(startState);
    }

    private Map<Integer, Boolean> bfsFromState(Integer fromState) {
        Map<Integer, Boolean> used = new HashMap<>();
        for (Integer state : states) {
            used.put(state, false);
        }

        Queue<Integer> states = new LinkedList<>();
        states.add(fromState);
        while (!states.isEmpty()) {
            Integer fromState_ = states.peek();
            used.put(fromState_, true);

            for (Character viaLetter : transitionFunction.get(fromState_).keySet()) {
                Integer toState = transitionFunction.get(fromState_).get(viaLetter);
                if (!used.get(toState)) {
                    states.add(toState);
                }
            }

            states.poll();
        }

        return used;
    }

    private Map<Integer, Boolean> bfsFromStates(Set<Integer> fromStates) {
        Map<Integer, Boolean> used = new HashMap<>();
        for (Integer state : states) {
            used.put(state, false);
        }

        Queue<Integer> states = new LinkedList<>(fromStates);
        while (!states.isEmpty()) {
            Integer fromState_ = states.peek();
            used.put(fromState_, true);

            for (Character viaLetter : transitionFunction.get(fromState_).keySet()) {
                Integer toState = transitionFunction.get(fromState_).get(viaLetter);
                if (!used.get(toState)) {
                    states.add(toState);
                }
            }

            states.poll();
        }

        return used;
    }

    private Set<Integer> getReachable() {
        return getReachableFromState(startState);
    }

    private Set<Integer> getReachableFromState(Integer fromState) {
        Set<Integer> fromStates = new HashSet<>();
        fromStates.add(fromState);
        return getReachableFromStates(fromStates);
    }

    private Set<Integer> getReachableFromStates(Set<Integer> fromStates) {
        Map<Integer, Boolean> used = bfsFromStates(fromStates);
        Set<Integer> visited = new HashSet<>();
        for (Integer state : used.keySet()) {
            if (used.get(state)) {
                visited.add(state);
            }
        }
        return visited;
    }

    Set<Integer> getNotReachable() {
        return getNotReachableFromState(startState);
    }

    private Set<Integer> getNotReachableFromState(Integer fromState) {
        Set<Integer> states_ = new HashSet<>(states);
        states_.removeAll(getReachableFromState(fromState));
        return states_;
    }

    Set<Integer> getNotReachableFromStates(Set<Integer> fromStates) {
        Set<Integer> states_ = new HashSet<>(states);
        states_.removeAll(getReachableFromStates(fromStates));
        return states_;
    }

    Set<Integer> getDead() {
        return getDeadForStates(finalStates);
    }

    Set<Integer> getDeadForState(Integer forState) {
        Set<Integer> states_ = new HashSet<>(states);
        states_.removeAll(getNotDeadForState(forState));
        return states_;
    }

    private Set<Integer> getDeadForStates(Set<Integer> forStates) {
        Set<Integer> states_ = new HashSet<>(states);
        states_.removeAll(getNotDeadForStates(forStates));
        return states_;
    }

    Set<Integer> getNotDead() {
        return getNotDeadForStates(finalStates);
    }

    private Set<Integer> getNotDeadForState(Integer forState) {
        Set<Integer> forStates = new HashSet<>();
        forStates.add(forState);
        return getNotDeadForStates(forStates);
    }

    private Set<Integer> getNotDeadForStates(Set<Integer> forStates) {
        NFA inverseDFA = inverse();
        return inverseDFA.getReachableFromStates(forStates);
    }

    Set<Integer> makeStep() {
        return makeStepFromState(startState);
    }

    private Set<Integer> makeStepFromState(Integer fromState) {
        Set<Integer> toStates = new HashSet<>();
        for (Character viaLetter : transitionFunction.get(fromState).keySet()) {
            toStates.add(transitionFunction.get(fromState).get(viaLetter));
        }
        return toStates;
    }

    private Set<Integer> makeStepFromStates(Set<Integer> fromStates) {
        Set<Integer> toStates = new HashSet<>();
        for (Integer fromState : fromStates) {
            toStates.addAll(makeStepFromState(fromState));
        }
        return toStates;
    }

    private Set<Integer> makeSteps(Integer steps) {
        return makeStepsFromState(steps, startState);
    }

    private Set<Integer> makeStepsFromState(Integer steps, Integer fromState) {
        Set<Integer> fromStates = new HashSet<>();
        fromStates.add(fromState);
        return makeStepsFromStates(steps, fromStates);
    }

    private Set<Integer> makeStepsFromStates(Integer steps, Set<Integer> fromStates) {
        Set<Integer> states_ = new HashSet<>(fromStates);
        for (int step = 0; step < steps; ++step) {
            states_ = makeStepFromStates(states_);
        }
        return states_;
    }

    Set<Integer> makeCompleteStep() throws Main.CompleteStepNotPossibleException {
        return makeCompleteStepFromState(startState);
    }

    private Set<Integer> makeCompleteStepFromState(Integer fromState) throws Main.CompleteStepNotPossibleException {
        Set<Integer> toStates = new HashSet<>();
        for (Character viaLetter : alphabet) {
            if (!transitionFunction.get(fromState).keySet().contains(viaLetter)) {
                throw new Main.CompleteStepNotPossibleException("NO transition from state " + fromState +
                        " via " + viaLetter + ".");
            }
            toStates.add(transitionFunction.get(fromState).get(viaLetter));
        }
        return toStates;
    }

    private Set<Integer> makeCompleteStepFromStates(Set<Integer> fromStates) throws Main.CompleteStepNotPossibleException {
        Set<Integer> toStates = new HashSet<>();
        for (Integer fromState : fromStates) {
            toStates.addAll(makeCompleteStepFromState(fromState));
        }
        return toStates;
    }

    Set<Integer> makeCompleteSteps(Integer steps) throws Main.CompleteStepNotPossibleException {
        return makeCompleteStepsFromState(steps, startState);
    }

    private Set<Integer> makeCompleteStepsFromState(Integer steps, Integer fromState) throws Main.CompleteStepNotPossibleException {
        Set<Integer> fromStates = new HashSet<>();
        fromStates.add(fromState);
        return makeCompleteStepsFromStates(steps, fromStates);
    }

    private Set<Integer> makeCompleteStepsFromStates(Integer steps, Set<Integer> fromStates) throws Main.CompleteStepNotPossibleException {
        Set<Integer> states_ = new HashSet<>(fromStates);
        for (int step = 0; step < steps; ++step) {
            states_ = makeCompleteStepFromStates(states_);
        }
        return states_;
    }

    Integer processWord(String word) {
        return processWordFromState(word, startState);
    }

    private Integer processWordFromState(String word, Integer fromState) {
        Integer state = fromState;
        for (Character viaLetter : word.toCharArray()) {
            state = transitionFunction.get(state).get(viaLetter);
        }
        return state;
    }

    private Set<Integer> processWordFromStates(String word, Set<Integer> fromStates) {
        Set<Integer> states_ = new HashSet<>();
        for (Integer fromState : fromStates) {
            states_.add(processWordFromState(word, fromState));
        }
        return states_;
    }

    Boolean isAcceptable(String word) {
        return isAcceptableFromState(word, startState);
    }

    private Boolean isAcceptableFromState(String word, Integer fromState) {
        Integer toState = processWordFromState(word, fromState);
        return finalStates.contains(toState);
    }

    Boolean isAcceptableFromStates(String word, Set<Integer> fromStates) {
        Set<Integer> toStates = processWordFromStates(word, fromStates);
        toStates.retainAll(fromStates);
        return (!toStates.isEmpty());
    }

    private Set<Character> getAcceptableLetters() {
        Set<Character> acceptableLetters = new HashSet<>();
        for (Character viaLetter : alphabet) {
            Integer toState = transitionFunction.get(startState).get(viaLetter);
            if (finalStates.contains(toState)) {
                acceptableLetters.add(viaLetter);
            }
        }
        return acceptableLetters;
    }

    Set<Character> getNotAcceptableLetters() {
        Set<Character> notAcceptableLetters = new HashSet<>(alphabet);
        notAcceptableLetters.removeAll(getAcceptableLetters());
        return notAcceptableLetters;
    }
}