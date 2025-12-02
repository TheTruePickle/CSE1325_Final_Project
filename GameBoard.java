package CSE1325_Final_Project;

import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;

public class GameBoard {
	private int[][] board;
	private Random r;
   private int score;
   //initialize game board
	public GameBoard() {
		this.board = new int[4][4];
		this.r = new Random();
        spawnTile();
        spawnTile();
	}
   //Initialize but without random or spawning tiles to save time
   public GameBoard(boolean f)
   {
      this.board = new int[4][4];
      r = null;
   }
   //getters and setters
   //---------------------
   //No protections set, shoudn't matter thougj
   //Set Board and setScore, score is metric used by AI to determine decisions
   public void setBoard(int[][] b)
   {
      board = b;
   }
   public void setScore(int s)
   {
      score = s;
   }
	//method to get board in GUI
   public int[][] getBoard() {
	    return board;	
   }
   //get score
   public int getScore()
   {
        return score;
   }
   //Helper Functions
   //----------------
   //Cloning board (It returns and instance, not a pointer to this instance)
   public GameBoard clone()
   {
      GameBoard c = new GameBoard();
      int[][] og = getBoard();
      int[][] copy = new int[og.length][];
      for(int i = 0; i < og.length; i ++)
      {
         copy[i] = og[i].clone();
      }
      c.setBoard(copy);
      c.setScore(getScore());
      return c;
   }
   //Getting Empty Tiels
	public ArrayList<int[]> getEmptyTiles()
   {
      ArrayList<int[]> emptyTiles = new ArrayList<>();
		//add positions of empty tiles
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				if(board[i][j] == 0)
				{
					emptyTiles.add(new int[] {i,j});
				}
			}
		}
      return emptyTiles;
   }
   //In Game Functions
   //-----------------
	//spawn tile after move
	public void spawnTile() {
		//initialize list
		ArrayList<int[]> emptyTiles = getEmptyTiles();
		//if board is full return
		if(emptyTiles.isEmpty()) {
			return;
		}
		//choose a random empty tile
		int[] newTilePos = emptyTiles.get(r.nextInt(emptyTiles.size()));
		
		//pick whether new tile is 2 or 4
		double spawnChance = Math.random();
		if (spawnChance < .9)
		{
			board[newTilePos[0]][newTilePos[1]] = 2;
		}
		else
		{
			board[newTilePos[0]][newTilePos[1]] = 4;
		}
	}

	//What happens when a move is done based on input direction
    public boolean move(Direction dir) {
        boolean moved = false;
        //process what happens in each row/col one at a time
        for (int i = 0; i < 4; i++) {
            int[] line = getLine(i, dir);
            int[] merged = mergeLine(moveLine(line));
            //keeps track if any of the lines have changed/moved
            moved |= !Arrays.equals(line, merged);
            setLine(i, merged, dir);
        }
        //if something did move we need to spawn a new tile
        if (moved) spawnTile();
        return moved;
    }
    //Moving without spawning, for AI purposes.
    public boolean moveWithoutSpawn(Direction dir)
    {
      boolean moved = false;
      for (int i = 0; i < 4; i++) {
            int[] line = getLine(i, dir);
            int[] merged = mergeLine(moveLine(line));
            //keeps track if any of the lines have changed/moved
            moved |= !Arrays.equals(line, merged);
            setLine(i, merged, dir);
        }
      return moved;
    }
    
    
    //return all values in row/col based on direction
    private int[] getLine(int index, Direction dir) {
        int[] line = new int[4];
        for (int i = 0; i < 4; i++) {
            switch (dir) {
                case LEFT: line[i] = board[index][i]; break;
                case RIGHT: line[3 - i] = board[index][i]; break;
                case UP: line[i] = board[i][index]; break;
                case DOWN: line[3 - i] = board[i][index]; break;
            }
        }
        return line;
    }

    //Changes values of rows/cols  based on direction
    private void setLine(int index, int[] line, Direction dir) {
        for (int i = 0; i < 4; i++) {
            switch (dir) {
                case LEFT: board[index][i] = line[i]; break;
                case RIGHT: board[index][i] = line[3 - i]; break;
                case UP: board[i][index] = line[i]; break;
                case DOWN: board[i][index] = line[3 - i]; break;
            }
        }
    }

    //move all non-zero numbers to the left
    private int[] moveLine(int[] line) {
        int[] newLine = new int[4];
        int idx = 0;
        for (int value : line) {
            if (value != 0) newLine[idx++] = value;
        }
        return newLine;
    }
    
    //merge adjacent tiles after the moveLine
    private int[] mergeLine(int[] line) {
        for (int i = 0; i < 3; i++) {
            if (line[i] != 0 && line[i] == line[i + 1]) {
                line[i] *= 2;
                score += line[i];
                line[i + 1] = 0;
            }
        }
        return moveLine(line);
    }

    //checks if user can still move based on any empty spaces or if a merge can still be done
    public boolean hasMoves() {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (board[i][j] == 0)
                    return true;

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j] == board[i][j + 1] || board[j][i] == board[j + 1][i])
                    return true;

        return false;
    }

    //print game board into console
    public String toString(){
        String s = "";
        for (int[] row : board) {
            for (int cell : row)
                s += String.format("%4d", cell);
            s += "\n";
        }
        return s;
    }
    //For Memoization purposes
    public String toCompactString()
    {
      String s ="";
      for(int[] row : board)
      {
         for(int cell : row)
         {
            s+= Integer.toString(cell);
         }
      }
      return s;
    }
}

