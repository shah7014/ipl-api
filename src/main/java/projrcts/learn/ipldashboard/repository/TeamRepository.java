package projrcts.learn.ipldashboard.repository;

import org.springframework.data.repository.CrudRepository;
import projrcts.learn.ipldashboard.model.Team;

public interface TeamRepository extends CrudRepository<Team, Long> {
    Team findByTeamName(String teamName);
}
