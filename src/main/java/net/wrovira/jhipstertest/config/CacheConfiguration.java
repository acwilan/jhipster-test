package net.wrovira.jhipstertest.config;

import com.github.benmanes.caffeine.jcache.configuration.CaffeineConfiguration;
import io.github.jhipster.config.JHipsterProperties;
import java.util.OptionalLong;
import java.util.concurrent.TimeUnit;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Caffeine caffeine = jHipsterProperties.getCache().getCaffeine();

        CaffeineConfiguration caffeineConfiguration = new CaffeineConfiguration();
        caffeineConfiguration.setMaximumSize(OptionalLong.of(caffeine.getMaxEntries()));
        caffeineConfiguration.setExpireAfterWrite(OptionalLong.of(TimeUnit.SECONDS.toNanos(caffeine.getTimeToLiveSeconds())));
        caffeineConfiguration.setStatisticsEnabled(true);
        jcacheConfiguration = caffeineConfiguration;
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, net.wrovira.jhipstertest.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, net.wrovira.jhipstertest.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, net.wrovira.jhipstertest.domain.User.class.getName());
            createCache(cm, net.wrovira.jhipstertest.domain.Authority.class.getName());
            createCache(cm, net.wrovira.jhipstertest.domain.User.class.getName() + ".authorities");
            createCache(cm, net.wrovira.jhipstertest.domain.Region.class.getName());
            createCache(cm, net.wrovira.jhipstertest.domain.Country.class.getName());
            createCache(cm, net.wrovira.jhipstertest.domain.Location.class.getName());
            createCache(cm, net.wrovira.jhipstertest.domain.Department.class.getName());
            createCache(cm, net.wrovira.jhipstertest.domain.Department.class.getName() + ".employees");
            createCache(cm, net.wrovira.jhipstertest.domain.Task.class.getName());
            createCache(cm, net.wrovira.jhipstertest.domain.Task.class.getName() + ".jobs");
            createCache(cm, net.wrovira.jhipstertest.domain.Employee.class.getName());
            createCache(cm, net.wrovira.jhipstertest.domain.Employee.class.getName() + ".jobs");
            createCache(cm, net.wrovira.jhipstertest.domain.Job.class.getName());
            createCache(cm, net.wrovira.jhipstertest.domain.Job.class.getName() + ".tasks");
            createCache(cm, net.wrovira.jhipstertest.domain.JobHistory.class.getName());
            // jhipster-needle-caffeine-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache == null) {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }
}
