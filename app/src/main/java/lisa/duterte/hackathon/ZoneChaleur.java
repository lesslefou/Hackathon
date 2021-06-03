package lisa.duterte.hackathon;

public class ZoneChaleur {

    String dateheure ;
    String position ;
    Long temperature;

    public ZoneChaleur() {
    }

    public ZoneChaleur(String dateheure, String position,Long temperature) {
        this.dateheure = dateheure;
        this.position = position;
        this.temperature = temperature;
    }

    public Long getTemperature() {
        return temperature;
    }

    public void setTemperature(Long temperature) {
        this.temperature = temperature;
    }

    public String getDateHeure() {
        return dateheure;
    }

    public void setDateHeure(String date) {
        this.dateheure = date;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
