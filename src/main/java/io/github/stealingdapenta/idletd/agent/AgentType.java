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

    private final int id;
    private final String name;
    private final int allowedAmountPerPlayer;

    AgentType(int id, String name, int allowedAmountPerPlayer) {
        this.id = id;
        this.name = name;
        this.allowedAmountPerPlayer = allowedAmountPerPlayer;
    }

    public static AgentType getAgentTypeById(int id) {
        for (AgentType agentType : AgentType.values()) {
            if (agentType.getId() == id) {
                return agentType;
            }
        }
        logger.warning("Error retrieving AgentType for ID " + id);
        return null;
    }

}
