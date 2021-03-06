/*
 * Copyright (C) 2016 Marco Willems
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package redrouter.data;

import java.util.List;
import java.util.Objects;
import redrouter.util.DVRange;
import redrouter.util.Range;

/**
 *
 * @author Marco Willems
 */
public abstract class Battler implements Cloneable {

//    public static final Battler NULL = new SingleBattler(null, 0, null);
    public static final Battler DUMMY = new SingleBattler(new Pokemon(null, 0, "Dummy Poke", Types.Type.NORMAL, null, 100, 100, 100, 100, 100, 100, ExperienceGroup.getExperienceGroup("Fast")), null, 5);
    protected Pokemon pokemon; // TODO: final!? -> evoluties?
    public final EncounterArea catchLocation;

    public Battler(Pokemon pokemon, EncounterArea catchLocation) {
        this.pokemon = pokemon;
        this.catchLocation = catchLocation;
    }

    @Override
    protected abstract Object clone() throws CloneNotSupportedException;
    
    public void defeatBattler(Battler b, int participants) {
        addStatXP(b.pokemon.hp, b.pokemon.atk, b.pokemon.def, b.pokemon.spd, b.pokemon.spc, participants);
        addXP(b.getExp(participants).getMin());
//        if (checkEvolve()) {
//            evolve(null);
//        }
    }

    // TODO: evolve condition (item, ...)
    public abstract void evolve(Item item);

    public abstract void addStatXP(int hp, int atk, int def, int spd, int spc, int nrOfPkmn);

    public abstract void resetStatXP();
    
    public abstract void addXP(int exp);
    
    /**
     * TODO
     * @return
     */
    public abstract boolean checkEvolve();

    public abstract List<Move> getMoveset();

    // TODO: range!
    public abstract int getLevel();

    public abstract DVRange getDVRange(int stat);

    public DVRange[] getDVRanges() {
        DVRange[] ranges = new DVRange[5];
        for (int s = 0; s < 5; s++) {
            ranges[s] = getDVRange(s);
        }
        return ranges;
    }

    public abstract Range getHP();

    public Range getAtk() {
        return getAtk(0, 0);
    }

    public Range getDef() {
        return getDef(0, 0);
    }

    public Range getSpd() {
        return getSpd(0, 0);
    }

    public Range getSpc() {
        return getSpc(0, 0);
    }

    public abstract Range getAtk(int badgeBoosts, int stage);

    public abstract Range getDef(int badgeBoosts, int stage);

    public abstract Range getSpd(int badgeBoosts, int stage);

    public abstract Range getSpc(int badgeBoosts, int stage);

    public abstract Range getHPStatIfDV(int DV);

    public abstract Range getAtkStatIfDV(int DV);

    public abstract Range getDefStatIfDV(int DV);

    public abstract Range getSpdStatIfDV(int DV);

    public abstract Range getSpcStatIfDV(int DV);

    // TODO: calculation with badge boosts & stages
    protected int getStat(int level, int base, int DV, int XP, int badgeBoosts, int stage) {
        double extraStats = 0;
        if (XP - 1 >= 0) {
            extraStats = Math.floor(Math.floor(Math.sqrt(XP - 1) + 1) / 4);
        }
        double statValue = Math.floor((((base + DV) * 2 + extraStats) * level / 100) + 5);
        return (int) statValue;
    }

    public Pokemon getPokemon() {
        return this.pokemon;
    }

    public boolean isType(Types.Type type) {
        return type == pokemon.type1 || (pokemon.type2 != null && type == pokemon.type2);
    }

    public abstract Range getExp(int participants);

    public abstract Range getLevelExp();

    @Override
    // TODO: NOT with hash codes!!
    public abstract boolean equals(Object obj);

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.pokemon);
        hash = 97 * hash + Objects.hashCode(this.catchLocation);
        return hash;
    }

    @Override
    public String toString() {
        String battler = pokemon.name + " Lv." + getLevel();
//        String battler = (this == NULL ? "-----" : pokemon.name + " Lv." + getLevel());
//        String moves = "";
//        if (moveset.size() > 0) {
//            for (Move m : moveset) {
//                moves += "," + m.NAME;
//            }
//            moves = moves.substring(1);
//        }
//        battler += " (" + moves + ")";

        return battler;
    }

}
