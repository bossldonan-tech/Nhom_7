let currentPage = 0;
const pageSize = 12;

async function initProductsPage() {
    await loadCategories();
    await loadProducts();

    document.getElementById("search-input").addEventListener("input", debounce(() => {
        currentPage = 0;
        loadProducts();
    }, 400));

    document.getElementById("category-select").addEventListener("change", () => {
        currentPage = 0;
        loadProducts();
    });
}

function debounce(fn, delay) {
    let timer;
    return (...args) => {
        clearTimeout(timer);
        timer = setTimeout(() => fn(...args), delay);
    };
}

async function loadCategories() {
    const select = document.getElementById("category-select");
    try {
        const categories = await apiFetch("/api/categories");
        categories.forEach(c => {
            const opt = document.createElement("option");
            opt.value = c.id;
            opt.textContent = c.name;
            select.appendChild(opt);
        });
    } catch (err) {
        console.error(err);
    }
}

async function loadProducts() {
    const grid = document.getElementById("product-grid");
    const errorBox = document.getElementById("error-box");
    errorBox.innerHTML = "";
    grid.innerHTML = `<div class="empty-state">Dang tai san pham...</div>`;

    const keyword = document.getElementById("search-input").value.trim();
    const categoryId = document.getElementById("category-select").value;

    const params = new URLSearchParams();
    if (keyword) params.set("keyword", keyword);
    if (categoryId) params.set("categoryId", categoryId);
    params.set("page", currentPage);
    params.set("size", pageSize);

    try {
        const result = await apiFetch("/api/products?" + params.toString());
        renderProducts(result.content || []);
        renderPagination(result);
    } catch (err) {
        showAlert(errorBox, err.message);
        grid.innerHTML = "";
    }
}

function renderProducts(products) {
    const grid = document.getElementById("product-grid");
    if (products.length === 0) {
        grid.innerHTML = `<div class="empty-state">Khong tim thay san pham nao.</div>`;
        return;
    }

    grid.innerHTML = products.map(p => `
        <div class="product-card">
            <img src="${p.imageUrl || 'https://placehold.co/400x300?text=No+Image'}" alt="${p.name}">
            <div class="body">
                <div class="brand">${p.brand || ""}</div>
                <div class="name">${p.name}</div>
                <div class="price">${formatCurrency(p.price)}</div>
                <div class="stock">Ton kho: ${p.stockQuantity}</div>
                <button class="btn btn-primary btn-block" ${p.stockQuantity <= 0 ? "disabled" : ""}
                    onclick="addToCart(${p.id})">
                    ${p.stockQuantity <= 0 ? "Het hang" : "Them vao gio"}
                </button>
            </div>
        </div>
    `).join("");
}

function renderPagination(result) {
    const box = document.getElementById("pagination");
    if (!box) return;
    const totalPages = result.totalPages || 1;
    if (totalPages <= 1) {
        box.innerHTML = "";
        return;
    }
    let html = "";
    for (let i = 0; i < totalPages; i++) {
        html += `<button class="btn ${i === currentPage ? "btn-primary" : "btn-outline"}" style="margin-right:6px" onclick="goToPage(${i})">${i + 1}</button>`;
    }
    box.innerHTML = html;
}

function goToPage(page) {
    currentPage = page;
    loadProducts();
    window.scrollTo({ top: 0, behavior: "smooth" });
}

async function addToCart(productId) {
    if (!isLoggedIn()) {
        window.location.href = "login.html";
        return;
    }
    const errorBox = document.getElementById("error-box");
    try {
        await apiFetch("/api/cart", {
            method: "POST",
            body: JSON.stringify({ productId, quantity: 1 }),
        });
        await refreshCartBadge();
        errorBox.innerHTML = `<div class="alert alert-success">Da them vao gio hang!</div>`;
        setTimeout(() => (errorBox.innerHTML = ""), 2000);
    } catch (err) {
        showAlert(errorBox, err.message);
    }
}
