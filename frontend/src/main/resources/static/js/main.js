function renderHeader(activePage) {
    const header = document.getElementById("site-header");
    if (!header) return;

    const loggedIn = isLoggedIn();
    const user = getUser();
    const admin = isAdmin();

    const links = [
        { href: "index.html", label: "Trang chu", key: "home" },
        { href: "orders.html", label: "Don hang", key: "orders", auth: true },
    ];

    let linksHtml = links
        .filter(l => !l.auth || loggedIn)
        .map(l => `<a href="${l.href}" class="${activePage === l.key ? "active" : ""}">${l.label}</a>`)
        .join("");

    if (admin) {
        linksHtml += `<a href="admin.html" class="${activePage === "admin" ? "active" : ""}">Quan tri</a>`;
    }

    linksHtml += `<a href="cart.html" class="${activePage === "cart" ? "active" : ""}">Gio hang <span id="cart-badge" class="badge" style="display:none">0</span></a>`;

    if (loggedIn) {
        linksHtml += `<a href="notifications.html" class="${activePage === "notifications" ? "active" : ""}">Thong bao</a>`;
        linksHtml += `<span>Xin chao, <strong>${user ? user.fullName : ""}</strong></span>`;
        linksHtml += `<button onclick="logout()">Dang xuat</button>`;
    } else {
        linksHtml += `<a href="login.html" class="${activePage === "login" ? "active" : ""}">Dang nhap</a>`;
        linksHtml += `<a href="register.html" class="${activePage === "register" ? "active" : ""}">Dang ky</a>`;
    }

    header.innerHTML = `
        <div class="container nav-bar">
            <a href="index.html" class="brand">🛒 PTPMHDV Shop</a>
            <nav class="nav-links">${linksHtml}</nav>
        </div>
    `;

    if (loggedIn) {
        refreshCartBadge();
    }
}

async function refreshCartBadge() {
    try {
        const items = await apiFetch("/api/cart");
        const badge = document.getElementById("cart-badge");
        if (!badge) return;
        const total = items.reduce((sum, i) => sum + i.quantity, 0);
        if (total > 0) {
            badge.style.display = "inline-block";
            badge.textContent = total;
        }
    } catch (e) {
        // Bo qua loi khi chua dang nhap hoac cac service chua san sang
    }
}

function requireAuth() {
    if (!isLoggedIn()) {
        window.location.href = "login.html";
    }
}

function requireAdmin() {
    if (!isLoggedIn() || !isAdmin()) {
        window.location.href = "index.html";
    }
}
