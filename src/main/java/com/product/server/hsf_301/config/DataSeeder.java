package com.product.server.hsf_301.config;

import com.product.server.hsf_301.blindBox.model.*;
import com.product.server.hsf_301.blindBox.repository.BlindBoxRepository;
import com.product.server.hsf_301.blindBox.repository.OrderItemRepository;
import com.product.server.hsf_301.blindBox.repository.OrderRepository;
import com.product.server.hsf_301.blindBox.repository.PrizeItemRepository;
import com.product.server.hsf_301.blindBox.repository.UserRepository;
import com.product.server.hsf_301.payment.TopUpRepository;
import com.product.server.hsf_301.payment.model.TopUpHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private TopUpRepository topUpRepository;
    
    // Add other repositories as needed
    @Autowired
    private UserRepository userRepository;
   //  @Autowired
   //  private BlogRepository blogRepository;
    @Autowired
    private BlindBoxRepository blindBoxRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private PrizeItemRepository prizeItemRepository;

    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting data seeding...");
        
        seedUsers();
        seedBlogs();
        seedBlindBoxes();
        seedPrizeItems();
//        seedOrders();
//        seedOrderItems();
        seedTopUpHistory();
        
        System.out.println("Data seeding completed!");
    }

    private void seedUsers() {
        System.out.println("Seeding Users...");
        
        // Example user seeding - adjust based on your User entity
        
        if (userRepository.count() > 0) {
            System.out.println("User data already exists, skipping...");
            return;
        }

        List<User> users = Arrays.asList(
            createUser("admin", "admin@example.com", "Admin", "User", "ADMIN", "password123"),
            createUser("john_doe", "john@example.com", "John", "Doe", "USER", "password123"),
            createUser("jane_smith", "jane@example.com", "Jane", "Smith", "USER", "password123"),
            createUser("bob_johnson", "bob@example.com", "Bob", "Johnson", "USER", "password123"),
            createUser("alice_brown", "alice@example.com", "Alice", "Brown", "USER", "password123"),
            createUser("charlie_wilson", "charlie@example.com", "Charlie", "Wilson", "MODERATOR", "password123")
        );

        userRepository.saveAll(users);
        System.out.println("Seeded " + users.size() + " users");
        
    }

    private void seedBlogs() {
        System.out.println("Seeding Blogs...");
        
        // Example blog seeding
        /*
        if (blogRepository.count() > 0) {
            System.out.println("Blog data already exists, skipping...");
            return;
        }

        List<Blog> blogs = Arrays.asList(
            createBlog("Welcome to Our Platform", 
                "We're excited to introduce our new mystery box platform where you can discover amazing prizes!",
                "This is the full content of our welcome blog post. Here you can learn about all the exciting features...",
                "PUBLISHED", 1L),
            
            createBlog("How to Open Your First Mystery Box", 
                "Learn the basics of opening mystery boxes and maximizing your chances of getting rare items.",
                "Step 1: Choose your mystery box carefully... Step 2: Cross your fingers... Step 3: Open and enjoy!",
                "PUBLISHED", 1L),
            
            createBlog("Rare Items Showcase", 
                "Check out some of the rarest items you can win from our premium mystery boxes.",
                "Our premium boxes contain incredible items including golden weapons, legendary armor, and exclusive cosmetics...",
                "PUBLISHED", 1L),
            
            createBlog("Tips for New Collectors", 
                "Essential tips for beginners who want to start their collection journey.",
                "Starting your collection can be overwhelming, but with these tips you'll be on your way to success...",
                "DRAFT", 2L),
            
            createBlog("Community Highlights", 
                "Featuring amazing collections from our community members.",
                "This month we're showcasing some incredible collections from our most dedicated users...",
                "PUBLISHED", 1L)
        );

        blogRepository.saveAll(blogs);
        System.out.println("Seeded " + blogs.size() + " blogs");
        */
    }

    private void seedBlindBoxes() {
        System.out.println("Seeding Blind Boxes...");
        
        // Example blind box seeding
        
        if (blindBoxRepository.count() > 0) {
            System.out.println("Blind Box data already exists, skipping...");
            return;
        }

        List<BlindPackage> blindBoxes = Arrays.asList(
            createBlindBox("Starter Mystery Box", 
                "Perfect for beginners! Contains common to rare items.",
                "starter-box.jpg", 9.99, true),
            
            createBlindBox("Premium Treasure Chest", 
                "High-value box with guaranteed rare items and chance for legendary.",
                "premium-chest.jpg", 29.99, true),
            
            createBlindBox("Legendary Collection Box", 
                "Exclusive box with only epic and legendary items.",
                "legendary-box.jpg", 49.99, true),
            
            createBlindBox("Seasonal Halloween Box", 
                "Limited edition Halloween-themed items.",
                "halloween-box.jpg", 19.99, true),
            
            createBlindBox("Christmas Special Box", 
                "Festive items and winter-themed collectibles.",
                "christmas-box.jpg", 24.99, true),
            
            createBlindBox("Gaming Gear Box", 
                "Virtual gaming items and accessories.",
                "gaming-box.jpg", 15.99, true)
        );

        blindBoxRepository.saveAll(blindBoxes);
        System.out.println("Seeded " + blindBoxes.size() + " blind boxes");
        
    }

    private void seedPrizeItems() {
        System.out.println("Seeding Prize Items...");
        
        // Example prize items seeding
        
        if (prizeItemRepository.count() > 0) {
            System.out.println("Prize Item data already exists, skipping...");
            return;
        }

        List<PrizeItem> prizeItems = Arrays.asList(
            // Starter Box Items
            createPrizeItem("Basic Sword", "common", 1L, 0.30, "basic-sword.jpg"),
            createPrizeItem("Health Potion", "common", 1L, 0.25, "health-potion.jpg"),
            createPrizeItem("Magic Ring", "uncommon", 1L, 0.20, "magic-ring.jpg"),
            createPrizeItem("Silver Shield", "rare", 1L, 0.15, "silver-shield.jpg"),
            createPrizeItem("Golden Helmet", "epic", 1L, 0.08, "golden-helmet.jpg"),
            createPrizeItem("Dragon Blade", "legendary", 1L, 0.02, "dragon-blade.jpg"),
            
            // Premium Box Items
            createPrizeItem("Crystal Wand", "rare", 2L, 0.25, "crystal-wand.jpg"),
            createPrizeItem("Enchanted Armor", "epic", 2L, 0.20, "enchanted-armor.jpg"),
            createPrizeItem("Phoenix Feather", "epic", 2L, 0.18, "phoenix-feather.jpg"),
            createPrizeItem("Mythril Sword", "legendary", 2L, 0.10, "mythril-sword.jpg"),
            createPrizeItem("Crown of Kings", "legendary", 2L, 0.05, "crown-kings.jpg"),
            
            // Legendary Box Items
            createPrizeItem("Excalibur", "legendary", 3L, 0.15, "excalibur.jpg"),
            createPrizeItem("Infinity Stone", "legendary", 3L, 0.12, "infinity-stone.jpg"),
            createPrizeItem("God's Blessing", "mythic", 3L, 0.08, "gods-blessing.jpg"),
            createPrizeItem("Universe Orb", "mythic", 3L, 0.05, "universe-orb.jpg")
        );

        prizeItemRepository.saveAll(prizeItems);
        System.out.println("Seeded " + prizeItems.size() + " prize items");
        
    }
