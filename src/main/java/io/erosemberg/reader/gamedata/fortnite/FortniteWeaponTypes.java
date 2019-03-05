package io.erosemberg.reader.gamedata.fortnite;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @author Erik Rosemberg
 * @since 23/12/2018
 */
@AllArgsConstructor
public enum FortniteWeaponTypes {
    ASSAULT_RIFLE(4, 260, "Assault Rifle"),
    PISTOL(2, 258, "Pistol"),
    SMG(5, 261, "Submachine Gun"),
    MINIGUN(14, 270, "Minigun"),
    SHOTGUN(3, 259, "Shotgun"),
    ROCKET_LAUNCHER(13, 269, "Rocket Launcher"),
    GRENADE_LAUNCHER(12, 268, "Grenade Launcher"),
    SNIPER_RIFLE(7, 263, "Sniper Rifle"),
    CROSS_BOW(15, 271, "Crossbow"),
    SWITCH_TEAMS(37, 293, "Switch Teams"),
    FALL_DAMAGE(1, 257, "Fall Damage"),
    RESPAWN(46, -2, "Respawn"),
    TRAP(16, 272, "Trap"),
    PICKAXE(8, 264, "Pickaxe"),
    VEHICLE(23, 279, "Vehicle"),
    BIPLANE_GUN(39, 295, "X-4 Fighter Wing"),
    TURRET(27, 283, "Turret"),
    STINK_NADE(25, 281, "Stink Grenade"),
    GRENADE(10, 266, "Grenade"),
    KICKED(31, -2, "Kicked from Party"),
    UNKNOWN(0, -2, "Unknown");

    private long id;
    private long knockId;
    @Getter
    private String humanName;

    /**
     * Checks if a weapon type matches the knock weapon types.
     * Apparently the weapons have different ids for the DBNO (down but not out) state.
     * <p>
     * If <code>true</code> is returned, the player was fully eliminated from the game.
     * If <code>false</code> is returned, the player was knocked out from the game.
     *
     * @param id the id of the weapon used
     */
    public static boolean isKnock(long id) {
        return Stream.of(values()).anyMatch(type -> type.id == id);
    }

    /**
     * Returns the weapon type if any match the id, if not, returns <code>UNKNOWN</code>
     *
     * @param id the id of the weapon.
     */
    public static FortniteWeaponTypes fromId(long id) {
        return Stream.of(values()).filter(type -> type.id == id || type.knockId == id).findAny().orElse(UNKNOWN);
    }
}
