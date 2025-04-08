import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Reaction_type extends JFrame {

    static final String MENU_CARD = "MENU";
    static final String NAME_INPUT_CARD = "NAME_INPUT";
    static final String MODE_SELECT_CARD = "MODE_SELECT";
    static final String GAME_CARD = "GAME";
    static final String GAMEOVER_CARD = "GAMEOVER";

    final CardLayout cardLayout;
    final JPanel cardPanel;

    final BufferedImage backgroundImage;
    JTextField inputField;
    JLabel timerLabel;
    JLabel scoreLabel;
    JLabel livesLabel;
    JLabel playerNameLabel;
    final List<FallingWord> fallingWords = new ArrayList<>();
    final Random random = new Random();

    int score = 0;
    int highScore = 0;
    int lives = 3;
    long startTime;
    Timer gameTimer, wordTimer;
    boolean isPaused = false;
    boolean gameMode = false;
    String playerName = "Player";
    final int buttonWidth = 180;
    final int buttonHeight = 60;
    final int buttonX = 88;

    public Reaction_type() throws IOException {
        setTitle("Reaction Type");
        ImageIcon icon = new ImageIcon("shrek.png");
        setIconImage(icon.getImage());
        setSize(360, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);


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
        Background menuPanel = new Background();
        menuPanel.setLayout(new BorderLayout());
        menuPanel.setOpaque(false);

        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 0, 20));
        centerPanel.setOpaque(false);
        centerPanel.setLayout(null);

        JLabel titleLabel = new JLabel("Reaction Type", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 32));
        titleLabel.setBounds(0,100,360,50);

        JButton startButton = new JButton("Start Game");
        startButton.setFocusable(false);
        startButton.addActionListener(_ -> showNameInput());
        startButton.setBounds(buttonX,280,buttonWidth,buttonHeight);

        JButton exitButton = new JButton("Exit");
        exitButton.setFocusable(false);
        exitButton.addActionListener(_ -> System.exit(0));
        exitButton.setBounds(buttonX,350,buttonWidth,buttonHeight);

        centerPanel.add(titleLabel);
        centerPanel.add(startButton);
        centerPanel.add(exitButton);

        menuPanel.add(centerPanel, BorderLayout.CENTER);

        return menuPanel;
    }

    private JPanel createNameInputCard() {

        Background namePanel = new Background();
        namePanel.setLayout(new BorderLayout());
        namePanel.setOpaque(false);

        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        centerPanel.setOpaque(false);
        centerPanel.setLayout(null);

        JLabel titleLabel = new JLabel("Enter Your Name", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 24));
        JTextField nameField = new JTextField();
        nameField.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        nameField.setBounds(88,170,180,60);
        nameField.setHorizontalAlignment(JTextField.CENTER);

        JButton continueButton = new JButton("Continue");
        continueButton.setFocusable(false);
        continueButton.setBounds(buttonX,280,buttonWidth,buttonHeight);
        continueButton.addActionListener(_ -> {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                playerName = name;
                showModeSelect();
            } else {
                JOptionPane.showMessageDialog(this, "Please enter your name", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton backButton = new JButton("Back to Menu");
        backButton.setFocusable(false);
        backButton.setBounds(buttonX,350,buttonWidth,buttonHeight);
        backButton.addActionListener(_ -> showMenu());

        centerPanel.add(titleLabel);
        centerPanel.add(nameField);
        centerPanel.add(continueButton);
        centerPanel.add(backButton);

        namePanel.add(centerPanel, BorderLayout.CENTER);

        return namePanel;
    }

    private JPanel createModeSelectCard() {
        Background modePanel = new Background();
        modePanel.setLayout(new BorderLayout());
        modePanel.setOpaque(false);

        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        centerPanel.setOpaque(false);
        centerPanel.setLayout(null);

        JLabel titleLabel = new JLabel("Select Game Mode", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 24));
        titleLabel.setBounds(0,100,360,50);

        JButton randomWordsButton = new JButton("Random Words");
        randomWordsButton.setFocusable(false);
        randomWordsButton.setBounds(buttonX,280, buttonWidth,buttonHeight);
        randomWordsButton.addActionListener(_ -> {
            gameMode = false;
            showGame();
        });

        JButton javaWordsButton = new JButton("Java Keywords");
        javaWordsButton.setFocusable(false);
        javaWordsButton.setBounds(buttonX,350,buttonWidth,buttonHeight);
        javaWordsButton.addActionListener(_ -> {
            gameMode = true;
            showGame();
        });

        JButton backButton = new JButton("Back");
        backButton.setFocusable(false);
        backButton.setBounds(buttonX,420,buttonWidth,buttonHeight);
        backButton.addActionListener(_ -> showNameInput());

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
                    g.setFont(new Font("Times New Roman", Font.BOLD, 50));
                    g.setColor(Color.BLACK);
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
    private void updateHighScore(){

        if(score > highScore){
            highScore = score;
        }
    }

    private JPanel createGameOverCard() {

        Background gameOverPanel = new Background();
        gameOverPanel.setLayout(new BorderLayout());
        gameOverPanel.setOpaque(false);

        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        centerPanel.setOpaque(false);
        centerPanel.setLayout(null);

        JLabel gameOverLabel = new JLabel("Game Over", SwingConstants.CENTER);
        gameOverLabel.setFont(new Font("Times New Roman", Font.BOLD, 32));
        gameOverLabel.setBounds(0, 100, 360, 100);

//        JLabel highScoreLabel = new JLabel("Highest Score: "+ highScore, SwingConstants.CENTER );
//        highScoreLabel.setFont(new Font("Times New Roman", Font.BOLD,20));
//        highScoreLabel.setBounds(0, 200, 360, 30);


        JLabel playerLabel = new JLabel("Player: " + playerName, SwingConstants.CENTER);
        playerLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        playerLabel.setBounds(0, 170, 360, 30);

        JLabel scoreLabel = new JLabel("Score: " + score, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Times New Roman", Font.PLAIN, 24));
        scoreLabel.setBounds(0, 230, 360, 35);

        JButton restartButton = new JButton("Play Again");
        restartButton.setFocusable(false);
        restartButton.setBounds(buttonX,280,buttonWidth,buttonHeight);
        restartButton.addActionListener(_ -> showNameInput());
        inputField.setText(" ");

        JButton exitButton = new JButton("Quit");
        exitButton.setFocusable(false);
        exitButton.setBounds(buttonX,350,buttonWidth,buttonHeight);
        exitButton.addActionListener(_ -> System.exit(0));



        centerPanel.add(gameOverLabel);
        centerPanel.add(playerLabel);
        centerPanel.add(scoreLabel);
        centerPanel.add(restartButton);
        centerPanel.add(exitButton);
        //centerPanel.add(highScoreLabel);
        updateHighScore();

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
        scoreLabel.setText("Score: " + score);


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
        score = 0;
        lives = 3;
        startTime = System.currentTimeMillis();
        fallingWords.clear();
        updateLabels();

        inputField.requestFocusInWindow();

        gameTimer = new Timer(16, _ -> {
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

        wordTimer = new Timer(2500, _ -> {
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
            score++;
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
        scoreLabel.setText("Score: " + score);
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
            if(score >20){
                y += 0.8;
            } else if (score >15) {
                y += 0.7;
            }
            else if (score >10) {
                y += 0.7;
            }
            else if (score >5) {
                y += 0.7;
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
