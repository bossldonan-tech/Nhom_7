async function initNotificationsPage() {
    requireAuth();
    await loadNotifications();
}

async function loadNotifications() {
    const box = document.getElementById("notif-box");
    const errorBox = document.getElementById("error-box");
    errorBox.innerHTML = "";

    try {
        const notifications = await apiFetch("/api/notifications");
        renderNotifications(notifications);
    } catch (err) {
        showAlert(errorBox, err.message);
    }
}

function renderNotifications(notifications) {
    const box = document.getElementById("notif-box");
    if (notifications.length === 0) {
        box.innerHTML = `<div class="empty-state">Ban chua co thong bao nao.</div>`;
        return;
    }

    box.innerHTML = notifications.map(n => `
        <div class="notif-item ${n.read ? "" : "unread"}">
            <div style="display:flex; justify-content:space-between">
                <strong>${n.title}</strong>
                <span style="color:var(--muted); font-size:12px">${formatDate(n.createdAt)}</span>
            </div>
            <div style="margin-top:4px; font-size:14px">${n.message}</div>
            ${n.read ? "" : `<button class="btn btn-outline" style="margin-top:8px" onclick="markRead(${n.id})">Danh dau da doc</button>`}
        </div>
    `).join("");
}

async function markRead(id) {
    try {
        await apiFetch(`/api/notifications/${id}/read`, { method: "PUT" });
        await loadNotifications();
    } catch (err) {
        showAlert(document.getElementById("error-box"), err.message);
    }
}
