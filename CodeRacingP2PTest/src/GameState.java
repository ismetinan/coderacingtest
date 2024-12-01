

import com.google.gson.Gson;

public class GameState {
    private String playerName;
    private int position;
    private int score;

    public GameState(String playerName, int position, int score) {
        this.playerName = playerName;
        this.position = position;
        this.score = score;
    }

    // Serialize to JSON
    public String toJson() {
        return new Gson().toJson(this);
    }

    // Deserialize from JSON
    public static GameState fromJson(String json) {
        return new Gson().fromJson(json, GameState.class);
    }

    // Getters and setters
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
