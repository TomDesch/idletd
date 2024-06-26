package io.github.stealingdapenta.idletd.agent.mainagent;

import io.github.stealingdapenta.idletd.agent.Agent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MainAgentStatsService {

    private final MainAgentStatsRepository mainAgentStatsRepository;

    public void saveMainAgentStats(MainAgentStats mainAgentStats) {
        mainAgentStatsRepository.saveMainAgentStats(mainAgentStats);
    }

    public MainAgentStats getMainAgentStats(Agent agent) {
        return getMainAgentStats(agent.getId());
    }

    public MainAgentStats getMainAgentStats(long agentId) {
        return mainAgentStatsRepository.getMainAgentStats(agentId);
    }

    public void updateMainAgentStats(MainAgentStats mainAgentStats) {
        mainAgentStatsRepository.updateMainAgentStats(mainAgentStats);
    }
}
