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
package jes.chessangels.Controller;

import java.util.ArrayList;
import static jes.chessangels.Controller.CommandHandler.wbtocolor;
import jes.chessangels.GameModel.Card;
import jes.chessangels.GameModel.Game;
import static jes.chessangels.GameModel.Game.incheck;
import jes.chessangels.GameModel.Piece;
import static jes.chessangels.GameModel.Piece.ischesspiece;

/**
 *
 * @author jes
 */
public class Liberties {

    public Point[] ps;
    
    public Liberties(ArrayList<Integer> rows, ArrayList<Integer> cols) {
        assert rows.size() == cols.size();
        
        ps = new Point[rows.size()];
        for (int i = 0; i < rows.size(); i++) {
            ps[i] = new Point(rows.get(i), cols.get(i));
        }
    }
    
    public void addAll(Liberties ls) {
        Point[] tempps = ps;
        ps = new Point[tempps.length + ls.ps.length];
        
        for (int i = 0; i < tempps.length + ls.ps.length; i++) {
            if (i < tempps.length)
                ps[i] = tempps[i];
            else
                ps[i] = ls.ps[i - tempps.length];
        }        
    }
    
    public boolean contains(int row, int col) {
        for (Point p : ps) {
            if (p.row == row && p.col == col)
                return true;
        }
        return false;
    }
    
    // Returns null if no King, otherwise returns location of king
    public Point containsaking(Game g) {
        for (Point p : ps) {
            if (g.gameboard.piecematrix[p.row][p.col].type == 'k') {
                return p;
            }
        }
        return null;
    }
    
