package gomoku.impl;

import study_gomoku_proj.CellValue;
import study_gomoku_proj.GameTable;

public class ImplGameTable implements GameTable {
	private final CellValue[][] gameTable;

	public ImplGameTable() {
		gameTable = new CellValue[ConstantsImpl.SIZE][ConstantsImpl.SIZE];
		reInit();
	}
	public ImplGameTable(int length) {
		gameTable = new CellValue[length][length];
		reInit();
	}

	@Override
	public CellValue getValue(int row, int col) {
		if (row >= 0 && row < getSize() && col >= 0 && col < getSize()) {
			return gameTable[row][col];
		} else {
			throw new IndexOutOfBoundsException(
					"Invalid row or col indexes: row=" + row + ", col=" + col + ", size=" + getSize());
		}
	}

	@Override
	public void setValue(int row, int col, CellValue cellValue) {
		if (row >= 0 && row < getSize() && col >= 0 && col < getSize()) {
			gameTable[row][col] = cellValue;
		} else {
			throw new IndexOutOfBoundsException(
					"Invalid row or col indexes: row=" + row + ", col=" + col + ", size=" + getSize());
		}
	}

	@Override
	public void reInit() {
		for (int i = 0; i < gameTable.length; i++) {
			for (int j = 0; j < gameTable.length; j++) {
				setValue(i, j, CellValue.EMPTY);
			}
		}
	}

	@Override
	public int getSize() {
		return gameTable.length;
	}

	@Override
	public boolean isCellFree(int row, int col) {
		return getValue(row, col) == CellValue.EMPTY;
	}

	@Override
	public boolean emptyCellExists() {
		for (int i = 0; i < getSize(); i++) {
			for (int j = 0; j < getSize(); j++) {
				if (getValue(i, j) == CellValue.EMPTY) {
					return true;
				}
			}
		}
		return false;
	}

}
