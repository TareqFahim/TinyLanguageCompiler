package tinylanguagecompiler;

import static java.awt.PageAttributes.MediaType.C;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Parser {

    int[][] pred_table = new int[32][27];
    Map nonTerminalMap = new HashMap();
    Map terminalMap = new HashMap();
    String[] productions = new String[60];
    Stack inputStack = new Stack();
    Stack parseStack = new Stack();
    int nonTerminalIndex, terminalIndex;
    String parseStackPeek , inputStackPeek;

    public Parser() {
        File json = new File("json.txt");
        File prod = new File("production.txt");
        File non_terminals = new File("non-terminal.txt");
        String terminalStr = "0,int,real,string,id,(,),main,#,begin,end,;,:=,QString,return,if,else,write,read,-,+,*,/,num,==,!=,$";
        String[] strs = terminalStr.split(",");
        for (int i = 0; i < strs.length; i++) {
            if (strs[i].equals("#")) {
                strs[i] = ",";
            }
            terminalMap.put(strs[i], i);
        }
        try {
            Scanner s = new Scanner(json);
            int row = 0;
            while (s.hasNext()) {
                String line = s.nextLine();
                String[] strings = line.split(",");
                for (int i = 0; i < 27; i++) {
                    pred_table[row][i] = Integer.parseInt(strings[i]);
                }
                row++;
            }
            s = new Scanner(prod);
            int index = 1;
            while (s.hasNext()) {
                String line = s.nextLine();
                productions[index] = line;
                index++;
            }
            s = new Scanner(non_terminals);
            index = 1;
            while (s.hasNext()) {
                String line = s.nextLine();
                nonTerminalMap.put(line, index);
                index++;
            }
        } catch (Exception ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<String> lineParse(String line) {
        int error = 0;
        ArrayList<String> errorList = new ArrayList<>();
        inputStack.push("$");
        parseStack.push("$");
        parseStack.push("Program");

        String[] inputStrArray = line.split(" ");
        for (int i = inputStrArray.length - 1; i >= 0; i--) {
            inputStack.push(inputStrArray[i]);
        }
        while (!inputStack.empty()) {
            parseStackPeek = (String) parseStack.peek();
            inputStackPeek = (String) inputStack.peek();
            System.out.print(parseStackPeek +  "  --  ");
            System.out.println(inputStackPeek);
            if (parseStackPeek.equals("EPSILON")) {
                parseStack.pop();
                continue;
            } else if (inputStackPeek.equals(parseStackPeek)) {
                inputStack.pop();
                parseStack.pop();
                System.out.println("POOOOOOOOOOOOOOOOOOP");
            } else {
                try {
                    nonTerminalIndex = (int) nonTerminalMap.get(toAlpha(parseStackPeek));
                } catch (Exception ex) {
                    error++;
                    parseStack.pop();
                    errorList.add("miss " + parseStackPeek);
                    continue;
                }
                try {
                    String dd = toAlpha(inputStackPeek);
                    if(!dd.equals(inputStackPeek))
                        dd = inputStackPeek;
                    terminalIndex = (int) terminalMap.get(dd);
                } catch (Exception ex) {
                    error++;
                    inputStack.pop();
                    errorList.add("miss " + inputStackPeek);
                    continue;
                }
                if (pred_table[nonTerminalIndex][terminalIndex] == 59) {
                    error++;
                    inputStack.pop();
                    errorList.add("skip " + inputStackPeek);
                    continue;
                } else if (pred_table[nonTerminalIndex][terminalIndex] == 58) {
                    parseStack.pop();
                    continue;
                } else {
                    String prod = productions[pred_table[nonTerminalIndex][terminalIndex]];
                    String[] str = prod.split(" ");
                    parseStack.pop();
                    for (int i = str.length - 1; i >= 0; i--) {
                        parseStack.push(str[i]);
                    }
                }
            }

        }
        return errorList;
    }
    private String toAlpha(String s) {
        return s.replaceAll("[^a-zA-Z']", "");
    }
}
