package io.github.stealingdapenta.idletd.service.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Schematic {
    TOWER_DEFENSE_SCHEMATIC("towerdefense.schem");

    private final String fileName;
}
