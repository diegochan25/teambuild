package org.typecrafters.teambuild.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.typecrafters.teambuild.entity.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByJsessionid(String jsessionid);
}
