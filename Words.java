import java.util.Random;

public class Words {
    private static final String[] WORD_LIST = {
            "java", "swing", "keyboard", "typing", "speed", "challenge",
            "practice", "game", "random", "word", "falling", "letters",
            "code", "developer", "computer", "fast", "accurate", "learn"
    };

    private static final String[] JAVA_SYNTAX = {
            "public", "class", "static", "void", "int", "String",
            "boolean", "System.out.println", "import", "new", "return",
            "if", "else", "for", "while", "try", "catch", "final"
    };

    private static final Random RANDOM = new Random();

    public static String getRandomWord() {
        return WORD_LIST[RANDOM.nextInt(WORD_LIST.length)];
    }

    public static String getRandomJavaWord() {
        return JAVA_SYNTAX[RANDOM.nextInt(JAVA_SYNTAX.length)];
    }
}

