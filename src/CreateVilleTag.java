import java.io.*;

import static jdk.nashorn.internal.objects.NativeString.substring;

public class CreateVilleTag {

    public void readFile(File data, File adjFile) throws FileNotFoundException, UnsupportedEncodingException {
       String cluster;
        int adjIndex;
        CsvCreator csvCreator;
        String dataLine;
        try(FileInputStream fsData = new FileInputStream(data)) {
            try(BufferedReader brData = new BufferedReader(new InputStreamReader(fsData))) {
                String adjLine;
                try (PrintWriter bW = new PrintWriter (new FileWriter("/res/resultat.csv", false))) {
                    while ((dataLine = brData.readLine()) != null) {
                        dataLine= dataLine.toLowerCase();
                        try(FileInputStream fsAdj= new FileInputStream(adjFile)) {
                            try( BufferedReader brAdj = new BufferedReader(new InputStreamReader(fsAdj))) {
                                while((adjLine = brAdj.readLine()) != null){
                                    adjLine = adjLine.toLowerCase();
                                    if(dataLine.contains(adjLine)){
                                        adjIndex = dataLine.indexOf(adjLine);
                                        if(adjIndex == 0){
                                            cluster = "Oil";
                                            csvCreator = new CsvCreator(dataLine,cluster);
                                            bW.println(csvCreator.toString());
                                            break;
                                        } else if(adjLine.length() == substring(dataLine,adjIndex).length()) {
                                            cluster = "Oc";
                                            csvCreator = new CsvCreator(dataLine,cluster);
                                            bW.println(csvCreator.toString());
                                            break;
                                        }
                                    }
                                }
                                brAdj.close();
                                fsAdj.close();
                            }
                        }
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

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        CreateVilleTag createVilleTag = new CreateVilleTag();
        createVilleTag.readFile(new File("C:\\bureau\\MIAGE2_2019_2020\\PRM2\\TD\\source\\sourceTest.txt"),new File("C:\\bureau\\MIAGE2_2019_2020\\PRM2\\TD\\source\\adjFile.txt"));
    }
}
