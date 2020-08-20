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
public class Board {
    public Piece[][] piecematrix = new Piece[5][5];
    public ArrayList<TileStatus>[][] statusesmatrix = new ArrayList[5][5];
    
    private final String[] defaultboard = {"eekee",
                                           "epppe",
                                           "eeeee",
                                           "epppe",
                                           "eekee"};
    
    public Board() {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                // set the top of the board to black's pieces
                char pce = defaultboard[row].charAt(col);
                int clr;
                if(row < 2 && pce != 'e') {
                    clr = -1;
                }
                else if(row > 2 && pce != 'e') {
                    clr = 1;
                }
                else {
                    clr = 0;
                }
                
                piecematrix[row][col] = new Piece(clr, pce);
                
                // Normal board
                statusesmatrix[row][col] = new ArrayList<>();
            }
        }
    }
        
    public void flipmodel() {
        Piece[][] temppiecematrix = new Piece[5][5];
        ArrayList<TileStatus>[][] tempstatusesmatrix = new ArrayList[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                tempstatusesmatrix[i][j] = new ArrayList<>();
            }
        }
        
        for (int i = 0; i < 5; i++) {
            System.arraycopy(piecematrix[4 - i], 0, temppiecematrix[i], 0, 5);
            System.arraycopy(statusesmatrix[4 - i], 0, tempstatusesmatrix[i], 0, 5);
        }
        
        piecematrix = temppiecematrix;
        statusesmatrix = tempstatusesmatrix;
    }

    // Returns original piece in destination
    public Piece move(int drow, int dcol, int srow, int scol) {
        Piece tmp = piecematrix[drow][dcol];
        piecematrix[drow][dcol] = piecematrix[srow][scol];
        piecematrix[srow][scol] = new Piece(0, 'e');
        return tmp;
    }
    
    public Integer[] endingtilestatuses(int i, int j) {
        ArrayList<Integer> finisheds = new ArrayList<>();
        
        for (TileStatus ts : statusesmatrix[i][j]) {
            if(ts.passturn())
                finisheds.add(statusesmatrix[i][j].indexOf(ts));
        }
        
        return finisheds.toArray(new Integer[0]);
    }
    
    public void healat(int i, int j, String tname) {
        for (int k = 0; k < statusesmatrix[i][j].size(); k++)
            if(tname.equals(statusesmatrix[i][j].get(k).name))
                statusesmatrix[i][j].remove(k);
    }
        
    public boolean containsstatusat(int i, int j, String tname) {
        for (TileStatus ts : statusesmatrix[i][j])
            if(tname.equals(ts.name))
                return true;
        return false;
    }
}
