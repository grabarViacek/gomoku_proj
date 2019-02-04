package study_gomoku_proj;

public interface WinnerChecker {
	void setGameTable(GameTable gameTable);

	WinnerResult isWinnerFound(CellValue cellValue);
}
