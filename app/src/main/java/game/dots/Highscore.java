package game.dots;

public class HighScore {
    public String _name;
    public int _score;
    public String _grid;

    public HighScore(){}

    public HighScore(String name, int score, String grid){
        this._name = name;
        this._score = score;
        this._grid = grid;
    }

}
