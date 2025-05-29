/**
 * HSF Store Main JavaScript
 */

document.addEventListener('DOMContentLoaded', function() {
    // Initialize quantity controls
    initQuantityControls();
    
    // Initialize rating stars
    initRatingStars();
    
    // Initialize product image gallery
    initProductGallery();
    
    // Initialize add to cart functionality
    initAddToCart();
    
    // Initialize wishlist functionality
    initWishlist();
});

/**
 * Initialize quantity control buttons
 */
function initQuantityControls() {
    const minusBtns = document.querySelectorAll('.quantity-minus');
    const plusBtns = document.querySelectorAll('.quantity-plus');
    
    minusBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            const input = this.parentNode.querySelector('.quantity-input');
            const value = parseInt(input.value);
            if (value > parseInt(input.min)) {
                input.value = value - 1;
            }
        });
    });
    
    plusBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            const input = this.parentNode.querySelector('.quantity-input');
            const value = parseInt(input.value);
            input.value = value + 1;
        });
    });
}

/**
 * Initialize rating stars functionality
 */
function initRatingStars() {
    const stars = document.querySelectorAll('.rating-input');
    const ratingInput = document.getElementById('rating');
    
    if (stars.length > 0 && ratingInput) {
        stars.forEach(star => {
            star.addEventListener('mouseover', function() {
                const value = parseInt(this.getAttribute('data-value'));
                highlightStars(value);
            });
            
            star.addEventListener('mouseout', function() {
                const currentRating = parseInt(ratingInput.value);
                highlightStars(currentRating);
            });
            
            star.addEventListener('click', function() {
                const value = parseInt(this.getAttribute('data-value'));
                ratingInput.value = value;
                highlightStars(value);
            });
        });
    }
    
    function highlightStars(count) {
        stars.forEach(star => {
            const value = parseInt(star.getAttribute('data-value'));
            if (value <= count) {
                star.classList.remove('far');
                star.classList.add('fas');
            } else {
                star.classList.remove('fas');
                star.classList.add('far');
            }
        });
    }
}

/**
 * Initialize product image gallery
 */
function initProductGallery() {
    const galleryImages = document.querySelectorAll('.image-gallery img');
    const mainImage = document.querySelector('.main-image img');
    
    if (galleryImages.length > 0 && mainImage) {
        galleryImages.forEach(img => {
            img.addEventListener('click', function() {
                const newSrc = this.getAttribute('src');
                mainImage.setAttribute('src', newSrc);
            });
        });
    }
}

/**
 * Initialize add to cart functionality
 */
function initAddToCart() {
    const addToCartBtns = document.querySelectorAll('.add-to-cart, .add-to-cart-btn');
    
    addToCartBtns.forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            const productId = this.getAttribute('data-id');
            const quantityInput = document.querySelector('.quantity-input');
            const quantity = quantityInput ? parseInt(quantityInput.value) : 1;
            
            // Here you would typically make an AJAX request to add the item to cart
            console.log(`Adding product ${productId} to cart, quantity: ${quantity}`);
            
            // Show success message
            showToast('Product added to cart successfully!', 'success');
        });
    });
}

/**
 * Initialize wishlist functionality
 */
function initWishlist() {
    const wishlistBtns = document.querySelectorAll('.add-to-wishlist, .wishlist-btn');
    
    wishlistBtns.forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            const productId = this.getAttribute('data-id');
            
            // Here you would typically make an AJAX request to add the item to wishlist
            console.log(`Adding product ${productId} to wishlist`);
            
            // Show success message
            showToast('Product added to wishlist successfully!', 'success');
        });
    });
}

/**
 * Show toast notification
 */
function showToast(message, type = 'info') {
    // Create toast container if it doesn't exist
    let toastContainer = document.querySelector('.toast-container');
    if (!toastContainer) {
        toastContainer = document.createElement('div');
        toastContainer.className = 'toast-container position-fixed bottom-0 end-0 p-3';
        document.body.appendChild(toastContainer);
    }
    
    // Create toast element
    const toastEl = document.createElement('div');
    toastEl.className = `toast align-items-center text-white bg-${type} border-0`;
    toastEl.setAttribute('role', 'alert');
    toastEl.setAttribute('aria-live', 'assertive');
    toastEl.setAttribute('aria-atomic', 'true');
    
    // Toast content
    toastEl.innerHTML = `
        <div class="d-flex">
            <div class="toast-body">
                ${message}
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    `;
    
    // Add toast to container
    toastContainer.appendChild(toastEl);
    
    // Initialize Bootstrap toast
    const toast = new bootstrap.Toast(toastEl);
    toast.show();
    
    // Remove toast after it's hidden
    toastEl.addEventListener('hidden.bs.toast', function() {
        toastEl.remove();
    });
}
