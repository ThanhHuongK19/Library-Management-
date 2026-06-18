package project.librarymanagement.controller.web;

import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import project.librarymanagement.dto.request.CreateCategoryRequest;
import project.librarymanagement.dto.request.UpdateCategoryRequest;
import project.librarymanagement.entity.Categories;
import project.librarymanagement.service.interfaces.ICategoriesService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/categories")
public class CategoriesWebController {

    private final ICategoriesService categoriesService;

    public CategoriesWebController(ICategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    // Danh sách và Tìm kiếm
    @GetMapping
    public String listCategories(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Page<Categories> categoriesPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            categoriesPage = categoriesService.searchCategories(keyword.trim(), page, size);
            model.addAttribute("keyword", keyword);
        } else {
            categoriesPage = categoriesService.getAllCategories(page, size);
        }

        model.addAttribute("categoriesPage", categoriesPage);
        model.addAttribute("newCategory", new CreateCategoryRequest());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);

        return "categories/list";
    }

    // Xử lý tạo mới
    @PostMapping("/add")
    public String createCategory(@Valid @ModelAttribute("newCategory") CreateCategoryRequest request,
                                 BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("categoriesPage", categoriesService.getAllCategories(0, 10));
            return "categories/list";
        }

        try {
            categoriesService.createCategory(request);
            return "redirect:/categories?success";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("categoriesPage", categoriesService.getAllCategories(0, 10));
            return "categories/list";
        }
    }

    // Hiển thị form sửa
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("category", categoriesService.getCategoryById(id));
        return "categories/edit";
    }

    // Xử lý cập nhật
    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable Long id, @Valid @ModelAttribute("category") UpdateCategoryRequest request) {
        categoriesService.updateCategory(id, request);
        return "redirect:/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoriesService.deleteCategory(id);
        return "redirect:/categories";
    }
}