package study_gomoku_proj;

public interface ComputerTurn {
	void setGameTable(GameTable gameTable);

	Cell makeTurn();

	Cell makeFirstTurn();
}
