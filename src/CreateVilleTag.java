    import java.io.*;
    import java.util.HashSet;
    import java.util.Set;

    import static jdk.nashorn.internal.objects.NativeString.substring;

    public class CreateVilleTag {

        public void readFile(File data) throws IOException {
            float score;
            int adjIndex;
            CsvCreator csvCreator;
            String dataLine;
            File adjFile = new File("src/res/adjFile.txt");
            Set<String> adjFileSet = getWords(adjFile);
            File pronomsALM = new File("src/res/pronomsALM.txt");
            Set<String> pronomsALMSet = getWords(pronomsALM);
            File pronomsENG = new File("src/res/pronomsENG.txt");
            Set<String> pronomsENGSet = getWords(pronomsENG);
            File pronomsESP = new File("src/res/pronomsESP.txt");
            Set<String> pronomsESPSet = getWords(pronomsESP);
            File pronomsFR = new File("src/res/pronomsFR.txt");
            Set<String> pronomsFRSet = getWords(pronomsFR);
            File pronomsITA = new File("src/res/pronomsITA.txt");
            Set<String> pronomsITASet = getWords(pronomsITA);
            File préfixesOc = new File("src/res/préfixesOc.txt");
            Set<String> préfixesOcSet = getWords(préfixesOc);
            File préfixesOil = new File("src/res/préfixesOil.txt");
            Set<String> préfixesOilSet = getWords(préfixesOil);
            File suffixesOc = new File("src/res/suffixesOc.txt");
            Set<String> suffixesOcSet = getWords(suffixesOc);
            File suffixesOil = new File("src/res/suffixesOil.txt");
            Set<String> suffixesOilSet = getWords(suffixesOil);
            try (FileInputStream fsData = new FileInputStream(data)) {
                try (BufferedReader brData = new BufferedReader(new InputStreamReader(fsData))) {
                    try (PrintWriter bW = new PrintWriter(new FileWriter("src/res/resultat.csv", false))) {
                        while ((dataLine = brData.readLine()) != null) {
                            dataLine = dataLine.toLowerCase();
                            score = 0;

                            // Contient un pronom allemand
                            score += containsPronom(dataLine, pronomsALMSet, (float) -0.40);
                            // Contient un pronom anglais
                            score += containsPronom(dataLine, pronomsENGSet, (float) -0.20);
                            // Contient un pronom espagnol
                            score += containsPronom(dataLine, pronomsESPSet, (float) -0.20);
                            // Contient un pronom français
                            score += containsPronom(dataLine, pronomsFRSet, (float) -0.20);
                            // Contient un pronom italien
                            score += containsPronom(dataLine, pronomsITASet, (float) -0.20);

                            // Contient un adjectif au début
                            score += containsWordAtBeginning(dataLine, adjFileSet, (float) 0.40, true);

                            // Contient un adjectif à la fin
                            score += containsWordAtBeginning(dataLine, adjFileSet, (float) -0.40, false);

                            // Contient un préfixe OC
                            score += containsWordAtBeginning(dataLine, préfixesOcSet, (float) 0.40, true);

                            // Contient un préfixe OIL
                            score += containsWordAtBeginning(dataLine, préfixesOilSet, (float) -0.40, true);

                            // Contient un suffixe OC
                            score += containsWordAtBeginning(dataLine, suffixesOcSet, (float) 0.40, false);

                            // Contient un suffixe OIL
                            score += containsWordAtBeginning(dataLine, suffixesOilSet, (float) -0.40, false);

                            // on écrit
                            csvCreator = new CsvCreator(dataLine, score);
                            bW.println(csvCreator.toString());
                        }
                        bW.flush();
                    }
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }

        }

        private float containsPronom(String currentLine, Set<String> pronomFile, float scoreIfContained) throws IOException {
            for (String pronomLine : pronomFile) {
                pronomLine = pronomLine.toLowerCase();
                if (currentLine.contains(pronomLine)) {
                    return scoreIfContained;
                }
            }
            return 0;
        }

            private float containsWordAtBeginning (String currentLine, Set < String > wordFile,float scoreIfFound,
            boolean findAtBeginning){
                int adjIndex;
                for (String wordLine : wordFile) {
                    wordLine = wordLine.toLowerCase();
                    // Adjectif avant ou après le nom ?
                    if (currentLine.contains(wordLine)) {
                        adjIndex = currentLine.indexOf(wordLine);
                        if (adjIndex == 0) {
                            if (findAtBeginning) {
                                return scoreIfFound;
                            } else {
                                return 0;
                            }
                        } else if (wordLine.length() == substring(currentLine, adjIndex).length()) {
                            if (!findAtBeginning) {
                                return scoreIfFound;
                            } else {
                                return 0;
                            }
                        }
                    }
                }
                return 0;
            }

            public static void main (String[]args) throws IOException {
                CreateVilleTag createVilleTag = new CreateVilleTag();
                createVilleTag.readFile(new File("src/res/sourceTest.txt"));
            }

            public Set<String> getWords (File file) throws IOException {
                System.out.println("Reading file :" + file.getName());
                String currentLine;
                Set<String> result = new HashSet<>();
                try (FileInputStream truc = new FileInputStream(file)) {
                    try (BufferedReader truc2 = new BufferedReader(new InputStreamReader(truc))) {
                        while ((currentLine = truc2.readLine()) != null) {
                            result.add(currentLine);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return result;
            }
        }