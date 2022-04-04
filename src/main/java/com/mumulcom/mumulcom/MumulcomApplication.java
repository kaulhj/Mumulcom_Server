package com.mumulcom.mumulcom;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;
import java.util.Locale;
import java.util.TimeZone;

@SpringBootApplication
// SpringBoot의 가장 기본적인 설정 선언.
// @Controller, @Service, @Repository 등의 Annotation 스캔 및 Bean 등록
// 사전의 정의한 라이브러리들을 Bean 등록
// Bean 간단 설명, 스프링 컨테이너가 관리하는 자바 객체
@EnableJpaAuditing
public class MumulcomApplication {

    @PostConstruct
    public void start() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        Locale.setDefault(Locale.KOREA);
    }

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "classpath:aws.yml,"
            + "classpath:fcm.yml";

    public static void main(String[] args) {

        new SpringApplicationBuilder(MumulcomApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);

        // 메모리 사용량 출력
        long heapSize = Runtime.getRuntime().totalMemory();
        System.out.println("HEAP Size(M) : "+ heapSize / (1024*1024) + " MB");
    }

}
