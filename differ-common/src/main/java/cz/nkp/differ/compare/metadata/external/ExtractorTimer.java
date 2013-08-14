package cz.nkp.differ.compare.metadata.external;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

public class ExtractorTimer {

    @Around("cz.nkp.differ.compare.metadata.external.ExternalMetadataExtractor.getMetadata()")
    public Object startAndStopTimer(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.nanoTime();
         System.out.println(startTime);
        Object retVal = pjp.proceed();
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        System.out.println(duration);

        return retVal;

    }
}
