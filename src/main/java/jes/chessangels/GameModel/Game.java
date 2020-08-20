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
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import static jes.chessangels.Controller.CommandHandler.wbtocolor;
import jes.chessangels.Controller.Liberties;
import static jes.chessangels.Controller.Liberties.generatelibertiesforpiece;
import jes.chessangels.Controller.Point;
import static jes.chessangels.GameModel.Piece.ischesspiece;

/**
 *
 * @author jes
 */
public class Game {
    
    public Player player1 = new Player();
    public Player player2 = new Player();
    
    public Player turnplayer = player1;
    public int turncounter = 1;

    private final int[] defaultdeck = {7, 7, 8, 8, 8, 9, 9, 9, 10, 10,
                                        2, 2, 2, 3, 4, 4, 5, 5, 6, 6};
    
    public Board gameboard = new Board();
    
    public Game() {
        
        player1.iswhite = true;
        player2.iswhite = false;
        
        // Generate permutations of default deck
        shuffle(defaultdeck);
        for (int i = 0; i < defaultdeck.length; i++) {
            player1.deck.add(new Card(defaultdeck[i]));
        }
        shuffle(defaultdeck);
        for (int i = 0; i < defaultdeck.length; i++) {
            player2.deck.add(new Card(defaultdeck[i]));
        }
        
        for (int i = 0; i < 4; i++) {
            player1.hand.add(player1.deck.remove(0));
            player2.hand.add(player2.deck.remove(0));
        }
        
    }
    
    public String toggleturnmodel() {
        String rtnmsg = "AOK";
        turncounter++;
        
        // Switch turnplayer pointer
        if(turnplayer == player1)
            turnplayer = player2;
        else
            turnplayer = player1;
        
        // Draw card for turn player
        if(turnplayer.deck.size() > 0)
            turnplayer.hand.add(turnplayer.deck.remove(0));
        else
            rtnmsg = "You couldn't draw...";
        
        // Full hand: Drop oldest card
        if(turnplayer.hand.size() > 7)
            rtnmsg = "Your <i>" + turnplayer.hand.remove(0).name + "</i> faded away...";
        
        turnplayer.wp = (int) ((turncounter + 1) / 2);
        // End-turn based wp increase caps at 10
        if(turnplayer.wp > 10)
            turnplayer.wp = 10;
        
        gameboard.flipmodel();
        return rtnmsg;
    }
    
    // Checks for any state of check 
    // (-1 for black's king, 0 for no check, 1 for white king)
    public static int incheck(Game g) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if(ischesspiece(g.gameboard.piecematrix[i][j].type) 
                        && g.gameboard.piecematrix[i][j].type != 'k') {
                    Liberties ls = generatelibertiesforpiece(g, i, j, true);
                    Point pnt = ls.containsaking(g);
                    // King contained in opposite colored liberties
                    if(pnt != null 
                            && (g.gameboard.piecematrix[i][j].color != 0)
                            && (g.gameboard.piecematrix[i][j].color 
                                != g.gameboard.piecematrix[pnt.row][pnt.col].color))
                        return g.gameboard.piecematrix[pnt.row][pnt.col].color;
                }
            }
        }
        return 0;
    }
    
    public String promote(int col) {
        Random rnd = ThreadLocalRandom.current();
        // Random piece 0, 1, 2 (knight, bishop, rook)
        char[] pieces = {'n', 'b', 'r'};
        int rp = rnd.nextInt(3);
        int clr = gameboard.piecematrix[0][col].color;
        gameboard.piecematrix[0][col] = new Piece(clr, pieces[rp]);

        String kind;        
        switch(pieces[rp]) {
            case 'n':
                kind = "Knight";
                break;
            case 'b':
                kind = "Bishop";
                break;
            case 'r':
                kind = "Rook";
                break;
            default:
                kind = "!";
                break;
        }
        return kind;
    }
    
    public String checknontargetingcondition(String cardname) {
        switch(cardname) {
            case "ANGEL Roar":
                for (int j = 0; j < 5; j++) {
                    if(ischesspiece(gameboard.piecematrix[2][j].type) 
                            && gameboard.piecematrix[2][j].color != wbtocolor(turnplayer.iswhite)
                            && gameboard.piecematrix[1][j].type == 'e'
                            && !gameboard.piecematrix[1][j].containsstatus("Hole"))
                        return "Able";
                }
                return "No enemies to blast-scare...";
            
        }
        return "Unable";
    }
    
    public String donontargetingeffect(String cardname) {
        switch(cardname) {
            case "ANGEL Roar":
                for (int j = 0; j < 5; j++) {
                    if(ischesspiece(gameboard.piecematrix[2][j].type) 
                            && gameboard.piecematrix[2][j].color != wbtocolor(turnplayer.iswhite)
                            && gameboard.piecematrix[1][j].type == 'e'
                            && !gameboard.piecematrix[1][j].containsstatus("Hole"))
                        gameboard.move(1, j, 2, j);
                }
                return "<i>EEEEEEYYYYYYYYRRRRAAAAAAAAAAA!!!</i>";
        }
        return "!";
    }
    
    public String[] statussweep() {
        ArrayList<String> endingupdates = new ArrayList<>();
        
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                
                // Decrement: Does status count down to zero
                Integer[] finishedtilestatuses = gameboard.endingtilestatuses(i, j);
                for (int k = 0; k < finishedtilestatuses.length; k++) {
                    TileStatus ts = gameboard.statusesmatrix[i][j].get(k);
                    endingupdates.add(ts.endmessage);
                    if(ts.name.equals("Entire Light") 
                            && ischesspiece(gameboard.piecematrix[i][j].type)) {
                        if (gameboard.piecematrix[i][j].type != 'k') {
                            endingupdates.add("The <i>" + gameboard.piecematrix[i][j].toString() + "</i> is consumed!");
                            gameboard.piecematrix[i][j] = new Piece(0, 'e');
                        } else if (gameboard.piecematrix[i][j].type == 'k') {
                            endingupdates.add("The <i>King</i> bathes in the light...");
                        }
                    }
                    gameboard.healat(i, j, ts.name);
                }
                
                // End all ending piece statuses
                Integer[] finishedpiecestatuses = gameboard.piecematrix[i][j].passturns();
                for (int k = 0; k < finishedpiecestatuses.length; k++) {
                    PieceStatus ps = gameboard.piecematrix[i][j].piecestatuses.get(k);
                    endingupdates.add(ps.endmessage);   
                    gameboard.piecematrix[i][j].heal(ps.name);
                }
            }
        }
        
        return endingupdates.toArray(new String[0]);
    }
    
    // Implement Fischer-Yates shuffle
    static void shuffle(int[] ar) {
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
    
}
