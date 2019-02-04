package gomoku.impl;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import study_gomoku_proj.Cell;
import study_gomoku_proj.CellValue;
import study_gomoku_proj.GUIGomoku;
import study_gomoku_proj.GameTable;
import study_gomoku_proj.HumanTurn;

public class HumanTurnImpl implements HumanTurn {
	private static final Logger LOGGER = LoggerFactory.getLogger(GUIGomoku.class);
	private GameTable gameTable;

	@Override
	public void setGameTable(GameTable gameTable) {
		Objects.requireNonNull(gameTable, "Game table can't be null");
		this.gameTable = gameTable;
	}

	@Override
	public Cell makeTurn(int row, int col) {
		gameTable.setValue(row, col, CellValue.HUMAN);
		Cell cell = new Cell(row, col);
		LOGGER.info("Human turn is {}", cell);
		return new Cell(row, col);
	}

}
