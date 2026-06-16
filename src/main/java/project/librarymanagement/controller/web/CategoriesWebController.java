package project.librarymanagement.controller.web;

import org.springframework.validation.BindingResult;
import project.librarymanagement.dto.request.CreateCategoryRequest;
import project.librarymanagement.dto.request.UpdateCategoryRequest;
import project.librarymanagement.entity.Categories;
import project.librarymanagement.exception.BadRequestException;
import project.librarymanagement.service.interfaces.ICategoriesService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoriesWebController {

    private final ICategoriesService categoriesService;

    public CategoriesWebController(ICategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    // Danh sách và Tìm kiếm
    @GetMapping
    public String listCategories(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Categories> categories;
        if (keyword != null && !keyword.isEmpty()) {
            categories = categoriesService.searchCategories(keyword); // Đảm bảo Service của bạn có hàm này
        } else {
            categories = categoriesService.getAllCategories();
        }
        model.addAttribute("categories", categories);
        model.addAttribute("newCategory", new CreateCategoryRequest());
        model.addAttribute("keyword", keyword);
        return "categories/list";
    }

    // Xử lý tạo mới
    @PostMapping("/add")
    public String createCategory(@Valid @ModelAttribute("newCategory") CreateCategoryRequest request,
                                 BindingResult result, Model model) {
        // 1. Kiểm tra lỗi validate cơ bản (VD: @NotBlank)
        if (result.hasErrors()) {
            model.addAttribute("categories", categoriesService.getAllCategories());
            return "categories/list";
        }

        // 2. Kiểm tra nghiệp vụ (trùng tên)
        try {
            categoriesService.createCategory(request);
            return "redirect:/categories?success";
        } catch (BadRequestException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("categories", categoriesService.getAllCategories());
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