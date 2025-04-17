package com.shacky.materialmanagement.controller;

import com.shacky.materialmanagement.entity.Comment;
import com.shacky.materialmanagement.entity.Customer;
import com.shacky.materialmanagement.service.CommentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/post")
    public String postComment(@RequestParam("content") String content,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        Customer customer = (Customer) session.getAttribute("loggedInCustomer");

        if (customer != null && content != null && !content.trim().isEmpty()) {
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setCustomer(customer);
            commentService.saveComment(comment);
            redirectAttributes.addFlashAttribute("successMessage", "Your comment has been posted successfully!");
        }

        return "redirect:/orders/my-orders";
    }

    @GetMapping("/comments")
    public String showAllComments(Model model) {
        model.addAttribute("comments", commentService.getAllComments());
        return "services"; // assuming the comments are displayed on the services page
    }
    @PostMapping("/{id}/like")
    @ResponseBody
    public String likeComment(@PathVariable("id") Long commentId) {
        Comment comment = commentService.getCommentById(commentId);
        if (comment != null) {
            comment.incrementLikes();
            commentService.saveComment(comment);
            return String.valueOf(comment.getLikes()); // return new like count
        }
        return "0";
    }

}

