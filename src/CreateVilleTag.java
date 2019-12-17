import java.io.*;

import static jdk.nashorn.internal.objects.NativeString.substring;

public class CreateVilleTag {

    public void readFile(File data) throws FileNotFoundException, UnsupportedEncodingException {
       float score;
        int adjIndex;
        CsvCreator csvCreator;
        String dataLine;
        File adjFile = new File("src/res/adjFile.txt");
        File pronomsALM = new File("src/res/pronomsALM.txt");
        File pronomsENG = new File("src/res/pronomsENG.txt");
        File pronomsESP = new File("src/res/pronomsESP.txt");
        File pronomsFR = new File("src/res/pronomsFR.txt");
        File pronomsITA = new File("src/res/pronomsITA.txt");
        try(FileInputStream fsData = new FileInputStream(data)) {
            try(BufferedReader brData = new BufferedReader(new InputStreamReader(fsData))) {
                String adjLine;
                try (PrintWriter bW = new PrintWriter (new FileWriter("src/res/resultat.csv", false))) {
                    while ((dataLine = brData.readLine()) != null) {
                        dataLine= dataLine.toLowerCase();
                        score = 0;
                        try(FileInputStream fsAdj= new FileInputStream(adjFile)) {
                            try( BufferedReader brAdj = new BufferedReader(new InputStreamReader(fsAdj))) {
                                while((adjLine = brAdj.readLine()) != null){
                                    adjLine = adjLine.toLowerCase();
                                    // Adjectif avant ou après le nom ?
                                    if(dataLine.contains(adjLine)){
                                        adjIndex = dataLine.indexOf(adjLine);
                                        if(adjIndex == 0){
                                            score -= 0.25;
                                            break;
                                        } else if(adjLine.length() == substring(dataLine,adjIndex).length()) {
                                            score += 0.25;
                                            break;
                                        }
                                    }
                                }
                                brAdj.close();
                                fsAdj.close();
                            }
                        }
                        // Contient un pronom allemand
                        score += containsPronom(dataLine, pronomsALM, (float) -0.30);
                        // Contient un pronom anglais
                        score += containsPronom(dataLine, pronomsENG, (float) -0.10);
                        // Contient un pronom espagnol
                        score += containsPronom(dataLine, pronomsESP, (float) -0.10);
                        // Contient un pronom français
                        score += containsPronom(dataLine, pronomsFR, (float) -0.10);
                        // Contient un pronom italien
                        score += containsPronom(dataLine, pronomsITA, (float) -0.10);

                        // on écrit
                        csvCreator = new CsvCreator(dataLine,score);
                        bW.println(csvCreator.toString());
                    }
                    brData.close();
                    fsData.close();
                    bW.flush();
                }
            }
        }catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

    }

    private float containsPronom(String currentLine, File pronomFile, float scoreIfContained) throws IOException {
        String pronomLine;
        try(FileInputStream fsAdj= new FileInputStream(pronomFile)) {
            try( BufferedReader brAdj = new BufferedReader(new InputStreamReader(fsAdj))) {
                while((pronomLine = brAdj.readLine()) != null){
                    pronomLine = pronomLine.toLowerCase();

                    if(currentLine.contains(pronomLine)){
                       return scoreIfContained;
                    }
                }
                brAdj.close();
                fsAdj.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        CreateVilleTag createVilleTag = new CreateVilleTag();
        createVilleTag.readFile(new File("src/res/sourceTest.txt"));
    }
}
