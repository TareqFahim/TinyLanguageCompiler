package tinylanguagecompiler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LexicalAnalyzer {

    private List<String> words = Arrays.asList("WRITE", "READ", "IF", "ELSE", "RETURN", "BEGIN", "END", "MAIN", "STRING", "INT", " REAL");
    private List<Character> separators = Arrays.asList(';', ',', '(', ')');
    private List<Character> operators = Arrays.asList('+', '-', '/', '*');
    private List<String> relationalOperators = Arrays.asList(":=", "==", "!=");

    boolean analyzeLine(String str, FileWriter fw) throws IOException {
        if (str.isEmpty()) {
            return true;
        }
        
        if (str.length() >= 2) {
            // split to portions
            str = str.replaceAll("([,();/+/-/*//])", " $1 ");
            String[] strParts = str.split(" ");
            if (strParts.length > 1) {
                for (String part : strParts) {
                    if (!analyzeLine(part.trim().replace(" ", ""), fw)) // recursively analyzeLine each portion
                    {
                        return false;
                    }
                }
            }
        }
        if (str.isEmpty()) {
            return true;
        }
        if (words.contains(str)) {
            fw.write(str.toLowerCase() + " ");
            return true;
        }
        if (relationalOperators.contains(str)) {
            fw.write(str + " ");
            return true;
        }
        char firstChar = str.charAt(0);
        if(firstChar == '"'){
            fw.write("QString" + " ");
            return true;
        }
        if (separators.contains(firstChar)) {
            fw.write(str + " ");
            return true;
        }
        if (operators.contains(firstChar)) {
            fw.write(str + " ");
            return true;
        }
        if (str.matches("^[0-9]+$")) {
            fw.write("num" + " ");
            return true;
        }
        if (str.matches("^[0-9]+.[0-9]+$")) {
            fw.write("num" + " ");
            return true;
        }
        if (str.matches("^_?[a-zA-Z][a-zA-Z0-9_]*$")) {
            fw.write("id" + " ");
            return true;
        }
        return true;
    }

    public boolean isComment(String in) {
        if (in.contains("/**")) {
            return true;
        }
        return false;
    }
}
