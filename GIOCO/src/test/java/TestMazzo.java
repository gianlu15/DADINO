import GestioneCarte.Mazzo;

public class TestMazzo {

    public static void main(String[] args){

        Mazzo maz = new Mazzo();

        maz.inizializzaMazzo();

        System.out.println("carte nel mazzo " + maz.getNumeroCarte());

        maz.mostraMazzo();

        maz.mescolaMazzo();

        maz.mostraMazzo();
    }
    
}
