async function initCartPage() {
    requireAuth();
    await loadCart();
}

async function loadCart() {
    const box = document.getElementById("cart-box");
    const errorBox = document.getElementById("error-box");
    errorBox.innerHTML = "";

    try {
        const items = await apiFetch("/api/cart");
        renderCart(items);
    } catch (err) {
        showAlert(errorBox, err.message);
    }
}

function renderCart(items) {
    const box = document.getElementById("cart-box");
    const summaryBox = document.getElementById("cart-summary");

    if (items.length === 0) {
        box.innerHTML = `<div class="empty-state">Gio hang cua ban dang trong. <a href="index.html" style="color:var(--primary)">Tiep tuc mua sam</a></div>`;
        summaryBox.innerHTML = "";
        return;
    }

    box.innerHTML = items.map(item => `
        <div class="cart-item">
            <img src="${item.productImage || 'https://placehold.co/100x100?text=No+Image'}" alt="${item.productName}">
            <div class="info">
                <div style="font-weight:600">${item.productName}</div>
                <div style="color:var(--muted); font-size:13px">${formatCurrency(item.price)} / san pham</div>
            </div>
            <div class="qty-control">
                <button onclick="changeQuantity(${item.productId}, ${item.quantity - 1})">-</button>
                <span>${item.quantity}</span>
                <button onclick="changeQuantity(${item.productId}, ${item.quantity + 1})">+</button>
            </div>
            <div style="width:120px; text-align:right; font-weight:700">${formatCurrency(item.subtotal)}</div>
            <button class="btn btn-danger" onclick="removeFromCart(${item.productId})">Xoa</button>
        </div>
    `).join("");

    const total = items.reduce((sum, i) => sum + i.subtotal, 0);
    summaryBox.innerHTML = `
        <div class="cart-summary">
            <span>Tong cong</span>
            <span>${formatCurrency(total)}</span>
        </div>
        <a href="checkout.html" class="btn btn-primary btn-block">Tien hanh thanh toan</a>
    `;
}

async function changeQuantity(productId, newQuantity) {
    const errorBox = document.getElementById("error-box");
    try {
        await apiFetch(`/api/cart/${productId}?quantity=${newQuantity}`, { method: "PUT" });
        await loadCart();
        await refreshCartBadge();
    } catch (err) {
        showAlert(errorBox, err.message);
    }
}

async function removeFromCart(productId) {
    const errorBox = document.getElementById("error-box");
    try {
        await apiFetch(`/api/cart/${productId}`, { method: "DELETE" });
        await loadCart();
        await refreshCartBadge();
    } catch (err) {
        showAlert(errorBox, err.message);
    }
}
