public class CsvCreator {
    String nomCommune;
    float score;

    public CsvCreator(String nomCommune, float score) {
        this.nomCommune = nomCommune;
        if (score < -1) {
            this.score = -1;
        }
        else if (score > 1) {
            this.score = 1;
        }
        else {
            this.score = score;
        }
    }

    @Override
    public String toString() {
        return nomCommune +";"+ score;
    }
}