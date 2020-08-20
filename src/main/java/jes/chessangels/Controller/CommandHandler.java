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

import javax.swing.JPanel;
import static jes.chessangels.Controller.Lander.Land;
import static jes.chessangels.Controller.Liberties.generatelibertiesforcard;
import static jes.chessangels.Controller.Liberties.generatelibertiesforpiece;
import jes.chessangels.GameModel.Card;
import jes.chessangels.GameModel.Game;
import static jes.chessangels.GameModel.Game.incheck;
import jes.chessangels.GameModel.Piece;
import static jes.chessangels.GameModel.Piece.ischesspiece;
import jes.chessangels.View.BoardPanel;
import jes.chessangels.View.CAsFrame;
import jes.chessangels.View.HandPanel;
import jes.chessangels.View.HistPanel;

/**
 *
 * @author jes
 */
public class CommandHandler {

    private final CAsFrame al;
    private final Game g;
    private final BoardPanel bp;
    private final HandPanel hand1;
    private final HandPanel hand2;
    private final HistPanel history;
    private final JPanel mainpanel;
    
    public CommandHandler(CAsFrame alch, Game gch, 
            BoardPanel bpch, HandPanel hand1ch, HandPanel hand2ch, 
            HistPanel historych, JPanel mainpanelch) {
        al = alch;
        g = gch;
        bp = bpch;
        hand1 = hand1ch;
        hand2 = hand2ch;
        history = historych;
        mainpanel = mainpanelch;
    }

    // Flag for listener ot know when a card is currently trying to target a tile
    public static boolean targeting = false;
    public static int pendinghandind;
    
    public static boolean moving = false;
    public static int movesthisturn = 0;
    public int startrow;
    public int startcol;
    
    public int endturntries = 0;
    
    String rtnmsg;
    
    public void handle(String comm) {
        if(endturntries == 4)
            return;

        if (comm.equals("End Turn")) {
                        
            if(incheck == wbtocolor(g.turnplayer.iswhite)) {
                endturntries++;
                switch (endturntries) {
                    case 1:
                        history.write("<b><i>You need to rescue your King first!</i></b>");
                        break;
                    case 2:
                        history.write("<b><i>Your King is calling out to you!!</i></b>");
                        break;
                    case 3:
                        history.write("<b><i>If your turn ends now, you'll </i>p e r i s h. . .</b>");
                        break;
                    case 4:
                        history.removeAll();
                        history.write("It's so cold...");                        
                        freezegame();
                        break;
                }
                return;
            }
            
            // Toggle game model
            rtnmsg = g.toggleturnmodel();
            hand1.togglehand();
            hand2.togglehand();
            // Check for status updates
            String[] tileupdates = g.statussweep();
            
            for(String upmsg : tileupdates)
                history.futurewrite(upmsg);
            
            // Activate screensaver
            al.fillfog();
            
        } else if (comm.contains("Next")) { // Deactivate screensaver
            
            al.clearfog();
            
            // Flip and redraw hands
            hand1.drawcards(al, g);
            hand2.drawcards(al, g);
            
            movesthisturn = 0;
            
            // Flip and redraw board
            bp.draw(al, g.gameboard);
            
            // Update history (with past turn and status)
            history.removeAll(); 
            history.writefromfuture();
            history.write("You have <b>" + g.turnplayer.deck.size() + "</b> cards in your deck!");
            history.write("You have <b>" + g.turnplayer.wp + "</b> willpower to spend.");
            if(!rtnmsg.equals("AOK"))
                history.write(rtnmsg);
                        
        } else if (comm.contains("Tile")) {
            int row = Character.getNumericValue(comm.charAt(5));
            int col = Character.getNumericValue(comm.charAt(7));
            if (targeting) {
                Card landingc = g.turnplayer.hand.get(pendinghandind);
                Liberties ls = generatelibertiesforcard(g, landingc);
                // Successfully land card: Finish activation
                if (ls.contains(row, col)) {
                    Land(g, landingc, row, col);
                    finishactivation(pendinghandind);
                    targeting = false;
                } else { // Picked a non-marked tile
                    changeofplans();
                }
            } else {
                // Move piece
                if (moving) 
                    movepiecephase2(g, row, col);
                else if (g.gameboard.piecematrix[row][col].type != 'e')
                    movepiecephase1(g, row, col);
            }

        } else if (comm.contains("Card")) {
            int handind = Character.getNumericValue(comm.charAt(5));
            Card c = g.turnplayer.hand.get(handind);

            // Pick a different card after trying to target or move
            if (targeting || moving) {
                changeofplans();
            }

            if (g.turnplayer.wp >= c.cost) {
                if (c.doestarget) {
                    Liberties ls = generatelibertiesforcard(g, c);
                    if(ls.ps.length == 0) {
                        history.write("There's nowhere to land!");
                        return;
                    }
                    
                    history.write("Activating <i>" + c.name + "</i> for <b>" + c.cost + "</b> willpower.");
                    // Postpone activation until targeting complete
                    targeting = true;
                    pendinghandind = handind;
                    history.write("Hm... Where to land...");
                    
                    // Demarcate possible target tiles
                    bp.drawmarked(al, g.gameboard, ls);
                } else {
                    // No targets: Maybe finish activation of card immediately
                    String confirmation = g.checknontargetingcondition(c.name);
                    if(!confirmation.equals("Able")) { // Condition unfulfilled
                        history.write(confirmation);
                    } else { // Condition fulfilled: Finish activation
                        confirmation = g.donontargetingeffect(c.name);
                        history.write("Activating <i>" + c.name + "</i> for <b>" + c.cost + "</b> willpower.");
                        history.write(confirmation);
                        finishactivation(handind);
                    }
                }
            } else {
                history.write("You'd need <b>" + (c.cost - g.turnplayer.wp) + "</b> more willpower for that!");
            }
        } else if (comm.contains("Opp")) {
            history.write("Who knows what it could be?");
        }
    }
    
