package com.product.server.hsf_301.config;

import com.product.server.hsf_301.blindBox.model.*;
import com.product.server.hsf_301.blindBox.repository.BlindBoxRepository;
import com.product.server.hsf_301.blindBox.repository.OrderItemRepository;
import com.product.server.hsf_301.blindBox.repository.OrderRepository;
import com.product.server.hsf_301.blindBox.repository.PrizeItemRepository;
import com.product.server.hsf_301.blindBox.repository.UserRepository;
import com.product.server.hsf_301.payment.TopUpRepository;
import com.product.server.hsf_301.payment.model.TopUpHistory;
import com.product.server.hsf_301.blindBox.repository.SpinHistoryRepository;
import com.product.server.hsf_301.blindBox.model.SpinHistory;
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

    @Autowired
    private BlindBoxRepository blindBoxRepository;

    @Autowired
    private PrizeItemRepository prizeItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private SpinHistoryRepository spinHistoryRepository;

    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting data seeding...");

        // Đảm bảo thứ tự đúng để tránh foreign key constraints
        seedUsers();
        seedBlindBoxes();
        seedPrizeItems();
        seedOrders();
        seedOrderItems();
        seedSpinHistory();
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

        if (prizeItemRepository.count() > 0) {
            System.out.println("Prize Item data already exists, skipping...");
            return;
        }

        // Lấy blind boxes đã được lưu từ database
        List<BlindPackage> blindBoxes = blindBoxRepository.findAll();
        if (blindBoxes.isEmpty()) {
            System.out.println("No blind boxes found, skipping prize items seeding...");
            return;
        }

        List<PrizeItem> prizeItems = Arrays.asList(
                // Starter Box Items (5 items total)
                // 2 COMMON items
                createPrizeItemWithEntity("Basic Sword", RareEnum.COMMON, blindBoxes.get(0), 0.35, "basic-sword.jpg", true),
                createPrizeItemWithEntity("Health Potion", RareEnum.COMMON, blindBoxes.get(0), 0.30, "health-potion.jpg", true),
                // 1 UNCOMMON item
                createPrizeItemWithEntity("Magic Ring", RareEnum.UNCOMMON, blindBoxes.get(0), 0.20, "magic-ring.jpg", true),
                // 1 RARE item
                createPrizeItemWithEntity("Silver Shield", RareEnum.RARE, blindBoxes.get(0), 0.13, "silver-shield.jpg", true),
                // 1 GOOD_LUCK item (not claimable)
                createPrizeItemWithEntity("Dragon Blade", RareEnum.GOOD_LUCK, blindBoxes.get(0), 0.02, "dragon-blade.jpg", false),

                // Premium Box Items (5 items total)
                // 2 COMMON items
                createPrizeItemWithEntity("Iron Sword", RareEnum.COMMON, blindBoxes.get(1), 0.30, "iron-sword.jpg", true),
                createPrizeItemWithEntity("Mana Potion", RareEnum.COMMON, blindBoxes.get(1), 0.25, "mana-potion.jpg", true),
                // 1 UNCOMMON item
                createPrizeItemWithEntity("Crystal Wand", RareEnum.UNCOMMON, blindBoxes.get(1), 0.22, "crystal-wand.jpg", true),
                // 1 RARE/SPECIAL item
                createPrizeItemWithEntity("Enchanted Armor", RareEnum.SPECIAL, blindBoxes.get(1), 0.18, "enchanted-armor.jpg", true),
                // 1 GOOD_LUCK item (not claimable)
                createPrizeItemWithEntity("Phoenix Feather", RareEnum.GOOD_LUCK, blindBoxes.get(1), 0.05, "phoenix-feather.jpg", false),

                // Legendary Box Items (5 items total)
                // 2 COMMON items
                createPrizeItemWithEntity("Steel Blade", RareEnum.COMMON, blindBoxes.get(2), 0.25, "steel-blade.jpg", true),
                createPrizeItemWithEntity("Divine Potion", RareEnum.COMMON, blindBoxes.get(2), 0.25, "divine-potion.jpg", true),
                // 1 UNCOMMON item
                createPrizeItemWithEntity("Mythril Ring", RareEnum.UNCOMMON, blindBoxes.get(2), 0.20, "mythril-ring.jpg", true),
                // 1 RARE/SPECIAL item
                createPrizeItemWithEntity("Crown of Kings", RareEnum.SPECIAL, blindBoxes.get(2), 0.15, "crown-kings.jpg", true),
                // 1 GOOD_LUCK item (not claimable)
                createPrizeItemWithEntity("Excalibur", RareEnum.GOOD_LUCK, blindBoxes.get(2), 0.15, "excalibur.jpg", false)
        );

        prizeItemRepository.saveAll(prizeItems);
        System.out.println("Prize Items seeded successfully: " + prizeItems.size() + " items");
    }

    private void seedOrders() {
        System.out.println("Seeding Orders...");

        if (orderRepository.count() > 0) {
            System.out.println("Order data already exists, skipping...");
            return;
        }

        // Lấy users thực tế từ database
        List<User> users = userRepository.findAll();
        if (users.size() < 6) {
            System.out.println("Not enough users found, skipping orders seeding...");
            return;
        }

        List<Order> orders = Arrays.asList(
                createOrderWithUser(users.get(1), 29.99, "COMPLETED", "PAID"),
                createOrderWithUser(users.get(2), 49.99, "COMPLETED", "PAID"),
                createOrderWithUser(users.get(3), 19.98, "COMPLETED", "PAID"),
                createOrderWithUser(users.get(4), 9.99, "PENDING", "PENDING"),
                createOrderWithUser(users.get(1), 79.97, "COMPLETED", "PAID"),
                createOrderWithUser(users.get(2), 15.99, "CANCELLED", "REFUNDED"),
                createOrderWithUser(users.get(3), 99.95, "COMPLETED", "PAID"),
                createOrderWithUser(users.get(5), 24.99, "PROCESSING", "PAID")
        );

        orderRepository.saveAll(orders);
        System.out.println("Seeded " + orders.size() + " orders");
    }

    private void seedOrderItems() {
        System.out.println("Seeding Order Items...");

        if (orderItemRepository.count() > 0) {
            System.out.println("Order Item data already exists, skipping...");
            return;
        }

        // Lấy dữ liệu thực tế từ database
        List<BlindPackage> blindBoxes = blindBoxRepository.findAll();
        List<PrizeItem> prizeItems = prizeItemRepository.findAll();
        List<Order> orders = orderRepository.findAll();

        if (blindBoxes.isEmpty() || prizeItems.isEmpty() || orders.isEmpty()) {
            System.out.println("Missing required data for order items, skipping...");
            return;
        }

        List<OrderItem> orderItems = Arrays.asList(
                // Order 1 - Sử dụng entities thực tế
                createOrderItemWithEntities(orders.get(0), blindBoxes.get(1),
                        prizeItems.size() > 6 ? prizeItems.get(6) : prizeItems.get(0), 29.99),

                // Order 2
                createOrderItemWithEntities(orders.get(1), blindBoxes.get(2),
                        prizeItems.size() > 11 ? prizeItems.get(11) : prizeItems.get(1), 49.99),

                // Order 3 - 2 items của starter box
                createOrderItemWithEntities(orders.get(2), blindBoxes.get(0),
                        prizeItems.get(0), 19.98),

                // Order 4
                createOrderItemWithEntities(orders.get(3), blindBoxes.get(0),
                        prizeItems.get(1), 9.99),

                // Order 5 - Multiple items
                createOrderItemWithEntities(orders.get(4), blindBoxes.get(1),
                        prizeItems.size() > 7 ? prizeItems.get(7) : prizeItems.get(2), 29.99),
                createOrderItemWithEntities(orders.get(4), blindBoxes.get(2),
                        prizeItems.size() > 12 ? prizeItems.get(12) : prizeItems.get(3), 49.99),

                // Order 6 - Gaming box (sử dụng box có index hợp lệ)
                createOrderItemWithEntities(orders.get(5),
                        blindBoxes.size() > 5 ? blindBoxes.get(5) : blindBoxes.get(blindBoxes.size()-1),
                        prizeItems.size() > 8 ? prizeItems.get(8) : prizeItems.get(4), 15.99),

                // Order 7 - Multiple items
                createOrderItemWithEntities(orders.get(6), blindBoxes.get(1),
                        prizeItems.size() > 8 ? prizeItems.get(8) : prizeItems.get(2), 59.98),
                createOrderItemWithEntities(orders.get(6), blindBoxes.get(0),
                        prizeItems.get(2), 39.96),

                // Order 8 - Halloween box
                createOrderItemWithEntities(orders.get(7),
                        blindBoxes.size() > 3 ? blindBoxes.get(3) : blindBoxes.get(0),
                        prizeItems.get(3), 19.99)
        );

        orderItemRepository.saveAll(orderItems);
        System.out.println("Seeded " + orderItems.size() + " order items");
    }

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

    private void seedSpinHistory() {
        System.out.println("Seeding Spin History...");

        if (spinHistoryRepository.count() > 0) {
            System.out.println("Spin History data already exists, skipping...");
            return;
        }

        // Lấy dữ liệu thực tế từ database
        List<User> users = userRepository.findAll();
        List<BlindPackage> blindBoxes = blindBoxRepository.findAll();
        List<PrizeItem> prizeItems = prizeItemRepository.findAll();

        if (users.size() < 2 || blindBoxes.isEmpty() || prizeItems.isEmpty()) {
            System.out.println("Missing required data for spin history, skipping...");
            return;
        }

        List<SpinHistory> spinHistories = Arrays.asList(
                createSpinHistoryWithRealData(users.get(1), blindBoxes.get(0), prizeItems.get(0), 9.99, true, false),
                createSpinHistoryWithRealData(users.get(1), blindBoxes.get(0), prizeItems.get(1), 9.99, true, true),
                createSpinHistoryWithRealData(users.get(2), blindBoxes.get(1),
                        prizeItems.size() > 6 ? prizeItems.get(6) : prizeItems.get(2), 29.99, true, false),
                createSpinHistoryWithRealData(users.get(2), blindBoxes.get(1),
                        prizeItems.size() > 7 ? prizeItems.get(7) : prizeItems.get(3), 29.99, true, true),
                createSpinHistoryWithRealData(users.get(3), blindBoxes.get(2),
                        prizeItems.size() > 11 ? prizeItems.get(11) : prizeItems.get(4), 49.99, true, false),
                createSpinHistoryWithRealData(users.get(3), blindBoxes.get(0), prizeItems.get(2), 9.99, true, true),
                createSpinHistoryWithRealData(users.get(4), blindBoxes.get(1),
                        prizeItems.size() > 8 ? prizeItems.get(8) : prizeItems.get(1), 29.99, true, false),
                createSpinHistoryWithRealData(users.get(4), blindBoxes.get(2),
                        prizeItems.size() > 12 ? prizeItems.get(12) : prizeItems.get(0), 49.99, true, false),
                createSpinHistoryWithRealData(users.get(5), blindBoxes.get(0), prizeItems.get(3), 9.99, true, true),
                createSpinHistoryWithRealData(users.get(1), blindBoxes.get(1),
                        prizeItems.size() > 9 ? prizeItems.get(9) : prizeItems.get(2), 29.99, true, false)
        );

        spinHistoryRepository.saveAll(spinHistories);
        System.out.println("Seeded " + spinHistories.size() + " spin histories");
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

    private PrizeItem createPrizeItem(String name, RareEnum rarity, Long blindBoxId, double probability, String image) {
        PrizeItem prizeItem = new PrizeItem();
        prizeItem.setItemName(name);
        prizeItem.setRarity(rarity);
        BlindPackage bl = new BlindPackage();
        bl.setId(Integer.valueOf(blindBoxId+""));
        prizeItem.setBlindBagType(bl);
        prizeItem.setProbability(probability);
        return prizeItem;
    }

    private Order createOrder(String orderNumber, int userId, double totalAmount, String status, String paymentStatus) {
        User user = new User();
        user.setUserId(userId);
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(totalAmount);
        order.setStatus(status);
        order.setPaymentStatus(paymentStatus);
        return order;
    }

    private OrderItem createOrderItem(int orderId, int blindBoxId, PrizeItem prizeItem, int quantity, double price) {
        BlindPackage bp = new BlindPackage();
        bp.setId(blindBoxId);
        Order order = new Order();
        order.setOrderId(orderId);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setBlindBagId(bp);
        orderItem.setPrizeItemId(prizeItem);
        orderItem.setPrice(price);
        return orderItem;
    }


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

    private SpinHistory createSpinHistoryWithRealData(User user, BlindPackage blindBox, PrizeItem prizeItem,
                                                      Double price, Boolean success, Boolean redeemed) {
        SpinHistory spinHistory = new SpinHistory();

        spinHistory.setUser(user);
        spinHistory.setBlindBagId(blindBox);
        spinHistory.setPrizeItemId(prizeItem);
        spinHistory.setPrice(price);
        spinHistory.setSuccess(success);
        spinHistory.setRedeemed(redeemed);
        spinHistory.setSpinTime(LocalDateTime.now().minusDays(new Random().nextInt(30)));

        if (redeemed) {
            spinHistory.setRedeemedAt(spinHistory.getSpinTime().plusHours(new Random().nextInt(48)));
        }

        return spinHistory;
    }

    private SpinHistory createSpinHistory(Integer userId, Integer blindBoxId, Integer prizeItemId, Double price, Boolean success, Boolean redeemed) {
        SpinHistory spinHistory = new SpinHistory();

        User user = userRepository.findById(userId).orElse(null);
        BlindPackage blindBox = blindBoxRepository.findById(blindBoxId).orElse(null);
        PrizeItem prizeItem = prizeItemRepository.findById(prizeItemId).orElse(null);

        spinHistory.setUser(user);
        spinHistory.setBlindBagId(blindBox);
        spinHistory.setPrizeItemId(prizeItem);
        spinHistory.setPrice(price);
        spinHistory.setSuccess(success);
        spinHistory.setRedeemed(redeemed);
        spinHistory.setSpinTime(LocalDateTime.now().minusDays(new Random().nextInt(30)));

        if (redeemed) {
            spinHistory.setRedeemedAt(spinHistory.getSpinTime().plusHours(new Random().nextInt(48)));
        }

        return spinHistory;
    }

    // Helper methods for creating entities with real database entities

    private PrizeItem createPrizeItemWithEntity(String itemName, RareEnum rarity, BlindPackage blindBox,
                                                double dropRate, String imageUrl, boolean claimable) {
        PrizeItem item = new PrizeItem();
        item.setItemName(itemName);
        item.setRarity(rarity);
        item.setBlindBagType(blindBox);
        item.setProbability(dropRate);
//        item.set(imageUrl);
        item.setClaimAble(claimable); // Set claimable based on parameter
        return item;
    }

    private Order createOrderWithUser(User user, double totalAmount, String status, String paymentStatus) {
        Order order = new Order();
        order.setUser(user); // Sử dụng user entity thực tế
        order.setTotalAmount(totalAmount);
        order.setStatus(status);
        order.setPaymentStatus(paymentStatus);
        return order;
    }

    private OrderItem createOrderItemWithEntities(Order order, BlindPackage blindBox, PrizeItem prizeItem, double price) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order); // Sử dụng order entity thực tế
        orderItem.setBlindBagId(blindBox); // Sử dụng blindBox entity thực tế
        orderItem.setPrizeItemId(prizeItem); // Sử dụng prizeItem entity thực tế
        orderItem.setPrice(price);
        return orderItem;
    }
}