    public static Liberties generatelibertiesforcard(Game g, Card c) {
        ArrayList<Integer> rs = new ArrayList<>();
        ArrayList<Integer> cs = new ArrayList<>();
        switch (c.name) {
            case "Pawn":
                for (int i = 0; i < 5; i++) {
                    // Empty in second-to-bottom row
                    if(canplacepiece(g, 3, i)) {
                        rs.add(3);
                        cs.add(i);
                    }
                }   break;
            case "Queen":
            case "Knight":
            case "Bishop":
            case "Rook":
                for (int i = 0; i < 5; i++) {
                    // Empty in bottom row
                    if (canplacepiece(g, 4, i)) {
                        rs.add(4);
                        cs.add(i);
                    }
                }   break;
            case "King":
                if (canplacepiece(g, 4, 2)) {
                    rs.add(4);
                    cs.add(2);
                }
                break;
            case "Hole":
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (g.gameboard.piecematrix[i][j].type == 'e'
                                && !g.gameboard.containsstatusat(i, j, "Hole")) {
                            rs.add(i);
                            cs.add(j);
                        }
                    }
                }
                break;
            case "Freeze":
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (ischesspiece(g.gameboard.piecematrix[i][j].type)
                                && g.gameboard.piecematrix[i][j].color != wbtocolor(g.turnplayer.iswhite)
                                && !g.gameboard.piecematrix[i][j].containsstatus("Frozen")) {
                            rs.add(i);
                            cs.add(j);
                        }
                    }
                }
                break;
            case "Entire Light":
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        if (!g.gameboard.containsstatusat(i, j, "Entire Light")) {
                            rs.add(i);
                            cs.add(j);
                        }
                    }
                }
                break;
            default:
                break;
        }
        return new Liberties(rs, cs);
    }
            
    public static Liberties generatelibertiesforpiece(Game g, int startrow, int startcol, boolean isenemy) {
        Piece p = g.gameboard.piecematrix[startrow][startcol];
        
        switch (p.type) {
            case 'p':
                int[] tocheckenemypawn = {SOUW, SOUE};
                int[] tocheckpawn = {NORT, NORE, NORW};
                if(isenemy)
                    return multitrace(g, 'p', tocheckenemypawn, startrow, startcol, false);
                else
                    return multitrace(g, 'p', tocheckpawn, startrow, startcol, false);
            case 'k':
                int[] tocheckking = {NORT, SOUT, EAST, WEST, NORW, NORE, SOUW, SOUE};
                return multitrace(g, 'k', tocheckking, startrow, startcol, false);
            case 'q':
                int[] tocheckqueen = {NORT, SOUT, EAST, WEST, NORW, NORE, SOUW, SOUE};
                return multitrace(g, 'q', tocheckqueen, startrow, startcol, true);
            case 'r':
                int[] tocheckrook = {NORT, SOUT, EAST, WEST};
                return multitrace(g, 'r', tocheckrook, startrow, startcol, true);
            case 'b':
                int[] tocheckbishop = {NORW, NORE, SOUW, SOUE};
                return multitrace(g, 'b', tocheckbishop, startrow, startcol, true);
            case 'n':
                return knightmoves(g, startrow, startcol);
            default:
                break;
        }
        return null;
    }
    
    // Calculates liberties for ray in certain direction
    private static Liberties raytrace(Game g, char typ, int DIR, int startrow, int startcol) {
        ArrayList<Integer> rs = new ArrayList<>();
        ArrayList<Integer> cs = new ArrayList<>();
 
        Point destincs = dirtoincs(DIR);
        int currow = startrow;
        int curcol = startcol;
        while (true) {
            // Move further along ray in direction DIR
            currow += destincs.row;
            curcol += destincs.col;

            if (!inbounds(currow, curcol)
                    || isblocked(g, currow, curcol, startrow, startcol)) {
                break;
            }

            rs.add(currow);
            cs.add(curcol);
            
            if (iscapture(g, currow, curcol, startrow, startcol))
                break;
        }

        return new Liberties(rs, cs);
    }
    
    // Calculates liberties for adjacent tile
    private static Liberties adj8trace(Game g, char typ, int DIR, int startrow, int startcol) {
        ArrayList<Integer> rs = new ArrayList<>();
        ArrayList<Integer> cs = new ArrayList<>();
 
        Point destincs = dirtoincs(DIR);
        int destrow = startrow + destincs.row;
        int destcol = startcol + destincs.col;
        if(inbounds(destrow, destcol)) {
            if (typ == 'k' 
                    && !isblocked(g, destrow, destcol, startrow, startcol)
                    && !isdangerous(g, destrow, destcol, startrow, startcol)) {
                rs.add(destrow);
                cs.add(destcol);
            } else if (typ == 'p') {
                if(DIR == NORT 
                        && !iscapture(g, destrow, destcol, startrow, startcol) 
                        && !isblocked(g, destrow, destcol, startrow, startcol)) {
                    rs.add(destrow);
                    cs.add(destcol);
                } else if((DIR == NORE || DIR == NORW)
                        && iscapture(g, destrow, destcol, startrow, startcol)) {
                    rs.add(destrow);
                    cs.add(destcol);
                } else if (DIR == SOUE || DIR == SOUW){ 
                    // check enemy pawns (sweeping for things like check)
                    // always attacking in southeast/southwest
                    rs.add(destrow);
                    cs.add(destcol);
                }
            }
        }
        return new Liberties(rs, cs);
    }
    
    private static Liberties knightmoves(Game g, int startrow, int startcol) {
        ArrayList<Integer> rs = new ArrayList<>();
        ArrayList<Integer> cs = new ArrayList<>();
        
        int[] rsincs = {-2, -2, -1, -1, 1, 1, 2, 2};
        int[] csincs = {-1, 1, -2, 2, -2, 2, -1, 1};
                
        for (int i = 0; i < 8; i++) {
            if(inbounds(startrow + rsincs[i], startcol + csincs[i]) 
                    && !isblocked(g, startrow + rsincs[i], startcol + csincs[i], startrow, startcol)) {
                rs.add(startrow + rsincs[i]);
                cs.add(startcol + csincs[i]);
            }
        }
        
        return new Liberties(rs, cs);
        
    }
    
    // Performs and concatenates liberties of multiple ray tracings or adjacent tracings
    private static Liberties multitrace(Game g, char typ, int[] dirs, int startrow, int startcol, boolean isray) {
        ArrayList<Integer> rs = new ArrayList<>();
        ArrayList<Integer> cs = new ArrayList<>();

        Liberties ls = new Liberties(rs, cs);
        for (int i = 0; i < dirs.length; i++) {
            if(isray)
                ls.addAll(raytrace(g, typ, dirs[i], startrow, startcol));
            else
                ls.addAll(adj8trace(g, typ, dirs[i], startrow, startcol));
        }
        
        return ls;
    }
    
    private static boolean iscapture(Game g, int drow, int dcol, int srow, int scol) {
        // Destination is colorful and has opposite color as start
        return g.gameboard.piecematrix[drow][dcol].color != 0
                && (g.gameboard.piecematrix[drow][dcol].color
                    != g.gameboard.piecematrix[srow][scol].color);
    }
    
    // Check for blockage: Does not do board bounds checking
    private static boolean isblocked(Game g, int drow, int dcol, int srow, int scol) {
        
        // Blocked by same color piece
        if(g.gameboard.piecematrix[drow][dcol].color == g.gameboard.piecematrix[srow][scol].color) 
            return true;
        // Blocked by hole
        else if(g.gameboard.containsstatusat(drow, dcol, "Hole"))
            return true;
        
        return false;
    }
    
    private static boolean canplacepiece(Game g, int row, int col) {
        return g.gameboard.piecematrix[row][col].type == 'e' 
                && !g.gameboard.containsstatusat(row, col, "Hole");
    }
    
    private static Point dirtoincs(int DIR) {
        int rowinc = 0, colinc = 0;
        switch(DIR) {
            case NORT:
                rowinc = -1;
                break;
            case SOUT:
                rowinc = +1;
                break;
            case EAST:
                colinc = -1;
                break;
            case WEST:
                colinc = +1;
                break;
            case NORE:
                rowinc = -1;
                colinc = +1;
                break;
            case NORW:
                rowinc = -1;
                colinc = -1;
                break;
            case SOUE:
                rowinc = +1;
                colinc = +1;
                break;
            case SOUW:
                rowinc = +1;
                colinc = -1;
                break;
        }
        
        return new Point(rowinc, colinc);
    }
      
    private static boolean inbounds(int row, int col) {
        return (row >= 0 && row <= 4) && (col >= 0 && col <= 4);
    }
    
    // Is destination under attack from non turn player
    public static boolean isdangerous(Game g, int destrow, int destcol, int startrow, int startcol) {
        Piece tmp = g.gameboard.move(destrow, destcol, startrow, startcol);
        int incheck = incheck(g);
        if (incheck == wbtocolor(g.turnplayer.iswhite)) {
            g.gameboard.move(startrow, startcol, destrow, destcol);
            g.gameboard.piecematrix[destrow][destcol] = tmp;
            return true;
        } else {
            g.gameboard.move(startrow, startcol, destrow, destcol);
            g.gameboard.piecematrix[destrow][destcol] = tmp;
            return containsadjacent(g, destrow, destcol, 'k', startrow, startcol);
        }
    }
    
    public static boolean containsadjacent(Game g, int row, int col, char c, int startrow, int startcol) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0)
                    && inbounds(row + i, col + j)
                    && !(row + i == startrow && col + j == startcol)
                    && (g.gameboard.piecematrix[row + i][col + j].type == c)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    static final int NORT = 1;
    static final int SOUT = 2;
    static final int EAST = 3;
    static final int WEST = 4;
    static final int NORE = 5;
    static final int SOUE = 6;
    static final int NORW = 7;
    static final int SOUW = 8;
}
