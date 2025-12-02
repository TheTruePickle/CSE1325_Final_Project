package CSE1325_Final_Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

//uses swing and keylisteners to present and handle input
public class GameGUI extends JFrame {

    private GameBoard board;
    private BoardPanel panel;

    public GameGUI() {
        board = new GameBoard();
        panel = new BoardPanel();
        JLabel score = new JLabel("Score: 0");
        
        setTitle("2048 Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 625);
        setLocationRelativeTo(null);
        setResizable(false);
        //Score stuff
        score.setVerticalAlignment(JLabel.BOTTOM);
        score.setFont(new Font("Arial", Font.PLAIN, 25));
        add(score, BorderLayout.NORTH);
        
        add(panel, BorderLayout.CENTER);


        //keylistener for wasd input
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Direction d = null;

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W: //for wasd
                    case KeyEvent.VK_UP: //for arrow keys
                        d = Direction.UP; break;

                    case KeyEvent.VK_S:
                    case KeyEvent.VK_DOWN:
                        d = Direction.DOWN; break;

                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        d = Direction.LEFT; break;

                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        d = Direction.RIGHT; break;
                    
                    case KeyEvent.VK_ENTER:
                        if(board.getEmptyTiles().size() > 3)
                           d = (new AiNode(board, 100, 3)).getDirection();
                        else
                           d = (new AiNode(board, 100, 4)).getDirection();
                }
                //if board has no moves left will end
                if (d != null) {
                    board.move(d);
                    panel.repaint();
                    //Show Score
                    score.setText("Score: " + board.getScore());
                    
                    if (!board.hasMoves()) {
                        JOptionPane.showMessageDialog(
                                GameGUI.this,
                                "Game Over!",
                                "2048",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                }
            }
        });
    }

    //custom panel draws the board
    private class BoardPanel extends JPanel {

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            int[][] b = board.getBoard();
            int tileSize = 100;
            int padding = 20;

            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++) {

                    int value = b[row][col];

                    //tile position
                    int x = padding + col * (tileSize + padding);
                    int y = padding + row * (tileSize + padding);

                    //tile background color
                    g.setColor(getTileColor(value));
                    g.fillRoundRect(x, y, tileSize, tileSize, 20, 20);

                    //draw tile
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Arial", Font.BOLD, 30));
                    
                    //draw tile number
                    if (value != 0) {
                        String text = String.valueOf(value);
                        FontMetrics fm = g.getFontMetrics();

                        int textX = x + (tileSize - fm.stringWidth(text)) / 2;
                        int textY = y + (tileSize + fm.getAscent()) / 2 - 10;

                        g.drawString(text, textX, textY);
                    }
                }
            }
        }

        //Colors for tile codes taken from 2048
        
        private Color getTileColor(int value) {
            switch (value) {
                case 0: return new Color(0xCDC1B4);
                case 2: return new Color(0xEEE4DA);
                case 4: return new Color(0xEDE0C8);
                case 8: return new Color(0xF2B179);
                case 16: return new Color(0xF59563);
                case 32: return new Color(0xF67C5F);
                case 64: return new Color(0xF65E3B);
                case 128: return new Color(0xEDCF72);
                case 256: return new Color(0xEDCC61);
                case 512: return new Color(0xEDC850);
                case 1024: return new Color(0xEDC53F);
                case 2048: return new Color(0xEDC22E);
                default: return Color.BLACK;
            }
        }
    }
    //main method
    public static void main(String[] args) {
    	
    new GameGUI().setVisible(true);
       
    }
}
