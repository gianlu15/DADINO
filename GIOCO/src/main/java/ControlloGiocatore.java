import java.util.Scanner;

public class ControlloGiocatore {

    public static boolean decisone() {

        boolean d = true;
        System.out.println("Scegli cosa fare: ");
        System.out.println("Inserisci 'p' per pescare e 'f' per fermarti");

        Scanner tastiera = new Scanner(System.in);

        String operazione = tastiera.next();

        switch (operazione) {
            case "p":
                d = true;
                break;

            case "f":
                d = false;
                break;

            default:
            System.out.println("Operazione non riconsciuta");
                break;
        }
        return d;
    }
}
