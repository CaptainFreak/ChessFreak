package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece.PieceType;
import com.google.common.collect.ImmutableList;

public class Queen extends Piece {

	private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES={-9,-8,-7,-1,1,7,8,9};

	
	public Queen(Alliance pieceAlliance, int piecePosition) {
		super(PieceType.QUEEN,piecePosition, pieceAlliance);
	}

	@Override
	public Collection<Move> calculatePossibleMoves(final Board board) {
		final List<Move> legalMoves=new ArrayList<>();
		
		for(final int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES){
			int candidateDestinationCoordinate=this.piecePosition;
			while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
				
				if(isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
						isEighthColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)){
					break;
				}
				
				candidateDestinationCoordinate+=candidateCoordinateOffset;
				if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
					final Tile candidateDestinationTile=board.getTile(candidateDestinationCoordinate);
					if(!candidateDestinationTile.isTileOccupied()){
						legalMoves.add(new Move.MajorMove(board,this,candidateDestinationCoordinate));
					}else{
						final Piece pieceAtDestination=candidateDestinationTile.getPiece();
						final Alliance pieceAlliance=pieceAtDestination.getPieceAlliance();
						if(this.pieceAlliance!=pieceAlliance){
							legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
						}

						break;
	
					}
				}
				
			}
		}
		
		return ImmutableList.copyOf(legalMoves);
	}
	@Override
	public String toString(){
		return PieceType.QUEEN.toString();
	}
	
	public Piece movePiece(Move move){
		return new Queen(move.getMovedPiece().getPieceAlliance(),move.getDestinationCoordinate()); 
	}

	
	private static boolean isFirstColumnExclusion(final int currentPosition,final int candidateOffset){
		return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1 || candidateOffset == -9 || candidateOffset==7);
	}
	
	private static boolean isEighthColumnExclusion(final int currentPosition,final int candidateOffset){
		return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == 1 || candidateOffset == -7 || candidateOffset==9);
	}
	
}
