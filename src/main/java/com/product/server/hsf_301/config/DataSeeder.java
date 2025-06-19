package com.product.server.hsf_301.config;

import com.product.server.hsf_301.blindBox.model.*;
import com.product.server.hsf_301.blindBox.repository.*;
import com.product.server.hsf_301.payment.TopUpRepository;
import com.product.server.hsf_301.payment.model.TopUpHistory;
import com.product.server.hsf_301.blindBox.model.SpinHistory;
import com.product.server.hsf_301.user.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserPrizeitemRepository userPrizeItemRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;

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
        seedUserPrizeItems();
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

        List<AppUser> users = Arrays.asList(
                createUser("admin", "admin@example.com", "Admin", "User", "ADMIN", "admin"),
                createUser("john_doe", "john@example.com", "John", "Doe", "USER", "admin"),
                createUser("jane_smith", "jane@example.com", "Jane", "Smith", "USER", "admin"),
                createUser("bob_johnson", "bob@example.com", "Bob", "Johnson", "USER", "admin"),
                createUser("alice_brown", "alice@example.com", "Alice", "Brown", "USER", "admin"),
                createUser("charlie_wilson", "charlie@example.com", "Charlie", "Wilson", "MODERATOR", "admin")
        );

        userRepository.saveAll(users);
        System.out.println("Seeded " + users.size() + " users");

    }

    private void seedBlogs() {
        System.out.println("Seeding Blogs...");

        // Example blog seeding

        if (blogRepository.count() > 0) {
            System.out.println("Blog data already exists, skipping...");
            return;
        }

        List<Blog> blogs = Arrays.asList(
                createBlog("Welcome to Our Platform",
                        "We're excited to introduce our new mystery box platform where you can discover amazing prizes!",
                        "This is the full content of our welcome blog post. Here you can learn about all the exciting features...",
                        "PUBLISHED", 2),

                createBlog("How to Open Your First Mystery Box",
                        "Learn the basics of opening mystery boxes and maximizing your chances of getting rare items.",
                        "Step 1: Choose your mystery box carefully... Step 2: Cross your fingers... Step 3: Open and enjoy!",
                        "PUBLISHED", 1),

                createBlog("Rare Items Showcase",
                        "Check out some of the rarest items you can win from our premium mystery boxes.",
                        "Our premium boxes contain incredible items including golden weapons, legendary armor, and exclusive cosmetics...",
                        "PUBLISHED", 1),

                createBlog("Tips for New Collectors",
                        "Essential tips for beginners who want to start their collection journey.",
                        "Starting your collection can be overwhelming, but with these tips you'll be on your way to success...",
                        "DRAFT", 2),

                createBlog("Community Highlights",
                        "Featuring amazing collections from our community members.",
                        "This month we're showcasing some incredible collections from our most dedicated users...",
                        "PUBLISHED", 1)
        );

        blogRepository.saveAll(blogs);
        System.out.println("Seeded " + blogs.size() + " blogs");

    }

    private void seedBlindBoxes() {
        System.out.println("Seeding Blind Boxes...");

        // Example blind box seeding

        if (blindBoxRepository.count() > 0) {
            System.out.println("Blind Box data already exists, skipping...");
            return;
        }

        List<PackagesBox> blindBoxes = Arrays.asList(
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
        List<PackagesBox> blindBoxes = blindBoxRepository.findAll();
        if (blindBoxes.isEmpty()) {
            System.out.println("No blind boxes found, skipping prize items seeding...");
            return;
        }

        List<PrizeItem> prizeItems = Arrays.asList(
                // Starter Box Items (5 items - GOOD_LUCK highest, then COMMON/UNCOMMON, then RARE/SPECIAL)
                createPrizeItemWithEntity("Basic Sword", RareEnum.COMMON, blindBoxes.get(0), 0.25, "basic-sword.jpg", true),
                createPrizeItemWithEntity("Health Potion", RareEnum.UNCOMMON, blindBoxes.get(0), 0.20, "health-potion.jpg", true),
                createPrizeItemWithEntity("Magic Ring", RareEnum.RARE, blindBoxes.get(0), 0.15, "magic-ring.jpg", true),
                createPrizeItemWithEntity("Silver Shield", RareEnum.SPECIAL, blindBoxes.get(0), 0.10, "silver-shield.jpg", true),
                createPrizeItemWithEntity("Better Luck Next Time", RareEnum.GOOD_LUCK, blindBoxes.get(0), 0.30, "good-luck.jpg", false),

                // Premium Box Items (5 items - slightly better chances for real items)
                createPrizeItemWithEntity("Iron Sword", RareEnum.COMMON, blindBoxes.get(1), 0.28, "iron-sword.jpg", true),
                createPrizeItemWithEntity("Mana Potion", RareEnum.UNCOMMON, blindBoxes.get(1), 0.25, "mana-potion.jpg", true),
                createPrizeItemWithEntity("Crystal Wand", RareEnum.RARE, blindBoxes.get(1), 0.20, "crystal-wand.jpg", true),
                createPrizeItemWithEntity("Enchanted Armor", RareEnum.SPECIAL, blindBoxes.get(1), 0.15, "enchanted-armor.jpg", true),
                createPrizeItemWithEntity("Better Luck Next Time", RareEnum.GOOD_LUCK, blindBoxes.get(1), 0.12, "good-luck.jpg", false),

                // Legendary Box Items (5 items - best chances for rare items, lowest GOOD_LUCK)
                createPrizeItemWithEntity("Steel Blade", RareEnum.COMMON, blindBoxes.get(2), 0.30, "steel-blade.jpg", true),
                createPrizeItemWithEntity("Divine Potion", RareEnum.UNCOMMON, blindBoxes.get(2), 0.28, "divine-potion.jpg", true),
                createPrizeItemWithEntity("Mythril Ring", RareEnum.RARE, blindBoxes.get(2), 0.25, "mythril-ring.jpg", true),
                createPrizeItemWithEntity("Crown of Kings", RareEnum.SPECIAL, blindBoxes.get(2), 0.20, "crown-kings.jpg", true),
                createPrizeItemWithEntity("Better Luck Next Time", RareEnum.GOOD_LUCK, blindBoxes.get(2), 0.05, "good-luck.jpg", false),

                // Halloween Box Items (5 items - spooky theme with high GOOD_LUCK)
                createPrizeItemWithEntity("Pumpkin Mask", RareEnum.COMMON, blindBoxes.get(3), 0.22, "pumpkin-mask.jpg", true),
                createPrizeItemWithEntity("Witch Hat", RareEnum.UNCOMMON, blindBoxes.get(3), 0.18, "witch-hat.jpg", true),
                createPrizeItemWithEntity("Ghost Cloak", RareEnum.RARE, blindBoxes.get(3), 0.12, "ghost-cloak.jpg", true),
                createPrizeItemWithEntity("Vampire Fangs", RareEnum.SPECIAL, blindBoxes.get(3), 0.08, "vampire-fangs.jpg", true),
                createPrizeItemWithEntity("Cursed - No Prize", RareEnum.GOOD_LUCK, blindBoxes.get(3), 0.40, "cursed.jpg", false),

                // Christmas Box Items (5 items - festive theme with moderate GOOD_LUCK)
                createPrizeItemWithEntity("Santa Hat", RareEnum.COMMON, blindBoxes.get(4), 0.26, "santa-hat.jpg", true),
                createPrizeItemWithEntity("Reindeer Antlers", RareEnum.UNCOMMON, blindBoxes.get(4), 0.24, "reindeer-antlers.jpg", true),
                createPrizeItemWithEntity("Christmas Bell", RareEnum.RARE, blindBoxes.get(4), 0.18, "christmas-bell.jpg", true),
                createPrizeItemWithEntity("Golden Star", RareEnum.SPECIAL, blindBoxes.get(4), 0.12, "golden-star.jpg", true),
                createPrizeItemWithEntity("Coal - No Prize", RareEnum.GOOD_LUCK, blindBoxes.get(4), 0.20, "coal.jpg", false),

                // Gaming Box Items (5 items - balanced distribution)
                createPrizeItemWithEntity("Gaming Mouse", RareEnum.COMMON, blindBoxes.get(5), 0.27, "gaming-mouse.jpg", true),
                createPrizeItemWithEntity("RGB Keyboard", RareEnum.UNCOMMON, blindBoxes.get(5), 0.23, "rgb-keyboard.jpg", true),
                createPrizeItemWithEntity("Gaming Headset", RareEnum.RARE, blindBoxes.get(5), 0.18, "gaming-headset.jpg", true),
                createPrizeItemWithEntity("Pro Controller", RareEnum.SPECIAL, blindBoxes.get(5), 0.15, "pro-controller.jpg", true),
                createPrizeItemWithEntity("Connection Lost", RareEnum.GOOD_LUCK, blindBoxes.get(5), 0.17, "no-connection.jpg", false)
        );

        prizeItemRepository.saveAll(prizeItems);
        System.out.println("Prize Items seeded successfully: " + prizeItems.size() + " items");

        // Print probability summary for verification
        System.out.println("\n=== PROBABILITY DISTRIBUTION SUMMARY ===");
        System.out.println("New Distribution Logic: GOOD_LUCK (highest) > COMMON > UNCOMMON > RARE > SPECIAL (lowest)");
        for (int i = 0; i < blindBoxes.size(); i++) {
            PackagesBox box = blindBoxes.get(i);
            System.out.println("\n" + box.getName() + ":");

            // Calculate probabilities for this box
            List<PrizeItem> boxItems = prizeItems.subList(i * 5, (i + 1) * 5);
            double totalProb = 0;

            // Sort by probability for better display
            boxItems.sort((a, b) -> Double.compare(b.getProbability(), a.getProbability()));

            for (PrizeItem item : boxItems) {
                String status = item.isClaimAble() ? "✅ Claimable" : "❌ No Prize";
                System.out.printf("  %-25s | %-10s | %5.1f%% | %s\n",
                        item.getItemName(),
                        item.getRarity().getValue(),
                        item.getProbability() * 100,
                        status);
                totalProb += item.getProbability();
            }
            System.out.printf("  Total Probability: %.1f%%\n", totalProb * 100);

            if (Math.abs(totalProb - 1.0) > 0.001) {
                System.out.println("  ⚠️  WARNING: Probabilities don't sum to 100%!");
            } else {
                System.out.println("  ✅ Probabilities sum correctly to 100%");
            }
        }
        System.out.println("==========================================\n");
    }

    private void seedOrders() {
        System.out.println("Seeding Orders...");

        if (orderRepository.count() > 0) {
            System.out.println("Order data already exists, skipping...");
            return;
        }

        // Lấy users thực tế từ database
        List<AppUser> users = userRepository.findAll();
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
        List<PackagesBox> blindBoxes = blindBoxRepository.findAll();
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
        List<AppUser> users = userRepository.findAll();
        List<PackagesBox> blindBoxes = blindBoxRepository.findAll();
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

    private void seedUserPrizeItems() {
        System.out.println("Seeding User Prize Items...");

        if (userPrizeItemRepository.count() > 0) {
            System.out.println("User Prize Item data already exists, skipping...");
            return;
        }

        // Get actual data from database
        List<AppUser> users = userRepository.findAll();
        List<PrizeItem> prizeItems = prizeItemRepository.findAll();
        List<SpinHistory> spinHistories = spinHistoryRepository.findAll();

        if (users.size() < 2 || prizeItems.isEmpty() || spinHistories.isEmpty()) {
            System.out.println("Missing required data for user prize items, skipping...");
            return;
        }

        List<UserPrizeItem> userPrizeItems = Arrays.asList(
                // User john_doe won prizes - mix of claimed and unclaimed
                createUserPrizeItem(users.get(1),  spinHistories.get(0), true, LocalDateTime.now().minusDays(5)),
                createUserPrizeItem(users.get(1),  spinHistories.get(1), false, null),
                createUserPrizeItem(users.get(1),
                        spinHistories.size() > 9 ? spinHistories.get(9) : spinHistories.get(0), true, LocalDateTime.now().minusDays(2)),

                // User jane_smith won prizes
                createUserPrizeItem(users.get(2),
                        spinHistories.get(2), false, null),
                createUserPrizeItem(users.get(2),
                        spinHistories.get(3), true, LocalDateTime.now().minusDays(8)),

                // User bob_johnson won prizes
                createUserPrizeItem(users.get(3),
                        spinHistories.get(4), false, null),
                createUserPrizeItem(users.get(3), spinHistories.get(5), true, LocalDateTime.now().minusDays(3)),

                // User alice_brown won prizes
                createUserPrizeItem(users.get(4),
                        spinHistories.get(6), false, null),
                createUserPrizeItem(users.get(4),
                        spinHistories.get(7), false, null),

                // User charlie_wilson won prizes
                createUserPrizeItem(users.get(5), spinHistories.get(8), true, LocalDateTime.now().minusDays(1))
        );

        userPrizeItemRepository.saveAll(userPrizeItems);
        System.out.println("Seeded " + userPrizeItems.size() + " user prize items");

        // Print summary
        System.out.println("\n=== USER PRIZE ITEMS SUMMARY ===");
        for (AppUser user : users.subList(1, Math.min(6, users.size()))) {
            long totalItems = userPrizeItems.stream()
                    .filter(item -> item.getUser().getUserId().equals(user.getUserId()))
                    .count();
            long claimedItems = userPrizeItems.stream()
                    .filter(item -> item.getUser().getUserId().equals(user.getUserId()) && item.isClaimed())
                    .count();
            long unclaimedItems = totalItems - claimedItems;

            System.out.printf("User: %-15s | Total: %d | Claimed: %d | Unclaimed: %d\n",
                    user.getUsername(), totalItems, claimedItems, unclaimedItems);
        }
        System.out.println("====================================\n");
    }

    // Helper methods for creating entities

    private AppUser createUser(String username, String email, String firstName, String lastName, String role, String password) {
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // In real app, this should be encoded
        user.setBalance(BigDecimal.valueOf(9999999999L));
        user.setRole(role);
        return user;
    }

    private Blog createBlog(String title, String summary, String content, String status, int authorId) {
        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setContent(content);
        blog.setStatus(status);
        AppUser user = new AppUser();
        user.setUserId(authorId);
        blog.setAuthor(user);
        blog.setCreatedAt(LocalDateTime.now());
        blog.setCreatedAt(LocalDateTime.now());
        return blog;
    }

    private PackagesBox createBlindBox(String name, String description, String image, double price, boolean status) {
        PackagesBox blindBox = new PackagesBox();
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
        PackagesBox bl = new PackagesBox();
        bl.setId(Integer.valueOf(blindBoxId+""));
        prizeItem.setBlindBagType(bl);
        prizeItem.setProbability(probability);
        return prizeItem;
    }

    private Order createOrder(String orderNumber, int userId, double totalAmount, String status, String paymentStatus) {
        AppUser user = new AppUser();
        user.setUserId(userId);
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(totalAmount);
        order.setStatus(status);
        order.setPaymentStatus(paymentStatus);
        return order;
    }

    private OrderItem createOrderItem(int orderId, int blindBoxId, PrizeItem prizeItem, int quantity, double price) {
        PackagesBox bp = new PackagesBox();
        bp.setId(blindBoxId);
        Order order = new Order();
        order.setOrderId(orderId);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
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

    private SpinHistory createSpinHistoryWithRealData(AppUser user, PackagesBox blindBox, PrizeItem prizeItem,
                                                      Double price, Boolean success, Boolean redeemed) {
        SpinHistory spinHistory = new SpinHistory();

        spinHistory.setUser(user);
        spinHistory.setBlindBagId(blindBox);
        spinHistory.setPrizeItemId(prizeItem);
        spinHistory.setPrice(price);
        spinHistory.setSuccess(success);

        spinHistory.setSpinTime(LocalDateTime.now().minusDays(new Random().nextInt(30)));


        return spinHistory;
    }

    private SpinHistory createSpinHistory(Integer userId, Integer blindBoxId, Integer prizeItemId, Double price, Boolean success, Boolean redeemed) {
        SpinHistory spinHistory = new SpinHistory();

        AppUser user = userRepository.findById(userId).orElse(null);
        PackagesBox blindBox = blindBoxRepository.findById(blindBoxId).orElse(null);
        PrizeItem prizeItem = prizeItemRepository.findById(prizeItemId).orElse(null);

        spinHistory.setUser(user);
        spinHistory.setBlindBagId(blindBox);
        spinHistory.setPrizeItemId(prizeItem);
        spinHistory.setPrice(price);
        spinHistory.setSuccess(success);
        spinHistory.setSpinTime(LocalDateTime.now().minusDays(new Random().nextInt(30)));


        return spinHistory;
    }

    private UserPrizeItem createUserPrizeItem(AppUser user, SpinHistory spin, boolean claimed, LocalDateTime claimedAt) {
        UserPrizeItem userPrizeItem = new UserPrizeItem();
        userPrizeItem.setUser(user);
        userPrizeItem.setSpin(spin);
        userPrizeItem.setClaimed(claimed);
        userPrizeItem.setClaimedAt(claimedAt);
        userPrizeItem.setActive(true);
        return userPrizeItem;
    }

    // Helper methods for creating entities with real database entities

    private PrizeItem createPrizeItemWithEntity(String itemName, RareEnum rarity, PackagesBox blindBox,
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

    private Order createOrderWithUser(AppUser user, double totalAmount, String status, String paymentStatus) {
        Order order = new Order();
        order.setUser(user); // Sử dụng user entity thực tế
        order.setTotalAmount(totalAmount);
        order.setStatus(status);
        order.setPaymentStatus(paymentStatus);
        return order;
    }

    private OrderItem createOrderItemWithEntities(Order order, PackagesBox blindBox, PrizeItem prizeItem, double price) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order); // Sử dụng order entity thực tế
        orderItem.setPrizeItemId(prizeItem); // Sử dụng prizeItem entity thực tế
        orderItem.setPrice(price);
        return orderItem;
    }
}