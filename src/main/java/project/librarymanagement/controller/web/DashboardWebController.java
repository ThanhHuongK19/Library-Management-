package project.librarymanagement.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.librarymanagement.service.interfaces.IDashboardService;

@Controller
@RequestMapping("/admin/dashboard")
public class DashboardWebController {
    @Autowired
    private IDashboardService dashboardService;

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("totalBooks", dashboardService.getTotalBooks());
        model.addAttribute("activeBorrows", dashboardService.getActiveBorrows());
        model.addAttribute("overdueRecords", dashboardService.getOverdueRecords());
        model.addAttribute("totalUsers", dashboardService.getTotalUsers());
        return "admin/dashboard";
    }
}
