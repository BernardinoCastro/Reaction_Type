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

    private static final String MENU_CARD = "MENU";
    private static final String NAME_INPUT_CARD = "NAME_INPUT";
    private static final String MODE_SELECT_CARD = "MODE_SELECT";
    private static final String GAME_CARD = "GAME";
    private static final String GAMEOVER_CARD = "GAMEOVER";

    private CardLayout cardLayout;
    private JPanel cardPanel;


    private BufferedImage backgroundImage;
    private JTextField inputField;
    private JLabel timerLabel;
    private JLabel scoreLabel;
    private JLabel livesLabel;
    private JLabel playerNameLabel;
    private List<FallingWord> fallingWords = new ArrayList<>();
    private Random random = new Random();

    private int correctWords = 0;
    private int lives = 3;
    private long startTime;
    private Timer gameTimer, wordTimer;
    private boolean isPaused = false;
    private boolean gameMode = false;
    private String playerName = "Player";

    public Reaction_type() throws IOException {
        setTitle("Reaction Type");
        setSize(360, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);



        File imgFile = new File("C:\\Users\\Guest-PC\\IdeaProjects\\Reaction_type\\src\\flappybirdbg.png");
        backgroundImage = ImageIO.read(imgFile);


        cardPanel.add(createMenuCard(), MENU_CARD);
        cardPanel.add(createNameInputCard(), NAME_INPUT_CARD);
        cardPanel.add(createModeSelectCard(), MODE_SELECT_CARD);
        cardPanel.add(createGameCard(), GAME_CARD);
        cardPanel.add(createGameOverCard(), GAMEOVER_CARD);

        add(cardPanel);

        showMenu();
    }

    private JPanel createMenuCard() {
        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.setOpaque(false);

        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 0, 20));
        centerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Reaction Type", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));

        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> showNameInput());

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        centerPanel.add(titleLabel);
        centerPanel.add(startButton);
        centerPanel.add(exitButton);

        menuPanel.add(centerPanel, BorderLayout.CENTER);

        return menuPanel;
    }

    private JPanel createNameInputCard() {
        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.setOpaque(false);

        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        centerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Enter Your Name", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JTextField nameField = new JTextField(15);
        nameField.setFont(new Font("Arial", Font.PLAIN, 18));
        nameField.setHorizontalAlignment(JTextField.CENTER);

        JButton continueButton = new JButton("Continue");
        continueButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                playerName = name;
                showModeSelect();
            } else {
                JOptionPane.showMessageDialog(this, "Please enter your name", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> showMenu());

        centerPanel.add(titleLabel);
        centerPanel.add(nameField);
        centerPanel.add(continueButton);
        centerPanel.add(backButton);

        namePanel.add(centerPanel, BorderLayout.CENTER);

        return namePanel;
    }

    private JPanel createModeSelectCard() {
        JPanel modePanel = new JPanel(new BorderLayout());
        modePanel.setOpaque(false);

        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        centerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Select Game Mode", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JButton randomWordsButton = new JButton("Random Words");
        randomWordsButton.addActionListener(e -> {
            gameMode = false;
            showGame();
        });

        JButton javaWordsButton = new JButton("Java Keywords");
        javaWordsButton.addActionListener(e -> {
            gameMode = true;
            showGame();
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showNameInput());

        centerPanel.add(titleLabel);
        centerPanel.add(randomWordsButton);
        centerPanel.add(javaWordsButton);
        centerPanel.add(backButton);

        modePanel.add(centerPanel, BorderLayout.CENTER);

        return modePanel;
    }

    private JPanel createGameCard() {
        JPanel gamePanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
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


        JPanel topPanel = new JPanel(new GridLayout(1, 4));

        playerNameLabel = new JLabel(playerName, SwingConstants.CENTER);
        playerNameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        timerLabel = new JLabel("Time: 0.00", SwingConstants.CENTER);
        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        livesLabel = new JLabel("Lives: 3", SwingConstants.CENTER);

        topPanel.add(playerNameLabel);
        topPanel.add(timerLabel);
        topPanel.add(scoreLabel);
        topPanel.add(livesLabel);

        gamePanel.add(topPanel, BorderLayout.NORTH);


        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 20));
        inputField.setHorizontalAlignment(JTextField.CENTER);
        gamePanel.add(inputField, BorderLayout.SOUTH);

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

        return gamePanel;
    }

    private JPanel createGameOverCard() {
        JPanel gameOverPanel = new JPanel(new BorderLayout());
        gameOverPanel.setOpaque(false);

        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        centerPanel.setOpaque(false);

        JLabel gameOverLabel = new JLabel("Game Over", SwingConstants.CENTER);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 32));

        JLabel playerLabel = new JLabel("Player: " + playerName, SwingConstants.CENTER);
        playerLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        JLabel scoreLabel = new JLabel("Score: " + correctWords, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        JButton restartButton = new JButton("Play Again");
        restartButton.addActionListener(e -> showNameInput());

        centerPanel.add(gameOverLabel);
        centerPanel.add(playerLabel);
        centerPanel.add(scoreLabel);
        centerPanel.add(restartButton);

        gameOverPanel.add(centerPanel, BorderLayout.CENTER);

        return gameOverPanel;
    }

    private void showMenu() {
        cardLayout.show(cardPanel, MENU_CARD);
    }

    private void showNameInput() {
        cardLayout.show(cardPanel, NAME_INPUT_CARD);
    }

    private void showModeSelect() {
        cardLayout.show(cardPanel, MODE_SELECT_CARD);
    }

    private void showGame() {
        playerNameLabel.setText(playerName);
        cardLayout.show(cardPanel, GAME_CARD);
        startGame();
    }

    private void showGameOver() {
        JPanel gameOverPanel = (JPanel) cardPanel.getComponent(4);
        JLabel playerLabel = (JLabel) ((JPanel) gameOverPanel.getComponent(0)).getComponent(1);
        JLabel scoreLabel = (JLabel) ((JPanel) gameOverPanel.getComponent(0)).getComponent(2);

        playerLabel.setText("Player: " + playerName);
        scoreLabel.setText("Score: " + correctWords);

        cardLayout.show(cardPanel, GAMEOVER_CARD);
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
        cardPanel.repaint();
    }

    private void startGame() {
        correctWords = 0;
        lives = 3;
        startTime = System.currentTimeMillis();
        fallingWords.clear();
        updateLabels();

        inputField.requestFocusInWindow();

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
                cardPanel.repaint();
                timerLabel.setText(String.format("Time: %.2f seconds", (System.currentTimeMillis() - startTime) / 1000.0));
            }
        });
        gameTimer.start();

        wordTimer = new Timer(2000, e -> {
            if (fallingWords.size() < 10) {
                String wordText = gameMode ? Words.getRandomJavaWord() : Words.getRandomWord();
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
        showGameOver();
    }

    private class FallingWord {
        final String text;
        final int x;
        double y;

        private FallingWord(String text, int x, int y) {
            this.text = text;
            this.x = x;
            this.y = y;
        }

        private void update() {
            y += 2;
            if(correctWords>20){
                y += 0.8;
            } else if (correctWords>15) {
                y += 0.5;
            }
            else if (correctWords>10) {
                y += 0.5;
            }
            else if (correctWords>5) {
                y += 0.5;
            }
        }

        private void draw(Graphics g) {
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.setColor(Color.BLACK);
            g.drawString(text, x,(int) y);
        }

        String getText() {
            return text;
        }

        int getY() {
            return (int) y;
        }
    }

}
