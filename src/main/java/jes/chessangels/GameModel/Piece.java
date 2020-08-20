/*
 * Copyright (C) 2020 jes.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package jes.chessangels.GameModel;

import java.util.ArrayList;

/**
 *
 * @author jes
 */
public class Piece {
    // 1 for white, 0 for empty, -1 for black
    public int color;
    public char type;
    
    public ArrayList<PieceStatus> piecestatuses = new ArrayList<>();
    
    public Piece(int clr, char ty, String[] pstypes) {
        color = clr;
        type = ty;
        
        for (String pstype : pstypes) {
            piecestatuses.add(new PieceStatus(pstype, toString()));
        }
        
    }
    
    public Piece(int clr, char ty) {
        color = clr;
        type = ty;
    }
    
    public static boolean ischesspiece(char c) {
        char piecesnames[] = {'k', 'p', 'q', 'b', 'n', 'r'};
        for (char pn : piecesnames) {
            if (c == pn)
                return true;
        }
        return false;
    }
    
    public void inflict(String pstype) {
        piecestatuses.add(new PieceStatus(pstype, toString()));
    }
    
    public void heal(String psname) {
        for (int i = 0; i < piecestatuses.size(); i++)
            if(psname.equals(piecestatuses.get(i).name))
                piecestatuses.remove(i);
    }
    
    public boolean containsstatus(String psname) {
        for (int i = 0; i < piecestatuses.size(); i++) {
            System.out.println(piecestatuses.get(i).name);
            if(psname.equals(piecestatuses.get(i).name))
                return true;
        }
        return false;
    }
    
    public Integer[] passturns() {
        ArrayList<Integer> finisheds = new ArrayList<>();
        
        for (PieceStatus ps : piecestatuses) {
            if(ps.passturn())
                finisheds.add(piecestatuses.indexOf(ps));
        }
        
        return finisheds.toArray(new Integer[0]);
    }
    
    @Override
    public String toString() {
        switch(type) {
            case 'e':
                return "Empty";
            case 'k':
                return "King";
            case 'p':
                return "Pawn";
            case 'q':
                return "Queen";
            case 'b':
                return "Bishop";
            case 'n':
                return "Knight";
            case 'r':
                return "Rook";
            default:
                return "!";
        }
    }

}
