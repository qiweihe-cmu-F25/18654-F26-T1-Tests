package userprofile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import userprofile.domain.UserProfile;

/** Repository interacts with the database. Contains methods for CRUD operations and is typically an interface extending Spring Data JPA repositories.
 *  Spring Boot annotations:
 *  {@code @Repository} annotation is used to indicate that the class provides data persistence and retrieval operations. Spring Repository is very close to DAO pattern where DAO classes are responsible for providing CRUD operations on database tables.
 */
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    UserProfile findByUsername(String username);
}
