package com.example.demo;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ShoppingController {

    private final ShoppingItemRepository repository;

    public ShoppingController(ShoppingItemRepository repository) {
        this.repository = repository;
    }

    // 一覧表示
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("items",
                repository.findAllByOrderByDisplayOrderAsc());
        return "items";
    }

    // 追加
    @PostMapping("/add")
    public String add(@RequestParam String name,
                      @RequestParam Integer quantity) {

        int nextOrder = repository.findAll().size();

        ShoppingItem item = new ShoppingItem();
        item.setName(name);
        item.setQuantity(quantity);
        item.setDisplayOrder(nextOrder);
        item.setPurchased(false);

        repository.save(item);

        return "redirect:/";
    }

    // 削除
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/";
    }

    // チェック切り替え
    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id) {

        ShoppingItem item =
                repository.findById(id).orElseThrow();

        item.setPurchased(!item.getPurchased());

        repository.save(item);

        return "redirect:/";
    }

    // 並び替え保存
    @PostMapping("/reorder")
    @ResponseBody
    public void reorder(@RequestBody List<Long> ids) {

        for (int i = 0; i < ids.size(); i++) {
            ShoppingItem item =
                    repository.findById(ids.get(i)).orElseThrow();

            item.setDisplayOrder(i);
            repository.save(item);
        }
    }
}