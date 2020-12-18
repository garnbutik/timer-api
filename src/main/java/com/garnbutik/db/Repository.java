package com.garnbutik.db;

import com.garnbutik.model.Project;
import com.garnbutik.model.TimeRegistration;
import com.garnbutik.model.User;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

public class Repository {

    @PersistenceContext
    private EntityManager entityManager;

    public User saveUser(User user) {
        if (user.getId() != null) {
            entityManager.merge(user);
            return user;
        }
        entityManager.persist(user);
        return user;
    }

    public User findUserByUsernameOrEmail(String searchParams) {
        TypedQuery<User> query =
                entityManager.createQuery("SELECT u FROM User u WHERE u.username = :searchParams OR u.email = :searchParams", User.class);
        query.setParameter("searchParams", searchParams);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    public User findUserById(Long id) {
        return entityManager.find(User.class, id);
    }

    public TimeRegistration saveTimeRegistration(TimeRegistration timeRegistration) {
        entityManager.persist(timeRegistration);
        return timeRegistration;
    }

    public List<TimeRegistration> findTimeRegistrationsByUsername(String username) {
        TypedQuery<TimeRegistration> typedQuery = entityManager
                        .createQuery("SELECT tr FROM TimeRegistration tr " +
                                "INNER JOIN User u ON u.id = tr.user.id " +
                                "where u.username =  :username", TimeRegistration.class);
        typedQuery.setParameter("username", username);
        return typedQuery.getResultList();
    }

    public List<TimeRegistration> findTimeRegistrationsByUsernameWithDateFilter(
            String username, LocalDate dateFrom, LocalDate dateTo) {
        TypedQuery<TimeRegistration> typedQuery = entityManager
                        .createQuery("SELECT tr FROM TimeRegistration tr " +
                                "INNER JOIN User u ON u.id = tr.user.id " +
                                "where u.username =  :username AND (tr.workDate >= :dateFrom AND tr.workDate <= :dateTo)", TimeRegistration.class);
        typedQuery.setParameter("username", username);
        typedQuery.setParameter("dateFrom", dateFrom);
        typedQuery.setParameter("dateTo", dateTo);

        return typedQuery.getResultList();
    }

    public void deleteTimeRegistration(Long id) {
        TimeRegistration timeRegistration = entityManager.find(TimeRegistration.class, id);
        entityManager.remove(timeRegistration);
    }

    public Project findProjectById(Long id) {
        return entityManager.find(Project.class, id);
    }
}
