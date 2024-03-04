package com.example.shoppingmall;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
  @Bean
  // JpaQueryFactory
  // : EntityManager를 받아서 Jpa를 사용해 DB를 조회하는 Querydsl 모듈 중 하나
  public JPAQueryFactory jpaQueryFactory(
    EntityManager entityManager
  ) {
    return new JPAQueryFactory(entityManager);
  }
}
