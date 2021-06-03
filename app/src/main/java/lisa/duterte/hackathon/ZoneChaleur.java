package lisa.duterte.hackathon;

public class ZoneChaleur {

    String date ;
    String heure ;
    String position ;

    public ZoneChaleur() {
    }

    public ZoneChaleur(String date, String heure, String position) {
        this.date = date;
        this.heure = heure;
        this.position = position;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
