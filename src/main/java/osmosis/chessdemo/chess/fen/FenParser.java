package osmosis.chessdemo.chess.fen;

import osmosis.chessdemo.chess.board.BoardSquares;
import osmosis.chessdemo.chess.exceptions.InvalidFenException;
import osmosis.chessdemo.chess.pieces.*;
import osmosis.chessdemo.chess.position.ChessPosition;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.BiFunction;

import static osmosis.chessdemo.chess.position.File.getFile;
import static osmosis.chessdemo.chess.position.Rank.getRank;

public class FenParser {
	private static final HashMap<Character, BiFunction<PieceColor, ChessPosition, Piece>> PIECE_CREATOR_MAP = new HashMap<>();

	static {
		PIECE_CREATOR_MAP.put('b', Bishop::new);
		PIECE_CREATOR_MAP.put('k', King::new);
		PIECE_CREATOR_MAP.put('n', Knight::new);
		PIECE_CREATOR_MAP.put('p', Pawn::new);
		PIECE_CREATOR_MAP.put('q', Queen::new);
		PIECE_CREATOR_MAP.put('r', Rook::new);
	}

	public static BoardSquares parse(String fen) throws InvalidFenException {
		FenValidator.validate(fen);
		Collection<Piece> pieces = new HashSet<>();
		int rankNumber = 8;
		String[] ranks = fen.split("/");
		for (String rank : ranks) {
			int fileNumber = 1;
			for (char character : rank.toCharArray()) {
				if (Character.isDigit(character)) {
					fileNumber += Character.getNumericValue(character);
					continue;
				}
				ChessPosition position = new ChessPosition(getFile(fileNumber), getRank(rankNumber));
				pieces.add(getPiece(character, position));
				fileNumber++;
			}
			rankNumber--;
		}
		return new BoardSquares(pieces);
	}

	private static Piece getPiece(char c, ChessPosition position) {
		PieceColor color = Character.isLowerCase(c) ? PieceColor.BLACK : PieceColor.WHITE;
		c = Character.toLowerCase(c);
		return PIECE_CREATOR_MAP.get(c).apply(color, position);
	}
}
