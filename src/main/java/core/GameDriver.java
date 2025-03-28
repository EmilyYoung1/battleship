package core;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GameDriver {
    private final Presenter presenter;
    private DefaultGridBuilder gridBuilder;

    public GameDriver(Presenter presenter) {
        this.presenter = presenter;
    }

    public void start() {
        presenter.displayMessage("Welcome to Battleship!");
        Map<String, Runnable> gameOptions = new HashMap<>();
        gameOptions.put("start", this::startGame);
        gameOptions.put("stop", this::stopGame);
        presenter.displayOptions("Choose an action: start or stop", gameOptions);
    }

    private void startGame() {
        presenter.displayMessage("Game is starting...");
        // User input for grid size
        Grid grid = DefaultGridBuilder.defaultGrid();

        Grid firstGrid = gridBuilder.defaultGrid();
        Grid secondGrid = gridBuilder.defaultGrid();
        Game game = new Game(firstGrid, secondGrid); // Temporary default grids for both players
        while (true) {
            if (game.isOver() == false) {
                presenter.displayGrid(grid);
                presenter.displayMessage("Insert a coordinate to shoot!");
                Coord playerInputCoord = presenter.askForCoordinate(grid);
                grid.shoot(playerInputCoord);
                game.shoot(playerInputCoord);
                reportIfShipSunk(grid, playerInputCoord);
                game.next();
            } else {
                break;
            }
        }
    }

    private void reportIfShipSunk(Grid grid, Coord playerInputCoord) {
        Optional<Ship> currShip = grid.isShipSunk(playerInputCoord, true);
        if (currShip.isPresent()) {
            presenter.displayMessage("You sunk your opponents " + currShip.get().getName() + "!");
        }
    }

    private void stopGame() {
        presenter.displayMessage("Game is stopping...");
    }
}
