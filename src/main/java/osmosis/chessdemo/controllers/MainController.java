package osmosis.chessdemo.controllers;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import osmosis.chessdemo.chess.board.Board;
import osmosis.chessdemo.chess.move.initiator.MoveInitiator;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController extends Controller {
	public GridPane boardGridPane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);

		// Board background
		BackgroundSize backgroundSize = new BackgroundSize(boardGridPane.getPrefWidth(), boardGridPane.getPrefHeight(), true, true, true, true);
		Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/osmosis/chessdemo/images/boards/board_background.png")).toExternalForm());
		boardGridPane.setBackground(new Background(new BackgroundImage(backgroundImage, null, null, null, backgroundSize)));

		Board board = Board.createChessBoard(boardGridPane);
		MoveInitiator.getInstance().registerBoard(board);
	}
}
