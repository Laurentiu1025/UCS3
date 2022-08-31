package ro.itschool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.itschool.entity.MyUser;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<MyUser, Long> {

    MyUser findByUsernameIgnoreCase(String username);

    MyUser findByEmail(String username);

    MyUser findByRandomToken(String randomToken);

    Optional<MyUser> findById(Long id);

    void deleteById(Long id);

    @Query(
            value = "SELECT * FROM my_user u WHERE u.username LIKE %:keyword% OR u.full_name LIKE %:keyword% OR u.email LIKE %:keyword% " +
                    "OR u.user_id LIKE %:keyword%",
            nativeQuery = true)
    List<MyUser> searchUser (@Param("keyword") String keyword);
}
