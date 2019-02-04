package study_gomoku_proj;

import java.util.List;

public interface WinnerResult {
	boolean winnerExists();

	List<Cell> getWinnerCells();
}
