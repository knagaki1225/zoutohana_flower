// ▼▼▼ 要素を取得 ▼▼▼
const menuOpen = document.getElementById('menuOpen');   // ハンバーガーメニューのアイコン
const menuClose = document.getElementById('menuClose'); // ハンバーガーメニューの閉じるボタン
const overlayMenu = document.getElementById('overlayMenu'); // ハンバーガーメニュー本体
const contactLink = document.getElementById('contact-link');

const gearButton = document.querySelector('.settings-icon'); // 歯車アイコン
const settingsModal = document.getElementById('settingsModal'); // 設定モーダル本体

// ▼▼▼ ハンバーガーメニューの動作（既存のまま） ▼▼▼
menuOpen.addEventListener('click', () => {
    overlayMenu.classList.add('active');
});

menuClose.addEventListener('click', () => {
    overlayMenu.classList.remove('active');
});

contactLink.addEventListener('click', () => {
    overlayMenu.classList.remove('active');
});