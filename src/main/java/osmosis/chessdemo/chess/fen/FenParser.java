package osmosis.chessdemo.chess.fen;

import osmosis.chessdemo.chess.board.BoardSquares;
import osmosis.chessdemo.chess.exceptions.InvalidFenException;
import osmosis.chessdemo.chess.pieces.*;
import osmosis.chessdemo.chess.position.ChessPosition;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
		return new BoardSquares(parsePieces(getRanksStrings(fen)));
	}

	private static String[] getRanksStrings(String fen) {
		return fen.split("/");
	}

	private static Collection<Piece> parsePieces(String rankString, int rankNumber) {
		Collection<Piece> rankPieces = new HashSet<>();
		Iterator<Character> iterator = rankString.chars().mapToObj(c -> (char) c).iterator();
		for (int fileNumber = 1; fileNumber <= 8; fileNumber++) {
			char character = iterator.next();
			if (Character.isDigit(character)) {
				fileNumber += Character.getNumericValue(character);
				continue;
			}
			ChessPosition position = new ChessPosition(getFile(fileNumber), getRank(rankNumber));
			rankPieces.add(getPiece(character, position));
		}
		return rankPieces;
	}

	private static Collection<Piece> parsePieces(String[] ranksStrings) {
		Collection<Piece> pieces = new HashSet<>();
		for (int rankNumber = 8; rankNumber >= 1; rankNumber--) {
			String rankString = ranksStrings[8 - rankNumber];
			Collection<Piece> rankPieces = parsePieces(rankString, rankNumber);
			pieces.addAll(rankPieces);
		}
		return pieces;
	}

	private static Piece getPiece(char c, ChessPosition position) {
		return PIECE_CREATOR_MAP.get(Character.toLowerCase(c)).apply(getPieceColor(c), position);
	}

	private static PieceColor getPieceColor(char c) {
		return Character.isLowerCase(c) ? PieceColor.BLACK : PieceColor.WHITE;
	}
}
