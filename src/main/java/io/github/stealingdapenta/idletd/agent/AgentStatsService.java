package io.github.stealingdapenta.idletd.agent;

import io.github.stealingdapenta.idletd.agent.mainagent.MainAgentStats;
import io.github.stealingdapenta.idletd.agent.mainagent.MainAgentStatsService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AgentStatsService {

    private final AgentService agentService;
    private final MainAgentStatsService mainAgentStatsService;


    public void saveMainAgentStats(AgentStats agentStats) {
        AgentType agentType = getAgentType(agentStats.getAgentId());

        switch (agentType) {
            case MAIN_AGENT -> mainAgentStatsService.saveMainAgentStats((MainAgentStats) agentStats);
        }

        // todo extend for all agent types
    }

    private AgentType getAgentType(int agentId) {
        Agent agent = agentService.getAgent(agentId);
        return agent.getAgentType();
    }

    public MainAgentStats getMainAgentStats(int agentId) {
        AgentType agentType = getAgentType(agentId);

        return switch (agentType) {
            case MAIN_AGENT -> mainAgentStatsService.getMainAgentStats(agentId);
            case MELEE -> null;
            case RANGED -> null;
            case MAGE -> null;
            case NECROMANCER -> null;
        };
    }

    public void updateMainAgentStats(AgentStats agentStats) {
        AgentType agentType = getAgentType(agentStats.getAgentId());

        switch (agentType) {
            case MAIN_AGENT -> mainAgentStatsService.updateMainAgentStats((MainAgentStats) agentStats);
        }
    }

    public void deleteMainAgentStats(int agentId) {
        AgentType agentType = getAgentType(agentId);

        switch (agentType) {
            case MAIN_AGENT -> mainAgentStatsService.deleteMainAgentStats(agentId);

        }
    }
}
