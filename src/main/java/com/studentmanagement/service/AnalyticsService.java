package com.studentmanagement.service;

import com.studentmanagement.entity.Score;
import com.studentmanagement.repository.ScoreRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class AnalyticsService {

    @Inject
    ScoreRepository scoreRepository;

    public Map<String, Long> getGradeDistribution() {
        List<Score> scores = scoreRepository.listAll();
        long a = 0, b = 0, c = 0, d = 0, f = 0;

        for (Score s : scores) {
            if (s.averageScore != null) {
                double avg = s.averageScore.doubleValue();
                if (avg >= 8.5) a++;
                else if (avg >= 7.0) b++;
                else if (avg >= 5.5) c++;
                else if (avg >= 4.0) d++;
                else f++;
            }
        }

        Map<String, Long> distribution = new HashMap<>();
        distribution.put("A", a);
        distribution.put("B", b);
        distribution.put("C", c);
        distribution.put("D", d);
        distribution.put("F", f);
        return distribution;
    }

    public Map<String, Object> getPassRate() {
        List<Score> scores = scoreRepository.listAll();
        long pass = 0;
        long fail = 0;

        for (Score s : scores) {
            if (s.averageScore != null) {
                if (s.averageScore.doubleValue() >= 4.0) {
                    pass++;
                } else {
                    fail++;
                }
            }
        }

        long total = pass + fail;
        double passPercentage = total == 0 ? 0.0 : ((double) pass / total) * 100.0;
        
        Map<String, Object> result = new HashMap<>();
        result.put("passCount", pass);
        result.put("failCount", fail);
        result.put("total", total);
        result.put("passPercentage", passPercentage);
        return result;
    }
}
