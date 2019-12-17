public class CsvCreator {
    String nomCommune;
    float score;

    public CsvCreator(String nomCommune, float score) {
        this.nomCommune = nomCommune;
        this.score = score;
    }

    @Override
    public String toString() {
        return nomCommune +";"+ score;
    }
}