import java.util.Scanner;

/**
 * This class is responsible for controlling the Treasure Hunter game.<p>
 * It handles the display of the menu and the processing of the player's choices.<p>
 * It handles all the display based on the messages it receives from the Town object. <p>
 *
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class TreasureHunter {
    // static variables
    private static final Scanner SCANNER = new Scanner(System.in);

    // instance variables
    private Town currentTown;
    private Hunter hunter;
    private boolean hardMode;
    private boolean easyMode;
    private boolean normalMode;
    private String treasure;
    private String[] treasureFound;
    private boolean searched;
    int index = 0;

    public boolean getEasyMode() {
        return easyMode;
    }
    public boolean getHardMode() {
        return hardMode;
    }
    public boolean getNormalMode() {
        return normalMode;
    }


    /**
     * Constructs the Treasure Hunter game.
     */
    public TreasureHunter() {
        // these will be initialized in the play method
        currentTown = null;
        hunter = null;
        hardMode = false;
        easyMode = false;
        normalMode = false;
    }

    /**
     * Starts the game; this is the only public method
     */
    public void play() {
        welcomePlayer();
        enterTown();
        showMenu();
    }

    /**
     * Creates a hunter object at the beginning of the game and populates the class member variable with it.
     */
    private void welcomePlayer() {
        System.out.println("Welcome to " + Colors.CYAN + "TREASURE HUNTER" + Colors.RESET + "!");
        System.out.println("Going hunting for the big treasure, eh?");
        System.out.print("What's your name, Hunter? ");
        String name = SCANNER.nextLine().toLowerCase();

        // set hunter instance variable
        hunter = new Hunter(name, 20);

        System.out.print("Difficulty ([e]asy, [n]ormal, [h]ard : ");
        String hard = SCANNER.nextLine().toLowerCase();
        if (hard.equals("h")) {
            hardMode = true;
            hunter.changeGold(80);
            String items[] = {"Water", "Rope", "Machete", "Horse", "Boat", "Boot"};
            for (String itm : items) {
                hunter.addItem(itm);
            }
            hunter.getInventory();
        }
        else if (hard.equals("e")) {
            easyMode = true;
            hunter.changeGold(20);
        }
        else if (hard.equals("n")) {
            normalMode = true;
            hunter.changeGold(0);
        }
    }

    /**
     * Creates a new town and adds the Hunter to it.
     */
    private void enterTown() {
        String treasures[] = {"crown", "trophy", "gem", "dust"};
        int idx = (int)(Math.random() * 3);
        treasure = treasures[idx];
        double markdown = 0.25;
        double toughness = 0.4;
        if (hardMode) {
            // in hard mode, you get less money back when you sell items
            markdown = 0.25;

            // and the town is "tougher"
            toughness = 0.75;
        }
        if (easyMode) {
            // in hard mode, you get less money back when you sell items
            markdown = 0;

            // and the town is "tougher"
            toughness = 0.3;
        }
        if (normalMode) {
            // in hard mode, you get less money back when you sell items
            markdown = .5;

            // and the town is "tougher"
            toughness = .4;
        }

        // note that we don't need to access the Shop object
        // outside of this method, so it isn't necessary to store it as an instance
        // variable; we can leave it as a local variable
        Shop shop = new Shop(markdown);

        // creating the new Town -- which we need to store as an instance
        // variable in this class, since we need to access the Town
        // object in other methods of this class
        currentTown = new Town(shop, toughness, this);

        // calling the hunterArrives method, which takes the Hunter
        // as a parameter; note this also could have been done in the
        // constructor for Town, but this illustrates another way to associate
        // an object with an object of a different class
        currentTown.hunterArrives(hunter);
    }

    public String searchForTreasure() {
        treasureFound[index] = treasure;
        index++;
        return "You found a " + treasure;
    }
    /**
     * Displays the menu and receives the choice from the user.<p>
     * The choice is sent to the processChoice() method for parsing.<p>
     * This method will loop until the user chooses to exit.
     */
    private void showMenu() {
        String choice = "";
        while (!choice.equals("x")) {
            if (hunter.gameOver()) {
                System.out.println("Game Over!");
                choice = "x";
                processChoice(choice);
            } else {
                System.out.println();
                System.out.println(currentTown.getLatestNews());
                System.out.println("***");
                System.out.println(hunter.infoString());
                System.out.println(currentTown.infoString());
                System.out.println("(B)uy something at the shop.");
                System.out.println("(S)ell something at the shop.");
                System.out.println("(E)xplore surrounding terrain.");
                System.out.println("(M)ove on to a different town.");
                System.out.println("(L)ook for trouble!");
                System.out.println("(H)unt for treasure");
                System.out.println("Give up the hunt and e(X)it.");
                System.out.println();
                System.out.print("What's your next move? ");
                choice = SCANNER.nextLine().toLowerCase();
                processChoice(choice);
            }
        }
    }

    /**
     * Takes the choice received from the menu and calls the appropriate method to carry out the instructions.
     * @param choice The action to process.
     */
    private void processChoice(String choice) {
        if (choice.equals("b") || choice.equals("s")) {
            currentTown.enterShop(choice);
        } else if (choice.equals("e")) {
            System.out.println(currentTown.getTerrain().infoString());
        } else if (choice.equals("m")) {
            if (currentTown.leaveTown()) {
                // This town is going away so print its news ahead of time.
                System.out.println(currentTown.getLatestNews());
                enterTown();
                searched = false;
            }
        } else if (choice.equals("l")) {
            currentTown.lookForTrouble();
        } else if (choice.equals("h")) {
            if (searched) {
                System.out.println("You have already searched this town!");
            } else {
                searchForTreasure();
                searched = true;
            }
        } else if (choice.equals("x")) {
            System.out.println("Fare thee well, " + hunter.getHunterName() + "!");
        } else {
            System.out.println("Yikes! That's an invalid option! Try again.");
        }
    }
}
//