    // (-1 for black in check, 0 for no check, 1 for white in check)
    public int incheck;

    public void movepiecephase1(Game g, int row, int col) {
        
        if (g.gameboard.piecematrix[row][col].color != wbtocolor(g.turnplayer.iswhite)) {
            history.write("...you have no control over it!");
            return;
        }
        
        if(movesthisturn > 0) {
            history.write("You already moved this turn.");
            return;
        }
        
        if (g.gameboard.piecematrix[row][col].containsstatus("Frozen")) {
            history.write("It's... frozen solid!");
            return;
        }
        
        if (g.gameboard.containsstatusat(row, col, "Hole")) {
            history.write("A deep, dark, hole.");
            return;
        }
        
        Liberties ls = generatelibertiesforpiece(g, row, col, false);
        if(ls.ps.length == 0) {
            history.write("It can't move!");
            return;
        }
        
        bp.drawmarked(al, g.gameboard, ls);
        
        // Successfully begin move
        moving = true;
        startrow = row;
        startcol = col;
    }
        
    public void movepiecephase2(Game g, int row, int col) {
        
        Liberties ls = generatelibertiesforpiece(g, startrow, startcol, false);
        
        if(!ls.contains(row, col)) {
            changeofplans();
            return;
        }
        
        // If move places you in check (or still in check)
        Piece tmp = g.gameboard.move(row, col, startrow, startcol);
        incheck = incheck(g);
        if (incheck == wbtocolor(g.turnplayer.iswhite)) { // In check: Undo move and warn
            g.gameboard.move(startrow, startcol, row, col);
            g.gameboard.piecematrix[row][col] = tmp;
            history.write("<b><i>Your King is in too much danger!</i></b>");
            return;
        }
        
        // Has captured a piece
        if (tmp != null && ischesspiece(tmp.type)) {
            history.write("You captured the opponents <i>" + tmp.toString() + "</i>.");
            history.futurewrite("The opponent captured your <i>" + tmp.toString() + "</i>.");
        }
        
        if(g.gameboard.piecematrix[row][col].type == 'p' && row == 0) {
            String kind = g.promote(col);
            history.write("<b>A</b>utomatic <b>N</b>ightstar-<b>G</b>en: <b>E</b>ntire <b>L</b>ight! <i>" + kind + "</i>, go!");
            history.futurewrite("Your opponent activated the ANGEL!! A <i>" + kind + "</i> emerged!");
        }
        
        // Successfully end move
        incheck = incheck(g);
        moving = false;
        movesthisturn++;
        bp.draw(al, g.gameboard);
        endturntries = 0;
    }
    
    public void finishactivation(int handind) {
        Card c = g.turnplayer.hand.get(handind);        
        g.turnplayer.wp -= c.cost;
        history.futurewrite("Your opponent activated <i>" + c.name + "</i>.");
        history.write("You have <b>" + g.turnplayer.wp + "</b> willpower left.");
        g.turnplayer.hand.remove(handind);

        // Redraw both hands and board after card activation
        hand1.drawcards(al, g);
        hand2.drawcards(al, g);
        bp.draw(al, g.gameboard);
        
        // Update check information (Card could've saved king)
        incheck = incheck(g);
        endturntries = 0;
    }
    
    public void changeofplans() {
        history.write("Change of plans.");
        targeting = false;
        moving = false;
        bp.draw(al, g.gameboard);
        
        endturntries = 0;
    }
    
    public static int wbtocolor(boolean iswhite) {
        if(iswhite)
            return 1;
        else
            return -1;
        
    }

    private void freezegame() {
        hand2.setEnabled(false);
        hand2.setFocusable(false);
        bp.setEnabled(false);
        bp.setFocusable(false);
        hand1.setEnabled(false);
        hand1.setFocusable(false);
    }
}
