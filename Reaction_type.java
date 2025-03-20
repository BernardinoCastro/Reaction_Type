import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Reaction_type extends JFrame {

    private final BufferedImage backgroundImage;
    private final JTextField inputField;
    private final JLabel timerLabel;
    private final JLabel scoreLabel;
    private final JLabel livesLabel;
    private final List<FallingWord> fallingWords = new ArrayList<>();
    private final Random random = new Random();

    private int correctWords = 0;
    private int lives = 3;
    private long startTime;
    private Timer gameTimer, wordTimer;
    private boolean isPaused = false;
    private boolean isJavaMode = false;

    public Reaction_type() throws IOException {
        setTitle("Reaction Type");
        setSize(360, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        File imgFile = new File("C:\\Users\\Guest-PC\\IdeaProjects\\Reaction_type\\src\\flappybirdbg.png");
        backgroundImage = ImageIO.read(imgFile);


        JPanel gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
                for (FallingWord word : fallingWords) {
                    word.draw(g);
                }
                if (isPaused) {
                    g.setColor(new Color(255, 255, 255, 150));
                    g.fillRect(0, 0, getWidth(), getHeight());
                    g.setFont(new Font("Arial", Font.BOLD, 50));
                    g.setColor(Color.RED);
                    g.drawString("PAUSED", getWidth() / 2 - 100, getHeight() / 2);
                }
            }
        };
        add(gamePanel, BorderLayout.CENTER);


        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 20));
        inputField.setHorizontalAlignment(JTextField.CENTER);
        add(inputField, BorderLayout.SOUTH);


        timerLabel = new JLabel("Time: 0.00 seconds", SwingConstants.CENTER);
        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        livesLabel = new JLabel("Lives: 3", SwingConstants.CENTER);
        JPanel topPanel = new JPanel(new GridLayout(1, 3));
        topPanel.add(timerLabel);
        topPanel.add(scoreLabel);
        topPanel.add(livesLabel);
        add(topPanel, BorderLayout.NORTH);


        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    checkWord();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    togglePause();
                }
            }
        });

        showStartMenu();
    }


    private void showStartMenu() {
        String[] options = {"Random Words", "Java Keywords"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Choose a game mode:",
                "Mode Selection",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 1) {
            isJavaMode = true;
        } else {
            isJavaMode = false;
        }
        startGame();
    }


    private void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            gameTimer.stop();
            wordTimer.stop();
        } else {
            gameTimer.start();
            wordTimer.start();
        }
        repaint();
    }


    private void startGame() {
        correctWords = 0;
        lives = 3;
        startTime = System.currentTimeMillis();
        fallingWords.clear();
        updateLabels();


        gameTimer = new Timer(16, e -> {
            if (!isPaused) {
                List<FallingWord> wordsToRemove = new ArrayList<>();
                for (FallingWord word : fallingWords) {
                    word.update();
                    if (word.getY() > getHeight()) {
                        wordsToRemove.add(word);
                        loseLife();
                    }
                }
                fallingWords.removeAll(wordsToRemove);
                repaint();
                timerLabel.setText(String.format("Time: %.2f seconds", (System.currentTimeMillis() - startTime) / 1000.0));
            }
        });
        gameTimer.start();


        wordTimer = new Timer(2000, e -> {
            if (fallingWords.size() < 10) {
                String wordText = isJavaMode ? Words.getRandomJavaWord() : Words.getRandomWord();
                int x = random.nextInt(getWidth() - 100);
                fallingWords.add(new FallingWord(wordText, x, 0));
            }
        });
        wordTimer.start();
    }

    private void checkWord() {
        String userInput = inputField.getText().trim();
        boolean wordFound = fallingWords.removeIf(word -> word.getText().equals(userInput));

        if (wordFound) {
            correctWords++;
        }

        inputField.setText("");
        updateLabels();
    }

    private void loseLife() {
        lives--;
        updateLabels();
        if (lives <= 0) {
            endGame();



        }
    }

    private void updateLabels() {
        scoreLabel.setText("Score: " + correctWords);
        livesLabel.setText("Lives: " + lives);
    }

    private void endGame() {
        gameTimer.stop();
        wordTimer.stop();
        JOptionPane.showMessageDialog(this, "Game Over! Your Score: " + correctWords);
        System.exit(0);
    }

    private class FallingWord {
        private final String text;
        private final int x;
        private int y;

        public FallingWord(String text, int x, int y) {
            this.text = text;
            this.x = x;
            this.y = y;
        }

        public void update() {
            y += 2;
        }

        public void draw(Graphics g) {
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.setColor(Color.BLACK);
            g.drawString(text, x, y);
        }

        public String getText() {
            return text;
        }

        public int getY() {
            return y;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new Reaction_type().setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
