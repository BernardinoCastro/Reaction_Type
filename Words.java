import java.util.Random;

public class Words {
    private static final String[] WORD_LIST = {
            "java", "swing", "keyboard", "typing", "speed", "challenge",
            "practice", "game", "random", "word", "falling", "letters",
            "code", "developer", "computer", "fast", "accurate", "learn",
            "debug", "compile", "execute", "syntax", "algorithm", "variable",
            "function", "loop", "condition", "inheritance", "polymorphism",
            "object", "classloader", "recursion", "framework", "instance", "constructor",
            "serialization",  "lambda", "expression",
            "generic", "exception", "stack", "heap", "garbage", "collection",
            "pointer", "reference", "method", "interface", "parameter", "argument",
            "callback", "listener", "event", "asynchronous", "synchronous", "thread",
            "heap", "stacktrace", "buffer", "cache", "optimization", "refactor",
            "dependency", "library", "repository", "query", "database", "schema",
            "transaction", "commit", "rollback", "index", "primary", "foreign",
            "normalization", "cluster", "shard", "node", "cloud",
            "API", "REST", "endpoint", "token", "authentication", "authorization",
            "session", "cookie", "encryption", "decryption", "hashing", "salting",
            "firewall", "proxy", "router", "bandwidth", "latency", "protocol",
            "packet", "socket",
            "server", "client", "hosting", "deployment", "container"
    };

    private static final String[] JAVA_KEYWORDS = {
            "public", "class", "static", "void", "int", "String",
            "boolean", "import", "new", "return",
            "if", "else", "for", "while", "try", "catch", "final",
            "extends", "implements", "abstract", "interface", "package",
            "private", "protected", "default", "super", "this", "throw",
            "throws", "synchronized", "volatile", "transient", "enum", "assert",
            "break", "continue", "case", "switch", "do", "instanceof",
            "strictfp", "native", "finally", "const", "goto", "module",
            "requires", "exports", "opens", "provides", "uses", "with",
            "record", "sealed", "permits", "yield", "var",
            "annotation", "lambda", "stream", "optional", "reflection",
            "serialization", "functional", "predicate", "consumer", "supplier",
            "biFunction", "comparator", "singleton", "dependency", "autoboxing",
            "unboxing", "wildcard", "bounded", "erasure", "generic-type",
            "covariant", "contravariant", "diamond", "immutable", "mutable",
            "concurrency", "executor", "future",
             "parallel", "finalizer"
    };

    private static final Random RANDOM = new Random();

    public static String getRandomWord() {
        return WORD_LIST[RANDOM.nextInt(WORD_LIST.length)];
    }

    public static String getRandomJavaWord() {
        return JAVA_KEYWORDS[RANDOM.nextInt(JAVA_KEYWORDS.length)];
    }
}
