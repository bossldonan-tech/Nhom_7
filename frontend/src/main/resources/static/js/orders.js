async function initOrdersPage() {
    requireAuth();
    const params = new URLSearchParams(window.location.search);
    const highlight = params.get("highlight");
    await loadOrders(highlight ? Number(highlight) : null);
}

async function loadOrders(highlightId) {
    const box = document.getElementById("orders-box");
    const errorBox = document.getElementById("error-box");
    errorBox.innerHTML = "";
    box.innerHTML = `<div class="empty-state">Dang tai don hang...</div>`;

    try {
        const orders = await apiFetch("/api/orders");
        renderOrders(orders, highlightId);
    } catch (err) {
        showAlert(errorBox, err.message);
        box.innerHTML = "";
    }
}

function renderOrders(orders, highlightId) {
    const box = document.getElementById("orders-box");

    if (orders.length === 0) {
        box.innerHTML = `<div class="empty-state">Ban chua co don hang nao. <a href="index.html" style="color:var(--primary)">Mua sam ngay</a></div>`;
        return;
    }

    box.innerHTML = orders.map(order => `
        <div class="card order-card" style="${order.id === highlightId ? 'border:2px solid var(--primary)' : ''}">
            <div class="order-header">
                <div>
                    <strong>Ma don: ${order.orderCode}</strong>
                    <span style="color:var(--muted); font-size:13px; margin-left:10px">${formatDate(order.createdAt)}</span>
                </div>
                <span class="status-pill status-${order.status}">${translateStatus(order.status)}</span>
            </div>
            <table>
                <thead>
                    <tr><th>San pham</th><th>Don gia</th><th>SL</th><th>Thanh tien</th></tr>
                </thead>
                <tbody>
                    ${order.items.map(i => `
                        <tr>
                            <td>${i.productName}</td>
                            <td>${formatCurrency(i.price)}</td>
                            <td>${i.quantity}</td>
                            <td>${formatCurrency(i.subtotal)}</td>
                        </tr>
                    `).join("")}
                </tbody>
            </table>
            <div class="cart-summary">
                <span>Giao toi: ${order.shippingName} - ${order.shippingPhone} - ${order.shippingAddress}</span>
                <span>${formatCurrency(order.totalAmount)}</span>
            </div>
        </div>
    `).join("");
}

function translateStatus(status) {
    const map = {
        PENDING: "Cho xu ly",
        PAID: "Da thanh toan",
        SHIPPING: "Dang giao",
        COMPLETED: "Hoan tat",
        CANCELLED: "Da huy",
    };
    return map[status] || status;
}
