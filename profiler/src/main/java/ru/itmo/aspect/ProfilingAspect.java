package ru.itmo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LongSummaryStatistics;
import java.util.Map;

@Aspect
@Component
public class ProfilingAspect {
    private final Map<String, LongSummaryStatistics> methodStatistics = new HashMap<>();
    private final Package profilingPackage;

    public ProfilingAspect(ApplicationArguments arguments) {
        Package profilingPackage = ClassLoader.getSystemClassLoader().getDefinedPackage(
            arguments.getOptionValues("package").get(0)
        );
        if (profilingPackage == null) {
            throw new IllegalArgumentException("Have no access to provided package");
        }
        this.profilingPackage = profilingPackage;
    }

    @Around("execution(* *(..)) && !within(ru.itmo.ProfilingApplication) && !within(org.springframework..*)")
    public Object profileMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        String method = signature.toLongString();

        if (!signature.getDeclaringType().getPackage().getName().startsWith(profilingPackage.getName())) {
            return joinPoint.proceed(joinPoint.getArgs());
        }

        methodStatistics.computeIfAbsent(method, k -> new LongSummaryStatistics());

        Long startTime = System.nanoTime();
        Object result = joinPoint.proceed(joinPoint.getArgs());
        Long finishTime = System.nanoTime();

        methodStatistics.get(method).accept(finishTime - startTime);

        return result;
    }

    public Map<String, LongSummaryStatistics> getMethodStatistics() {
        return methodStatistics;
    }
}
