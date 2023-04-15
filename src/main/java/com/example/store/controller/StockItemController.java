package com.example.store.controller;

import com.example.store.model.*;
import com.example.store.repository.CartItemRepository;
import com.example.store.repository.CartRepository;
import com.example.store.repository.PurchaseHistoryRepository;
import com.example.store.repository.UserRepository;
import com.example.store.service.StockItemService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class StockItemController {

    @Autowired
    private StockItemService stockItemService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private PurchaseHistoryRepository purchaseHistoryRepository;

    @GetMapping("")
    public String viewStockItems(Model model,
                                 @RequestParam(required = false) String category,
                                 @RequestParam(required = false) String manufacturer,
                                 @RequestParam(required = false) String title,
                                 @RequestParam(defaultValue = "title") String sortBy,
                                 @RequestParam(defaultValue = "asc") String sortDir) {

        List<StockItem> stockItemList;

        if (category != null || manufacturer != null || title != null) {
            // if search parameters are provided, search for matching StockItems
            stockItemList = stockItemService.searchStockItems(category, manufacturer, title, sortBy, sortDir);
        } else {
            // if no search parameters are provided, return all StockItems
            stockItemList = stockItemService.getAllStockItems(sortBy, sortDir);
        }

        model.addAttribute("stockItemList", stockItemList);
        return "user/stockItem-list";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam("stockItemId") Long stockItemId, HttpSession session, @AuthenticationPrincipal User user) {

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart(user);
        }

        StockItem stockItem = stockItemService.getStockItemById(stockItemId);
        if (stockItem == null) {
            return "redirect:/products";
        }

        CartItem cartItem = new CartItem();
        cartItem.setProductId(stockItem.getId());
        cartItem.setTitle(stockItem.getTitle());
        cartItem.setPrice(stockItem.getPrice());
        cartItem.setQuantity(1);

        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());
        User cartUser = userOptional.orElseThrow(() -> new RuntimeException("User not found"));
        cart.setUserId(cartUser);

        cart.addItem(cartItem);
        cartRepository.save(cart);

        session.setAttribute("cart", cart);

        return "redirect:/products";
    }

    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
        }

        // Retrieve the cart items from the database based on the user's ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<CartItem> cartItems = cartRepository.findAllByUserId(user.getId());

        // Add the cart items to the cart object
        for (CartItem cartItem : cartItems) {
            cart.addItem(cartItem);
        }

        // Update the session with the updated cart
        session.setAttribute("cart", cart);

        model.addAttribute("cart", cart);
        return "user/cart";
    }

    @PostMapping("/checkout")
    public String checkout(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.getItems().isEmpty()) {
            return "redirect:/user/products/cart";
        }

        // Decrease the inventory of each product in the cart
        for (CartItem item : cart.getItems()) {
            StockItem stockItem = stockItemService.getStockItemById(item.getProductId());
            if (stockItem != null) {
                stockItem.setInventoryNum(stockItem.getInventoryNum() - item.getQuantity());
                stockItemService.saveStockItem(stockItem);
            }
        }

        // Save the cart data to the purchase history table
        PurchaseHistory purchaseHistory = new PurchaseHistory();
        purchaseHistory.setUser(cart.getUserId());
        purchaseHistory.setCart(cart);
        purchaseHistoryRepository.save(purchaseHistory);

        // Delete the cart
        cartRepository.delete(cart);
        session.removeAttribute("cart");

        return "user/checkout-success";
    }
}