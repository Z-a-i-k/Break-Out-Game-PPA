public class ScoreManager {
    private static int highScore = 0;
    
    public static int getHighScore() {
        return highScore;
    }
    
    public static void setHighScore(int score) {
        highScore = score;
    }
    
    public static void resetHighScore() {
        highScore = 0;
    }
}
