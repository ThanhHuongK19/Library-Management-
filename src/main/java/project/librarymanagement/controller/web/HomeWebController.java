package project.librarymanagement.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.librarymanagement.service.interfaces.IBooksService;

@Controller
public class HomeWebController {

    private final IBooksService booksService;

    public HomeWebController(IBooksService booksService) {
        this.booksService = booksService;
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute(
                "books",
                booksService.getAllBooks(0, 8, "bookId", "asc").getContent()
        );

        return "home";
    }
}