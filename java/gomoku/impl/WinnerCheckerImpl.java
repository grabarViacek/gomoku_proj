package gomoku.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import study_gomoku_proj.Cell;
import study_gomoku_proj.CellValue;
import study_gomoku_proj.GUIGomoku;
import study_gomoku_proj.GameTable;
import study_gomoku_proj.WinnerChecker;
import study_gomoku_proj.WinnerResult;

public class WinnerCheckerImpl implements WinnerChecker {
	private static final Logger LOGGER = LoggerFactory.getLogger(GUIGomoku.class);
	private GameTable gameTable;
	private int winCount = ConstantsImpl.WIN_COUNT;

	@Override
	public void setGameTable(GameTable gameTable) {
		Objects.requireNonNull(gameTable, "Game Table can't be null");
		if (gameTable.getSize() < winCount) {
			throw new IndexOutOfBoundsException(
					"Size of Game Table is small:" + gameTable.getSize() + ". Must be at>=" + winCount);
		}
		this.gameTable = gameTable;

	}

	@Override
	public WinnerResult isWinnerFound(CellValue cellValue) {
		Objects.requireNonNull(cellValue, "cellValue can't be null");
		List<Cell> searchWinRes = new ArrayList<>();
		LOGGER.trace("Try to find winner by row: is {} winner?", cellValue);
		searchWinRes = findWinnerByRow(cellValue);
		if (searchWinRes != null) {
			LOGGER.debug("Winner is {}. by row {}", cellValue, searchWinRes);
			return new WinnerSearchRes(searchWinRes);
		}
		LOGGER.trace("Try to find winner by coll: is {} winner?", cellValue);
		searchWinRes = findWinnerByCol(cellValue);
		if (searchWinRes != null) {
			LOGGER.debug("Winner is {}. by coll {}", cellValue, searchWinRes);
			return new WinnerSearchRes(searchWinRes);
		}
		LOGGER.trace("Try to find winner by main diagonal: is {} winner?", cellValue);
		searchWinRes = findWinnerByMainDiagonal(cellValue);
		if (searchWinRes != null) {
			LOGGER.debug("Winner is {}. by main diagonal {}", cellValue, searchWinRes);
			return new WinnerSearchRes(searchWinRes);
		}
		LOGGER.trace("Try to find winner by not main diagonal: is {} winner?", cellValue);
		searchWinRes = findWinnerByNoMainDiagonal(cellValue);
		if (searchWinRes != null) {
			LOGGER.debug("Winner is {}. by not main diagonal {}", cellValue, searchWinRes);
			return new WinnerSearchRes(searchWinRes);
		}
		LOGGER.trace("Winner not found");
		return new WinnerSearchRes(null);
	}

	private static class WinnerSearchRes implements WinnerResult {
		private final List<Cell> winnerCells;

		WinnerSearchRes(List<Cell> winnerCells) {
			if (winnerCells != null) {
				this.winnerCells = Collections.unmodifiableList(winnerCells);
			} else {
				this.winnerCells = Collections.emptyList();
			}
		}

		public List<Cell> getWinnerCells() {
			return winnerCells;
		}

		public boolean winnerExists() {
			return winnerCells.size() > 0;
		}
	}

	protected List<Cell> findWinnerByRow(CellValue cellValue) {
		for (int i = 0; i < gameTable.getSize(); i++) {
			List<Cell> temp = new ArrayList<>();
			for (int j = 0; j < gameTable.getSize(); j++) {
				if (gameTable.getValue(i, j) == cellValue) {
					temp.add(new Cell(i, j));
					if (temp.size() == winCount) {
						return temp;
					}
				} else {
					temp.clear();
					if (j > gameTable.getSize() - winCount) {
						break;
					}
				}
			}
		}
		return null;
	}

	protected List<Cell> findWinnerByCol(CellValue cellValue) {
		for (int i = 0; i < gameTable.getSize(); i++) {
			List<Cell> temp = new ArrayList<>();
			for (int j = 0; j < gameTable.getSize(); j++) {
				if (gameTable.getValue(j, i) == cellValue) {
					temp.add(new Cell(j, i));
					if (temp.size() == winCount) {
						return temp;
					}
				} else {
					temp.clear();
					if (j > gameTable.getSize() - winCount) {
						break;
					}
				}
			}
		}
		return null;
	}

	protected List<Cell> findWinnerByMainDiagonal(CellValue cellValue) {
		int winCountMin1 = winCount - 1;
		for (int i = 0; i < gameTable.getSize() - winCountMin1; i++) {
			for (int j = 0; j < gameTable.getSize() - winCountMin1; j++) {
				if (gameTable.getValue(i, j) == cellValue) {
					List<Cell> tempCombination = new ArrayList<>();
					tempCombination = searchWinningCombinationByMainDiagonal(i, j, cellValue, tempCombination);
					if (tempCombination != null) {
						return tempCombination;
					}
				}
			}
		}
		return null;
	}

	private List<Cell> searchWinningCombinationByMainDiagonal(int i, int j, CellValue cellValue,
			List<Cell> tempCombination) {
		for (int k = 0; k < winCount; k++) {
			if (gameTable.getValue(i + k, j + k) == cellValue) {
				tempCombination.add(new Cell(i + k, j + k));
				if (tempCombination.size() == winCount) {
					return tempCombination;
				}
			} else {
				break;
			}
		}
		return null;
	}

	protected List<Cell> findWinnerByNoMainDiagonal(CellValue cellValue) {
		int winCountMin1 = winCount - 1;
		for (int i = 0; i < gameTable.getSize() - winCountMin1; i++) {
			for (int j = winCountMin1; j < gameTable.getSize(); j++) {
				if (gameTable.getValue(i, j) == cellValue) {
					List<Cell> tempCombination = new ArrayList<>();
					tempCombination = searchWinningCombinationByNoMainDiagonal(i, j, cellValue, tempCombination);
					if (tempCombination != null) {
						return tempCombination;
					}
				}
			}
		}
		return null;
	}

	private List<Cell> searchWinningCombinationByNoMainDiagonal(int i, int j, CellValue cellValue,
			List<Cell> tempCombination) {
		for (int k = 0; k < winCount; k++) {
			if (gameTable.getValue(i + k, j - k) == cellValue) {
				tempCombination.add(new Cell(i + k, j - k));
				if (tempCombination.size() == winCount) {
					return tempCombination;
				}
			} else {
				break;
			}
		}
		return null;
	}

}