//
//    private void seedOrders() {
//        System.out.println("Seeding Orders...");
//
//        // Example orders seeding
//
//        if (orderRepository.count() > 0) {
//            System.out.println("Order data already exists, skipping...");
//            return;
//        }
//
//        List<Order> orders = Arrays.asList(
//            createOrder("ORD-001", 2L, 29.99, "COMPLETED", "PAID"),
//            createOrder("ORD-002", 3L, 49.99, "COMPLETED", "PAID"),
//            createOrder("ORD-003", 4L, 19.98, "COMPLETED", "PAID"),
//            createOrder("ORD-004", 5L, 9.99, "PENDING", "PENDING"),
//            createOrder("ORD-005", 2L, 79.97, "COMPLETED", "PAID"),
//            createOrder("ORD-006", 3L, 15.99, "CANCELLED", "REFUNDED"),
//            createOrder("ORD-007", 4L, 99.95, "COMPLETED", "PAID"),
//            createOrder("ORD-008", 6L, 24.99, "PROCESSING", "PAID")
//        );
//
//        orderRepository.saveAll(orders);
//        System.out.println("Seeded " + orders.size() + " orders");
//
//    }

//    private void seedOrderItems() {
//        System.out.println("Seeding Order Items...");
//
//        // Example order items seeding
//
//        if (orderItemRepository.count() > 0) {
//            System.out.println("Order Item data already exists, skipping...");
//            return;
//        }
//
//        List<OrderItem> orderItems = Arrays.asList(
//            createOrderItem(1L, 2L, 1, 29.99), // Order 1, Premium Box, qty 1
//            createOrderItem(2L, 3L, 1, 49.99), // Order 2, Legendary Box, qty 1
//            createOrderItem(3L, 1L, 2, 19.98), // Order 3, Starter Box, qty 2
//            createOrderItem(4L, 1L, 1, 9.99),  // Order 4, Starter Box, qty 1
//            createOrderItem(5L, 2L, 1, 29.99), // Order 5, Premium Box, qty 1
//            createOrderItem(5L, 3L, 1, 49.99), // Order 5, Legendary Box, qty 1
//            createOrderItem(6L, 6L, 1, 15.99), // Order 6, Gaming Box, qty 1
//            createOrderItem(7L, 2L, 2, 59.98), // Order 7, Premium Box, qty 2
//            createOrderItem(7L, 1L, 4, 39.96), // Order 7, Starter Box, qty 4
//            createOrderItem(8L, 4L, 1, 19.99)  // Order 8, Halloween Box, qty 1
//        );
//
//        orderItemRepository.saveAll(orderItems);
//        System.out.println("Seeded " + orderItems.size() + " order items");
//
//    }

    private void seedTopUpHistory() {
        if (topUpRepository.count() > 0) {
            System.out.println("TopUp data already exists, skipping seeding...");
            return;
        }

        System.out.println("Seeding TopUp History data...");

        List<TopUpHistory> topUpHistories = Arrays.asList(
            createTopUpHistory("PAY-1ABC123DEF456GHI", "25.00", "approved", 
                LocalDateTime.now().minusDays(30), LocalDateTime.now().minusDays(30)),
            
            createTopUpHistory("PAY-2XYZ789ABC012DEF", "50.00", "approved", 
                LocalDateTime.now().minusDays(25), LocalDateTime.now().minusDays(25)),
            
            createTopUpHistory("PAY-3MNO345PQR678STU", "10.00", "approved", 
                LocalDateTime.now().minusDays(20), LocalDateTime.now().minusDays(20)),
            
            createTopUpHistory("PAY-4VWX901YZA234BCD", "100.00", "approved", 
                LocalDateTime.now().minusDays(15), LocalDateTime.now().minusDays(15)),
            
            createTopUpHistory("PAY-5EFG567HIJ890KLM", "75.00", "pending", 
                LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(10)),
            
            createTopUpHistory("PAY-6NOP123QRS456TUV", "20.00", "approved", 
                LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(5)),
            
            createTopUpHistory("PAY-7WXY789ZAB012CDE", "35.00", "failed", 
                LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(3)),
            
            createTopUpHistory("PAY-8FGH345IJK678LMN", "60.00", "approved", 
                LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1))
        );

        topUpRepository.saveAll(topUpHistories);
        System.out.println("Seeded " + topUpHistories.size() + " TopUp History records");
    }

    // Helper methods for creating entities
    
    private User createUser(String username, String email, String firstName, String lastName, String role, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password); // In real app, this should be encoded
        return user;
    }

