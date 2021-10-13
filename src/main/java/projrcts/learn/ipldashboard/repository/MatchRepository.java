package projrcts.learn.ipldashboard.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import projrcts.learn.ipldashboard.model.Match;

import java.util.List;

@Repository
public interface MatchRepository extends CrudRepository<Match, Long> {
    @Query("select m.team1, count(m) from Match m group by m.team1")
    List<Object[]> findMatchesPlayedByTeam1();

    @Query("select m.team2, count(m) from Match m group by m.team2")
    List<Object[]> findMatchesPlayedByTeam2();

    @Query("select m.matchWinner, count(m) from Match m group by m.matchWinner")
    List<Object[]> findMatchesWonByEachTeam();

    List<Match> findByTeam1OrTeam2OrderByDateDesc(String team1, String team2, Pageable pageable);

    default List<Match> findLatestMatchesForTeam(String teamName, int count) {
        Pageable pageable = PageRequest.of(1, count);
        return findByTeam1OrTeam2OrderByDateDesc(teamName, teamName, pageable);
    }
}
