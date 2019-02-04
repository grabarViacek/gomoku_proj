package gomoku.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import study_gomoku_proj.Cell;
import study_gomoku_proj.CellValue;
import study_gomoku_proj.ComputerTurn;
import study_gomoku_proj.GUIGomoku;
import study_gomoku_proj.GameTable;

public class ImplComputerTurn implements ComputerTurn {
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
	public Cell makeTurn() {
		int quantityFigure = winCount - 1;
		int quantityEmpty = 1;
		if (gameTable.emptyCellExists() == true) {
			for (int i = 0; i < winCount - 1; i++) {
				Cell cell = attemptOrNotLettingWIN(quantityFigure, quantityEmpty, CellValue.COMPUTER);
				if (cell != null) {
					gameTable.setValue(cell.getIndexRow(), cell.getIndexCol(), CellValue.COMPUTER);
					LOGGER.info("Computer turn is {}", cell);
					return cell;
				}
				cell = attemptOrNotLettingWIN(quantityFigure, quantityEmpty, CellValue.HUMAN);
				if (cell != null) {
					gameTable.setValue(cell.getIndexRow(), cell.getIndexCol(), CellValue.COMPUTER);
					LOGGER.info("Computer turn is {}", cell);
					return cell;
				}
				quantityFigure--;
				quantityEmpty++;
			}
		} else {
			throw new ComputerCantMakeTurnException("All cells are filled");
		}
		return findaRandomMove();
	}

	private Cell findaRandomMove() {
		List<Cell> freeCells = new ArrayList<>();
		for (int i = 0; i < gameTable.getSize(); i++) {
			for (int j = 0; j < gameTable.getSize(); j++) {
				if (gameTable.getValue(i, j) == CellValue.EMPTY) {
					freeCells.add(new Cell(i, j));
				}
			}
		}
		Collections.shuffle(freeCells);
		Cell cell = freeCells.get(0);
		gameTable.setValue(cell.getIndexRow(), cell.getIndexCol(), CellValue.COMPUTER);
		return cell;
	}

	protected Cell attemptOrNotLettingWIN(int quantityFigure, int quantityEmpty, CellValue figure) {
		LOGGER.trace("Try to make turn by row for pattern {} empty and {} not empty", quantityEmpty, quantityFigure);
		Cell tryTurn = findSetByRow(quantityFigure, quantityEmpty, figure);
		if (tryTurn != null) {
			return tryTurn;
		}
		LOGGER.trace("Try to make turn by coll for pattern {} empty and {} not empty", quantityEmpty, quantityFigure);
		tryTurn = findSetByCol(quantityFigure, quantityEmpty, figure);
		if (tryTurn != null) {
			return tryTurn;
		}
		LOGGER.trace("Try to make turn by main diagonal for pattern {} empty and {} not empty", quantityEmpty,
				quantityFigure);
		tryTurn = findSetByMainDiagonal(quantityFigure, quantityEmpty, figure);
		if (tryTurn != null) {
			return tryTurn;
		}
		LOGGER.trace("Try to make turn by not main diagonal for pattern {} empty and {} not empty", quantityEmpty,
				quantityFigure);
		tryTurn = findSetByNoMainDiagonal(quantityFigure, quantityEmpty, figure);
		if (tryTurn != null) {
			return tryTurn;
		}
		return null;
	}

	protected Cell findSetByRow(int quantityFigure, int quantityEmpty, CellValue figure) {
		for (int i = 0; i < gameTable.getSize(); i++) {
			for (int j = 0; j < gameTable.getSize() - (winCount - 1); j++) {
				Cell cell = trySetByRow(quantityFigure, quantityEmpty, i, j, figure);
				if (cell != null) {
					LOGGER.info("Found {} empty and {} not empty cells by row ", quantityEmpty, quantityFigure);
					return cell;
				}
			}
		}
		return null;
	}

	private Cell trySetByRow(int quantityFigure, int quantityEmpty, int i, int j, CellValue figure) {
		List<Cell> cellEmpty = new ArrayList<>(winCount);
		List<Cell> cellFigure = new ArrayList<>(winCount);
		for (int k = 0; k < winCount; k++) {
			if (gameTable.getValue(i, j + k) == figure) {
				cellFigure.add(new Cell(i, j + k));
			} else if (gameTable.getValue(i, j + k) == CellValue.EMPTY) {
				cellEmpty.add(new Cell(i, j + k));
			} else {
				return null;
			}
		}
		if (cellFigure.size() == quantityFigure && cellEmpty.size() == quantityEmpty) {
			Collections.shuffle(cellEmpty);
			return cellEmpty.get(0);
		}
		return null;
	}

