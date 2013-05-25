package br.com.tbp.module.pnl.preprocessor;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class StopWordManager {


    public Set<String> loadStopWords() throws FileNotFoundException {
        Set<String> stopWords = new HashSet<String>();
        Scanner scanner = new Scanner(new File("src/main/resources/stop_words_portugues.txt".replace("/", File.separator)));
        String string = null;
        while (scanner.hasNext()) {
            string = scanner.nextLine();
            if (string != null) {
                string = string.trim();
                if (string.length() > 0) {
                    stopWords.add(string.trim());
                }
            }
        }
        return stopWords;
    }


}
