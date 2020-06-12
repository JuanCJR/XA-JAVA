package com.databorough.utils;

public class RowNumberSpecification {
	private int numberOfRowsRequested;
	private int positionToRelativePosition;
	private int numberOfRowsReturned;
	private int totalCount;
	public RowNumberSpecification() {
	}
	public RowNumberSpecification(int positionToRelativePosition, int numberOfRowsRequested) {
		this.positionToRelativePosition = positionToRelativePosition;
		this.numberOfRowsRequested = numberOfRowsRequested;
	}
	public int getNumberOfRowsRequested() {
		return numberOfRowsRequested;
	}
	public void setNumberOfRowsRequested(int numberOfRowsRequested) {
		this.numberOfRowsRequested = numberOfRowsRequested;
	}
	public int getPositionToRelativePosition() {
		return positionToRelativePosition;
	}
	public void setPositionToRelativePosition(int positionToRelativePosition) {
		this.positionToRelativePosition = positionToRelativePosition;
	}
	public int getNumberOfRowsReturned() {
		return numberOfRowsReturned;
	}
	public void setNumberOfRowsReturned(int numberOfRowsReturned) {
		this.numberOfRowsReturned = numberOfRowsReturned;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}