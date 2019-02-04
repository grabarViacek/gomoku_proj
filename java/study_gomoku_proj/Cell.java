package study_gomoku_proj;

public class Cell {
	private final int indexRow;
	private final int indexCol;

	public Cell(int indexRow, int indexCol) {
		super();
		this.indexRow = indexRow;
		this.indexCol = indexCol;
	}

	public int getIndexRow() {
		return indexRow;
	}

	public int getIndexCol() {
		return indexCol;
	}

	public String toString() {
		return indexRow + ":" + indexCol;
	}

}
