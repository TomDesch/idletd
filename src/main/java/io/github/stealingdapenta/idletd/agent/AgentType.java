package io.github.stealingdapenta.idletd.agent;

import lombok.Getter;

import static io.github.stealingdapenta.idletd.Idletd.logger;

@Getter
public enum AgentType {
    MAIN_AGENT(1, "Hero", 1),
    MELEE(2, "Soldier", 2),
    RANGED(3, "Ranger", 2),
    MAGE(4, "Mage", 1),
    NECROMANCER(5, "Necromancer", 1);

    private final int typeId;
    private final String name;
    private final int allowedAmountPerPlayer;

    AgentType(int typeId, String name, int allowedAmountPerPlayer) {
        this.typeId = typeId;
        this.name = name;
        this.allowedAmountPerPlayer = allowedAmountPerPlayer;
    }

    public static AgentType getAgentTypeById(int typeId) {
        for (AgentType agentType : AgentType.values()) {
            if (agentType.getTypeId() == typeId) {
                return agentType;
            }
        }
        logger.warning("Error retrieving AgentType for ID " + typeId);
        return null;
    }

}