	protected Cell findSetByCol(int quantityFigure, int quantityEmpty, CellValue figure) {
		for (int i = 0; i < gameTable.getSize() - (winCount - 1); i++) {
			for (int j = 0; j < gameTable.getSize(); j++) {
				Cell cell = trySetByCol(quantityFigure, quantityEmpty, i, j, figure);
				if (cell != null) {
					LOGGER.info("Found {} empty and {} not empty cells by coll ", quantityEmpty, quantityFigure);
					return cell;
				}
			}
		}
		return null;
	}

	private Cell trySetByCol(int quantityFigure, int quantityEmpty, int i, int j, CellValue figure) {
		List<Cell> cellEmpty = new ArrayList<>(winCount);
		List<Cell> cellFigure = new ArrayList<>(winCount);
		for (int k = 0; k < winCount; k++) {
			if (gameTable.getValue(i + k, j) == figure) {
				cellFigure.add(new Cell(i + k, j));
			} else if (gameTable.getValue(i + k, j) == CellValue.EMPTY) {
				cellEmpty.add(new Cell(i + k, j));
			} else {
				return null;
			}
		}
		if (cellFigure.size() == quantityFigure && cellEmpty.size() == quantityEmpty) {
			Collections.shuffle(cellEmpty);
			return cellEmpty.get(0);
		}
		return null;
	}

	protected Cell findSetByMainDiagonal(int quantityFigure, int quantityEmpty, CellValue figure) {
		for (int i = 0; i < gameTable.getSize() - (winCount - 1); i++) {
			for (int j = 0; j < gameTable.getSize() - (winCount - 1); j++) {
				Cell cell = trySetByMainDiagonal(quantityFigure, quantityEmpty, i, j, figure);
				if (cell != null) {
					LOGGER.info("Found {} empty and {} not empty cells by main diagonal ", quantityEmpty,
							quantityFigure);
					return cell;
				}
			}
		}
		return null;
	}

	private Cell trySetByMainDiagonal(int quantityFigure, int quantityEmpty, int i, int j, CellValue figure) {
		List<Cell> cellEmpty = new ArrayList<>(winCount);
		List<Cell> cellFigure = new ArrayList<>(winCount);
		for (int k = 0; k < winCount; k++) {
			if (gameTable.getValue(i + k, j + k) == figure) {
				cellFigure.add(new Cell(i + k, j + k));
			} else if (gameTable.getValue(i + k, j + k) == CellValue.EMPTY) {
				cellEmpty.add(new Cell(i + k, j + k));
			} else {
				return null;
			}
		}
		if (cellFigure.size() == quantityFigure && cellEmpty.size() == quantityEmpty) {
			Collections.shuffle(cellEmpty);
			return cellEmpty.get(0);
		}
		return null;
	}

	protected Cell findSetByNoMainDiagonal(int quantityFigure, int quantityEmpty, CellValue figure) {
		for (int i = 0; i < gameTable.getSize() - (winCount - 1); i++) {
			for (int j = winCount - 1; j < gameTable.getSize()- 1; j++) {
				Cell cell = trySetByNoMainDiagonal(quantityFigure, quantityEmpty, i, j, figure);
				if (cell != null) {
					LOGGER.info("Found {} empty and {} not empty cells by not main diagonal ", quantityEmpty,
							quantityFigure);
					return cell;
				}
			}
		}
		return null;
	}

	private Cell trySetByNoMainDiagonal(int quantityFigure, int quantityEmpty, int i, int j, CellValue figure) {
		List<Cell> cellEmpty = new ArrayList<>(winCount);
		List<Cell> cellFigure = new ArrayList<>(winCount);
		for (int k = 0; k < winCount; k++) {
			if (gameTable.getValue(i + k, j - k) == figure) {
				cellFigure.add(new Cell(i + k, j - k));
			} else if (gameTable.getValue(i + k, j - k) == CellValue.EMPTY) {
				cellEmpty.add(new Cell(i + k, j - k));
			} else {
				return null;
			}
		}
		if (cellFigure.size() == quantityFigure && cellEmpty.size() == quantityEmpty) {
			Collections.shuffle(cellEmpty);
			return cellEmpty.get(0);
		}
		return null;
	}

	@Override
	public Cell makeFirstTurn() {
		Cell cell = new Cell(gameTable.getSize() / 2, gameTable.getSize() / 2);
		gameTable.setValue(cell.getIndexRow(), cell.getIndexCol(), CellValue.COMPUTER);
		LOGGER.info("Computer turn is {}", cell);
		return cell;
	}

}
