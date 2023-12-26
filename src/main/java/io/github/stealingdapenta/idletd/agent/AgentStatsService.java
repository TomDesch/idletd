package io.github.stealingdapenta.idletd.agent;

import io.github.stealingdapenta.idletd.agent.mainagent.MainAgentStats;
import io.github.stealingdapenta.idletd.agent.mainagent.MainAgentStatsService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AgentStatsService {

    private final MainAgentStatsService mainAgentStatsService;


    public void saveAgentStats(AgentStats agentStats) {
        if (agentStats instanceof MainAgentStats mainAgentStats) {
            mainAgentStatsService.saveMainAgentStats(mainAgentStats);
        }
        // todo extend for all agent types
    }

    public AgentStats getAgentStats(Agent agent) {
        AgentType agentType = agent.getAgentType();

        return switch (agentType) {
            case MAIN_AGENT -> mainAgentStatsService.getMainAgentStats(agent.getId());
            case MELEE -> null;
            case RANGED -> null;
            case MAGE -> null;
            case NECROMANCER -> null;
        };
        // todo extend for all agent types

    }

    public void updateAgentStats(AgentStats agentStats) {
        if (agentStats instanceof MainAgentStats mainAgentStats) {
            mainAgentStatsService.updateMainAgentStats(mainAgentStats);
        }
        // todo extend for all agent types

    }

    public void deleteAgentStats(AgentStats agentStats) {
        if (agentStats instanceof MainAgentStats mainAgentStats) {
            mainAgentStatsService.deleteMainAgentStats(mainAgentStats.getAgentId());
        }
        // todo extend for all agent types
    }
}
