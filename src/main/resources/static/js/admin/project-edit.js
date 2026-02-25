// ========== タブ切り替え ==========
document.addEventListener("DOMContentLoaded", () => {
const tabItems = document.querySelectorAll(".tab-item");

tabItems.forEach((tabItem) => {
  tabItem.addEventListener("click", () => {
    tabItems.forEach((t) => {
      t.classList.remove("active");
    });

    const tabPanels = document.querySelectorAll(".tab-panel");
    tabPanels.forEach((tabPanel) => {
      tabPanel.classList.remove("active");
    });

    tabItem.classList.add("active");

    const tabIndex = Array.from(tabItems).indexOf(tabItem);
    tabPanels[tabIndex].classList.add("active");
  });
});
});
