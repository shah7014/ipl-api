package projrcts.learn.ipldashboard.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import projrcts.learn.ipldashboard.model.Team;

import java.util.List;

public interface TeamRepository extends CrudRepository<Team, Long> {
    Team findByTeamName(String teamName);

    @Query("select t.teamName from Team t")
    List<String> findAllTeamNames();
}
