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
	private static final HashMap<Character, BiFunction<PieceColor, ChessPosition, Piece>> PIECE_CREATOR_MAP = new HashMap<>() {
		{
			put('b', Bishop::new);
			put('k', King::new);
			put('n', Knight::new);
			put('p', Pawn::new);
			put('q', Queen::new);
			put('r', Rook::new);
		}
	};

	public static BoardSquares parse(String fen) throws InvalidFenException {
		FenValidator.validate(fen);
		String[] ranks = fen.split("/");
		Collection<Piece> pieces = parsePieces(ranks);
		return new BoardSquares(pieces);
	}

	private static Collection<Piece> parsePieces(String[] ranks) {
		Collection<Piece> pieces = new HashSet<>();
		for (int rankNumber = 8; rankNumber >= 1; rankNumber--) {
			Collection<Piece> rankPieces = parsePieces(rankNumber, ranks[8 - rankNumber]);
			pieces.addAll(rankPieces);
		}
		return pieces;
	}

	private static Collection<Piece> parsePieces(int rankNumber, String rankString) {
		Collection<Piece> rankPieces = new HashSet<>();
		int fileNumber = 1;
		for (char character : rankString.toCharArray()) {
			if (Character.isDigit(character)) {
				fileNumber += Character.getNumericValue(character);
				continue;
			}
			ChessPosition position = new ChessPosition(getFile(fileNumber), getRank(rankNumber));
			rankPieces.add(getPiece(character, position));
			fileNumber++;
		}
		return rankPieces;
	}

	private static Piece getPiece(char c, ChessPosition position) {
		PieceColor color = Character.isLowerCase(c) ? PieceColor.BLACK : PieceColor.WHITE;
		c = Character.toLowerCase(c);
		return PIECE_CREATOR_MAP.get(c).apply(color, position);
	}
}
