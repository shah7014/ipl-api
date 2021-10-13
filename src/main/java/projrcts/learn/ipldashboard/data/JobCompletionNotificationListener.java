package projrcts.learn.ipldashboard.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;
import projrcts.learn.ipldashboard.model.Team;
import projrcts.learn.ipldashboard.repository.MatchRepository;
import projrcts.learn.ipldashboard.repository.TeamRepository;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final EntityManager em;
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;

    public JobCompletionNotificationListener(EntityManager entityManager, MatchRepository matchRepository, TeamRepository teamRepository) {
        this.em = entityManager;
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

            Map<String, Team> teams1ByName = matchRepository.findMatchesPlayedByTeam1()
                    .stream()
                    .map(res -> new Team((String) res[0], (long) res[1]))
                    .collect(Collectors.toMap(team -> team.getTeamName(), team -> team));

            Map<String, Team> teams2ByName = matchRepository.findMatchesPlayedByTeam2()
                    .stream()
                    .map(res -> new Team((String) res[0], (long) res[1]))
                    .collect(Collectors.toMap(team -> team.getTeamName(), team -> team));

            Map<String , Team> teamsByName = new HashMap<>();

            teams1ByName.keySet().forEach(teamName -> {
                Team team;
                if (teams2ByName.containsKey(teamName)) {
                    team = new Team(teamName, teams1ByName.get(teamName).getTotalMatches() + teams2ByName.get(teamName).getTotalMatches());
                } else {
                    team = teams1ByName.get(teamName);
                }
                teamsByName.put(teamName, team);
            });

            matchRepository.findMatchesWonByEachTeam()
                    .stream()
                    .forEach(res -> {
                        Team team = teamsByName.get((String) res[0]);
                        if(team != null) team.setTotalWins((long) res[1]);
                    });

            teamsByName.values().forEach(team -> teamRepository.save(team));

            teamRepository.findAll().forEach(team -> System.out.println(team));
        }
    }
}
