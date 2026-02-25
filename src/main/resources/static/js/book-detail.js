// 画面の読み込みが終わってから実行する
document.addEventListener('DOMContentLoaded', () => {

    // --- 1. 要素の取得 ---
    const gearButton = document.querySelector('.settings-icon');
    const settingsModal = document.getElementById('settingsModal');
    const openDeleteModal = document.getElementById('openDeleteModal');
    const deleteModal = document.getElementById('deleteModal');
    const deleteCancel = document.getElementById('deleteCancel');

    // --- 2. 歯車ボタンの動作 ---
    if (gearButton && settingsModal) {
        gearButton.addEventListener('click', (e) => {
            settingsModal.classList.toggle('active');
        });

        // 設定モーダルの背景をクリックしたら閉じる
        settingsModal.addEventListener('click', (e) => {
            if (e.target === settingsModal) {
                settingsModal.classList.remove('active');
            }
        });
    }

    // --- 3. 削除モーダルの動作 ---
    if (openDeleteModal && deleteModal && settingsModal) {
        // 削除モーダルを開く
        openDeleteModal.addEventListener('click', (e) => {
            e.preventDefault();
            deleteModal.classList.add('active');
            settingsModal.classList.remove('active'); // 設定モーダルを閉じる
        });
    }

    if (deleteCancel && deleteModal) {
        // 「もどる」ボタン
        deleteCancel.addEventListener('click', () => {
            deleteModal.classList.remove('active');
        });

        // 削除モーダルの背景クリックで閉じる
        deleteModal.addEventListener('click', (e) => {
            if (e.target === deleteModal) {
                deleteModal.classList.remove('active');
            }
        });
    }
});