//    private Blog createBlog(String title, String summary, String content, String status, Long authorId) {
//        Blog blog = new Blog();
//        blog.setTitle(title);
//        blog.setSummary(summary);
//        blog.setContent(content);
//        blog.setStatus(status);
//        blog.setAuthorId(authorId);
//        blog.setCreatedAt(LocalDateTime.now());
//        blog.setUpdatedAt(LocalDateTime.now());
//        return blog;
//    }

    private BlindPackage createBlindBox(String name, String description, String image, double price, boolean status) {
        BlindPackage blindBox = new BlindPackage();
        blindBox.setName(name);
        blindBox.setDescription(description);
        blindBox.setPricePerSpin(BigDecimal.valueOf(price));
        blindBox.setActive(status);
        return blindBox;
    }

    private PrizeItem createPrizeItem(String name, String rarity, Long blindBoxId, double probability, String image) {
        PrizeItem prizeItem = new PrizeItem();
        prizeItem.setItemName(name);
//        prizeItem.setRarity("");
        BlindPackage bl = new BlindPackage();
        bl.setId(Integer.valueOf(blindBoxId+""));
        prizeItem.setBlindBagType(bl);
        prizeItem.setProbability(probability);
        return prizeItem;
    }

//    private Order createOrder(String orderNumber, Long userId, double totalAmount, String status, String paymentStatus) {
//        Order order = new Order();
//        order.setOrderNumber(orderNumber);
//        order.setUserId(userId);
//        order.setTotalAmount(totalAmount);
//        order.setStatus(status);
//        order.setPaymentStatus(paymentStatus);
//        order.setCreatedAt(LocalDateTime.now());
//        return order;
//    }
//
//    private OrderItem createOrderItem(Long orderId, Long blindBoxId, int quantity, double price) {
//        OrderItem orderItem = new OrderItem();
//        orderItem.setOrderId(orderId);
//        orderItem.setBlindBoxId(blindBoxId);
//        orderItem.setQuantity(quantity);
//        orderItem.setPrice(price);
//        orderItem.setCreatedAt(LocalDateTime.now());
//        return orderItem;
//    }
//

    private TopUpHistory createTopUpHistory(String transactionId, String amount, String status,
                                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        TopUpHistory topUp = new TopUpHistory();
        topUp.setTransaction_id(transactionId);
        topUp.setAmount(amount);
        topUp.setStatus(status);
        topUp.setCreatedAt(createdAt);
        topUp.setUpdatedAt(updatedAt);
        return topUp;
    }
}
