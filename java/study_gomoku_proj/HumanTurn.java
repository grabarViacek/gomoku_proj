package study_gomoku_proj;

public interface HumanTurn {
	void setGameTable(GameTable gameTable);

	Cell makeTurn(int row, int col);

}
