import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static class PatternWithColor {

        private Pattern pattern;
        private String color;

        private PatternWithColor(String pattern, String color) {
            this.pattern = Pattern.compile(pattern);
            this.color = color;
        }
    }


    private static void writeToFile(String content, String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

            writer.write(content);

            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getFileContent(String fileName) {
        try {
            return new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static PatternWithColor[] getPatterns() {
        return new PatternWithColor[] {
                // multiline comments //
                new PatternWithColor("/[*](.+|\r|\n)*[*]/", "#74705c"),

                // single line comment //
                new PatternWithColor("//.*", "#74705c"),

                // string formatting //
                new PatternWithColor("[\"][^\"][ ]*(%d|%s|%c|%f)[^\"][\"]", "#e7db62"),

                // string "*\"*" //
                new PatternWithColor("\"(?:\\\\\"|[^\"])*?\"", "#e7db62"),

                // char //
                new PatternWithColor("(\"([^\"\\\\]|\\\\.)*\"|'(\\\\['\"tvrnafb\\\\]|[^'\\\\])')",
                        "#e7db62"),

                // char error //
                new PatternWithColor("[ ]*'..'", "#ff0000"),

                // directives //
                new PatternWithColor("#[ ]*(define|import|include|elif|else|ifndef|" +
                        "error|if|ifdef|pragma|line|undef|using|endif)", "#e62569"),

                // libraries //
                new PatternWithColor("[ ]+<(cassert|cctype|cerrno|cfloat|ciso646|climits|clocale|cmath|" +
                        "csetjmp|csignal|cstdarg|cstddef|cstdio|cstdlib|cstring|ctime|ccomplex|cfenv|cinttypes|" +
                        "cstdalign|cstdbool|cstdint|ctgmath|cwchar|cwctype|algorithm|bitset|complex|deque|exception|" +
                        "fstream|functional|iomanip|ios|iosfwd|iostream|istream|iterator|limits|list|locale|map|" +
                        "memory|new|numeric|ostream|queue|set|sstream|stack|stdexcept|streambuf|string|typeinfo|" +
                        "utility|valarray|vector|array|atomic|chrono|condition_variable|forward_list|future|" +
                        "initializer_list|mutex|random|ratio|regex|scoped_allocator|system_error|thread|tuple|" +
                        "typeindex|type_traits|unordered_map|unordered_set|bits\\/stdc\\+\\+\\.h)>", "#e7db62"),

                // C libraries //
                new PatternWithColor("[ ]+<(assert|complex|ctype|errno|fenv|float|inttypes|iso646|limits|" +
                        "locale|math|setjmp|signal|stdalign|stdarg|stdatomic|stdbool|stddef|stdint|stdio|stdlib|" +
                        "stdnoreturn|string|tgmath|threads|time|uchar|wchar|wctype)\\.h>", "#e7db64"),

                // reserved //
                new PatternWithColor(
                        "(?<!\\w)(break|case|catch|const|const_cast|continue|delete|do|dynamic_cast|far|near|" +
                                "new|sizeof|volatile|goto|static|explicit|export|else|huge|try|this|throw|typeid|" +
                                "switch|sizeof|if|register|operator|mutable|namespace|extern|inline|return|using|" +
                                "virtual|while|default|for)(?!\\w)", "#e62569"),

                // typenames //
                new PatternWithColor("(?<!\\w)(asm|bool|auto|char|class|double|struct|enum|typedef|template|" +
                        "int|union|wchar_t|long|unsigned|signed|void|int)(?!\\w)",
                        "#67d8ef;font-style:italic"),

//                // algorithms //
//                new PatternWithColor("(?<!\\w)(sort|begin|end|empty|" +
//                        // "vector|set|map|queue|stack|pair|tuple|priority_queue|" +
//                        "push_back|back|make_pair|make_tuple|insert|erase|find|" +
//                        "get|max|min|push|pop|tie|top)(?!\\w)","#67d8ef"),

                // type definition //
                new PatternWithColor("(?<=typedef.{1,100}\\s{1,10})([A-Za-z_][A-Za-z_0-9]*)(?>\\;)",
                        "#a6e22d"),

                // bool //
                new PatternWithColor("(?<!\\w)(false|true)(?!\\w)", "#ac80ff"),

                // HEX //
                new PatternWithColor("0x[0-9A-Fa-f]*", "#ac80ff"),

                // BIN //
                new PatternWithColor("0b[01]*", "#ac80ff"),

                // int + float scientific //
                new PatternWithColor("[0-9]*[.]?[0-9]+([eE][-+]?[0-9]+)", "#ac80ff"),

                // function definition //
                new PatternWithColor("(?<!\\w)(main)(?=\\()", "#a6e22c"),

                // function definition //
                new PatternWithColor("[_A-Za-z][0-9A-Za-z_]*" +
                        "(?=\\([A-Za-z_][A-Za-z_0-9]*[*]*[\\s]+[A-Za-z_][A-Za-z_0-9]*)", "#a6e22c"),

                // function call //
                new PatternWithColor("[_A-Za-z][0-9A-Za-z_]*(?=\\()", "#67d8ef"),

                // variable //
                new PatternWithColor("[_A-Za-z][0-9A-Za-z_]*(?!\\()", "#f8f8f2"),

                // int + float //
                new PatternWithColor("[0-9]*[.]?[0-9]+([eE][-+]?[0-9]+)?", "#ac80ff"),

                // OCT //
                new PatternWithColor("0[1-7][0-7]*", "#ac80ff"),

                // delimiters
                new PatternWithColor("[\\(\\)\\{\\}\\;]", "#f8f8f0"),

                // operators //
                new PatternWithColor("[\\+\\-\\*\\/\\.\\%\\[\\]]", "#f8f8f4"),

                // more operators //
                new PatternWithColor("[\\<\\>\\=\\&\\|\\,\\?\\:\\!\\^\\~]", "#f8f8f4"),

                // error //
                new PatternWithColor("\"", "#ff0000"),

        };
    }

    private static String[] colorText(String source) {
        String[] coloredText = new String[source.length()];
        for (int i = 0; i < source.length(); ++i)
            coloredText[i] = null;

        PatternWithColor[] patterns = getPatterns();

        for (PatternWithColor pattern : patterns) {
            Matcher m = pattern.pattern.matcher(source);

            while (m.find()) {
                boolean canColoring = true;

                for (int i = m.start(); i < m.end(); i++)
                    if (coloredText[i] != null)
                        canColoring = false;

                if (canColoring)
                    for (int i = m.start(); i < m.end(); i++)
                        coloredText[i] = pattern.color;
            }
        }

        return coloredText;
    }

    private static void createHtml(String source, String out_file_name) {
        StringBuilder builder = new StringBuilder();

        String[] colors = colorText(source);

        builder.append(
                "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <title>C++ program colored lexemes</title>\n" +
                        "</head>\n" +
                        "<style>\n" +
                        "html {\n" +
                        "    font-family: monospace;\n" +
                        "    font-size: 16px;\n" +
                        "    line-height: 60%;\n" +
                        "}\n" +
                        "\n" +
                        "span {\n" +
                        "    white-space: pre-wrap;\n" +
                        "}\n" +
                        "</style>\n" +
                        "<body bgcolor=\"#282923\">\n" +
                        "\n"
        );

        for (int i = 0; i < source.length(); i++)
            builder.append(String.format("<span style='color:%s;'>%c</span>", colors[i], source.charAt(i)));

        builder.append(
                "</body>\n" +
                        "</html>"
        );

        writeToFile(builder.toString(), out_file_name);
    }

    public static void main(String[] args) {
        for (String s : Arrays.asList("C", "D", "source", "main")) {

            String source = getFileContent(s + ".cpp");

            createHtml(source, s + ".html");
        }
    }
}
