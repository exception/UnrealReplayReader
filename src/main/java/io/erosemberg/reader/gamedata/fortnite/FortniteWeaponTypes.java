package io.erosemberg.reader.gamedata.fortnite;

import lombok.AllArgsConstructor;

import java.util.stream.Stream;

/**
 * @author Erik Rosemberg
 * @since 23/12/2018
 */
@AllArgsConstructor
public enum FortniteWeaponTypes {
    ASSAULT_RIFLE(4),
    PISTOL(2),
    SMG(5),
    MINIGUN(14),
    SHOTGUN(3),
    ROCKET_LAUNCHER(13),
    GRENADE_LAUNCHER(12),
    SNIPER_RIFLE(7),
    CROSS_BOW(15),
    SWITCH_TEAMS(37),
    FALL_DAMAGE(1),
    RESPAWN(39),
    TRAP(16),
    PICKAXE(8),
    VEHICLE(23),
    BIPLANE_GUN(39),
    TURRET(27),
    STINK_NADE(25),
    GRENADE(10),
    UNKNOWN(0);

    private long id;

    public static FortniteWeaponTypes fromId(long id) {
        return Stream.of(values()).filter(type -> type.id == id).findAny().orElse(UNKNOWN);
    }
}
