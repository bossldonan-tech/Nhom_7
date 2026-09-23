async function initCheckoutPage() {
    requireAuth();
    await loadSummary();

    const user = getUser();
    if (user) {
        document.getElementById("shippingName").value = user.fullName || "";
        document.getElementById("shippingPhone").value = user.phone || "";
        document.getElementById("shippingAddress").value = user.address || "";
    }

    document.getElementById("checkout-form").addEventListener("submit", handleCheckout);
}

async function loadSummary() {
    const box = document.getElementById("order-summary");
    try {
        const items = await apiFetch("/api/cart");
        if (items.length === 0) {
            window.location.href = "cart.html";
            return;
        }
        const total = items.reduce((sum, i) => sum + i.subtotal, 0);
        box.innerHTML = items.map(i => `
            <div style="display:flex; justify-content:space-between; padding:6px 0">
                <span>${i.productName} x${i.quantity}</span>
                <span>${formatCurrency(i.subtotal)}</span>
            </div>
        `).join("") + `
            <div class="cart-summary" style="border-top:1px solid var(--border); margin-top:8px; padding-top:12px">
                <span>Tong cong</span>
                <span>${formatCurrency(total)}</span>
            </div>
        `;
    } catch (err) {
        showAlert(document.getElementById("error-box"), err.message);
    }
}

async function handleCheckout(event) {
    event.preventDefault();
    const errorBox = document.getElementById("error-box");
    errorBox.innerHTML = "";

    const payload = {
        shippingName: document.getElementById("shippingName").value.trim(),
        shippingPhone: document.getElementById("shippingPhone").value.trim(),
        shippingAddress: document.getElementById("shippingAddress").value.trim(),
        paymentMethod: document.getElementById("paymentMethod").value,
    };

    const submitBtn = document.getElementById("submit-btn");
    submitBtn.disabled = true;
    submitBtn.textContent = "Dang xu ly...";

    try {
        const order = await apiFetch("/api/orders/checkout", {
            method: "POST",
            body: JSON.stringify(payload),
        });
        window.location.href = `orders.html?highlight=${order.id}`;
    } catch (err) {
        showAlert(errorBox, err.message);
        submitBtn.disabled = false;
        submitBtn.textContent = "Dat hang";
    }
}
