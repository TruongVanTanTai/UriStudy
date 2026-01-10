package com.tantai.uristudy.service;

import com.tantai.uristudy.dto.ProportionStatistic;
import com.tantai.uristudy.repository.FlashCardRepository;
import com.tantai.uristudy.repository.FlashCardSetRepository;
import com.tantai.uristudy.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class StatisticService {
    UserRepository userRepository;
    FlashCardSetRepository flashCardSetRepository;
    FlashCardRepository flashCardRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    public Map<String, Double> getStatisticOverview() {
        Map<String, Double> map = new HashMap<>();
        map.put("totalUsers", (double) userRepository.count());
        map.put("totalFlashCardSets", (double) flashCardSetRepository.count());
        map.put("totalFlashCards", (double) flashCardRepository.count());
        map.put("averageFlashCardSetPerUser", (double) map.get("totalFlashCardSets") / map.get("totalUsers"));
        map.put("averageFlashCardPerFlashCardSet", (double) map.get("totalFlashCards") / map.get("totalFlashCardSets"));
        return map;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public List<ProportionStatistic> getStatisticByShareStatus() {
        List<Object[]> statistic = flashCardSetRepository.statisticByShareStatus();
        List<ProportionStatistic> shareStatusStatistics = new ArrayList<>();
        for (Object[] obj : statistic){
            shareStatusStatistics.add(ProportionStatistic.builder()
                            .field((String) obj[0])
                            .quantity((Integer) obj[1])
                            .build());
        }
        return shareStatusStatistics;
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<ProportionStatistic> getStatisticByType() {
        List<Object[]> statistic = flashCardSetRepository.statisticType();
        List<ProportionStatistic> typeStatistics = new ArrayList<>();
        for (Object[] obj : statistic){
            typeStatistics.add(ProportionStatistic.builder()
                            .field((String) obj[0])
                            .quantity((Integer) obj[1])
                            .build());
        }
        return typeStatistics;
    }
}
