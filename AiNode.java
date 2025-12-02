package CSE1325_Final_Project;

import java.util.*;

public class AiNode {

    // This GameBoard
    private GameBoard thisBoard;

    // Chance is important
    private double chance;

    // Height, how high are we
    private int height;

    // Weighted Score
    private double weightedScore;

    // All possible game states from here
    private ArrayList<AiNode> right;
    private ArrayList<AiNode> left;
    private ArrayList<AiNode> up;
    private ArrayList<AiNode> down;

    public AiNode(GameBoard s, double c, int h) {
        right = new ArrayList<>();
        left = new ArrayList<>();
        up = new ArrayList<>();
        down = new ArrayList<>();

        thisBoard = s.clone();
        chance = c;
        height = h;

        ArrayList<int[]> emptyTiles = thisBoard.getEmptyTiles();

        if (emptyTiles.size() == 0) {
            weightedScore = 0;
        } else {
            weightedScore = c * thisBoard.getScore() * (emptyTiles.size() / 3);
        }

        if (height > 0) {
            getAiNodes(Direction.LEFT, left);
            getAiNodes(Direction.RIGHT, right);
            getAiNodes(Direction.UP, up);
            getAiNodes(Direction.DOWN, down);
        }
    }

    // getBoard
    private GameBoard getBoard() {
        return thisBoard;
    }

    private ArrayList<AiNode> getDirectionList(Direction dir) {
        switch (dir) {
            case LEFT:
                return left;
            case RIGHT:
                return right;
            case UP:
                return up;
            case DOWN:
                return down;
        }
        return null;
    }

    // getChance
    private double getChance() {
        return chance;
    }

    // getWeightedScore
    private double getWeightedScore() {
        return weightedScore;
    }

    // get all possible combinations of things from this position, in one direction
    private void getAiNodes(Direction dir, ArrayList<AiNode> d) {
        GameBoard base = thisBoard.clone();
        boolean moved = base.moveWithoutSpawn(dir);

        ArrayList<int[]> emptyTiles = base.getEmptyTiles();

        for (int i = 0; i < emptyTiles.size(); i++) {

            // Select the Tile
            int[] selectedTile = emptyTiles.get(i);

            // Get the newBoards, for cases 2 and 4
            GameBoard newBoard = base.clone();
            GameBoard newBoardBut4 = base.clone();

            // Set them on the new tile
            newBoard.getBoard()[selectedTile[0]][selectedTile[1]] = 2;
            newBoardBut4.getBoard()[selectedTile[0]][selectedTile[1]] = 4;

            if (moved) {
                double chance2 = (chance / emptyTiles.size()) * .9;
                double chance4 = (chance / emptyTiles.size()) * .1;

                d.add(new AiNode(newBoard, chance2, height - 1));
                if (height > 1) {
                    d.add(new AiNode(newBoardBut4, chance4, height - 1));
                }
            }
        }
    }

    public String toString() {
        String ans =
            "Chance: " + chance +
            ", Score: " + thisBoard.getScore() +
            ", Weighted Score (Chance * Score): " +
            (chance * thisBoard.getScore()) + "\n";

        ans += thisBoard.toString() + "\n";
        return ans;
    }

    public double findMaxScore() {
        if (height > 0) {
            double upDouble = weightArrayListScore(Direction.UP, up);
            double downDouble = weightArrayListScore(Direction.DOWN, down);
            double rightDouble = weightArrayListScore(Direction.RIGHT, right);
            double leftDouble = weightArrayListScore(Direction.LEFT, left);

            return Math.max(Math.max(upDouble, downDouble), Math.max(leftDouble, rightDouble));
        } else {
            return weightedScore;
        }
    }

    public double weightArrayListScore(Direction dir, ArrayList<AiNode> d) {
        double total = 0.0;
        for (int i = 0; i < d.size(); i++) {
            total += d.get(i).findMaxScore();
        }
        return total;
    }

    public Direction getDirection() {
        double leftScore = weightArrayListScore(Direction.LEFT, getDirectionList(Direction.LEFT));
        double rightScore = weightArrayListScore(Direction.RIGHT, getDirectionList(Direction.RIGHT));
        double upScore = weightArrayListScore(Direction.UP, getDirectionList(Direction.UP));
        double downScore = weightArrayListScore(Direction.DOWN, getDirectionList(Direction.DOWN));

        double max = leftScore;
        Direction maxDir = Direction.LEFT;

        if (rightScore > max) { max = rightScore; maxDir = Direction.RIGHT; }
        if (upScore > max)    { max = upScore;    maxDir = Direction.UP; }
        if (downScore > max)  { max = downScore;  maxDir = Direction.DOWN; }
        if(thisBoard.clone().move(maxDir) == false) {maxDir = Direction.UP;}
        if(thisBoard.clone().move(maxDir) == false) {maxDir = Direction.RIGHT;} 
        return maxDir;
    }
}
