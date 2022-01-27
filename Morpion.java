import "Grid.deca"


class IO {
	int askWhereToPlay(Grid grid) {
		boolean validNumber = false;
		int numBox;
		while (validNumber == false) {
			print("Where would you play (1 - 9): ");
			numBox = readInt();
			if (1 <= numBox && numBox <= 9) {
				if (grid.isBoxEmpty(numBox)) {
				validNumber = true;
				} else {
					println("This box is not empty!");
				}
			} else {
				println("Bad input, need to be between 1 and 9.");
			}
		}
		return numBox;
	}
}



{
	IO io = new IO();
	Grid grid = new Grid();
	Player player = new Player();

	while (grid.isGameEnd() == 0) {
		print("Player ");
		println(player.num);
		grid.renderGrid();
		grid.placeInBoxes(io.askWhereToPlay(grid), player.num);

		player.changePlayer();
		grid.boxDecrement();
	}

	grid.renderGrid();
	if (grid.isGameEnd() == -1) {
		println("Egalité ! 🤷");
	} else if (grid.isGameEnd() == 1) {
		println("Bravo ! 🥳");
	} else {
		println("L'IA est vraiment trop forte ! 🤖");
	}
}
