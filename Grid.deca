/*
 *	Draw the grid
 */

class Grid {
	int box00;
	int box01;
	int box02;
	int box10;
	int box11;
	int box12;
	int box20;
	int box21;
	int box22;

	int boxLeft = 9;

	boolean isWin(int numPlayer) {
		return box00 == box01 && box01 == box02 && box00 == numPlayer
			|| box10 == box11 && box11 == box12 && box10 == numPlayer
			|| box20 == box21 && box21 == box22 && box20 == numPlayer
			|| box00 == box11 && box11 == box22 && box00 == numPlayer
			|| box20 == box11 && box11 == box02 && box20 == numPlayer
			|| box00 == box10 && box10 == box20 && box00 == numPlayer
			|| box01 == box11 && box11 == box21 && box01 == numPlayer
			|| box02 == box12 && box12 == box22 && box02 == numPlayer;


	}

	int isGameEnd() {
		
		if (isWin(1)) {
			return 1;
		}
		if (isWin(2)) {
			return 2;
		}
		if (boxLeft == 0) {
			return -1;
		}

		return 0;
	}

	void boxDecrement() {
		boxLeft = boxLeft - 1;
	}


	boolean isBoxEmpty(int numBox) {
		if (numBox == 1) {return box00 == 0;}
		else if (numBox == 2) {return box01 == 0;}
		else if (numBox == 3) {return box02 == 0;}
		else if (numBox == 4) {return box10 == 0;}
		else if (numBox == 5) {return box11 == 0;}
		else if (numBox == 6) {return box12 == 0;}
		else if (numBox == 7) {return box20 == 0;}
		else if (numBox == 8) {return box21 == 0;}
		else if (numBox == 9) {return box22 == 0;}
		else {return false;}
	}

	void placeInBoxes(int numBox, int numPlayer) {
		if (numBox == 1) {
			box00 = numPlayer;
		} else if (numBox == 2) {
			box01 = numPlayer;
		} else if (numBox == 3) {
			box02 = numPlayer;
		} else if (numBox == 4) {
			box10 = numPlayer;
		} else if (numBox == 5) {
			box11 = numPlayer;
		} else if (numBox == 6) {
			box12 = numPlayer;
		} else if (numBox == 7) {
			box20 = numPlayer;
		} else if (numBox == 8) {
			box21 = numPlayer;
		} else if (numBox == 9) {
			box22 = numPlayer;
		}
	}

	void drawBox(int box, int line) {
		if (line == 1) {
			if (box == 1) {
				//print("│    \\ /   ");
				print("│         ");
			} else if (box == 2) {
				//print("│    / \\   ");
				print("│         ");
			} else {
				print("│         ");
			}
		} else if (line == 2) {
			if (box == 1) {
				print("│    🟢   ");
			} else if (box == 2) {
				//print("│   │   │   ");
				print("│    ❌   ");
			} else {
				print("│         ");
			}
		} else if (line == 3) {
			if (box == 1) {
				//print("│    / \\    ");
				print("│         ");
			} else if (box == 2) {
				//print("│    \\ /    ");
				print("│         ");
			} else {
				print("│         ");
			}

		}
	}

	void renderGrid() {
		println();
		println();
		println();
		println();
		println();
		println();
		println("┌─────────┬─────────┬─────────┐");
		drawBox(box00, 1);
		drawBox(box01, 1);
		drawBox(box02, 1);
		println("│");

		drawBox(box00, 2);
		drawBox(box01, 2);
		drawBox(box02, 2);
		println("│");

		drawBox(box00, 3);
		drawBox(box01, 3);
		drawBox(box02, 3);
		println("│");

		println("├─────────┼─────────┼─────────┤");

		drawBox(box10, 1);
		drawBox(box11, 1);
		drawBox(box12, 1);
		println("│");

		drawBox(box10, 2);
		drawBox(box11, 2);
		drawBox(box12, 2);
		println("│");

		drawBox(box10, 3);
		drawBox(box11, 3);
		drawBox(box12, 3);
		println("│");

		println("├─────────┼─────────┼─────────┤");


		drawBox(box20, 1);
		drawBox(box21, 1);
		drawBox(box22, 1);
		println("│");

		drawBox(box20, 2);
		drawBox(box21, 2);
		drawBox(box22, 2);
		println("│");

		drawBox(box20, 3);
		drawBox(box21, 3);
		drawBox(box22, 3);
		println("│");

		println("└─────────┴─────────┴─────────┘");
		println();
		println();
		println();
	}
}
