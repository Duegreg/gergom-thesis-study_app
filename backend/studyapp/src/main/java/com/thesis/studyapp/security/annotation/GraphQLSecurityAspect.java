package com.thesis.studyapp.security.annotation;


import com.thesis.studyapp.exception.UnauthorizedException;
import com.thesis.studyapp.security.SecurityContextUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(1)
@RequiredArgsConstructor
public class GraphQLSecurityAspect {

    private final SecurityContextUtil securityContextUtil;
    private final Logger logger = LoggerFactory.getLogger(GraphQLSecurityAspect.class);

    /**
     * All graphQLResolver methods can be called only by authenticated user.
     * Exclusions are named in Pointcut expression.
     */
    @Before("isDefinedInApplication() && allGraphQLResolverMethods() && !notAuthenticatedAnnotation()")
    public void isAuthenticated() throws Throwable {
        if (!securityContextUtil.isAuthenticated()) {
            logger.error("Unauthorized request");
            throw new UnauthorizedException("Not authenticated");
        }
    }

    /**
     * Matches all beans that implement {@link com.coxautodev.graphql.tools.GraphQLResolver}
     * note: {@code GraphQLMutationResolver}, {@code GraphQLQueryResolver} etc
     * extend base GraphQLResolver interface
     */
    @Pointcut("target(com.coxautodev.graphql.tools.GraphQLResolver)")
    private void allGraphQLResolverMethods() {
    }

    /**
     * Matches all beans in com.mi3o.springgraphqlsecurity package
     * resolvers must be in this package (subpackages)
     */
    @Pointcut("within(com.thesis.studyapp..*)")
    private void isDefinedInApplication() {
    }

    /**
     * Exact method signature which will be excluded from security check
     */
    @Pointcut("@annotation(com.thesis.studyapp.security.annotation.Authenticated)")
    private void authenticatedAnnotation() {
    }

    @Pointcut("@annotation(com.thesis.studyapp.security.annotation.NotAuthenticated)")
    private void notAuthenticatedAnnotation() {
    }


}