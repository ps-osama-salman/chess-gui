package osmosis.chessdemo.controllers;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import osmosis.chessdemo.chess.board.Board;
import osmosis.chessdemo.chess.board.BoardFactory;
import osmosis.chessdemo.chess.board.BoardProvider;
import osmosis.chessdemo.chess.fen.FenParser;
import osmosis.chessdemo.chess.pieces.PieceFactory;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController extends Controller {
	public GridPane boardGridPane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);

		setBoardBackground();

		BoardFactory boardFactory = new BoardFactory(new FenParser(new PieceFactory(new BoardProvider(null)))); // TODO
		Board board = boardFactory.create(boardGridPane);
	}

	private void setBoardBackground() {
		BackgroundSize backgroundSize = new BackgroundSize(boardGridPane.getPrefWidth(), boardGridPane.getPrefHeight(), true, true, true, true);
		Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/osmosis/chessdemo/images/boards/board_background.png")).toExternalForm());
		boardGridPane.setBackground(new Background(new BackgroundImage(backgroundImage, null, null, null, backgroundSize)));
	}
}
