package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece.PieceType;
import com.google.common.collect.ImmutableList;

public class Pawn extends Piece {

	private final static int[] CANDIDATE_MOVE_COORDINATE={8,16,7,9};
	
	public Pawn(Alliance pieceAlliance, int piecePosition) {
		super(PieceType.PAWN,piecePosition, pieceAlliance);
	}

	@Override
	public Collection<Move> calculatePossibleMoves(final Board board) {
		
		final List<Move> legalMoves=new ArrayList<>();
		for(final int currentCandidateOffset:CANDIDATE_MOVE_COORDINATE){
			final int candidateDestinationCoordinate=this.piecePosition+(this.getPieceAlliance().getDirection()*currentCandidateOffset);
			if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
				continue;
			}
			if(currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
				legalMoves.add(new Move.MajorMove(board, this,candidateDestinationCoordinate) );
			}else if(currentCandidateOffset==16 && this.isFirstMove() &&
					(BoardUtils.SEVENTH_RANK[this.piecePosition] && this.getPieceAlliance().isBlack())&& 
					(BoardUtils.SECOND_RANK[this.piecePosition] && this.getPieceAlliance().isWhite())){
				
				final int behindCandidateDestinationCoordinate=this.piecePosition+(this.pieceAlliance.getDirection()*8);
				if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
					!board.getTile(candidateDestinationCoordinate).isTileOccupied()){
					legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
				}
				
			}
			else if(currentCandidateOffset==7 &&
					!((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()||
						(BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))){
				if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
					final Piece pieceOnCandidate=board.getTile(candidateDestinationCoordinate).getPiece();
					if(this.pieceAlliance != pieceOnCandidate.pieceAlliance){
						legalMoves.add(new Move.MajorMove(board,this,candidateDestinationCoordinate));
					}
				}
				
			}
			else if(currentCandidateOffset==9 &&
					!((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()||
					(BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))){
				if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
					final Piece pieceOnCandidate=board.getTile(candidateDestinationCoordinate).getPiece();
					if(this.pieceAlliance != pieceOnCandidate.pieceAlliance){
						legalMoves.add(new Move.MajorMove(board,this,candidateDestinationCoordinate));
					}
				} 
			}
		}
		return ImmutableList.copyOf(legalMoves);
	}
	
	@Override
	public String toString(){
		return PieceType.PAWN.toString();
	}
	
	
	public Piece movePiece(Move move){
		return new Pawn(move.getMovedPiece().getPieceAlliance(),move.getDestinationCoordinate()); 
	}

	
	
	
}
