package projrcts.learn.ipldashboard.controller;

import org.springframework.web.bind.annotation.*;
import projrcts.learn.ipldashboard.model.Match;
import projrcts.learn.ipldashboard.model.Team;
import projrcts.learn.ipldashboard.repository.MatchRepository;
import projrcts.learn.ipldashboard.repository.TeamRepository;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
public class TeamController {
    private TeamRepository teamRepository;
    private MatchRepository matchRepository;

    public TeamController(TeamRepository teamRepository, MatchRepository matchRepository) {
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
    }

    @GetMapping("/team/{teamName}")
    public Team getTeam(@PathVariable String teamName) {
        Team team = teamRepository.findByTeamName(teamName);
        team.setMatches(matchRepository.findLatestMatchesForTeam(teamName, 4));
        return team;
    }

    @GetMapping("/team/{teamName}/matches")
    public List<Match> getMatchesForTeamAndYear(@PathVariable("teamName") String teamName,
                                                @RequestParam("year") int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year + 1, 1, 1);
        return matchRepository.findByTeamAndDateBetween(teamName, startDate, endDate);
    }
}
