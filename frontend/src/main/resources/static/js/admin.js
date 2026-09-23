let editingProductId = null;
let allCategories = [];

async function initAdminPage() {
    requireAdmin();
    await loadAdminCategories();
    await loadAdminProducts();
    switchTab("products");

    document.getElementById("product-form").addEventListener("submit", handleProductSubmit);
    document.getElementById("category-form").addEventListener("submit", handleCategorySubmit);
}

function switchTab(tab) {
    document.querySelectorAll(".tabs button").forEach(b => b.classList.remove("active"));
    document.getElementById("tab-" + tab).classList.add("active");
    document.querySelectorAll(".tab-panel").forEach(p => (p.style.display = "none"));
    document.getElementById("panel-" + tab).style.display = "block";

    if (tab === "orders") {
        loadAdminOrders();
    }
}

// ---- Categories ----

async function loadAdminCategories() {
    try {
        allCategories = await apiFetch("/api/categories");
        renderCategoryTable();
        renderCategoryOptions();
    } catch (err) {
        showAlert(document.getElementById("error-box"), err.message);
    }
}

function renderCategoryOptions() {
    const select = document.getElementById("product-category");
    select.innerHTML = `<option value="">-- Khong danh muc --</option>` +
        allCategories.map(c => `<option value="${c.id}">${c.name}</option>`).join("");
}

function renderCategoryTable() {
    const box = document.getElementById("category-table");
    box.innerHTML = `
        <table>
            <thead><tr><th>Ten</th><th>Mo ta</th><th></th></tr></thead>
            <tbody>
                ${allCategories.map(c => `
                    <tr>
                        <td>${c.name}</td>
                        <td>${c.description || ""}</td>
                        <td><button class="btn btn-danger" onclick="deleteCategory(${c.id})">Xoa</button></td>
                    </tr>
                `).join("")}
            </tbody>
        </table>
    `;
}

async function handleCategorySubmit(event) {
    event.preventDefault();
    const errorBox = document.getElementById("error-box");
    try {
        await apiFetch("/api/categories", {
            method: "POST",
            body: JSON.stringify({
                name: document.getElementById("category-name").value.trim(),
                description: document.getElementById("category-description").value.trim(),
            }),
        });
        document.getElementById("category-form").reset();
        await loadAdminCategories();
    } catch (err) {
        showAlert(errorBox, err.message);
    }
}

async function deleteCategory(id) {
    if (!confirm("Xoa danh muc nay?")) return;
    try {
        await apiFetch(`/api/categories/${id}`, { method: "DELETE" });
        await loadAdminCategories();
    } catch (err) {
        showAlert(document.getElementById("error-box"), err.message);
    }
}

// ---- Products ----

async function loadAdminProducts() {
    try {
        const result = await apiFetch("/api/products?size=100");
        renderProductTable(result.content || []);
    } catch (err) {
        showAlert(document.getElementById("error-box"), err.message);
    }
}

function renderProductTable(products) {
    const box = document.getElementById("product-table");
    box.innerHTML = `
        <table>
            <thead><tr><th>Ten</th><th>Danh muc</th><th>Gia</th><th>Ton kho</th><th></th></tr></thead>
            <tbody>
                ${products.map(p => `
                    <tr>
                        <td>${p.name}</td>
                        <td>${p.categoryName || ""}</td>
                        <td>${formatCurrency(p.price)}</td>
                        <td>${p.stockQuantity}</td>
                        <td>
                            <button class="btn btn-outline" onclick='editProduct(${JSON.stringify(p)})'>Sua</button>
                            <button class="btn btn-danger" onclick="deleteProduct(${p.id})">Xoa</button>
                        </td>
                    </tr>
                `).join("")}
            </tbody>
        </table>
    `;
}

function editProduct(product) {
    editingProductId = product.id;
    document.getElementById("product-form-title").textContent = "Sua san pham: " + product.name;
    document.getElementById("product-name").value = product.name;
    document.getElementById("product-description").value = product.description || "";
    document.getElementById("product-price").value = product.price;
    document.getElementById("product-stock").value = product.stockQuantity;
    document.getElementById("product-image").value = product.imageUrl || "";
    document.getElementById("product-brand").value = product.brand || "";
    document.getElementById("product-category").value = product.categoryId || "";
    window.scrollTo({ top: 0, behavior: "smooth" });
}

function resetProductForm() {
    editingProductId = null;
    document.getElementById("product-form-title").textContent = "Them san pham moi";
    document.getElementById("product-form").reset();
}

async function handleProductSubmit(event) {
    event.preventDefault();
    const errorBox = document.getElementById("error-box");

    const payload = {
        name: document.getElementById("product-name").value.trim(),
        description: document.getElementById("product-description").value.trim(),
        price: parseFloat(document.getElementById("product-price").value),
        stockQuantity: parseInt(document.getElementById("product-stock").value, 10),
        imageUrl: document.getElementById("product-image").value.trim(),
        brand: document.getElementById("product-brand").value.trim(),
        categoryId: document.getElementById("product-category").value || null,
    };

    try {
        if (editingProductId) {
            await apiFetch(`/api/products/${editingProductId}`, {
                method: "PUT",
                body: JSON.stringify(payload),
            });
        } else {
            await apiFetch("/api/products", {
                method: "POST",
                body: JSON.stringify(payload),
            });
        }
        resetProductForm();
        await loadAdminProducts();
    } catch (err) {
        showAlert(errorBox, err.message);
    }
}

async function deleteProduct(id) {
    if (!confirm("Xoa san pham nay?")) return;
    try {
        await apiFetch(`/api/products/${id}`, { method: "DELETE" });
        await loadAdminProducts();
    } catch (err) {
        showAlert(document.getElementById("error-box"), err.message);
    }
}

// ---- Orders ----

async function loadAdminOrders() {
    const box = document.getElementById("admin-orders-box");
    try {
        const orders = await apiFetch("/api/orders/admin/all");
        renderAdminOrders(orders);
    } catch (err) {
        showAlert(document.getElementById("error-box"), err.message);
    }
}

function renderAdminOrders(orders) {
    const box = document.getElementById("admin-orders-box");
    if (orders.length === 0) {
        box.innerHTML = `<div class="empty-state">Chua co don hang nao.</div>`;
        return;
    }
    const statuses = ["PENDING", "PAID", "SHIPPING", "COMPLETED", "CANCELLED"];
    box.innerHTML = orders.map(order => `
        <div class="card order-card">
            <div class="order-header">
                <div>
                    <strong>${order.orderCode}</strong>
                    <span style="color:var(--muted); font-size:13px; margin-left:10px">${formatDate(order.createdAt)}</span>
                </div>
                <select onchange="updateOrderStatus(${order.id}, this.value)">
                    ${statuses.map(s => `<option value="${s}" ${s === order.status ? "selected" : ""}>${s}</option>`).join("")}
                </select>
            </div>
            <div>Nguoi nhan: ${order.shippingName} - ${order.shippingPhone} - ${order.shippingAddress}</div>
            <div style="font-weight:700; margin-top:6px">${formatCurrency(order.totalAmount)}</div>
        </div>
    `).join("");
}

async function updateOrderStatus(orderId, status) {
    try {
        await apiFetch(`/api/orders/admin/${orderId}/status?status=${status}`, { method: "PUT" });
        await loadAdminOrders();
    } catch (err) {
        showAlert(document.getElementById("error-box"), err.message);
    }
}
