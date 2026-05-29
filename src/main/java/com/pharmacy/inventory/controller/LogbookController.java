package com.pharmacy.inventory.controller;

import com.pharmacy.inventory.repository.ReceivingLogbookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/warehouse/logbook")
@RequiredArgsConstructor
public class LogbookController {

    private final ReceivingLogbookRepository logbookRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("logs", logbookRepository.findAllByOrderByLogDateDesc());
        return "warehouse/logbook-list";
    }
}
