package CSE1325_Final_Project;
import java.util.Scanner;
import java.util.Random;
public class Game {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        GameBoard game = new GameBoard();

        while (true) {
            game.print();
            System.out.print("Move (WASD): ");
            //get user input
            String input = sc.nextLine().toUpperCase();
            Direction d;
            switch (input) {
                case "W":
                	d = Direction.UP;
                	break;
                case "S":
                	d = Direction.DOWN;
                	break;
                case "A":
                	d = Direction.LEFT;
                	break;
                case "D": 
                	d = Direction.RIGHT;
                	break;
                default: 
                	d = null;
                	break;
            };

            if (d == null) continue;
            //move the board
            game.move(d);
            if (!game.hasMoves()) {
                game.print();
                System.out.println("Game Over!");
                break;
            }
        }
    }
}
