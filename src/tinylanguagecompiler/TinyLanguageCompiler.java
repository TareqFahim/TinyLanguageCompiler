package tinylanguagecompiler;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.applet.Main;

public class TinyLanguageCompiler {

    public static void main(String[] args) {

        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
        Scanner s;
        File f_in = new File("inputCode.txt");
        File f_out = new File("outputLexical.txt");
//        FileOutputStream out = null;
        FileWriter fw;
        List<String> tokens = new ArrayList();
        try {
            s = new Scanner(f_in);
            fw = new FileWriter(f_out);
            while (s.hasNext()) {
                String str = s.nextLine();
                if (!lexicalAnalyzer.isComment(str)) {
                    if (!lexicalAnalyzer.analyzeLine(str, fw)) {
                        fw.write(" ERROR");
                    }
//	                fw.write("\n");
                }

            }
            fw.close();
            s = new Scanner(f_out);
            while (s.hasNext()) {
//                tokens.add(s.next());
                Parser parser = new Parser();
                System.out.println("" + parser.lineParse(s.nextLine()));
            }
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
