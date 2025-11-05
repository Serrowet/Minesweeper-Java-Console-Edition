public class Main {
    public static void main(String[] args) {
        WelcomeScreen welcomeScreen = new WelcomeScreen();
        welcomeScreen.welcome();

        GameController game = new GameController(4, 4, Difficulty.MEDIUM);
        game.startGame();
    }
}