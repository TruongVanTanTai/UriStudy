package com.tantai.uristudy.controller;

import com.tantai.uristudy.dto.ProportionStatistic;
import com.tantai.uristudy.repository.FlashCardSetRepository;
import com.tantai.uristudy.service.StatisticService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class StatisticController {
    StatisticService statisticService;
    private final FlashCardSetRepository flashCardSetRepository;

    @GetMapping("/statistic/overview")
    public String showStatisticOverview(Model model) {
        Map<String, Double> map = statisticService.getStatisticOverview();

        List<String> labels = new ArrayList<String>();
        labels.add("Tổng số người dùng");
        labels.add("Tổng số bộ flash card");
        labels.add("Tổng số flash card");
        labels.add("Trung bình số bộ flash card trên mỗi người");
        labels.add("Trung bình số flash card trên bộ flash card");
        model.addAttribute("labels", labels);

        model.addAttribute("values", new ArrayList<Double>(map.values()));

        return "statistic-overview";
    }

    @GetMapping("/statistic/proportion")
    public String showStatisticByShareStatus(@RequestParam("mode") String mode, Model model) {
        List<ProportionStatistic> proportionStatistics;

        if (mode.equals("share-status")) {
            proportionStatistics = statisticService.getStatisticByShareStatus();
        }
        else {
            proportionStatistics = statisticService.getStatisticByType();
        }

        List<String>  labels = new ArrayList<>();
        List<Integer> quantity = new ArrayList<>();

        for (ProportionStatistic proportionStatistic : proportionStatistics) {
            labels.add(proportionStatistic.getField());
            quantity.add(proportionStatistic.getQuantity());
        }

        model.addAttribute("labels", labels);
        model.addAttribute("values", quantity);
        model.addAttribute("mode", mode);

        return "statistic-proportion";
    }
}
