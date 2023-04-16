package com.example.store.controller;

import com.example.store.model.*;
import com.example.store.repository.*;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class StockItemController {

    private final StockItemRepository stockItemRepository;
    private final UserRepository userRepository;
    private final PurchaseHistoryRepository purchaseHistoryRepository;

    public StockItemController(StockItemRepository stockItemRepository, UserRepository userRepository, PurchaseHistoryRepository purchaseHistoryRepository) {
        this.stockItemRepository = stockItemRepository;
        this.userRepository = userRepository;
        this.purchaseHistoryRepository = purchaseHistoryRepository;
    }

    // USER - GET LIST OF ITEMS
    @GetMapping
    public String viewProductList(@RequestParam(required = false) String category,
                                  @RequestParam(required = false) String manufacturer,
                                  @RequestParam(required = false) String title,
                                  @RequestParam(required = false, defaultValue = "title_asc") String order,
                                  Model model) {
        List<StockItem> stockItemList;

        if (category != null && !category.isEmpty()) {
            stockItemList = stockItemRepository.findByCategoryContainingIgnoreCase(category);
        } else if (manufacturer != null && !manufacturer.isEmpty()) {
            stockItemList = stockItemRepository.findByManufacturerContainingIgnoreCase(manufacturer);
        } else if (title != null && !title.isEmpty()) {
            stockItemList = stockItemRepository.findByTitleContainingIgnoreCase(title);
        } else {
            stockItemList = stockItemRepository.findAll();
        }

        switch (order) {
            case "title_asc":
                stockItemList.sort(Comparator.comparing(StockItem::getTitle));
                break;
            case "title_desc":
                stockItemList.sort(Comparator.comparing(StockItem::getTitle).reversed());
                break;
            case "manufacturer_asc":
                stockItemList.sort(Comparator.comparing(StockItem::getManufacturer));
                break;
            case "manufacturer_desc":
                stockItemList.sort(Comparator.comparing(StockItem::getManufacturer).reversed());
                break;
            case "price_asc":
                stockItemList.sort(Comparator.comparing(StockItem::getPrice));
                break;
            case "price_desc":
                stockItemList.sort(Comparator.comparing(StockItem::getPrice).reversed());
                break;
        }

        model.addAttribute("stockItemList", stockItemList);
        return "user/stockItem-list";
    }

    // ADD ITEMS TO CART
    @PostMapping("/addToCart")
    public String addToCart(@RequestParam("itemId") Long itemId, @RequestParam("quantity") int quantity, Principal principal) {

        // RETRIEVE CURRENT USER
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        // RETRIEVE STOCK ITEM
        Optional<StockItem> optionalStockItem = stockItemRepository.findById(itemId);
        if (!optionalStockItem.isPresent()) {
            return "redirect:/products";
        }
        StockItem stockItem = optionalStockItem.get();

        // CHECK IF CARTITEM ALREADY EXISTS
        Optional<CartItem> optionalCartItem = user.getCartItems().stream()
                .filter(item -> item.getProductId().equals(itemId))
                .findFirst();

        if (optionalCartItem.isPresent()) {
            // UPDATE IF CARTITEM EXISTS
            CartItem cartItem = optionalCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            // CREATE NEW IF CARTITEM DOES NOT EXIST
            CartItem cartItem = new CartItem(stockItem.getId(), stockItem.getTitle(), stockItem.getPrice(), quantity);
            user.addItemToCart(cartItem);
        }

        userRepository.save(user);

        return "redirect:/products";
    }

    // USER - GET CART PAGE
    @GetMapping("/cartPage")
    public String getCartPage(Model model, Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("cartItems", user.getCartItems());
        return "user/cart";
    }

    // USER - CHECKOUT CART, ADJUST INVENTORY NUM FOR ITEMS AND SAVE PURCHASE HISTORY
    @PostMapping("/checkout")
    public String checkoutCart(Principal principal) {

        // RETRIEVE CURRENT USER
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        // GET CART ITEMS + CALCULATE TOTAL PRICE
        List<CartItem> cartItems = user.getCartItems();
        double totalPrice = 0;
        for (CartItem cartItem : cartItems) {
            totalPrice += cartItem.getPrice() * cartItem.getQuantity();
        }

        // UPDATE INVENTORYNUM + SAVE PURCHASE HISTORY
        for (CartItem cartItem : cartItems) {
            Optional<StockItem> optionalStockItem = stockItemRepository.findById(cartItem.getProductId());
            if (optionalStockItem.isPresent()) {
                StockItem stockItem = optionalStockItem.get();
                stockItem.setInventoryNum(stockItem.getInventoryNum() - cartItem.getQuantity());
                stockItemRepository.save(stockItem);

                PurchaseHistory purchaseHistory = new PurchaseHistory(cartItem.getTitle(), cartItem.getQuantity(), cartItem.getPrice(), totalPrice, user);
                purchaseHistoryRepository.save(purchaseHistory);
            }
        }

        // DELETE CART ITEMS + SAVE USER
        user.getCartItems().clear();
        userRepository.save(user);

        return "user/checkout-success";
    }

    // ADMIN - VIEW STOCK LIST
    @GetMapping("/stockList")
    public String viewStockList(@RequestParam(required = false) String category,
                                @RequestParam(required = false) String manufacturer,
                                @RequestParam(required = false) String title,
                                @RequestParam(required = false, defaultValue = "title_asc") String order,
                                Model model) {
        List<StockItem> stockItemList;

        if (category != null && !category.isEmpty()) {
            stockItemList = stockItemRepository.findByCategoryContainingIgnoreCase(category);
        } else if (manufacturer != null && !manufacturer.isEmpty()) {
            stockItemList = stockItemRepository.findByManufacturerContainingIgnoreCase(manufacturer);
        } else if (title != null && !title.isEmpty()) {
            stockItemList = stockItemRepository.findByTitleContainingIgnoreCase(title);
        } else {
            stockItemList = stockItemRepository.findAll();
        }

        switch (order) {
            case "title_asc":
                stockItemList.sort(Comparator.comparing(StockItem::getTitle));
                break;
            case "title_desc":
                stockItemList.sort(Comparator.comparing(StockItem::getTitle).reversed());
                break;
            case "manufacturer_asc":
                stockItemList.sort(Comparator.comparing(StockItem::getManufacturer));
                break;
            case "manufacturer_desc":
                stockItemList.sort(Comparator.comparing(StockItem::getManufacturer).reversed());
                break;
            case "price_asc":
                stockItemList.sort(Comparator.comparing(StockItem::getPrice));
                break;
            case "price_desc":
                stockItemList.sort(Comparator.comparing(StockItem::getPrice).reversed());
                break;
        }

        model.addAttribute("stockItemList", stockItemList);
        return "admin/stockMgmt";
    }

    // ADMIN - UPDATE STOCK
    @PostMapping("/updateStock")
    public String updateStock(@RequestParam("itemId") Long itemId, @RequestParam("quantity") Integer quantity) {
        StockItem stockItem = stockItemRepository.findById(itemId).orElseThrow();
        stockItem.setInventoryNum(stockItem.getInventoryNum() + quantity);
        stockItemRepository.save(stockItem);
        return "redirect:/admin/stockMgmt";
    }
}