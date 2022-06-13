import java.util.*;

public class TerrainMap {
    final static int NUM_ARGS = 3;
    final static String[] terTypes = {"Brownian = 0"};

    // TODO: code to restart prompt
    // TODO: throw exception with message?
    // TODO: prompt to ask to restart?
    public static void restart(int type, String msg) throws Exception {
        // type == 0: restart from invalid args[]
        // throw an exception dependent on type
        if (type == 0) throw new Exception(msg);
    }

    // remember to add stdIn and user input
    public static void main(String[] args) throws Exception {
        int terType = -1;
        int lenX = -1;
        int lenY = -1;
        
        int len = args.length;
        if (len == 0) {
            Scanner sc = new Scanner(System.in);
            String arg = "First";
            while (true) {
                try {
                    if (arg.equals("First")) {
                        System.out.print("Enter map type (Brownian = 0): ");
                        terType = sc.nextInt();
                        if (terType <= 0) throw new NumberFormatException();
                        arg = "Second";
                        System.out.println("Enter map dimensions");
                    }
                    if (arg.equals("Second")) {
                        System.out.print("Width: ");
                        lenX = sc.nextInt();
                        if (lenX <= 0) throw new NumberFormatException();
                        arg = "Third";
                    }
                    else {
                        System.out.print("Length: ");
                        lenY = sc.nextInt();
                        if (lenY <= 0) throw new NumberFormatException();
                        break;
                    }
                }
                catch (Exception e) {
                    System.out.println(arg + " input invalid, try again");
                    sc.nextLine();
                }
            }
            sc.close();
        }
        else if (len == NUM_ARGS || len == 0) {
            String arg = "first";
            try {
                terType = Integer.parseInt(args[0]);
                if (terType <= 0) {
                    restart(0, "Invalid: first input must be a positive integer");
                }
                arg = "second";
                lenX = Integer.parseInt(args[1]);
                if (lenX <= 0) {
                    restart(0, "Invalid: second input must be a positive integer");
                }
                arg = "third";
                lenY = Integer.parseInt(args[2]);
                if (lenY <= 0) {
                    restart(0, "Invalid: third input must be a positive integer");
                }
            }
            catch (NumberFormatException e) {
                restart(0, "Invalid: " + arg + " input must be a positive integer");
            }
        }
        else {
            restart(0, "Number of arguments is not equal to " + NUM_ARGS);
        }

        Point[][] arr = new Point[lenY][lenX];
    }
}