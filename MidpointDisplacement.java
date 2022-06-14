import java.util.Scanner;

public class MidpointDisplacement {
    // Deviation: used in noise generation and randomness
    private int dev;

    public void setMatrix(Point[][] mat) {
        return;
    }

    // Constructor
    public MidpointDisplacement() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Enter deviation (1 to 10): ");
                dev = sc.nextInt();
                if (dev <= 0) throw new Exception();
                break;
            }
            catch (Exception e) {
                System.out.println("Invalid input, try again");
                sc.nextLine();
            }
        }
        sc.close();
    }
}