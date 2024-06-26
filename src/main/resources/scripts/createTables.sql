-- Create the PLOT table with unique constraint on PLAYERUUID
create TABLE IF NOT EXISTS PLOT (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    STARTX INT NOT NULL,
    STARTZ INT NOT NULL,
    PLAYERUUID VARCHAR(36) NOT NULL UNIQUE);


-- Insert Default value if needed
insert into PLOT(STARTX, STARTZ, PLAYERUUID) values (0, 0, 'SERVER');
COMMIT;


-- Create the IDLE_PLAYER table
create TABLE IF NOT EXISTS IDLE_PLAYER (
    PLAYERUUID VARCHAR(36) PRIMARY KEY NOT NULL,
    BALANCE DOUBLE NOT NULL,
    FK_PLOT INT,
    FOREIGN KEY (FK_PLOT) REFERENCES PLOT(ID)
);


-- Create the TowerDefense game table
create TABLE IF NOT EXISTS TOWER_DEFENSE (
    PLAYERUUID VARCHAR(36) PRIMARY KEY NOT NULL,
    FK_PLOT INT NOT NULL,
    STAGE_LEVEL INT NOT NULL,
    FOREIGN KEY (PLAYERUUID) REFERENCES IDLE_PLAYER(PLAYERUUID) ON delete CASCADE,
    FOREIGN KEY (FK_PLOT) REFERENCES PLOT(ID) ON delete CASCADE
);

-- Create Skin table
create TABLE IF NOT EXISTS SKIN (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    NAME VARCHAR(50),
    DESCRIPTION VARCHAR(1000),
    DATA_TOKEN VARCHAR(1000),
    SIGNATURE_TOKEN VARCHAR(1000)
);

-- Create location table
create TABLE IF NOT EXISTS LOCATION (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    WORLD_NAME VARCHAR(50) NOT NULL,
    LOCATION_X DOUBLE NOT NULL,
    LOCATION_Y DOUBLE NOT NULL,
    LOCATION_Z DOUBLE NOT NULL,
    LOCATION_YAW FLOAT NOT NULL,
    LOCATION_PITCH FLOAT NOT NULL
);

-- Create Agent table
create TABLE IF NOT EXISTS AGENT (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    PLAYERUUID VARCHAR(36) NOT NULL,
    FK_LOCATION INT NOT NULL,
    ACTIVE_SKIN_ID INT NOT NULL,
    FOREIGN KEY (FK_LOCATION) REFERENCES LOCATION(ID) ON delete CASCADE,
    FOREIGN KEY (ACTIVE_SKIN_ID) REFERENCES SKIN (ID)
);

-- Create the BATTLE_STATS table
CREATE TABLE IF NOT EXISTS BATTLE_STATS (
    PLAYERUUID VARCHAR(36) PRIMARY KEY NOT NULL,
    MOVEMENT_SPEED DOUBLE NOT NULL,
    MAX_HEALTH DOUBLE NOT NULL,
    REGENERATION_PER_SECOND DOUBLE NOT NULL,
    OVERHEAL_SHIELD_LIMIT DOUBLE NOT NULL,
    OVERHEAL_SHIELD_REGEN_PER_SECOND DOUBLE NOT NULL,
    KNOCKBACK_RESISTANCE DOUBLE NOT NULL,
    SWORD_RESISTANCE DOUBLE NOT NULL,
    AXE_RESISTANCE DOUBLE NOT NULL,
    MAGIC_RESISTANCE DOUBLE NOT NULL,
    ARROW_RESISTANCE DOUBLE NOT NULL,
    TRIDENT_RESISTANCE DOUBLE NOT NULL,
    EXPLOSION_RESISTANCE DOUBLE NOT NULL,
    FIRE_RESISTANCE DOUBLE NOT NULL,
    POISON_RESISTANCE DOUBLE NOT NULL,
    CRITICAL_HIT_RESISTANCE DOUBLE NOT NULL,
    BLOCK_CHANCE DOUBLE NOT NULL,
    ATTACK_POWER DOUBLE NOT NULL,
    ATTACK_RANGE DOUBLE NOT NULL,
    ATTACK_KNOCKBACK DOUBLE NOT NULL,
    ATTACK_SPEED DOUBLE NOT NULL,
    PROJECTILE_SPEED DOUBLE NOT NULL,
    CRITICAL_HIT_CHANCE DOUBLE NOT NULL,
    CRITICAL_HIT_DAMAGE_MULTIPLIER DOUBLE NOT NULL,
    FOREIGN KEY (PLAYERUUID) REFERENCES IDLE_PLAYER(PLAYERUUID)
    );


-- Create the MAIN_AGENT_STATS table
CREATE TABLE IF NOT EXISTS MAIN_AGENT_STATS (
                                                ID INT AUTO_INCREMENT PRIMARY KEY,
                                                MAX_HEALTH DOUBLE NOT NULL,
                                                REGENERATION_PER_SECOND DOUBLE NOT NULL,
                                                OVERHEAL_SHIELD_LIMIT DOUBLE NOT NULL,
                                                OVERHEAL_SHIELD_REGEN_PER_SECOND DOUBLE NOT NULL,
                                                SWORD_RESISTANCE DOUBLE NOT NULL,
                                                AXE_RESISTANCE DOUBLE NOT NULL,
                                                MAGIC_RESISTANCE DOUBLE NOT NULL,
                                                ARROW_RESISTANCE DOUBLE NOT NULL,
                                                TRIDENT_RESISTANCE DOUBLE NOT NULL,
                                                EXPLOSION_RESISTANCE DOUBLE NOT NULL,
                                                FIRE_RESISTANCE DOUBLE NOT NULL,
                                                POISON_RESISTANCE DOUBLE NOT NULL,
                                                CRITICAL_HIT_RESISTANCE DOUBLE NOT NULL,
                                                BLOCK_CHANCE DOUBLE NOT NULL,
                                                ATTACK_POWER DOUBLE NOT NULL,
                                                ATTACK_RANGE DOUBLE NOT NULL,
                                                ATTACK_KNOCKBACK DOUBLE NOT NULL,
                                                ATTACK_SPEED DOUBLE NOT NULL,
                                                PROJECTILE_SPEED DOUBLE NOT NULL,
                                                CRITICAL_HIT_CHANCE DOUBLE NOT NULL,
                                                CRITICAL_HIT_DAMAGE_MULTIPLIER DOUBLE NOT NULL,
);








