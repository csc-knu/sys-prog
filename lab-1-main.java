// Some necessary imports
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class Main {
    // I do not actually need command line arguments, but let's leave it as is
    public static void main(String[] args) {
        // Construct new Scanner to read input from console
        Scanner scanner = new Scanner(System.in);

        // Read a pathname from scanner
        System.out.format("Please enter the name of the input file: ");
        String pathname = scanner.next();

        // Handle possible FileNotFoundException later on
        try {
            // Option 1: find words of maximal length
            System.out.format("Do you want to run option 1 (max len)? [Y/n]: ");
            String response = scanner.next();
            if (Objects.equals(response, "Y")) {
                Scanner fileScanner = getConfiguredScanner(pathname);
                Set<String> maximalLengthWords = findMaxLenWords(fileScanner);
                fileScanner.close(); // Always close Scanners
                System.out.println("Maximal length words:");
                printStringSet(maximalLengthWords, "\t");
            }

            // Option 2: count frequencies for all words
            System.out.format("Do you want to run option 2 (count freqs)? [Y/n]: ");
            response = scanner.next();
            if (Objects.equals(response, "Y")) {
                Scanner fileScanner = getConfiguredScanner(pathname);
                Map<String, Integer> wordsFrequencies = countWordsFrequencies(fileScanner);
                fileScanner.close(); // Always close Scanners
                System.out.println("Word: frequency:");
                printStringIntegerMap(wordsFrequencies, "\t", ": ");
            }

            // Option 3: words with vowels only
            System.out.format("Do you want to run option 3 (vowel words)? [Y/n]: ");
            response = scanner.next();
            if (Objects.equals(response, "Y")) {
                Scanner fileScanner = getConfiguredScanner(pathname);
                Set<String> vowelWords = findVowelWords(fileScanner);
                fileScanner.close(); // Always close Scanners
                System.out.println("Vowel words:");
                printStringSet(vowelWords, "\t");
            }

            // Option 4: words with unique letters
            System.out.format("Do you want to run option 4 (unique letters)? [Y/n]: ");
            response = scanner.next();
            if (Objects.equals(response, "Y")) {
                Scanner fileScanner = getConfiguredScanner(pathname);
                Set<String> uniqueLettersWords = findUniqueLettersWords(fileScanner);
                fileScanner.close(); // Always close Scanners
                System.out.println("Unique letters words:");
                printStringSet(uniqueLettersWords, "\t");
            }

            // Option 5: words with double consonants
            System.out.format("Do you want to run option 5 (double consonants)? [Y/n]: ");
            response = scanner.next();
            if (Objects.equals(response, "Y")) {
                Scanner fileScanner = getConfiguredScanner(pathname);
                Set<String> doubleConsonantsWords = findDoubleConsonantsWords(fileScanner);
                fileScanner.close(); // Always close Scanners
                System.out.println("Double consonants words:");
                printStringSet(doubleConsonantsWords, "\t");
            }

            // Option 6: words with maximal consonant substring
            System.out.format("Do you want to run option 6 (max consonant substr)? [Y/n]: ");
            response = scanner.next();
            if (Objects.equals(response, "Y")) {
                Scanner fileScanner = getConfiguredScanner(pathname);
                Set<String> maximalConsonantSubstringWords = findMaximalConsonantSubstringWords(fileScanner);
                fileScanner.close(); // Always close Scanners
                System.out.println("Words with maximal consonant substring:");
                printStringSet(maximalConsonantSubstringWords, "\t");
            }

            // Option 7: most frequent words
            System.out.format("Do you want to run option 7 (most frequent)? [Y/n]: ");
            response = scanner.next();
            if (Objects.equals(response, "Y")) {
                Scanner fileScanner = getConfiguredScanner(pathname);
                Set<String> mostFrequentWords = findMostFrequentWords(fileScanner);
                fileScanner.close(); // Always close Scanners
                System.out.println("Most frequent words:");
                printStringSet(mostFrequentWords, "\t");
            }

            // Option 8: words with more vowels than consonants
            System.out.format("Do you want to run option 8 (more vowels than consonants)? [Y/n]: ");
            response = scanner.next();
            if (Objects.equals(response, "Y")) {
                Scanner fileScanner = getConfiguredScanner(pathname);
                Set<String> moreVowelsThanConsonantsWords = findMoreVowelsThanConsonantsWords(fileScanner);
                fileScanner.close(); // Always close Scanners
                System.out.println("Words with more vowels than consonants:");
                printStringSet(moreVowelsThanConsonantsWords, "\t");
            }

            // Option 9: find words of maximal length
            System.out.format("Do you want to run option 9 (max unique)? [Y/n]: ");
            response = scanner.next();
            if (Objects.equals(response, "Y")) {
                Scanner fileScanner = getConfiguredScanner(pathname);
                Set<String> maximalUniqueLettersWords = findMaximalUniqueLettersWords(fileScanner);
                fileScanner.close(); // Always close Scanners
                System.out.println("Words with maximal number of unique letters:");
                printStringSet(maximalUniqueLettersWords, "\t");
            }

            // Option 10: sort words by length
            System.out.format("Do you want to run option 10 (sort len)? [Y/n]: ");
            response = scanner.next();
            if (Objects.equals(response, "Y")) {
                Scanner fileScanner = getConfiguredScanner(pathname);
                List<String> sortedByLengthWords = getSortedByLengthWords(fileScanner);
                fileScanner.close(); // Always close Scanners
                System.out.println("Words sorted by length:");
                printStringList(sortedByLengthWords, "\t");
            }

            // Option 11: sort words by vowels fraction
            System.out.format("Do you want to run option 11 (sort vowels frac)? [Y/n]: ");
            response = scanner.next();
            if (Objects.equals(response, "Y")) {
                Scanner fileScanner = getConfiguredScanner(pathname);
                List<String> sortedByVowelsFractionWords = getSortedByVowelsFractionWords(fileScanner);
                fileScanner.close(); // Always close Scanners
                System.out.println("Words sorted by vowels fraction:");
                printStringList(sortedByVowelsFractionWords, "\t");
            }

            // Option 12: sort words by number of consonants
            System.out.format("Do you want to run option 12 (sort consonants num)? [Y/n]: ");
            response = scanner.next();
            if (Objects.equals(response, "Y")) {
                Scanner fileScanner = getConfiguredScanner(pathname);
                List<String> sortedByNumberOfConsonantsWords = getSortedByNumberOfConsonantsWords(fileScanner);
                fileScanner.close(); // Always close Scanners
                System.out.println("Words sorted by number of consonants:");
                printStringList(sortedByNumberOfConsonantsWords, "\t");
            }

            // TODO: options 13-14
        } catch (FileNotFoundException e) {
            System.out.format("Could not find file '%s'.%n", pathname);
            // An error occurred, we shall stop
            System.exit(1);
        }

        // Always close Scanners
        scanner.close();
    }

    private static Set<String> findMaximalConsonantSubstringWords(Scanner scanner) {
        int maxConsonantSubstringLength = 0;

        String currentWord;

        Set<String> maxConsonantSubstringLengthWords = new HashSet<>();

        while (scanner.hasNext()) {
            currentWord = scanner.next();

            if (getLengthOfMaximalConsonantSubstring(currentWord) > maxConsonantSubstringLength) {
                maxConsonantSubstringLength = getLengthOfMaximalConsonantSubstring(currentWord);
                maxConsonantSubstringLengthWords.clear();
                maxConsonantSubstringLengthWords.add(currentWord);
            } else if (getLengthOfMaximalConsonantSubstring(currentWord) == maxConsonantSubstringLength) {
                maxConsonantSubstringLengthWords.add(currentWord);
            }
        }

        return maxConsonantSubstringLengthWords;
    }

    private static int getLengthOfMaximalConsonantSubstring(String word) {
        int maxConsonantSubstringLength = 0, currentConsonantSubstringLength = 1;

        for (int currentPosition = 0; currentPosition < word.length(); ++currentPosition) {
            if (isConsonant(word.charAt(currentPosition))) {
                ++currentConsonantSubstringLength;

                if (currentConsonantSubstringLength > maxConsonantSubstringLength) {
                    maxConsonantSubstringLength = currentConsonantSubstringLength;
                }
            } else {
                currentConsonantSubstringLength = 0;
            }
        }

        return maxConsonantSubstringLength;
    }

    private static Set<String> getUniqueWords(Scanner scanner) {
        Set<String> uniqueWords = new HashSet<>();

        String currentWord;

        while (scanner.hasNext()) {
            currentWord = scanner.next();

            uniqueWords.add(currentWord);
        }

        return uniqueWords;
    }

    private static List<String> getSortedByNumberOfConsonantsWords(Scanner scanner) {
        Set<String> uniqueWords = getUniqueWords(scanner);

        // Not sorted yet
        List<String> sortedByConsonantsNumberFractionWords = new ArrayList<>(uniqueWords);

        sortedByConsonantsNumberFractionWords.sort(Comparator.comparing(Main::getNumberOfConsonants));

        return sortedByConsonantsNumberFractionWords;
    }

    private static List<String> getSortedByVowelsFractionWords(Scanner scanner) {
        Set<String> uniqueWords = getUniqueWords(scanner);

        // Not sorted yet
        List<String> sortedByVowelsFractionWords = new ArrayList<>(uniqueWords);

        sortedByVowelsFractionWords.sort((s1, s2) -> {
            // Multiplications instead of division stands for improved precision
            return getNumberOfVowels(s1) * s2.length() - getNumberOfVowels(s2) * s1.length();
        });

        return sortedByVowelsFractionWords;
    }

    private static Scanner getConfiguredScanner(String pathname) throws FileNotFoundException {
        // Construct new File by given filename
        File file = new File(pathname);

        if (!file.exists()) {
            System.out.format("File '%s' does not exist.%n", pathname);
            // An error occurred, we shall stop
            System.exit(3);
        }

        if (!file.canRead()) {
            System.out.format("Cannot read file '%s'.%n", pathname);
            // An error occurred, we shall stop
            System.exit(2);
        }

        // Construct new Scanner to read file
        Scanner fileScanner = new Scanner(file);

        // Set its delimiter to any sequence of non-alpha characters
        fileScanner.useDelimiter("[^a-zA-Z]+");

        return fileScanner;
    }

    private static List<String> getSortedByLengthWords(Scanner scanner) {
        Set<String> uniqueWords = getUniqueWords(scanner);

        // Not sorted yet
        List<String> sortedByLengthWords = new ArrayList<>(uniqueWords);

        sortedByLengthWords.sort(Comparator.comparing(String::length));

        return sortedByLengthWords;
    }

    private static void printStringList(List<String> ls, String indent) {
        for (String s: ls) {
            System.out.println(indent + s);
        }
    }

    private static Set<String> findMaximalUniqueLettersWords(Scanner scanner) {
        int maxUnique = 0;

        String currentWord;

        Set<String> maximalUniqueLettersWords = new HashSet<>();

        while (scanner.hasNext()) {
            currentWord = scanner.next();

            if (getNumberOfUniqueLetters(currentWord) > maxUnique) {
                maxUnique = getNumberOfUniqueLetters(currentWord);
                maximalUniqueLettersWords.clear();
                maximalUniqueLettersWords.add(currentWord);
            } else if (getNumberOfUniqueLetters(currentWord) == maxUnique) {
                maximalUniqueLettersWords.add(currentWord);
            }
        }

        return maximalUniqueLettersWords;
    }

    private static int getNumberOfUniqueLetters(String s) {
        return getUniqueCharacters(s).size();
    }

    private static Set<String> findMoreVowelsThanConsonantsWords(Scanner scanner) {
        Set<String> moreVowelsThanConsonantsWords = new HashSet<>();

        String currentWord;

        while (scanner.hasNext()) {
            currentWord = scanner.next();

            if (getNumberOfVowels(currentWord) > getNumberOfConsonants(currentWord)) {
                moreVowelsThanConsonantsWords.add(currentWord);
            }
        }

        return moreVowelsThanConsonantsWords;
    }

    private static boolean isVowel(Character c) {
        return Objects.equals(c, 'a') || Objects.equals(c, 'e') || Objects.equals(c, 'i') ||
                Objects.equals(c, 'u') || Objects.equals(c, 'o') || Objects.equals(c, 'A') ||
                Objects.equals(c, 'E') || Objects.equals(c, 'I') || Objects.equals(c, 'U') ||
                Objects.equals(c, 'O');
    }

    private static boolean isConsonant(Character c) {
        return !isVowel(c);
    }

    private static int getNumberOfVowels(String s) {
        int numberOfVowels = 0;

        for (Character c: s.toCharArray()) {
            if (isVowel(c)) {
                ++numberOfVowels;
            }
        }

        return numberOfVowels;
    }

    private static int getNumberOfConsonants(String s) {
        return s.length() - getNumberOfVowels(s);
    }

    private static Set<String> findMostFrequentWords(Scanner scanner) {
        // From option 2
        Map<String, Integer> wordsFrequencies = countWordsFrequencies(scanner);

        Integer maxFrequency = Collections.max(wordsFrequencies.values());

        Set<String> mostFrequentWords = new HashSet<>();

        for (String word: wordsFrequencies.keySet()) {
            if (Objects.equals(wordsFrequencies.get(word), maxFrequency)) {
                mostFrequentWords.add(word);
            }
        }

        return mostFrequentWords;
    }

    private static Set<String> findDoubleConsonantsWords(Scanner scanner) {
        Set<String> doubleConsonantsWords = new HashSet<>();

        String currentWord;

        while (scanner.hasNext()) {
            currentWord = scanner.next();

            if (currentWord.matches(".*([^auioeAUIOE])\\1.*")) {
                doubleConsonantsWords.add(currentWord);
            }
        }

        return doubleConsonantsWords;
    }

    private static Set<Character> getUniqueCharacters(String s) {
        Set<Character> uniqueCharacters = new HashSet<>();

        for (char c: s.toCharArray()) {
            uniqueCharacters.add(c);
        }

        return uniqueCharacters;
    }

    private static Set<String> findUniqueLettersWords(Scanner scanner) {
        Set<String> uniqueLettersWords = new HashSet<>();

        String currentWord;

        while (scanner.hasNext()) {
            currentWord = scanner.next();

            Set<Character> uniqueCharacters = getUniqueCharacters(currentWord);

            if (uniqueCharacters.size() == currentWord.length()) {
                uniqueLettersWords.add(currentWord);
            }
        }

        return uniqueLettersWords;
    }

    private static Set<String> findVowelWords(Scanner scanner) {
        Set<String> vowelWords = new HashSet<>();

        String currentWord;

        while (scanner.hasNext()) {
            currentWord = scanner.next();

            if (currentWord.matches("[auioeAUIOE]+")) {
                vowelWords.add(currentWord);
            }
        }

        return vowelWords;
    }

    private static Map<String, Integer> countWordsFrequencies(Scanner scanner) {
        Map<String, Integer> wordsFrequencies = new HashMap<>();

        String currentWord;

        while (scanner.hasNext()) {
            currentWord = scanner.next();

            if (wordsFrequencies.containsKey(currentWord)) {
                wordsFrequencies.put(currentWord, wordsFrequencies.get(currentWord) + 1);
            } else {
                wordsFrequencies.put(currentWord, 1);
            }
        }

        return wordsFrequencies;
    }

    private static Set<String> findMaxLenWords(Scanner scanner) {
        // Current maximal word length
        int maxLength = 0;

        String currentWord; // = null;

        // Set of words with (current) maximal length
        Set<String> maxLengthWords = new HashSet<>();

        // Check if there is one more word in scanner
        while (scanner.hasNext()) {
            // Read next word
            currentWord = scanner.next();

            // Current word has length greater than current maximal length
            if (currentWord.length() > maxLength) {
                // Update current maximal length
                maxLength = currentWord.length();
                // Remove all (shorter than current word) words from the set
                maxLengthWords.clear();
                // Add current word to the set of maximal length words
                maxLengthWords.add(currentWord);
            }
            // Current word has length equal to current maximal length
            else if (currentWord.length() == maxLength) {
                // Add current word to the set of maximal length words
                maxLengthWords.add(currentWord);
            }
        }

        return maxLengthWords;
    }

    private static void printStringSet(Set<String> ss, String indent) {
        // For each loop over set
        for (String s: ss) {
            // Print words tab-indented for better reading
            System.out.println(indent + s);
        }
    }

    private static void printStringIntegerMap(Map<String, Integer> msi, String indent, String separator) {
        // For each loop over map
        for (String s: msi.keySet()) {
            // Print words tab-indented and colon separated for better reading
            System.out.println(indent + s + separator + msi.get(s));
        }
    }
}
